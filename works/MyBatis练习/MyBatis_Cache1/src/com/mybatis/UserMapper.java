package com.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserMapper {
	public void addUser(User user);
	public User selectUserByID(int id);
	public void updateUser(User user);
	public void deleteUser(int id);
	public List<User> selectUsers();    
	public List<Map>  selectMaps();  
    public void addDept(Dept dept);
    public List<Article> dynamicsqlif(Condition condition);

}
