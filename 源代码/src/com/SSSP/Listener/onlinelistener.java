package com.SSSP.Listener;
import java.util.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.ServletContext;
import com.SSSP.bean.*;
@WebListener
public class onlinelistener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {
	// 声明一个ServletContext对象
	private ServletContext application = null ;
    public void sessionCreated(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	
    public void sessionDestroyed(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    	// 将用户名称从列表中删除
    			Set s = (Set)this.application.getAttribute("alluser") ;
    			UserBean user=(UserBean)arg0.getSession().getAttribute("UserBean") ;
    			s.remove(user) ;
    			this.application.setAttribute("alluser",s) ;
    		    Map m = (Map)this.application.getAttribute("allmap") ;
    		    m.remove(user) ;
    		    this.application.setAttribute("allmap",m);
    }

	
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	
    public void attributeAdded(HttpSessionBindingEvent arg0)  { 
         // TODO Auto-generated method stub

		// 如果登陆成功，则将用户名保存在列表之中
		Set s = (Set)this.application.getAttribute("alluser") ;
		s.add(arg0.getValue()) ;
		this.application.setAttribute("alluser",s);
		Map m = (Map)this.application.getAttribute("allmap");
		m.put(arg0.getValue(),new ArrayList());
		this.application.setAttribute("allmap",m);
    }

	
    public void attributeRemoved(HttpSessionBindingEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	
    public void attributeReplaced(HttpSessionBindingEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	
    public void contextInitialized(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
		// 容器初始化时，向application中存放一个空的容器
		this.application = arg0.getServletContext() ;
		this.application.setAttribute("alluser",new HashSet()) ;//在线用户
	    this.application.setAttribute("allmap",new HashMap()) ;//私聊信息
	    this.application.setAttribute("PUBLIC_MSG",new HashMap()) ;//公聊信息
	    Map m = (Map)this.application.getAttribute("PUBLIC_MSG");
	   m.put("public",new ArrayList());
	  
	  
	    
    }
	
}
