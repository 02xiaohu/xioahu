package com.bjsxt.struts;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * ²âÊÔjstlº¯Êý¿â
 * @author Administrator
 *
 */
public class JstlFnAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		request.setAttribute("hello", "hello world");
		
		List list = new ArrayList();
		list.add("t1");
		list.add("t2");
		
		request.setAttribute("list", list);
		
		request.setAttribute("name", "Tom");
		return mapping.findForward("success");
	}

}
