package com.eweb4j.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.jdbc.JDBCRow;
import com.eweb4j.orm.config.JPAClassInfo;
import com.eweb4j.orm.config.JPAFieldInfo;
import com.eweb4j.orm.config.JoinType;
import com.eweb4j.utils.ClassUtil;
import com.eweb4j.utils.ReflectUtil;

public class PojoMappings {

	private Class<?> cls;
	private ConfigurationFactory configFactory = null;
	
	public PojoMappings(Class<?> cls, ConfigurationFactory configFactory){
		this.cls = cls;
		this.configFactory = configFactory;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> mapping(List<JDBCRow> rows) {
		if (rows == null || rows.isEmpty()) 
			return null;
		
		JPAClassInfo jpaInfo = this.configFactory.getJPA(cls);
		List<T> list = new ArrayList<T>();
		T t = null;
		for (JDBCRow row : rows) {
			try {
				t = (T) cls.newInstance();
				ReflectUtil ru = new ReflectUtil(t);
				for (JPAFieldInfo jpa : jpaInfo.fieldInfos) {
					String relCol = jpa.relCol;
					String dataType = jpa.dataType;
					String colName = jpa.column;
					String fieldName = jpa.name;
	
					Object value = row.value(colName);
					if (value == null)
						continue;
					
					Method m = ru.getSetter(fieldName);
					if (m == null)
						continue;
	
					String v = String.valueOf(value);
					if (v == null) {
						v = "";
					}
					
					if ("int".equalsIgnoreCase(dataType) || "java.lang.Integer".equalsIgnoreCase(dataType)) {
						if ("".equals(v.trim())) {
							v = "0";
						}
						if (value instanceof Boolean)
							v = ((Boolean)value ? "1" : "0");
						
						m.invoke(t, Integer.parseInt(v));
					} else if ("long".equalsIgnoreCase(dataType) || "java.lang.Long".equalsIgnoreCase(dataType)) {
						if ("".equals(v.trim())) {
							v = "0";
						}
						if (value instanceof Boolean)
							v = ((Boolean)value ? "1" : "0");
						
						m.invoke(t, Long.parseLong(v));
					} else if ("float".equalsIgnoreCase(dataType) || "java.lang.Float".equalsIgnoreCase(dataType)) {
						if ("".equals(v.trim())) {
							v = "0.0";
						}
						if (value instanceof Boolean)
							v = ((Boolean)value ? "1.0" : "0.0");
						
						m.invoke(t, Float.parseFloat(v));
					} else if ("double".equalsIgnoreCase(dataType) || "java.lang.Double".equalsIgnoreCase(dataType)) {
						if ("".equals(v.trim())) {
							v = "0.0";
						}
						if (value instanceof Boolean)
							v = ((Boolean)value ? "1.0" : "0.0");
						m.invoke(t, Double.parseDouble(v));
					} else if ("string".equalsIgnoreCase(dataType) || "java.lang.String".equalsIgnoreCase(dataType)) {
						m.invoke(t, v);
					} else if ("boolean".equalsIgnoreCase(dataType) || "java.lang.Boolean".equalsIgnoreCase(dataType)){
						if ("1".equals(v.trim()) || "true".equals(v.trim())){
							m.invoke(t, true);
						}else if ("0".equals(v.trim()) || "false".equals(v.trim())){
							m.invoke(t, false);
						}
					} else if ("date".equalsIgnoreCase(dataType) || "java.sql.Date".equalsIgnoreCase(dataType) || "java.util.Date".equalsIgnoreCase(dataType)) {
						m.invoke(t, value);
					} else if ("timestamp".equalsIgnoreCase(dataType) || "java.sql.Timestamp".equalsIgnoreCase(dataType)) {
						m.invoke(t, value);
					} else if ("time".equalsIgnoreCase(dataType) || "java.sql.Time".equalsIgnoreCase(dataType)) {
						m.invoke(t, value);
					} else if ("byte[]".equalsIgnoreCase(dataType) || "[Ljava.lang.Byte;".equalsIgnoreCase(dataType)) {
						m.invoke(t, value);
					} else if (JoinType.ONE_2_ONE.toString().equalsIgnoreCase(dataType) || JoinType.MANY_2_ONE.toString().equalsIgnoreCase(dataType)) {
						if ("".equals(v))
							continue;
						Field field = ru.getField(fieldName);
						Class<?> tarClass = field.getType();
						Object tarObj = tarClass.newInstance();
						JPAClassInfo tarJpa = this.configFactory.getJPA(tarClass);
						String relField = null;
						if (relCol == null || relCol.trim().length() == 0){
							relCol = tarJpa.idCol;
							jpa.relCol = relCol;
							relField = tarJpa.id;
						} else {
							JPAFieldInfo tarFieldInfo = tarJpa.getFieldInfoByColumn(relCol);
							relField = tarFieldInfo.name;
						}
						
						tarObj = ClassUtil.injectFieldValue(tarObj, relField, new String[] { v });
						m.invoke(t, tarObj);
	
					} else if (JoinType.ONE_2_MANY.toString().equalsIgnoreCase(dataType)) {
	
					} else if (JoinType.MANY_2_MANY.toString().equalsIgnoreCase(dataType)) {
	
					} else if (!"".equals(dataType)) {
						m.invoke(t, String.valueOf(value));
					}
				}
				
				list.add(t);
			} catch (Throwable e){
				e.printStackTrace();
			}
		}

		return list.isEmpty() ? null : list;
	}
}
