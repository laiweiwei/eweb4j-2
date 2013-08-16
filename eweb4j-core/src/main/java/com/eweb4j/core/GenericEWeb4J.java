package com.eweb4j.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.ioc.IOC;
import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.core.plugin.PluginManager;


/**
 * 普通的EWeb4J实现
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:00:39
 */
public class GenericEWeb4J implements EWeb4J{
	
	private ConfigurationFactory configFactory = null;
	private PluginManager pluginManager = null;
	private IOC ioc = null;
	
	private List<Listener> listeners = new ArrayList<Listener>();
	private Map<String, Plugin> plugins = new HashMap<String, Plugin>();
	
	public GenericEWeb4J(ConfigurationFactory configFactory, PluginManager pluginManager) {
		this.configFactory = configFactory;
		this.pluginManager = pluginManager;
		this.pluginManager.setEWeb4J(this);
	}
	
	public PluginManager getPluginManager() {
		return pluginManager;
	}
	
	public ConfigurationFactory getConfigFactory() {
		return this.configFactory;
	}

	public void addListener(Listener listener){
		this.listeners.add(listener);
	}
	
	public List<Listener> getListeners() {
		return listeners;
	}
	
	public void addPlugin(String id, Plugin plugin) {
		this.plugins.put(id, plugin);
	}
	
	/**
	 * 启动框架
	 * @date 2013-6-13 上午11:55:02
	 */
	public final GenericEWeb4J startup() {
		for (Iterator<Entry<String, Plugin>> it = this.plugins.entrySet().iterator(); it.hasNext(); ) {
			Entry<String, Plugin> e = it.next();
			String id = e.getKey();
			Plugin plugin = e.getValue();
			this.pluginManager.install(id, plugin);
		}
		
		List<String> listenerClasses = this.configFactory.getListeners();
		if (listenerClasses != null) {
			for (String listenerClass : listenerClasses) {
				try {
					@SuppressWarnings("unchecked")
					Class<EWeb4J.Listener> clazz = (Class<EWeb4J.Listener>) Thread.currentThread().getContextClassLoader().loadClass(listenerClass);
					EWeb4J.Listener listener = clazz.newInstance();
					this.addListener(listener);
				} catch (Throwable e) {
				}
			}
		}
		
		for (Listener listener : this.listeners) 
			listener.onStartup(this);
		
		return this;
	}
	
	/**
	 * 停止框架
	 * @date 2013-6-29 上午12:47:50
	 */
	public final GenericEWeb4J shutdown() {
		for (Listener listener : this.listeners) 
			listener.onShutdown(this);
		
		this.pluginManager.stopAll();
		return this;
	}

	public IOC getIOC() {
		return this.ioc;
	}

	public void setIOC(IOC ioc) {
		this.ioc = ioc;
	}

}
