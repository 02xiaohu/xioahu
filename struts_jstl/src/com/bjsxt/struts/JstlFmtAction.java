package com.bjsxt.struts;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * ≤‚ ‘jstl∏Ò ΩªØø‚
 * @author Administrator
 *
 */
public class JstlFmtAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("today", new Date());
		
		request.setAttribute("n", 123456.123);
		
		request.setAttribute("p", 0.12346);
		return mapping.findForward("success");
	}
	
	
}
