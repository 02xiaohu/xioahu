package com.mybatis;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import junit.framework.TestCase;


public class Sqlone2oneTest extends TestCase {
	 
	/**
     * 测试 一对一的方式 联合进行查询
     */
	  public void testGetPerson(){
	         
	         SqlSession session =MyBatisUtils.getSession();
	         try {
	         	Mapper userOperation=session.getMapper(Mapper.class);
	         	Person person= userOperation.getPerson(1);

	         	 
	                System.out.println("person的id是:"+person.getId()+":"+"person的name是:"+person.getName()+
	                		":"+"person的cardNo是:"+person.getIdCard().getCardNo());
	         	 
	  
	         } finally {
	         	MyBatisUtils.closeSession(session);
	         }
	      }
	  
	    /*
	     * 测试 一对一的方式储存--（person）维护关系
	     * 
	     */
	  public void testSave(){

		    IdCard idCard=new IdCard();
		    idCard.setCardNo("2345667");
	       
		    Person person=new Person();
		    person.setName("Tom");
		    person.setIdCard(idCard);
	       
//		    Person person1=new Person();
//		    person1.setName("Tom1");
//		    person1.setIdCard(idCard);
	        
	        
	        SqlSession session =MyBatisUtils.getSession();
	         try {
	         	
	        	 Mapper userOperation=session.getMapper(Mapper.class);
	             userOperation.addIdCard(idCard);
	            
	             userOperation.addPerson(person);
	            // userOperation.addPerson(person1);
	             
	             session.commit();
	         } finally {
	         	MyBatisUtils.closeSession(session);
	         }
	    
	
	 
	       
	         }
}