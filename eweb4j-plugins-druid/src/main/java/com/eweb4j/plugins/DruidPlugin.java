package com.eweb4j.plugins;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.configuration.Configuration;
import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.configuration.MapConfiguration;
import com.eweb4j.core.plugin.Plugin;

/**
 * Alibaba Druid 连接池
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午04:50:52
 */
public class DruidPlugin extends Plugin{

	/**
	 * JDBC 配置信息
	 */
	private Configuration<String, String> jdbcConfig = null;
	/**
	 * 数据源容器
	 */
	private Configuration<String, DataSource> dsHolder = null;
	
	/**
	 * 初始化
	 */
	public void init(EWeb4J eweb4j){
		ConfigurationFactory configFactory = eweb4j.getConfigFactory();
		this.jdbcConfig = configFactory.getJdbcConfig();
		
		final String dsConfigID = EWeb4J.Constants.Configurations.DATA_SOURCE_ID;
		//准备好数据源容器
		Configuration<String, DataSource> dsHolder = configFactory.getConfiguration(dsConfigID);
		if (dsHolder == null) {
			//如果没有，初始化一个容器
			dsHolder = new MapConfiguration<String, DataSource>();
			configFactory.addConfiguration(dsConfigID, dsHolder);
		}
		
		this.dsHolder = dsHolder;
	}
	
	public void start() {
		String dsNameStr = this.jdbcConfig.getString("ds.names", "ds");
		String[] dsNameArr = dsNameStr.split(",");
		for (String dsName : dsNameArr) {
			String prefix = dsName+".";
			DruidDataSource ds = new DruidDataSource();
			ds.setUrl(this.jdbcConfig.getString(prefix + "url"));
			ds.setUsername(this.jdbcConfig.getString(prefix + "username"));
			ds.setPassword(this.jdbcConfig.getString(prefix + "password"));
			ds.setTestOnBorrow(this.jdbcConfig.getBoolean(prefix + "test_on_borrow", false));
			ds.setTestWhileIdle(this.jdbcConfig.getBoolean(prefix + "test_while_idle", true));
			ds.setValidationQuery(this.jdbcConfig.getString(prefix + "validation_query"));
			
			System.out.println("add ds to dsConfig->" + dsName);
			//将数据源实例添加到容器里
			this.dsHolder.put(dsName, ds);
		}
	}

	public void stop() {
		this.dsHolder.clear();
	}

	public String ID() {
		return "druid-plugin";
	}

	public String name() {
		return ID();
	}

	public String provider() {
		return "l.weiwei@163.com";
	}
}
