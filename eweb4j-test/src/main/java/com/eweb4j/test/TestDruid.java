package com.eweb4j.test;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.pool.DruidDataSource;
import com.eweb4j.core.jdbc.JDBCColumn;
import com.eweb4j.core.jdbc.JDBCHelper;
import com.eweb4j.core.jdbc.JDBCRow;
import com.eweb4j.core.orm.RowMappings;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午01:00:03
 */
public class TestDruid {

	public static void main(String[] args) throws SQLException, ParseException{
		DruidDataSource ds = new DruidDataSource();
		ds.setUrl("jdbc:mysql://localhost:3306/petstore_db");
		ds.setUsername("root");
		ds.setPassword("root");
		ds.setTestOnBorrow(false);
		ds.setTestWhileIdle(true);
		ds.setValidationQuery("select * from t_pet where 1 = 1");
		
		int r = JDBCHelper.execute(ds, "update t_pet set created_at = ? where id = ?", new Date(), 29);
		System.out.println(r);
		
		int[] rr = JDBCHelper.batchExecute(ds, "insert into t_pet(name, age) values(?,?)", new Object[][]{new Object[]{1,1}, new Object[]{2,2}});
		for (int n : rr)
			System.out.println(n);
		
		List<JDBCRow> rows = JDBCHelper.find(ds, "select * from t_pet");
		for (JDBCRow row : rows) {
			int num = row.number();
			System.out.println("r->" + num);
			for (JDBCColumn col : row.columns()){
				System.out.println("\t"+col);
			}
		}
		
		RowMappings mappings = new RowMappings(Map.class);
		List<Map> maps = mappings.mapping(rows);
		if (maps != null)
			for (Map m : maps) 
				System.out.println(m);
	}
	
}
