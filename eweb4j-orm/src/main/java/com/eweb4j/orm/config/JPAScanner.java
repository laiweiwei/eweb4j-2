package com.eweb4j.orm.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.eweb4j.orm.helper.JPALib;
import com.eweb4j.orm.helper.JPAReader;
import com.eweb4j.utils.CommonUtil;
import com.eweb4j.utils.ReflectUtil;

/**
 * JPA Scanner
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午02:37:46
 */
public class JPAScanner{
	
	private final static Map<String, JPAClassInfo> cache = new HashMap<String, JPAClassInfo>(); 
	
	public synchronized final static void clear(){
		cache.clear();
	}
	
	/**
	 * 加载Entity类的JPA注解信息 
	 * @date 2013-6-13 下午03:09:21
	 * @param entityClassName
	 */
	public synchronized final static JPAClassInfo scan(Class<?> entityClass) {
		if (entityClass == null) return null;
		
		if (cache.containsKey(entityClass.getName()))
			return cache.get(entityClass.getName());
		
		Annotation entity = JPALib.getAnnotation(entityClass, JPALib.Entity);
		if (entity == null) return null;
		
		JPAClassInfo classInfo = new JPAClassInfo();
		
		Annotation table = JPALib.getAnnotation(entityClass, JPALib.Table);
		if (table != null) 
			classInfo.table = JPAReader.getValue(table, JPAReader.TableName);
		if (CommonUtil.isBlank(classInfo.table))
			classInfo.table = entityClass.getSimpleName();
		
		ReflectUtil ru = new ReflectUtil(entityClass);
		for (Field field : ru.getFields()){
			Annotation trans = JPALib.getAnnotation(field, JPALib.Transient);
			if (trans != null)
				continue;
			
			JPAFieldInfo fieldInfo = new JPAFieldInfo();
			fieldInfo.name = field.getName();
			fieldInfo.dataType = field.getType().getName();
			
			Annotation column = JPALib.getAnnotation(field, JPALib.Column);
			if (column != null){
				String columnName = JPAReader.getValue(column, JPAReader.ColumnName);
				fieldInfo.column = columnName;
			}
			
			if (CommonUtil.isBlank(fieldInfo.column))
				fieldInfo.column = fieldInfo.name;
			
			Annotation id = JPALib.getAnnotation(field, JPALib.Id);
			if (id != null && CommonUtil.isBlank(classInfo.id)){
				classInfo.id = field.getName();
				classInfo.idCol = classInfo.id;
				if (!CommonUtil.isBlank(fieldInfo.column))
					classInfo.idCol = fieldInfo.column;
			}
			
			// 若属性的类型也是一个Entity类
			Annotation tarEntity = JPALib.getAnnotation(field.getType(), JPALib.Entity);
			if (tarEntity != null){
				fieldInfo.joinEntity = field.getType();
				Annotation joinColumn = JPALib.getAnnotation(field, JPALib.JoinColumn);
				if (joinColumn != null) {
					String joinColumnName = JPAReader.getValue(joinColumn, JPAReader.JoinColumnName);
					if (!CommonUtil.isBlank(joinColumnName)) {
						fieldInfo.column = joinColumnName;
					}else {
						fieldInfo.column = field.getName()+"_id";
					}
					String referencedColumnName = JPAReader.getValue(joinColumn, JPAReader.JoinColumnReferenceName);
					fieldInfo.relCol = referencedColumnName;
				}
				//one_2_one关系
				fieldInfo.dataType = JoinType.ONE_2_ONE.toString();
			}
			
			classInfo.fieldInfos.add(fieldInfo);
		}
		
		cache.put(entityClass.getName(), classInfo);
		
		return classInfo;
	}
}
