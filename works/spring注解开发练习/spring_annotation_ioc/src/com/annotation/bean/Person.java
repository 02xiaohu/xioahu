
  package com.annotation.bean;

  public class Person {
 
   private String name;
   private Integer age;
   private String nickName;

   public Person() {
	   
    }
   
   public Person(String name, Integer age, String nickName) {
	super();
	this.name = name;
	this.age = age;
	this.nickName = nickName;
    System.out.println("Person构造方法被调用");
   }
   
   public Integer getAge() {
	 return age;
   }
   
   public void setAge(Integer age) {
	this.age = age;
   }
   
   public String getName() {
	return name;
   }
   
   public void setName(String name) {
	this.name = name;
   }
   
   public String getNickName() {
	return nickName;
   }
   
   public void setNickName(String nickName) {
	this.nickName = nickName;
   }
  @Override
  public String toString() {
	// TODO Auto-generated method stub
	return "Person [name=" + name + ", age=" + age + ", nickName=" + nickName + "]";
    }
  }
