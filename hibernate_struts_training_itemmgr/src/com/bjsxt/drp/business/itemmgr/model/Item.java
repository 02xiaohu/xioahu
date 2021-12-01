package com.bjsxt.drp.business.itemmgr.model;

/**
 * 物料实体类
 * @author Administrator
 *
 */
public class Item {
	
    //物料代码
	private String itemNo;
	
	//物料名称
	private String itemName;
	
	//规格
	private String spec;
	
	//型号
	private String pattern;
	
	//类型
	private ItemCategory category;
	
	//单位
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
