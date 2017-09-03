/*
 * Copyright 2005-2020 GreenTube Team All rights reserved.
 * Support: Huxg
 * License: CND team license
 */
package huxg.framework.service;

import java.io.Serializable;
import java.util.List;

import huxg.framework.PageInfo;

/**
 * Service - 基类
 */
public interface BaseService<T, ID extends Serializable> {

	/**
	 * 查找实体对象
	 * 
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	T get(ID id);

	/**
	 * 查找所有实体对象集合
	 * 
	 * @return 所有实体对象集合
	 */
	List<T> findAll();

	/**
	 * 查找实体对象集合
	 * 
	 * @param ids
	 *            ID
	 * @return 实体对象集合
	 */
	@SuppressWarnings("unchecked")
	List<T> findList(ID... ids);

	/**
	 * 保存实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	void save(T entity);

	/**
	 * 更新实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	T update(T entity);

	/**
	 * 更新实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @param ignoreProperties
	 *            忽略属性
	 * @return 实体对象
	 */
	T update(T entity, String... ignoreProperties);

	/**
	 * 删除实体对象
	 * 
	 * @param id
	 *            ID
	 */
	void delete(ID id);

	/**
	 * 删除实体对象
	 * 
	 * @param ids
	 *            ID
	 */
	@SuppressWarnings("unchecked")
	void delete(ID... ids);

	/**
	 * 删除实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	void delete(T entity);

	List<T> findList(String jpql);

	List<T> findList(String jpql, Object[] params);

	List<T> findList(final String jpql, final Object[] params, final int firstResult, final int maxResults);

	List<T> findList(final String jpql, final String countQl, final Object[] params, PageInfo pageInfo);

	int queryCount(String jpql, Object[] params);

	String makeSelectQuery();
}