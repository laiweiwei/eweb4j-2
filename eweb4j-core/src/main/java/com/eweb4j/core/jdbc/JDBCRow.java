package com.eweb4j.core.jdbc;

import java.util.ArrayList;
import java.util.List;

/**
 * JDBC结果集
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午12:22:07
 */
public class JDBCRow {

	private int number;
	
	private List<JDBCColumn> columns = new ArrayList<JDBCColumn>();

	public JDBCRow(){}
	
	public JDBCRow(int number){
		this.number = number;
	}
	
	public int number() {
		return this.number;
	}

	public List<JDBCColumn> columns() {
		return this.columns;
	}
	
	public Object value(String name){
		for (JDBCColumn column : columns){
			if (column.name().equals(name))
				return column.value();
		}
		return null;
	}

	@Override
	public String toString() {
		return "JDBCRow [number=" + this.number + ", columns=" + this.columns
				+ "]";
	}
	
}
