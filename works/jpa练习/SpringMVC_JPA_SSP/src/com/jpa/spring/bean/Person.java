
 package com.jpa.spring.bean;
 
 import javax.persistence.Column;
 import javax.persistence.Entity;
 import javax.persistence.GeneratedValue;
 import javax.persistence.GenerationType;
 import javax.persistence.Id;
 import javax.persistence.Table;
 
 @Table(name="jpa_persons")
 @Entity
 public class Person 
 {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)
   private int id;
   @Column(name="t_name")
   private String name;
   @Column(name="t_age")
   private int age;
   
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
   
   public int getAge() {
	return age;
   }
   public void setAge(int age) {
	this.age = age;
   }

 }
