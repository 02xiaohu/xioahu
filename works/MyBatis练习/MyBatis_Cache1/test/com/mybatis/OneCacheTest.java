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
 * һ�������� SqlSession ����Ļ��棬�ǻ��� HashMap �ı��ػ���
 * MyBatis Ĭ�Ͽ���һ������
 */
public class OneCacheTest extends TestCase {
	 
		/*
		 *  ֻ�е�һ�β�ѯʱ���������ݿ⣬�ڶ��β�ѯֱ�Ӵӻ����ȡ����.
		 * */
		  public void test1Cache(){
		         
		         SqlSession session =MyBatisUtils.getSession();;
		         try {
		        	 //userOperationΪʵ����UserMapper�ӿڵĶ�̬�������
		        	 UserMapper userOperation=session.getMapper(UserMapper.class);
		        	 System.out.println("-----��һ�β�ѯ------");
		        	// ��һ�β�ѯʱ���������ݿ�-������SQL���
		        	 User user1 =userOperation.selectUserByID(1);
		        	 System.out.println("�û� idΪ:"+user1.getId());
		             System.out.println("�û�NameΪ:"+user1.getUserName());
		             System.out.println("�û�AddressΪ:"+user1.getUserAddress());
		             
		             System.out.println("-----�ڶ��β�ѯ------");
		             //�ڶ��β�ѯʹ��һ������-δ������SQL���
		             User user2 =userOperation.selectUserByID(1);
		        	 System.out.println("�û� idΪ:"+user2.getId());
		             System.out.println("�û�NameΪ:"+user2.getUserName());
		             System.out.println("�û�AddressΪ:"+user2.getUserAddress());
		             System.out.println("-----��ѯ����------");
		         } finally {
		         	MyBatisUtils.closeSession(session);
		         }
		     }
	
		    /**
		     * �� SqlSession ִ�� insert��update��delete�������ύ�����ݿ�ʱ 
			 *����ջ��棬��֤�����е���Ϣ�����µ�
		     */
			  public void test2Cache(){
			         
			         SqlSession session =MyBatisUtils.getSession();;
			         try {
			        	 //userOperationΪʵ����UserMapper�ӿڵĶ�̬�������
			        	 UserMapper userOperation=session.getMapper(UserMapper.class);
			        	 System.out.println("-----��һ�β�ѯ------");
			        	 //��һ�β�ѯδʹ��һ������-������SQL���
			        	 User user1 =userOperation.selectUserByID(1);
			        	 System.out.println("�û� idΪ:"+user1.getId());
			             System.out.println("�û�NameΪ:"+user1.getUserName());
			             System.out.println("�û�AddressΪ:"+user1.getUserAddress());
			             System.out.println("-----delete �� commit------");
			          
			             //�� delete ���������Ѳ����ύ�������ݿ⣬MyBatis ����˻��� 			          
			             userOperation.deleteUser(9);
			             session.commit(); 
			             System.out.println("-----�ڶ��β�ѯ------");
			             //�ڶ��β�ѯδʹ��һ������-������SQL���
                         User user2 =userOperation.selectUserByID(1);
			        	 System.out.println("�û� idΪ:"+user2.getId());
			             System.out.println("�û�NameΪ:"+user2.getUserName());
			             System.out.println("�û�AddressΪ:"+user2.getUserAddress());
			             System.out.println("-----��ѯ����------");
			         } finally {
			         	MyBatisUtils.closeSession(session);
			         }
			     }
		 
			    /**
			     *�����ǲ�ͬ�� SqlSession������֮��Ļ����������򻥲�Ӱ�죬������ξ����������ݿ�
			     */
				  public void test3Cache(){
				         
				         SqlSession session =MyBatisUtils.getSession();;
				         try {
				        	 //userOperationΪʵ����UserMapper�ӿڵĶ�̬�������
				        	 UserMapper userOperation=session.getMapper(UserMapper.class);
				        	 System.out.println("-----��һ�β�ѯ------");
				        	// ��һ�β�ѯʱ���������ݿ�-������SQL���
				        	 User user1 =userOperation.selectUserByID(1);
				        	 System.out.println("�û� idΪ:"+user1.getId());
				             System.out.println("�û�NameΪ:"+user1.getUserName());
				             System.out.println("�û�AddressΪ:"+user1.getUserAddress());
				         } finally {
				         	MyBatisUtils.closeSession(session);
				         }
				          session =MyBatisUtils.getSession();;
				         try {
				        	 //userOperationΪʵ����UserMapper�ӿڵĶ�̬�������
				        	 UserMapper userOperation=session.getMapper(UserMapper.class);
				        	 System.out.println("-----�ڶ��β�ѯ------");
				        	// �ڶ��β�ѯʱ���������ݿ�-������SQL���
				        	 User user2 =userOperation.selectUserByID(1);
				        	 System.out.println("�û� idΪ:"+user2.getId());
				             System.out.println("�û�NameΪ:"+user2.getUserName());
				             System.out.println("�û�AddressΪ:"+user2.getUserAddress());
				         } finally {
				         	MyBatisUtils.closeSession(session);
				         }
				  
				  }
	 
				 /*����һ������*/
				  public void test4Cache(){
				         
				         SqlSession session =MyBatisUtils.getSession();;
				         try {
				        	 //userOperationΪʵ����UserMapper�ӿڵĶ�̬�������
				        	 UserMapper userOperation=session.getMapper(UserMapper.class);
				        	 System.out.println("-----��һ�β�ѯ------");
				        	// ��һ�β�ѯʱ���������ݿ�-������SQL���
				        	 User user1 =userOperation.selectUserByID(1);
				        	 System.out.println("�û� idΪ:"+user1.getId());
				             System.out.println("�û�NameΪ:"+user1.getUserName());
				             System.out.println("�û�AddressΪ:"+user1.getUserAddress());
				             //���SqlSession�����е�����
				             session.clearCache();
				             System.out.println("-----�ڶ��β�ѯ------");
				             //�ڶ��β�ѯ������SQL���
				             User user2 =userOperation.selectUserByID(1);
				        	 System.out.println("�û� idΪ:"+user2.getId());
				             System.out.println("�û�NameΪ:"+user2.getUserName());
				             System.out.println("�û�AddressΪ:"+user2.getUserAddress());
				             System.out.println("-----��ѯ����------");
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
        user.setUserName("kkll");
        user.setUserAge(23);
        SqlSession session =MyBatisUtils.getSession();;
        try {
        	//userOperationΪʵ����UserMapper�ӿڵĶ�̬�������
        	UserMapper userOperation=session.getMapper(UserMapper.class);
            userOperation.addUser(user);
            session.commit();
            System.out.println("��ǰ���ӵ��û� idΪ:"+user.getId());
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
        	//userOperationΪʵ����UserMapper�ӿڵĶ�̬�������
        	 UserMapper userOperation=session.getMapper(UserMapper.class);
            User user =userOperation.selectUserByID(2);
         
             System.out.println("����ǰ���û� idΪ:"+user.getId());
             System.out.println("����ǰ���û�NameΪ:"+user.getUserName());
             System.out.println("����ǰ���û�AddressΪ:"+user.getUserAddress());
            
             user.setUserAddress("senzen");
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
        	//userOperationΪʵ����UserMapper�ӿڵĶ�̬�������
        	 UserMapper userOperation=session.getMapper(UserMapper.class);
         	userOperation.deleteUser(6);
            session.commit();            
  
         } finally {
         	MyBatisUtils.closeSession(session);
         }
     }




}
