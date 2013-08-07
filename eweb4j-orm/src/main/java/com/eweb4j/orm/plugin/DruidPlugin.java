package com.eweb4j.orm.plugin;

import java.util.ArrayList;
import java.util.List;

import com.eweb4j.core.configuration.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.utils.PropertiesUtil;
import com.eweb4j.jdbc.config.DSPool;

/**
 * Alibaba Druid 连接池
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午04:50:52
 */
public class DruidPlugin extends Plugin{

	public final static List<String> DS_NAMES = new ArrayList<String>();
	
	public void init(Configuration config){
		
	}
	
	public void start() {
		PropertiesUtil prop = new PropertiesUtil();
		String classPath = DruidPlugin.class.getResource("/").getPath();
		String filePath = classPath+"/db.properties"; 
		prop.loadFile(filePath);
		String dsNameStr = prop.getStr("ds");
		if (dsNameStr == null)
			dsNameStr = "ds";
		String[] dsNameArr = dsNameStr.split(",");
		for (String dsName : dsNameArr) {
			String prefix = dsName+".";
			DruidDataSource ds = new DruidDataSource();
			ds.setUrl(prop.getStr(prefix + "url"));
			ds.setUsername(prop.getStr(prefix + "username"));
			ds.setPassword(prop.getStr(prefix + "password"));
			ds.setTestOnBorrow(prop.getBoolean(prefix + "test_on_borrow"));
			ds.setTestWhileIdle(prop.getBoolean(prefix + "test_while_idle"));
			ds.setValidationQuery(prop.getStr(prefix + "validation_query"));
			System.out.println("add ds pool->" + dsName);
			DSPool.add(dsName, ds);
			DS_NAMES.add(dsName);
		}
	}

	public void stop() {
		DSPool.clear();
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
