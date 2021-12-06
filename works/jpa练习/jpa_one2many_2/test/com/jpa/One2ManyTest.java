
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
			Classes classes = entityManager.find(Classes.class, 1);
			System.out.println("classes.name=" + classes.getName());
			Set students = classes.getStudents();
			for (Iterator iter=students.iterator(); iter.hasNext();) {
				Student student = (Student)iter.next();
				System.out.println("student.name=" + student.getName());
			}
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
			Student student = entityManager.find(Student.class, 1);
			System.out.println("student.name=" + student.getName());
			System.out.println("student.classes.name=" + student.getClasses().getName());
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
		 
		// �ύ����
		transaction.commit();
		//�ر� EntityManager
		entityManager.close();
	  }
	
	    @Test
		public void testSave2(){
		//���� EntityManager. ������ Hibernate�� Session 
		entityManager = entityManagerFactory.createEntityManager();
		//��������
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
		  
		// �ύ����
		transaction.commit();
		//�ر� EntityManager
		entityManager.close();
	  }
	
	    @Test
		public void testSave3(){
		//���� EntityManager. ������ Hibernate�� Session 
		entityManager = entityManagerFactory.createEntityManager();
		//��������
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