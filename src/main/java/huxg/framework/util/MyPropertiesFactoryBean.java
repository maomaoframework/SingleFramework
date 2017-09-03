package huxg.framework.util;

import java.net.MalformedURLException;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class MyPropertiesFactoryBean extends PropertiesFactoryBean{
    private String[] configurations;
    private static final String PREFIX = "${";
    private static final String SUFFIX = "}";
	public void setConfigurations(String[] values) throws MalformedURLException {
	    this.configurations = values;
	    if (values == null) return;
	    Resource[] resources = new Resource[values.length];
	    for (int i = 0; i < values.length; i++) {
	        String config = convert2UrlFormat(values[i]);
	        resources[i] = getResource(config);
	    }
	    super.setLocations(resources);
	}
	private Resource getResource(String config) throws MalformedURLException {
        Resource r = null;
        int cpIndex = config.indexOf("classpath:");
        if (cpIndex!=-1) {
            r = new ClassPathResource(config.substring(cpIndex+"classpath:".length()));
        }else{
            r = new UrlResource(config);
        }
        return r;

    }

    public String[] getConfigurations() {
        return configurations;
    }

    private String convert2UrlFormat(String originalPlaceholderToUse) {
        if (originalPlaceholderToUse == null) return null;
        StringBuffer buf = new StringBuffer(originalPlaceholderToUse);
        int startIndex = buf.indexOf(PREFIX);
        while (startIndex != -1) {
            int endIndex = buf.indexOf(
                    SUFFIX, startIndex + PREFIX.length());
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
}
