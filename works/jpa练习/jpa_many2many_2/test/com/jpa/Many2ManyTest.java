
 package com.jpa;

 import java.util.HashSet;
 import java.util.Set;
 import java.util.Iterator;
 import javax.persistence.EntityManager;
 import javax.persistence.EntityManagerFactory;
 import javax.persistence.EntityTransaction;
 import javax.persistence.Persistence;
 import org.junit.After;
 import org.junit.Before;
 import org.junit.Test;

 public class Many2ManyTest {
	 
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
		public void testLoad1(){
	       //���� EntityManager. ������ Hibernate�� Session 
			entityManager = entityManagerFactory.createEntityManager();
			//��������
			transaction = entityManager.getTransaction();
			transaction.begin();
			
			User user = entityManager.find(User.class, 1);
			System.out.println(user.getName());
			for (Iterator iter=user.getRoles().iterator(); iter.hasNext();) {
				Role role = (Role)iter.next();
				System.out.println(role.getName());
			}
			// �ύ����
	 		transaction.commit();
	 		//�ر� EntityManager
	 		entityManager.close();
          }
	    
		@Test
		public void testLoad2(){
	       //���� EntityManager. ������ Hibernate�� Session 
			entityManager = entityManagerFactory.createEntityManager();
			//��������
			transaction = entityManager.getTransaction();
			transaction.begin();
			
			Role role =entityManager.find(Role.class, 1);
			System.out.println(role.getName());
			for (Iterator iter=role.getUsers().iterator(); iter.hasNext();) {
				User user = (User)iter.next();
				System.out.println(user.getName());
			}
			// �ύ����
	 		transaction.commit();
	 		//�ر� EntityManager
	 		entityManager.close();
          }
		    
		/*
	     * ���� n��n�ķ�ʽ����--��User��ά����ϵ
	     * 
	     */
	    @Test
		public void testSave(){
		//���� EntityManager. ������ Hibernate�� Session 
		entityManager = entityManagerFactory.createEntityManager();
		//��������
		transaction = entityManager.getTransaction();
		transaction.begin();
		
		Role r1 = new Role();
		r1.setName("aaaa");
		entityManager.persist(r1);
		
		Role r2 = new Role();
		r2.setName("bbbb");
		entityManager.persist(r2);
		
		Role r3 = new Role();
		r3.setName("cccc"); 
		entityManager.persist(r3);
		
		User u1 = new User();
		u1.setName("zhangsan");
		Set u1Roles = new HashSet();
		u1Roles.add(r1);
		u1Roles.add(r2);
		u1.setRoles(u1Roles);
		entityManager.persist(u1);
		
		User u2 = new User();
		u2.setName("lisi");
		Set u2Roles = new HashSet();
		u2Roles.add(r2);
		u2Roles.add(r3);
		u2.setRoles(u2Roles);
		entityManager.persist(u2);
		
		User u3 = new User();
		u3.setName("wangwu");
		Set u3Roles = new HashSet();
		u3Roles.add(r1);
		u3Roles.add(r2);
		u3Roles.add(r3);
		u3.setRoles(u3Roles);
		entityManager.persist(u3);
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