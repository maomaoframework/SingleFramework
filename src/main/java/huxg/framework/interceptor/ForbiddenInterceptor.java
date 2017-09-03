/*
 * Copyright 2005-2020 Top Team All rights reserved.
 * Support: 
 * License: top team license
 */
package huxg.framework.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor - 会员权限
 * 
 * @author Top Team（ ）
 * @version 1.0
 */
public class ForbiddenInterceptor extends HandlerInterceptorAdapter {
//
//	/** 重定向视图名称前缀 */
//	private static final String REDIRECT_VIEW_NAME_PREFIX = "redirect:";
//
//	/** "重定向URL"参数名称 */
//	private static final String REDIRECT_URL_PARAMETER_NAME = "redirectUrl";
//
//	/** "会员"属性名称 */
//	private static final String MEMBER_ATTRIBUTE_NAME = "member";
//
//	/** 默认登录URL */
//	private static final String DEFAULT_LOGIN_URL = "/front/login.jhtml";
//
//	/** 登录URL */
//	private String loginUrl = DEFAULT_LOGIN_URL;
//
//	@Value("${url_escaping_charset}")
//	private String urlEscapingCharset;
//
//	@Resource(name = "memberServiceImpl")
//	private MemberService memberService;
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//			String requestType = request.getHeader("X-Requested-With");
//			if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")) {
//				response.addHeader("loginStatus", "accessDenied");
//				response.sendError(HttpServletResponse.SC_FORBIDDEN);
//				return false;
//			} else {
//				if (request.getMethod().equalsIgnoreCase("GET")) {
//					String redirectUrl = request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();
//					response.sendRedirect(request.getContextPath() + loginUrl + "?" + REDIRECT_URL_PARAMETER_NAME + "=" + URLEncoder.encode(redirectUrl, urlEscapingCharset));
//				} else {
//					response.sendRedirect(request.getContextPath() + loginUrl);
//				}
//				return false;
//			}
//	}
//
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//		if (modelAndView != null) {
//			String viewName = modelAndView.getViewName();
//			if (!StringUtils.startsWith(viewName, REDIRECT_VIEW_NAME_PREFIX)) {
//				modelAndView.addObject(MEMBER_ATTRIBUTE_NAME, memberService.getCurrent());
//			}
//		}
//	}
//
//	/**
//	 * 获取登录URL
//	 * 
//	 * @return 登录URL
//	 */
//	public String getLoginUrl() {
//		return loginUrl;
//	}
//
//	/**
//	 * 设置登录URL
//	 * 
//	 * @param loginUrl
//	 *            登录URL
//	 */
//	public void setLoginUrl(String loginUrl) {
//		this.loginUrl = loginUrl;
//	}

}