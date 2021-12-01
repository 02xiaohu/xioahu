package com.bjsxt.drp.business.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * π‹¿Ìsession
 * @author Administrator
 *
 */
public class HibernateFilter implements Filter {
	
	private static ThreadLocal hibernateHolder = new ThreadLocal();
	
	private static SessionFactory factory = null; 
	
	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		try {
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			Session session = (Session)hibernateHolder.get();
			if (session != null) {
				if (session.isOpen()) {
					session.close();
				}
				hibernateHolder.remove();
			}
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			Configuration cfg = new Configuration().configure();
			factory = cfg.buildSessionFactory();
		}catch(Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}	
	}
	
	public static Session getSession() {
		Session session = (Session)hibernateHolder.get();
		if (session == null) {
			session = factory.openSession();
			hibernateHolder.set(session);
		}
		return session;
	}
}
