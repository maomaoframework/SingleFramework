package huxg.framework.web.view;

import java.io.File;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class HtmlInterceptor implements HandlerInterceptor {

	private static final HashMap<String, String> HTML_MAPPING = new HashMap<String, String>();

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView view) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 请求的 url 地址
		String urlBorw = request.getServletPath();
		String htmlUrl = HTML_MAPPING.get(urlBorw);
		if (null != htmlUrl && htmlUrl.length() > 0) {
			File file = new File(request.getSession().getServletContext().getRealPath("") + htmlUrl);
			if (file.exists()) {
				request.getRequestDispatcher(htmlUrl).forward(request, response);
				return false;
			}
		}
		return true;
	}
}