package com.asm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class MyInterceptor2 extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		System.out.println("MyInterceptor2.preHandle()");
		return true;  //¼ÌÐøÖ´ÐÐaction
	}

}
