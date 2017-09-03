package huxg.framework.filter.parameter;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author
 * @version 1.0
 * @date 2007-9-30 11:49:10
 */
public class ForwardInterceptorInvoker {
	private String paramKey;

	private ForwardInterceptor interceptor;

	public ForwardInterceptorInvoker(String paramKey,
			ForwardInterceptor interceptor) {
		this.paramKey = paramKey;
		this.interceptor = interceptor;
	}

	public Object pack() throws Exception {
		return interceptor.pack(paramKey, ParameterWrapper.getWrapper()
				.getRequest(), ParameterWrapper.getWrapper().getResponse());
	}
	
	public void forward()throws Exception {
		interceptor.forward(paramKey, ParameterWrapper.getWrapper()
				.getRequest(), ParameterWrapper.getWrapper().getResponse());
	}
}
