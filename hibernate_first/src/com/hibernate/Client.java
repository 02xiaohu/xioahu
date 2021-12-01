package com.hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.Date;
public class Client {

	 public static void main(String[] args) {
	    //��ȡhibernate.cfg.xml�ļ�
		 Configuration cfg = new Configuration().configure(); 
        //����SessionFactory����
		 SessionFactory factory=cfg.buildSessionFactory();
	 
		 Session session = null;
		 try { 
			 session=factory.openSession();//����Session
		   //��������
		   session.beginTransaction();
		   User user=new User();
		   user.setName("wangwu");
		   user.setPassword("789");
		   user.setCreateTime(new Date());
		   user.setExpireTime(new Date());
		   //��������
		   session.save(user);
		   //�ύ����
		   session.getTransaction().commit();
		 } catch(Exception e) {
		  e.printStackTrace();
		  //�ع�����
		  session.getTransaction().rollback();
		 }finally{
			 if (session != null) {
					if (session.isOpen()) {
						session.close();
					}
				} 
		 }
		 
	 }

}
