/*
 * Copyright 2005-2020 Top Team All rights reserved.
 * Support: 
 * License: top team license
 */
package huxg.framework.web.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.alibaba.fastjson.JSONObject;
import huxg.framework.DateEditor;
import huxg.framework.HtmlCleanEditor;
import huxg.framework.PageInfo;
import huxg.framework.exception.Status;
import huxg.framework.filter.parameter.ParameterWrapper;
import huxg.framework.security.UserAuthentication;
import huxg.framework.util.CurrencyUtils;
import huxg.framework.util.SpringUtils;
import huxg.framework.util.StringUtils;

/**
 * Controller - 基类
 * 
 * @author Top Team（ ）
 * @version 1.0
 */
public class BaseController extends MyBaseController {

	/** 错误视图 */
	protected static final String ERROR_VIEW = "/error";

	/** "验证结果"参数名称 */
	private static final String CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME = "constraintViolations";

	@Resource(name = "validator")
	private Validator validator;

	/**
	 * 数据绑定
	 * 
	 * @param binder
	 *            WebDataBinder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new HtmlCleanEditor(true, true));
		binder.registerCustomEditor(Date.class, new DateEditor(true));
	}

	/**
	 * 数据验证
	 * 
	 * @param target
	 *            验证对象
	 * @param groups
	 *            验证组
	 * @return 验证结果
	 */
	protected boolean isValid(Object target, Class<?>... groups) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target, groups);
		if (constraintViolations.isEmpty()) {
			return true;
		} else {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME, constraintViolations, RequestAttributes.SCOPE_REQUEST);
			return false;
		}
	}

	/**
	 * 数据验证
	 * 
	 * @param type
	 *            类型
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @param groups
	 *            验证组
	 * @return 验证结果
	 */
	protected boolean isValid(Class<?> type, String property, Object value, Class<?>... groups) {
		Set<?> constraintViolations = validator.validateValue(type, property, value, groups);
		if (constraintViolations.isEmpty()) {
			return true;
		} else {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME, constraintViolations, RequestAttributes.SCOPE_REQUEST);
			return false;
		}
	}

	/**
	 * 货币格式化
	 * 
	 * @param amount
	 *            金额
	 * @param showSign
	 *            显示标志
	 * @param showUnit
	 *            显示单位
	 * @return 货币格式化
	 */
	protected String currency(BigDecimal amount, boolean showSign, boolean showUnit) {
		String price = CurrencyUtils.setScale(amount).toString();
		if (showSign) {
			price = "￥" + price;
		}
		if (showUnit) {
			price += "元";
		}
		return price;
	}

	/**
	 * 获取国际化消息
	 * 
	 * @param code
	 *            代码
	 * @param args
	 *            参数
	 * @return 国际化消息
	 */
	protected String message(String code, Object... args) {
		return SpringUtils.getMessage(code, args);
	}

	public JSONObject getLoginUserInfo() {
		JSONObject user = UserAuthentication.getLoginUserInfo(getRequest());
		return user;
	}

	// /**
	// * 返回json
	// *
	// * @param results
	// * @return
	// */
	// public static String okJson(List<?> results) {
	// JSONObject js = new JSONObject();
	// js.element("success", true);
	//
	// JSONArray jsarray = JSONArray.fromObject(results);
	//
	// js.element("msg", jsarray);
	// return js.toString();
	// }
	//
	// /**
	// * 返回错误的json字符串
	// *
	// * @param msg
	// * @param keys
	// * @param values
	// * @return
	// */
	// public static String errorJson(String msg, String[] keys, String[]
	// values) {
	// JSONObject js = new JSONObject();
	// js.element("success", false);
	// js.element("msg", msg);
	//
	// if (keys != null) {
	// for (int i = 0; i < keys.length; i++) {
	// js.element(keys[i], values[i]);
	// }
	// }
	// return js.toString();
	// }
	//
	// public static String errorJson() {
	// JSONObject js = new JSONObject();
	// js.element("success", false);
	// js.element("msg", "服务器繁忙，请稍后重试！");
	// return js.toString();
	// }
	//
	// public static String errorJson(String msg) {
	// JSONObject js = new JSONObject();
	// js.element("success", false);
	// js.element("msg", msg);
	// return js.toString();
	// }
	//
	// public static String errorJson(Throwable t) {
	// JSONObject js = new JSONObject();
	// js.element("success", false);
	// js.element("msg", t.getCause());
	// return js.toString();
	// }
	//
	// /**
	// * 返回结果正确的json字符串
	// *
	// * @param msg
	// * @param keys
	// * @param values
	// * @return
	// */
	// public static String okJson(String msg, String[] keys, String[] values) {
	// JSONObject js = new JSONObject();
	// js.element("success", true);
	// js.element("msg", msg);
	//
	// if (keys != null) {
	// for (int i = 0; i < keys.length; i++) {
	// js.element(keys[i], values[i]);
	// }
	// }
	// return js.toString();
	// }
	//
	// public static String okJson(String[] keys, String[] values) {
	// JSONObject js = new JSONObject();
	// js.element("success", true);
	//
	// if (keys != null) {
	// for (int i = 0; i < keys.length; i++) {
	// js.element(keys[i], values[i]);
	// }
	// }
	// return js.toString();
	// }
	//
	// public static String okJson(Object o) {
	// JSONObject js = new JSONObject();
	// js.element("success", true);
	//
	// if (o instanceof String) {
	// js.element("msg", (String) o);
	// } else if (o instanceof Collection) {
	// JSONArray ja = JSONArray.fromObject(o);
	// js.element("msg", ja);
	// } else {
	// JSONObject j = JSONObject.fromObject(o);
	// js.element("msg", j);
	// }
	// return js.toString();
	// }
	//
	// public static String okJson() {
	// JSONObject js = new JSONObject();
	// js.element("success", true);
	// return js.toString();
	// }

	public Object getParameter(String key) {
		return ParameterWrapper.getWrapper().get(key);
	}

	public void setParameter(String key, Object value) {
		ParameterWrapper.getWrapper().set(key, value);
	}

	public long getParameterLong(String key) {
		return Long.parseLong((String) ParameterWrapper.getWrapper().get(key));
	}

	public int getParameterInt(String key) {
		return Integer.parseInt((String) ParameterWrapper.getWrapper().get(key));
	}

	public void registerProperty(String name, Class clazz) {
		ParameterWrapper.getWrapper().registerProperty(name, clazz);
	}

	/**
	 * 注册一个集合类 针对属性做全过滤，仅提取集合属性 对于正常的集合来说，registerProperty方法可以实现提取集合中的内容
	 * 但是对于某些情况，提交的内容不符合完全的规范，例如列表提交过来 的数据中集合与非集合无法定位，所以此方法针对这类情况进行再次过滤。
	 * 
	 * @param name
	 * @param clazz
	 * @return
	 */
	public void registerCollection(String name, Class clazz) {
		ParameterWrapper.getWrapper().registerCollection(name, clazz);
	}

	public void registerProperty(String name, Object destObject) {
		ParameterWrapper.getWrapper().registerProperty(name, destObject);
	}

	protected Object getUploadFiles(String key) {
		Map uploadFiles = (Map) getParameter(ParameterWrapper.UPLOAD_FILES);
		if (uploadFiles != null) {
			return uploadFiles.get(key);
		}
		return null;
	}

	protected String[] getParaments(String sPar) {
		String[] saValues;
		Object objValues = getParameter(sPar);

		if (objValues != null) {
			if (objValues.getClass().getComponentType() != null) {
				saValues = (String[]) getParameter(sPar);
			} else {
				saValues = new String[1];
				saValues[0] = (String) getParameter(sPar);
			}
		} else
			return null;

		return saValues;
	}

	/**
	 * 机构入驻首页面
	 * 
	 * @return
	 */
	public String getSelectedProvinceId() {
		// 查询全部市区
		HttpServletRequest request = ParameterWrapper.getWrapper().getRequest();
		String selectedUserAreaId = "110000";
		Cookie[] cookies = ((HttpServletRequest) request).getCookies();
		for (Cookie cookie : cookies) {

			// 找到cookie中存在的登录信息
			if ("user_area_id".equals(cookie.getName())) {
				selectedUserAreaId = cookie.getValue();
				break;
			}
		}
		return selectedUserAreaId;
	}

	/**
	 * 初始化页面分页信息，第一个分页。
	 */
	public void initPageInfo() {
		String start = (String) getParameter("pageNum");
		if (StringUtils.isEmptyAfterTrim(start) || "NaN".equalsIgnoreCase(start)) {
			start = "0";
		}

		PageInfo pageInfo = new PageInfo();
		int pageindex = Integer.valueOf(start) / 20;
		pageInfo.setCurrentPageIndex(pageindex + 1);
		pageInfo.setPageSize(20);
		setParameter("com.huxg.taglib.PageSplitTag", pageInfo);
	}

	public void initPageInfo(int pageSize) {
		String start = (String) getParameter("pageNum");
		if (StringUtils.isEmptyAfterTrim(start) || "NaN".equalsIgnoreCase(start)) {
			start = "0";
		}
		PageInfo pageInfo = new PageInfo();
		int pageindex = Integer.valueOf(start) / pageSize;
		pageInfo.setCurrentPageIndex(pageindex + 1);
		pageInfo.setPageSize(pageSize);
		setParameter("com.huxg.taglib.PageSplitTag", pageInfo);
	}

	/**
	 * 判断是否有错误
	 * 
	 * @return
	 */
	public boolean isError() {
		Status status = (Status) getParameter("_page_status_");
		if (status != null && !status.isSuccess()) {
			return true;
		}
		return false;
	}

	/**
	 * 添加一个输入框错误
	 * 
	 * @param field
	 * @param message
	 */
	public void addFieldError(String field, String message) {
		Status status = (Status) getParameter("_page_status_");
		if (status == null) {
			status = new Status();
			status.setSuccess(false);
			setParameter("_page_status_", status);
		}
		status.addMessage(field, message);
	}

	// /**
	// * 添加一个输入框错误
	// *
	// * @param field
	// * @param message
	// */
	// public void setError(String field, String message) {
	// Status status = new Status();
	// status.setSuccess(false);
	// status.addMessage("", message);
	// setParameter("_page_status_", status);
	// }
	//
	// /**
	// * 创建一个空得错误信息
	// *
	// * @return
	// */
	// public String error() {
	// Status status = (Status) getParameter("_page_status_");
	// if (status == null) {
	// status = new Status();
	// }
	// status.setSuccess(false);
	// return JSONObject.fromObject(status).toString();
	// }
	//
	// /**
	// * 创建一个错误反馈
	// *
	// * @param message
	// * @throws Exception
	// */
	// public void error(String message) throws Exception {
	// addFieldError("", message);
	// }
	//
	// // 错误的json返回值
	// public String errorJsonResult() {
	// Status status = (Status) getParameter("_page_status_");
	// return JSONObject.fromObject(status).toString();
	// }
	//
	// public String errorJsonResult(String message) {
	// Status status = new Status();
	// status.setSuccess(false);
	// status.addMessage("", message);
	// setParameter("_page_status_", status);
	// return JSONObject.fromObject(status).toString();
	// }
	//
	// /**
	// * 显示页面几秒中跳转
	// *
	// * @param message
	// * @param redirect
	// * @return
	// * @throws Exception
	// */
	// public String pageRedirect(String message, String redirect) {
	// Status status = (Status) getParameter("_page_status_");
	// if (status == null) {
	// status = new Status();
	// setParameter("_page_status_", status);
	// }
	// status.empty();
	// status.setSuccess(true);
	// status.addMessage("", message, redirect);
	// ParameterWrapper.getWrapper().set("_ViewForwardPath_",
	// "/front/message.jsp");
	// return "_ok_redirect_";
	// }
	//
	// public String messagePage(String message, String redirect) {
	// Status status = (Status) getParameter("_page_status_");
	// if (status == null) {
	// status = new Status();
	// setParameter("_page_status_", status);
	// }
	// status.empty();
	// status.setSuccess(true);
	// status.addMessage("", message, redirect);
	// return "/front/message";
	// }
	//
	// public String ok(String[] keys, String[] values) {
	// Status status = (Status) getParameter("_page_status_");
	// if (status == null) {
	// status = new Status();
	// status.setSuccess(true);
	// setParameter("_page_status_", status);
	// }
	//
	// JSONObject js = new JSONObject();
	// if (keys != null) {
	// for (int i = 0; i < keys.length; i++) {
	// js.element(keys[i], values[i]);
	// }
	// }
	// status.setData(js.toString());
	//
	// return JSONObject.fromObject(status).toString();
	// }
	//
	// public String ok() {
	// Status status = (Status) getParameter("_page_status_");
	// if (status == null) {
	// status = new Status();
	// }
	// status.empty();
	// status.setSuccess(true);
	// return JSONObject.fromObject(status).toString();
	// }
	//
	// public void ok(String message) throws Exception {
	// Status status = (Status) getParameter("_page_status_");
	// if (status == null) {
	// status = new Status();
	// }
	// status.empty();
	// status.setSuccess(true);
	// status.addMessage("", message);
	// throw new AppException(status);
	// }
	//
	// public String ok(String message, String redirect) {
	// Status status = (Status) getParameter("_page_status_");
	// if (status == null) {
	// status = new Status();
	// }
	// status.setSuccess(true);
	// status.addMessage("", message, redirect);
	// return JSONObject.fromObject(status).toString();
	// }

	public JSONObject createGuestUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		JSONObject user = new JSONObject();
		user.put("name", "游客");
		user.put("id", session.getId());
		user.put("nickname", "游客");
		return user;
	}

	public int getPageStart() {
		String start = (String) getParameter("pageNum");
		if (StringUtils.isEmptyAfterTrim(start) || "NaN".equalsIgnoreCase(start)) {
			start = "1";
		}
		try {
			return Integer.parseInt(start);
		} catch (Exception e) {
			return 1;
		}
	}
}