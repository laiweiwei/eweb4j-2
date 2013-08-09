package com.eweb4j.orm.plugin;

import static com.eweb4j.core.EWeb4J.Constants.Configurations.JPA_ID;
import static com.eweb4j.core.EWeb4J.Constants.Configurations.ORM_ID;

import java.util.List;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.configuration.Configuration;
import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.configuration.MapConfiguration;
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
	private Configuration<String, Object> ormConfig = null;
	
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
	public void init(EWeb4J eweb4j) {
		ConfigurationFactory configFactory = eweb4j.getConfigFactory();
		this.ormConfig = configFactory.getConfiguration(ORM_ID);
		
		//构建JPA实体类配置信息存放容器
		Configuration<String, JPAClassInfo> jpaConfig = configFactory.getConfiguration(JPA_ID);
		if (jpaConfig == null) {
			jpaConfig = new MapConfiguration<String, JPAClassInfo>();
			configFactory.addConfiguration(JPA_ID, jpaConfig);
		}
		
		//构建JPA扫描器
		this.scanner = new JPAScanner(jpaConfig);
	}
	
	public void start() {
		//从配置文件里获取JPA实体类配置
		List<String> entities = this.ormConfig.getListString("entity");
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
