/*
 * Copyright 2005-2020 Top Team All rights reserved.
 * Support: 
 * License: top team license
 */
package huxg.framework.service;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import huxg.framework.PageInfo;
import huxg.framework.dao.BaseDao;
import huxg.framework.entity.BaseEntity;

/**
 * Service - 基类
 */
@Transactional
public class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {
	/** 实体类类型 */
	@SuppressWarnings("unused")
	private Class<T> entityClass;

	/** 更新忽略属性 */
	private static final String[] UPDATE_IGNORE_PROPERTIES = new String[] { BaseEntity.ID_PROPERTY_NAME };

	/** baseDao */
	protected BaseDao<T, ID> dao;

	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
			entityClass = (Class<T>) parameterizedType[0];
		}
	}

	public void setDao(BaseDao<T, ID> baseDao) {
		this.dao = baseDao;
	}

	public BaseDao<T, ID> getDao() {
		return this.dao;
	}

	@Transactional(readOnly = true)
	public T get(ID id) {
		return dao.get(id);
	}

	@Transactional(readOnly = true)
	public List<T> findAll() {
		return findList(null, null, null, null);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<T> findList(ID... ids) {
		List<T> result = new ArrayList<T>();
		if (ids != null) {
			for (ID id : ids) {
				T entity = get(id);
				if (entity != null) {
					result.add(entity);
				}
			}
		}
		return result;
	}

	@Transactional(readOnly = true)
	public List<T> findList(String jpql) {
		return dao.findList(jpql);
	}

	@Transactional(readOnly = true)
	public List<T> findList(String jpql, Object[] params) {
		return dao.findList(jpql, params);
	}

	@Transactional(readOnly = true)
	public List<T> findList(final String jpql, final Object[] params, final int firstResult, final int maxResults) {
		return dao.findList(jpql, params, firstResult, maxResults);
	}

	@Transactional(readOnly = true)
	public List<T> findList(final String jpql, final String countQl, final Object[] params, PageInfo pageInfo) {
		return dao.findList(jpql, countQl, params, pageInfo);
	}

	@Transactional(readOnly = true)
	public int queryCount(String jpql, Object[] params) {
		return dao.queryCount(jpql, params);
	}

	@Transactional
	public void save(T entity) {
		dao.save(entity);
	}

	@Transactional
	public T update(T entity) {
		return dao.update(entity);
	}

	@Transactional
	public T update(T entity, String... ignoreProperties) {
		Assert.notNull(entity);
		if (dao.isManaged(entity)) {
			throw new IllegalArgumentException("Entity must not be managed");
		}
		T persistant = dao.get(dao.getIdentifier(entity));
		if (persistant != null) {
			copyProperties(entity, persistant, (String[]) ArrayUtils.addAll(ignoreProperties, UPDATE_IGNORE_PROPERTIES));
			return update(persistant);
		} else {
			return update(entity);
		}
	}

	@Transactional
	public void delete(ID id) {
		delete(dao.get(id));
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void delete(ID... ids) {
		if (ids != null) {
			for (ID id : ids) {
				delete(dao.get(id));
			}
		}
	}

	@Transactional
	public void delete(T entity) {
		dao.delete(entity);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void copyProperties(Object source, Object target, String[] ignoreProperties) throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(target.getClass());
		List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null && (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {
				PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object sourceValue = readMethod.invoke(source);
						Object targetValue = readMethod.invoke(target);
						if (sourceValue != null && targetValue != null && targetValue instanceof Collection) {
							Collection collection = (Collection) targetValue;
							collection.clear();
							collection.addAll((Collection) sourceValue);
						} else {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, sourceValue);
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}

	public String makeSelectQuery() {
		return dao.makeSelectQuery();
	}

	public Object jsonToBean(String json, Class clazz) {
		return JSON.parseObject(json, clazz);
	}

	public String beanToJson(Object bean) {
		return JSON.toJSONString(bean);
	}

	public PageInfo initPageInfo(int pageNum) {
		int pageIndex = pageNum <= 0 ? 1 : pageNum;

		PageInfo pageInfo = new PageInfo();
		pageInfo.setCurrentPageIndex(pageIndex);
		pageInfo.setPageSize(20);
		return pageInfo;
	}
}