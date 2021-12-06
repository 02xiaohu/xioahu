package com.mybatis;

import java.util.List;

public interface Mapper {
 public void addRole(Role role);
 public void addUser(User user);
 public User getUser(int id);
 public void addUserRole(UseRole useRole);
 public Role getRoleByID (int id);


 

}
