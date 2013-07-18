package com.eweb4j.core.plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.eweb4j.core.config.Config;
import com.eweb4j.core.config.ConfigImpl;

/**
 * 插件管理器
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:39:03
 */
public class PluginsMgr extends Plugins{
	
	protected Map<String, Plugin> plugins = new HashMap<String, Plugin>();
	
	private final Config config = new ConfigImpl(); 
	
	public Config getConfig(){
		return this.config;
	}
	
	/**
	 * 获取所有插件
	 * @date 2013-6-29 上午12:41:39
	 * @return
	 */
	public Map<String, Plugin> getPlugins(){
		return this.plugins;
	}
	
	/**
	 * 获取指定插件
	 * @date 2013-6-29 上午12:41:39
	 * @param id 插件ID
	 * @return
	 */
	public Plugin getPlugin(String id) {
		return this.plugins == null ? null : this.plugins.get(id);
	}
	
	/**
	 * 安装插件
	 */
	public boolean install(Plugin plugin) {
		if (this.plugins.containsKey(plugin.ID()))
			return true;
		
//		plugin.setConfigs(super.configs);
		
		//初始化插件
		try {
			plugin.init(this.config);
		} catch (Throwable e) {
			throw new RuntimeException("plugin->" + plugin.ID() + " init failed.", e);
		}
		
		//启动插件
		try {
			plugin.start();
		} catch (Throwable e){
			throw new RuntimeException("plugin->" + plugin.ID() + " start failed.", e);
		}
		
		//注册插件ID到池中
		this.plugins.put(plugin.ID(), plugin);
		
		//TODO: 使用监听器代替
		System.out.println("plugin->"+plugin.ID()+" install success.");
		
		return true;
	}
	
	/**
	 * 卸载插件
	 */
	public boolean uninstall(Plugin plugin) {
		//先关闭插件
		try {
			plugin.stop();
		} catch (Throwable e){
			throw new RuntimeException("plugin->" + plugin.ID() + " stop failed.", e);
		}
		
		//从池中去除
		this.plugins.remove(plugin.ID());
		
		//TODO: 使用监听器代替
		System.out.println("plugin->"+plugin.ID()+" uninstall success.");
		
		return true;
	}
	
	/**
	 * 升级插件 
	 */
	public boolean upgrade(Plugin plugin) {
		//先卸载插件
		uninstall(plugin);
		//然后安装插件
		install(plugin);
		
		//TODO: 使用监听器代替
		System.out.println("plugin->"+plugin.ID()+" upgrade success.");
		
		return true;
	}

	public boolean stopAll() {
		for (Iterator<Entry<String, Plugin>> it = this.plugins.entrySet().iterator(); it.hasNext(); ){
			Entry<String, Plugin> e = it.next();
			Plugin plugin =  e.getValue();
			it.remove();
			uninstall(plugin);
		}
		
		return true;
	}
}
