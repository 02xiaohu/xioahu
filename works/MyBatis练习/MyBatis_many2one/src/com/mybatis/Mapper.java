package com.mybatis;

import java.util.List;

public interface Mapper {
 public void addUser(User user);
 public void addGroup(Group group);
 
 public List<User> getUserGroup(int id);

 }
