
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
	
	//����
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
	
	//����id��ȡPerson
	@Test
	public void testCrudReposioryGet1(){
		Person person=personService.getPerson(1);
	
	System.out.println("person.id="+person.getId()+
			" person.name="+person.getName()+
			" person.address.city="+person.getAddress().getCity());
	}
	
	//����id��ȡAddress����
	@Test
	public void testCrudReposioryGet2(){
		Address address=personService.getAddress(2);
	System.out.println(address);
	}
	
	//�������е�Person��¼
	@Test
	public void testAllPersons(){
	  Iterable<Person> persons =personService.getAllPerson();
	System.out.println(persons);
	}
	
	//����Address��¼
	@Test
	public void testCrudReposiorymodifyAddress(){
		Address address=new Address();
		address.setId(3);
		address.setCity("shenzhen123");
		personService.testModifyAddress(address);
	}
	
	//ɾ��person��¼
	@Test
	public void testCrudReposiorydelete(){
		personService.testDeletePerson(3);
	}
	
	@Test
	public void testSpringDataJpa(){
		
	}
 
 }
