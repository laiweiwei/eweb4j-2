package com.eweb4j.core.ioc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.configuration.Configuration;
import com.eweb4j.core.configuration.xml.Constructor;
import com.eweb4j.core.configuration.xml.Pojo;
import com.eweb4j.core.configuration.xml.Setter;

/**
 * POJO工厂实现类
 * @author vivi
 *
 */
public class EWeb4JIOC implements IOC{

	private final static String ctxRegex = "(?<=\\$\\{).*(?=\\})";
	private final static String argRegex = "(?<=\\$\\{args\\[)\\d+(?=\\]\\})";
	
	private EWeb4J eweb4j = null;
	
	//pojo instance
	private Map<String, Object> pojoInstances = new HashMap<String, Object>();
	//pojo class cache
	private Map<String, Class<?>> clsMap = new HashMap<String, Class<?>>();
	
	private Configuration<String, Pojo> configHolder = null;
	
	public EWeb4JIOC(Configuration<String, Pojo> iocConfig) {
		this.configHolder = iocConfig;
	}
	
	public EWeb4JIOC(EWeb4J eweb4j) {
		this.eweb4j = eweb4j;
		this.configHolder = eweb4j.getConfigFactory().getIOCConfig();
	}

	/**
	 * fetch Pojo instance
	 */
	@SuppressWarnings("unchecked")
	public <T> T getInstance(String key, Object... args) {
		Pojo pojo = this.configHolder.get(key);
		if (pojo == null) return null;
		String scope = pojo.getScope();
		if ("singleton".equals(scope)){
			synchronized (this.pojoInstances) {
				Object instance = this.pojoInstances.get(key);
				if (instance == null) {
					instance = this._get_pojo_instance(pojo, args);
					this.pojoInstances.put(pojo.getId(), instance);
				}
				
				return (T) instance;
			}
		}
		
		return this._get_pojo_instance(pojo, args);
	}
	
	/**
	 * 获取pojo的实例
	 * @param pojo
	 * @param args 构造器参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T _get_pojo_instance(Pojo pojo, Object... args){
		try {
			String clazz = pojo.getClazz();
			Class<?> cls = this.clsMap.get(pojo.getId());
			if (cls == null) {
				cls = Thread.currentThread().getContextClassLoader().loadClass(clazz);
				this.clsMap.put(pojo.getId(), cls);
			}
			
			T pojoInstance = null;
			
			
			// 使用构造方法进行实例化
			List<Constructor> constructors = pojo.getConstructors();
			if (constructors != null && !constructors.isEmpty()) {
				List<Class<?>> argTypes = new ArrayList<Class<?>>(constructors.size());
				List<Object> argValues = new ArrayList<Object>(constructors.size());
				for (Constructor c : constructors){
					String refer = c.getRefer();
					String type = c.getType();
					Class<?> argType = null;
					if (type != null && type.trim().length() > 0) {
						argType = Thread.currentThread().getContextClassLoader().loadClass(type);
						argTypes.add(argType);
					}
					if (refer == null || refer.trim().length() == 0){
						//处理基本类型
						String val = c.getValue();
						argValues.add(val);
						if (argType == null) argTypes.add(String.class);
					}else {
						//处理${}上下文变量
						Pattern p = Pattern.compile(ctxRegex, Pattern.DOTALL);
						Matcher m = p.matcher(refer);
						if (m.find()) {
							String ctxName = m.group();
							if ("eweb4j".equals(ctxName)){
								argValues.add(this.eweb4j);
								if (argType == null) argTypes.add(EWeb4J.class);
							}else if ("args".equals(ctxName)) {
								argValues.add(args);
								if (argType == null) argTypes.add(Object[].class);
							}else {
								Pattern p2 = Pattern.compile(argRegex, Pattern.DOTALL);
								Matcher m2 = p2.matcher(refer);
								if (m2.find()) {
									String index = m2.group();
									int i = Integer.parseInt(index);
									argValues.add(args[i]);
									if (argType == null) argTypes.add(args[i].getClass());
								}
							}
						} else {
							//处理普通的refer
							//递归
							Pojo _pojo = this.configHolder.get(refer);
							Object val = this._get_pojo_instance(_pojo, args);
							argValues.add(val);
							if (argType == null) argTypes.add(val.getClass());
						}
					}
				}
				
				pojoInstance = (T) cls.getDeclaredConstructor(argTypes.toArray(new Class<?>[]{})).newInstance(argValues.toArray());
			} else {
				pojoInstance = (T) cls.newInstance();
			}
			
			List<Setter> setters = pojo.getSetters();
			if (setters == null || setters.isEmpty()) return pojoInstance;
			for (Setter setter : setters){
				String name = setter.getName();
				String refer = setter.getRefer();
				String type = setter.getType();
				Class<?> argType = null;
				if (type != null && type.trim().length() > 0) {
					Class<?> typeCls = Thread.currentThread().getContextClassLoader().loadClass(type);
					argType = typeCls;
				}
				Object argValue = null;
				if (refer == null || refer.trim().length() == 0){
					//处理基本类型
					String val = setter.getValue();
					argValue = val;
					if (argType == null) argType = String.class;
				}else {
					if (refer.matches(ctxRegex)) {
						//处理${}上下文变量
						Pattern p = Pattern.compile(ctxRegex, Pattern.DOTALL);
						Matcher m = p.matcher(refer);
						m.find();
						String ctxName = m.group();
						if ("eweb4j".equals(ctxName)){
							argValue = this.eweb4j;
							if (argType == null) argType = EWeb4J.class;
						}else if ("args".equals(ctxName)) {
							argValue = args;
							if (argType == null) argType = Object[].class;
						}else {
							Pattern p2 = Pattern.compile(argRegex, Pattern.DOTALL);
							Matcher m2 = p2.matcher(refer);
							if (m2.find()){
								String index = m2.group();
								int i = Integer.parseInt(index);
								argValue = args[i];
								if (argType == null) argType = argValue.getClass();
							}
						} 
					} else {
						//处理普通的refer
						//递归
						Pojo _pojo = this.configHolder.get(refer);
						Object _val = this._get_pojo_instance(_pojo, args);
						argValue = _val;
						if (argType == null) argType = _val.getClass();
					}
				}
				
				String firstUpperCase = name.toUpperCase().substring(0, 1);
				String methodName = "set" + firstUpperCase + name.substring(1);
				Method method = cls.getMethod(methodName, argType);
				method.invoke(pojoInstance, argValue);
			}
			
			return pojoInstance;
		} catch (Throwable e){
			throw new RuntimeException(e);
		}
	}

	public Map<String, Object> getMap() {
		return this.pojoInstances;
	}
	
	public static void main(String[] args){
		//处理${}上下文变量
		Pattern p = Pattern.compile(ctxRegex, Pattern.DOTALL);
		Matcher m = p.matcher("${eweb4j}");
		boolean find = m.find();
		if (find) {
			String ctxName = m.group();
			System.out.println(ctxName);
		}
	}

	public void setEWeb4J(EWeb4J eweb4j) {
		this.eweb4j = eweb4j;
	}

}
