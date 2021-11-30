package com.SSSP.bean;
import javax.persistence.*;
@Entity
@Table(name="t_admin")
public class Admin {
	@Id
	@GeneratedValue
	private Integer id;
	@Column(name="admin_name")
private String name;
	@Column(name="admin_password")
private String password;
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}

}
