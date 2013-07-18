package com.eweb4j.orm.plugin;

import com.eweb4j.core.config.Config;
import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.orm.config.JPAScanner;

/**
 * ORM插件
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午12:02:34
 */
public class ORMPlugin extends Plugin{

	public String ID() {
		return "EWeb4J-ORM";
	}

	public String name() {
		return ID();
	}

	public String provider() {
		return "l.weiwei@163.com";
	}

	@Override
	public void init(Config config) {
		// TODO Auto-generated method stub
		
	}
	
	public void start() {
		//扫描${ClassPath}里所类文件
		
		//扫描${Lib}里jar文件所有类文件
		
		//加载Entity实体类的JPA注解信息
//		JPAConfig.loadEntity(Pets.class);
	}

	public void stop() {
		JPAScanner.clear();
	}

}
