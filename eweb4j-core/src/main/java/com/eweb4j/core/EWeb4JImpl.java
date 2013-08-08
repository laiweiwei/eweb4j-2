package com.eweb4j.core;

import java.util.ArrayList;
import java.util.List;

import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.core.plugin.PluginManager;


/**
 * 框架
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:00:39
 */
public class EWeb4JImpl implements EWeb4J{
	
	private PluginManager pluginManager = null;
	private List<Listener> listeners = new ArrayList<Listener>();
	private List<Plugin> plugins = new ArrayList<Plugin>();
	
	public EWeb4JImpl(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
		List<String> listenerClasses = this.pluginManager.getConfigFactory().getListeners();
		if (listenerClasses != null) {
			for (String listenerClass : listenerClasses) {
				try {
					@SuppressWarnings("unchecked")
					Class<EWeb4JImpl.Listener> clazz = (Class<EWeb4JImpl.Listener>) Thread.currentThread().getContextClassLoader().loadClass(listenerClass);
					EWeb4JImpl.Listener listener = clazz.newInstance();
					this.addListener(listener);
				} catch (Throwable e) {
				}
			}
		}
	}
	
	public void setPluginManager(PluginManager pluginManager){
		this.pluginManager = pluginManager;
	}
	
	public PluginManager getPluginManager() {
		return pluginManager;
	}

	public void addListener(Listener listener){
		this.listeners.add(listener);
	}
	
	public List<Listener> getListeners() {
		return listeners;
	}
	
	public void addPlugin(Plugin plugin) {
		this.plugins.add(plugin);
	}
	
	public List<Plugin> getPlugins() {
		return plugins;
	}
	
	/**
	 * 启动框架
	 * @date 2013-6-13 上午11:55:02
	 */
	public final EWeb4JImpl startup() {
		for (Plugin plugin : this.plugins) {
			this.pluginManager.install(plugin);
		}
		
		for (Listener listener : this.listeners) 
			listener.onStartup(this);
		
		return this;
	}
	
	/**
	 * 停止框架
	 * @date 2013-6-29 上午12:47:50
	 */
	public final EWeb4JImpl shutdown() {
		for (Listener listener : this.listeners) 
			listener.onShutdown(this);
		
		this.pluginManager.stopAll();
		return this;
	}

}
