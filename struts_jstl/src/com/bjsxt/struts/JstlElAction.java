package com.bjsxt.struts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * ����EL���ʽ
 * @author Administrator
 *
 */
public class JstlElAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//��ͨ�ַ���
		request.setAttribute("hello", "hello world");
		
		//�ṹ
		Group group = new Group();
		group.setName("zte");
		
		User user = new User();
		user.setUsername("����");
		user.setAge(18);
		user.setGroup(group);
		
		request.setAttribute("user", user);
		
		//map
		Map mapValue  = new HashMap();
		mapValue.put("key1", "value1");
		mapValue.put("key2", "value2");
		
		request.setAttribute("mapvalue", mapValue);
		
		//�ַ�������
		String[] strArray =null; 
			
		strArray =new String[]{"a", "b", "c"};
		request.setAttribute("str", strArray);
		
		User[] users = new User[10];
		for (int i=0; i<10; i++) {
			User u = new User();
			u.setUsername("U_" + i);
			users[i] = u;
		}
		request.setAttribute("usersArray", users);
		
		List userList = new ArrayList();
		for (int i=0; i<10; i++) {
			User uu = new User();
			uu.setUsername("UU_" + i);
			userList.add(uu);
		}
		request.setAttribute("userlist", userList);
		
		//empty
		request.setAttribute("abcd",6);
		request.setAttribute("value1", null);
		request.setAttribute("value2", "");
		request.setAttribute("value3", new ArrayList());
		request.setAttribute("value4", "123456");
		return mapping.findForward("success");
	}

	
}
