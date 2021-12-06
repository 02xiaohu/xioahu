
 package com.jpa;
 
 import java.util.Date;
 import java.util.Iterator;
 import java.util.List;
 import javax.persistence.EntityManager;
 import javax.persistence.EntityManagerFactory;
 import javax.persistence.EntityTransaction;
 import javax.persistence.Persistence;
 import javax.persistence.Query;
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
			
		Customer customer = entityManager.getReference(Customer.class, 5);
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
		Customer customer = entityManager.find(Customer.class, 7);
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
	
	/**
	 * 保存多对一时, 建议先保存 1 的一端, 后保存 n 的一端, 这样不会多出额外的 UPDATE 语句.
	 */
	@Test
	public void testManyToOnePersist(){
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("gg@163.com");
		customer.setLastName("GG");
		entityManager.persist(customer);
		
		Order order1 = new Order();
		order1.setOrderName("G-GG-1");
		order1.setCustomer(customer);
		entityManager.persist(order1);
		
		Order order2 = new Order();
		order2.setOrderName("G-GG-2");
		order2.setCustomer(customer);
		entityManager.persist(order2);
		
		
		// 提交事务
		transaction.commit();
		//关闭 EntityManager
		entityManager.close();
	}

	
	//使用createQuery完成JPQL属性查询
	@Test
	public void testJPQL1(){
		entityManager = entityManagerFactory.createEntityManager();
		String jpql = "select c.id,c.age,c.lastName FROM Customer c WHERE c.age > ?";
		Query query=entityManager.createQuery(jpql);
		//占位符的索引是从 1 开始
		query.setParameter(1, 1);
		List<Customer> customers = query.getResultList();
		System.out.println(customers);
		entityManager.close();
	}
	
	//使用createQuery完成JPQL对象查询
	@Test
	public void testJPQL2(){
		entityManager = entityManagerFactory.createEntityManager();
		String jpql = "FROM Customer c WHERE c.age > ?";
		Query query=entityManager.createQuery(jpql);
		//占位符的索引是从 1 开始
		query.setParameter(1, 1);
		List<Customer> customers = query.getResultList();
		for(Customer customer:customers)
			System.out.println(customer); 
		 System.out.println("size="+customers.size());
		entityManager.close();
	}
	
	//createNativeQuery 适用于原生 SQL查询
	@Test
	public void testJPQL3(){
		entityManager = entityManagerFactory.createEntityManager();
		String sql = "SELECT * FROM jpa_cutomers" ;
		Query query = entityManager.createNativeQuery(sql);
	    List customers = query.getResultList();
        for (Iterator iter=customers.iterator(); iter.hasNext();) {
			Object[] obj = (Object[])iter.next();
			System.out.println(obj[0] + "," + obj[1]+ "," + obj[2]+ "," + obj[3]+ "," + obj[4] + "," + obj[5]);
		}
		entityManager.close();
	}
	
	//createNamedQuery 适用于在实体类前使用 @NamedQuery 标记的查询语句
	@Test
	public void testJPQL4(){
		entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createNamedQuery("testNamedQuery").setParameter(1, 9);
		Customer customer = (Customer) query.getSingleResult();
		
		System.out.println(customer);
		entityManager.close();
	}
	
	//测试分组GROUP BY和排序ORDER BY
	@Test
	public void testGroupBy(){
		entityManager = entityManagerFactory.createEntityManager();
		String jpql = "SELECT c.age,count(*) FROM Customer c GROUP BY c.age ORDER BY c.age DESC"; 
		Query query=entityManager.createQuery(jpql);
	
		List  customers = query.getResultList();
		for (Iterator iter=customers.iterator(); iter.hasNext();)
		{
			Object[] obj = (Object[])iter.next();
			System.out.println(obj[0]+ ", " + obj[1]);
		}
		entityManager.close();
	}
	
	/**
	 * JPQL 的关联查询同 HQL 的关联查询. 
	 */
	@Test
	public void testLeftOuterJoin(){
		entityManager = entityManagerFactory.createEntityManager();
		String jpql = "select o.orderName,c.lastName from Order o left outer join o.customer c";
		List  result = entityManager.createQuery(jpql).getResultList();

		for (Iterator iter=result.iterator(); iter.hasNext();)
		{
			Object[] obj = (Object[])iter.next();
			System.out.println(obj[0]+ ", " + obj[1]);
		}
		entityManager.close();
	}
	
	//可以使用 JPQL 完成 UPDATE 和 DELETE 操作. 
	@Test
	public void testExecuteUpdate(){
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
		String jpql = "UPDATE Customer c SET c.lastName = ? WHERE c.id > ?";
		Query query = entityManager.createQuery(jpql).setParameter(1, "YYY").setParameter(2, 6);
		
		query.executeUpdate();
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
