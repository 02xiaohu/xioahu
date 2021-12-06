
 package com.jpa;

 import java.util.Set;
 import javax.persistence.Entity;
 import javax.persistence.GeneratedValue;
 import javax.persistence.Id;
 import javax.persistence.JoinTable;
 import javax.persistence.ManyToMany;
 import javax.persistence.Table;
 import javax.persistence.JoinColumn;
 @Entity
 @Table(name="t_user")
 public class User {
   @Id
   @GeneratedValue
   private int id;
   private String name;
   //ʹ�� @ManyToMany ע����ӳ���Զ������ϵ
   //ʹ�� @JoinTable ��ӳ���м��
   //1. name ָ���м�������
   //2. joinColumns ӳ�䵱ǰ�����ڵı����м���е����
   //2.1 name ָ������е�����
   //2.2 referencedColumnName ָ������ж�Ӧ��ǰ�����һ��
   //3. inverseJoinColumns ӳ���������Role��Ӧ�м������
   @JoinTable(name="t_user_role",
			joinColumns={@JoinColumn(name="userid")},
			inverseJoinColumns={@JoinColumn(name="roleid")})
   @ManyToMany
   private Set<Role> roles;
   
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
   
  public Set getRoles() {
	return roles;
  }
  public void setRoles(Set roles) {
	this.roles = roles;
  }

 }
