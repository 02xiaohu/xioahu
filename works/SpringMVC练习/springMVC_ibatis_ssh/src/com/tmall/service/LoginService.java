package com.tmall.service;
import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.ModelAndView;
 
import com.tmall.bean.Ibatis;
import com.tmall.dao.Dao;
import com.tmall.dao.DaoImp;
 
public class LoginService {
@Resource(name ="daoImp") 
private Dao  daoImp;

public void add(Ibatis ibatis){
 
	 
	 daoImp.insert(ibatis);
}

 

 
}

 
 
 
