package com.bjsxt.drp.business.itemmgr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bjsxt.drp.business.itemmgr.model.Item;
import com.bjsxt.drp.business.itemmgr.model.ItemCategory;
import com.bjsxt.drp.business.itemmgr.model.ItemUnit;
import com.bjsxt.drp.business.util.AppException;
import com.bjsxt.drp.business.util.DB;
import com.bjsxt.drp.business.util.PageModel;

/**
 * 物料维护MySql实现
 * @author Administrator
 * 
 */
public class ItemDao4MySqlImpl implements ItemDao {

	/**
	 * 添加物料
	 * @param conn
	 * @param item Item对象
	 */
	public void addItem(Connection conn, Item item) {
		String sql = "insert into t_items(item_no, item_name, spec, pattern, category, unit) " +
				"values(?, ?, ?, ?, ?, ?) ";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getItemNo());
			pstmt.setString(2, item.getItemName());
			pstmt.setString(3, item.getSpec());
			pstmt.setString(4, item.getPattern());
			pstmt.setString(5, item.getCategory().getId());
			pstmt.setString(6, item.getUnit().getId());
			pstmt.executeUpdate();
		}catch(SQLException e) {
			//记录日志,log4j等......
			e.printStackTrace();
			throw new AppException("drp.basedata.item.error.add"); 
		}finally {
			DB.closeStmt(pstmt);
		}
	}
	
	/**
	 * 根据物料代码批量删除
	 * @param conn
	 * @param itemNoList 物料代码的集合
	 */
	public void deleteItem(Connection conn, String[] itemNoList) {
		StringBuffer sbfSql = new StringBuffer();
		for (int i = 0; i < itemNoList.length; i++) {
			sbfSql.append("'")
				.append(itemNoList[i])
				.append("'")
				.append(",");
		}
		String sql = "delete from t_items where item_no in (" + sbfSql.substring(0, sbfSql.length()-1) + ")";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			//记录日志,log4j等......
			e.printStackTrace();
			throw new AppException("drp.basedata.item.error.delete");
		} finally {
			DB.closeStmt(stmt);
		}
	}
	
	/**
	 * 分页查询物料信息
	 * @param pageNo 第几页
	 * @param pageSize 每页多少条数据
	 * @param queryStr 条件
	 * @return pageModel对象
	 */
	public PageModel findAllItem(int pageNo, int pageSize, String queryStr) {
		StringBuffer sbSql = new StringBuffer();
		//第一种方法
		sbSql.append("select a.item_no, a.item_name, a.spec, a.pattern, a.category as category_id, ")
		     .append("b.name as category_name, a.unit as unit_id, c.name as unit_name ")
		     .append("from t_items a, t_data_dict b, t_data_dict c ")
		     .append("where a.category=b.id and a.unit=c.id ");

        //第二种方法 		
//		sbSql.append("select a.item_no, a.item_name, a.spec, a.pattern, a.category as category_id, ")
//			 .append("(select b.name from t_data_dict b where a.category=b.id) as category_name, ")
//			 .append("a.unit as unit_id, ")
//			 .append("(select c.name from t_data_dict c where a.unit=c.id) as unit_name ")
//			 .append("from t_items a ");
		if (queryStr != null && queryStr.trim().length() != 0) {
			sbSql.append("where a.item_no like '" + queryStr + "%' or a.item_name like '" + queryStr + "%'");
		}
		sbSql.append("order by a.item_no ")
			 .append("limit ")
			 .append((pageNo -1 ) * pageSize)
			 .append(", ")
			 .append(pageSize);
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		PageModel pageModel = null;
		try {
			conn = DB.getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sbSql.toString());
			List itemList = new ArrayList(); 
			while (rs.next()) {
				Item item = new Item();
				item.setItemNo(rs.getString("item_no"));
				item.setItemName(rs.getString("item_name"));
				item.setSpec(rs.getString("spec"));
				item.setPattern(rs.getString("pattern"));
				
				//构造ItemCategory
				ItemCategory ic = new ItemCategory();
				ic.setId(rs.getString("category_id"));
				ic.setName(rs.getString("category_name"));
				item.setCategory(ic);
				
				//构造ItemUnit
				ItemUnit iu = new ItemUnit();
				iu.setId(rs.getString("unit_id"));
				iu.setName(rs.getString("unit_name"));
				item.setUnit(iu);
				itemList.add(item);
			}
			pageModel = new PageModel();
			pageModel.setPageNo(pageNo);
			pageModel.setPageSize(pageSize);
			pageModel.setList(itemList);
			pageModel.setTotalRecords(getTotalRecords(conn, queryStr));
		}catch(SQLException e) {
			//记录日志,log4j等......
			e.printStackTrace();
			throw new AppException("drp.database.item.error.findallitem");
		}finally {
			DB.closeRs(rs);
			DB.closeStmt(stmt);
			DB.closeConn(conn);
		}		
		return pageModel;
	}

	/**
	 * 查询记录数
	 * @param conn
	 * @param queryStr 查询条件
	 * @return 符合条件的记录数
	 * @throws SQLException
	 */
	private int getTotalRecords(Connection conn, String queryStr) 
	throws SQLException {
		String sql = "select count(*) from t_items ";
		if (queryStr != null && queryStr.trim().length() != 0) {
			sql+="where item_no like '" + queryStr + "%' or item_name like '" + queryStr + "%'";
		}
		
		int totalRecords = 0;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				totalRecords = rs.getInt(1);
			}
		}finally {
			DB.closeRs(rs);
			DB.closeStmt(stmt);
		}
		return totalRecords;
	}	
	/**
	 * 根据Id查询物料信息
	 * 
	 * 关于类似t_items表，多个字段关联一个表可以采用多种方式进行查询,我们采用sql嵌套方式实现
	 * 
	 * @param itemNo 物料代码
	 * @return item对象的集合
	 */
	public Item findItemById(String itemNo) {
		//方法一：
		StringBuffer sbfSql = new StringBuffer();
//		sbfSql.append("select a.item_no, a.item_name, a.spec, a.pattern, a.category, ")
//		.append("(select b.name from t_data_dict b where b.id=a.category) as category_name, ")
//		.append("a.unit, (select c.name from t_data_dict c where c.id=a.unit) as unit_name ")
//		.append("from t_items a  where a.item_no=?");
		
//方法二：		
		sbfSql.append("select a.item_no, a.item_name, a.spec, a.pattern, b.id as category_id, ")
			  .append("b.name as category_name, c.id as unit_id, c.name as unit_name ")
			  .append("from t_items a, t_data_dict b, t_data_dict c ")
			  .append(" where a.category=b.id and a.unit=c.id and a.item_no=? ");
		
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		Item item = null;
		try {
			conn = DB.getConn();
			pstmt = conn.prepareStatement(sbfSql.toString());
			pstmt.setString(1, itemNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				item = new Item();
				item.setItemNo(rs.getString(1));
				item.setItemName(rs.getString(2));
				item.setSpec(rs.getString(3));
				item.setPattern(rs.getString(4));
				ItemCategory category = new ItemCategory();
				category.setId(rs.getString(5));
				category.setName(rs.getString(6));
				item.setCategory(category);
				ItemUnit unit = new ItemUnit();
				unit.setId(rs.getString(7));
				unit.setName(rs.getString(8));
				item.setUnit(unit);
			}
		} catch (SQLException e) {
			//记录日志,log4j等......
			e.printStackTrace();
			throw new AppException("drp.database.item.error.finditembyid", itemNo);
		} finally {
			DB.closeRs(rs);
			DB.closeStmt(pstmt);
			DB.closeConn(conn);
		}
		return item;	
	}

	/**
	 * 修改物料
	 * @param conn
	 * @param item Item对象
	 */
	public void modifyItem(Connection conn, Item item) {
		String sql = "update t_items set item_name=?, spec=?, pattern=?, category=?, unit=? " +
				"where item_no=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getItemName());
			pstmt.setString(2, item.getSpec());
			pstmt.setString(3, item.getPattern());
			pstmt.setString(4, item.getCategory().getId());
			pstmt.setString(5, item.getUnit().getId());
			pstmt.setString(6, item.getItemNo());
			pstmt.executeUpdate();
		}catch(SQLException e) {
			//记录日志,log4j等......
			e.printStackTrace();
			throw new AppException("drp.database.item.error.modify", item.getItemNo());
		}finally {
			DB.closeStmt(pstmt);
		}
	}
}