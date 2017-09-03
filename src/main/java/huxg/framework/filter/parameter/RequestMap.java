package huxg.framework.filter.parameter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * 对request作用域内的Attribute封装访问
 * 
 * @author 胡晓光
 */
public class RequestMap extends AbstractHttpMap {

    protected HttpServletRequest request;
    protected Set keySet;

    public RequestMap(HttpServletRequest request) {
        this.request = request;
        this.keySet = new HashSet();
        Enumeration emu = request.getParameterNames();
        while (emu.hasMoreElements()) {
            keySet.add(emu.nextElement());
        }
    }

    protected Enumeration getNames() {
        return new Enumeration() {

            private Iterator iter = keySet.iterator();

            public boolean hasMoreElements() {
                return iter.hasNext();
            }

            public Object nextElement() {
                return iter.next();
            }

        };
    }

    public boolean containsKey(Object key) {
        return keySet.contains(key);
    }

    public int size() {
        return keySet.size();
    }

    public Set keySet() {
        return Collections.unmodifiableSet(keySet);
    }

    public void putAll(Map m) {
        Iterator iter = m.entrySet().iterator();
        Map.Entry e;
        while (iter.hasNext()) {
            e = (Entry) iter.next();
            put(e.getKey(), e.getValue());
        }
    }

    public Object get(Object key) {
        if (key == null) {
            return null;
        }

        String sKey = String.valueOf(key);
        Object value = request.getAttribute(sKey);
        if (value != null) {
            return value;
        }
        String[] values = request.getParameterValues(sKey);
        if (values != null) {
            if (values.length == 1) {
                return values[0];
            }
            if (values.length > 1) {
                return values;
            }
        }
        return null;
    }

    public Object put(Object key, Object value) {
        if (key == null) {
            return null;
        }

        String sKey = String.valueOf(key);
        // 同步修改keySet
        if (value == null) {
            if (request.getParameter(sKey) == null) {
                keySet.remove(sKey);
            }
        } else if (!keySet.contains(sKey)) {
            keySet.add(sKey);
        }

        Object oldValue = request.getAttribute(sKey);
        // value==null 时将调用 removeAttribute.
        request.setAttribute(sKey, value);
        return oldValue;
    }

    public Object remove(Object key) {
        if (key == null) {
            return null;
        }
        String sKey = String.valueOf(key);
        // 同步修改keySet
        keySet.remove(sKey);

        Object oldValue = request.getAttribute(sKey);
        request.removeAttribute(sKey);
        return oldValue;
    }

}
