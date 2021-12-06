package com.zte.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity//ע����������Ϊ�־���
@Table(name = "t_user1")//ָ�����ݿ����Ĭ��ΪPOJO�ļ�����
public class User {
@Id//ָ������ӳ����
@GeneratedValue(strategy=GenerationType.AUTO)//�������ɷ�ʽ����
@Column(name = "t_id")
private int id;
@Column(name = "t_username")//ָ�����ݿ���ӳ����
private String username;
@Column(name = "t_password")//ָ�����ݿ���ӳ����
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
