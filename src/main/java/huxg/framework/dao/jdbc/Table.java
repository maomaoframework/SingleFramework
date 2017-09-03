package huxg.framework.dao.jdbc;

import java.util.HashMap;
import java.util.Map;

public class Table {
	private String tableName;
	private String className;
	private String idColumnName;
	private String idGetterName;
	private String idSetterName;
	private Map<String, MetaData> metas = new HashMap<String, MetaData>();
	private MetaData idMetaData;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, MetaData> getMetas() {
		return metas;
	}

	public void setMetas(Map<String, MetaData> metas) {
		this.metas = metas;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public MetaData getMeta(String columnName) {
		return metas.get(columnName);
	}

	public String getIdColumnName() {
		return idColumnName;
	}

	public void setIdColumnName(String idColumnName) {
		this.idColumnName = idColumnName;
	}

	public MetaData getIdMetaData() {
		return idMetaData;
	}

	public void setIdMetaData(MetaData idMetaData) {
		this.idMetaData = idMetaData;
	}

	public String getIdGetterName() {
		return idGetterName;
	}

	public void setIdGetterName(String idGetterName) {
		this.idGetterName = idGetterName;
	}

	public String getIdSetterName() {
		return idSetterName;
	}

	public void setIdSetterName(String idSetterName) {
		this.idSetterName = idSetterName;
	}

}
