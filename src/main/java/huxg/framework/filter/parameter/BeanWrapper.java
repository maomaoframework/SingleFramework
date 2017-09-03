package huxg.framework.filter.parameter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeanWrapper {

	private static final Log logger = LogFactory.getLog(BeanWrapper.class);

	/** 错误类型 */
	public static final int TypeError = -1;
	/** 未设置类型 */
	public static final int TypeUndefined = 0;
	/** Pojo类型 */
	public static final int TypePojo = 1;
	/** 集合类型 */
	public static final int TypeList = 2;
	/** 数组类型 */
	public static final int TypeArray = 3;

	/**
	 * 属性名应该满足的规则(正则)
	 */
	protected static final Pattern propPattern = Pattern
			.compile("^[\\w&&[^\\d]]\\w*");

	/** Bean类型 */
	protected int beanType = TypeUndefined;
	/** Bean的参数名 */
	protected String beanName = null;
	/** 要组装成的Bean类 */
	protected Class beanClazz = null;
	/** 要组装到的Bean对象 */
	protected Object destBean = null;

	/** 缓存参数值，用于populate */
	protected Map properties = null;
	protected List lstProperties = null;

	public BeanWrapper() {
	}

	public static String getBeanTypeName(int beanType) {
		switch (beanType) {
		case -1:
			return "<Error>";
		case 0:
			return "<Undefined>";
		case 1:
			return "<Pojo>";
		case 2:
			return "<List>";
		case 3:
			return "<Array>";
		default:
			return "Unknown";
		}
	}

	public String getBeanName() {
		return this.beanName;
	}

	public int getBeanType() {
		return this.beanType;
	}

	protected Map getContainerProperties(ParameterDescriptor pd)
			throws BeanPopulateException {
		if (beanType == TypeUndefined) {
			beanType = pd.getBeanType();
		} else {
			if (beanType != pd.getBeanType()) {
				if (logger.isDebugEnabled()){
					logger.error(new BeanPopulateException(
							"Bean类型不一致,请检查相关的参数名:\nbeanType="
							+ getBeanTypeName(beanType) + ", paramName='"
							+ pd.getParamName() + "', paramBeanType="
							+ getBeanTypeName(pd.getBeanType())));
				}
				return null;
				// return
//				throw new BeanPopulateException(
//						"Bean类型不一致,请检查相关的参数名:\nbeanType="
//								+ getBeanTypeName(beanType) + ", paramName='"
//								+ pd.getParamName() + "', paramBeanType="
//								+ getBeanTypeName(pd.getBeanType()));
			}
		}
		if (beanType == TypePojo) {
			if (properties == null) {
				properties = new HashMap();
			}
			return properties;
		}
		if (beanType == TypeArray || beanType == TypeList) {
			if (lstProperties == null) {
				lstProperties = new ArrayList();
			}
			int size = lstProperties.size();
			while (size <= pd.getIndex()) {
				lstProperties.add(null);
				size++;
			}
			Map map = (Map) lstProperties.get(pd.getIndex());
			if (map == null) {
				map = new HashMap();
				lstProperties.set(pd.getIndex(), map);
			}
			return map;
		}
		return null;
	}

	protected void addProperty(String paramName, Object value)
			throws BeanPopulateException {
		ParameterDescriptor pd = ParameterDescriptor.build(this.beanName,
				paramName);
		if (pd == null) {
			return;
		}
		if (pd.getBeanType() == TypeError) {
			// return
			throw new BeanPopulateException("Bean类型无法确定,请检查参数名:"
					+ pd.getParamName());
		}
		if (pd.getProp() == null) {
			// return
			throw new BeanPopulateException("Bean属性名无法提取,请检查参数名:"
					+ pd.getParamName());
		}
		Matcher matcher = propPattern.matcher(pd.getProp());
		if (!matcher.matches()) {
			// return
			if (logger.isErrorEnabled()) {
				logger.error("属性名不符合变量命名规则->" + pd.getProp());
			}
			throw new BeanPopulateException("Bean属性名错误,请检查参数名:"
					+ pd.getParamName());
		}
		
		Map propMap = getContainerProperties(pd);
		if (propMap != null)
			propMap.put(pd.getProp(), value);
	}

	protected void buildWraper(Map params) throws BeanPopulateException {
		// clean
		// 解析
		Iterator iter = params.keySet().iterator();
		String key = null;
		while (iter.hasNext()) {
			key = (String) iter.next();
			if (logger.isDebugEnabled()) {
				logger.debug("addProperty->" + key);
			}
			addProperty(key, params.get(key));
		}
	}

	protected Object populate() throws Throwable {
		if (this.beanType == TypePojo) {
			if (this.properties == null || this.properties.isEmpty()) {
				return null;
			}
			Object bean = this.destBean;
			if (bean == null) {
				// new
				bean = ConstructorUtils.invokeConstructor(this.beanClazz, null);
			}
			BeanUtils.populate(bean, properties);
			// clean
			this.properties.clear();
			return bean;
		}
		if (this.beanType == TypeArray || this.beanType == TypeList) {
			if (lstProperties.size() == 0) {
				return null;
			}
			List lstBean = new ArrayList();
			Map propertiesMap = null;
			Object bean = null;

			Iterator iter = lstProperties.iterator();
			while (iter.hasNext()) {
				propertiesMap = (Map) iter.next();
				if (propertiesMap == null || propertiesMap.isEmpty()) {
					lstBean.add(null);
					continue;
				}
				bean = ConstructorUtils.invokeConstructor(this.beanClazz, null);
				BeanUtils.populate(bean, propertiesMap);
				lstBean.add(bean);
				// clean
				propertiesMap.clear();
			}
			if (this.beanType == TypeArray) {
				Object[] array = (Object[]) Array.newInstance(this.beanClazz,
						lstBean.size());
				return lstBean.toArray(array);
			}
			return lstBean;
		}
		return null;
	}

	public Object populate(String name, Object bean, Map params) {
		this.beanName = name;
		this.destBean = bean;
		this.beanType = TypePojo;
		if (this.beanName == null || this.destBean == null || params == null
				|| params.isEmpty()) {
			return null;
		}
		try {
			buildWraper(params);
			return populate();
		} catch (BeanPopulateException e) {
			logger.error(e);
		} catch (Throwable e) {
			logger.error("", e);
		} finally {
			// clean
			release();
		}
		return null;
	}

	public Object populate(String name, Class clazz, Map params) {
		this.beanName = name;
		this.beanClazz = clazz;
		if (this.beanName == null || this.beanClazz == null || params == null
				|| params.isEmpty()) {
			return null;
		}
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("buildWraper start->" + this.beanName);
			}
			buildWraper(params);
			if (logger.isDebugEnabled()) {
				logger.debug("buildWraper complete->" + this.beanName);
				logger.debug("populate start->" + this.beanName);
			}
			Object obj = populate();
			if (logger.isDebugEnabled()) {
				logger.debug("populate complete->" + this.beanName);
				logger.debug("populate result->" + this.beanName + ": " + obj);
			}
			return obj;
		} catch (BeanPopulateException e) {
			logger.error(e);
		} catch (Throwable e) {
			logger.error("", e);
		} finally {
			// clean
			release();
		}
		return null;
	}

	public void release() {
		beanType = TypeUndefined;
		beanName = null;
		beanClazz = null;
		destBean = null;
		if (properties != null) {
			properties.clear();
		}
		if (lstProperties != null) {
			lstProperties.clear();
		}
	}

}
