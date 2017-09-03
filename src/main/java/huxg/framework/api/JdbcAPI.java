package huxg.framework.api;

import huxg.framework.dao.jdbc.JdbcDAO;
import huxg.framework.util.SpringUtils;

public class JdbcAPI {
	public static JdbcDAO getJdbc() {
		JdbcDAO jdbc = (JdbcDAO) SpringUtils.getBean("jdbcDao");
		return jdbc;
	}
}
