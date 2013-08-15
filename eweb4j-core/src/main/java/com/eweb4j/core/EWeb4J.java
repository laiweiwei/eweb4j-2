package com.eweb4j.core;

import java.util.List;

import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.ioc.IOC;
import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.core.plugin.PluginManager;


/**
 * 框架接口
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 12:51:39
 */
public interface EWeb4J {
	
	/**
	 * 设置IOC容器实例
	 * @param ioc
	 */
	public void setIOC(IOC ioc);
	
	/**
	 * 获取IOC容器
	 * @return
	 */
	public IOC getIOC();
	
	/**
	 * 获取插件管理器
	 * @return
	 */
	public PluginManager getPluginManager() ;
	
	/**
	 * 获取配置工厂
	 * @return
	 */
	public ConfigurationFactory getConfigFactory();
	
	/**
	 * 添加监听器
	 * @param listener
	 */
	public void addListener(Listener listener) ;
	
	/**
	 * 获取所有监听器
	 * @return
	 */
	public List<Listener> getListeners() ;
	
	/**
	 * 添加插件
	 * @param plugin
	 */
	public void addPlugin(Plugin plugin) ;
	
	/**
	 * 获取所有插件
	 * @return
	 */
	public List<Plugin> getPlugins() ;
	
	
	/**
	 * 启动框架
	 * @date 2013-8-8
	 */
	public EWeb4J startup() ;
	
	/**
	 * 停止框架
	 * @date 2013-8-8
	 */
	public EWeb4J shutdown() ;
	
	/**
	 * 监听器接口
	 * @author vivi
	 *
	 */
	public static interface Listener {
		public void onStartup(EWeb4J eweb4j);
		public void onShutdown(EWeb4J eweb4j);
	}
	
	/**
	 * 一些常量
	 * @author vivi
	 *
	 */
	public static class Constants {
		public final static String classpath = Constants.class.getResource("/").getFile();
		public final static String config_xml = resolve_path("classpath:eweb4j-config.xml");
		
		public final static String resolve_path(String path){
			return path.replace("classpath:", classpath);
		}
		
		public static interface Configurations{
			public static String BASE_ID = "base";
			public static String PLUGIN_ID = "plugin";
			public static String MVC_ID = "mvc";
			public static String ORM_ID = "orm";
			public static String JPA_ID = "jpa";
			public static String JDBC_ID = "jdbc";
			public static String LISTENER_ID = "listener";
			public static String DATA_SOURCE_ID = "data_source";
			public static String IOC_ID = "ioc";
			
			public static interface Types{
				public static String PROPERTIES = "properties";
				public static String XML = "xml";
				public static String JSON = "json";
			}
		}
	}

}
