package com.bjsxt.drp.business.itemmgr.factory;

import com.bjsxt.drp.business.itemmgr.dao.ItemDao;
import com.bjsxt.drp.business.itemmgr.dao.ItemDao4MySqlImpl;

/**
 * ���Ϲ�����   
 * @author Administrator
 *
 */
public class ItemDaoFactory {
	
	private static ItemDaoFactory instance; 
	
	private ItemDao itemDao;
	
	private ItemDaoFactory() {
		//
		//���Դ������ļ��ж�̬װ��ItemDao4MySqlImplʵ����,����������
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
	 * ����ItemDao����
	 * @return itemDao
	 */
	public ItemDao createItemDao() {
		return itemDao;
	}
}
