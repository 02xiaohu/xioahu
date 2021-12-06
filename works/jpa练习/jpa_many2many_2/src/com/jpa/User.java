
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
   //使用 @ManyToMany 注解来映射多对多关联关系
   //使用 @JoinTable 来映射中间表
   //1. name 指向中间表的名字
   //2. joinColumns 映射当前类所在的表在中间表中的外键
   //2.1 name 指定外键列的列名
   //2.2 referencedColumnName 指定外键列对应当前表的哪一列
   //3. inverseJoinColumns 映射关联的类Role对应中间表的外键
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
