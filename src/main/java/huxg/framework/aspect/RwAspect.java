package huxg.framework.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import huxg.framework.annotation.MasterDataSource;
import huxg.framework.annotation.SlaveDataSource;
import huxg.framework.context.Context;
import huxg.framework.context.ThreadVariable;
import huxg.framework.datasource.ReadWriteDataSourceDecision;
import huxg.framework.service.BaseService;
import huxg.framework.util.StringUtils;

@Component
@Aspect
public class RwAspect implements Ordered {
	private String[] readMethods = new String[] { "find*", "count*", "get*",
			"query*", "list*" ,"jfinalFind*", "jfinalCount*", "jfinalGet*",
			"jfinalQuery*", "jfinalList*"};

	@Override
	public int getOrder() {
		return -2147483648;
	}

	@Around("huxg.framework.aspect.NamePointcut.rw()")
	public Object joinPointCache(ProceedingJoinPoint pjp) throws Throwable {
		Object proxy = pjp.getThis();// 代理对象

		Class<?> proxyClass = proxy.getClass();// 代理类

		Object object = pjp.getTarget();

		Class<?> cls = pjp.getTarget().getClass();// 目标类

		MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();

		Method method = joinPointObject.getMethod();// 目标方法

		Object[] params = pjp.getArgs();// 方法参数

		String methodName = getMethodName(cls,method, params);
		try {
			if (object instanceof BaseService) {// assert not null
				
				if(null==ThreadVariable.getFirstMethod()){
					ThreadVariable.setFirstMethod(methodName);
				}
				
				if(ThreadVariable.getLastTopContext()!=null){
					ThreadVariable.setThreadMap(methodName,ThreadVariable.getLastTopContext());
				}
				
				if (method.isAnnotationPresent(SlaveDataSource.class)) {
					SlaveDataSource slaveDataSource = method
							.getAnnotation(SlaveDataSource.class);
					String dataSourceName = slaveDataSource.dataSourceName();
					
					ThreadVariable.setLastTopContext(new Context(true));
					ReadWriteDataSourceDecision.markRead();
					if (StringUtils.isNotEmptyString(dataSourceName)) {
						ThreadVariable.setDataSourceName(dataSourceName);
						ThreadVariable.setLastTopContext(new Context(true, dataSourceName));
					}
				}else if(method.isAnnotationPresent(MasterDataSource.class)){
					ThreadVariable.setLastTopContext(new Context(false));
					ReadWriteDataSourceDecision.markWrite();// default
				}else {
					boolean flag = isChoiceReadDB(method.getName());
					if (flag) {
						ThreadVariable.setLastTopContext(new Context(true));
						ReadWriteDataSourceDecision.markRead();
					} else {
						ThreadVariable.setLastTopContext(new Context(false));
						ReadWriteDataSourceDecision.markWrite();// default
					}
				}
			}
			
			
			Object returnObject = pjp.proceed();

			if (object instanceof BaseService) {//还原
				Map<String, Context> threadMap = ThreadVariable
						.getThreadMap();
				if (threadMap != null) {
					Context topContext = threadMap.get(methodName);
					if (topContext!=null&&topContext.isRead()) {
						ReadWriteDataSourceDecision.markRead();
						if (StringUtils.isNotEmptyString(topContext
								.getDataSourceName())) {
							ThreadVariable.setDataSourceName(topContext
									.getDataSourceName());
						}
					}else{
						ReadWriteDataSourceDecision.markWrite();
					}
				}
			}
			
			return returnObject;
		} catch (Throwable e) {
			throw e;
		} finally {
			if (methodName.equals(ThreadVariable.getFirstMethod())) {
				ReadWriteDataSourceDecision.reset();
				ThreadVariable.removeFirstMethod();
				ThreadVariable.removeDataSourceName();
				ThreadVariable.removeThreadMap();
				ThreadVariable.removeLastTopContext();
			}
		}
	}

	private boolean isChoiceReadDB(String methodName) {
		String bestNameMatch = null;
		for (String mappedName : readMethods) {
			if (isMatch(methodName, mappedName)) {
				bestNameMatch = mappedName;
				break;
			}
		}
		if (StringUtils.isNotEmptyString(bestNameMatch)) {
			return true;
		}
		return false;
	}

	protected boolean isMatch(String methodName, String mappedName) {
		return PatternMatchUtils.simpleMatch(mappedName, methodName);
	}

	private String getMethodName(Class cls,Method method, Object[] params) {
		return cls.getName() + "-" + method.getName() + "-" + Arrays.toString(params);
	}

}
