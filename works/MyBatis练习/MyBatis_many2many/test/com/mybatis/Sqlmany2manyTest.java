package com.mybatis;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import junit.framework.TestCase;


public class Sqlmany2manyTest extends TestCase {
	
	/**
	  * 查询Role
	  */
	
	 public void testGetRole(){
         
         SqlSession session =MyBatisUtils.getSession();
         try {
         	Mapper userOperation=session.getMapper(Mapper.class);
         	Role role= userOperation.getRoleByID(10);
         	System.out.println("角色id是:"+role.getId()+":"+"角色名字是:"+role.getName() );
         	 
  
         } finally {
         	MyBatisUtils.closeSession(session);
         }
      }
  
	
	
	/**
     * 查询User
     * 测试 多对多的方式进行查询(从User方向).
     */
	 public void testGetUser(){
	         
	         SqlSession session =MyBatisUtils.getSession();
	         try {
	         	Mapper userOperation=session.getMapper(Mapper.class);
	         	 
	         	 User user= userOperation.getUser(12);
	         	 int userid=user.getId();
	         	 System.out.println("用户id是:"+user.getId()+":"+"用户名字是:"+user.getName() );
        	     List Roles=user.getRoles();
        	     Roles=user.getRoles();
        	     Iterator iter = Roles.iterator() ;
        	 	while(iter.hasNext())
        	 	{   Role role=(Role)(iter.next());
        	 		System.out.println("用户角色id是:"+role.getId()+":"+"用户角色的名字是:"+role.getName() );
        	 		 
        	        
        	 	}
	         } finally {
	         	MyBatisUtils.closeSession(session);
	         }
	         }
	  
	 
	    /*
	     * 测试 多对多的方式储存 (从User方向)
	     * 
	     */
	  public void testSave(){
		    Role r1 = new Role();
			r1.setName("aaaa");
		 
			Role r2 = new Role();
			r2.setName("bbbb");
		  
			Role r3 = new Role();
			r3.setName("cccc");
		 
			
			User u1 = new User();
			u1.setName("zhangsan");
			List u1Roles = new ArrayList();
			u1Roles.add(r1);
			u1Roles.add(r2);
			u1.setRoles(u1Roles);
			
			User u2 = new User();
			u2.setName("lisi");
			List u2Roles = new ArrayList();
			u2Roles.add(r2);
			u2Roles.add(r3);
			u2.setRoles(u2Roles);
			
			User u3 = new User();
			u3.setName("wangwu");
			List u3Roles = new ArrayList();
			u3Roles.add(r1);
			u3Roles.add(r2);
			u3Roles.add(r3);
			u3.setRoles(u3Roles);
	        
	        SqlSession session =MyBatisUtils.getSession();
	         try {
	         	
	        	 Mapper userOperation=session.getMapper(Mapper.class);
	             userOperation.addRole(r1);
	             userOperation.addRole(r2);
	             userOperation.addRole(r3);
	             
	             userOperation.addUser(u1);
	             userOperation.addUser(u2);
	             userOperation.addUser(u3);
	             List<Role> Roles=null;
	              Roles=u1.getRoles();
                 for(Role role:Roles)
	            { 
            	 userOperation.addUserRole(new UseRole(u1.getId(),role.getId()));
                 }
 	            		 
                  Roles=u2.getRoles();
                 for(Role role:Roles)
	            { 
            	 userOperation.addUserRole(new UseRole(u2.getId(),role.getId()));
                 }	
                 
                 Roles=u3.getRoles();
                 for(Role role:Roles)
	            { 
            	 userOperation.addUserRole(new UseRole(u3.getId(),role.getId()));
                 }	

                 
	             session.commit();
	         } finally {
	         	MyBatisUtils.closeSession(session);
	         }
	    
	
	 
	       
	         }
}