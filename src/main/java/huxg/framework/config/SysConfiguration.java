package huxg.framework.config;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.PropertyPlaceholderHelper;

import huxg.framework.util.SpringUtils;

/**
 * 属性配置文件管理扩展：主要是为了解决平台中多个项目，属性配置文件管理重复，分散，难以维护 以及多种环境（开发、测试、生产甚至更多）的配置冲突问题
 * 
 * @author
 * 
 *         v 1.0
 */
public class SysConfiguration extends PropertyPlaceholderConfigurer {
	private static Map<String, String> properties = new HashMap<String, String>();

	private String[] configurations;
	private static final String PREFIX = "${";
	private static final String SUFFIX = "}";
	private Map<String, Resource> resourceMapping = new HashMap<String, Resource>();

	public void setConfigurations(String[] values) throws MalformedURLException {
		this.configurations = values;
		if (values == null)
			return;
		Resource[] resources = new Resource[values.length];
		for (int i = 0; i < values.length; i++) {
			String config = convert2UrlFormat(values[i]);
			Resource resource = getResource(config);
			resources[i] = resource;
			resourceMapping.put(config.substring(config.lastIndexOf("/"), config.length()), resource);
		}
		super.setLocations(resources);
	}

	public Resource getResourceByPath(String path) {
		return resourceMapping.get(path);
	}

	private Resource getResource(String config) throws MalformedURLException {
		Resource r = null;
		int cpIndex = config.indexOf("classpath:");
		if (cpIndex != -1) {
			r = new ClassPathResource(config.substring(cpIndex + "classpath:".length()));
		} else {
			r = new UrlResource(config);
		}
		return r;

	}

	public String[] getConfigurations() {
		return configurations;
	}

	private String convert2UrlFormat(String originalPlaceholderToUse) {
		if (originalPlaceholderToUse == null)
			return null;
		StringBuffer buf = new StringBuffer(originalPlaceholderToUse);
		int startIndex = buf.indexOf(PREFIX);
		while (startIndex != -1) {
			int endIndex = buf.indexOf(SUFFIX, startIndex + PREFIX.length());
			if (endIndex != -1) {
				String placeholder = buf.substring(startIndex + PREFIX.length(), endIndex);
				String propVal = resolvePlaceholder(placeholder);
				if (propVal != null) {
					buf.replace(startIndex, endIndex + SUFFIX.length(), propVal);
					startIndex = buf.toString().indexOf(PREFIX, startIndex + propVal.length());
				} else {
					startIndex = buf.toString().indexOf(PREFIX, endIndex + SUFFIX.length());
				}
			} else {
				startIndex = -1;
			}
		}
		return buf.toString();
	}

	private String resolvePlaceholder(String placeholder) {
		String value = System.getProperty(placeholder);
		if (value == null) {
			value = System.getenv(placeholder);
		}
		return value;
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		if (properties == null || properties.size() == 0) {
			// cache the properties
			PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(DEFAULT_PLACEHOLDER_PREFIX, DEFAULT_PLACEHOLDER_SUFFIX, DEFAULT_VALUE_SEPARATOR, false);
			for (Entry<Object, Object> entry : props.entrySet()) {
				String stringKey = String.valueOf(entry.getKey());
				String stringValue = String.valueOf(entry.getValue());
				stringValue = helper.replacePlaceholders(stringValue, props);
				properties.put(stringKey, stringValue);
			}
		}
		super.processProperties(beanFactoryToProcess, props);
	}

	public static SysConfiguration getInstance() {
		return (SysConfiguration) SpringUtils.getBean("placeholderConfig");
	}

	public static Map<String, String> getProperties() {
		return properties;
	}

	public static String getProperty(String key) {
		return properties.get(key);
	}
}