
 package com.asm;

 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.springframework.web.servlet.ModelAndView;
 import org.springframework.web.servlet.mvc.Controller;
 import org.springframework.web.servlet.view.RedirectView;

 public class spmvclongin1 implements  Controller {
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)throws Exception {  
		 
		    
		   
		return new ModelAndView("login");   
		   } 
 }
 
