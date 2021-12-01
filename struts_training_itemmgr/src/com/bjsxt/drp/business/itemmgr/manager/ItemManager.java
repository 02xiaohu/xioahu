package com.bjsxt.drp.business.itemmgr.manager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.bjsxt.drp.business.itemmgr.factory.ItemDaoFactory;
import com.bjsxt.drp.business.itemmgr.model.Item;
import com.bjsxt.drp.business.util.DB;
import com.bjsxt.drp.business.util.PageModel;

/**
 * ���Ϲ����࣬���õ���ģʽʵ��
 * @author Administrator
 *
 */
public class ItemManager {

	private static ItemManager instance = new ItemManager();
	
	public static ItemManager getInstance() {
		return instance;
	}
	
	/**
	 * ������ϣ�����Daoʵ����
	 * @param item item����
	 */
	public void addItem(Item item) {
		Connection conn = null;
		try {
			conn = DB.getConn();
			ItemDaoFactory.getInstance().createItemDao().addItem(conn, item);
		}finally {
			DB.closeConn(conn);
		}
	}

	/**
	 * �޸����ϣ�����Daoʵ����
	 * @param item item����
	 */
	public void modifyItem(Item item) {
		Connection conn = null;
		try {
			conn = DB.getConn();
			ItemDaoFactory.getInstance().createItemDao().modifyItem(conn, item);
		}finally {
			DB.closeConn(conn);
		}
		
	}

	/**
	 * ɾ�����ϣ�����Daoʵ����
	 * @param itemNoList ���ϴ��뼯��
	 */
	public void deleteItem(String[] itemNoList) {
		Connection conn = null;
		try {
			conn = DB.getConn();
			ItemDaoFactory.getInstance().createItemDao().deleteItem(conn, itemNoList);
		}finally {
			DB.closeConn(conn);
		}
	}

	/**
	 * ����������ѯ������Ϣ
	 * @param queryStr ��ѯ����
	 * @return item����ļ���
	 */
	public PageModel findAllItem(int pageNo, int pageSize, String queryStr) {
		return ItemDaoFactory.getInstance().createItemDao().findAllItem(pageNo, pageSize, queryStr);
	}

	/**
	 * ����Id��ѯ���ϣ�����Daoʵ����
	 * @param item item����
	 */
	public Item findItemById(String itemNo) {
		return ItemDaoFactory.getInstance().createItemDao().findItemById(itemNo);
	}
}