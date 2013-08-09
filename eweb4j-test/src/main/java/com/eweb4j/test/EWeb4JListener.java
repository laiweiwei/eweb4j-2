package com.eweb4j.test;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.orm.helper.DAOHelper;
import com.eweb4j.orm.helper.SQLHelper;

public class EWeb4JListener implements EWeb4J.Listener{
	
	/**
	 * 初始化数据
	 */
	public void onStartup(EWeb4J eweb4j) {
		ConfigurationFactory configFactory = eweb4j.getConfigFactory();
		
		User u = new User();
		u.setAccount("vivi");
		u.setPwd("123456");
		new DAOHelper(configFactory, u).create();
		
		User u2 = new User();
		u2.setAccount("leo");
		u2.setPwd("321654");
		new DAOHelper(configFactory, u2).create();
		
		Pets p = new Pets();
		p.setNickname("小黄");
		p.setNumber("95271");
		p.setAge(2);
		p.setUser(u);
		new DAOHelper(configFactory, p).create();
		
		Pets p2 = new Pets();
		p2.setNickname("小红");
		p2.setNumber("95272");
		p2.setAge(1);
		p2.setUser(u2);
		new DAOHelper(configFactory, p2).create();
		
		System.out.println("数据初始化成功......");
	}

	/**
	 * 清掉临时数据
	 */
	public void onShutdown(EWeb4J eweb4j) {
		ConfigurationFactory configFactory = eweb4j.getConfigFactory();
		
		final String eql = "delete from #table";
		new SQLHelper(configFactory, Pets.class).update(eql);
		new SQLHelper(configFactory, User.class).update(eql);
		
		System.out.println("数据清空成功......");
	}
}