package com.zte.dao;
import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.zte.model.User;
public class UserDao {
   @Resource(name ="hibernateTemplate")
  private HibernateTemplate hibernateTemplate;
 public void add(User u){
 
	 System.out.println("UserDao.add()");
	 hibernateTemplate.save(u);
 }
}
