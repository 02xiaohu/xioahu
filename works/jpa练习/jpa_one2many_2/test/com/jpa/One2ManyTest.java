
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

 public class One2ManyTest {
	 
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
			Classes classes = entityManager.find(Classes.class, 1);
			System.out.println("classes.name=" + classes.getName());
			Set students = classes.getStudents();
			for (Iterator iter=students.iterator(); iter.hasNext();) {
				Student student = (Student)iter.next();
				System.out.println("student.name=" + student.getName());
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
			Student student = entityManager.find(Student.class, 1);
			System.out.println("student.name=" + student.getName());
			System.out.println("student.classes.name=" + student.getClasses().getName());
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
		 
		Student student1 = new Student();
		student1.setName("zhangsan");
		//entityManager.persist(student1);
		
		Student student2 = new Student();
		student2.setName("lisi");
		//entityManager.persist(student2);
		
		Set students = new HashSet();
		students.add(student1);
		students.add(student2);
		
		Classes classes = new Classes();
		classes.setName("zte");
		classes.setStudents(students);    
		entityManager.persist(classes);  
		 
		// 提交事务
		transaction.commit();
		//关闭 EntityManager
		entityManager.close();
	  }
	
	    @Test
		public void testSave2(){
		//创建 EntityManager. 类似于 Hibernate的 Session 
		entityManager = entityManagerFactory.createEntityManager();
		//开启事务
		transaction = entityManager.getTransaction();
		transaction.begin();
		 
		Classes classes = new Classes();
		classes.setName("zte"); 
		entityManager.persist(classes);
		
		Student student1 = new Student();
		student1.setName("zhangsan");
		student1.setClasses(classes);
		entityManager.persist(student1);
		
		Student student2 = new Student();
		student2.setName("lisi");
		student2.setClasses(classes);
		entityManager.persist(student2);
		  
		// 提交事务
		transaction.commit();
		//关闭 EntityManager
		entityManager.close();
	  }
	
	    @Test
		public void testSave3(){
		//创建 EntityManager. 类似于 Hibernate的 Session 
		entityManager = entityManagerFactory.createEntityManager();
		//开启事务
		transaction = entityManager.getTransaction();
		transaction.begin();  
		
		Classes classes = new Classes();
		classes.setName("zte");
		
		Student student1 = new Student();
		student1.setName("zhangsan");
		student1.setClasses(classes);
		
		Student student2 = new Student();
		student2.setName("lisi");
		student2.setClasses(classes);
		
		Set students = new HashSet();
		students.add(student1);
		students.add(student2);
		
		classes.setStudents(students);
		entityManager.persist(classes);
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