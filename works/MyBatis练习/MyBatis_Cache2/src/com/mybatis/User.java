package com.mybatis;

import java.io.Serializable;
/*��������������ڴ�Ҳ���������
 * ʹ�ö�������ʱUser�����ʵ��һ��Serializable�ӿ�
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
