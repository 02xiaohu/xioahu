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
	// ����һ��ServletContext����
	private ServletContext application = null ;
    public void sessionCreated(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	
    public void sessionDestroyed(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    	// ���û����ƴ��б���ɾ��
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

		// �����½�ɹ������û����������б�֮��
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
		// ������ʼ��ʱ����application�д��һ���յ�����
		this.application = arg0.getServletContext() ;
		this.application.setAttribute("alluser",new HashSet()) ;//�����û�
	    this.application.setAttribute("allmap",new HashMap()) ;//˽����Ϣ
	    this.application.setAttribute("PUBLIC_MSG",new HashMap()) ;//������Ϣ
	    Map m = (Map)this.application.getAttribute("PUBLIC_MSG");
	   m.put("public",new ArrayList());
	  
	  
	    
    }
	
}
