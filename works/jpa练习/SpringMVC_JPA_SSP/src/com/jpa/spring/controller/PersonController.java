
 package com.jpa.spring.controller;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import com.jpa.spring.bean.Person;
 import com.jpa.spring.service.PersonService;

 @Controller
 public class PersonController {
  @Autowired
  private PersonService personService; 
  @RequestMapping("/add.do")
  public String addPerson(Person person) {
	Person p1=new Person();
	p1.setName("zhangsan");
	p1.setAge(23);
	
	Person p2=new Person();
	p2.setName("lisi");
	p2.setAge(21);
	
	personService.savePersons(p1, p2);
	return "success";
	}
 
 }
