
 package com.springdata;

 import java.util.List;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;
 
 @Service
 public class PersonService {
  @Autowired
  private PersonRepsotory personRepsotory;
  @Autowired
  private AddressRepsotory addressRepsotory;
  
  //存储Address对象
  @Transactional
  public void saveAddress(Address address){
	 addressRepsotory.save(address);
	}
  /**织入事物传播特性
   * @Transactional(propagation=Propagation.REQUIRED) ：
   * 如果有事务, 那么事务管理器加入事务, 没有的话事务管理器新建一个事物(默认情况下)
   */
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
  
  public Iterable getAllPerson(){
	  Iterable<Person> persons=personRepsotory.findAll();
			return persons;
	  }
  
 
  //查找id为2的Address对象
  public Address getAddress(int id){
		Address address=addressRepsotory.findOne(id);
			return address;
	  }
 
  //若address对象在数据库中有对应的记录，则addressRepsotory.save(address)表示更新，发两条SQL语句。
  //select address0_.id as id1_0_0_, address0_.city as city2_0_0_ from JPA_ADDRESSES address0_ where address0_.id=?
  //update JPA_ADDRESSES set city=? where id=? 
  public void testModifyAddress(Address address){
		addressRepsotory.save(address);
			
	  }
  
  //删除Person的记录
  //select person0_.id as id1_1_1_, person0_.ADDRESS_ID as ADDRESS_5_1_1_, person0_.birth as birth2_1_1_, person0_.email as email3_1_1_, person0_.name as name4_1_1_, address1_.id as id1_0_0_, address1_.city as city2_0_0_ from JPA_PERSONS person0_ left outer join JPA_ADDRESSES address1_ on person0_.ADDRESS_ID=address1_.id where person0_.id=?
  //delete from JPA_PERSONS where id=?
  /**织入事物传播特性
   * @Transactional(propagation=Propagation.REQUIRED) ：
   * 如果有事务, 那么事务管理器加入事务, 没有的话事务管理器新建一个事物(默认情况下)
   */
  @Transactional
  public void testDeletePerson(int id){
	  personRepsotory.delete(id);
			
	  }
 
 
 }
