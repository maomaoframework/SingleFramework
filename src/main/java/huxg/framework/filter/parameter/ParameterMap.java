package huxg.framework.filter.parameter;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * 对request中提交的参数封装访问
 * 
 * @author 胡晓光
 */
public class ParameterMap extends AbstractHttpMap {

    protected HttpServletRequest request;

    public ParameterMap(HttpServletRequest request) {
        this.request = request;
    }

    protected Enumeration getNames() {
        return request.getParameterNames();
    }

    public Object get(Object key) {
        if (key == null) {
            return null;
        }

        return request.getParameter(String.valueOf(key));
    }

    /**
     * 返回key对应的所有值
     * 
     * @Parameters: key the key whose associated value is to be returned
     * @Returns: the values to which the specified key is mapped, or null if
     *           this map contains no mapping for the key
     */
    public Object[] getValues(Object key) {
        if (key == null) {
            return null;
        }

        return request.getParameterValues(String.valueOf(key));
    }

    public Object put(Object arg0, Object arg1) {
        throw new UnsupportedOperationException(
            "Cannot put value to ParameterMap.");
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException(
            "Cannot remove value from ParameterMap.");
    }
}
