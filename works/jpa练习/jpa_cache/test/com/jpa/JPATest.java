
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
			
		Customer customer = entityManager.getReference(Customer.class, 1);
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
		Customer customer = entityManager.find(Customer.class, 1);
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
	
	//���Զ�������
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
	
	//����һ�����棬����session������
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
	
	//ʹ��jpa�Ĳ�ѯ���������.
	//��ʹ��һ�����棬�رն������棬���ò�ѯ���� �洢����
	@Test
	public void testQueryCache1(){
		//�ᷢSQL��������
		entityManager = entityManagerFactory.createEntityManager();
		String jpql = "select c.id,c.age FROM Customer c";
		Query query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		
		List customers = query.getResultList();
		System.out.println("size="+customers.size());
		entityManager.close();
		System.out.println("---------------------------------------------------");
		 //���ᷢSQL��䣬��ѯ����ֻ��������
		entityManager = entityManagerFactory.createEntityManager();
		jpql = "select c.id,c.age FROM Customer c";
		query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		
		customers = query.getResultList();
		System.out.println("size="+customers.size());
		entityManager.close();
	}
	//ʹ��jpa�Ĳ�ѯ��������.
	//��ʹ��һ�����棬�رն������棬���ò�ѯ���� �洢����
	@Test
	public void testQueryCache2(){
		//�ᷢSQL����ѯ���ж��󣬲������ж����ID���Դ�ŵ���ѯ����
		entityManager = entityManagerFactory.createEntityManager();
		String jpql = "FROM Customer";
		Query query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		
		List<Customer> customers = query.getResultList();
		for(Customer customer:customers)
			System.out.println(customer); 
		 System.out.println("size="+customers.size());
		entityManager.close();
		System.out.println("---------------------------------------------------");
		 //�ᷢ��N��SQL���
		//�ȵ���ѯ����ȡ�����ж����ID���ԣ�Ȼ�󵽶����������ID���Ҷ���
		// ����������û�п����������ID�����ݿⷢN��SQL���������ж��󣬼�n+1����
		entityManager = entityManagerFactory.createEntityManager();
		query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
	
		customers = query.getResultList();
		for(Customer customer:customers)
			System.out.println(customer); 
		 System.out.println("size="+customers.size());
		entityManager.close();
	}
	
	//ʹ��jpa�Ĳ�ѯ��������.
	//��ʹ��һ�����棬�����������棬���ò�ѯ���棬 �洢����
	@Test
	public void testQueryCache3(){
		//�ᷢSQL����ѯ���ж��󣬲������ж����ID���Դ�ŵ���ѯ����
		entityManager = entityManagerFactory.createEntityManager();
		String jpql = "FROM Customer";
		Query query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		
		List<Customer> customers = query.getResultList();
		for(Customer customer:customers)
			System.out.println(customer); 
		 System.out.println("size="+customers.size());
		entityManager.close();
		System.out.println("---------------------------------------------------");
		 //���ᷢSQL���
		//�ȵ���ѯ����ȡ�����ж����ID���ԣ�Ȼ�󵽶����������ID���Ҷ���
		// ����ID������������������ж���
		entityManager = entityManagerFactory.createEntityManager();
		query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
	
		customers = query.getResultList();
		for(Customer customer:customers)
			System.out.println(customer); 
		 System.out.println("size="+customers.size());
		entityManager.close();
	}
	//����ִ��֮��ִ��
	@After
	public void destroy(){
		//�ر� EntityManagerFactory
		entityManagerFactory.close();
	}
 
  }
