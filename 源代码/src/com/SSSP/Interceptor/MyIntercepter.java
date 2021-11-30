package com.SSSP.Interceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.SSSP.bean.*;
import javax.servlet.http.*;
public class MyIntercepter extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		HttpSession session=request.getSession();
		String url=request.getRequestURI();
		String[]attr= {"/Login.do","/register.do","/Login_error","/register_error","/back.do"};
		for(int i=0;i<attr.length;i++) {
			if(url.indexOf(attr[i])!=-1) {
				return true;
			}
		}
	if(session.getAttribute("UserBean")==null) {
		response.sendRedirect("Hello.jsp");
	}
	return true;
}
}
