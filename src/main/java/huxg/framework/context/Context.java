package huxg.framework.context;

import java.io.Serializable;

/**
 * @author 
 *
 */
public class Context implements Serializable{
	private static final long serialVersionUID = 4964467123192295987L;
	private String  dataSourceName=null;
	private boolean isRead=false;
	public Context(boolean isRead,String dataSourceName) {
		super();
		this.isRead=isRead;
		this.dataSourceName = dataSourceName;
	}
	public Context(boolean isRead){
		super();
		this.isRead=isRead;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	
	
}
