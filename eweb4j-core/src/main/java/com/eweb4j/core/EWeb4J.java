package com.eweb4j.core;

import com.eweb4j.core.plugin.PluginManager;


/**
 * EWeb4J 启动类
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:00:39
 */
public class EWeb4J {

	public PluginManager pluginManager = null;
	
	public EWeb4J(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}
	
	public void setPluginManager(PluginManager pluginManager){
		this.pluginManager = pluginManager;
	}
	
	/**
	 * 启动框架
	 * @date 2013-6-13 上午11:55:02
	 * @param listener
	 */
	public final EWeb4J startup(final Listener listener) {
		listener.onStartup(pluginManager);
		
		return this;
	}
	
	/**
	 * 停止框架
	 * @date 2013-6-29 上午12:47:50
	 * @param listener
	 */
	public final EWeb4J shutdown(final Listener listener) {
		listener.onShutdown(pluginManager);
		
		return this;
	}
	
	public static interface Listener {
		
		public void onStartup(PluginManager plugins);
		
		public void onShutdown(PluginManager plugins);
		
	}
	
	public static interface Configs {
		/*默认的数据源*/
		String DEFAULT_DATA_SOURCE = "DEFAULT_DATA_SOURCE";
	}
}
