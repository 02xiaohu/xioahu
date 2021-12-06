
 package com.jpa;

 import java.util.Set;
 import javax.persistence.Entity;
 import javax.persistence.GeneratedValue;
 import javax.persistence.Id;
 import javax.persistence.ManyToMany;
 import javax.persistence.Table;
 @Entity
 @Table(name="t_role")
 public class Role {
   @Id
   @GeneratedValue
   private int id;
   private String name;
   //ManyToMany关系由User类的roles属性来维护
   @ManyToMany(mappedBy="roles")
   private Set<User> users;
   
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
   
   public Set getUsers() {
	return users;
   }
   public void setUsers(Set users) {
	this.users = users;
   }

 }
