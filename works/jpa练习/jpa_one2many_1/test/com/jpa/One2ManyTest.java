
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
		public void testLoad(){
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
	  
	    /*
	     * ���� 1�Զ�ķ�ʽ����--1��һ����Classes��ά����ϵ
	     * 
	     */
	    @Test
		public void testSave(){
		//���� EntityManager. ������ Hibernate�� Session 
		entityManager = entityManagerFactory.createEntityManager();
		//��������
		transaction = entityManager.getTransaction();
		transaction.begin();
		
		Student student1 = new Student();
		student1.setName("zhangsan");
		entityManager.persist(student1);
		
		Student student2 = new Student();
		student2.setName("lisi");
		entityManager.persist(student2);
		
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
	
	  //����ִ��֮��ִ��
	  @After
	  public void destroy(){
		//�ر� EntityManagerFactory
		entityManagerFactory.close();
		}
 
  }