
 package com.springdata;

 
 import java.util.Date;
 import org.springframework.context.ApplicationContext;
 import org.springframework.context.support.ClassPathXmlApplicationContext;
 import org.junit.Test;

 public class SpringDataTest {
	private ApplicationContext ctx = null;
	private PersonService personService;
	{
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		personService = ctx.getBean(PersonService.class);
	
	}
	
	//插入
	@Test
	public void testCrudReposiorySave(){
		
		Address address=new Address();
		address.setCity("shenzhen");
		personService.saveAddress(address);
		
		Person person = new Person();
		person.setName("lisi");
		person.setEmail("lisi@123.com");
		person.setBirth(new Date());
		person.setAddress(address);
		
		personService.savePerson(person);
	}
	
	//根据id获取Person
	@Test
	public void testCrudReposioryGet1(){
		Person person=personService.getPerson(1);
	
	System.out.println("person.id="+person.getId()+
			" person.name="+person.getName()+
			" person.address.city="+person.getAddress().getCity());
	}
	
	//根据id获取Address对象
	@Test
	public void testCrudReposioryGet2(){
		Address address=personService.getAddress(2);
	System.out.println(address);
	}
	
	//查找所有的Person记录
	@Test
	public void testAllPersons(){
	  Iterable<Person> persons =personService.getAllPerson();
	System.out.println(persons);
	}
	
	//更新Address记录
	@Test
	public void testCrudReposiorymodifyAddress(){
		Address address=new Address();
		address.setId(3);
		address.setCity("shenzhen123");
		personService.testModifyAddress(address);
	}
	
	//删除person记录
	@Test
	public void testCrudReposiorydelete(){
		personService.testDeletePerson(3);
	}
	
	@Test
	public void testSpringDataJpa(){
		
	}
 
 }
