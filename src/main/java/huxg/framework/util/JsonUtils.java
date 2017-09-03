/*
 * Copyright 2005-2020 Top Team All rights reserved.
 * Support: 
 * License: CND team license
 */
package huxg.framework.util;

import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * json转换工具类
 * @author huxg
 *
 */
public final class JsonUtils {
	public static final String bean2String(Object obj){
		return JSON.toJSONString(obj);
	}
	
	/**
	 * 字符串转JSONArray
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static final JSONArray String2JSONArray(String jsonStr) {
		return (JSONArray) JSONArray.parse(jsonStr);
	}

	/**
	 * 字符串转换JSONObject
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static final JSONObject String2JSONObject(String jsonStr) {
		return (JSONObject) JSONObject.parse(jsonStr);
	}

	/**
	 * 字符串转Bean
	 * 
	 * @param jsonStr
	 * @param clazz
	 * @return
	 */
	public static final <T> T String2Bean(String jsonStr, Class<T> clazz) {
		return JSON.parseObject(jsonStr, clazz);
	}

	/**
	 * JSONObject转Bean
	 * 
	 * @param jsonObject
	 * @param clazz
	 * @return
	 */
	public static final <T> T JSONObject2Bean(JSONObject jsonObject, Class<T> clazz) {
		return JSON.parseObject(jsonObject.toString(), clazz);
	}

	/**
	 * 字符串转换为Collection
	 * 
	 * @param str
	 * @param clazz
	 * @return
	 */
	public static final <T> Collection<T> String2Collection(String jsonStr, Class<T> clazz) {
		Collection<T> t = (Collection<T>) JSON.parseArray(jsonStr, clazz);
		return t;
	}

	/**
	 * 字符串转换为List
	 * 
	 * @param str
	 * @param clazz
	 * @return
	 */
	public static final <T> List<T> String2List(String jsonStr, Class<T> clazz) {
		List<T> t = (List<T>) JSON.parseArray(jsonStr, clazz);
		return t;
	}

	/**
	 * 将JSONArray转换为List对象
	 * 
	 * @param jsonArray
	 * @param clazz
	 * @return
	 */
	public static final <T> List<T> JSONArray2List(JSONArray jsonArray, Class<T> clazz) {
		List<T> t = (List<T>) JSON.parseArray(jsonArray.toString(), clazz);
		return t;
	}

	/**
	 * 将JSONArray转换为List对象
	 * 
	 * @param jsonArray
	 * @param clazz
	 * @return
	 */
	public static final <T> Collection<T> JSONArray2Collection(JSONArray jsonArray, Class<T> clazz) {
		Collection<T> t = (Collection<T>) JSON.parseArray(jsonArray.toString(), clazz);
		return t;
	}
}