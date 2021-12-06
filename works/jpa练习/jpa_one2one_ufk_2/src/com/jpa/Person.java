
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
	//ʹ�� @OneToOne ��ӳ�� 1��1 ������ϵ��
	//��Ҫ�ڵ�ǰ���ݱ���������������IdCard���Ӧ�ı���Ҫʹ�� @JoinColumn������ӳ��. ע��, 1-1 ������ϵ, ������Ҫ��� unique=true
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
