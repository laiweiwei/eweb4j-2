package com.eweb4j.test;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.orm.helper.QueryHelper;

public class EWeb4JListener implements EWeb4J.Listener{
	
	/**
	 * 初始化数据
	 */
	public void onStartup(EWeb4J eweb4j) {
		ConfigurationFactory configFactory = eweb4j.getPluginManager().getConfigFactory();
		
		final String eql = "insert into #table(#columns) values(#values)";
		
		User u = new User();
		u.setAccount("vivi");
		u.setPwd("123456");
		Number id = new QueryHelper<User>(u, configFactory).update(eql);
		u.setId(id.longValue());
		
		User u2 = new User();
		u2.setAccount("leo");
		u2.setPwd("321654");
		id = new QueryHelper<User>(u2, configFactory).update(eql);
		u2.setId(id.longValue());
		
		Pets p = new Pets();
		p.setNickname("小黄");
		p.setNumber("95271");
		p.setAge(2);
		p.setUser(u);
		new QueryHelper<Pets>(p, configFactory).update(eql);
		
		Pets p2 = new Pets();
		p2.setNickname("小红");
		p2.setNumber("95272");
		p2.setAge(1);
		p2.setUser(u2);
		new QueryHelper<Pets>(p2, configFactory).update(eql);
		
		System.out.println("数据初始化成功......");
	}

	/**
	 * 清掉临时数据
	 */
	public void onShutdown(EWeb4J eweb4j) {
		ConfigurationFactory configFactory = eweb4j.getPluginManager().getConfigFactory();
		
		final String eql = "delete from #table";
		new QueryHelper<Pets>(Pets.class, configFactory).update(eql);
		new QueryHelper<User>(User.class, configFactory).update(eql);
		
		System.out.println("数据清空成功......");
	}
}
