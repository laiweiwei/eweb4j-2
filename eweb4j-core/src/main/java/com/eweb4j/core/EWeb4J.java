package com.eweb4j.core;

import java.util.ArrayList;
import java.util.List;

import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.core.plugin.PluginManager;


/**
 * EWeb4J 启动类
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:00:39
 */
public class EWeb4J {
	
	private PluginManager pluginManager = null;
	private List<Listener> listeners = new ArrayList<Listener>();
	private List<Plugin> plugins = new ArrayList<Plugin>();
	
	public EWeb4J(){
		
	}
	
	public EWeb4J(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}
	
	public void setPluginManager(PluginManager pluginManager){
		this.pluginManager = pluginManager;
	}
	
	public void addListener(Listener listener){
		this.listeners.add(listener);
	}
	
	public void addPlugin(Plugin plugin) {
		this.plugins.add(plugin);
	}
	
	/**
	 * 启动框架
	 * @date 2013-6-13 上午11:55:02
	 * @param listener
	 */
	public final EWeb4J startup() {
		for (Plugin plugin : this.plugins){
			this.pluginManager.install(plugin);
		}
		
		for (Listener listener : this.listeners) {
			listener.onStartup(this.pluginManager);
		}
		
		return this;
	}
	
	/**
	 * 停止框架
	 * @date 2013-6-29 上午12:47:50
	 * @param listener
	 */
	public final EWeb4J shutdown() {
		for (Listener listener : this.listeners) {
			listener.onShutdown(this.pluginManager);
		}
		
		this.pluginManager.stopAll();
		
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

	public List<Listener> getListeners() {
		return listeners;
	}

	public void setListeners(List<Listener> listeners) {
		this.listeners = listeners;
	}

	public List<Plugin> getPlugins() {
		return plugins;
	}

	public void setPlugins(List<Plugin> plugins) {
		this.plugins = plugins;
	}

	public PluginManager getPluginManager() {
		return pluginManager;
	}
	
}
