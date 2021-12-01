package com.bjsxt.drp.business.itemmgr.model;

/**
 * ����ʵ����
 * @author Administrator
 *
 */
public class Item {
	
    //���ϴ���
	private String itemNo;
	
	//��������
	private String itemName;
	
	//���
	private String spec;
	
	//�ͺ�
	private String pattern;
	
	//����
	private ItemCategory category;
	
	//��λ
	private ItemUnit unit;

	public ItemCategory getCategory() {
		return category;
	}

	public void setCategory(ItemCategory category) {
		this.category = category;
	}

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

	public ItemUnit getUnit() {
		return unit;
	}

	public void setUnit(ItemUnit unit) {
		this.unit = unit;
	}
}
