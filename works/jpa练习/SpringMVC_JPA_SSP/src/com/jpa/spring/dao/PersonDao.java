
 package com.jpa.spring.dao;

 import javax.persistence.EntityManager;
 import javax.persistence.PersistenceContext;
 import com.jpa.spring.bean.Person;
 import org.springframework.stereotype.Repository;
 
 @Repository
 public class PersonDao {
   //通过 @PersistenceContext 注解来取得entityManager对象
   @PersistenceContext
   private EntityManager entityManager;
   public void save(Person person){
	 entityManager.persist(person);
    System.out.println(person.getName());
   }
 }
