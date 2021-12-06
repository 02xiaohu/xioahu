
 package com.jpa;

 import javax.persistence.CascadeType;
import javax.persistence.Entity;
 import javax.persistence.GeneratedValue;
 import javax.persistence.GenerationType;
 import javax.persistence.Id;
 import javax.persistence.JoinColumn;
 import javax.persistence.ManyToOne;
import javax.persistence.Table;

 @Table(name="t_user")
 @Entity
 public class User {
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private int id;
	private String name;
	//中当前表中添加一个外键字段groupid，参照Group类对应表的主键
	@JoinColumn(name="groupid")
	//CascadeType.ALL代表在所有的情况下都执行级联操作
	@ManyToOne(cascade={CascadeType.ALL})
	//@ManyToOne
	private Group group;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Group getGroup() {
		return group;
	}
	
	public void setGroup(Group group) {
		this.group = group;
	}
	
	 
 }
