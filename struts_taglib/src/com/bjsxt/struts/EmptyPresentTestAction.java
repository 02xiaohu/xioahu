package com.bjsxt.struts;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * ≤‚ ‘empty,notEmpty,present,notPresent
 * @author Administrator
 *
 */
public class EmptyPresentTestAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("attr1", null);
		request.setAttribute("attr2", "");
		request.setAttribute("attr3", new ArrayList());
		return mapping.findForward("success");
	}

}
