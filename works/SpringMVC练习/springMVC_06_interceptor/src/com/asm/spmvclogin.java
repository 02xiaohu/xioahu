package com.asm;
import  org.springframework.web.servlet.mvc.Controller;
import  org.springframework.web.servlet.ModelAndView;
import  javax.servlet.ServletException;
import  javax.servlet.http.HttpServletRequest;
import  javax.servlet.http.HttpServletResponse;

public   class  spmvclogin  implements  Controller   {

	        private String username;
	        private String password;
	        private boolean bl;
		   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)throws Exception {  
			   System.out.println("Ö´ÐÐaction");
			   username=(String)request.getParameter("username");
			   password=(String)request.getParameter("password"); 
			    
			    bl=new UserManager().login(username, password);
			   if (bl==true)
			   {ModelAndView mav1 = new ModelAndView("success");
			   mav1.addObject("name", username);
			   return mav1;
			   }
			   else
			   {ModelAndView mav2= new ModelAndView("failure");  
			   mav2.addObject("name", username);
			   mav2.addObject("pass", password); 
			   return mav2;
			   } 
   }

		 
} 
