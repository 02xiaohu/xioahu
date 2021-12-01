
 package com.bjsxt.drp.business.util;

 import org.hibernate.Session;
 import com.bjsxt.drp.business.itemmgr.model.ItemCategory;
 import com.bjsxt.drp.business.itemmgr.model.ItemUnit;

 public class InitData {

	public static void main(String[] args) {
		Session session = null; 
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			ItemCategory ic1 = new ItemCategory();
			ic1.setId("B01");
			ic1.setName("yiliaoqixie");
			session.save(ic1);
			
			ItemCategory ic2 = new ItemCategory();
			ic2.setId("B02");
			ic2.setName("zhongchengyao");
			session.save(ic2);
			
			ItemCategory ic3 = new ItemCategory();
			ic3.setId("B03");
			ic3.setName("xiyao");
			session.save(ic3);
			
			ItemUnit iu1 = new ItemUnit();
			iu1.setId("C01");
			iu1.setName("he");
			session.save(iu1);
			
			ItemUnit iu2 = new ItemUnit();
			iu2.setId("C02");
			iu2.setName("pian");
			session.save(iu2);

			ItemUnit iu3 = new ItemUnit();
			iu3.setId("C03");
			iu3.setName("xiang");
			session.save(iu3);
			
			session.getTransaction().commit();
		}catch(Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally {
			HibernateUtils.closeSession(session);
		}

	}

 }
