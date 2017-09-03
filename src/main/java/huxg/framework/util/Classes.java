package huxg.framework.util;

/** 
 * <p> 
 * <font color="red">依赖javassit</font>的工具类，获取方法的参数名 
 * </p> 
 * 
 * @author dixingxing 
 * @date Apr 20, 2012 
 */

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class Classes {
	private Classes() {
	}

	/**
	 * 
	 * <p>
	 * 获取方法参数名称
	 * </p>
	 * 
	 * @param cm
	 * @return
	 */
	protected static String[] getMethodParamNames(CtMethod cm) throws Exception {
		CtClass cc = cm.getDeclaringClass();
		MethodInfo methodInfo = cm.getMethodInfo();
		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
		if (attr == null) {
			throw new Exception("属性不能为空");
		}

		String[] paramNames = null;
		try {
			paramNames = new String[cm.getParameterTypes().length];
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
		for (int i = 0; i < paramNames.length; i++) {
			paramNames[i] = attr.variableName(i + pos);
		}
		return paramNames;
	}

	/**
	 * 获取方法参数名称，按给定的参数类型匹配方法
	 * 
	 * @param clazz
	 * @param method
	 * @param paramTypes
	 * @return
	 */
	public static String[] getMethodParamNames(Class<?> clazz, String method, Class<?>... paramTypes) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = null;
		CtMethod cm = null;
		try {
			cc = pool.get(clazz.getName());

			String[] paramTypeNames = new String[paramTypes.length];
			for (int i = 0; i < paramTypes.length; i++)
				paramTypeNames[i] = paramTypes[i].getName();

			cm = cc.getDeclaredMethod(method, pool.get(paramTypeNames));
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return getMethodParamNames(cm);
	}

	/**
	 * 获取方法参数名称，匹配同名的某一个方法
	 * 
	 * @param clazz
	 * @param method
	 * @return
	 * @throws NotFoundException
	 *             如果类或者方法不存在
	 * @throws MissingLVException
	 *             如果最终编译的class文件不包含局部变量表信息
	 */
	public static String[] getMethodParamNames(Class<?> clazz, String method) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath(clazz));
		
		CtClass cc;
		CtMethod cm = null;
		try {
			cc = pool.get(clazz.getName());
			cm = cc.getDeclaredMethod(method);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return getMethodParamNames(cm);
	}
	
	public static void main(String [] args) throws Exception {
		// String [] parameters = getMethodParamNames(CmsManager.Client.class,
		// "getChannelsForPagelit");
		// System.out.println(parameters);
	}
}
