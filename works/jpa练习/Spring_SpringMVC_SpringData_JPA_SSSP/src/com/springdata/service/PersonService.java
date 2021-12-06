
 package com.springdata.service;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;
 import com.springdata.bean.Address;
 import com.springdata.bean.Person;
 import com.springdata.dao.AddressRepsotory;
 import com.springdata.dao.PersonRepsotory;
 
 @Service
 public class PersonService {

  @Autowired
  private AddressRepsotory addressRepsotory;
  @Autowired
  private PersonRepsotory personRepsotory;
  
  //存储Address对象
  @Transactional
  public void saveAddress(Address address){
	 addressRepsotory.save(address);
	}
  
  //存储Person对象
  @Transactional
  public void savePerson(Person person){
	  personRepsotory.save(person);
	}
  

  //查找id为1的Person对象
  public Person getPerson(int id){
		Person person=personRepsotory.findOne(id);
			return person;
	  }
 }
