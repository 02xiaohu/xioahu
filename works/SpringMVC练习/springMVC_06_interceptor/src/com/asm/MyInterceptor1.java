package com.asm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class MyInterceptor1 implements HandlerInterceptor {

	 
	public void afterCompletion(HttpServletRequest request,	HttpServletResponse response, Object handler, Exception ex)	throws Exception {
		System.out.println("���ִ�У�����һ�������ͷ���Դ����");
		
	}

	 
	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,	ModelAndView modelAndView) throws Exception {
		System.out.println("Actionִ��֮��������ͼ֮ǰִ�У���");
	}

	 
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		System.out.println("action֮ǰִ�У�����");
		return true;  //����ִ��action
	}

}
