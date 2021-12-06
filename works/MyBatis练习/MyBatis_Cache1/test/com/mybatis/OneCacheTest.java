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

/**
 * 一级缓存是 SqlSession 级别的缓存，是基于 HashMap 的本地缓存
 * MyBatis 默认开启一级缓存
 */
public class OneCacheTest extends TestCase {
	 
		/*
		 *  只有第一次查询时访问了数据库，第二次查询直接从缓存读取数据.
		 * */
		  public void test1Cache(){
		         
		         SqlSession session =MyBatisUtils.getSession();;
		         try {
		        	 //userOperation为实现了UserMapper接口的动态代理对象
		        	 UserMapper userOperation=session.getMapper(UserMapper.class);
		        	 System.out.println("-----第一次查询------");
		        	// 第一次查询时访问了数据库-发出了SQL语句
		        	 User user1 =userOperation.selectUserByID(1);
		        	 System.out.println("用户 id为:"+user1.getId());
		             System.out.println("用户Name为:"+user1.getUserName());
		             System.out.println("用户Address为:"+user1.getUserAddress());
		             
		             System.out.println("-----第二次查询------");
		             //第二次查询使用一级缓存-未产生了SQL语句
		             User user2 =userOperation.selectUserByID(1);
		        	 System.out.println("用户 id为:"+user2.getId());
		             System.out.println("用户Name为:"+user2.getUserName());
		             System.out.println("用户Address为:"+user2.getUserAddress());
		             System.out.println("-----查询结束------");
		         } finally {
		         	MyBatisUtils.closeSession(session);
		         }
		     }
	
		    /**
		     * 当 SqlSession 执行 insert、update、delete操做并提交到数据库时 
			 *会清空缓存，保证缓存中的信息是最新的
		     */
			  public void test2Cache(){
			         
			         SqlSession session =MyBatisUtils.getSession();;
			         try {
			        	 //userOperation为实现了UserMapper接口的动态代理对象
			        	 UserMapper userOperation=session.getMapper(UserMapper.class);
			        	 System.out.println("-----第一次查询------");
			        	 //第一次查询未使用一级缓存-发出了SQL语句
			        	 User user1 =userOperation.selectUserByID(1);
			        	 System.out.println("用户 id为:"+user1.getId());
			             System.out.println("用户Name为:"+user1.getUserName());
			             System.out.println("用户Address为:"+user1.getUserAddress());
			             System.out.println("-----delete 和 commit------");
			          
			             //有 delete 操作，并把操作提交到了数据库，MyBatis 清空了缓存 			          
			             userOperation.deleteUser(9);
			             session.commit(); 
			             System.out.println("-----第二次查询------");
			             //第二次查询未使用一级缓存-发出了SQL语句
                         User user2 =userOperation.selectUserByID(1);
			        	 System.out.println("用户 id为:"+user2.getId());
			             System.out.println("用户Name为:"+user2.getUserName());
			             System.out.println("用户Address为:"+user2.getUserAddress());
			             System.out.println("-----查询结束------");
			         } finally {
			         	MyBatisUtils.closeSession(session);
			         }
			     }
		 
			    /**
			     *由于是不同的 SqlSession，它们之间的缓存数据区域互不影响，因此两次均访问了数据库
			     */
				  public void test3Cache(){
				         
				         SqlSession session =MyBatisUtils.getSession();;
				         try {
				        	 //userOperation为实现了UserMapper接口的动态代理对象
				        	 UserMapper userOperation=session.getMapper(UserMapper.class);
				        	 System.out.println("-----第一次查询------");
				        	// 第一次查询时访问了数据库-发出了SQL语句
				        	 User user1 =userOperation.selectUserByID(1);
				        	 System.out.println("用户 id为:"+user1.getId());
				             System.out.println("用户Name为:"+user1.getUserName());
				             System.out.println("用户Address为:"+user1.getUserAddress());
				         } finally {
				         	MyBatisUtils.closeSession(session);
				         }
				          session =MyBatisUtils.getSession();;
				         try {
				        	 //userOperation为实现了UserMapper接口的动态代理对象
				        	 UserMapper userOperation=session.getMapper(UserMapper.class);
				        	 System.out.println("-----第二次查询------");
				        	// 第二次查询时访问了数据库-发出了SQL语句
				        	 User user2 =userOperation.selectUserByID(1);
				        	 System.out.println("用户 id为:"+user2.getId());
				             System.out.println("用户Name为:"+user2.getUserName());
				             System.out.println("用户Address为:"+user2.getUserAddress());
				         } finally {
				         	MyBatisUtils.closeSession(session);
				         }
				  
				  }
	 
				 /*管理一级缓存*/
				  public void test4Cache(){
				         
				         SqlSession session =MyBatisUtils.getSession();;
				         try {
				        	 //userOperation为实现了UserMapper接口的动态代理对象
				        	 UserMapper userOperation=session.getMapper(UserMapper.class);
				        	 System.out.println("-----第一次查询------");
				        	// 第一次查询时访问了数据库-发出了SQL语句
				        	 User user1 =userOperation.selectUserByID(1);
				        	 System.out.println("用户 id为:"+user1.getId());
				             System.out.println("用户Name为:"+user1.getUserName());
				             System.out.println("用户Address为:"+user1.getUserAddress());
				             //清除SqlSession缓存中的内容
				             session.clearCache();
				             System.out.println("-----第二次查询------");
				             //第二次查询产生了SQL语句
				             User user2 =userOperation.selectUserByID(1);
				        	 System.out.println("用户 id为:"+user2.getId());
				             System.out.println("用户Name为:"+user2.getUserName());
				             System.out.println("用户Address为:"+user2.getUserAddress());
				             System.out.println("-----查询结束------");
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
        user.setUserName("kkll");
        user.setUserAge(23);
        SqlSession session =MyBatisUtils.getSession();;
        try {
        	//userOperation为实现了UserMapper接口的动态代理对象
        	UserMapper userOperation=session.getMapper(UserMapper.class);
            userOperation.addUser(user);
            session.commit();
            System.out.println("当前增加的用户 id为:"+user.getId());
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
        	//userOperation为实现了UserMapper接口的动态代理对象
        	 UserMapper userOperation=session.getMapper(UserMapper.class);
            User user =userOperation.selectUserByID(2);
         
             System.out.println("更新前的用户 id为:"+user.getId());
             System.out.println("更新前的用户Name为:"+user.getUserName());
             System.out.println("更新前的用户Address为:"+user.getUserAddress());
            
             user.setUserAddress("senzen");
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
        	//userOperation为实现了UserMapper接口的动态代理对象
        	 UserMapper userOperation=session.getMapper(UserMapper.class);
         	userOperation.deleteUser(6);
            session.commit();            
  
         } finally {
         	MyBatisUtils.closeSession(session);
         }
     }




}
