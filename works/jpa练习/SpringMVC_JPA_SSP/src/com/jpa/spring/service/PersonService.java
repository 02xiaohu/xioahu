
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
  
	/**֯�����ﴫ������
	 * @Transactional(propagation=Propagation.REQUIRED) ��
	 * ���������, ��ô�����������������, û�еĻ�����������½�һ������(Ĭ�������)
	 */
  @Transactional
  public void savePersons(Person p1, Person p2){
		personDao.save(p1);
		personDao.save(p2);
	}
 }
