package com.tmall.inter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tmall.bean.User;
public interface OperationMapper {
	public void addUser(User user);
	public User selectUserByID(int id);
	public void updateUser(User user);
	public void deleteUser(int id);
	public List<User> selectUsers();    
	public List<Map>  selectMaps();  

}
