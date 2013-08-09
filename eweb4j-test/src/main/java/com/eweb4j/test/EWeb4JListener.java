package com.eweb4j.test;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.configuration.ConfigurationFactory;
import com.eweb4j.orm.toolbox.DAOTool;

public class EWeb4JListener implements EWeb4J.Listener{
	
	/**
	 * 初始化数据
	 */
	public void onStartup(EWeb4J eweb4j) {
		ConfigurationFactory configFactory = eweb4j.getConfigFactory();
		
		User u = new User();
		u.setAccount("vivi");
		u.setPwd("123456");
		DAOTool userDao = new DAOTool();
		userDao.init(eweb4j, u);
		userDao.create();
		
		User u2 = new User();
		u2.setAccount("leo");
		u2.setPwd("321654");
		DAOTool userDao2 = new DAOTool();
		userDao2.init(eweb4j, u2);
		userDao2.create();
		
		Pets p = new Pets();
		p.setNickname("小黄");
		p.setNumber("95271");
		p.setAge(2);
		p.setUser(u);
		DAOTool petDao = new DAOTool();
		petDao.init(eweb4j, p);
		petDao.create();
		
		Pets p2 = new Pets();
		p2.setNickname("小红");
		p2.setNumber("95272");
		p2.setAge(1);
		p2.setUser(u2);
		DAOTool petDao2 = new DAOTool();
		petDao2.init(eweb4j, p2);
		petDao2.create();
		
		System.out.println("数据初始化成功......");
	}

	/**
	 * 清掉临时数据
	 */
	public void onShutdown(EWeb4J eweb4j) {
		ConfigurationFactory configFactory = eweb4j.getConfigFactory();
		
		DAOTool petDao = new DAOTool();
		petDao.init(eweb4j, Pets.class);
		petDao.clear();
		
		DAOTool userDao = new DAOTool();
		userDao.init(eweb4j, User.class);
		userDao.clear();
		
		System.out.println("数据清空成功......");
	}
}
