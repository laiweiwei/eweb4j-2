package com.eweb4j.test;

import java.util.List;

import com.eweb4j.core.ioc.EWeb4JIOC;
import com.eweb4j.core.ioc.IOC;
import com.eweb4j.core.ioc.IOCConfiguration;
import com.eweb4j.orm.db.DAO;

public class TestIOC {

	public static void main(String[] args) throws Exception{
    	IOC ioc = new EWeb4JIOC(new IOCConfiguration("d:/eweb4j-ioc.xml"));
    	DAO dao = ioc.getInstance("dao", Pets.class);
    	List<Pets> pets = dao.query("select * from #table");
    	System.out.println(pets);
	}
	
}