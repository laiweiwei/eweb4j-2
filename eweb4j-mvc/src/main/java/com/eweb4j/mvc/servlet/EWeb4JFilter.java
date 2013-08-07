package com.eweb4j.mvc.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.configuration.Configuration;
import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.configuration.ConfigurationFactoryImpl;
import com.eweb4j.core.configuration.MapConfiguration;
import com.eweb4j.core.configuration.PropertiesConfiguration;
import com.eweb4j.core.plugin.Plugin;
import com.eweb4j.core.plugin.PluginManager;
import com.eweb4j.core.plugin.PluginManagerImpl;
import com.eweb4j.mvc.controller.WebContext;
import com.eweb4j.mvc.plugin.JSPPlugin;
import com.eweb4j.mvc.view.MVCParamNames;
import com.eweb4j.mvc.view.TemplateEngine;
import com.eweb4j.mvc.view.TemplateEngineBuilder;
import com.eweb4j.mvc.view.TemplateEngineBuilders;
import com.eweb4j.utils.CommonUtil;

public class EWeb4JFilter implements Filter{

	private FilterConfig cfg = null;
	private ServletContext servletContext = null;
	private EWeb4J eweb4j = null;
	private Configuration<String, String> baseConfig = null;
	
	private final static String class_path ;
	static {
		class_path = EWeb4JFilter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		System.out.println("----------- CLASS_PATH -----------");
		System.out.println("----------- "+class_path+" -----------");
	}
	
	public void init(FilterConfig _cfg) throws ServletException {
		//获取配置
		this.cfg = _cfg;
		this.servletContext = cfg.getServletContext();
		//网站根目录
		this.root_path = this.servletContext.getRealPath("/");
		
		System.out.println("----------- ROOT_PATH -----------");
		System.out.println("----------- "+this.root_path+" -----------");
		
		//视图文件存放目录
		this.view_path = "/WEB-INF/views";
		System.out.println("----------- VIEW_PATH -----------");
		System.out.println("----------- "+this.view_path+" -----------");
		
		//控制器包名
		this.controller_package = "controller";
		System.out.println("----------- CONTROLLER_PACKAGE -----------");
		System.out.println("----------- "+this.controller_package+" -----------");
		
		
		//构建配置容器
		final Configuration<String, Plugin> pluginsConfig = new MapConfiguration<String, Plugin>();
		
		//构建配置工厂实例
		this.baseConfig = new MapConfiguration<String, String>();
		this.baseConfig.put("root_path", this.root_path);
		this.baseConfig.put("view_path", this.view_path);
		this.baseConfig.put("controller_package", this.controller_package);
		
		final ConfigurationFactory configFactory = new ConfigurationFactoryImpl(baseConfig, "eweb4j-config.xml");
		configFactory.getConfiguration().put(MVCParamNames.view_absolute_path, root_path + view_path);
		configFactory.getConfiguration().put(MVCParamNames.view_relative_path, view_path);
		
		//构建插件管理器
		final PluginManager pluginManager = new PluginManagerImpl(pluginsConfig, configFactory);
		
		//构建框架实例
		eweb4j = new EWeb4J(pluginManager);
		
		//add listener
		String listenerClasses = cfg.getInitParameter("eweb4j_listener");
		if (listenerClasses != null && listenerClasses.trim().length() > 0) {
			for (String listenerClass : listenerClasses.split(",")) {
				try {
					@SuppressWarnings("unchecked")
					Class<EWeb4J.Listener> clazz = (Class<EWeb4J.Listener>) Thread.currentThread().getContextClassLoader().loadClass(listenerClass);
					EWeb4J.Listener listener = clazz.newInstance();
					eweb4j.addListener(listener);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
		
		//安装JSP模板引擎插件
		Plugin jspPlugin = new JSPPlugin();
		eweb4j.addPlugin(jspPlugin);
		
		//启动框架
		eweb4j.startup();
	}
	
	public void destroy() {
		//关闭框架
		eweb4j.shutdown();
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		try {
			//初始化WebContext
			WebContext ctx = WebContext.init(request, response);
			ctx.setServletContext(this.servletContext);
			ctx.setRootPath(this.root_path);
			ctx.setViewRelativePath(this.view_path);
			ctx.setViewAbsolutePath(this.root_path + this.view_path);
			//FIXME:考虑多级域名对应不同视图文件夹的功能
			final String uri = ctx.getUri();
			System.out.println("----------- URI -----------");
			System.out.println("----------- "+uri+" -----------");
			//映射URI到视图文件
			/*
			 * 映射规则：
			 * 1.先找跟URI一样的命名的File存在,若是文件，直接渲染，若是文件夹，找里面的index文件，若没有进入下一步
			 * 2.找绑定了URI的Controller.method,若有，执行它，否则交给下一个filter处理
			 */
			
			//获取当前的模板引擎建造者
			TemplateEngineBuilder engineBuilder = TemplateEngineBuilders.current();
			
			//创建模板引擎实例
			TemplateEngine engine = engineBuilder.build();
			ctx.setTemplateEngine(engine);
			
			//设置一些必要的参数
			engine.setServletContext(servletContext);
			engine.setRequest(request);
			engine.setResponse(response);
			engine.setViewRelativePath(ctx.getViewRelativePath());
			engine.setViewAbsolutePath(ctx.getViewAbsolutePath());
			
			//根据映射规则找到对应的模板文件路径
			final String path = engine.getPath(uri);
			if (path != null && path.trim().length() > 0){
				//若找到了路径，渲染该模板内容作为HTTP响应
				System.out.println("----------- VIEW_FILE_FOUND -----------");
				System.out.println("----------- "+path+" -----------");
				Map<String, Object> datas = new HashMap<String, Object>();
				datas.put("ctx", ctx);
				datas.put("request", request);
				datas.put("response", response);
				datas.put("request_uri", uri);
				
				//渲染模板，并输出到HTTP响应流
				engine.target(path).render(datas);
				return;
			}
			
			//映射URI到Pojo
			/*
			 *  /users/hello -> {controller_package}+Users.class#hello
			 */
			String[] uris = uri.split("/");
			if (uris.length == 3) {
				String controllerClassName = this.controller_package+"."+CommonUtil.toUpCaseFirst(uris[1]);
				System.out.println("----------- CONTROLLER_CLASS_NAME -----------");
				System.out.println("----------- "+controllerClassName+" -----------");
				String methodName = uris[2];
				Class<?> controllerClass = null;
				Object controller = null;
				Method method = null;
				try {
					controllerClass = Thread.currentThread().getContextClassLoader().loadClass(controllerClassName);
				} catch (Throwable e){
					e.printStackTrace();
				}
				
				if (controllerClass != null) {
					System.out.println("----------- CONTROLLER_CLASS -----------");
					System.out.println("----------- "+controllerClass+" -----------");
					try {
						controller = controllerClass.newInstance();
					} catch (Throwable e){
						e.printStackTrace();
					}
					if (controller != null) {
						System.out.println("----------- CONTROLLER -----------");
						System.out.println("----------- "+controller+" -----------");
						try {
							method = controllerClass.getMethod(methodName);
						} catch (Throwable e){
							e.printStackTrace();
						}
					}
				}
				
				Object output = null;
				if (method != null){
					System.out.println("----------- CONTROLLER_METHOD -----------");
					System.out.println("----------- "+method+" -----------");
					try {
						output = method.invoke(controller);
					} catch (Throwable e){
						e.printStackTrace();
					}
				}
				
				if (output != null) {
					System.out.println("----------- METHOD_OUTPUT -----------");
					System.out.println("----------- "+output+" -----------");
					ctx.getWriter().print(output);
					return ;
				}
			}
			//继续往下执行URL
			chain.doFilter(req, res);
		} catch (Throwable e) {
			e.printStackTrace();
		}finally{
			
		}
	}

	public static void main(String[] args){
		System.out.println("/users/hello".split("/")[1]);
	}
	
}
