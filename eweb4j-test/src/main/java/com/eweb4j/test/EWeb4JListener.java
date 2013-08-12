package com.eweb4j.test;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.orm.db.DAO;

public class EWeb4JListener implements EWeb4J.Listener{
	
	/**
	 * 初始化数据
	 */
	public void onStartup(EWeb4J eweb4j) {
		User u = new User();
		u.setAccount("vivi");
		u.setPwd("123456");
		DAO<User> userDao = eweb4j.getFeature("dao", u);
		userDao.create();
		
		User u2 = new User();
		u2.setAccount("leo");
		u2.setPwd("321654");
		DAO<User> userDao2 = eweb4j.getFeature("dao", u2);
		userDao2.create();
		
		Pets p = new Pets();
		p.setNickname("小黄");
		p.setNumber("95271");
		p.setAge(2);
		p.setUser(u);
		DAO<Pets> petDao = eweb4j.getFeature("dao", p);
		petDao.create();
		
		Pets p2 = new Pets();
		p2.setNickname("小红");
		p2.setNumber("95272");
		p2.setAge(1);
		p2.setUser(u2);
		DAO<Pets> petDao2 = eweb4j.getFeature("dao", p2);
		petDao2.create();
		
		System.out.println("数据初始化成功......");
	}

	/**
	 * 清掉临时数据
	 */
	public void onShutdown(EWeb4J eweb4j) {
		DAO<Pets> petDao = eweb4j.getFeature("dao", Pets.class);
		petDao.clear();
		
		DAO<User> userDao = eweb4j.getFeature("dao", User.class);
		userDao.clear();
		
		System.out.println("数据清空成功......");
	}
}
