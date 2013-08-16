package com.eweb4j.core.plugin;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.configuration.Configuration;


/**
 * 插件管理器
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:05:32
 */
public abstract class PluginManager {

	protected EWeb4J eweb4j;
	
	public void setEWeb4J(EWeb4J eweb4j){
		this.eweb4j = eweb4j;
	}
	
	/**
	 * 获取插件仓库
	 * @return
	 */
	public abstract Configuration<String, Plugin> getPlugins();
	
	/**
	 * 获取指定插件
	 * @date 2013-6-29 上午12:41:39
	 * @param id 插件ID
	 * @return
	 */
	public abstract Plugin getPlugin(String pluginID);
	
	/**
	 * 安装插件 
	 * @date 2013-6-13 上午11:08:54
	 * @param pluginID
	 * @param plugin
	 */
	public abstract boolean install(String pluginID, Plugin plugin);
	
	/**
	 * 卸载插件 
	 * @date 2013-6-13 上午11:10:13
	 * @param plugin
	 * @return
	 */
	public abstract boolean uninstall(String pluginID, Plugin plugin);

	/**
	 * 升级插件
	 * @date 2013-6-13 上午11:10:53
	 * @param pluginID
	 * @param plugin
	 * @return
	 */
	public abstract boolean upgrade(String pluginID, Plugin plugin);
	
	/**
	 * 停止所有插件 
	 * @date 2013-6-29 上午12:36:34
	 * @return
	 */
	public abstract boolean stopAll();
	
}
