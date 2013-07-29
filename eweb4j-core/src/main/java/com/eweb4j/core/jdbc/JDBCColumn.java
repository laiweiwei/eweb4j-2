package com.eweb4j.core.jdbc;
/**
 * JDBC结果集
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午12:22:07
 */
public class JDBCColumn {

	private int index;
	
	private String name;
	
	private Object value;
	
	public JDBCColumn(){
		
	}
	
	public JDBCColumn(int index, String name, Object value) {
		this.index = index;
		this.name = name;
		this.value = value;
	}

	public int index() {
		return this.index;
	}

	public String name() {
		return this.name;
	}

	public Object value() {
		return this.value;
	}

	@Override
	public String toString() {
		return "JDBCResult [index=" + this.index + ", name=" + this.name
				+ ", value=" + this.value + "]";
	}
	
}
