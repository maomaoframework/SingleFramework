package huxg.framework.interceptor;

import org.hibernate.EmptyInterceptor;

public class Test extends EmptyInterceptor{
	@Override
	public String onPrepareStatement(String sql) {
		return super.onPrepareStatement(sql);
	}
}
