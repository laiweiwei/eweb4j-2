package com.eweb4j.core.plugin;

import com.eweb4j.core.config.Config;




/**
 * 插件接口
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:05:56
 */
public abstract class Plugin {

	/**
	 * 插件ID 
	 * @date 2013-6-13 上午11:43:15
	 * @return
	 */
	public abstract String ID();
	
	/**
	 * 插件名称 
	 * @date 2013-6-13 上午11:43:24
	 * @return
	 */
	public abstract String name();
	
	/**
	 * 插件提供者 
	 * @date 2013-6-13 上午11:43:34
	 * @return
	 */
	public abstract String provider();
	
	/**
	 * 初始化插件
	 * @param config 一些配置参数
	 * @return
	 */
	public abstract void init(Config config);
	
	/**
	 * 启动插件 
	 * @date 2013-6-13 上午11:08:11
	 * @return
	 */
	public abstract void start();
	
	/**
	 * 停止插件 
	 * @date 2013-6-13 上午11:08:38
	 * @return
	 */
	public abstract void stop();
	
}
