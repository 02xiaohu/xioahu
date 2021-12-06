
 package com.jpa;

 import java.util.Set;

 import javax.persistence.CascadeType;
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
	//使用 @JoinColumn 来映射外键列的名称
	//可以使用 @OneToMany 的 fetch 属性来修改默认的加载策略
	//可以通过 @OneToMany 的 cascade 属性来修改默认的删除策略. 
	//注意: 若在 1 的一端的 @OneToMany 中使用 mappedBy 属性, 则 @OneToMany 端就不能再使用 @JoinColumn 属性了. 
	//mappedBy属性 表示由Student类中的classes属性来维护关系
	//@OneToMany(fetch=FetchType.LAZY,cascade={CascadeType.REMOVE},mappedBy="classes")
	//@JoinColumn(name="classesid")
	//CascadeType.ALL代表在所有的情况下都执行级联操作
	@OneToMany(cascade={CascadeType.ALL},mappedBy="classes")
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
