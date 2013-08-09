package com.eweb4j.test;

import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.eweb4j.core.SimpleEWeb4J;
import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.core.configuration.xml.EWeb4j;
import com.eweb4j.orm.helper.SQLHelper;

/**
 * 测试ORM插件功能
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:15:27
 */
public class TestORMPlugin {

	public static void main3(String[] args) throws Exception{
		
		final String xml = "src/main/resources/eweb4j-config.xml";
		
		//构建框架实例
		SimpleEWeb4J eweb4j = new SimpleEWeb4J(xml);
		
		ConfigurationFactory configFactory = eweb4j.getConfigFactory();
		
		Pets p = new Pets();
		p.setNickname("小黄2");
		p.setNumber("95278");
		p.setAge(8);
		SQLHelper db = eweb4j.getFeature("sql", p);
		
		Number number = db.update("insert into #table(#columns) values(#values)");
		System.out.println("insert->"+number);
		
		List<Pets> pets = db.query("select * from #table");
		System.out.println("pet->"+pets);
		
		pets = db.alias("p").join("user", "u")
			    .query("select p.* from #p.table p, #u.table u where #p.user = #u.id and #u.pwd = ? order by #p.id asc", 123);
		
		System.out.println("pet->"+pets);
		
		System.out.println();
		
		//停止框架
		for (int i = 10; i > 0; i--){
			System.out.println("还有 " + i + " 秒就清空数据");
			Thread.sleep(1*1000);
		}
		
		eweb4j.shutdown();
	}
	
	public static void main(String[] args){
		
	}
	
}
