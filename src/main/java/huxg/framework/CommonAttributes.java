/*
 * Copyright 2005-2020 Top Team All rights reserved.
 * Support: 
 * License: top team license
 */
package huxg.framework;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import huxg.framework.dao.jdbc.Table;

/**
 * 公共参数
 * 
 * @author Top Team（ ）
 * @version 1.0
 */
public final class CommonAttributes {
	public static final String UTF8 = "UTF-8";
	
	public static final boolean FILTER_SHOW_SQL=false;
	
	public static Map<String,Table> tableMapping=new HashMap<String,Table>();
	
	public static Map<Class,String> classMapping=new HashMap<Class,String>();
	
	public static Map<Class,Map<Method,String>> classMethodColumnMapping=new HashMap<Class,Map<Method,String>>();
	
	public static Map<Class,Map<String,Method>> classColumnMethodMapping=new HashMap<Class,Map<String,Method>>();

	/** 日期格式配比 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	/** top.xml文件路径 */
	public static final String TOP_XML_PATH = "/top.xml";

	/** top.properties文件路径 */
	public static final String TOP_PROPERTIES_PATH = "/top.properties";
	
	public static final Map<String,String> DICT_TYPE_MAP=new HashMap<String,String>();
	
	static{
		//机构类型
		DICT_TYPE_MAP.put("trainingAgency.typeId", "agencyType");
		//商品类型
		DICT_TYPE_MAP.put("product.typeId", "productType");
		//班级类型
		DICT_TYPE_MAP.put("trainingClass.typeId", "classType");
		//上课时间
		DICT_TYPE_MAP.put("trainingClass.schooltimeId", "classSchooltime");
		//课程类型
		DICT_TYPE_MAP.put("course.typeId", "courseType");
		// 题型
		DICT_TYPE_MAP.put("question.typeId", "questionType");
		// 来源
		DICT_TYPE_MAP.put("question.originId", "questionOrigin");
		// 来源
		DICT_TYPE_MAP.put("question.diff", "questionDiff");
		// 组卷方式
		DICT_TYPE_MAP.put("paper.createType", "paperCreateType");
		// 试卷类型
		DICT_TYPE_MAP.put("paper.typeId", "paperType");
		//TODO 
	}
	/** 商品类型字典 */
	public static Long PRODUCT_TYPE_COURSE = 999L;
	/** 默认收获地址 */
	public static Long DEFAULT_RECEIVER_ID = 999L;
	/** 默认支付方式 */
	public static Long DEFAULT_PAYMENTMETHOD_Id = 999L;
	/** 默认配送方式 */
	public static Long DEFAULT_SHIPPINGMETHOD_Id = 999L;
	/** 课程类别-机考系统 */
	public static Long COURSE_TYPE_EXAM = 1009L;
	
	/** 题型-判断 */
	public static Long QUESTION_JUDGE = 4L;
	/** 题型-单选 */
	public static Long QUESTION_SINGLE = 5L;
	/** 题型-多选 */
	public static Long QUESTION_MULTI = 6L;
	
	/** 组卷方式-自动 */
	public static Long PAPER_CREATETYPE_AUTO = 16L;
	/** 组卷方式-手动 */
	public static Long PAPER_CREATETYPE_MANUAL = 15L;
	
	/** 章练习题 */
	public static Long PAPER_TYPE_CHAPTER = 12L;
	/** 模拟考试 */
	public static Long PAPER_TYPE_SIMULATION = 13L;
	/** 历年真题 */
	public static Long PAPER_TYPE_HISTORY = 14L;
	
	/**xian shang  */
	public static Long XIANSAHN = 1L;
	/**xian xia  */
	public static Long XIANXIA = 2L;

	/**
	 * 不可实例化
	 */
	private CommonAttributes() {
	}
	
	public static void main(String[] args) {
	}

}