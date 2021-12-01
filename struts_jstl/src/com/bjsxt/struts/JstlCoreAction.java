
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
  * ����jstl���Ŀ�
  * @author Administrator
  *
  */
 public class JstlCoreAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//��ͨ����
		request.setAttribute("hello", "Hello World");
	
		//�����������Ʊ�ǩ
		request.setAttribute("v1", 1);
		request.setAttribute("v2", 2);
		request.setAttribute("v3", new ArrayList());
		request.setAttribute("v4", "abc");
		
		//html�ı�
		request.setAttribute("bj", "<font color='red'>������ӭ��</font>");
		
		
		//����c:forEach
		Group group = new Group();
		group.setName("zte");
		
		List userList = new ArrayList();
		for (int i=0; i<10; i++) {
			User user = new User();
			user.setUsername("user_" + i);
			user.setAge(18+i);
			user.setGroup(group);
			userList.add(user);
		}
		
		request.setAttribute("userlist", userList);
		
		//����ѭ�����map
		Map map = new HashMap();
		map.put("k1", "v1");
		map.put("k2", "v2");
		request.setAttribute("mapvalue", map);
		
		//����c:forTokens
		request.setAttribute("strTokens", "1,2,3#4,5,6");
		return mapping.findForward("success");
	}

}
