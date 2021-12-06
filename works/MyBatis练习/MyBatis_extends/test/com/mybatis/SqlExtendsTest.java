package com.mybatis;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import junit.framework.TestCase;


public class SqlExtendsTest extends TestCase {
	 
	/**
     * 测试 继承映射进行查询-返回 Animal类型.
     */
	  public void testGet1(){
	         
	         SqlSession session =MyBatisUtils.getSession();
	         try {
	         	Mapper userOperation=session.getMapper(Mapper.class);
	         	 Animal animal=userOperation.getAnimalBy(2);
	         	System.out.println(animal.getName());
	         	
	         } finally {
	         	MyBatisUtils.closeSession(session);
	         }
	      }
	  
	  /**
	     * 测试 继承映射进行查询-多态查询.
	     */
	  public void testGet2(){
	         
	         SqlSession session =MyBatisUtils.getSession();
	         try {
	         	Mapper userOperation=session.getMapper(Mapper.class);
	         	 Animal animal=userOperation.getAnimalBy(5);
	         	 if (animal instanceof Pig) {
					System.out.println(animal.getName());
					System.out.println( "Pig的重量为"+((Pig) animal).getWeight());
	         	 }else
					if (animal instanceof Bird)
					{
						System.out.println(animal.getName());
						System.out.println("Bird飞的高度为:"+((Bird) animal).getHeight());
					}
	         	 
	         } finally {
	         	MyBatisUtils.closeSession(session);
	         }
	      }
	  
	    /*
	     * 测试 extends储存 
	     * 
	     */
	  public void testSave(){
     	    Pig pig = new Pig();
			pig.setName("pig3");
			pig.setSex(true);
			pig.setWeight(103);
			pig.setType("P");
			
			Bird bird = new Bird();
			bird.setName("bird2");
			bird.setSex(false);
			bird.setHeight(52);
			bird.setType("B");
		 
	        
	        SqlSession session =MyBatisUtils.getSession();
	         try {
	         	
         	 Mapper userOperation=session.getMapper(Mapper.class);
             userOperation.addPig(pig);
             userOperation.addBird(bird);
  
	         session.commit();
	         } finally {
	         	MyBatisUtils.closeSession(session);
	         }
	    
	
	 
	       
	         }
}