package com.SSSP.bean;
import javax.persistence.*;
@Entity
@Table(name="t_user")
public class AllUserBean {
private String user_name;
private String user_password;
@Id
@GeneratedValue
private Integer user_id;
@Column(name="flag")
private int user_flag=1;
private double balance;
public String getUser_name() {
	return user_name;
}
public void setUser_name(String user_name) {
	this.user_name = user_name;
}
public String getUser_password() {
	return user_password;
}
public void setUser_password(String user_password) {
	this.user_password = user_password;
}
public Integer getUser_id() {
	return user_id;
}
public void setUser_id(Integer user_id) {
	this.user_id = user_id;
}
public int getUser_flag() {
	return user_flag;
}
public void setUser_flag( int user_flag) {
	this.user_flag = user_flag;
}
public double getBalance() {
	return balance;
}
public void setBalance(double balance) {
	this.balance = balance;
}
@Override
public String toString() {
	int uuid=this.getUser_id();
	String uid=String.valueOf(uuid);
	return uid;
}
}
