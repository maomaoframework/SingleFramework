/*
 * Copyright 2005-2020 Top Team All rights reserved.
 * Support: 
 * License: top team license
 */
package huxg.framework.listener;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import huxg.framework.entity.BaseEntity;

/**
 * Listener - 创建日期、修改日期处理
 * 
 * @author Top Team（ ）
 * @version 1.0
 */
public class EntityListener {

	/**
	 * 保存前处理
	 * 
	 * @param entity
	 *            基类
	 */
	@PrePersist
	public void prePersist(BaseEntity entity) {
	}

	/**
	 * 更新前处理
	 * 
	 * @param entity
	 *            基类
	 */
	@PreUpdate
	public void preUpdate(BaseEntity entity) {
	}

}