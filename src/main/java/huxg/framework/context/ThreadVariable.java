package huxg.framework.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 
 *
 */
public class ThreadVariable {
	
	private static ThreadLocal<String> dataSourceName = new ThreadLocal<String>();
	
	private static ThreadLocal<String> firstMethod=new ThreadLocal<String>();
	
	private static ThreadLocal<Context> lastTopContext = new ThreadLocal<Context>();
	
	private static ThreadLocal<Map<String,Context>> threadMap=new ThreadLocal<Map<String,Context>>();
	
	public static Context getLastTopContext(){
		return lastTopContext.get();
	}
	public static void setLastTopContext(Context topContext){
		lastTopContext.set(topContext);
	}
	public static void removeLastTopContext(){
		lastTopContext.remove();
	}
	
	public static String getDataSourceName() {
		return dataSourceName.get();
	}
	
	public static void setDataSourceName(String dsName) {
		dataSourceName.set(dsName);
	}
	
	public static void removeDataSourceName() {
		dataSourceName.remove();
	}
	
	public static String getFirstMethod() {
		return firstMethod.get();
	}
	
	public static void setFirstMethod(String str) {
		firstMethod.set(str);
	}
	
	public static void removeFirstMethod(){
		firstMethod.remove();
	}
	
	public static Map<String,Context> getThreadMap() {
		return threadMap.get();
	}
	
	public static void setThreadMap(String key,Context value) {
		if(null!=getThreadMap()){
			getThreadMap().put(key, value);
		}else{
			Map<String,Context> map=new HashMap<String,Context>();
			map.put(key, value);
			threadMap.set(map);
		}
	}
	public static void removeThreadMap(){
		threadMap.remove();
	}
	
	
	
}