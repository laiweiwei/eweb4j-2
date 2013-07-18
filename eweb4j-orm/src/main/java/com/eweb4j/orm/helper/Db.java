package com.eweb4j.orm.helper;

public class Db {

	public static <T> QueryHelper<T> ar(Class<T> model){
		return new QueryHelper<T>(model);
	}
	
}
