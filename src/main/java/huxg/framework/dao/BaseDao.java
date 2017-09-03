/*
 * Copyright 2005-2020 GreenTube Team All rights reserved.
 * Support: Huxg
 * License: CND team license
 */
package huxg.framework.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.LockModeType;

import huxg.framework.PageInfo;

/**
 * Dao - 基类
 * 
 */
public interface BaseDao<T, ID extends Serializable> {

	/**
	 * 查找实体对象
	 * 
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	T get(ID id);

	/**
	 * 查找实体对象
	 * 
	 * @param id
	 *            ID
	 * @param lockModeType
	 *            锁定方式
	 * @return 实体对象，若不存在则返回null
	 */
	T get(ID id, LockModeType lockModeType);

	/**
	 * JPA 查找列表
	 * @param jpql
	 * @return
	 */
	List<T> findList(String jpql);
	
	List<T> findList(String jpql, Object[] params);
	
	List<T> findList(final String jpql, final Object[] params, final int firstResult, final int maxResults);
	
	List<T> findList(final String jpql, final String countQl, final Object[] params, PageInfo pageInfo);
	
	int queryCount(String jpql, Object[] params) ;
	
	List<?> findCount(final String jpql, final Object[] params, final int firstResult, final int maxResults) ;
	
	/**
	 * 持久化实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	void save(T entity);

	/**
	 * 合并实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	T update(T entity);

	/**
	 * 移除实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	void delete(T entity);
	void delete(ID id);

	/**
	 * 刷新实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	void refresh(T entity);

	/**
	 * 刷新实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @param lockModeType
	 *            锁定方式
	 */
	void refresh(T entity, LockModeType lockModeType);

	/**
	 * 获取实体对象ID
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象ID
	 */
	ID getIdentifier(T entity);

	/**
	 * 判断是否为托管状态
	 * 
	 * @param entity
	 *            实体对象
	 * @return 是否为托管状态
	 */
	boolean isManaged(T entity);

	/**
	 * 设置为游离状态
	 * 
	 * @param entity
	 *            实体对象
	 */
	void detach(T entity);

	/**
	 * 锁定实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @param lockModeType
	 *            锁定方式
	 */
	void lock(T entity, LockModeType lockModeType);

	/**
	 * 清除缓存
	 */
	void clear();

	/**
	 * 同步数据
	 */
	void flush();

	
	public String makeSelectQuery(); 
}