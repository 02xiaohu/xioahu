package com.bjsxt.drp.web.itemmgr.forms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class ItemActionForm extends ActionForm {

    //物料代码
	private String itemNo;
	
	//物料名称
	private String itemName;
	
	//规格
	private String spec;
	
	//型号
	private String pattern;
	
	//类型
	private String categoryId;
	
	//单位
	private String unitId;
	
	//收集物料代码，主要用户批量删除
	private String[] selectFlag;
	
	//查询条件
	private String clientIdOrName;
	
	//上传的文件名
	private FormFile fileName;
	
	//第几页
	private int pageNo;
	
	//每页多少条
	private int pageSize;

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String[] getSelectFlag() {
		return selectFlag;
	}

	public void setSelectFlag(String[] selectFlag) {
		this.selectFlag = selectFlag;
	}

	public String getClientIdOrName() {
		return clientIdOrName;
	}

	public void setClientIdOrName(String clientIdOrName) {
		this.clientIdOrName = clientIdOrName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public FormFile getFileName() {
		return fileName;
	}

	public void setFileName(FormFile fileName) {
		this.fileName = fileName;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}	
}
