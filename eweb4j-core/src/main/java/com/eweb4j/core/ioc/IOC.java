package com.eweb4j.core.ioc;

import com.eweb4j.core.EWeb4J;

public interface IOC {

	public <T> T getInstance(String key, Object... args);
	
	public void setEWeb4J(EWeb4J eweb4j);
	
}
