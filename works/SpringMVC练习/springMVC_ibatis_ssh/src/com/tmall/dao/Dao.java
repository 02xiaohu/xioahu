  
  package com.tmall.dao;
  import java.util.List;
  import com.tmall.bean.Ibatis;;

  public interface Dao {
	  public List<Ibatis> getList();
	  public Ibatis getByName(String username);
	  public Ibatis getById(int id);
	  public void insert(Ibatis ibatis);
	  public void delete(int id);
	  public void update(Ibatis ibatis);

   }
