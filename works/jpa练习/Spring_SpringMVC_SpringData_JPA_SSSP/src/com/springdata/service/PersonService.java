
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
  
  //�洢Address����
  @Transactional
  public void saveAddress(Address address){
	 addressRepsotory.save(address);
	}
  
  //�洢Person����
  @Transactional
  public void savePerson(Person person){
	  personRepsotory.save(person);
	}
  

  //����idΪ1��Person����
  public Person getPerson(int id){
		Person person=personRepsotory.findOne(id);
			return person;
	  }
 }
