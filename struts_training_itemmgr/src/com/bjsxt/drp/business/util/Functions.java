package com.bjsxt.drp.business.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import com.bjsxt.drp.business.itemmgr.model.ItemCategory;
import com.bjsxt.drp.business.itemmgr.model.ItemUnit;

/**
 * DRP������
 * ע�⿪�������ⷽ�������Ǿ�̬��
 * @author Administrator
 *
 */
public class Functions {

	/**
	 * �����������Ľ��
	 * @return ItemCategory����ļ���
	 */
	public static List getItemCategoryList() {
		String sql = "select id, name from t_data_dict where category='item_category' order by id";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List itemCategoryList = new ArrayList();
		try {
			conn = DB.getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ItemCategory ic = new ItemCategory();
				ic.setId(rs.getString("id"));
				ic.setName(rs.getString("name"));
				itemCategoryList.add(ic);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeRs(rs);
			DB.closeStmt(stmt);
			DB.closeConn(conn);
		}
		return itemCategoryList;
	}

	/**
	 * �������ϵ�λ�ļ���
	 * @return ItemCategory����ļ���
	 */
	public static List getItemUnitList() {
		String sql = "select id, name from t_data_dict where category='item_unit' order by id";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List itemUnitList = new ArrayList();
		try {
			conn = DB.getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ItemUnit iu = new ItemUnit();
				iu.setId(rs.getString("id"));
				iu.setName(rs.getString("name"));
				itemUnitList.add(iu);
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeRs(rs);
			DB.closeStmt(stmt);
			DB.closeConn(conn);
		}
		return itemUnitList;
	}
}
