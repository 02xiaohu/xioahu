package com.bjsxt.drp.business.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import org.hibernate.Session;

import com.bjsxt.drp.business.itemmgr.model.ItemCategory;
import com.bjsxt.drp.business.itemmgr.model.ItemUnit;

/**
 * DRP函数库
 * 注意开发函数库方法必须是静态的
 * @author Administrator
 *
 */
public class Functions {

	/**
	 * 返回物料类别的集合
	 * @return ItemCategory对象的集合
	 */
	public static List getItemCategoryList() {
		Session session = null;
		List itemCategoryList = null; 
		try {
//			session = HibernateUtils.getSession();
			session = HibernateFilter.getSession();
			session.beginTransaction();
			itemCategoryList = session.createQuery("from ItemCategory a order by a.id").list();
//			select itemcatego0_.id as id0_, itemcatego0_.name as name0_ from t_data_dict itemcatego0_ where itemcatego0_.category='item_category' order by itemcatego0_.id
//			
//			session.getTransaction().commit();
		}catch(Exception e) {
			//记录日志,log4j等......
			e.printStackTrace();
			session.getTransaction().rollback();
//		}finally {
//			HibernateUtils.closeSession(session);
		}
		return itemCategoryList;
	}

	/**
	 * 返回物料单位的集合
	 * @return ItemCategory对象的集合
	 */
	public static List getItemUnitList() {
		Session session = null;
		List itemUnitList = null; 
		try {
//			session = HibernateUtils.getSession();
			session = HibernateFilter.getSession();
			session.beginTransaction();
			itemUnitList = session.createQuery("from ItemUnit a order by a.id").list();
		//Hibernate: select itemunit0_.id as id0_, itemunit0_.name as name0_ from t_data_dict itemunit0_ where itemunit0_.category='item_unit' order by itemunit0_.id
			session.getTransaction().commit();
		}catch(Exception e) {
			//记录日志,log4j等......
			e.printStackTrace();
			session.getTransaction().rollback();
//		}finally {
//			HibernateUtils.closeSession(session);
		}
		return itemUnitList;
	}
}
