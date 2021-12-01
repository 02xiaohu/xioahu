package com.bjsxt.drp.business.itemmgr.dao;

import java.sql.Connection;
import java.util.List;

import com.bjsxt.drp.business.itemmgr.model.Item;
import com.bjsxt.drp.business.util.PageModel;

/**
 * 物料Dao接口
 * @author Administrator
 *
 */
public interface ItemDao {
	
	/**
	 * 添加物料
	 * @param conn
	 * @param item Item对象
	 */
	public void addItem(Connection conn, Item item);
	
	/**
	 * 修改物料
	 * @param conn
	 * @param item Item对象
	 */
	public void modifyItem(Connection conn, Item item);
	
	/**
	 * 根据物料代码批量删除
	 * @param conn
	 * @param itemNoList 物料代码的集合
	 */
	public void deleteItem(Connection conn, String[] itemNoList);

	/**
	 * 根据条件查询物料信息
	 * @param pageNo
	 * @param pageSize
	 * @param queryStr 查询条件
	 * @return
	 */
	public PageModel findAllItem(int pageNo, int pageSize, String queryStr);
	
	/**
	 * 根据Id查询物料信息
	 * @param itemNo 物料代码
	 * @return Item对象
	 */
	public Item findItemById(String itemNo);
}
