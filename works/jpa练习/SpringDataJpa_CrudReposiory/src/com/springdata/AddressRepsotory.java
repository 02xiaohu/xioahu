
 package com.springdata;

 import org.springframework.data.repository.CrudRepository;
 import org.springframework.data.repository.RepositoryDefinition;

 /**
  * 1.Repository ��һ���սӿ�. ����һ����ǽӿ�
  * 2.CrudRepository��Repository���ӽӿ�
  * 3. �����Ƕ���Ľӿڼ̳��� Repository�ӿڻ����ӽӿ�, ��ýӿڻᱻ IOC ����ʶ��Ϊһ�� Repository Bean.
  *   ���뵽 IOC ������. ���������ڸýӿ��ж�������һ���淶�ķ���. 
  * 4. ʵ����, Ҳ����ͨ�� @RepositoryDefinition ע��������̳� Repository �ӿ�
  * public interface PersonRepsotory extends Repository<Person, Integer> 
  * @RepositoryDefinition(domainClass=Person.class,idClass=Integer.class)
  * public interface PersonRepsotory{}
  */
 public interface AddressRepsotory extends CrudRepository<Address, Integer> {

 }
