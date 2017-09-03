package huxg.framework.filter.parameter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

public abstract class RequestWrapperFilter implements Filter {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	/* The FilterConfig of this filter */
	private FilterConfig filterConfig;

	public final void init(FilterConfig filterConfig) throws ServletException {
		Assert.notNull(filterConfig, "FilterConfig must not be null");
		if (logger.isDebugEnabled()) {
			logger.debug("Initializing filter '" + filterConfig.getFilterName()
					+ "'");
		}
		this.filterConfig = filterConfig;
		this.initFilter();
	}

	public void destroy() {
		this.filterConfig = null;
	}

	/**
	 * Subclasses may implements this to perform custom initialization.
	 * 
	 * @throws ServletException
	 *             if subclass initialization fails
	 */
	protected abstract void initFilter() throws ServletException;

	/**
	 * Make the FilterConfig of this filter available, if any.
	 * <p>
	 * Public to resemble the <code>getFilterConfig()</code> method of the
	 * Servlet Filter version that shipped with WebLogic 6.1.
	 * 
	 * @return the FilterConfig instance, or <code>null</code> if none available
	 */
	public final FilterConfig getFilterConfig() {
		return this.filterConfig;
	}

	/**
	 * Make the ServletContext of this filter available to subclasses.
	 * 
	 * @return the ServletContext instance, or <code>null</code> if none
	 *         available
	 * @see javax.servlet.FilterConfig#getServletContext()
	 */
	protected final ServletContext getServletContext() {
		return (this.filterConfig != null ? this.filterConfig
				.getServletContext() : null);
	}

	/**
	 * Check for a multipart request via this filter's MultipartResolver, and
	 * wrap the original request with a MultipartHttpServletRequest if
	 * appropriate.
	 * <p>
	 * All later elements in the filter chain, most importantly servlets,
	 * benefit from proper parameter extraction in the multipart case, and are
	 * able to cast to MultipartHttpServletRequest if they need to.
	 */
	public final void doFilter(ServletRequest request,
			ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest processedRequest = (HttpServletRequest) request;

		MultipartResolver multipartResolver = lookupMultipartResolver(processedRequest);
		if (multipartResolver.isMultipart(processedRequest)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Resolving multipart request ["
						+ processedRequest.getRequestURI()
						+ "] with MultipartFilter");
			}
			processedRequest = multipartResolver
					.resolveMultipart(processedRequest);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Request [" + processedRequest.getRequestURI()
						+ "] is not a multipart request");
			}
			processedRequest = new MyHttpServletRequest(processedRequest);
		}

		try {
			doFilterInternal(processedRequest, (HttpServletResponse) response,
					filterChain);
		} finally {
			if (processedRequest instanceof MultipartHttpServletRequest) {
				multipartResolver
						.cleanupMultipart((MultipartHttpServletRequest) processedRequest);
			}
		}
	}

	/**
	 * Same contract as for <code>doFilter</code>, but guaranteed to be just
	 * invoked once per request. Provides HttpServletRequest and
	 * HttpServletResponse arguments instead of the default ServletRequest and
	 * ServletResponse ones.
	 */
	protected abstract void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException;

	/**
	 * Look up the MultipartResolver that this filter should use, taking the
	 * current HTTP request as argument.
	 * 
	 * @return the MultipartResolver to use
	 */
	protected abstract MultipartResolver lookupMultipartResolver(
			HttpServletRequest request);
}
