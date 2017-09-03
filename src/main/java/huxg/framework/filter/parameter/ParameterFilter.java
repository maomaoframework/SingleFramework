package huxg.framework.filter.parameter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartResolver;

import huxg.framework.util.SpringUtils;

public class ParameterFilter extends RequestWrapperFilter {

	public static final String DEFAULT_MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";

	private String multipartResolverBeanName = DEFAULT_MULTIPART_RESOLVER_BEAN_NAME;

	protected void initFilter() throws ServletException {
		multipartResolverBeanName = getFilterConfig().getInitParameter("multipartResolverBeanName");
		if (multipartResolverBeanName == null) {
			multipartResolverBeanName = DEFAULT_MULTIPART_RESOLVER_BEAN_NAME;
		} else {
			multipartResolverBeanName = multipartResolverBeanName.trim();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Using MultipartResolver '" + multipartResolverBeanName + "' for '" + getFilterConfig().getFilterName() + "'");
		}
	}

	public void destroy() {
		super.destroy();
		this.multipartResolverBeanName = null;
	}

	protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException,
			IOException {
		try {
			// init ParameterWrapper
			ParameterWrapper.initWrapper(request, response);
			ParameterWrapper.getWrapper().buildWrapper();
			//
			// // do filter chain
			filterChain.doFilter(request, response);
		} finally {
			// reset ParameterWrapper
			ParameterWrapper.resetWrapper();
		}
	}

	/**
	 * Look for a MultipartResolver bean in the root web application context.
	 * Supports a "multipartResolverBeanName" filter init param; the default
	 * bean name is "filterMultipartResolver".
	 * <p>
	 * This can be overridden to use a custom MultipartResolver instance, for
	 * example if not using a Spring web application context.
	 * 
	 * @return the MultipartResolver instance, or <code>null</code> if none
	 *         found
	 */
	protected MultipartResolver lookupMultipartResolver(HttpServletRequest request) {
		try {
			return (MultipartResolver) SpringUtils.getBean(multipartResolverBeanName);
		} catch (RuntimeException re) {
			logger.error(re);
			throw re;
		}
	}

}
