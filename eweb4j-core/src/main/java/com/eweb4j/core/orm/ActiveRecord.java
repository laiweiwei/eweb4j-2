package com.eweb4j.core.orm;

import java.util.List;

public abstract class ActiveRecord {

	/**
	 * 创建一条记录
	 * @param field
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecord> T create(String... field){
		
		return (T)this;
	}

	/**
	 * 保存一条记录，若有ID，会更新
	 * @param field
	 * @return
	 */
	public boolean save(String... field){
	
		return true;
	}

	/**
	 * 根据当前实体的ID值来删除自己
	 */
	public boolean delete(){
		
		return true;
	}

	/**
	 * 根据当前实体ID值去查询数据库
	 */
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecord> T load(){
		
		return (T)this;
	}

	public boolean delete(String query, Object... params) {
		return true;
	}

	public boolean deleteAll(){
		return false;
	}

	@SuppressWarnings("unchecked")
	public <T extends ActiveRecord> T findById(long id) {
		
		return (T) this;
	}

	public Query find(){
		return null;
	}

	public Query find(String query, Object... args){
		return null;
	}

	public <T extends ActiveRecord> List<T> findAll(){
	
		return null;
	}

	public long count() {
		return 0;
	}

	public long count(String query, Object... args) {
		return 0;
	}
	
}
