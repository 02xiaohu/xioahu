package com.mybatis;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import junit.framework.TestCase;


public class SqlSessionTest extends TestCase {

	 /**
     * ���Բ���ָ��id���û�.
     */
	  public void testUserById(){
	         
	         SqlSession session =MyBatisUtils.getSession();
	         try {
	        	 UserMapper userOperation=session.getMapper(UserMapper.class);
	        	 User user =userOperation.selectUserByID(1);
	        	 System.out.println("�û� idΪ:"+user.getId());
	             System.out.println("�û�NameΪ:"+user.getUserName());
	             System.out.println("�û�AddressΪ:"+user.getUserAddress());
           
	  
	         } finally {
	         	MyBatisUtils.closeSession(session);
	         }
	     }
   

}
