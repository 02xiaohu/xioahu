
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
	//ӳ�䵥�� One-Many �Ĺ�����ϵ
	//ʹ�� @OneToMany ��ӳ�� One-Many�Ĺ�����ϵ
	//ʹ�� @JoinColumn ��ӳ������е�����
	//����ʹ�� @OneToMany �� fetch �������޸�Ĭ�ϵļ��ز���
	//����ͨ�� @OneToMany �� cascade �������޸�Ĭ�ϵ�ɾ������. 
	//ע��: ���� 1 ��һ�˵� @OneToMany ��ʹ�� mappedBy ����, �� @OneToMany �˾Ͳ�����ʹ�� @JoinColumn ������. 
	//mappedBy���� ��ʾ��Student���е�classes������ά����ϵ
	//@OneToMany(fetch=FetchType.LAZY,cascade={CascadeType.REMOVE},mappedBy="classes")
	//@JoinColumn(name="classesid")
	//CascadeType.ALL���������е�����¶�ִ�м�������
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
