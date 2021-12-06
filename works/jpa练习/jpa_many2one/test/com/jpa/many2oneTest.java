
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
		
		//方法执行之前执行
		@Before
		public void init(){
		//创建 EntitymanagerFactory类似于 Hibernate的 SessionFactory
		entityManagerFactory = Persistence.createEntityManagerFactory("jpa-1");
		
		}
		@Test
		public void testGet(){
	      //创建 EntityManager. 类似于 Hibernate的 Session 
		  entityManager = entityManagerFactory.createEntityManager();
		  //开启事务
		  transaction = entityManager.getTransaction();
		  transaction.begin();
	    
		  User user = entityManager.find(User.class, 1);
		  System.out.println("user.name=" + user.getName());
		  System.out.println("user.group.name=" + user.getGroup().getName());
			
		  // 提交事务
	 	  transaction.commit();
	 	  //关闭 EntityManager
	 	  entityManager.close();
          }
	  
	    /*
	     * 测试 多对一的方式储存--多的一方（User）维护关系
	     * 
	     */
		@Test
		public void testSave(){
		//创建 EntityManager. 类似于 Hibernate的 Session 
		entityManager = entityManagerFactory.createEntityManager();
		//开启事务
		transaction = entityManager.getTransaction();
		transaction.begin();
		
		Group group = new Group();
		group.setName("zte");
		//如省略则产生TransientObjectException异常
		//entityManager.persist(group); 
		
	    User user1 = new User();
		user1.setName("zhangsan");
		user1.setGroup(group);
		entityManager.persist(user1);
		
		User user2 = new User();
		user2.setName("lisi");
		user2.setGroup(group);
		entityManager.persist(user2);
		
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