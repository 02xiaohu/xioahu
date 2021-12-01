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
 * ≤‚ ‘Iterate
 * @author Administrator
 *
 */
public class IterateTestAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Group group = new Group();
		group.setName("ZTE");
		
		List userList = new ArrayList();
		for (int i=0; i<10; i++) {
			User user = new User();
			user.setUsername("user_" + i);
			user.setAge(18+i);
			user.setGroup(group);
			userList.add(user);
		}
		
		request.setAttribute("userlist", userList);
		
		return mapping.findForward("success");
	}

}
