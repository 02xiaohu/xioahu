package com.bjsxt.drp.business.itemmgr.dao;

import java.sql.Connection;
import java.util.List;

import com.bjsxt.drp.business.itemmgr.model.Item;
import com.bjsxt.drp.business.util.PageModel;

/**
 * ����Dao�ӿ�
 * @author Administrator
 *
 */
public interface ItemDao {
	
	/**
	 * �������
	 * @param conn
	 * @param item Item����
	 */
	public void addItem(Connection conn, Item item);
	
	/**
	 * �޸�����
	 * @param conn
	 * @param item Item����
	 */
	public void modifyItem(Connection conn, Item item);
	
	/**
	 * �������ϴ�������ɾ��
	 * @param conn
	 * @param itemNoList ���ϴ���ļ���
	 */
	public void deleteItem(Connection conn, String[] itemNoList);

	/**
	 * ����������ѯ������Ϣ
	 * @param pageNo
	 * @param pageSize
	 * @param queryStr ��ѯ����
	 * @return
	 */
	public PageModel findAllItem(int pageNo, int pageSize, String queryStr);
	
	/**
	 * ����Id��ѯ������Ϣ
	 * @param itemNo ���ϴ���
	 * @return Item����
	 */
	public Item findItemById(String itemNo);
}
