/*
 * Copyright 2005-2020 GreenTube Team All rights reserved.
 * Support: Huxg
 * License: CND team license
 */
package huxg.framework.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.util.Assert;

import huxg.framework.PageInfo;

/**
 * Dao - 基类
 */
// @Rwable
public abstract class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {

	/** 实体类类型 */
	protected Class<T> entityClass;

	/** 别名数 */
	protected static volatile long aliasCount = 0;

	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
			entityClass = (Class<T>) parameterizedType[0];
		}
	}

	/**
	 * JPA 根据主键查找一个实体
	 */
	public T get(ID id) {
		if (id != null) {
			return entityManager.find(entityClass, id);
		}
		return null;
	}

	/**
	 * JPA 通过主键查找
	 */
	public T get(ID id, LockModeType lockModeType) {
		if (id != null) {
			if (lockModeType != null) {
				return entityManager.find(entityClass, id, lockModeType);
			} else {
				return entityManager.find(entityClass, id);
			}
		}
		return null;
	}

	public void save(T entity) {
		Assert.notNull(entity);
		entityManager.persist(entity);
	}

	public T update(T entity) {
		Assert.notNull(entity);
		return entityManager.merge(entity);
	}

	public void delete(T entity) {
		if (entity != null) {
			entityManager.remove(entity);
		}
	}

	public void delete(ID id) {
		T entity = get(id);
		entityManager.remove(entity);
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

	@SuppressWarnings("unchecked")
	@Override
	public String makeSelectQuery() {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
			entityClass = (Class<T>) parameterizedType[0];
			return "select t from " + entityClass.getSimpleName() + " " + " t";
		}
		return "";
	}

	/**
	 * JPA 查找对象列表
	 */
	@SuppressWarnings("unchecked")
	public List<T> findList(String jpql) {
		Query query = entityManager.createQuery(jpql);
		return query.getResultList();
	}

	/**
	 * jfinal 根据条件查询
	 * 
	 * @param clazz
	 * @param ql
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findList(String jpql, Object[] params) {
		if (!jpql.contains("?") && (params == null || params.length == 0)) {
			return findList(jpql);
		} else {
			StringBuffer qlwithParams = new StringBuffer(jpql.length());
			char c;
			for (int i = 0; i < jpql.length(); i++) {
				c = jpql.charAt(i);
				if (jpql.charAt(i) == '?') {
					qlwithParams.append("?" + i);
				} else
					qlwithParams.append(c);
			}

			Query query = entityManager.createQuery(jpql);

			int idx = 1;
			for (Object o : params) {
				query.setParameter(idx++, o);
			}

			List<T> result = query.getResultList();
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
	@SuppressWarnings("unchecked")
	public List<T> findList(final String jpql, final Object[] params, final int firstResult, final int maxResults) {
		StringBuffer qlwithParams = new StringBuffer(jpql.length());
		char c;
		for (int i = 0; i < jpql.length(); i++) {
			c = jpql.charAt(i);
			if (jpql.charAt(i) == '?') {
				qlwithParams.append("?" + i);
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
		List<T> result = query.getResultList();
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
		StringBuffer qlwithParams = new StringBuffer(jpql.length());
		char c;
		for (int i = 0; i < jpql.length(); i++) {
			c = jpql.charAt(i);
			if (jpql.charAt(i) == '?') {
				qlwithParams.append("?" + i);
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
}