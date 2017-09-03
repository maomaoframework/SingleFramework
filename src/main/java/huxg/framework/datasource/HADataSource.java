package huxg.framework.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.WrapperAdapter;

public class HADataSource extends WrapperAdapter implements DataSource {
	private int loginTimeSeconds;
	protected PrintWriter logWriter = new PrintWriter(System.out);
	private DruidDataSource master;
	private DruidDataSource second;

	public DruidDataSource getMaster() {
		return master;
	}

	public void setMaster(DruidDataSource master) {
		this.master = master;
	}

	public DruidDataSource getSecond() {
		return second;
	}

	public void setSecond(DruidDataSource second) {
		this.second = second;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return logWriter;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		this.logWriter = out;
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		this.loginTimeSeconds = seconds;
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return loginTimeSeconds;
	}

	 //@Override TODO 
	 public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		 throw new SQLFeatureNotSupportedException();
	 }
	@Override
	public Connection getConnection() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}
}
