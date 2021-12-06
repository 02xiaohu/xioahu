package com.zte.action;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.zte.model.User;
import com.zte.service.LoginService;

//import entity.User;

@Controller  //ÀàËÆStrutsµÄAction
public class TestController {
	@Resource(name = "loginService") 
	private LoginService loginService;   

	@RequestMapping("/login.do")
	public String regLogin(User user) {
 	System.out.println("HelloController.handleRequest()");
	loginService.add(user); 
	 return "success";
	}

//	public LoginService getLoginService() {
//		return loginService;
//	}
//
//	public void setLoginService(LoginService loginService) {
//		this.loginService = loginService;
//	}
 }
