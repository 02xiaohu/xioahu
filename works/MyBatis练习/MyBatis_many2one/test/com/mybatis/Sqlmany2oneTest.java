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
     * ���� User ���Ͻ��в�ѯ(���һ�ķ�ʽ).
     */
	  public void testGetUserGroup(){
	         
	         SqlSession session =MyBatisUtils.getSession();
	         try {
	         	Mapper userOperation=session.getMapper(Mapper.class);
	         	List<User> users = userOperation.getUserGroup(5);

	         	for(User user:users){
	                System.out.println( "�û��������:"+user.getGroup().getId()+
	                		            "�û���������:"+user.getGroup().getName()+
	                        "�û�id:"+user.getId()+":"+"�û���:"+user.getName());
	         	}
	  
	         } finally {
	         	MyBatisUtils.closeSession(session);
	         }
	      }
	  
	    /*
	     * ���� ���һ�ķ�ʽ����--���һ����Article��ά����ϵ
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