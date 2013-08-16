package com.eweb4j.jpa.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.eweb4j.core.orm.EntityRelationMapping;
import com.eweb4j.core.orm.FieldRelationMapping;
import com.eweb4j.core.orm.JoinType;
import com.eweb4j.core.util.CommonUtil;
import com.eweb4j.core.util.ReflectUtil;

/**
 * JPA Scanner
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 下午02:37:46
 */
public class JPAScanner{
	
	/**
	 * 加载Entity类的JPA注解信息 
	 * @date 2013-6-13 下午03:09:21
	 * @param entityClass
	 */
	public final static EntityRelationMapping scan(Class<?> entityClass) {
		if (entityClass == null) return null;
		
		Annotation entity = JPALib.getAnnotation(entityClass, JPALib.Entity);
		if (entity == null) return null;
		
		EntityRelationMapping classInfo = new JPAEntityRelationMapping();
		
		Annotation table = JPALib.getAnnotation(entityClass, JPALib.Table);
		if (table != null) 
			classInfo.setTable(JPAReader.getValue(table, JPAReader.TableName));
		if (CommonUtil.isBlank(classInfo.getTable()))
			classInfo.setTable(entityClass.getSimpleName());
		
		ReflectUtil ru = new ReflectUtil(entityClass);
		for (Field field : ru.getFields()){
			Annotation trans = JPALib.getAnnotation(field, JPALib.Transient);
			if (trans != null)
				continue;
			
			FieldRelationMapping fieldInfo = new JPAFieldRelationMapping();
			fieldInfo.setName(field.getName());
			fieldInfo.setDataType(field.getType().getName());
			
			Annotation column = JPALib.getAnnotation(field, JPALib.Column);
			if (column != null){
				String columnName = JPAReader.getValue(column, JPAReader.ColumnName);
				fieldInfo.setColumn(columnName);
			}
			
			if (CommonUtil.isBlank(fieldInfo.getColumn()))
				fieldInfo.setColumn(fieldInfo.getName());
			
			Annotation id = JPALib.getAnnotation(field, JPALib.Id);
			if (id != null && CommonUtil.isBlank(classInfo.getId())){
				classInfo.setId(field.getName());
				classInfo.setIdCol(classInfo.getId());
				if (!CommonUtil.isBlank(fieldInfo.getColumn()))
					classInfo.setIdCol(fieldInfo.getColumn());
			}
			
			// 若属性的类型也是一个Entity类
			Annotation tarEntity = JPALib.getAnnotation(field.getType(), JPALib.Entity);
			if (tarEntity != null){
				fieldInfo.setJoinEntity(field.getType());
				Annotation joinColumn = JPALib.getAnnotation(field, JPALib.JoinColumn);
				if (joinColumn != null) {
					String joinColumnName = JPAReader.getValue(joinColumn, JPAReader.JoinColumnName);
					if (!CommonUtil.isBlank(joinColumnName)) {
						fieldInfo.setColumn(joinColumnName);
					}else {
						fieldInfo.setColumn(field.getName()+"_id");
					}
					String referencedColumnName = JPAReader.getValue(joinColumn, JPAReader.JoinColumnReferenceName);
					fieldInfo.setRelCol(referencedColumnName);
				}
				//one_2_one关系
				fieldInfo.setDataType(JoinType.ONE_2_ONE.toString());
			}
			
			classInfo.getFieldRelationMappings().add(fieldInfo);
		}
		
		return classInfo;
	}
}
