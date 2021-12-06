
 package com.jpa;

 import java.util.Date;
 import javax.persistence.Column;
 import javax.persistence.Entity;
 import javax.persistence.GeneratedValue;
 import javax.persistence.GenerationType;
 import javax.persistence.Id; 
 import javax.persistence.NamedQuery;
 import javax.persistence.Table;
 import javax.persistence.Transient;
 
 @NamedQuery(name="testNamedQuery", query="FROM Customer c WHERE c.id = ?")
 @Table(name="JPA_CUTOMERS")
 @Entity
 public class Customer {
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private Integer id;
	@Column(name="LAST_NAME",length=50,nullable=false)
	private String lastName;
    private String email;
	private int age;
	private Date createdTime;
	private Date birth;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
	//工具方法. 不需要映射为数据表的一列. 
	@Transient
	public String getInfo(){
		return "lastName: " + lastName + ", email: " + email;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", lastName=" + lastName + ", email="
				+ email + ", age=" + age + ", createdTime=" + createdTime
				+ ", birth=" + birth + "]";
	}

  }
