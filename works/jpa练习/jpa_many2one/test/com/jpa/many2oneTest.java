
 package com.jpa;

 import javax.persistence.EntityManager;
 import javax.persistence.EntityManagerFactory;
 import javax.persistence.EntityTransaction;
 import javax.persistence.Persistence;
 import org.junit.After;
 import org.junit.Before;
 import org.junit.Test;

 public class many2oneTest {
	 
	    private EntityManagerFactory entityManagerFactory;
		private EntityManager entityManager;
		private EntityTransaction transaction;
		
		//����ִ��֮ǰִ��
		@Before
		public void init(){
		//���� EntitymanagerFactory������ Hibernate�� SessionFactory
		entityManagerFactory = Persistence.createEntityManagerFactory("jpa-1");
		
		}
		@Test
		public void testGet(){
	      //���� EntityManager. ������ Hibernate�� Session 
		  entityManager = entityManagerFactory.createEntityManager();
		  //��������
		  transaction = entityManager.getTransaction();
		  transaction.begin();
	    
		  User user = entityManager.find(User.class, 1);
		  System.out.println("user.name=" + user.getName());
		  System.out.println("user.group.name=" + user.getGroup().getName());
			
		  // �ύ����
	 	  transaction.commit();
	 	  //�ر� EntityManager
	 	  entityManager.close();
          }
	  
	    /*
	     * ���� ���һ�ķ�ʽ����--���һ����User��ά����ϵ
	     * 
	     */
		@Test
		public void testSave(){
		//���� EntityManager. ������ Hibernate�� Session 
		entityManager = entityManagerFactory.createEntityManager();
		//��������
		transaction = entityManager.getTransaction();
		transaction.begin();
		
		Group group = new Group();
		group.setName("zte");
		//��ʡ�������TransientObjectException�쳣
		//entityManager.persist(group); 
		
	    User user1 = new User();
		user1.setName("zhangsan");
		user1.setGroup(group);
		entityManager.persist(user1);
		
		User user2 = new User();
		user2.setName("lisi");
		user2.setGroup(group);
		entityManager.persist(user2);
		
		// �ύ����
		transaction.commit();
		//�ر� EntityManager
		entityManager.close();
	  }
	
	  //����ִ��֮��ִ��
	  @After
	  public void destroy(){
		//�ر� EntityManagerFactory
		entityManagerFactory.close();
		}
 
  }