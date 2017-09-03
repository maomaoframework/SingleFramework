package huxg.framework.filter.parameter;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用来对request、reponse中的Cookie封装访问
 * 
 * @author 胡晓光
 */
public class CookieMap extends AbstractHttpMap {

    protected HttpServletRequest request;
    protected HttpServletResponse reponse;

    public CookieMap(HttpServletRequest request, HttpServletResponse reponse) {
        this.request = request;
        this.reponse = reponse;
    }

    /**
     * 返回request域中所有Cookie(浏览器提交给服务器的)的名称枚举
     */
    protected Enumeration getNames() {
        return new Enumeration() {

            protected int pos = -1;
            protected boolean hasMore = false;

            public boolean hasMoreElements() {
                pos++;
                Cookie[] cookies = CookieMap.this.request.getCookies();
                hasMore = (cookies != null && cookies.length > pos);
                return hasMore;
            }

            public Object nextElement() {
                if (hasMore) {
                    Cookie[] cookies = CookieMap.this.request.getCookies();
                    return cookies[pos];
                }
                return null;
            }

        };
    }

    /**
     * 从request中获取Cookie(浏览器提交给服务器的)
     * 
     * @see javax.servlet.http.HttpServletRequest#getCookies()
     */
    public Object get(Object key) {
        if (key == null) {
            return null;
        }

        String sKey = String.valueOf(key);
        Cookie[] cookies = CookieMap.this.request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (sKey.equals(cookies[i].getName())) {
                return cookies[i].getValue();
            }
        }
        return null;
    }

    /**
     * 将Cookie添加到response中去(由服务器返回给浏览器)
     * 
     * @see javax.servlet.http.HttpServletResponse#addCookie(Cookie cookie)
     */
    public Object put(Object key, Object m) {
        if (key == null || m == null) {
            return null;
        }

        reponse.addCookie(new Cookie(String.valueOf(key), String.valueOf(m)));
        return null;
    }

    /**
     * 不支持该方法
     * 
     * @throws java.lang.UnsupportedOperationException
     */
    public Object remove(Object key) {
        throw new UnsupportedOperationException(getClass().getName());
    }

}
