package huxg.framework.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import huxg.framework.wrapper.HtmlCleanRequestWrapper;

/**
 * trim() and 防止注入攻击
 * 
 * @author 
 *  
 *  v1.0
 *  
 *  <filter>
		<filter-name>HtmlCleanFilter</filter-name>
		<filter-class>huxg.framework.filter.HtmlCleanFilter</filter-class>
		<init-param>
            <param-name>blackListURL</param-name>
            <param-value>/static/**,*.js,*.css,*.jpg,*.gif,*.png,*.ico,/druid/**,/admin/monitor/druid/**</param-value>
        </init-param>
        <init-param>
            <param-name>whiteListURL</param-name>
            <param-value>/**</param-value>
        </init-param>
	</filter>
 */
public class HtmlCleanFilter extends MyBaseFilter {

	 public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		 chain.doFilter(new HtmlCleanRequestWrapper(request), response);
	 }

}
