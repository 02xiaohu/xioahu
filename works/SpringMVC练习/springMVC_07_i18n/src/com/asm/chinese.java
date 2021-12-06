
 package com.asm;

 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.springframework.web.servlet.ModelAndView;
 import org.springframework.web.servlet.mvc.Controller;
 import org.springframework.web.servlet.view.RedirectView;

 public class chinese implements  Controller {
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)throws Exception {  
		 
		    
		   
		return new ModelAndView(new RedirectView("./index.jsp"));  // 采用重定向方式跳转页面
		   } 
 }
 
