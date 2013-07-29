package com.eweb4j.core.plugin;

import com.eweb4j.core.configurator.Storage;


/**
 * 插件管理器
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:05:32
 */
public abstract class PluginManager {

	/**
	 * 获取配置仓库
	 * @return
	 */
	public abstract Storage<String, Object> getConfigStorage();
	
	/**
	 * 获取插件仓库
	 * @return
	 */
	public abstract Storage<String, Plugin> getPluginStorage();
	
	/**
	 * 获取指定插件
	 * @date 2013-6-29 上午12:41:39
	 * @param id 插件ID
	 * @return
	 */
	public abstract Plugin getPlugin(String id);
	
	/**
	 * 安装插件 
	 * @date 2013-6-13 上午11:08:54
	 * @param plugin
	 */
	public abstract boolean install(Plugin plugin);
	
	/**
	 * 卸载插件 
	 * @date 2013-6-13 上午11:10:13
	 * @param plugin
	 * @return
	 */
	public abstract boolean uninstall(Plugin plugin);

	/**
	 * 升级插件
	 * @date 2013-6-13 上午11:10:53
	 * @param plugin
	 * @return
	 */
	public abstract boolean upgrade(Plugin plugin);
	
	/**
	 * 停止所有插件 
	 * @date 2013-6-29 上午12:36:34
	 * @return
	 */
	public abstract boolean stopAll();
	
}
