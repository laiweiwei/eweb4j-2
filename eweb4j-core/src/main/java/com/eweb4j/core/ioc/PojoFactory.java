package com.eweb4j.core.ioc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eweb4j.core.configuration.PojoConfiguration;
import com.eweb4j.core.configuration.xml.Pojo;
import com.eweb4j.core.configuration.xml.Setter;

/**
 * POJO工厂实现类
 * @author vivi
 *
 */
public class PojoFactory extends PojoConfiguration<Object>{

	//pojo instance
	private Map<String, Object> pojoInstances = null;
	//pojo class cache
	private Map<String, Class<?>> clsMap = null;

	public PojoFactory() {
		this.pojoInstances = new HashMap<String, Object>();
		this.clsMap = new HashMap<String, Class<?>>();
	}
	
	/**
	 * fetch Pojo instance
	 */
	public Object get(String key, Object... args) {
		Pojo pojo = super.pojoMap.get(key);
		if (pojo == null) return null;
		String scope = pojo.getScope();
		if ("singleton".equals(scope)){
			synchronized (this.pojoInstances) {
				Object instance = this.pojoInstances.get(key);
				if (instance == null) {
					instance = this._get_pojo_instance(pojo, args);
					this.pojoInstances.put(pojo.getId(), instance);
				}
				
				return instance;
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
			// 如果构造方法参数列表不为空，说明需要使用构造方法进行注入
			if (args != null && args.length > 0) {
				Class<?>[] _args = new Class<?>[args.length];
				for (int i = 0; i < args.length; i++) {
					if (Class.class.isInstance(args[i])) {
						_args[i] = (Class<?>) args[i];
					}else{
						_args[i] = args[i].getClass();
					}
				}

				pojoInstance = (T) cls.getDeclaredConstructor(_args).newInstance(args);
			} else {
				pojoInstance = (T) cls.newInstance();
			}
			
			//TODO: 暂时只做setter的注入，构造器的以后再补
			List<Setter> setters = pojo.getSetters();
			if (setters == null || setters.isEmpty()) return pojoInstance;
			for (Setter setter : setters){
				String name = setter.getName();
				String refer = setter.getRefer();
				Object val = null;
				if (refer != null && refer.trim().length() > 0){
					//递归
					val = this._get_pojo_instance(super.pojoMap.get(refer));
				} else {
					val = setter.getValue();
				}
				
				String firstUpperCase = name.toUpperCase().substring(0, 1);
				String methodName = "set" + firstUpperCase + name.substring(1);
				Method method = cls.getMethod(methodName);
				method.invoke(pojoInstance, val);
			}
			
			return pojoInstance;
		} catch (Throwable e){
			throw new RuntimeException(e);
		}
	}

}
