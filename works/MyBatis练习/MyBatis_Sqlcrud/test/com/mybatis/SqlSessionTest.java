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


public class SqlSessionTest extends TestCase {
	
	/**
     * 测试查找所有用户.
     */
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
			         int offset=1;//起点
			         int limit=2;//查几条
			         SqlSession session =MyBatisUtils.getSession();;
			         try {
			         	UserMapper userOperation=session.getMapper(UserMapper.class);
			         	RowBounds rowBounds=new RowBounds(offset,limit);
			         	List<User> users=session.selectList("selectUsers",null,rowBounds);
			         	//List<User> users = userOperation.selectUsers();

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
		  public void testUserById(){
		         
		         SqlSession session =MyBatisUtils.getSession();;
		         try {
		        	 UserMapper userOperation=session.getMapper(UserMapper.class);
		        	 User user =userOperation.selectUserByID(2);
		        	 System.out.println("用户 id为:"+user.getId());
		             System.out.println("用户Name为:"+user.getUserName());
		             System.out.println("用户Address为:"+user.getUserAddress());
	           
		  
		         } finally {
		         	MyBatisUtils.closeSession(session);
		         }
		     }
	
		  /**
		     * 测试动态sql语句查找符合条件的记录.
		     */
			  public void testdynamicSqlIf(){
			         
			         SqlSession session =MyBatisUtils.getSession();;
			         try {
			         	UserMapper userOperation=session.getMapper(UserMapper.class);
			         	Condition condition=new Condition();
			         	condition.setUserid(1);
			         	condition.setTitle("test_title_1");
			         	
			         	List<Article> articles = userOperation.dynamicsqlif(condition);

			         	for(Article article:articles){
			                System.out.println(article.getId()+":"+article.getUserid()+":"+article.getTitle()+":"+article.getContent());
			            }
		           
			  
			         } finally {
			         	MyBatisUtils.closeSession(session);
			         }
			     } 
	 
		  
	/**
     * 测试增加,主键自动递增。增加后，必须提交事务，否则不会写入到数据库.
     */
     public void testAddUser(){
        User user=new User();
        user.setUserAddress("beijing");
        user.setUserName("hhhh");
        user.setUserAge(26);
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
          dept.setDeptno(5);
         dept.setDname("zzz");
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
            User user =userOperation.selectUserByID(2);
         
             System.out.println("更新前的用户 id为:"+user.getId());
             System.out.println("更新前的用户Name为:"+user.getUserName());
             System.out.println("更新前的用户Address为:"+user.getUserAddress());
            
             user.setUserAddress("浦东创新园区");
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
         	userOperation.deleteUser(3);
            session.commit();            
  
         } finally {
         	MyBatisUtils.closeSession(session);
         }
     }




}
