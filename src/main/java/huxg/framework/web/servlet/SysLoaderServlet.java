package huxg.framework.web.servlet;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;

import huxg.framework.filter.parameter.converter.DateConverter;
import huxg.framework.filter.parameter.converter.SqlDateConverter;

public class SysLoaderServlet extends HttpServlet {
	private final Log logger = LogFactory.getLog(this.getClass());

	/** 应用的文件系统路径(根目录) */
	protected static String WEBAPP_RealPath = null;

	/** 应用的访问URL ContextPath */
	protected static String WEBAPP_ContextPath = null;

	private static WebApplicationContext SPRING_MVC_ContextPath = null;

	final String PARAM_DATE_STYLE_REGEX = "date-style-regex";

	final String PARAM_TIME_STYLE_REGEX = "time-style-regex";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		initContextPath();

		initConvertUtils();

		ServletContext context = config.getServletContext();

		SPRING_MVC_ContextPath = (WebApplicationContext) context.getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.springmvc");
	}

	protected void initContextPath() {
		WEBAPP_RealPath = getServletContext().getRealPath("/");
		WEBAPP_ContextPath = this.getServletContext().getContextPath();
		if (logger.isInfoEnabled()) {
			logger.info("系统真实路径: " + WEBAPP_RealPath);
			logger.info("系统相对路径: " + WEBAPP_ContextPath);
		}
	}

	protected void initConvertUtils() {
		// 配置日期类型的转换器
		String dateRegex = getInitParameter(PARAM_DATE_STYLE_REGEX);
		String timeRegex = getInitParameter(PARAM_TIME_STYLE_REGEX);
		if (dateRegex == null) {
			dateRegex = DateConverter.DEF_DATE_STYLE_REGEX;
		}
		if (timeRegex == null) {
			timeRegex = DateConverter.DEF_TIME_STYLE_REGEX;
		}

		ConvertUtils.register(new DateConverter(dateRegex, timeRegex, null), java.util.Date.class);
		/*
		 * limin: 因为beanutils的SqlConvert，对于null和空串会报参数不合法的错误。
		 * 此处通过重载相关的Converter来解决此问题。
		 */
		ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);

		// 以下将相关类型的转换默认值置为null
		ConvertUtils.register(new FloatConverter(null), Float.class);
		ConvertUtils.register(new DoubleConverter(null), Double.class);

		ConvertUtils.register(new ByteConverter(null), Byte.class);
		ConvertUtils.register(new ShortConverter(null), Short.class);
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		ConvertUtils.register(new LongConverter(null), Long.class);

		ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
		ConvertUtils.register(new BigIntegerConverter(null), BigInteger.class);
		ConvertUtils.register(new BooleanConverter(null), Boolean.class);
	}

	/**
	 * @return 返回当前应用系统根目录在操作系统中的文件路径
	 */
	public static String getRealPath() {
		return WEBAPP_RealPath;
	}

	/**
	 * @see HttpServletRequest#getContextPath()
	 */
	public static String getContextPath() {
		return WEBAPP_ContextPath;
	}

	public static WebApplicationContext getSpringMVCContext() {
		return SPRING_MVC_ContextPath;
	}
}
