package com.eweb4j.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 宠物实体类
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-13 上午11:56:52
 */
@Entity
@Table(name="t_pet")
public class Pets {

	@Id
	private Long id;
	
	@Column(name="num")
	private String number;
	
	@Column(name="name")
	private String nickname;
	
	private int age;
	
	@ManyToOne
	@JoinColumn(name="userid")
	private User user;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Pets [id=" + id + ", number=" + number + ", nickname="
				+ nickname + ", age=" + age + ", user=" + user + "]";
	}

}
