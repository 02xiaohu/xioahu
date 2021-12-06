
 package com.springdata.dao;

 import org.springframework.data.repository.CrudRepository;
 import com.springdata.bean.Address;
 /**
  * 1.Repository 是一个空接口. 即是一个标记接口
  * 2.若我们定义的接口继承了 Repository, 则该接口会被 IOC 容器识别为一个 Repository Bean.
  * 3.CrudRepository为Repository的子接口
  *
  */
 public interface AddressRepsotory extends CrudRepository<Address, Integer> {

 }
