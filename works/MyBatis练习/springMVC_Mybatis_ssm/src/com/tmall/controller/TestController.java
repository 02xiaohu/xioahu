package com.tmall.controller;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.tmall.bean.User;
import com.tmall.inter.OperationMapper;
import com.tmall.service.LoginService;
 

 

@Controller  //ÀàËÆStrutsµÄAction
 

public class TestController {
	@Resource(name = "loginService") 
	private LoginService loginService; 
	 
	@RequestMapping("/login.do")
	public String regLogin(User user) {
	  
	 
	loginService.insert(user); 
	return "success";
	}
	
	@RequestMapping("/find.do")
	public String findById(int id) 
	{
		loginService.findUserById(id);
		return "success";
	}
	
	@RequestMapping("/alluse.do")
	public String findAllUse(HttpServletRequest request) 
	{
		List alluse=loginService.findAllUse();
		request.setAttribute("all",alluse);

		return "alluse";
	}
	
	@RequestMapping("/delUser.do")
	public String delUser(int id) 
	{ 
	   loginService.delUser(id);
		return "delUser";
	}
	@RequestMapping("/findUserById.do")
	public String findUserById(HttpServletRequest request,int id) 
	{ 
	      User user=loginService.findUserById(id);
	      request.setAttribute("user",user);
		return "updateUse";
	}

	@RequestMapping("/updateUser.do")
	public String updateUser(User user) {
		 
	    loginService.updateUse(user); 
	return "updateUserSuccess";
	}

}
