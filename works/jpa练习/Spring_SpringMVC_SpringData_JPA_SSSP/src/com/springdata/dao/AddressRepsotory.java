
 package com.springdata.dao;

 import org.springframework.data.repository.CrudRepository;
 import com.springdata.bean.Address;
 /**
  * 1.Repository ��һ���սӿ�. ����һ����ǽӿ�
  * 2.�����Ƕ���Ľӿڼ̳��� Repository, ��ýӿڻᱻ IOC ����ʶ��Ϊһ�� Repository Bean.
  * 3.CrudRepositoryΪRepository���ӽӿ�
  *
  */
 public interface AddressRepsotory extends CrudRepository<Address, Integer> {

 }
