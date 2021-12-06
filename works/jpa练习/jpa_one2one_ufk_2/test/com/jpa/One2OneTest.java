
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

 public class One2OneTest {
	 
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
			Person person = entityManager.find(Person.class, 1);
			System.out.println("person.name=" + person.getName());
			System.out.println("idCard.cardNo=" + person.getIdCard().getCardNo());
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

			IdCard idCard = entityManager.find(IdCard.class, 1);
			System.out.println("idcard.cardNo=" + idCard.getCardNo());
			System.out.println("idcard.person.name=" + idCard.getPerson().getName());
			// �ύ����
	 		transaction.commit();
	 		//�ر� EntityManager
	 		entityManager.close();
          }
		
	    /*
	     *  
	     * 
	     */
	    @Test
		public void testSave1(){
		//���� EntityManager. ������ Hibernate�� Session 
		entityManager = entityManagerFactory.createEntityManager();
		//��������
		transaction = entityManager.getTransaction();
		transaction.begin();
		 
		IdCard idCard = new IdCard();
		idCard.setCardNo("88888888888888");
	    //jpa�������OneToOne�����������Զ�����
		entityManager.persist(idCard);
		
		Person person = new Person();
		person.setName("zhangsan");
		person.setIdCard(idCard); 
		entityManager.persist(person);  
		 
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