package com.SSSP.bean;
import javax.persistence.*;
@Entity
@Table(name="t_log")
public class logBean {
private String log_type;
private double log_amount;
@Id
@GeneratedValue
private Integer log_id;
@ManyToOne
@JoinColumn(name="userid")
private AllUserBean user;
public String getLog_type() {
	return log_type;
}
public void setLog_type(String log_type) {
	this.log_type = log_type;
}
public double getLog_amount() {
	return log_amount;
}
public void setLog_amount(double log_amount) {
	this.log_amount = log_amount;
}
public Integer getLog_id() {
	return log_id;
}
public void setLog_id(Integer log_id) {
	this.log_id = log_id;
}
public AllUserBean getUser() {
	return user;
}
public void setUser(AllUserBean user) {
	this.user = user;
}


}
