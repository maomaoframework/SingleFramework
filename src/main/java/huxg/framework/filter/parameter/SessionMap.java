package huxg.framework.filter.parameter;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 对Session作用域内的对象进行包装的类
 * 
 * @author 胡晓光
 */
public class SessionMap extends AbstractHttpMap {

    protected HttpSession session;

    public SessionMap(HttpServletRequest request) {
        this.session = request.getSession();
    }

    protected Enumeration getNames() {
        return session.getAttributeNames();
    }

    public Object get(Object key) {
        if (key == null) {
            return null;
        }

        return session.getAttribute(String.valueOf(key));
    }

    public Object put(Object key, Object value) {
        if (key == null) {
            return null;
        }

        String sKey = String.valueOf(key);
        Object oldValue = session.getAttribute(sKey);
        session.setAttribute(sKey, value);
        return oldValue;
    }

    public Object remove(Object key) {
        if (key == null) {
            return null;
        }

        String sKey = String.valueOf(key);
        Object oldValue = session.getAttribute(sKey);
        session.removeAttribute(sKey);
        return oldValue;
    }

}
