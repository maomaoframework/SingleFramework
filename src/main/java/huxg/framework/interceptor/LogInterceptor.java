/*
 * Copyright 2005-2020 Top Team All rights reserved.
 * Support: 
 * License: top team license
 */
package huxg.framework.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor - 日志记录
 * 
 * @author Top Team（ ）
 * @version 1.0
 */
public class LogInterceptor extends HandlerInterceptorAdapter {
//
//	/** 默认忽略参数 */
//	private static final String[] DEFAULT_IGNORE_PARAMETERS = new String[] { "password", "rePassword", "currentPassword" };
//
//	/** antPathMatcher */
//	private static AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//	/** 忽略参数 */
//	private String[] ignoreParameters = DEFAULT_IGNORE_PARAMETERS;
//
//	@Resource(name = "logConfigServiceImpl")
//	private LogConfigService logConfigService;
//	@Resource(name = "logServiceImpl")
//	private LogService logService;
//	@Resource(name = "adminServiceImpl")
//	private AdminService adminService;
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//		List<LogConfig> logConfigs = logConfigService.getAll();
//		if (logConfigs != null) {
//			String path = request.getServletPath();
//			for (LogConfig logConfig : logConfigs) {
//				if (antPathMatcher.match(logConfig.getUrlPattern(), path)) {
//					String username = adminService.getCurrentUsername();
//					String operation = logConfig.getOperation();
//					String operator = username;
//					String content = (String) request.getAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME);
//					String ip = request.getRemoteAddr();
//					request.removeAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME);
//					StringBuffer parameter = new StringBuffer();
//					Map<String, String[]> parameterMap = request.getParameterMap();
//					if (parameterMap != null) {
//						for (Entry<String, String[]> entry : parameterMap.entrySet()) {
//							String parameterName = entry.getKey();
//							if (!ArrayUtils.contains(ignoreParameters, parameterName)) {
//								String[] parameterValues = entry.getValue();
//								if (parameterValues != null) {
//									for (String parameterValue : parameterValues) {
//										parameter.append(parameterName + " = " + parameterValue + "\n");
//									}
//								}
//							}
//						}
//					}
//					Log log = new Log();
//					log.setOperation(operation);
//					log.setOperator(operator);
//					log.setContent(content);
//					log.setParameter(parameter.toString());
//					log.setIp(ip);
//					logService.save(log);
//					break;
//				}
//			}
//		}
//	}
//
//	/**
//	 * 设置忽略参数
//	 * 
//	 * @return 忽略参数
//	 */
//	public String[] getIgnoreParameters() {
//		return ignoreParameters;
//	}
//
//	/**
//	 * 设置忽略参数
//	 * 
//	 * @param ignoreParameters
//	 *            忽略参数
//	 */
//	public void setIgnoreParameters(String[] ignoreParameters) {
//		this.ignoreParameters = ignoreParameters;
//	}

}