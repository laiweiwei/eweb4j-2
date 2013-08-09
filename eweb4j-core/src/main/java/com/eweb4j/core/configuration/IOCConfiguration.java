package com.eweb4j.core.configuration;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.eweb4j.core.configuration.xml.IOC;
import com.eweb4j.core.configuration.xml.Pojo;
import com.eweb4j.core.configuration.xml.Setter;

/**
 * IOC Configuration
 * @author vivi
 *
 * @param <T>
 */
public class IOCConfiguration<T> extends XMLConfiguration<T>{

	//singleton Pojo instance cache
	private Map<String, T> values = null;
	//IOC Pojo xml config cache
	private Map<String, Pojo> pojoMap = null;
	//Pojo class cache
	private Map<String, Class<?>> clsMap = new HashMap<String, Class<?>>();

	@Override
	public void init() {
		try {
			this.values = new HashMap<String, T>();
			//使用spring的simple-xml组件解析XML为POJO
			Serializer serializer = null;
			if (context == null)
				serializer = new Persister();
			else
				serializer = new Persister(context);
			
			File f = new File(xml);
			if (!f.exists()) throw new Exception("xml->"+f.getAbsolutePath()+" not found");
        	if (!f.isFile()) throw new Exception("xml->"+f.getAbsolutePath()+" is not a file");
        	IOC ioc = serializer.read(IOC.class, f);
			if (ioc == null) return ;
			
			List<Pojo> pojos = ioc.getPojos();
			if (pojos == null || pojos.isEmpty()) return;
			this.pojoMap = new HashMap<String, Pojo>(pojos.size());
			for (Pojo pojo : pojos){
				pojoMap.put(pojo.getId(), pojo);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * fetch Pojo instance
	 */
	public T get(String key, Object... args) {
		Pojo pojo = this.pojoMap.get(key);
		String scope = pojo.getScope();
		if ("singleton".equals(scope)){
			synchronized (this.values) {
				T instance = this.values.get(key);
				if (instance == null) {
					instance = this._get_pojo_instance(pojo, args);
					this.values.put(pojo.getId(), instance);
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
	private T _get_pojo_instance(Pojo pojo, Object... args){
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
					val = this._get_pojo_instance(this.pojoMap.get(refer));
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
