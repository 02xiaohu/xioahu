
 package com.springdata.bean;

 import javax.persistence.Entity;
 import javax.persistence.GeneratedValue;
 import javax.persistence.Id;
 import javax.persistence.Table;
 
 @Entity
 @Table(name="t_address")
 public class Address {
	@Id
	@GeneratedValue
	private Integer id;
	private String city;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	@Override
	public String toString() {
		return "Address [city=" + city + ", id=" + id + "]";
	}
 
 }
