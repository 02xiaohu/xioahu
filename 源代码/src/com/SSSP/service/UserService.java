package com.SSSP.service;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import com.SSSP.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.SSSP.bean.AllUserBean;
import com.SSSP.bean.logBean;

import org.springframework.transaction.annotation.Transactional;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.io.IOException;
import java.util.*;
@Service
public class UserService {
	@Autowired
private UserRepository userrepository;
	@PersistenceContext
	private EntityManager entitymanager;
	@Autowired
	private UserLogin userlogin;
	@Autowired
	private LogRepository logrepository;
	@Autowired
	private AdminLogin adminlogin;
	@Transactional
	public void saveUser(AllUserBean user) {
		userrepository.save(user);
	}
	public AllUserBean findById(Integer id) {
		AllUserBean user=userrepository.findOne(id);
		return user;
	}
	public AllUserBean findByName(String name) {
		return userlogin.findByName(name);
	}
	public boolean findByNaPa(String name,String password) {
		return userlogin.findByNaPa(name,password);
	}
	@Transactional
	public boolean register(String name,String password) {
		return userlogin.register(name,password);
	}
	public double inquiry(String name) {
		return userlogin.inquiry(name);
	}
	@Transactional
	public void deposit(String name,double money) {
		 userlogin.deposit(name,money);
	}
	@Transactional
	public void withdrawal(String name,double money) {
		 userlogin.withdrawal(name,money);
	}
	/*
	@Transactional
	public Page PageSorting(int pageNo,int pageSize) {
		PageRequest pageable=new PageRequest(pageNo,pageSize);
		Page<logBean> logs=logrepository.findAll(pageable);
		return logs;
	}
	*/
@Transactional
public List findUserLog(String name) {
return 	userlogin.findUserLog(name);
}
public Integer getId(String name)throws Exception {
	return userlogin.getId(name);
}

public boolean findAdmin(String name) {
	return adminlogin.findAdmin(name);
}
public Page findAllUserLogs(int PageNo,int PageSize) {
	return adminlogin.findAllUserLogs(PageNo,PageSize);
}
public List findAllUsers() {
	return adminlogin.findAllUsers();
}
@Transactional
public void freeze(String name) {
	adminlogin.freeze(name);
}
@Transactional
public void unfreeze(String name) {
	adminlogin.unfreeze(name);
}
@Transactional
public void inquiryLog(AllUserBean u) {
	logBean bean=new logBean();
	bean.setLog_type("查询");
	bean.setLog_amount(0);
	bean.setUser(u);
	logrepository.save(bean);
}
@Transactional
public void depositLog(AllUserBean u,double money) {
	logBean bean=new logBean();
	bean.setLog_type("存款");
	bean.setLog_amount(money);
	bean.setUser(u);
	logrepository.save(bean);
	
}
@Transactional
public void withdrawalLog(AllUserBean u,double money) {
	logBean bean=new logBean();
	bean.setLog_type("取款");
	bean.setLog_amount(-money);
	bean.setUser(u);
	logrepository.save(bean);
	
}
@Transactional
public void transferLog (AllUserBean u1,AllUserBean u2,double money) {
	logBean bean=new logBean();
	bean.setLog_type("转出");
	bean.setLog_amount(-money);
	bean.setUser(u1);
	logrepository.save(bean);
	logBean b=new logBean();
	b.setLog_type("转入");
	b.setLog_amount(money);
	b.setUser(u2);
	logrepository.save(b);
}
public int getFlag(String name) throws Exception{
	return userlogin.getFlag(name);
}
}
