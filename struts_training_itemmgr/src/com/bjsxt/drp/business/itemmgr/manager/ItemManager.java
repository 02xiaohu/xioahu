package com.bjsxt.drp.business.itemmgr.manager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.bjsxt.drp.business.itemmgr.factory.ItemDaoFactory;
import com.bjsxt.drp.business.itemmgr.model.Item;
import com.bjsxt.drp.business.util.DB;
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
	 * 添加物料，调用Dao实现类
	 * @param item item对象
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
	 * 修改物料，调用Dao实现类
	 * @param item item对象
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
	 * 删除物料，调用Dao实现类
	 * @param itemNoList 物料代码集合
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
	 * 根据条件查询物料信息
	 * @param queryStr 查询条件
	 * @return item对象的集合
	 */
	public PageModel findAllItem(int pageNo, int pageSize, String queryStr) {
		return ItemDaoFactory.getInstance().createItemDao().findAllItem(pageNo, pageSize, queryStr);
	}

	/**
	 * 根据Id查询物料，调用Dao实现类
	 * @param item item对象
	 */
	public Item findItemById(String itemNo) {
		return ItemDaoFactory.getInstance().createItemDao().findItemById(itemNo);
	}
}