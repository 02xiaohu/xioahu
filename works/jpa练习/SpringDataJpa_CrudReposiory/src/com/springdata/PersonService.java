
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
  
  //�洢Address����
  @Transactional
  public void saveAddress(Address address){
	 addressRepsotory.save(address);
	}
  /**֯�����ﴫ������
   * @Transactional(propagation=Propagation.REQUIRED) ��
   * ���������, ��ô�����������������, û�еĻ�����������½�һ������(Ĭ�������)
   */
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
  
  public Iterable getAllPerson(){
	  Iterable<Person> persons=personRepsotory.findAll();
			return persons;
	  }
  
 
  //����idΪ2��Address����
  public Address getAddress(int id){
		Address address=addressRepsotory.findOne(id);
			return address;
	  }
 
  //��address���������ݿ����ж�Ӧ�ļ�¼����addressRepsotory.save(address)��ʾ���£�������SQL��䡣
  //select address0_.id as id1_0_0_, address0_.city as city2_0_0_ from JPA_ADDRESSES address0_ where address0_.id=?
  //update JPA_ADDRESSES set city=? where id=? 
  public void testModifyAddress(Address address){
		addressRepsotory.save(address);
			
	  }
  
  //ɾ��Person�ļ�¼
  //select person0_.id as id1_1_1_, person0_.ADDRESS_ID as ADDRESS_5_1_1_, person0_.birth as birth2_1_1_, person0_.email as email3_1_1_, person0_.name as name4_1_1_, address1_.id as id1_0_0_, address1_.city as city2_0_0_ from JPA_PERSONS person0_ left outer join JPA_ADDRESSES address1_ on person0_.ADDRESS_ID=address1_.id where person0_.id=?
  //delete from JPA_PERSONS where id=?
  /**֯�����ﴫ������
   * @Transactional(propagation=Propagation.REQUIRED) ��
   * ���������, ��ô�����������������, û�еĻ�����������½�һ������(Ĭ�������)
   */
  @Transactional
  public void testDeletePerson(int id){
	  personRepsotory.delete(id);
			
	  }
 
 
 }
