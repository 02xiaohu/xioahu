package com.asm;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

//import entity.User;

@Controller  //����Struts��Action
public class TestController {

	@RequestMapping("/login.do")  // ����url��ַӳ�䣬����Struts��action-mapping
	public String testLogin(@RequestParam(value="username")String name, String password, HttpServletRequest request) {
		// @RequestParamһ�����ڵ���������������ͷ������ββ�һ��ʱ����ָ����������������������β�
		// @RequestParam�ɼ�дΪ��@RequestParam("username")
		 request.setAttribute("username", name);
	     request.setAttribute("password", password);
		if (!"admin".equals(name) || !"admin".equals(password)) {
			return "failure"; // ��תҳ��·����Ĭ��Ϊת��������·������Ҫ����spring-servlet�����ļ������õ�ǰ׺�ͺ�׺
		}
		return "success";
	}
	
	
	@RequestMapping("/login2.do")
	public ModelAndView testLogin2(String username, String password,int age,HttpServletRequest request){
		// request��response���ط�Ҫ�����ڷ����У�����ò��ϵĻ�����ȥ��
		// ��������������ҳ��ؼ���name��ƥ�䣬�������ͻ��Զ���ת��
		request.setAttribute("username", username);
		request.setAttribute("password", password);
		request.setAttribute("age", age);
		if ("admin".equals(username) &&"admin".equals(password)) {
			return new ModelAndView("success"); // �ֶ�ʵ����ModelAndView�����תҳ�棨ת������Ч����ͬ������ķ��������ַ���
		}
		return new ModelAndView(new RedirectView("./index.jsp"));  // �����ض���ʽ��תҳ��
		// �ض�����һ�ּ�д��
		// return new ModelAndView("redirect:../index.jsp");
	}
	
	
	@RequestMapping("/login3.do")
	
	public ModelAndView testLogin3(User user) {
		// ͬ��֧�ֲ���Ϊ������User������Struts��ActionForm��User����Ҫ�κ����ã�ֱ��д����,�ŵ�request���ö����С�
		String username = user.getUsername();
		String password = user.getPassword();
		int age = user.getAge();
		
		if (!"admin".equals(username) || !"admin".equals(password) ) {
			return new ModelAndView("error");
		}
		return new ModelAndView("success");
	}

	@Resource(name = "loginService")  // ��ȡapplicationContext.xml��bean��idΪloginService�ģ���ע��
	private LoginService loginService;  //�ȼ���spring��ͳע�뷽ʽдget��set�����������ĺô��Ǽ�๤����ʡȥ�˲���Ҫ�ô���

	@RequestMapping("/login4.do")
	public String testLogin4(User user) {
		if (loginService.login(user) == false) {
			return "error";
		}
		return "success";
	}
 }
