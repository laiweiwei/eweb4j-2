package com.eweb4j.core.orm;

import java.util.Collection;

public interface Query {

	/**
	 * Retrieve all results of the query.
	 * 
	 * @param <T>
	 * @return
	 */
	<T> Collection<T> fetch();

	/**
	 * Retrieve results of query.
	 * 
	 * @param <T>
	 * @param max
	 * @return
	 */
	<T> Collection<T> fetch(int max);

	/**
	 * Retrieve a page of result.
	 * 
	 * @param <T>
	 * @param page
	 * @param length
	 * @return
	 */
	<T> Collection<T> fetch(int page, int length);

	/**
	 * 
	 * @param <T>
	 * @return
	 */
	<T> T first();
	
}
