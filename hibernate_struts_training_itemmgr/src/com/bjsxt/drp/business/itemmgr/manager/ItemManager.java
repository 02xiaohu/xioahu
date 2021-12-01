package com.bjsxt.drp.business.itemmgr.manager;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.bjsxt.drp.business.itemmgr.model.Item;
import com.bjsxt.drp.business.util.AppException;
import com.bjsxt.drp.business.util.HibernateFilter;
import com.bjsxt.drp.business.util.HibernateUtils;
import com.bjsxt.drp.business.util.PageModel;

/**
 * 物料管理类，采用单例模式实现
 * @author Administrator
 *
 */
public class ItemManager {

	private static ItemManager instance = new ItemManager();
	
	public static ItemManager getInstance() {
		return instance;
	}
	
	/**
	 * 添加物料
	 * @param item item对象
	 */
	public void addItem(Item item) {
		Session session = null;
		try {
			//session = HibernateUtils.getSession();
			session = HibernateFilter.getSession();
			session.beginTransaction();
			session.save(item);
			session.getTransaction().commit();
		}catch(Exception e) {
			//记录日志,log4j等......
			e.printStackTrace();
			session.getTransaction().rollback();
			throw new AppException("drp.basedata.item.error.add"); 
//		}finally {
//			HibernateUtils.closeSession(session);
		}
	}

	/**
	 * 修改物料
	 * @param item item对象
	 */
	public void modifyItem(Item item) {
		Session session = null;
		try {
			//session = HibernateUtils.getSession();
			session = HibernateFilter.getSession();
			session.beginTransaction();
			session.update(item);
			session.getTransaction().commit();
		}catch(Exception e) {
			//记录日志,log4j等......
			e.printStackTrace();
			session.getTransaction().rollback();
			throw new AppException("drp.database.item.error.modify", item.getItemNo());
//		}finally {
//			HibernateUtils.closeSession(session);
		}
	}

	/**
	 * 删除物料
	 * @param itemNoList 物料代码集合
	 */
	public void deleteItem(String[] itemNoList) {
		Session session = null;
		try {
			//session = HibernateUtils.getSession();
			session = HibernateFilter.getSession();
			session.beginTransaction();
			for (int i=0; i<itemNoList.length; i++) {
				Item item = (Item)session.load(Item.class, itemNoList[i]);
				session.delete(item);
			}
			session.getTransaction().commit();
		}catch(Exception e) {
			//记录日志,log4j等......
			e.printStackTrace();
			session.getTransaction().rollback();
			throw new AppException("drp.basedata.item.error.delete");
//		}finally {
//			HibernateUtils.closeSession(session);
		}		
	}

	/**
	 * 根据条件查询物料信息
	 * @param queryStr 查询条件
	 * @return item对象的集合
	 */
	public PageModel findAllItem(int pageNo, int pageSize, String queryStr) {
		Session session = null;
		PageModel pageModel = null;
		try {
			//session = HibernateUtils.getSession();
			session = HibernateFilter.getSession();
			session.beginTransaction();
			Query query = null;
			if (queryStr != null && queryStr.trim().length() != 0) {
				query = session.createQuery("from Item a where a.itemNo like ? or a.itemName like ? order by a.itemNo")
					   			.setParameter(0, queryStr + "%")
					   			.setParameter(1, queryStr + "%");
			}else {
				query = session.createQuery("from Item a order by a.itemNo"); 
				//query = session.createQuery("select a from Item a join fetch a.category b join fetch a.unit c order by a.itemNo");
			}
			List itemList = query.setFirstResult((pageNo - 1) * pageSize)
				 				 .setMaxResults(pageSize)
				 				 .list();
			pageModel = new PageModel();
			pageModel.setPageNo(pageNo);
			pageModel.setPageSize(pageSize);
			pageModel.setList(itemList);
			pageModel.setTotalRecords(getTotalRecords(session, queryStr));
			
			session.getTransaction().commit();
		}catch(Exception e) {
			//记录日志,log4j等......
			e.printStackTrace();
			session.getTransaction().rollback();
			throw new AppException("drp.database.item.error.findallitem");
//		}finally {
//			HibernateUtils.closeSession(session);
		}	
		return pageModel;

	}
	
	/**
	 * 查询记录数
	 * 
	 * @param session
	 * @param queryStr
	 * @return
	 */
	private int getTotalRecords(Session session, String queryStr) {
		Query query = null;
		if (queryStr != null && queryStr.trim().length() != 0) {
			query = session.createQuery("select count(*) from Item a where a.itemNo like ? or a.itemName like ?")
   						   .setParameter(0, queryStr + "%")
   			               .setParameter(1, queryStr + "%");
		}else {
			query = session.createQuery("select count(*) from Item a");
		}
		Long count = (Long)query.uniqueResult();
		return count.intValue();
	}
	
	/**
	 * 根据Id查询物料
	 * @param item item对象
	 */
	public Item findItemById(String itemNo) {
		Session session = null;
		Item item = null;
		try {
//			session = HibernateUtils.getSession();
			session = HibernateFilter.getSession();
			session.beginTransaction();
			item = (Item)session.load(Item.class, itemNo);
			session.getTransaction().commit();
		}catch(Exception e) {
			//记录日志,log4j等......
			e.printStackTrace();
			session.getTransaction().rollback();
			throw new AppException("drp.basedata.item.error.delete");
//		}finally {
//			HibernateUtils.closeSession(session);
		}	
		return item;
	}
}