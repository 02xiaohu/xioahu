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

/* ���������� mapper ����Ļ��棬��� SqlSession ����*/
public class TwoCacheTest extends TestCase {
	 
		 /**
	     * ʹ����һ������
	     * ֻ�е�һ�β�ѯʱ���������ݿ⣬�ڶ��β�ѯֱ�Ӵӻ����ȡ����.
	     */
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
		     * ӳ���ļ������е�select��佫�ᱻ����
		     * ӳ���ļ������е� insert��update��delete������ˢ�»��棬
		     * ��֤�����е���Ϣ�����µ�
		     */
			  public void test2Cache(){
			         
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
			        //��ն������棬��֤�����е���Ϣ�����µ�
			         System.out.println("-----delete �� commit------");
			         session =MyBatisUtils.getSession();;
			         try {
			        	//userOperationΪʵ����UserMapper�ӿڵĶ�̬�������
			        	 UserMapper userOperation=session.getMapper(UserMapper.class);
			         	userOperation.deleteUser(10);
			            session.commit();            
			  
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
		 
			    /**
			     *����ʹ���˶�������
			     *�����ǲ�ͬ�� SqlSession
			     *ֻ�е�һ�β�ѯʱ���������ݿ⣬�ڶ��β�ѯֱ�ӴӶ��������ȡ����
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
				            /**
				             *��User.xml��mapper�����ļ���������selectUserByID
				             *��Article.xml��mapper�����ļ���������selectArticleByID
						     *������User.xml������ʹ���˶������棬����User.xmlӳ���ļ������е�select��佫������
						     *selectArticleByID����ʹ�ö�������
						     */
							  public void test5Cache(){
							         
							         SqlSession session =MyBatisUtils.getSession();;
							         try {
							        	 //userOperationΪʵ����UserMapper�ӿڵĶ�̬�������
							        	 UserMapper userOperation=session.getMapper(UserMapper.class);
							        	 System.out.println("-----��һ�β�ѯ------");
							        	// ��һ�β�ѯʱ���������ݿ�-������SQL���
							        	 Article article1 =userOperation.selectArticleByID(1);
							             System.out.println(article1);
							         } finally {
							         	MyBatisUtils.closeSession(session);
							         }
							          session =MyBatisUtils.getSession();;
							         try {
							        	 //userOperationΪʵ����UserMapper�ӿڵĶ�̬�������
							        	 UserMapper userOperation=session.getMapper(UserMapper.class);
							        	 System.out.println("-----�ڶ��β�ѯ------");
							        	// �ڶ��β�ѯʱ���������ݿ�-������SQL���
							        	 Article article2 =userOperation.selectArticleByID(1);
							        	 System.out.println(article2);
							         } finally {
							         	MyBatisUtils.closeSession(session);
							         }
				  
				  }
							  /**
					             *��ͬһ��User.xml��mapper�����ļ���������selectArticleByID��selectUserByID
							     *ʹ���˶�������
							     *�����ǲ�ͬ�� SqlSession
							     *ֻ�е�һ�β�ѯʱ���������ݿ⣬�ڶ��β�ѯֱ�ӴӶ��������ȡ����
							     */
								  public void test4Cache(){
								         
								         SqlSession session =MyBatisUtils.getSession();;
								         try {
								        	 //userOperationΪʵ����UserMapper�ӿڵĶ�̬�������
								        	 UserMapper userOperation=session.getMapper(UserMapper.class);
								        	 System.out.println("-----��һ�β�ѯ------");
								        	// ��һ�β�ѯʱ���������ݿ�-������SQL���
								        	 Article article1 =userOperation.selectArticleByID(1);
								             System.out.println(article1);
								         } finally {
								         	MyBatisUtils.closeSession(session);
								         }
								          session =MyBatisUtils.getSession();;
								         try {
								        	 //userOperationΪʵ����UserMapper�ӿڵĶ�̬�������
								        	 UserMapper userOperation=session.getMapper(UserMapper.class);
								        	 System.out.println("-----�ڶ��β�ѯ------");
								        	// �ڶ��β�ѯʱ���������ݿ�-������SQL���
								        	 Article article2 =userOperation.selectArticleByID(1);
								        	 System.out.println(article2);
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
