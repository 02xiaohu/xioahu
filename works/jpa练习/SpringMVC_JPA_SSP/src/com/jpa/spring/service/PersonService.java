
 package com.jpa.spring.service;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;
 import com.jpa.spring.bean.Person;
 import com.jpa.spring.dao.PersonDao;
 
 @Service
 public class PersonService {
  @Autowired
  private PersonDao personDao;
  
	/**织入事物传播特性
	 * @Transactional(propagation=Propagation.REQUIRED) ：
	 * 如果有事务, 那么事务管理器加入事务, 没有的话事务管理器新建一个事物(默认情况下)
	 */
  @Transactional
  public void savePersons(Person p1, Person p2){
		personDao.save(p1);
		personDao.save(p2);
	}
 }
