
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
	//�е�ǰ�������һ������ֶ�groupid������Group���Ӧ�������
	@JoinColumn(name="groupid")
	//CascadeType.ALL���������е�����¶�ִ�м�������
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
