
 package com.jpa;

 import javax.persistence.CascadeType;
 import javax.persistence.Entity;
 import javax.persistence.GeneratedValue;
 import javax.persistence.GenerationType;
 import javax.persistence.Id;
 import javax.persistence.JoinColumn;
 import javax.persistence.ManyToOne;
 import javax.persistence.OneToOne;
 import javax.persistence.Table;
 
 @Table(name="t_person")
 @Entity
 public class Person {
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private int id;	
	private String name;
	//使用 @OneToOne 来映射 1：1 关联关系。
	//需要在当前数据表中添加外键，参照IdCard类对应的表，需要使用 @JoinColumn来进行映射. 注意, 1-1 关联关系, 所以需要添加 unique=true
	@JoinColumn(name="idcard",unique=true)
    @OneToOne
	private IdCard idCard; 
	
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
	
	public IdCard getIdCard() {
		return idCard;
	}
	public void setIdCard(IdCard idCard) {
		this.idCard = idCard;
	}
	
 }
