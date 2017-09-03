/*
 * Copyright 2005-2020 Top Team All rights reserved.
 * Support: 
 * License: top team license
 */
package huxg.framework.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

/**
 * Filter - 限制访问
 * 
 * @author Top Team（ ）
 * @version 1.0
 */
public class AccessDeniedFilter implements Filter {

	/** 错误消息 */
	private static final String ERROR_MESSAGE = "Access denied!";

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.addHeader(new String(Base64.decodeBase64("UG93ZXJlZEJ5"), "utf-8"), new String(Base64.decodeBase64("VG9wIFRlYW0="), "utf-8"));
		response.sendError(HttpServletResponse.SC_FORBIDDEN, ERROR_MESSAGE);
	}
}