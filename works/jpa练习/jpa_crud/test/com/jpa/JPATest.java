
 package com.jpa;
 
 import java.util.Date;
 import javax.persistence.EntityManager;
 import javax.persistence.EntityManagerFactory;
 import javax.persistence.EntityTransaction;
 import javax.persistence.Persistence;
 import org.junit.After;
 import org.junit.Before;
 import org.junit.Test;

 public class JPATest {
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private EntityTransaction transaction;
	
	//方法执行之前执行
	@Before
	public void init(){
	//创建 EntitymanagerFactory类似于 Hibernate的 SessionFactory
	entityManagerFactory = Persistence.createEntityManagerFactory("jpa-1");
	
	}
	
	//进行持久化操作
	//类似于 hibernate 的 save 方法. 使对象由临时状态变为持久化状态. 
	//和 hibernate 的 save 方法的不同之处: 若对象有 id, 则不能执行 insert 操作, 而会抛出异常. 
	@Test
	public void testPersistence(){
		//创建 EntityManager. 类似于 Hibernate的 Session 
		entityManager = entityManagerFactory.createEntityManager();
		//开启事务
		transaction = entityManager.getTransaction();
		transaction.begin();
		
		Customer customer = new Customer();
		customer.setAge(20);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("tom@163.com");
		customer.setLastName("tom");
		//customer.setId(100);
		entityManager.persist(customer);
		System.out.println(customer.getId());
	
		// 提交事务
		transaction.commit();
		//关闭 EntityManager
		entityManager.close();
	
	}
	
	//类似于 hibernate 中 Session 的 load 方法
	@Test
	public void testGetReference(){
		//创建 EntityManager. 类似于 Hibernate的 Session 
		entityManager = entityManagerFactory.createEntityManager();
		//开启事务
		transaction = entityManager.getTransaction();
		transaction.begin();
			
		Customer customer = entityManager.getReference(Customer.class, 1);
		System.out.println(customer.getLastName());
		System.out.println("-------------------------------------");
		System.out.println(customer);
	
		// 提交事务
		transaction.commit();
		//关闭 EntityManager
		entityManager.close();
	}
	
	//类似于 hibernate 中 Session 的 get 方法. 
	@Test
	public void testFind() {
		//创建 EntityManager. 类似于 Hibernate的 Session 
		entityManager = entityManagerFactory.createEntityManager();
		//开启事务
		transaction = entityManager.getTransaction();
		transaction.begin();
		Customer customer = entityManager.find(Customer.class, 1);
		System.out.println("-------------------------------------");
		System.out.println(customer);
	
		// 提交事务
		transaction.commit();
		//关闭 EntityManager
		entityManager.close();
	
	}
	
	//类似于 hibernate 中 Session 的 delete 方法. 把对象对应的记录从数据库中移除
	//但注意: 该方法只能移除 持久化 对象. 而 hibernate 的 delete方法实际上还可以离线对象.
	@Test
	public void testRemove(){
		//创建 EntityManager. 类似于 Hibernate的 Session 
		entityManager = entityManagerFactory.createEntityManager();
		//开启事务
		transaction = entityManager.getTransaction();
		transaction.begin();
		Customer customer = entityManager.find(Customer.class, 1);
		entityManager.remove(customer);
	
		// 提交事务
		transaction.commit();
		//关闭 EntityManager
		entityManager.close(); 
	}
	
	@Test
	public void testUpdate(){
		//创建 EntityManager. 类似于 Hibernate的 Session 
		entityManager = entityManagerFactory.createEntityManager();
		//开启事务
		transaction = entityManager.getTransaction();
		transaction.begin();
		Customer customer = entityManager.find(Customer.class, 1);
		customer.setLastName("kkkkkkk");
	
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
