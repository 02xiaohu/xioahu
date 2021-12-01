package com.bjsxt.drp.web.itemmgr.forms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class ItemActionForm extends ActionForm {

    //���ϴ���
	private String itemNo;
	
	//��������
	private String itemName;
	
	//���
	private String spec;
	
	//�ͺ�
	private String pattern;
	
	//����
	private String categoryId;
	
	//��λ
	private String unitId;
	
	//�ռ����ϴ��룬��Ҫ�û�����ɾ��
	private String[] selectFlag;
	
	//��ѯ����
	private String clientIdOrName;
	
	//�ϴ����ļ���
	private FormFile fileName;
	
	//�ڼ�ҳ
	private int pageNo;
	
	//ÿҳ������
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
