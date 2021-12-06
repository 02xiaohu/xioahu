package com.tmall.service;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

 
 
import com.tmall.bean.User;
import com.tmall.inter.OperationMapper;
 
public class LoginService {
	 
	@Resource(name ="Mapper") 
	 
	private OperationMapper Mapper;
	 
	
	
	
	public void insert(User user){
		 
		 
		Mapper.addUser(user);
	}


public User findUserById(int id){
	 
	   User element=Mapper.selectUserByID(id);
	   
      return element;
}

public List findAllUse()
{
	 List<User> users=Mapper.selectUsers();
     return users;
}

public void delUser(int id)
{  
	Mapper.deleteUser(id);
}

 
public void updateUse(User user){
	 
	Mapper.updateUser(user);
	 
}
}

 
 
 
