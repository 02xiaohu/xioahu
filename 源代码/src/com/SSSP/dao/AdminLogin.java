package com.SSSP.dao;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.SSSP.bean.logBean;

@Repository
public class AdminLogin {
	@PersistenceContext
	private EntityManager entitymanager;
		@Autowired
		private AdminRepository adminrepository;
		@Autowired
		private LogRepository logrepository;
		public boolean findAdmin(String name) {
			String sql="select a from Admin a where a.name=?1";
			Query query=entitymanager.createQuery(sql);
			query.setParameter(1,name);
			List result=query.getResultList();
			if(result.isEmpty()) {
				return false;
			}
			else return true;
		}
		public Page findAllUserLogs(int PageNo,int PageSize) {
			Order order=new Order(Direction.ASC,"log_id");
			Sort sort=new Sort(order);
			PageRequest pageable = new PageRequest(PageNo, PageSize, sort);
			Page<logBean> page = logrepository.findAll(pageable);
			return page;
		}
	public List findAllUsers() {
		String sql="from AllUserBean ";
		Query query=entitymanager.createQuery(sql);
		List<logBean> result=query.getResultList();
		return result;
	}
	public void freeze(String name) {
		String sql="update AllUserBean u set u.user_flag=0 where u.user_name=?1";
		Query query=entitymanager.createQuery(sql);
		query.setParameter(1,name);
		query.executeUpdate();
	}
	public void unfreeze(String name) {
		String sql="update AllUserBean u set u.user_flag=1 where u.user_name=?1";
		Query query=entitymanager.createQuery(sql);
		query.setParameter(1,name);
		query.executeUpdate();
	}
}
