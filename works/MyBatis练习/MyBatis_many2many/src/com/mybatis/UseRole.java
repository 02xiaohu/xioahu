package com.mybatis;

public class UseRole {
 
private int uesrid;
 private int roleid;
public int getUesrid() {
	return uesrid;
}
public void setUesrid(int uesrid) {
	this.uesrid = uesrid;
}
public int getRoleid() {
	return roleid;
}
public void setRoleid(int roleid) {
	this.roleid = roleid;
}
public UseRole(int uesrid,int roleid)
{this.uesrid=uesrid;
 this.roleid=roleid;
	}
}
