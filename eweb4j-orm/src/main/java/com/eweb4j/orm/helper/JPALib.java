package com.eweb4j.orm.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class JPALib {
	
	public final static String Entity = "javax.persistence.Entity";
	public final static String Table = "javax.persistence.Table";
	public final static String Transient = "javax.persistence.Transient";
	public final static String JoinColumn = "javax.persistence.JoinColumn";
	public final static String Id = "javax.persistence.Id";
	public final static String Column = "javax.persistence.Column";
	
	public static Annotation getAnnotation(Class<?> clazz, String type) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(type);
			return clazz.getAnnotation(cls);
		} catch (Throwable e){
		}
		
		return null;
	}
	
	public static Annotation getAnnotation(Field field, String type) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(type);
			return field.getAnnotation(cls);
		} catch (Throwable e){
		}
		
		return null;
	}
	
	public static Annotation getAnnotation(Annotation[] anns, String type) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(type);
			for (Annotation a : anns) {
				if (a == null)
					continue;

				if (a.annotationType().isAssignableFrom(cls)) {
					return a;
				}
			}
		} catch (Throwable e){
		}
		
		return null;
	}
	
}
