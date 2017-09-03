package huxg.framework.web.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import huxg.framework.config.SysConfiguration;
import huxg.framework.filter.parameter.ParameterWrapper;
import huxg.framework.util.BrowserUtils;
import huxg.framework.util.StringUtils;
import huxg.framework.web.servlet.SysLoaderServlet;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MyFreemarkerView extends FreeMarkerView {
	@Override
	protected Map<String, Object> createMergedOutputModel(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> mergedModel = super.createMergedOutputModel(model, request, response);

		// 合并ParameterWrapper中的参数
		Map<String, Object> dyProperties = ParameterWrapper.getWrapper().getDynProperties();
		if (null != dyProperties) {
			mergedModel.putAll(dyProperties);
		}
		return mergedModel;
	}

	protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		exposeModelAsRequestAttributes(model, request);
		SimpleHash fmModel = buildTemplateModel(model, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("Rendering FreeMarker template [" + getUrl() + "] in FreeMarkerView '" + getBeanName() + "'");
		}
		Locale locale = RequestContextUtils.getLocale(request);

		// if (Boolean.FALSE.equals(model.get("STATIC_HTML"))) {
		processTemplate(getTemplate(getUrl(), locale, request), fmModel, response);
		// } else {
		// createHTML(getTemplate(locale), fmModel, request, response);
		// }
	}

	/**
	 * 生成HTML
	 * 
	 * @param template
	 * @param model
	 * @param filePath
	 * @param response
	 * @throws IOException
	 * @throws TemplateException
	 * @throws ServletException
	 */
	private void createHTML(Template template, SimpleHash model, HttpServletRequest request, HttpServletResponse response) throws IOException,
			TemplateException, ServletException {
		// 站点根目录的绝对路径
		String basePath = request.getSession().getServletContext().getRealPath("/");
		String requestHTML = this.getRequestHTML(request);
		// <strong>静态</strong><strong>页面</strong>绝对路径
		String htmlPath = basePath + requestHTML;
		File htmlFile = new File(htmlPath);

		if (!htmlFile.getParentFile().exists()) {
			htmlFile.getParentFile().mkdirs();
		}

		if (!htmlFile.exists()) {
			htmlFile.createNewFile();
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), "UTF-8"));
			// 处理模版
			template.process(model, out);
			out.flush();
			out.close();
		}
		/* 将请求转发到生成的htm文件 */
		request.getRequestDispatcher(requestHTML).forward(request, response);
	}

	protected Template getTemplate(String name, Locale locale, HttpServletRequest request) throws IOException {
		String templateName = name;
		if (!StringUtils.isEmpty(templateName)) {
			if (templateName.startsWith("/front/")) {

				// 判断是哪种浏览器
				if (BrowserUtils.isMobileBrowser(request)) {
					// 判断当前是是否存在手机版文件
					// 处理url，并转向到手机端页面
					String mobilePath = templateName.substring(7);
					mobilePath = "/mobile/" + mobilePath;
					if (existTemplate(mobilePath)) {
						templateName = mobilePath;
					}
				}
			}
		}
		return (getEncoding() != null ? getConfiguration().getTemplate(templateName, locale, getEncoding()) : getConfiguration().getTemplate(templateName,
				locale));
	}

	/**
	 * 计算要生成的<strong>静态</strong>文件相对路径.
	 */
	private String getRequestHTML(HttpServletRequest request) {

		// web应用名称,部署在ROOT目录时为空
		String contextPath = request.getContextPath();

		// web应用/目录/文件.do
		String requestURI = request.getRequestURI();

		// basePath里面已经有了web应用名称，所以直接把它replace掉，以免重复
		requestURI = requestURI.replaceFirst(contextPath, "");
		// 将请求改为.htm,稍后将请求转发到此htm文件
		requestURI += ".htm";
		return requestURI;
	}

	boolean existTemplate(String templateName) {
		String rootFolder = SysConfiguration.getProperty("template.loader_path");
		String[] s = rootFolder.split(",");
		String folder = s[0] + templateName;
		String filepath = SysLoaderServlet.getRealPath() + folder;
		File f = new File(filepath);
		return f.exists();
	}
}
