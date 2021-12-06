
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
		customer.setLastName("tom");
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
		Customer customer = entityManager.find(Customer.class, 1);
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
		Customer customer = entityManager.find(Customer.class, 1);
		customer.setLastName("kkkkkkk");
	
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
