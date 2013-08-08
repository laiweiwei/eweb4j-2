package com.eweb4j.test;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.EWeb4JImpl;
import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.configuration.ConfigurationFactoryImpl;
import com.eweb4j.core.plugin.PluginManager;
import com.eweb4j.core.plugin.PluginManagerImpl;
import com.eweb4j.orm.helper.QueryHelper;
import com.eweb4j.orm.plugin.ORMPlugin;
import com.eweb4j.plugins.DruidPlugin;

/**
 * 测试ORM插件功能
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:15:27
 */
public class TestORMPlugin {

	public static void main(String[] args) throws Exception{
		
		//构建配置工厂实例
		final ConfigurationFactory configFactory = new ConfigurationFactoryImpl("src/main/resources/eweb4j-config.xml");
		
		//构建插件管理器
		final PluginManager pluginManager = new PluginManagerImpl(configFactory);
		
		//构建框架实例
		EWeb4J eweb4j = new EWeb4JImpl(pluginManager);
		
		//添加ORM插件
		eweb4j.addPlugin(new ORMPlugin());
		
		//安装 DRUID 插件
		eweb4j.addPlugin(new DruidPlugin());
		
		//准备完毕，启动框架
		eweb4j.startup();
		
		Pets p = new Pets();
		p.setNickname("小黄2");
		p.setNumber("95278");
		p.setAge(8);
		QueryHelper<Pets> db = new QueryHelper<Pets>(p, configFactory);
		
		Number number = db.update("insert into #table(#columns) values(#values)");
		System.out.println("insert->"+number);
		
//		List<Pets> pets = db.query("select * from #table where #nickname = ?", "testName");
//		System.out.println("pet->"+pets);
//		
//		pets = db.alias("p").join("user", "u")
//			    .query("select p.* from #p.table p, #u.table u where #p.user = #u.id and #u.pwd = ? order by #p.id asc", 123);
//		
//		System.out.println("pet->"+pets);
		
		System.out.println();
		
		//停止框架
		for (int i = 10; i > 0; i--){
			System.out.println("还有 " + i + " 秒就清空数据");
			Thread.sleep(1*1000);
		}
		
		eweb4j.shutdown();
	}
	
}
