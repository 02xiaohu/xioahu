
 package com.springdata;

 import org.springframework.data.repository.CrudRepository;
 import org.springframework.data.repository.RepositoryDefinition;

 /**
  * 1.Repository 是一个空接口. 即是一个标记接口
  * 2.CrudRepository是Repository的子接口
  * 3. 若我们定义的接口继承了 Repository接口或其子接口, 则该接口会被 IOC 容器识别为一个 Repository Bean.
  *   纳入到 IOC 容器中. 进而可以在该接口中定义满足一定规范的方法. 
  * 4. 实际上, 也可以通过 @RepositoryDefinition 注解来替代继承 Repository 接口
  * public interface PersonRepsotory extends Repository<Person, Integer> 
  * @RepositoryDefinition(domainClass=Person.class,idClass=Integer.class)
  * public interface PersonRepsotory{}
  */
 public interface AddressRepsotory extends CrudRepository<Address, Integer> {

 }
