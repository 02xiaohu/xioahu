package com.tmall.action;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.tmall.bean.Ibatis;
import com.tmall.service.LoginService;
 

//import entity.User;

@Controller  //ÀàËÆStrutsµÄAction
public class TestController {
	@Resource(name = "loginService") 
	private LoginService loginService;   

	@RequestMapping("/login.do")
	public String regLogin(Ibatis user) {
	 ;
	System.out.println(user.getUsername());
	System.out.println(user.getPassword());
 	System.out.println("HelloController.handleRequest()");
	loginService.add(user); 
	 return "success";
	}

 }
