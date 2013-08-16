package com.eweb4j.jpa.plugin;

import static com.eweb4j.core.EWeb4J.Constants.Configurations.ENTITY_RELATION_MAPPING_ID;
import static com.eweb4j.core.EWeb4J.Constants.Configurations.ORM_ID;

import java.util.List;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.configuration.Configuration;
import com.eweb4j.core.configuration.MapConfiguration;
import com.eweb4j.core.orm.EntityRelationMapping;
import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.jpa.config.JPAScanner;

/**
 * JPA 插件
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午12:02:34
 */
public class JPAPlugin extends Plugin{

	private EWeb4J eweb4j = null;

	@Override
	public void init(EWeb4J eweb4j) {
		this.eweb4j = eweb4j;
		//构建JPA实体类配置信息存放容器
		this.eweb4j.getConfigFactory().addConfiguration(ENTITY_RELATION_MAPPING_ID, new MapConfiguration<String, EntityRelationMapping>());
	}
	
	@Override
	public void start() {
		//获取ORM配置信息
		Configuration<String, ?> ormConfig = eweb4j.getConfigFactory().getConfiguration(ORM_ID);
		//获取JPA实体类配置
		List<String> entities = ormConfig.getListString("entity");
		if (entities == null) return ;
		
		for (String entity : entities) {
			try {
				Class<?> entityClass = Thread.currentThread().getContextClassLoader().loadClass(entity);
				//扫描实体类的JPA注解信息，转化成关系映射信息
				EntityRelationMapping erm = JPAScanner.scan(entityClass);
				//添加实体类的关系映射信息
				this.eweb4j.getConfigFactory().addEntityRelationMapping(entity, erm);
			} catch (Throwable e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
		this.eweb4j.getConfigFactory().getEntityRelationMappingConfig().clear();
	}

	public String ID() {
		return "EWeb4J-JPA";
	}

	public String name() {
		return ID();
	}

	public String provider() {
		return "l.weiwei@163.com";
	}
}
