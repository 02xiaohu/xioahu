
 package com.asm;

 import org.springframework.web.servlet.ModelAndView;

 public class UserManager {
	ModelAndView mav;
	
	public UserManager() {}
	
	public ModelAndView login (String username,String password)throws Exception
	{
	  if ("admin".equals(username)&&"admin".equals(password)){
	    mav= new ModelAndView("success");
		mav.addObject("name", username);
		}
	  else if (!"admin".equals(username)){
		  throw new UserNotFoundException(username);
	  }
	  else { 
	    throw new Exception("√‹¬Î:"+password+"¥ÌŒÛ"); } 
	  
	  return mav;	 
	}
 }