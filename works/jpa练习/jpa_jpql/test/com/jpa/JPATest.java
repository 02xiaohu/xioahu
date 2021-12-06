
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
	
	//����ִ��֮ǰִ��
	@Before
	public void init(){
	//���� EntitymanagerFactory������ Hibernate�� SessionFactory
	entityManagerFactory = Persistence.createEntityManagerFactory("jpa-1");
	
	}
	
	//���г־û�����
	//������ hibernate �� save ����. ʹ��������ʱ״̬��Ϊ�־û�״̬. 
	//�� hibernate �� save �����Ĳ�֮ͬ��: �������� id, ����ִ�� insert ����, �����׳��쳣. 
	@Test
	public void testPersistence(){
		//���� EntityManager. ������ Hibernate�� Session 
		entityManager = entityManagerFactory.createEntityManager();
		//��������
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
	
		// �ύ����
		transaction.commit();
		//�ر� EntityManager
		entityManager.close();
	
	}
	
	//������ hibernate �� Session �� load ����
	@Test
	public void testGetReference(){
		//���� EntityManager. ������ Hibernate�� Session 
		entityManager = entityManagerFactory.createEntityManager();
		//��������
		transaction = entityManager.getTransaction();
		transaction.begin();
			
		Customer customer = entityManager.getReference(Customer.class, 5);
		System.out.println(customer.getLastName());
		System.out.println("-------------------------------------");
		System.out.println(customer);
	
		// �ύ����
		transaction.commit();
		//�ر� EntityManager
		entityManager.close();
	}
	
	//������ hibernate �� Session �� get ����. 
	@Test
	public void testFind() {
		//���� EntityManager. ������ Hibernate�� Session 
		entityManager = entityManagerFactory.createEntityManager();
		//��������
		transaction = entityManager.getTransaction();
		transaction.begin();
		Customer customer = entityManager.find(Customer.class, 5);
		System.out.println("-------------------------------------");
		System.out.println(customer);
	
		// �ύ����
		transaction.commit();
		//�ر� EntityManager
		entityManager.close();
	
	}
	
	//������ hibernate �� Session �� delete ����. �Ѷ����Ӧ�ļ�¼�����ݿ����Ƴ�
	//��ע��: �÷���ֻ���Ƴ� �־û� ����. �� hibernate �� delete����ʵ���ϻ��������߶���.
	@Test
	public void testRemove(){
		//���� EntityManager. ������ Hibernate�� Session 
		entityManager = entityManagerFactory.createEntityManager();
		//��������
		transaction = entityManager.getTransaction();
		transaction.begin();
		Customer customer = entityManager.find(Customer.class, 7);
		entityManager.remove(customer);
	
		// �ύ����
		transaction.commit();
		//�ر� EntityManager
		entityManager.close(); 
	}
	
	@Test
	public void testUpdate(){
		//���� EntityManager. ������ Hibernate�� Session 
		entityManager = entityManagerFactory.createEntityManager();
		//��������
		transaction = entityManager.getTransaction();
		transaction.begin();
		Customer customer = entityManager.find(Customer.class, 5);
		customer.setLastName("kkkkkkk");
	
		// �ύ����
		transaction.commit();
		//�ر� EntityManager
		entityManager.close();
	}
	
	/**
	 * ������һʱ, �����ȱ��� 1 ��һ��, �󱣴� n ��һ��, ��������������� UPDATE ���.
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
		
		
		// �ύ����
		transaction.commit();
		//�ر� EntityManager
		entityManager.close();
	}

	
	//ʹ��createQuery���JPQL���Բ�ѯ
	@Test
	public void testJPQL1(){
		entityManager = entityManagerFactory.createEntityManager();
		String jpql = "select c.id,c.age,c.lastName FROM Customer c WHERE c.age > ?";
		Query query=entityManager.createQuery(jpql);
		//ռλ���������Ǵ� 1 ��ʼ
		query.setParameter(1, 1);
		List<Customer> customers = query.getResultList();
		System.out.println(customers);
		entityManager.close();
	}
	
	//ʹ��createQuery���JPQL�����ѯ
	@Test
	public void testJPQL2(){
		entityManager = entityManagerFactory.createEntityManager();
		String jpql = "FROM Customer c WHERE c.age > ?";
		Query query=entityManager.createQuery(jpql);
		//ռλ���������Ǵ� 1 ��ʼ
		query.setParameter(1, 1);
		List<Customer> customers = query.getResultList();
		for(Customer customer:customers)
			System.out.println(customer); 
		 System.out.println("size="+customers.size());
		entityManager.close();
	}
	
	//createNativeQuery ������ԭ�� SQL��ѯ
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
	
	//createNamedQuery ��������ʵ����ǰʹ�� @NamedQuery ��ǵĲ�ѯ���
	@Test
	public void testJPQL4(){
		entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createNamedQuery("testNamedQuery").setParameter(1, 9);
		Customer customer = (Customer) query.getSingleResult();
		
		System.out.println(customer);
		entityManager.close();
	}
	
	//���Է���GROUP BY������ORDER BY
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
	 * JPQL �Ĺ�����ѯͬ HQL �Ĺ�����ѯ. 
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
	
	//����ʹ�� JPQL ��� UPDATE �� DELETE ����. 
	@Test
	public void testExecuteUpdate(){
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
		String jpql = "UPDATE Customer c SET c.lastName = ? WHERE c.id > ?";
		Query query = entityManager.createQuery(jpql).setParameter(1, "YYY").setParameter(2, 6);
		
		query.executeUpdate();
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
