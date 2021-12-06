
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
		
		//方法执行之前执行
		@Before
		public void init(){
		//创建 EntitymanagerFactory类似于 Hibernate的 SessionFactory
		entityManagerFactory = Persistence.createEntityManagerFactory("jpa-1");
		
		}
		@Test
		public void testLoad1(){
	       //创建 EntityManager. 类似于 Hibernate的 Session 
			entityManager = entityManagerFactory.createEntityManager();
			//开启事务
			transaction = entityManager.getTransaction();
			transaction.begin();
			
			User user = entityManager.find(User.class, 1);
			System.out.println(user.getName());
			for (Iterator iter=user.getRoles().iterator(); iter.hasNext();) {
				Role role = (Role)iter.next();
				System.out.println(role.getName());
			}
			// 提交事务
	 		transaction.commit();
	 		//关闭 EntityManager
	 		entityManager.close();
          }
	    
		@Test
		public void testLoad2(){
	       //创建 EntityManager. 类似于 Hibernate的 Session 
			entityManager = entityManagerFactory.createEntityManager();
			//开启事务
			transaction = entityManager.getTransaction();
			transaction.begin();
			
			Role role =entityManager.find(Role.class, 1);
			System.out.println(role.getName());
			for (Iterator iter=role.getUsers().iterator(); iter.hasNext();) {
				User user = (User)iter.next();
				System.out.println(user.getName());
			}
			// 提交事务
	 		transaction.commit();
	 		//关闭 EntityManager
	 		entityManager.close();
          }
		    
		/*
	     * 测试 n对n的方式储存--（User）维护关系
	     * 
	     */
	    @Test
		public void testSave(){
		//创建 EntityManager. 类似于 Hibernate的 Session 
		entityManager = entityManagerFactory.createEntityManager();
		//开启事务
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
		// 提交事务
		transaction.commit();
		//关闭 EntityManager
		entityManager.close();
	  }
	
	  //方法执行之后执行
	  @After
	  public void destroy(){
		//关闭 EntityManagerFactory
		entityManagerFactory.close();
		}
 
  }