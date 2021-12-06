
 package com.jpa;

 import javax.persistence.Entity;
 import javax.persistence.GeneratedValue;
 import javax.persistence.GenerationType;
 import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

 @Table(name="t_student")
 @Entity
 public class Student {
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private int id;
	private String name;
	//中当前表中添加一个外键字段classesid，参照Classes类对应表的主键
	@JoinColumn(name="classesid")
	@ManyToOne
	private Classes classes;
	
	public Classes getClasses() {
		return classes;
	}
	public void setClasses(Classes classes) {
		this.classes = classes;
	}
	
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
  }
