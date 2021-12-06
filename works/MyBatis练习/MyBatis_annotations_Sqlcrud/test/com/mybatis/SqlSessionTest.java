package com.mybatis;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
 

import junit.framework.TestCase;
 
import org.junit.Test;

public class SqlSessionTest extends TestCase {
	
	/**
     * 测试查找所有用户.
     */
@Test
public void testGetAllUser(){
	         
	         SqlSession session =MyBatisUtils.getSession();;
	         try {
	         	UserMapper userOperation=session.getMapper(UserMapper.class);
	         	 List<User> users = userOperation.selectUsers();

	         	for(User user:users){
	                System.out.println(user.getId()+":"+user.getUserName()+":"+user.getUserAddress());
	            }
           
	  
	         } finally {
	         	MyBatisUtils.closeSession(session);
	         }
	     }
	
	  /**
	     * 测试Map类型查询操作.
	     */
        
		  public void testMap(){
		         SqlSession session =MyBatisUtils.getSession();;
		         try {
		         	UserMapper userOperation=session.getMapper(UserMapper.class);
		          
		          List<Map> list = userOperation.selectMaps();

		         	for(Map map:list){
		                System.out.println(map);
		            }
	           
		  
		         } finally {
		         	MyBatisUtils.closeSession(session);
		         }
		     }
		  /**
		     * 测试分页查询.
		     */
        
        public void testPage(){
			         int offset=2;//起点
			         int limit=2;//查几条
			         SqlSession session =MyBatisUtils.getSession();;
			         try {
			         	 
			         	RowBounds rowBounds=new RowBounds(offset,limit);
			         	List<User> users=session.selectList("selectUsers",null,rowBounds);
			         	 

			         	for(User user:users){
			                System.out.println(user.getId()+":"+user.getUserName()+":"+user.getUserAddress());
			            }
		           
			  
			         } finally {
			         	MyBatisUtils.closeSession(session);
			         }
			     }
	  
	  
		  
		 /**
	     * 测试查找指定id的用户.
	     */
         @Test
		  public void testUserById(){
		         
		         SqlSession session =MyBatisUtils.getSession();;
		         try {
		        	 UserMapper userOperation=session.getMapper(UserMapper.class);
		        	 User user =userOperation.selectUserByID(1);
		        	 System.out.println("用户 id为:"+user.getId());
		             System.out.println("用户Name为:"+user.getUserName());
		             System.out.println("用户Address为:"+user.getUserAddress());
	           
		  
		         } finally {
		         	MyBatisUtils.closeSession(session);
		         }
		     }
	
		   
		  
	/**
     * 测试增加,主键自动递增。增加后，必须提交事务，否则不会写入到数据库.
     */
       
     public void testAddUser(){
        User user=new User();
        user.setUserAddress("shenzen");
        user.setUserName("dfdfdf");
        user.setUserAge(27);
        SqlSession session =MyBatisUtils.getSession();;
        try {
        	UserMapper userOperation=session.getMapper(UserMapper.class);
            userOperation.addUser(user);
            session.commit();
            System.out.println("当前增加的用户 id为:"+user.getId());
        } finally {
        	MyBatisUtils.closeSession(session);
        }
    }
     
     /**
      * 测试增加,主键非自动递增。增加后，必须提交事务，否则不会写入到数据库.
      */
      
     public void testAddDept(){
         Dept dept=new Dept();
          dept.setDeptno(39);
         dept.setDname("lis1");
          dept.setLoc("nc");
         SqlSession session =MyBatisUtils.getSession();;
         try {
         	UserMapper userOperation=session.getMapper(UserMapper.class);
             userOperation.addDept(dept);
             session.commit();
             System.out.println("当前增加的员工 号为:"+dept.getDeptno());
         } finally {
         	MyBatisUtils.closeSession(session);
         }
     }
     
     
     /**
      * 测试 更新数据,先得到用户,然后修改，提交。

      */
     
     public void testUpdateUser(){
        
         SqlSession session =MyBatisUtils.getSession();;
         try {
         	UserMapper userOperation=session.getMapper(UserMapper.class);
            User user =userOperation.selectUserByID(12);
         
             System.out.println("更新前的用户 id为:"+user.getId());
             System.out.println("更新前的用户Name为:"+user.getUserName());
             System.out.println("更新前的用户Address为:"+user.getUserAddress());
            
             user.setUserAddress("bbbbb");
             userOperation.updateUser(user);
             session.commit();
             
             System.out.println("更新后的用户 id为:"+user.getId());
             System.out.println("更新后的用户Name为:"+user.getUserName());
             System.out.println("更新后的用户Address为:"+user.getUserAddress());
        
         } finally {
         	MyBatisUtils.closeSession(session);
         }
     }

     
     /**
      * 删除数据，删除一定要 commit.
      * @param id
      */
    
      public void testDeleteUser(){
         
         SqlSession session =MyBatisUtils.getSession();;
         try {
         	UserMapper userOperation=session.getMapper(UserMapper.class);
         	userOperation.deleteUser(12);
            session.commit();            
  
         } finally {
         	MyBatisUtils.closeSession(session);
         }
     }




}
