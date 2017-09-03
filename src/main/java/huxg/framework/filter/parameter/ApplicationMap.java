package huxg.framework.filter.parameter;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * 用来对ServletContext域内的资源封装访问
 * 
 * @author 胡晓光
 */
public class ApplicationMap extends AbstractHttpMap {

    protected ServletContext context;

    public ApplicationMap(HttpServletRequest request) {
        context = request.getSession().getServletContext();
    }

    protected Enumeration getNames() {
        return context.getAttributeNames();
    }

    public Object get(Object key) {
        if (key == null) {
            return null;
        }

        return context.getAttribute(String.valueOf(key));
    }

    public Object put(Object key, Object value) {
        if (key == null) {
            return null;
        }

        String sKey = String.valueOf(key);
        Object oldValue = context.getAttribute(sKey);
        context.setAttribute(sKey, value);
        return oldValue;
    }

    public Object remove(Object key) {
        if (key == null) {
            return null;
        }

        String sKey = String.valueOf(key);
        Object oldValue = context.getAttribute(sKey);
        context.removeAttribute(sKey);
        return oldValue;
    }
}
