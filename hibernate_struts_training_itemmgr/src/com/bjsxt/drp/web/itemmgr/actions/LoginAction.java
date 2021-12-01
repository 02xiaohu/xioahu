package com.bjsxt.drp.web.itemmgr.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bjsxt.drp.web.itemmgr.forms.LoginActionForm;

/**
 * ÓÃ»§µÇÂ¼Action
 * @author Administrator
 *
 */
public class LoginAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginActionForm laf = (LoginActionForm)form;
		String username = laf.getUsername();
		String password = laf.getPassword();
		
		if ("admin".equals(username) && "admin".equals(password)) {
			request.getSession().setAttribute("user", "admin");
			return mapping.findForward("sucess");
		}else {
			return mapping.findForward("index");
		}
	}

}
