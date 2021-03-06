package com.eweb4j.test;

import java.util.List;

import com.eweb4j.core.EWeb4J;
import com.eweb4j.core.ioc.EWeb4JIOC;
import com.eweb4j.core.ioc.IOC;
import com.eweb4j.core.ioc.IOCConfiguration;
import com.eweb4j.core.orm.DAO;

public class TestIOC {

	public static void main(String[] args) throws Exception{
		
    	IOC ioc = new EWeb4JIOC(new IOCConfiguration("classpath:eweb4j-ioc-2.xml"));
    	
    	DAO dao = ioc.getInstance("dao", Pets.class); 
    	
    	List<Pets> pets = dao.query("select * from #table");
    	System.out.println(pets);
    	
    	User user = ioc.getInstance("user");
    	System.out.println("user->"+user);
    	
    	EWeb4J eweb4j = ioc.getInstance("eweb4j");
    	eweb4j.shutdown();
	}
	
}
