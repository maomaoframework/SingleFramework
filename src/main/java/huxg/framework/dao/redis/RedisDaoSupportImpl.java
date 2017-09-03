package huxg.framework.dao.redis;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import redis.clients.jedis.Jedis;

import huxg.framework.PageInfo;
import huxg.framework.dao.BaseDaoImpl;
import huxg.framework.dao.redis.JedisTemplate.JedisAction;
import huxg.framework.dao.redis.JedisTemplate.JedisActionNoResult;
import com.thoughtworks.xstream.XStream;

public class RedisDaoSupportImpl<T, ID extends Serializable> extends BaseDaoImpl<T, ID> {
	public static final String POOL = "objpool/";

	@Resource(name = "jedisTemplate")
	protected JedisTemplate jedisTemplate;

	protected XStream x = new XStream();

	/**
	 * JPA 根据主键查找一个实体
	 */
	public T getFromDB(final ID id) {
		return super.get(id);
	}

	/**
	 * JPA 根据主键查找一个实体
	 */
	@Override
	public T get(final ID id) {
		if (id != null) {
			T t = jedisTemplate.execute(new JedisAction<T>() {
				@Override
				public T action(Jedis jedis) {
					String xml = jedis.hget(POOL + entityClass.getSimpleName(), id.toString());
					T t = null;
					if (StringUtils.isEmpty(xml)) {
						t = entityManager.find(entityClass, id);

						if (t != null) {
							jedis.hset(POOL + entityClass.getSimpleName(), id.toString(), x.toXML(t));
						}
					} else {
						t = (T) x.fromXML(xml);
					}
					return t;
				}
			});
			return t;
		}
		return null;
	}

	/**
	 * JPA 通过主键查找
	 */
	@Override
	public T get(final ID id, final LockModeType lockModeType) {
		if (id != null) {
			T t = jedisTemplate.execute(new JedisAction<T>() {
				@Override
				public T action(Jedis jedis) {
					String xml = jedis.hget(POOL + entityClass.getSimpleName(), id.toString());
					T t = null;
					if (StringUtils.isEmpty(xml)) {
						if (lockModeType != null) {
							t = entityManager.find(entityClass, id, lockModeType);
						} else {
							t = entityManager.find(entityClass, id);
						}

						if (t != null) {
							jedis.hset(POOL + entityClass.getSimpleName(), id.toString(), x.toXML(t));
						}
					} else {
						t = (T) x.fromXML(xml);
					}
					return t;
				}
			});
			return t;
		}
		return null;
	}

	/**
	 * JPA 查找对象列表
	 */
	@SuppressWarnings("unchecked")
	public List findListFromDB(String jpql) {
		return super.findList(jpql);
	}

	/**
	 * JPA 查找对象列表
	 */
	@SuppressWarnings("unchecked")
	public List findListFromDB(String jpql, Object[] params) {
		return super.findList(jpql, params);
	}

	/**
	 * JPA 查找对象列表
	 */
	@SuppressWarnings("unchecked")
	public List<T> findList(String jpql) {
		Query query = entityManager.createQuery(jpql);
		final List<String> keys = query.getResultList();

		final List<T> result = new ArrayList<T>();
		jedisTemplate.execute(new JedisAction<List<T>>() {
			@Override
			public List<T> action(Jedis jedis) {
				String xml;
				T o = null;
				for (String key : keys) {
					xml = jedis.hget(POOL + entityClass.getSimpleName(), key);
					if (StringUtils.isEmpty(xml) || "<null/>".equals(xml)) {
						o = RedisDaoSupportImpl.super.get((ID) key);
					} else {
						o = (T) x.fromXML(xml);
					}
					result.add(o);
				}
				return result;
			}
		});
		return result;
	}

	/**
	 * jfinal 根据条件查询
	 * 
	 * @param clazz
	 * @param ql
	 * @param params
	 * @return
	 */
	public List<T> findList(String jpql, Object[] params) {
		if (!jpql.contains("?") && (params == null || params.length == 0)) {
			return findList(jpql);
		} else {
			int count = 0;
			StringBuffer qlwithParams = new StringBuffer(jpql.length());
			char c;
			for (int i = 0; i < jpql.length(); i++) {
				c = jpql.charAt(i);
				if (jpql.charAt(i) == '?') {
					qlwithParams.append("?" + i);
					count++;
				} else
					qlwithParams.append(c);
			}

			Query query = entityManager.createQuery(jpql);

			int idx = 1;
			for (Object o : params) {
				query.setParameter(idx++, o);
			}

			final List<String> keys = query.getResultList();

			final List<T> result = new ArrayList<T>();
			jedisTemplate.execute(new JedisAction<List<T>>() {
				@Override
				public List<T> action(Jedis jedis) {
					String xml;
					T o = null;
					for (String key : keys) {
						xml = jedis.hget(POOL + entityClass.getSimpleName(), key);
						if (StringUtils.isEmpty(xml) || "<null/>".equals(xml)) {
							o = RedisDaoSupportImpl.super.get((ID) key);

							if (o != null) {
								xml = x.toXML(o);
								jedis.hset(POOL + entityClass.getSimpleName(), key, xml);
							}
						} else {
							o = (T) x.fromXML(xml);
						}
						result.add(o);
					}
					return result;
				}
			});
			return result;
		}

	}

	/**
	 * 根据条件，并发iyongfenye查询
	 * 
	 * @param jpql
	 * @param params
	 * @return
	 */
	public List<T> findList(final String jpql, final Object[] params, final int firstResult, final int maxResults) {
		int count = 0;
		StringBuffer qlwithParams = new StringBuffer(jpql.length());
		char c;
		for (int i = 0; i < jpql.length(); i++) {
			c = jpql.charAt(i);
			if (jpql.charAt(i) == '?') {
				qlwithParams.append("?" + i);
				count++;
			} else
				qlwithParams.append(c);
		}

		Query query = entityManager.createQuery(jpql);
		if (params != null) {
			int idx = 1;
			for (Object o : params) {
				query.setParameter(idx++, o);
			}
		}

		if (firstResult > 0)
			query.setFirstResult(firstResult);
		if (maxResults > 0)
			query.setMaxResults(maxResults);
		final List<String> keys = query.getResultList();
		final List<T> result = new ArrayList<T>();
		jedisTemplate.execute(new JedisAction<List<T>>() {
			@Override
			public List<T> action(Jedis jedis) {
				String xml;
				T o = null;
				for (String key : keys) {
					xml = jedis.hget(POOL + entityClass.getSimpleName(), key);
					if (StringUtils.isEmpty(xml) || "<null/>".equals(xml)) {
						o = RedisDaoSupportImpl.super.get((ID) key);
						if (o != null) {
							xml = x.toXML(o);
							jedis.hset(POOL + entityClass.getSimpleName(), key, xml);
						}
					} else {
						o = (T) x.fromXML(xml);
					}
					result.add(o);
				}
				return result;
			}
		});

		return result;
	}

	/**
	 * 通过JPQL进行查询，可传递参数，可传递count语句，可传递分页信息
	 * 
	 * @param ql
	 * @param countQl
	 * @param params
	 * @param pageInfo
	 * @return
	 */
	public List<T> findList(final String jpql, final String countQl, final Object[] params, PageInfo pageInfo) {
		if (pageInfo == null && countQl != null) {
			List<T> result = findList(jpql, params, 0, -1);
			return result;
		} else if (pageInfo == null && countQl == null) {
			List<T> result = this.findList(jpql, params, 0, -1);
			return result;
		} else if (pageInfo != null && countQl == null) {
			List<T> result = findList(jpql, params, pageInfo.getFirstResult(), pageInfo.getMaxResults());
			return result;
		} else {
			List<T> result = findList(jpql, params, pageInfo.getFirstResult(), pageInfo.getMaxResults());

			int count = queryCount(countQl, params);
			pageInfo.setRowCount(count);
			pageInfo.setShowRows(result.size());

			// 判定是否已经到达尾页了
			if (count == 0) {
				pageInfo.setEnd(true);
			} else {
				// 求余
				int left = count % pageInfo.getPageSize();
				if (left == 0) {
					// 能被整除，则总页数应该等于
					int pageCount = count / pageInfo.getPageSize();

					// 如果总数大于当前页数，则表示后面还有页数，如果总页数数小于或者等于当前页数，则表示已经没有下一页了
					pageInfo.setEnd(pageCount > pageInfo.getCurrentPageIndex());
				} else {
					// 余数不为0，则总页数等于除数+1
					int pageCount = count / pageInfo.getPageSize() + 1;
					pageInfo.setEnd(pageCount > pageInfo.getCurrentPageIndex());
				}
			}

			return result;
		}
	}

	/**
	 * 通过JPQL执行一个查询语句，并返回结果数
	 * 
	 * @param hql
	 * @param params
	 * @return
	 */
	public int queryCount(String jpql, Object[] params) {
		List<?> r = this.findCount(jpql, params, 0, -1);
		if (r == null || r.isEmpty())
			return 0;

		try {
			return Integer.parseInt(r.get(0).toString());
		} catch (Exception e) {
			return 0;
		}
	}

	public List<?> findCount(final String jpql, final Object[] params, final int firstResult, final int maxResults) {
		int count = 0;
		StringBuffer qlwithParams = new StringBuffer(jpql.length());
		char c;
		for (int i = 0; i < jpql.length(); i++) {
			c = jpql.charAt(i);
			if (jpql.charAt(i) == '?') {
				qlwithParams.append("?" + i);
				count++;
			} else
				qlwithParams.append(c);
		}

		Query query = entityManager.createQuery(jpql);

		if (params != null) {
			int idx = 1;
			for (Object o : params) {
				query.setParameter(idx++, o);
			}
		}

		if (firstResult > 0)
			query.setFirstResult(firstResult);
		if (maxResults > 0)
			query.setMaxResults(maxResults);
		return query.getResultList();
	}

	public void persist(T entity) {
		Assert.notNull(entity);
		entityManager.persist(entity);

		final ID id = getIdentifier(entity);
		final T e2 = entity;
		jedisTemplate.execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis) {
				String xml = x.toXML(e2);
				jedis.hset(POOL + entityClass.getSimpleName(), id.toString(), xml);
			}
		});
	}

	@Override
	public void save(T entity) {
		persist(entity);
	}

	@Override
	public T update(T entity) {
		return merge(entity);
	}

	public T merge(T entity) {
		Assert.notNull(entity);
		T t = entityManager.merge(entity);

		final T t2 = t;
		final ID id = getIdentifier(entity);
		jedisTemplate.execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis) {
				String xml = x.toXML(t2);
				jedis.hset(POOL + entityClass.getSimpleName(), id.toString(), xml);
			}
		});

		return t;
	}

	public void remove(T entity) {
		if (entity != null) {
			final ID id = getIdentifier(entity);
			entityManager.remove(entity);
			jedisTemplate.execute(new JedisActionNoResult() {
				@Override
				public void action(Jedis jedis) {
					jedis.hdel(POOL + entityClass.getSimpleName(), id.toString());
				}
			});
		}
	}

	@Override
	public void delete(T entity) {
		if (entity != null) {
			remove(entity);
		}
	}

	@Override
	public void delete(final ID id) {
		T entity = super.get(id);
		entityManager.remove(entity);
		jedisTemplate.execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis) {
				jedis.hdel(POOL + entityClass.getSimpleName(), id.toString());
			}
		});
	}

	public void refresh(T entity) {
		if (entity != null) {
			entityManager.refresh(entity);
		}
	}

	public void refresh(T entity, LockModeType lockModeType) {
		if (entity != null) {
			if (lockModeType != null) {
				entityManager.refresh(entity, lockModeType);
			} else {
				entityManager.refresh(entity);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public ID getIdentifier(T entity) {
		Assert.notNull(entity);
		return (ID) entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
	}

	public boolean isManaged(T entity) {
		return entityManager.contains(entity);
	}

	public void detach(T entity) {
		entityManager.detach(entity);
	}

	public void lock(T entity, LockModeType lockModeType) {
		if (entity != null && lockModeType != null) {
			entityManager.lock(entity, lockModeType);
		}
	}

	public void clear() {
		entityManager.clear();
	}

	public void flush() {
		entityManager.flush();
	}

	@Override
	public String makeSelectQuery() {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
			entityClass = (Class<T>) parameterizedType[0];
			return "select id from " + entityClass.getSimpleName() + " " + " t";
		}
		return "";
	}
}
