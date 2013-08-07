package com.eweb4j.orm.plugin;

import java.util.List;

import com.eweb4j.core.configuration.PropertiesConfiguration;
import com.eweb4j.core.configuration.Configuration;
import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.orm.config.JPAClassInfo;
import com.eweb4j.orm.config.JPAScanner;

/**
 * ORM插件
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午12:02:34
 */
public class ORMPlugin extends Plugin{

	private JPAScanner scanner = null;
	private Configuration<String, Object> config = null;
	
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
	public void init(Configuration<String, Object> config) {
		this.config = config;
		//构建JPA实体类配置信息存放容器
		Configuration<String, JPAClassInfo> jpaStorage = new PropertiesConfiguration<String, JPAClassInfo>();
		this.config.put(JPAClassInfo.STORAGE_KEY, jpaStorage);
		
		//构建JPA扫描器
		this.scanner = new JPAScanner(jpaStorage);
	}
	
	public void start() {
		//扫描${ClassPath}里所类文件
		
		//扫描${Lib}里jar文件所有类文件
		
		//从配置仓库里获取JPA实体类的类路径
		List<String> entities = this.config.getListString("jpa.entities", ",");
		if (entities == null) return ;
		
		for (String entity : entities) {
			try {
				//加载Entity实体类的JPA注解信息
				Class<?> entityClass = Thread.currentThread().getContextClassLoader().loadClass(entity);
				//扫描实体类
				scanner.scan(entityClass);
			} catch (Throwable e){
				e.printStackTrace();
			}
		}
		
	}

	public void stop() {
		scanner.clear();
	}

}
