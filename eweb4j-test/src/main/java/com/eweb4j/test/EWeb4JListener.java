package com.eweb4j.test;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.ioc.IOC;
import com.eweb4j.orm.db.DAO;

public class EWeb4JListener implements EWeb4J.Listener{
	
	private IOC ioc = null;
	
	/**
	 * 初始化数据
	 */
	public void onStartup(EWeb4J eweb4j) {
		this.ioc = eweb4j.getIOC();
		User u = new User();
		u.setAccount("vivi");
		u.setPwd("123456");
		
		DAO userDao = ioc.getInstance("dao", u);
		userDao.create();
		
		User u2 = new User();
		u2.setAccount("leo");
		u2.setPwd("321654");
		DAO userDao2 = ioc.getInstance("dao", u2);
		userDao2.create();
		
		Pets p = new Pets();
		p.setNickname("小黄");
		p.setNumber("95271");
		p.setAge(2);
		p.setUser(u);
		DAO petDao = ioc.getInstance("dao", p);
		petDao.create();
		
		Pets p2 = new Pets();
		p2.setNickname("小红");
		p2.setNumber("95272");
		p2.setAge(1);
		p2.setUser(u2);
		DAO petDao2 = ioc.getInstance("dao", p2);
		petDao2.create();
		
		System.out.println("数据初始化成功......");
	}

	/**
	 * 清掉临时数据
	 */
	public void onShutdown(EWeb4J eweb4j) {
		DAO petDao = ioc.getInstance("dao", Pets.class);
		petDao.clear();
		
		DAO userDao = ioc.getInstance("dao", User.class);
		userDao.clear();
		
		System.out.println("数据清空成功......");
	}
}
