package com.mybatis;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

 

public class MyBatisUtils {

	private static SqlSessionFactory factory;
	
	static {
		try {
			Reader reader= Resources.getResourceAsReader("Configuration.xml");
        	SqlSessionFactoryBuilder sfb=new SqlSessionFactoryBuilder();
            //´´½¨SessionFactory 
        	factory =sfb.build(reader);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static SqlSessionFactory getSessionFactory() {
		return factory;
	}
	
	public static SqlSession getSession() {
		return factory.openSession();
	}
	
	public static void closeSession(SqlSession session) {
		 
				session.close();
		 	 
		 
	}
}
