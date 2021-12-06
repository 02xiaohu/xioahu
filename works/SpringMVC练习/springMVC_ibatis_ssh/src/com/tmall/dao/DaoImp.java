
  package com.tmall.dao;

  import java.util.List;
  import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
  import com.tmall.bean.Ibatis;

  public class DaoImp extends SqlMapClientDaoSupport implements Dao {

	public void delete(int id) {
        getSqlMapClientTemplate().delete("deleteUsers", id);
    }

    public Ibatis getById(int id) {
        return (Ibatis)getSqlMapClientTemplate().queryForObject("getUsersById",id);
    }

    public Ibatis getByName(String username) {
        
        return (Ibatis)getSqlMapClientTemplate().queryForObject("getUsersByName",username);
    }

    @SuppressWarnings("unchecked")
	public List<Ibatis> getList() {
        return getSqlMapClientTemplate().queryForList("getAllUsers",null);
    }

    public void insert(Ibatis ibatis)
    {
        getSqlMapClientTemplate().insert("insertUsers",ibatis);
    }

    public void update(Ibatis ibatis) {
        getSqlMapClientTemplate().update("updateUsers", ibatis);
    }


  }
