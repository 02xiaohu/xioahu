package com.bjsxt.drp.business.itemmgr.factory;

import com.bjsxt.drp.business.itemmgr.dao.ItemDao;
import com.bjsxt.drp.business.itemmgr.dao.ItemDao4MySqlImpl;

/**
 * 物料工厂类   
 * @author Administrator
 *
 */
public class ItemDaoFactory {
	
	private static ItemDaoFactory instance; 
	
	private ItemDao itemDao;
	
	private ItemDaoFactory() {
		//
		//可以从配置文件中动态装载ItemDao4MySqlImpl实现类,便于灵活更换
		//
		itemDao = new ItemDao4MySqlImpl();
	}
	
	public static synchronized ItemDaoFactory getInstance() {
		if (instance == null) {
			instance = new ItemDaoFactory();
		}
		return instance;
	}
	
	/**
	 * 创建ItemDao对象
	 * @return itemDao
	 */
	public ItemDao createItemDao() {
		return itemDao;
	}
}
