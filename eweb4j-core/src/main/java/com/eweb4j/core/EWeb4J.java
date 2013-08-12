package com.eweb4j.core;

import java.util.List;

import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.feature.Feature;
import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.core.plugin.PluginManager;


/**
 * 框架接口
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 12:51:39
 */
public interface EWeb4J {
	
	/**
	 * 获取一个具有某种特性的对象，比如DAO对象
	 * @param <T extends Feature>
	 * @param toolName
	 * @param args
	 * @return
	 */
	public <T extends Feature> T getFeature(String feature, Object... args);
	
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
	public static interface Constants {
		String config_xml = "eweb4j-config.xml";
		public static interface Configurations{
			String BASE_ID = "base";
			String PLUGIN_ID = "plugin";
			String MVC_ID = "mvc";
			String ORM_ID = "orm";
			String JPA_ID = "jpa";
			String JDBC_ID = "jdbc";
			String LISTENER_ID = "listener";
			String DATA_SOURCE_ID = "data_source";
			String FEATURE_ID = "feature";
			
			public static interface Types{
				String PROPERTIES = "properties";
				String XML = "xml";
				String JSON = "json";
			}
		}
	}

}
