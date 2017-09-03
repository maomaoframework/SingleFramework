package huxg.framework.aspect;

import org.aspectj.lang.annotation.Pointcut;

/**
 * 切点统一在此管理
 * 
 * @author 
 *
 */
public class NamePointcut {
	@Pointcut("@within(huxg.framework.annotation.Cachable)")
	public void cacheCut(){}
	
	@Pointcut("@within(huxg.framework.annotation.OtherCachable)")
	public void otherCacheCut(){}
	
	@Pointcut("@within(huxg.framework.annotation.Rwable)")
	public void rw(){};
	
	@Pointcut("@within(huxg.framework.annotation.ClsRw)")
	public void clsRw(){};

}
