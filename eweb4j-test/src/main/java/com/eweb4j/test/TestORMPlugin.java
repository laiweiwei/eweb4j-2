package com.eweb4j.test;

import java.util.List;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.SimpleEWeb4J;
import com.eweb4j.core.ioc.IOC;
import com.eweb4j.orm.db.DAO;

/**
 * 测试ORM插件功能
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:15:27
 */
public class TestORMPlugin {

	public static void main(String[] args) throws Exception{
		
		final String xml = "src/main/resources/eweb4j-config.xml";
		
		//构建框架实例
		EWeb4J eweb4j = new SimpleEWeb4J(xml);
		//获取IOC容器
		IOC ioc = eweb4j.getIOC();
		Pets p = new Pets();
		p.setNickname("小黄2");
		p.setNumber("95278");
		p.setAge(8);
		DAO dao = ioc.getInstance("dao", p);
		
		//这种行为会将插入后的newID注入到p对象
		dao.create();
		
		p.setAge(22);
		p.setNickname("小黄黄");
		
		//这种行为不会自动将产生的newID注入到p对象
		Number newID = dao.update("insert into #table(#columns) values(#values)");
		System.out.println("insert->"+newID);
		
		//由于前面的插入操作并没有把newID更新，因此这里更新的是小黄2这条记录而不是小黄黄
		p.setAge(55);
		dao.updateFields("age");
		
		List<Pets> pets = dao.query("select * from #table");
		System.out.println("pet->"+pets);
		
		pets = dao.alias("p").join("user", "u")
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
	
}
