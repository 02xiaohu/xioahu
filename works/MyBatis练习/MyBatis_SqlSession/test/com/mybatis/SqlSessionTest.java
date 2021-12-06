package com.mybatis;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import junit.framework.TestCase;


public class SqlSessionTest extends TestCase {

	 /**
     * 测试查找指定id的用户.
     */
	  public void testUserById(){
	         
	         SqlSession session =MyBatisUtils.getSession();
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
   

}
