package com.bjsxt.struts;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * 登录的ActionForm，ActionForam是做数据收集的，
 * 
 * ActionForm中的属性必须和表单中输入域的名称一致
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
	
	 
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		System.out.println("----------LoginActionForm.reset()-----------");
	}

	 
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		System.out.println("----------LoginActionForm.validate()-----------");
		return null;
	}
	
}
