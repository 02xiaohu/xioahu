package com.zte.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity//注释声明该类为持久类
@Table(name = "t_user1")//指定数据库表名默认为POJO的简单名称
public class User {
@Id//指定主键映射名
@GeneratedValue(strategy=GenerationType.AUTO)//主键生成方式序列
@Column(name = "t_id")
private int id;
@Column(name = "t_username")//指定数据库列映射名
private String username;
@Column(name = "t_password")//指定数据库列映射名
private String password;
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
}
