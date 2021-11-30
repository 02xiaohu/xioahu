package com.SSSP.dao;
import java.util.Iterator;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.*;

import com.SSSP.bean.AllUserBean;
import com.SSSP.bean.logBean;
@Repository
public class UserLogin {
	@PersistenceContext
private EntityManager entitymanager;
	@Autowired
	private UserRepository userrepository;
	public boolean findByNaPa(String name,String password) {
		Integer id=0;
		String sql="select u.user_id from AllUserBean  u where u.user_name=?1 and u.user_password=?2";
		Query query=entitymanager.createQuery(sql);
		query.setParameter(1,name).setParameter(2,password);
		List result=query.getResultList();
		for(Iterator iter=result.iterator();iter.hasNext();) {
		id=(Integer)iter.next();
		}
		if(id==0) {return false;}
		else {return true;}
	}
	public AllUserBean findByName(String name) {
		AllUserBean user=new AllUserBean();
		String sql="select u from AllUserBean u where u.user_name=?1";
		Query  query=entitymanager.createQuery(sql);
		query.setParameter(1,name);
		List<AllUserBean> list=query.getResultList();
		for(Iterator iter=list.iterator();iter.hasNext();) {
			 user=(AllUserBean)iter.next();
		}
		return user;
	}
	
	public boolean register(String name,String password) {
		String sql=" select u from AllUserBean u where u.user_name=?1 ";
	    Query query=entitymanager.createQuery(sql);
	    query.setParameter(1,name);
	    List result=query.getResultList();
	    if(result.isEmpty()) {
	    	AllUserBean user=new AllUserBean();
	    	user.setUser_name(name);user.setUser_password(password);
	    	userrepository.save(user);
	    	return true;
	    }
	    
	   return false;
	}
	public double inquiry(String name) {
		double balance=0;
		String sql="select u.balance from AllUserBean u where u.user_name=?1 ";
		Query query=entitymanager.createQuery(sql);
		 query.setParameter(1,name);
		 List result=query.getResultList();
			for(Iterator iter=result.iterator();iter.hasNext();) {
				balance=(double)iter.next();
				}
			return balance;
	}
	public void deposit(String name,double money) {
		String sql="update  AllUserBean u  set u.balance=?1 where u.user_name=?2";
		double m=this.inquiry(name);
		Query query=entitymanager.createQuery(sql);
		 query.setParameter(1,money+m).setParameter(2,name);
		 query.executeUpdate();
	}
	public void withdrawal(String name,double money) {
		String sql="update  AllUserBean u  set u.balance=?1 where u.user_name=?2";
		double m=this.inquiry(name);
		Query query=entitymanager.createQuery(sql);
		 query.setParameter(1,m-money).setParameter(2,name);
		 query.executeUpdate();
	}
	public List findUserLog(String name) {
		AllUserBean user=this.findByName(name);
		String sql="select b from logBean b where b.user=?1";
		Query query=entitymanager.createQuery(sql);
		 query.setParameter(1,user);
		 List<logBean> logs=query.getResultList();
		 return logs;
	}
	public Integer getId(String name) throws Exception{
		String sql="select u.user_id from AllUserBean u where u.user_name=?1";
		Query query=entitymanager.createQuery(sql);
		 query.setParameter(1,name);
		 Integer id=(Integer)query.getSingleResult();
		 return id;
	}
	public int getFlag(String name)throws Exception {
		String sql="select u.user_flag from AllUserBean u where u.user_name=?1";
		Query query=entitymanager.createQuery(sql);
		query.setParameter(1,name);
		int flag=(int)query.getSingleResult();
		return flag;
	}
}
