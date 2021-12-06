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

/* 二级缓存是 mapper 级别的缓存，多个 SqlSession 共享*/
public class TwoCacheTest extends TestCase {
	 
		 /**
	     * 使用了一级缓存
	     * 只有第一次查询时访问了数据库，第二次查询直接从缓存读取数据.
	     */
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
		     * 映射文件中所有的select语句将会被缓存
		     * 映射文件中所有的 insert、update、delete操做会刷新缓存，
		     * 保证缓存中的信息是最新的
		     */
			  public void test2Cache(){
			         
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
			        //清空二级缓存，保证缓存中的信息是最新的
			         System.out.println("-----delete 和 commit------");
			         session =MyBatisUtils.getSession();;
			         try {
			        	//userOperation为实现了UserMapper接口的动态代理对象
			        	 UserMapper userOperation=session.getMapper(UserMapper.class);
			         	userOperation.deleteUser(10);
			            session.commit();            
			  
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
		 
			    /**
			     *由于使用了二级缓存
			     *尽管是不同的 SqlSession
			     *只有第一次查询时访问了数据库，第二次查询直接从二级缓存读取数据
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
				            /**
				             *在User.xml的mapper配置文件中配置了selectUserByID
				             *在Article.xml的mapper配置文件中配置了selectArticleByID
						     *由于在User.xml中配置使用了二级缓存，所以User.xml映射文件中所有的select语句将被缓存
						     *selectArticleByID不能使用二级缓存
						     */
							  public void test5Cache(){
							         
							         SqlSession session =MyBatisUtils.getSession();;
							         try {
							        	 //userOperation为实现了UserMapper接口的动态代理对象
							        	 UserMapper userOperation=session.getMapper(UserMapper.class);
							        	 System.out.println("-----第一次查询------");
							        	// 第一次查询时访问了数据库-发出了SQL语句
							        	 Article article1 =userOperation.selectArticleByID(1);
							             System.out.println(article1);
							         } finally {
							         	MyBatisUtils.closeSession(session);
							         }
							          session =MyBatisUtils.getSession();;
							         try {
							        	 //userOperation为实现了UserMapper接口的动态代理对象
							        	 UserMapper userOperation=session.getMapper(UserMapper.class);
							        	 System.out.println("-----第二次查询------");
							        	// 第二次查询时访问了数据库-发出了SQL语句
							        	 Article article2 =userOperation.selectArticleByID(1);
							        	 System.out.println(article2);
							         } finally {
							         	MyBatisUtils.closeSession(session);
							         }
				  
				  }
							  /**
					             *在同一个User.xml的mapper配置文件中配置了selectArticleByID和selectUserByID
							     *使用了二级缓存
							     *尽管是不同的 SqlSession
							     *只有第一次查询时访问了数据库，第二次查询直接从二级缓存读取数据
							     */
								  public void test4Cache(){
								         
								         SqlSession session =MyBatisUtils.getSession();;
								         try {
								        	 //userOperation为实现了UserMapper接口的动态代理对象
								        	 UserMapper userOperation=session.getMapper(UserMapper.class);
								        	 System.out.println("-----第一次查询------");
								        	// 第一次查询时访问了数据库-发出了SQL语句
								        	 Article article1 =userOperation.selectArticleByID(1);
								             System.out.println(article1);
								         } finally {
								         	MyBatisUtils.closeSession(session);
								         }
								          session =MyBatisUtils.getSession();;
								         try {
								        	 //userOperation为实现了UserMapper接口的动态代理对象
								        	 UserMapper userOperation=session.getMapper(UserMapper.class);
								        	 System.out.println("-----第二次查询------");
								        	// 第二次查询时访问了数据库-发出了SQL语句
								        	 Article article2 =userOperation.selectArticleByID(1);
								        	 System.out.println(article2);
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
