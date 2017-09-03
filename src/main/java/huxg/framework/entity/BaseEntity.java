/*
 * Copyright 2005-2020 GreenTube Team All rights reserved.
 * Support: Huxg
 * License: CND team license
 */
package huxg.framework.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.groups.Default;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;

import huxg.framework.listener.EntityListener;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity - 基类
 * 
 * @author Top Team（ ）
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
@EntityListeners(EntityListener.class)
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -67188388306700736L;

	/** "ID"属性名称 */
	public static final String ID_PROPERTY_NAME = "id";

	// /** "创建日期"属性名称 */
	// public static final String CREATE_DATE_PROPERTY_NAME = "createDate";
	//
	// /** "修改日期"属性名称 */
	// public static final String MODIFY_DATE_PROPERTY_NAME = "modifyDate";

	/**
	 * 保存验证组
	 */
	public interface Save extends Default {

	}

	/**
	 * 更新验证组
	 */
	public interface Update extends Default {

	}

	/** ID */
	@JsonProperty
	@DocumentId
	@Id
	// MySQL/SQLServer: @GeneratedValue(strategy = GenerationType.AUTO)
	// Oracle: @GeneratedValue(strategy = GenerationType.AUTO, generator =
	// "sequenceGenerator")
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "C_ID")
	private String id;

	// /** 创建日期 */
	// private Date createDate;
	//
	// /** 修改日期 */
	// private Date modifyDate;

	// /**
	// * 获取创建日期
	// *
	// * @return 创建日期
	// */
	// @JsonProperty
	// @Field(store = Store.YES, index = Index.UN_TOKENIZED)
	// @DateBridge(resolution = Resolution.SECOND)
	// @Column(nullable = false, updatable = false)
	// public Date getCreateDate() {
	// return createDate;
	// }

	// /**
	// * 设置创建日期
	// *
	// * @param createDate
	// * 创建日期
	// */
	// public void setCreateDate(Date createDate) {
	// this.createDate = createDate;
	// }

	// /**
	// * 获取修改日期
	// *
	// * @return 修改日期
	// */
	// @JsonProperty
	// @Field(store = Store.YES, index = Index.UN_TOKENIZED)
	// @DateBridge(resolution = Resolution.SECOND)
	// @Column(nullable = false)
	// public Date getModifyDate() {
	// return modifyDate;
	// }
	//
	// /**
	// * 设置修改日期
	// *
	// * @param modifyDate
	// * 修改日期
	// */
	// public void setModifyDate(Date modifyDate) {
	// this.modifyDate = modifyDate;
	// }

	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!BaseEntity.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		BaseEntity other = (BaseEntity) obj;
		return getId() != null ? getId().equals(other.getId()) : false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSid() {
		return this.id;
	}

	public void setSid(String sid) {
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return hashCode
	 */
	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode += null == getId() ? 0 : getId().hashCode() * 31;
		return hashCode;
	}

}