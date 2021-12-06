
 package com.jpa;
 
 import java.util.Date;
 import java.util.List;
 import javax.persistence.EntityManager;
 import javax.persistence.EntityManagerFactory;
 import javax.persistence.EntityTransaction;
 import javax.persistence.Persistence;
 import javax.persistence.Query;
 import org.hibernate.ejb.QueryHints;
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
		customer.setLastName("tom123");
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
		Customer customer = entityManager.find(Customer.class, 5);
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
		Customer customer = entityManager.find(Customer.class, 5);
		customer.setLastName("kkkkkkk");
	
		// 提交事务
		transaction.commit();
		//关闭 EntityManager
		entityManager.close();
	}
	
	//测试二级缓存
	@Test
	public void testSecondLevelCache(){
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
		Customer customer1 = entityManager.find(Customer.class, 2);
		transaction.commit();
		entityManager.close();
		
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
		Customer customer2 = entityManager.find(Customer.class, 2);
		transaction.commit();
		entityManager.close();
	}
	
	//测试一级缓存，类似session级缓存
	@Test
	public void testFristLevelCache(){
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
		Customer customer1 = entityManager.find(Customer.class, 2);
		
		Customer customer2 = entityManager.find(Customer.class, 2);
		transaction.commit();
		entityManager.close();
	}
	
	//使用jpa的查询缓存查属性.
	//不使用一级缓存，关闭二级缓存，启用查询缓存 存储属性
	@Test
	public void testQueryCache1(){
		//会发SQL语句查属性
		entityManager = entityManagerFactory.createEntityManager();
		String jpql = "select c.id,c.age FROM Customer c";
		Query query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		
		List customers = query.getResultList();
		System.out.println("size="+customers.size());
		entityManager.close();
		System.out.println("---------------------------------------------------");
		 //不会发SQL语句，查询缓存只缓存属性
		entityManager = entityManagerFactory.createEntityManager();
		jpql = "select c.id,c.age FROM Customer c";
		query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		
		customers = query.getResultList();
		System.out.println("size="+customers.size());
		entityManager.close();
	}
	//使用jpa的查询缓存查对象.
	//不使用一级缓存，关闭二级缓存，启用查询缓存 存储对象
	@Test
	public void testQueryCache2(){
		//会发SQL语句查询所有对象，并把所有对象的ID属性存放到查询缓存
		entityManager = entityManagerFactory.createEntityManager();
		String jpql = "FROM Customer";
		Query query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		
		List<Customer> customers = query.getResultList();
		for(Customer customer:customers)
			System.out.println(customer); 
		 System.out.println("size="+customers.size());
		entityManager.close();
		System.out.println("---------------------------------------------------");
		 //会发发N条SQL语句
		//先到查询缓存取出所有对象的ID属性，然后到二级缓存根据ID查找对象，
		// 若二级缓存没有开启，则根据ID到数据库发N条SQL语句查找所有对象，即n+1问题
		entityManager = entityManagerFactory.createEntityManager();
		query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
	
		customers = query.getResultList();
		for(Customer customer:customers)
			System.out.println(customer); 
		 System.out.println("size="+customers.size());
		entityManager.close();
	}
	
	//使用jpa的查询缓存查对象.
	//不使用一级缓存，开启二级缓存，启用查询缓存， 存储对象
	@Test
	public void testQueryCache3(){
		//会发SQL语句查询所有对象，并把所有对象的ID属性存放到查询缓存
		entityManager = entityManagerFactory.createEntityManager();
		String jpql = "FROM Customer";
		Query query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		
		List<Customer> customers = query.getResultList();
		for(Customer customer:customers)
			System.out.println(customer); 
		 System.out.println("size="+customers.size());
		entityManager.close();
		System.out.println("---------------------------------------------------");
		 //不会发SQL语句
		//先到查询缓存取出所有对象的ID属性，然后到二级缓存根据ID查找对象，
		// 根据ID到到二级缓存查找所有对象。
		entityManager = entityManagerFactory.createEntityManager();
		query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
	
		customers = query.getResultList();
		for(Customer customer:customers)
			System.out.println(customer); 
		 System.out.println("size="+customers.size());
		entityManager.close();
	}
	//方法执行之后执行
	@After
	public void destroy(){
		//关闭 EntityManagerFactory
		entityManagerFactory.close();
	}
 
  }
