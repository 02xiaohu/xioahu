
 package com.jpa;

 import java.util.Set;
 import javax.persistence.Entity;
 import javax.persistence.GeneratedValue;
 import javax.persistence.GenerationType;
 import javax.persistence.Id;
 import javax.persistence.JoinColumn;
 import javax.persistence.OneToMany;
 import javax.persistence.Table;

 @Table(name="t_classes")
 @Entity
 public class Classes {
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private int id;
	private String name;
	//映射单向 One-Many 的关联关系
	//使用 @OneToMany 来映射 One-Many的关联关系
	//使用 @JoinColumn 在Student类对应表中建立外键列classesid 参照当前类对应表的主键
	@JoinColumn(name="classesid")
	@OneToMany
	private Set<Student> students;
	 
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
	
	public Set getStudents() {
		return students;
	}
	public void setStudents(Set students) {
		this.students = students;
	}
	
 }
