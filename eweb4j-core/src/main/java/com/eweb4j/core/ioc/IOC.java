package com.eweb4j.core.ioc;

import com.eweb4j.core.configuration.Configuration;

public interface IOC {

	public void setConfigHolder(Configuration<String, ?> config);
	
	public <T> T getInstance(String key, Object... args);
	
}
