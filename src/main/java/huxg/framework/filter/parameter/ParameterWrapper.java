package huxg.framework.filter.parameter;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import huxg.framework.util.StringUtils;

/**
 * 该类用于将业务逻辑层与请求层分离.<br>
 * 为了达到业务逻辑层不依靠request、response等属于控制层的相关类, <br>
 * 就需要一种方式将业务逻辑与控制层相互隔离,为了使业务逻辑获得相关的参数信息, <br>
 * 提供了这样一个ParameterWrapper类,该类对Web应用中的请求进行了封装, <br>
 * 是的业务逻辑可以只从这个包装类中来获取所需要的一切资源,而不必与控制层交互了. <br>
 * 这么做的一种好处是业务逻辑更加独立,对request等内容的依赖性更小.一次编写的业务层 <br>
 * 代码既可以运行在web环境下,也可以运行于Application环境中,而无需进行大的改动. <br>
 * 如果进行移植的话,只需要重新实现一个ParameterWrapper即可.
 * 
 * @author 胡晓光
 */
public class ParameterWrapper {

	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * The attribute key for upload files in request.
	 */
	public static final String UPLOAD_FILES = ParameterWrapper.class.getName() + ".UPLOAD_FILES";

	private static final ThreadLocal localContext = new ThreadLocal();

	protected HttpServletRequest request;
	protected HttpServletResponse response;

	protected Map cookieMap = null;
	protected Map requestMap = null;
	protected Map sessionMap = null;
	protected Map applicationMap = null;

	/** 请求参数的缓存 */
	protected Map dynaProperties = null;

	/** 需要重新 build 参数包装器 */
	protected boolean needRebuild = false;

	/**
	 * TODO:useless properties?
	 */
	protected String urlParameters = null;

	private String viewForwardPath = null;

	protected ForwardInterceptorInvoker forwardInterceptorInvoker = null;

	/**
	 * 是否已初始化,调用:<br>
	 * {@link #initWrapper(HttpServletRequest, HttpServletResponse)}
	 */
	protected boolean inited = false;

	/**
	 * 构造方法，拒绝外部实例化
	 */
	protected ParameterWrapper() {
	}

	/**
	 * 获取包装器<br>
	 * 注意：该实例对象在使用前必须先调用
	 * {@link #initWrapper(HttpServletRequest, HttpServletResponse)}<br>
	 * 进行初始化,否则将抛出异常。使用结束后调用 {@link #release()} 进行资源回收。<br>
	 * 每次回收完之后要再次进行初始化才可用。一般情况下，上述工作在底层统一已经处理了。
	 * 
	 * @return ParameterWrapper
	 */
	public static ParameterWrapper getWrapper() {
		ParameterWrapper ctx = (ParameterWrapper) localContext.get();
		if (ctx == null) {
			ctx = new ParameterWrapper();
			localContext.set(ctx);
		}
		return ctx;
	}

	/**
	 * 初始化包装器.<br>
	 * MemoryAction和AjaxServlet已经调用了该方法,因此开发人员不必调用该方法,如果是自己写的Action,则需要调用.
	 * 
	 * @param request
	 *            请求
	 * @param response
	 *            反馈
	 */
	public static void initWrapper(HttpServletRequest request, HttpServletResponse response) {
		ParameterWrapper ctx = getWrapper();
		if (ctx.inited != false) {
			ctx.release();
		}
		ctx.request = request;
		ctx.response = response;
		ctx.dynaProperties = new HashMap();
		ctx.dynaProperties.put(UPLOAD_FILES, new HashMap());

		// 初始化完毕
		ctx.inited = true;
	}

	/**
	 * 触发包装器的 rebuild (即重新调用 {@link #buildWrapper()})<br>
	 * rebuild不是马上进行，而是采用延迟的方式，在下一次通过包装器获取参数时进行。
	 */
	public static void fireRebuild() {
		ParameterWrapper ctx = getWrapper();
		ctx.needRebuild = true;
	}

	/**
	 * 重置包装器的状态.
	 */
	public static void resetWrapper() {
		ParameterWrapper ctx = getWrapper();
		ctx.release();
	}

	/**
	 * 释放占用的资源,重置状态.
	 */
	public void release() {
		this.request = null;
		this.response = null;

		this.cookieMap = null;
		this.requestMap = null;
		this.sessionMap = null;
		this.applicationMap = null;
		this.dynaProperties = null;

		this.urlParameters = null;
		this.forwardInterceptorInvoker = null;

		// 重置完毕,状态为"未初始化"
		this.inited = false;
	}

	/**
	 * @return Map,将对{@link Cookie}域内变量的访问封装为Map
	 */
	public Map getCookieMap() {
		if (cookieMap == null) {
			cookieMap = new CookieMap(request, response);
		}
		return cookieMap;
	}

	/**
	 * @return Map,将对{@link ServletRequest}域内变量的访问封装为Map
	 */
	public Map getRequestMap() {
		if (requestMap == null) {
			requestMap = new RequestMap(request);
		}
		return requestMap;
	}

	/**
	 * @return Map,将对{@link HttpSession}域内变量的访问封装为Map
	 */
	public Map getSessionMap() {
		if (sessionMap == null) {
			sessionMap = new SessionMap(request);
		}
		return sessionMap;
	}

	/**
	 * @return Map,将对{@link ServletContext}域内变量的访问封装为Map
	 */
	public Map getApplicationMap() {
		if (applicationMap == null) {
			applicationMap = new ApplicationMap(request);
		}
		return applicationMap;
	}

	/**
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @return HttpServletResponse
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * 返回动态属性Map.<br>
	 * <p>
	 * 包括所有的请求参数, 业务处理的结果, 都被存储到了request域,<br>
	 * 该Map封装了对request域内的参数存取.
	 * 
	 * @see ServletRequest#setAttribute(String, Object)
	 * @see ServletRequest#getAttribute(String)
	 * @return Map 动态属性
	 */
	public Map getDynProperties() {
		if (needRebuild) {
			needRebuild = false;
			try {
				buildWrapper();
			} catch (ServletException e) {
				logger.error(e);
			}
		}
		return dynaProperties;
	}

	/**
	 * 根据对象的键值返回对象<br>
	 * 本方法只是简单的从缓存中返回给定键值所对应的对象
	 * 
	 * @see #getDynProperties()
	 * @param key
	 *            对象名
	 * @return Object 对象
	 */
	public Object get(String key) {
		return getDynProperties().get(key);
	}

	/**
	 * 根据对象的键值返回对象,并自动进行类型转换(转换为指定Class).<br>
	 * 本方法适用于将特定的请求参数转换为非Bean对象,例如：{@link Date}、{@link Integer}等.<br>
	 * 如果需要组装到Bean类型,请使用 {@link #registerProperty(String, Class)} 或
	 * {@link #registerProperty(String, Object)}<br>
	 * <br>
	 * 注：
	 * <ul>
	 * <li>clazz必须已经在BeanUtils中注册了相应的Converter,否则:<br>
	 * 在单值情况下将被转换为String,在多值情况下将有可能出现异常.</li>
	 * <li>当参数是多值(String[])时,返回值将是一个clazz[]数组对象(注意不是Object[]).</li>
	 * </ul>
	 * 
	 * @see #get(String)
	 * @param key
	 *            对象名
	 * @param clazz
	 *            要返回的对象类型,当clazz==null时,返回值同{@link #get(String)}
	 * @return Object clazz对象,或者clazz[]数组对象.(异常情况下有可能返回String或者抛出异常)
	 */
	public Object get(String key, Class clazz) {
		Object value = get(key);
		if (clazz == null || value == null || clazz.isInstance(value)) {
			return value;
		}
		// 类型不匹配,尝试类型转换
		Class valueCls = value.getClass();
		if (valueCls.isArray()) {
			if (!clazz.isAssignableFrom(valueCls.getComponentType())) {
				if (value instanceof String[]) {
					value = ConvertUtils.convert((String[]) value, clazz);
				} else {
					value = ConvertUtils.convert(String.valueOf(value), clazz);
				}
			}
		} else {
			value = ConvertUtils.convert(String.valueOf(value), clazz);
		}
		return value;
	}

	/**
	 * 将包装器中与指定name相关的参数组装为给定Class的Bean对象(或对象Array/List),<br>
	 * 并将结果保存在包装器的动态Map中.<br>
	 * 例：
	 * <p>
	 * 组装为POJO,假定如下一组参数:<br>
	 * a.b1={1,2,3} <--数组<br>
	 * a.b2=2<br>
	 * 参数name=a,组装的结果是一个类型为clazz的Object,Object的b1属性是一个数组.
	 * </p>
	 * <p>
	 * 组装为Array,假定如下一组参数:<br>
	 * a[0].b1={1,2,3} <--数组<br>
	 * a[0].b2=2<br>
	 * a[1].b1={11,12,13} <--数组<br>
	 * a[1].b2=22<br>
	 * 参数name=a,组装的结果是一个大小为2的Array,Array中每个元素的b1属性是一个数组.
	 * </p>
	 * <p>
	 * 组装为List,假定如下一组参数:<br>
	 * a(0).b1={1,2,3} <--数组<br>
	 * a(0).b2=2<br>
	 * a(1).b1={11,12,13} <--数组<br>
	 * a(1).b2=22<br>
	 * 参数name=a,组装的结果是一个大小为2的List,List中每个元素的b1属性是一个数组.
	 * </p>
	 * 
	 * @param name
	 *            对象的参数名
	 * @param clazz
	 *            对象类型
	 * @return 组装结果(Bean)
	 */
	public Object registerProperty(String name, Class clazz) {
		try {
			BeanWrapper generator = new BeanWrapper();
			Object obj = generator.populate(name, clazz, getDynProperties());
			set(name, obj);
			return obj;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 注册一个集合类 针对属性做全过滤，仅提取集合属性 对于正常的集合来说，registerProperty方法可以实现提取集合中的内容
	 * 但是对于某些情况，提交的内容不符合完全的规范，例如列表提交过来 的数据中集合与非集合无法定位，所以此方法针对这类情况进行再次过滤。
	 * 
	 * @param name
	 * @param clazz
	 * @return
	 */
	public Object registerCollection(String name, Class clazz) {
		try {
			BeanWrapper generator = new BeanWrapper();
			Map mp = getDynProperties();
			Map newMap = new HashMap();
			Iterator iter = mp.keySet().iterator();
			String key;
			while (iter.hasNext()) {
				key = (String) iter.next();
				if (!StringUtils.isEmpty(key)) {
					if (key.startsWith(name + "[")) {
						newMap.put(key, mp.get(key));
					}
				}
			}
			Object obj = generator.populate(name, clazz, newMap);
			set(name, obj);
			return obj;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 组装对象,作用与 {@link ParameterWrapper#registerProperty(String, Class)}相似.<br>
	 * 该方法的不同之处是: 将请求中提交的参数,附加到给定的已有对象之上.
	 * 
	 * @param name
	 *            对象的参数名
	 * @param destBean
	 *            属性值要组装到的目标Bean
	 * @return 组装结果(Bean)
	 * @see #registerProperty(String, Class)
	 */
	public Object registerProperty(String name, Object destBean) {
		try {
			BeanWrapper generator = new BeanWrapper();
			Object obj = generator.populate(name, destBean, getDynProperties());
			set(name, obj);
			return obj;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 将对象保存到request中,实现request范围内对该对象的共享访问.
	 * 
	 * @see #getDynProperties()
	 * @param key
	 *            对象名
	 * @param value
	 *            对象值
	 */
	public void set(String key, Object value) {
		if (UPLOAD_FILES.equals(key)) {
			if (logger.isErrorEnabled()) {
				logger.error("Forbidden: can't set attribute->" + UPLOAD_FILES);
			}
			return;
		}
		getDynProperties().put(key, value);
	}

	/**
	 * 从request域中移除给定的变量.
	 * 
	 * @see #getDynProperties()
	 * @param key
	 *            对象名
	 */
	public void remove(String key) {
		if (UPLOAD_FILES.equals(key)) {
			if (logger.isErrorEnabled()) {
				logger.error("Forbidden: can't remove attribute->" + UPLOAD_FILES);
			}
			return;
		}
		getDynProperties().remove(key);
	}

	/**
	 * 向包装器中填充一个对象,实现request范围内对该对象的共享访问.
	 * 
	 * @see #set(String, Object)
	 * @param key
	 *            对象名
	 * @param value
	 *            对象值
	 * @param forwardInterceptor
	 */
	public void set(String key, Object value, ForwardInterceptor forwardInterceptor) {
		set(key, value);
		this.forwardInterceptorInvoker = new ForwardInterceptorInvoker(key, forwardInterceptor);
	}

	/**
	 * TODO: Document ME!
	 */
	public void setAdditionalUrlParameters(String additionalUrlParameters) {
		this.urlParameters = additionalUrlParameters;
	}

	/**
	 * TODO: Document ME!
	 */
	public String getAdditionalUrlParameter() {
		return this.urlParameters;
	}

	/**
	 * TODO: Document ME!
	 */
	public ForwardInterceptorInvoker getForwardInterceptorInvoker() {
		return this.forwardInterceptorInvoker;
	}

	/**
	 * 构建包装器: 该方法用于将请求中的数据拷贝到Wrapper中去.<br>
	 * 
	 * @throws ServletException
	 */
	public void buildWrapper() throws ServletException {
		try {
			// 文本参数处理
			Enumeration names = request.getParameterNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				if ((name.startsWith("org.apache.struts."))) {
					continue;
				}
				Object value = request.getParameterValues(name);
				if (value == null) {
					continue;
				}
				Class valueCls = value.getClass();
				if (valueCls.isArray() && valueCls.getComponentType() == java.lang.String.class && ((String[]) value).length == 1) {
					set(name, ((String[]) value)[0]);
				} else {
					set(name, value);
				}
			}
			// 上传文件处理
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Map uploadFiles = (Map) get(UPLOAD_FILES);
				uploadFiles.putAll(multipartRequest.getFileMap());
			}
		} catch (Throwable t) {
			throw new ServletException(t);
		}
	}

	public String getViewForwardPath() {
		return this.viewForwardPath;
	}

	public void setViewForwardPath(String forwardPath) {
		this.viewForwardPath = forwardPath;
	}

	public Object getUploadFiles(String key) {
		Map uploadFiles = (Map) get(ParameterWrapper.UPLOAD_FILES);
		if (uploadFiles != null) {
			return uploadFiles.get(key);
		}
		return null;
	}

	// public List<?> registerObjectList(String name, Class clazz) {
	// try {
	// BeanWrapper generator = new BeanWrapper();
	// Map mp = getDynProperties();
	// List<?> result = new ArrayList<T>();
	// Iterator iter = mp.keySet().iterator();
	// String key;
	// while (iter.hasNext()) {
	// key = (String) iter.next();
	// if (!StringUtils.isEmpty(key) && key.startsWith(name + ".") &&
	// key.contains("[") && key.endsWith("]")) {
	// newMap.put(key, mp.get(key));
	// }
	// }
	// Object obj = generator.populate(name, clazz, newMap);
	// set(name, obj);
	// return obj;
	// } catch (Exception e) {
	// logger.error(e);
	// }
	// return null;
	// }

}
