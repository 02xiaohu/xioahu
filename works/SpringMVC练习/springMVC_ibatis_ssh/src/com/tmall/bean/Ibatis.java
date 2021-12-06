
  package com.tmall.bean;

  public class Ibatis {
  private int id;
  private String username;
  private String password;
  public Ibatis()
  {  
  }
  public Ibatis(String userName,String password)
  { this.username=username;
    this.password=password;
  }
  public String getPassword() {
	return password;
  }
  public void setPassword(String password) {
	this.password = password;
  }
  public String getUsername() {
	return username;
  }
  public void setUsername(String username) {
	this.username = username;
  }
  public int getId() {
	return id;
  }
  public void setId(int id) {
	this.id = id;
  }
  
   }
