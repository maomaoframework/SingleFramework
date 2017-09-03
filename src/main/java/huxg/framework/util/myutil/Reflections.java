package huxg.framework.util.myutil;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import huxg.framework.util.StringUtils;

/**
 * 反射工具类.
 * 
 * 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 * 
 * @author
 */
public class Reflections {
	public static final String SETTER_PREFIX = "set";

	public static final String GETTER_PREFIX = "get";

	public static final String INNER_CLASS_REGEX_SEPARATOR = "\\$";

	public static final String INNER_CLASS_SEPARATOR = "$";

	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	private static Logger logger = LoggerFactory.getLogger(Reflections.class);

	/** 获取指定包名下的所有类 **/
	public static List<Class<?>> getClassList(String packageName, boolean isRecursive) {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		try {
			Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (url != null) {
					String protocol = url.getProtocol();
					if (protocol.equals("file")) {
						String packagePath = url.getPath().replaceAll("%20", " ");
						addClass(classList, packagePath, packageName, isRecursive);
					} else if (protocol.equals("jar")) {
						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
						JarFile jarFile = jarURLConnection.getJarFile();
						Enumeration<JarEntry> jarEntries = jarFile.entries();
						while (jarEntries.hasMoreElements()) {
							JarEntry jarEntry = jarEntries.nextElement();
							String jarEntryName = jarEntry.getName();
							if (jarEntryName.endsWith(".class")) {
								String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
								if (isRecursive || className.substring(0, className.lastIndexOf(".")).equals(packageName)) {
									classList.add(Class.forName(className));
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取类出错！", e);
			throw new RuntimeException(e);
		}
		return classList;
	}

	/** 获取指定包名下指定父类的所有类 **/
	public static List<Class<?>> getClassListBySuper(String packageName, Class<?> superClass) {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		try {
			Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (url != null) {
					String protocol = url.getProtocol();
					if (protocol.equals("file")) {
						String packagePath = url.getPath();
						addClassBySuper(classList, packagePath, packageName, superClass);
					} else if (protocol.equals("jar")) {
						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
						JarFile jarFile = jarURLConnection.getJarFile();
						Enumeration<JarEntry> jarEntries = jarFile.entries();
						while (jarEntries.hasMoreElements()) {
							JarEntry jarEntry = jarEntries.nextElement();
							String jarEntryName = jarEntry.getName();
							if (jarEntryName.endsWith(".class")) {
								String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
								Class<?> cls = Class.forName(className);
								if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
									classList.add(cls);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取类出错！", e);
			throw new RuntimeException(e);
		}
		return classList;
	}

	private static void addClassBySuper(List<Class<?>> classList, String packagePath, String packageName, Class<?> superClass) {
		try {
			File[] files = getClassFiles(packagePath);
			if (files != null) {
				for (File file : files) {
					String fileName = file.getName();
					if (file.isFile()) {
						String className = getClassName(packageName, fileName);
						Class<?> cls = Class.forName(className);
						if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
							classList.add(cls);
						}
					} else {
						String subPackagePath = getSubPackagePath(packagePath, fileName);
						String subPackageName = getSubPackageName(packageName, fileName);
						addClassBySuper(classList, subPackagePath, subPackageName, superClass);
					}
				}
			}
		} catch (Exception e) {
			logger.error("添加类出错！", e);
			throw new RuntimeException(e);
		}
	}

	private static void addClass(List<Class<?>> classList, String packagePath, String packageName, boolean isRecursive) {
		try {
			File[] files = getClassFiles(packagePath);
			if (files != null) {
				for (File file : files) {
					String fileName = file.getName();
					if (file.isFile()) {
						String className = getClassName(packageName, fileName);
						classList.add(Class.forName(className));
					} else {
						if (isRecursive) {
							String subPackagePath = getSubPackagePath(packagePath, fileName);
							String subPackageName = getSubPackageName(packageName, fileName);
							addClass(classList, subPackagePath, subPackageName, true);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("添加类出错！", e);
			throw new RuntimeException(e);
		}
	}

	private static File[] getClassFiles(String packagePath) {
		return new File(packagePath).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
			}
		});
	}

	private static String getClassName(String packageName, String fileName) {
		String className = fileName.substring(0, fileName.lastIndexOf("."));
		if (StringUtils.isNotEmptyString(packageName)) {
			className = packageName + "." + className;
		}
		return className;
	}

	private static String getSubPackagePath(String packagePath, String filePath) {
		String subPackagePath = filePath;
		if (StringUtils.isNotEmptyString(packagePath)) {
			subPackagePath = packagePath + "/" + subPackagePath;
		}
		return subPackagePath;
	}

	private static String getSubPackageName(String packageName, String filePath) {
		String subPackageName = filePath;
		if (StringUtils.isNotEmptyString(packageName)) {
			subPackageName = packageName + "." + subPackageName;
		}
		return subPackageName;
	}

	/** 定位method */
	public static Method getMethod(Object obj, String method, Object[] params) {
		Class[] paramClass = getClasses(params);
		Method m = null;
		try {
			m = obj.getClass().getMethod(method, paramClass);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
		return m;
	}

	public static Class[] getClasses(Object[] objects) {
		if (objects == null) {
			return null;
		}
		Class[] result = new Class[objects.length];
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				result[i] = objects[i].getClass();
			}
		}
		return result;
	}

	public static String getterMethodName(String propertyName) {
		return GETTER_PREFIX + org.apache.commons.lang.StringUtils.capitalize(propertyName);
	}

	public static String setterMethodName(String propertyName) {
		return SETTER_PREFIX + org.apache.commons.lang.StringUtils.capitalize(propertyName);
	}

	/**
	 * 调用Getter方法.
	 */
	public static Object invokeGetter(Object obj, String propertyName) {
		String getterMethodName = GETTER_PREFIX + org.apache.commons.lang.StringUtils.capitalize(propertyName);
		return invokeMethod(obj, getterMethodName, new Class[] {}, new Object[] {});
	}

	/**
	 * 调用Setter方法, 仅匹配方法名。
	 */
	public static void invokeSetter(Object obj, String propertyName, Object value) {
		String setterMethodName = SETTER_PREFIX + org.apache.commons.lang.StringUtils.capitalize(propertyName);
		invokeMethodByName(obj, setterMethodName, new Object[] { value });
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用. 同时匹配方法名+参数类型，
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符，
	 * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
	 * 只匹配函数名，如果有多个同名函数调用第一个。
	 */
	public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
		Method method = getAccessibleMethodByName(obj, methodName);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notEmpty(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {// NOSONAR
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/** 循环向上转型 找filed 如果没有 返回 null */
	public static Field getField(final Class cls, final String fieldName) {
		Validate.notNull(cls, "cls can't be null");
		Validate.notEmpty(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = cls; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				return field;
			} catch (NoSuchFieldException e) {// NOSONAR
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
	 * 匹配函数名+参数类型。
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
	 * args)
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName, final Class<?>... parameterTypes) {
		Validate.notNull(obj, "object can't be null");
		Validate.notEmpty(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			try {
				Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	public static Method getMethod(final Object obj, final String methodName, final Class<?>... parameterTypes) {
		Validate.notNull(obj, "object can't be null");
		Validate.notEmpty(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			try {
				Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
				return method;
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null. 只匹配函数名。
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
	 * args)
	 */
	public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notEmpty(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					makeAccessible(method);
					return method;
				}
			}
		}
		return null;
	}

	public static Method getMethodByName(final Object obj, final String methodName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notEmpty(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					return method;
				}
			}
		}
		return null;
	}

	public static Method getMethodByName(final Class cls, final String methodName) {
		Validate.notNull(cls, "cls can't be null");
		Validate.notEmpty(methodName, "methodName can't be blank");

		for (Class<?> searchType = cls; searchType != Object.class; searchType = searchType.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	/**
	 * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers()))
				&& !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	/**
	 * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处 如无法找到, 返回Object.class. eg.
	 * public UserDao extends HibernateDao<User>
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be
	 *         determined
	 */
	public static <T> Class<T> getClassGenricType(final Class clazz) {
		return getClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 * 
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be
	 *         determined
	 */
	public static Class getClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if ((index >= params.length) || (index < 0)) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	public static Class<?> getUserClass(Object instance) {
		Assert.notNull(instance, "Instance must not be null");
		Class clazz = instance.getClass();
		if ((clazz != null) && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if ((superClass != null) && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;

	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if ((e instanceof IllegalAccessException) || (e instanceof IllegalArgumentException) || (e instanceof NoSuchMethodException)) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

	/**
	 * 检测是否基本数据类型对应的包装类：int, double, float, long, short, boolean, byte, char，
	 * void
	 * 
	 * @param clz
	 * @return
	 */
	public static boolean isWrapClass(Class clz) {
		try {
			return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}

	/** 获取类加载器 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/** 获取类路径 */
	public static String getClassPath() {
		String classpath = "";
		URL resource = getClassLoader().getResource("");
		if (resource != null) {
			classpath = resource.getPath();
		}
		return classpath;
	}

	/**
	 * 给Bean设置Map里边对应的属性值（简单模拟Commons BeanUtils的populate方法）
	 * 
	 * @param bean
	 * @param properties
	 */
	public static void populate(Object bean, Map<String, ? extends Object> properties) {
		try {
			Field[] fields = bean.getClass().getDeclaredFields();
			for (Field field : fields) {
				Object obj = properties.get(field.getName());
				if (obj == null) {
					continue;
				}
				invokeSetter(bean, field.getName(), obj);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Returns array of all methods that are accessible from given class.
	 * 
	 * @see #getAccessibleMethods(Class, Class)
	 */
	public static Method[] getAccessibleMethods(Class clazz) {
		return getAccessibleMethods(clazz, Object.class);
	}

	/**
	 * Returns array of all methods that are accessible from given class, upto
	 * limit (usually <code>Object.class</code>). Abstract methods are ignored.
	 */
	public static Method[] getAccessibleMethods(Class clazz, Class limit) {
		Package topPackage = clazz.getPackage();
		List<Method> methodList = new ArrayList<Method>();
		int topPackageHash = topPackage == null ? 0 : topPackage.hashCode();
		boolean top = true;
		do {
			if (clazz == null) {
				break;
			}
			Method[] declaredMethods = clazz.getDeclaredMethods();
			for (Method method : declaredMethods) {
				if (Modifier.isVolatile(method.getModifiers())) {
					continue;
				}
				// if (Modifier.isAbstract(method.getModifiers())) {
				// continue;
				// }
				if (top == true) { // add all top declared methods
					methodList.add(method);
					continue;
				}
				int modifier = method.getModifiers();
				if (Modifier.isPrivate(modifier) == true) {
					continue; // ignore super private methods
				}
				if (Modifier.isAbstract(modifier) == true) { // ignore super
																// abstract
																// methods
					continue;
				}
				if (Modifier.isPublic(modifier) == true) {
					addMethodIfNotExist(methodList, method); // add super public
																// methods
					continue;
				}
				if (Modifier.isProtected(modifier) == true) {
					addMethodIfNotExist(methodList, method); // add super
																// protected
																// methods
					continue;
				}
				// add super default methods from the same package
				Package pckg = method.getDeclaringClass().getPackage();
				int pckgHash = pckg == null ? 0 : pckg.hashCode();
				if (pckgHash == topPackageHash) {
					addMethodIfNotExist(methodList, method);
				}
			}
			top = false;
		} while ((clazz = clazz.getSuperclass()) != limit);

		Method[] methods = new Method[methodList.size()];
		for (int i = 0; i < methods.length; i++) {
			methods[i] = methodList.get(i);
		}
		return methods;
	}

	/**
	 * Compares method signatures: names and parameters.
	 */
	public static boolean compareSignatures(Method first, Method second) {
		if (first.getName().equals(second.getName()) == false) {
			return false;
		}
		return compareParameters(first.getParameterTypes(), second.getParameterTypes());
	}

	private static void addMethodIfNotExist(List<Method> allMethods, Method newMethod) {
		for (Method m : allMethods) {
			if (compareSignatures(m, newMethod) == true) {
				return;
			}
		}
		allMethods.add(newMethod);
	}

	public static boolean compareParameters(Class[] first, Class[] second) {
		if (first.length != second.length) {
			return false;
		}
		for (int i = 0; i < first.length; i++) {
			if (first[i] != second[i]) {
				return false;
			}
		}
		return true;
	}

	public static Field[] getAccessibleFields(Class clazz) {
		return getAccessibleFields(clazz, Object.class);
	}

	public static Field[] getAccessibleFields(Class clazz, Class limit) {
		Package topPackage = clazz.getPackage();
		List<Field> fieldList = new ArrayList<Field>();
		int topPackageHash = topPackage == null ? 0 : topPackage.hashCode();
		boolean top = true;
		do {
			if (clazz == null) {
				break;
			}
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field field : declaredFields) {
				if (top == true) { // add all top declared fields
					fieldList.add(field);
					continue;
				}
				int modifier = field.getModifiers();
				if (Modifier.isPrivate(modifier) == true) {
					continue; // ignore super private fields
				}
				if (Modifier.isPublic(modifier) == true) {
					addFieldIfNotExist(fieldList, field); // add super public
															// methods
					continue;
				}
				if (Modifier.isProtected(modifier) == true) {
					addFieldIfNotExist(fieldList, field); // add super protected
															// methods
					continue;
				}
				// add super default methods from the same package
				Package pckg = field.getDeclaringClass().getPackage();
				int pckgHash = pckg == null ? 0 : pckg.hashCode();
				if (pckgHash == topPackageHash) {
					addFieldIfNotExist(fieldList, field);
				}
			}
			top = false;
		} while ((clazz = clazz.getSuperclass()) != limit);

		Field[] fields = new Field[fieldList.size()];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = fieldList.get(i);
		}
		return fields;
	}

	private static void addFieldIfNotExist(List<Field> allFields, Field newField) {
		for (Field f : allFields) {
			if (compareSignatures(f, newField) == true) {
				return;
			}
		}
		allFields.add(newField);
	}

	public static boolean compareSignatures(Field first, Field second) {
		return first.getName().equals(second.getName());
	}

	public static boolean isSubclass(Class thisClass, Class target) {
		if (target.isInterface() != false) {
			return isInterfaceImpl(thisClass, target);
		}
		for (Class x = thisClass; x != null; x = x.getSuperclass()) {
			if (x == target) {
				return true;
			}
		}
		return false;
	}

	public static boolean isInterfaceImpl(Class thisClass, Class targetInterface) {
		for (Class x = thisClass; x != null; x = x.getSuperclass()) {
			Class[] interfaces = x.getInterfaces();
			for (Class i : interfaces) {
				if (i == targetInterface) {
					return true;
				}
				if (isInterfaceImpl(i, targetInterface)) {
					return true;
				}
			}
		}
		return false;
	}
}
