package com.asm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class MyInterceptor1 implements HandlerInterceptor {

	 
	public void afterCompletion(HttpServletRequest request,	HttpServletResponse response, Object handler, Exception ex)	throws Exception {
		System.out.println("最后执行！！！一般用于释放资源！！");
		
	}

	 
	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,	ModelAndView modelAndView) throws Exception {
		System.out.println("Action执行之后，生成视图之前执行！！");
	}

	 
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		System.out.println("action之前执行！！！");
		return true;  //继续执行action
	}

}
