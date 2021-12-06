package com.asm;

import  org.springframework.web.servlet.mvc.Controller;
import  org.springframework.web.servlet.ModelAndView;
import  javax.servlet.ServletException;
import  javax.servlet.http.HttpServletRequest;
import  javax.servlet.http.HttpServletResponse;

 public   class  HelloWord  implements  Controller   {

    /**  Logger for this class and subclasses  */ 
	   
		   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)throws Exception {  
      
			   ModelAndView mav = new ModelAndView("hello.jsp");
			   mav.addObject("message", "Hello World!");            
			   return mav; 
   } 
} 
