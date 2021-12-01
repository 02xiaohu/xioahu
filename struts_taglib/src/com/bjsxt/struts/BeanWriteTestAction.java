package com.bjsxt.struts;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 测试BeanWrite
 * @author Administrator
 *
 */
public class BeanWriteTestAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//普通属性
		request.setAttribute("hello", "Hello World");
		
		//html文本
		request.setAttribute("bj", "<font color='red'>北京欢迎您</font>");
		
		//日期
		request.setAttribute("today", new Date());
		
		//数字
		request.setAttribute("n", 123456.987);
		
		//结构
		Group group = new Group();
		group.setName("zte");
		
		User user = new User();
		user.setUsername("张三");
		user.setAge(18);
		user.setGroup(group);
		
		request.setAttribute("user", user);
		
		return mapping.findForward("success");
	}

}
