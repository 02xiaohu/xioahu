package com.bjsxt.struts;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * ����BeanWrite
 * @author Administrator
 *
 */
public class BeanWriteTestAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//��ͨ����
		request.setAttribute("hello", "Hello World");
		
		//html�ı�
		request.setAttribute("bj", "<font color='red'>������ӭ��</font>");
		
		//����
		request.setAttribute("today", new Date());
		
		//����
		request.setAttribute("n", 123456.987);
		
		//�ṹ
		Group group = new Group();
		group.setName("zte");
		
		User user = new User();
		user.setUsername("����");
		user.setAge(18);
		user.setGroup(group);
		
		request.setAttribute("user", user);
		
		return mapping.findForward("success");
	}

}
