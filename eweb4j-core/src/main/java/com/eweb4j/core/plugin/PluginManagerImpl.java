package com.eweb4j.core.plugin;

import java.util.Iterator;
import java.util.Map.Entry;

import com.eweb4j.core.configuration.Configuration;
import com.eweb4j.core.configuration.MapConfiguration;

/**
 * 插件管理器
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:39:03
 */
public class PluginManagerImpl extends PluginManager{
	
	/**
	 * 插件仓库
	 */
	protected Configuration<String, Plugin> plugins = new MapConfiguration<String, Plugin>();
	
	/**
	 * 无参构造器
	 */
	public PluginManagerImpl(){}
	
	/**
	 * 获取插件仓库实例
	 */
	public Configuration<String, Plugin> getPlugins() {
		return plugins;
	}

	/**
	 * 注入插件仓库实例
	 * @param plugins
	 */
	public void setPlugins(Configuration<String, Plugin> plugins) {
		this.plugins = plugins;
	}

	/**
	 * 获取指定插件
	 * @date 2013-6-29 上午12:41:39
	 * @param id 插件ID
	 * @return
	 */
	public Plugin getPlugin(String pluginID) {
		return this.plugins == null ? null : this.plugins.get(pluginID);
	}
	
	/**
	 * 安装插件
	 */
	public boolean install(String pluginID, Plugin plugin) {
		if (this.plugins.containsKey(pluginID))
			return true;
		
		//初始化插件
		try {
			plugin.init(eweb4j);
		} catch (Throwable e) {
			throw new RuntimeException("plugin->" + pluginID + " init failed.", e);
		}
		
		//启动插件
		try {
			plugin.start();
		} catch (Throwable e){
			throw new RuntimeException("plugin->" + pluginID + " start failed.", e);
		}
		
		//注册插件ID到仓库中
		this.plugins.put(pluginID, plugin);
		
		//TODO: 使用监听器代替
		System.out.println("plugin->"+pluginID+" install success.");
		
		return true;
	}
	
	/**
	 * 卸载插件
	 */
	public boolean uninstall(String pluginID, Plugin plugin) {
		//先关闭插件
		try {
			Plugin _plugin = getPlugin(pluginID);
			if (_plugin != null)
				_plugin.stop();
			if (plugin != null)
				plugin.stop();
			
		} catch (Throwable e){
			throw new RuntimeException("plugin->" + pluginID + " stop failed.", e);
		}
		
		//从仓库中去除
		this.plugins.remove(pluginID);
		
		//TODO: 使用监听器代替
		System.out.println("plugin->"+pluginID+" uninstall success.");
		
		return true;
	}
	
	/**
	 * 升级插件 
	 */
	public boolean upgrade(String pluginID, Plugin plugin) {
		//先卸载插件
		uninstall(pluginID, plugin);
		//然后安装插件
		install(pluginID, plugin);
		
		//TODO: 使用监听器代替
		System.out.println("plugin->"+pluginID+" upgrade success.");
		
		return true;
	}

	/**
	 * 停止所有插件
	 */
	public boolean stopAll() {
		for (Iterator<Entry<String, Plugin>> it = this.plugins.getMap().entrySet().iterator(); it.hasNext();) {
			Entry<String, Plugin> e = it.next();
			Plugin plugin = e.getValue();
			String pluginID = e.getKey();
			it.remove();
			uninstall(pluginID, plugin);
		}
		
		return true;
	}

}
