
 package com.springdata;

 import java.util.Date;
 import javax.persistence.Entity;
 import javax.persistence.JoinColumn;
 import javax.persistence.ManyToOne;
 import javax.persistence.Table;
 import javax.persistence.GeneratedValue;
 import javax.persistence.Id;
 @Entity
 @Table(name="JPA_PERSONS")
 public class Person {
  @Id
  @GeneratedValue
  private Integer id;
  private String name;
  private String email;
  private Date birth;
  @ManyToOne
  @JoinColumn(name="ADDRESS_ID")
  private Address address;

  public Integer getId() {
	return id;
  }
  public void setId(Integer id) {
	this.id = id;
  }
  
  public String getName() {
	return name;
  }
  public void setName(String name) {
	  this.name = name;
  }
  
  public String getEmail() {
	return email;
  }
  public void setEmail(String email) {
	this.email = email;
  }
	  
  public Date getBirth() {
	return birth;
  } 
  public void setBirth(Date birth) {
	this.birth = birth;
  }
  
  public Address getAddress() {
	return address;
  }
  public void setAddress(Address address) {
	this.address = address;
  }

  @Override
  public String toString() {
	return "Person [birth=" + birth + ", email=" + email + ", id=" + id
			+ ", name=" + name + "]";
   }
   
 
 }
