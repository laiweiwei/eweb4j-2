package com.eweb4j.core.jdbc;

import javax.sql.DataSource;

/**
 * JDBC 数据源
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-29 上午01:00:40
 */
public class JDBCDataSource {

	private String name;
	
	private DataSource ds;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}
	
}
