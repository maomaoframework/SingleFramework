package huxg.framework.dao.jdbc;

import java.util.HashMap;
import java.util.Map;

/**
 * 表结构管理器
 * 
 * @author apple
 * 
 */
public class TableMetaManager {
	private static TableMetaManager instance;
	private Map<String, Table> mp;

	public static TableMetaManager getInstance() {
		if (instance == null) {
			instance = new TableMetaManager();
			instance.init();
		}
		return instance;
	}

	private void init() {
		mp = new HashMap<String, Table>();
	}

	public void addMeta(Table t) {
		mp.put(t.getTableName(), t);
	}
	
	public Table getTable(String tableName){
		return mp.get(tableName);
	}
}
