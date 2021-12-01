package com.bjsxt.drp.business.util;

import java.util.List;

/**
 * ��ҳ���
 * @author Administrator
 *
 */
public class PageModel {
	
	//�ܼ�¼��
	private int totalRecords;
	
	//�����
	private List list;
	
	//��ǰҳ
	private int pageNo;
	
    //ÿҳ��ʾ������
	private int pageSize; 
	
	public int getTotalRecords() {
		return totalRecords;
	}
	
	/**
	 * ȡ����ҳ��
	 * @return
	 */
	public int getTotalPages() {
		return (totalRecords + pageSize - 1) / pageSize;
	}
	
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	/**
	 * ȡ�õ�һҳ
	 * @return
	 */
	public int getTopPageNo() {
		return 1;
	}
	
	/**
	 * ȡ����һҳ
	 * @return
	 */
	public int getPreviousPageNo() {
		if (pageNo <= 1) {
			return 1;
		}
		return pageNo -1;
	}
	
	/**
	 * ȡ����һҳ
	 * @return
	 */
	public int getNextPageNo() {
		if (pageNo >= getTotalPages()) {
			return getTotalPages()==0?1:getTotalPages();
		}
		return pageNo + 1;
	}
	
	/**
	 * ȡ�����һҳ
	 * @return
	 */
	public int getBottomPageNo() {
		return getTotalPages() == 0?1:getTotalPages();
	}
}
