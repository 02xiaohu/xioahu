
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
			Person person = entityManager.find(Person.class, 1);
			System.out.println("person.name=" + person.getName());
			System.out.println("idCard.cardNo=" + person.getIdCard().getCardNo());
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

			IdCard idCard = entityManager.find(IdCard.class, 1);
			System.out.println("idcard.cardNo=" + idCard.getCardNo());
			System.out.println("idcard.person.name=" + idCard.getPerson().getName());
			// 提交事务
	 		transaction.commit();
	 		//关闭 EntityManager
	 		entityManager.close();
          }
		
	    /*
	     *  
	     * 
	     */
	    @Test
		public void testSave1(){
		//创建 EntityManager. 类似于 Hibernate的 Session 
		entityManager = entityManagerFactory.createEntityManager();
		//开启事务
		transaction = entityManager.getTransaction();
		transaction.begin();
		 
		IdCard idCard = new IdCard();
		idCard.setCardNo("88888888888888");
	    //jpa外键进行OneToOne关联，不能自动级联
		entityManager.persist(idCard);
		
		Person person = new Person();
		person.setName("zhangsan");
		person.setIdCard(idCard); 
		entityManager.persist(person);  
		 
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