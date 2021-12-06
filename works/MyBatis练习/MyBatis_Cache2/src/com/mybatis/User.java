package com.mybatis;

import java.io.Serializable;
/*二级缓存可以在内存也可以在外存
 * 使用二级缓存时User类必须实现一个Serializable接口
 * */
public class User implements Serializable {
	private int id;
	private String userName;
	private int userAge;
	private String userAddress;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	
	public int getUserAge() {
		return userAge;
	}
	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
