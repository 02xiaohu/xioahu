package com.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {
	@Insert("insert into user(userName,userAge,userAddress)values(#{userName},#{userAge},#{userAddress})")
    /*����ʹ�� @Optionsע���userGeneratedKeys��keyProperty���������ݿ���� auto_increment�����������е�ֵ��
     * Ȼ�����ɵ�ֵ���õ�������������������*/
    @Options(useGeneratedKeys = true, keyProperty = "id")  
    public void addUser(User user);
	
	@Select("select * from `user` where id = #{id}")
	public User selectUserByID(int id);
	
	@Update("update user set userName=#{userName},userAge=#{userAge},userAddress=#{userAddress} where id=#{id}")
	public void updateUser(User user);
	
	@Delete("delete from user where id=#{id}")
	public void deleteUser(int id);
	
	@Select("select * from user")
	public List<User> selectUsers();    
	
	@Select("select id,userName,userAge from user")
	public List<Map>  selectMaps();  
    
	@Insert("insert into t_dept(Did,dname,loc) values(#{deptno},#{dname},#{loc})") 
	public void addDept(Dept dept);
    
	 

}
