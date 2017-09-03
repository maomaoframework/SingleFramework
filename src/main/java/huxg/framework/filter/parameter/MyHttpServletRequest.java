package huxg.framework.filter.parameter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class MyHttpServletRequest extends HttpServletRequestWrapper {

    public MyHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    public void setRequest(ServletRequest request) {
        super.setRequest(request);
        ParameterWrapper.fireRebuild();
    }
}
