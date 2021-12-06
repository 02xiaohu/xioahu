
 package com.springdata.controller;

 import java.util.Date;
 import java.util.Map;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import com.springdata.bean.Address;
 import com.springdata.bean.Person;
 import com.springdata.service.PersonService;
  
 @Controller
 public class PersonController {
  @Autowired
  private PersonService personService; 
 //http://127.0.0.1:8080/Spring_SpringMVC_SpringData_JPA_SSSP/addperson.do
  @RequestMapping("/addperson.do")
  public String addPerson() {
	Address address=new Address();
	address.setCity("shenzhen");
	personService.saveAddress(address);
	
	Person person = new Person();
	person.setName("tom");
	person.setEmail("tom@123.com");
	person.setBirth(new Date());
	person.setAddress(address);
	
	personService.savePerson(person);
	
	//¸üÐÂ
	address.setCity("nng");
	personService.saveAddress(address);
	
	return "success";
	}
  
  //http://127.0.0.1:8080/Spring_SpringMVC_SpringData_JPA_SSSP/findbyid.do?id=1
  @RequestMapping("/findbyid.do")
  public String findbyid(int id,Map map) {
	  Person person=personService.getPerson(id);
	 System.out.println(person);
	 map.put("person", person);
	 return "view";
  }
 
 }
