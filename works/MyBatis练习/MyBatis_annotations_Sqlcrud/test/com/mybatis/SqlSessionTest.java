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
     * ���Բ��������û�.
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
	     * ����Map���Ͳ�ѯ����.
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
		     * ���Է�ҳ��ѯ.
		     */
        
        public void testPage(){
			         int offset=2;//���
			         int limit=2;//�鼸��
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
	     * ���Բ���ָ��id���û�.
	     */
         @Test
		  public void testUserById(){
		         
		         SqlSession session =MyBatisUtils.getSession();;
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
	
		   
		  
	/**
     * ��������,�����Զ����������Ӻ󣬱����ύ���񣬷��򲻻�д�뵽���ݿ�.
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
            System.out.println("��ǰ���ӵ��û� idΪ:"+user.getId());
        } finally {
        	MyBatisUtils.closeSession(session);
        }
    }
     
     /**
      * ��������,�������Զ����������Ӻ󣬱����ύ���񣬷��򲻻�д�뵽���ݿ�.
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
             System.out.println("��ǰ���ӵ�Ա�� ��Ϊ:"+dept.getDeptno());
         } finally {
         	MyBatisUtils.closeSession(session);
         }
     }
     
     
     /**
      * ���� ��������,�ȵõ��û�,Ȼ���޸ģ��ύ��

      */
     
     public void testUpdateUser(){
        
         SqlSession session =MyBatisUtils.getSession();;
         try {
         	UserMapper userOperation=session.getMapper(UserMapper.class);
            User user =userOperation.selectUserByID(12);
         
             System.out.println("����ǰ���û� idΪ:"+user.getId());
             System.out.println("����ǰ���û�NameΪ:"+user.getUserName());
             System.out.println("����ǰ���û�AddressΪ:"+user.getUserAddress());
            
             user.setUserAddress("bbbbb");
             userOperation.updateUser(user);
             session.commit();
             
             System.out.println("���º���û� idΪ:"+user.getId());
             System.out.println("���º���û�NameΪ:"+user.getUserName());
             System.out.println("���º���û�AddressΪ:"+user.getUserAddress());
        
         } finally {
         	MyBatisUtils.closeSession(session);
         }
     }

     
     /**
      * ɾ�����ݣ�ɾ��һ��Ҫ commit.
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
