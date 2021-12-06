package com.mybatis;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import junit.framework.TestCase;


public class Sqlmany2oneTest extends TestCase {
	 
	/**
     * 测试 User 联合进行查询(多对一的方式).
     */
	  public void testGetUserGroup(){
	         
	         SqlSession session =MyBatisUtils.getSession();
	         try {
	         	Mapper userOperation=session.getMapper(Mapper.class);
	         	List<User> users = userOperation.getUserGroup(5);

	         	for(User user:users){
	                System.out.println( "用户所属组号:"+user.getGroup().getId()+
	                		            "用户所属组名:"+user.getGroup().getName()+
	                        "用户id:"+user.getId()+":"+"用户名:"+user.getName());
	         	}
	  
	         } finally {
	         	MyBatisUtils.closeSession(session);
	         }
	      }
	  
	    /*
	     * 测试 多对一的方式储存--多的一方（Article）维护关系
	     * 
	     */
	  public void testSave(){
		  Group group = new Group();
		  group.setName("zte");

	       
		  User user1 = new User();
		  user1.setName("zhangsan");
		  user1.setGroup(group);

		  User user2 = new User();
		  user2.setName("lisi");
		  user2.setGroup(group);

	        
	        
	        SqlSession session =MyBatisUtils.getSession();
	         try {
	         	
	        	 Mapper userOperation=session.getMapper(Mapper.class);
	             userOperation.addGroup(group);
	            
	             userOperation.addUser(user1);
	             userOperation.addUser(user2);
	         
	             
	             session.commit();
	         } finally {
	         	MyBatisUtils.closeSession(session);
	         }
	    
	
	 
	       
	         }
}