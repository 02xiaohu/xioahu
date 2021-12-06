
 package com.asm;
 
 import  org.springframework.web.servlet.mvc.Controller;
 import  org.springframework.web.servlet.ModelAndView;
 import  javax.servlet.ServletException;
 import  javax.servlet.http.HttpServletRequest;
 import  javax.servlet.http.HttpServletResponse;

public   class  spmvclogin  implements  Controller   {

	        private String username;
	        private String password;
	      
		   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)throws Exception {  
			   username=(String)request.getParameter("username");
			   password=(String)request.getParameter("password"); 
			    
			   return(new UserManager().login(username, password));
			    
   }

}		 
 
