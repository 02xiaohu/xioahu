package com.bjsxt.drp.web.itemmgr.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * �ռ������ݵ�ActionForm��ActionForm�е����Ա�����html�б��е�name����һ��
 * @author Administrator
 *
 */
public class LoginActionForm extends ActionForm {

	private String username;
	
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
