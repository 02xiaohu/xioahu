
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
 
 @Entity
 @Table(name="t_idcard")
 public class IdCard {
	

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private int id;
	private String cardNo;
	//ʹ�� @OneToOne ��ʹ�� 1��1 ������ϵ
	//mappedBy="idCard"��ʾ�ù�ϵ��Person���idCard������ά��
	@OneToOne(mappedBy="idCard")
	private Person person;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
 }
