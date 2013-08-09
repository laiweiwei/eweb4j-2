package com.eweb4j.core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

/**
 * JDBC 帮助类
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午12:08:31
 */
public class JDBCHelper {

	/**
	 * 执行一段SQL 
	 * @date 2013-6-13 下午12:20:46
	 * @param sql
	 * @param args
	 * @return
	 */
	public static int execute(DataSource ds, String sql, Object... args){
		if (ds == null || sql == null || sql.trim().length() == 0)
			return 0;
		
		int result = 0;

		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			fillArgs(new Object[][]{args}, pstmt, 0);
			result = pstmt.executeUpdate();
			if (result > 0 && sql.toUpperCase().contains("INSERT INTO")) {
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()){
					result = rs.getInt(1);
				}
			}
			pstmt.close();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			close(null, pstmt, con);
			System.out.println("---- EWEB4J SQL ----");
			if (args != null && args.length > 0)
				System.out.println("---- "+sql+" " + Arrays.asList(args)+" ----");
			else
				System.out.println("---- "+sql+" ----");
		}

		return result;
	}
	
	/**
	 * 批量执行一段SQL 
	 * @date 2013-6-13 下午12:20:58
	 * @param sql
	 * @param args
	 * @return
	 */
	public static int[] batchExecute(DataSource ds, String sql, Object[]... args){
		if (ds == null || sql == null || sql.trim().length() == 0)
			return null;
		
		int[] result = new int[]{0};

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < args.length; ++i) {
				for (int j = 0; j < args[i].length; j++){
					pstmt.setObject(j+1, args[i][j]);
				}
				pstmt.addBatch();
			}
			
			int[] rows = pstmt.executeBatch();
			if (rows != null && rows.length > 0) {
				result = new int[rows.length];
				if (sql.toUpperCase().contains("INSERT INTO")){
					ResultSet rs = pstmt.getGeneratedKeys();
					int i = 0;
					while (rs.next()){
						result[i] = rs.getInt(1);
						i++;
					}
				}else{
					for (int i = 0; i < rows.length; i++){
						result[i] = rows[i];
					}
				}
			}
			pstmt.close();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			close(null, pstmt, con);
			if (args != null && args.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (Object[] _args : args){
					if (sb.length() > 0)
						sb.append(",");
					
					sb.append(Arrays.asList(_args));
				}
				System.out.println("---- "+sql+" ["+sb.toString()+"]");
			} else
				System.out.println("---- "+sql+" ----");
		}

		return result;
	}

	public static JDBCRow findOne(DataSource ds, String sql, Object... args){
		List<JDBCRow> rows = find(ds, sql, args);
		return rows == null ? null : rows.isEmpty() ? null : rows.get(0);
	}
	
	/**
	 * 获取数据库数据 
	 * @date 2013-6-13 下午12:23:48
	 * @param ds
	 * @param sql
	 * @param args
	 * @return
	 */
	public static List<JDBCRow> find(DataSource ds, String sql, Object... args){
		if (ds == null || sql == null || sql.trim().length() == 0)
			return null;
		
		List<JDBCRow> result = new ArrayList<JDBCRow>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		Connection con = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql);
			if (args != null && args.length > 0) {
				for (int i = 0; i < args.length; ++i) {
					pstmt.setObject(i + 1, args[i]);
				}
			}
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
			List<String> columns = new ArrayList<String>();
			for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
				columns.add(rsmd.getColumnName(i));
			}
			while (rs.next()) {
				JDBCRow jdbcResultSet = new JDBCRow(rs.getRow());
				for (int i = 1; i <= columns.size(); ++i) {
					int index = i-1;
					String name = columns.get(index);
					Object value = rs.getObject(name);
					jdbcResultSet.columns().add(new JDBCColumn(index, name, value));
				}
				
				result.add(jdbcResultSet);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			close(rs, pstmt, con);
			System.out.println("---- EWEB4J SQL ----");
			if (args != null && args.length > 0)
				System.out.println("---- "+sql+" " + Arrays.asList(args)+" ----");
			else
				System.out.println("---- "+sql+" ----");
		}

		return result;
	}
	
	/**
	 * 关闭资源
	 * 
	 * @param rs
	 * @param pstmt
	 * @param con
	 * @throws Exception
	 */
	public static void close(ResultSet rs, PreparedStatement pstmt, Connection con) {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (con != null) 
				con.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 填充参数
	 * 
	 * @param args
	 * @param pstmt
	 * @param i
	 * @throws SQLException
	 */
	private static void fillArgs(Object[][] args, PreparedStatement pstmt, int i) throws SQLException {
		if (args == null || args.length == 0)
			return ;
		if (args[i] == null || args[i].length == 0)
			return ;
		
		for (int j = 0; j < args[i].length; ++j) 
			pstmt.setObject(j + 1, args[i][j]);
	}
	
}
