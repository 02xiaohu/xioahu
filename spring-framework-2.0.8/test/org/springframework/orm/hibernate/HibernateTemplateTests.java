/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.orm.hibernate;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import net.sf.hibernate.Criteria;
import net.sf.hibernate.FlushMode;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Interceptor;
import net.sf.hibernate.JDBCException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.ObjectDeletedException;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.PersistentObjectException;
import net.sf.hibernate.Query;
import net.sf.hibernate.QueryException;
import net.sf.hibernate.ReplicationMode;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.StaleObjectStateException;
import net.sf.hibernate.TransientObjectException;
import net.sf.hibernate.UnresolvableObjectException;
import net.sf.hibernate.WrongClassException;
import net.sf.hibernate.type.Type;
import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;

import org.springframework.beans.TestBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author Juergen Hoeller
 * @since 06.05.2003
 */
public class HibernateTemplateTests extends TestCase {

	private MockControl sfControl;
	private SessionFactory sf;
	private MockControl sessionControl;
	private Session session;

	protected void setUp() {
		sfControl = MockControl.createControl(SessionFactory.class);
		sf = (SessionFactory) sfControl.getMock();
		sessionControl = MockControl.createControl(Session.class);
		session = (Session) sessionControl.getMock();
	}

	public void testExecuteWithNewSession() throws HibernateException {
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		assertTrue("Correct allowCreate default", ht.isAllowCreate());
		assertTrue("Correct flushMode default", ht.getFlushMode() == HibernateTemplate.FLUSH_AUTO);
		final List l = new ArrayList();
		l.add("test");
		List result = ht.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				return l;
			}
		});
		assertTrue("Correct result list", result == l);
	}

	public void testExecuteWithNewSessionAndFlushNever() throws HibernateException {
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.setFlushMode(FlushMode.NEVER);
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setFlushMode(HibernateTemplate.FLUSH_NEVER);
		final List l = new ArrayList();
		l.add("test");
		List result = ht.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				return l;
			}
		});
		assertTrue("Correct result list", result == l);
	}

	public void testExecuteWithNewSessionAndFlushCommit() throws HibernateException {
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.setFlushMode(FlushMode.COMMIT);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setFlushMode(HibernateTemplate.FLUSH_COMMIT);
		final List l = new ArrayList();
		l.add("test");
		List result = ht.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				return l;
			}
		});
		assertTrue("Correct result list", result == l);
	}

	public void testExecuteWithNotAllowCreate() {
		HibernateTemplate ht = new HibernateTemplate();
		ht.setSessionFactory(sf);
		ht.setAllowCreate(false);
		try {
			ht.execute(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					return null;
				}
			});
			fail("Should have thrown IllegalStateException");
		}
		catch (IllegalStateException ex) {
			// expected
		}
	}

	public void testExecuteWithNotAllowCreateAndThreadBound() {
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.isOpen();
		sessionControl.setReturnValue(true, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setAllowCreate(false);

		TransactionSynchronizationManager.bindResource(sf, new SessionHolder(session));
		try {
			final List l = new ArrayList();
			l.add("test");
			List result = ht.executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					return l;
				}
			});
			assertTrue("Correct result list", result == l);
		}
		finally {
			TransactionSynchronizationManager.unbindResource(sf);
		}
	}

	public void testExecuteWithThreadBoundAndFlushCommit() {
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.isOpen();
		sessionControl.setReturnValue(true, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.setFlushMode(FlushMode.COMMIT);
		sessionControl.setVoidCallable(1);
		session.setFlushMode(FlushMode.AUTO);
		sessionControl.setVoidCallable(1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setAllowCreate(false);
		ht.setFlushMode(HibernateTemplate.FLUSH_COMMIT);

		TransactionSynchronizationManager.bindResource(sf, new SessionHolder(session));
		try {
			final List l = new ArrayList();
			l.add("test");
			List result = ht.executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					return l;
				}
			});
			assertTrue("Correct result list", result == l);
		}
		finally {
			TransactionSynchronizationManager.unbindResource(sf);
		}
	}

	public void testExecuteWithThreadBoundAndFlushEager() throws HibernateException {
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.isOpen();
		sessionControl.setReturnValue(true, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.NEVER);
		session.setFlushMode(FlushMode.AUTO);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.setFlushMode(FlushMode.NEVER);
		sessionControl.setVoidCallable(1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setFlushModeName("FLUSH_EAGER");
		ht.setAllowCreate(false);

		TransactionSynchronizationManager.bindResource(sf, new SessionHolder(session));
		try {
			final List l = new ArrayList();
			l.add("test");
			List result = ht.executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					return l;
				}
			});
			assertTrue("Correct result list", result == l);
		}
		finally {
			TransactionSynchronizationManager.unbindResource(sf);
		}
	}

	public void testExecuteWithThreadBoundAndNewSession() throws HibernateException {
		MockControl conControl = MockControl.createControl(Connection.class);
		Connection con = (Connection) conControl.getMock();
		MockControl session2Control = MockControl.createControl(Session.class);
		Session session2 = (Session) session2Control.getMock();

		session2.connection();
		session2Control.setReturnValue(con, 1);
		sf.openSession(con);
		sfControl.setReturnValue(session, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		session2Control.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setAlwaysUseNewSession(true);

		TransactionSynchronizationManager.bindResource(sf, new SessionHolder(session2));
		try {
			final List l = new ArrayList();
			l.add("test");
			List result = ht.executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					return l;
				}
			});
			assertTrue("Correct result list", result == l);
		}
		finally {
			TransactionSynchronizationManager.unbindResource(sf);
		}
	}

	public void testExecuteWithThreadBoundAndNewSessionAndEntityInterceptor() throws HibernateException {
		MockControl interceptorControl = MockControl.createControl(net.sf.hibernate.Interceptor.class);
		Interceptor entityInterceptor = (Interceptor) interceptorControl.getMock();

		MockControl conControl = MockControl.createControl(Connection.class);
		Connection con = (Connection) conControl.getMock();
		MockControl session2Control = MockControl.createControl(Session.class);
		Session session2 = (Session) session2Control.getMock();

		session2.connection();
		session2Control.setReturnValue(con, 1);
		sf.openSession(con, entityInterceptor);
		sfControl.setReturnValue(session, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		session2Control.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setAlwaysUseNewSession(true);
		ht.setEntityInterceptor(entityInterceptor);

		TransactionSynchronizationManager.bindResource(sf, new SessionHolder(session2));
		try {
			final List l = new ArrayList();
			l.add("test");
			List result = ht.executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					return l;
				}
			});
			assertTrue("Correct result list", result == l);
		}
		finally {
			TransactionSynchronizationManager.unbindResource(sf);
		}
	}

	public void testExecuteWithEntityInterceptor() throws HibernateException {
		MockControl interceptorControl = MockControl.createControl(net.sf.hibernate.Interceptor.class);
		Interceptor entityInterceptor = (Interceptor) interceptorControl.getMock();

		sf.openSession(entityInterceptor);
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setEntityInterceptor(entityInterceptor);
		final List l = new ArrayList();
		l.add("test");
		List result = ht.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				return l;
			}
		});
		assertTrue("Correct result list", result == l);
	}

	public void testExecuteWithCacheQueries() throws HibernateException {
		MockControl query1Control = MockControl.createControl(Query.class);
		Query query1 = (Query) query1Control.getMock();
		MockControl query2Control = MockControl.createControl(Query.class);
		Query query2 = (Query) query2Control.getMock();
		MockControl criteriaControl = MockControl.createControl(Criteria.class);
		Criteria criteria = (Criteria) criteriaControl.getMock();

		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query");
		sessionControl.setReturnValue(query1);
		query1.setCacheable(true);
		query1Control.setReturnValue(query1, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query2);
		query2.setCacheable(true);
		query2Control.setReturnValue(query2, 1);
		session.createCriteria(TestBean.class);
		sessionControl.setReturnValue(criteria, 1);
		criteria.setCacheable(true);
		criteriaControl.setReturnValue(criteria, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		query1Control.replay();
		query2Control.replay();
		criteriaControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setCacheQueries(true);
		ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session sess) throws HibernateException {
				assertNotSame(session, sess);
				assertTrue(Proxy.isProxyClass(sess.getClass()));
				sess.createQuery("some query");
				sess.getNamedQuery("some query name");
				sess.createCriteria(TestBean.class);
				// should be ignored
				sess.close();
				return null;
			}
		});

		query1Control.verify();
		query2Control.verify();
		criteriaControl.verify();
	}

	public void testExecuteWithCacheQueriesAndCacheRegion() throws HibernateException {
		MockControl query1Control = MockControl.createControl(Query.class);
		Query query1 = (Query) query1Control.getMock();
		MockControl query2Control = MockControl.createControl(Query.class);
		Query query2 = (Query) query2Control.getMock();
		MockControl criteriaControl = MockControl.createControl(Criteria.class);
		Criteria criteria = (Criteria) criteriaControl.getMock();

		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query");
		sessionControl.setReturnValue(query1);
		query1.setCacheable(true);
		query1Control.setReturnValue(query1, 1);
		query1.setCacheRegion("myRegion");
		query1Control.setReturnValue(query1, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query2);
		query2.setCacheable(true);
		query2Control.setReturnValue(query2, 1);
		query2.setCacheRegion("myRegion");
		query2Control.setReturnValue(query2, 1);
		session.createCriteria(TestBean.class);
		sessionControl.setReturnValue(criteria, 1);
		criteria.setCacheable(true);
		criteriaControl.setReturnValue(criteria, 1);
		criteria.setCacheRegion("myRegion");
		criteriaControl.setReturnValue(criteria, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		query1Control.replay();
		query2Control.replay();
		criteriaControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setCacheQueries(true);
		ht.setQueryCacheRegion("myRegion");
		ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session sess) throws HibernateException {
				assertNotSame(session, sess);
				assertTrue(Proxy.isProxyClass(sess.getClass()));
				sess.createQuery("some query");
				sess.getNamedQuery("some query name");
				sess.createCriteria(TestBean.class);
				// should be ignored
				sess.close();
				return null;
			}
		});

		query1Control.verify();
		query2Control.verify();
		criteriaControl.verify();
	}

	public void testExecuteWithCacheQueriesAndCacheRegionAndNativeSession() throws HibernateException {
		MockControl query1Control = MockControl.createControl(Query.class);
		Query query1 = (Query) query1Control.getMock();
		MockControl query2Control = MockControl.createControl(Query.class);
		Query query2 = (Query) query2Control.getMock();
		MockControl criteriaControl = MockControl.createControl(Criteria.class);
		Criteria criteria = (Criteria) criteriaControl.getMock();

		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query");
		sessionControl.setReturnValue(query1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query2);
		session.createCriteria(TestBean.class);
		sessionControl.setReturnValue(criteria, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		query1Control.replay();
		query2Control.replay();
		criteriaControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setExposeNativeSession(true);
		ht.setCacheQueries(true);
		ht.setQueryCacheRegion("myRegion");
		ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session sess) throws HibernateException {
				assertSame(session, sess);
				sess.createQuery("some query");
				sess.getNamedQuery("some query name");
				sess.createCriteria(TestBean.class);
				return null;
			}
		});

		query1Control.verify();
		query2Control.verify();
		criteriaControl.verify();
	}

	public void testExecuteWithFetchSizeAndMaxResults() throws HibernateException {
		MockControl query1Control = MockControl.createControl(Query.class);
		Query query1 = (Query) query1Control.getMock();
		MockControl query2Control = MockControl.createControl(Query.class);
		Query query2 = (Query) query2Control.getMock();
		MockControl criteriaControl = MockControl.createControl(Criteria.class);
		Criteria criteria = (Criteria) criteriaControl.getMock();

		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query");
		sessionControl.setReturnValue(query1);
		query1.setFetchSize(10);
		query1Control.setReturnValue(query1, 1);
		query1.setMaxResults(20);
		query1Control.setReturnValue(query1, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query2);
		query2.setFetchSize(10);
		query2Control.setReturnValue(query2, 1);
		query2.setMaxResults(20);
		query2Control.setReturnValue(query2, 1);
		session.createCriteria(TestBean.class);
		sessionControl.setReturnValue(criteria, 1);
		criteria.setFetchSize(10);
		criteriaControl.setReturnValue(criteria, 1);
		criteria.setMaxResults(20);
		criteriaControl.setReturnValue(criteria, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		query1Control.replay();
		query2Control.replay();
		criteriaControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setFetchSize(10);
		ht.setMaxResults(20);
		ht.execute(new HibernateCallback() {
			public Object doInHibernate(Session sess) throws HibernateException {
				sess.createQuery("some query");
				sess.getNamedQuery("some query name");
				sess.createCriteria(TestBean.class);
				return null;
			}
		});

		query1Control.verify();
		query2Control.verify();
		criteriaControl.verify();
	}

	public void testGet() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.get(TestBean.class, "");
		sessionControl.setReturnValue(tb, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		Object result = ht.get(TestBean.class, "");
		assertTrue("Correct result", result == tb);
	}

	public void testGetWithLockMode() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.get(TestBean.class, "", LockMode.UPGRADE_NOWAIT);
		sessionControl.setReturnValue(tb, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		Object result = ht.get(TestBean.class, "", LockMode.UPGRADE_NOWAIT);
		assertTrue("Correct result", result == tb);
	}

	public void testLoad() throws HibernateException {
		TestBean tb = new TestBean();

		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.load(TestBean.class, "");
		sessionControl.setReturnValue(tb, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		Object result = ht.load(TestBean.class, "");
		assertTrue("Correct result", result == tb);
	}

	public void testLoadWithNotFound() throws HibernateException {
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.load(TestBean.class, "id");
		ObjectNotFoundException onfex = new ObjectNotFoundException("id", TestBean.class);
		sessionControl.setThrowable(onfex);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		try {
			ht.load(TestBean.class, "id");
			fail("Should have thrown HibernateObjectRetrievalFailureException");
		}
		catch (HibernateObjectRetrievalFailureException ex) {
			// expected
			assertEquals(TestBean.class, ex.getPersistentClass());
			assertEquals("id", ex.getIdentifier());
			assertEquals(onfex, ex.getCause());
		}
	}

	public void testLoadWithLockMode() throws HibernateException {
		TestBean tb = new TestBean();

		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.load(TestBean.class, "", LockMode.UPGRADE);
		sessionControl.setReturnValue(tb, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		Object result = ht.load(TestBean.class, "", LockMode.UPGRADE);
		assertTrue("Correct result", result == tb);
	}

	public void testLoadWithObject() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.load(tb, "");
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.load(tb, "");
	}

	public void testLoadAll() throws HibernateException {
		MockControl criteriaControl = MockControl.createControl(Criteria.class);
		Criteria criteria = (Criteria) criteriaControl.getMock();
		List list = new ArrayList();

		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createCriteria(TestBean.class);
		sessionControl.setReturnValue(criteria, 1);
		criteria.list();
		criteriaControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		criteriaControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.loadAll(TestBean.class);
		assertTrue("Correct result", result == list);

		criteriaControl.verify();
	}

	public void testLoadAllWithCacheable() throws HibernateException {
		MockControl criteriaControl = MockControl.createControl(Criteria.class);
		Criteria criteria = (Criteria) criteriaControl.getMock();
		List list = new ArrayList();

		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createCriteria(TestBean.class);
		sessionControl.setReturnValue(criteria, 1);
		criteria.setCacheable(true);
		criteriaControl.setReturnValue(criteria, 1);
		criteria.list();
		criteriaControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		criteriaControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setCacheQueries(true);
		List result = ht.loadAll(TestBean.class);
		assertTrue("Correct result", result == list);

		criteriaControl.verify();
	}

	public void testLoadAllWithCacheableAndCacheRegion() throws HibernateException {
		MockControl criteriaControl = MockControl.createControl(Criteria.class);
		Criteria criteria = (Criteria) criteriaControl.getMock();
		List list = new ArrayList();

		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createCriteria(TestBean.class);
		sessionControl.setReturnValue(criteria, 1);
		criteria.setCacheable(true);
		criteriaControl.setReturnValue(criteria, 1);
		criteria.setCacheRegion("myCacheRegion");
		criteriaControl.setReturnValue(criteria, 1);
		criteria.list();
		criteriaControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		criteriaControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setCacheQueries(true);
		ht.setQueryCacheRegion("myCacheRegion");
		List result = ht.loadAll(TestBean.class);
		assertTrue("Correct result", result == list);

		criteriaControl.verify();
	}

	public void testRefresh() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.refresh(tb);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.refresh(tb);
	}

	public void testContains() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.contains(tb);
		sessionControl.setReturnValue(true, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		assertTrue(ht.contains(tb));
	}

	public void testEvict() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.evict(tb);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.evict(tb);
	}

	public void testLock() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.lock(tb, LockMode.WRITE);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
			sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.lock(tb, LockMode.WRITE);
	}

	public void testSave() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.save(tb);
		sessionControl.setReturnValue(new Integer(0), 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		assertEquals("Correct return value", ht.save(tb), new Integer(0));
	}

	public void testSaveWithId() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.save(tb, "id");
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.save(tb, "id");
	}

	public void testUpdate() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.update(tb);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.update(tb);
	}

	public void testUpdateWithLockMode() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.update(tb);
		sessionControl.setVoidCallable(1);
		session.lock(tb, LockMode.UPGRADE);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.update(tb, LockMode.UPGRADE);
	}

	public void testSaveOrUpdate() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.saveOrUpdate(tb);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.saveOrUpdate(tb);
	}

	public void testSaveOrUpdateWithFlushModeNever() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.NEVER);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		try {
			ht.saveOrUpdate(tb);
			fail("Should have thrown InvalidDataAccessApiUsageException");
		}
		catch (InvalidDataAccessApiUsageException ex) {
			// expected
		}
	}

	public void testSaveOrUpdateAll() throws HibernateException {
		TestBean tb1 = new TestBean();
		TestBean tb2 = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.saveOrUpdate(tb1);
		sessionControl.setVoidCallable(1);
		session.saveOrUpdate(tb2);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List tbs = new ArrayList();
		tbs.add(tb1);
		tbs.add(tb2);
		ht.saveOrUpdateAll(tbs);
	}

	public void testSaveOrUpdateCopy() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.saveOrUpdateCopy(tb);
		sessionControl.setReturnValue(tb, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.saveOrUpdateCopy(tb);
	}

	public void testReplicate() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.replicate(tb, ReplicationMode.LATEST_VERSION);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.replicate(tb, ReplicationMode.LATEST_VERSION);
	}

	public void testDelete() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.delete(tb);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.delete(tb);
	}

	public void testDeleteWithLock() throws HibernateException {
		TestBean tb = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.lock(tb, LockMode.UPGRADE);
		sessionControl.setVoidCallable(1);
		session.delete(tb);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.delete(tb, LockMode.UPGRADE);
	}

	public void testDeleteAll() throws HibernateException {
		TestBean tb1 = new TestBean();
		TestBean tb2 = new TestBean();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.delete(tb1);
		sessionControl.setVoidCallable(1);
		session.delete(tb2);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List tbs = new ArrayList();
		tbs.add(tb1);
		tbs.add(tb2);
		ht.deleteAll(tbs);
	}

	public void testFlush() throws HibernateException {
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.setFlushMode(FlushMode.NEVER);
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setFlushMode(HibernateTemplate.FLUSH_NEVER);
		ht.flush();
	}

	public void testClear() throws HibernateException {
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.clear();
		sessionControl.setVoidCallable(1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.clear();
	}

	public void testFind() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.find("some query string");
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindWithParameter() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setParameter(0, "myvalue");
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.find("some query string", "myvalue");
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindWithParameterAndType() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setParameter(0, "myvalue", Hibernate.STRING);
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.find("some query string", "myvalue", Hibernate.STRING);
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindWithParameters() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setParameter(0, "myvalue1");
		queryControl.setReturnValue(query, 1);
		query.setParameter(1, new Integer(2));
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.find("some query string", new Object[] {"myvalue1", new Integer(2)});
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindWithParametersAndTypes() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setParameter(0, "myvalue1", Hibernate.STRING);
		queryControl.setReturnValue(query, 1);
		query.setParameter(1, new Integer(2), Hibernate.INTEGER);
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.find("some query string",
				new Object[] {"myvalue1", new Integer(2)},
				new Type[] {Hibernate.STRING, Hibernate.INTEGER});
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindWithParametersAndTypesForInvalidArguments() {
		HibernateTemplate ht = new HibernateTemplate();
		try {
			ht.find("some query string",
							new Object[] {"myvalue1", new Integer(2)},
							new Type[] {Hibernate.STRING});
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
	}

	public void testFindWithNamedParameter() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setParameter("myparam", "myvalue");
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedParam("some query string", "myparam", "myvalue");
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindWithNamedParameterAndType() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setParameter("myparam", "myvalue", Hibernate.STRING);
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedParam("some query string", "myparam", "myvalue", Hibernate.STRING);
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindWithNamedParameters() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setParameter("myparam1", "myvalue1");
		queryControl.setReturnValue(query, 1);
		query.setParameter("myparam2", new Integer(2));
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedParam("some query string",
				new String[] {"myparam1", "myparam2"},
				new Object[] {"myvalue1", new Integer(2)});
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindWithNamedParametersAndTypes() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setParameter("myparam1", "myvalue1", Hibernate.STRING);
		queryControl.setReturnValue(query, 1);
		query.setParameter("myparam2", new Integer(2), Hibernate.INTEGER);
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedParam("some query string",
				new String[] {"myparam1", "myparam2"},
				new Object[] {"myvalue1", new Integer(2)},
				new Type[] {Hibernate.STRING, Hibernate.INTEGER});
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindByValueBean() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		TestBean tb = new TestBean();
		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setProperties(tb);
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByValueBean("some query string", tb);
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindByNamedQuery() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedQuery("some query name");
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindByNamedQueryWithParameter() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query, 1);
		query.setParameter(0, "myvalue");
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedQuery("some query name", "myvalue");
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindByNamedQueryWithParameterAndType() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query, 1);
		query.setParameter(0, "myvalue", Hibernate.STRING);
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedQuery("some query name", (Object) "myvalue", Hibernate.STRING);
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindByNamedQueryWithParameters() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query, 1);
		query.setParameter(0, "myvalue1");
		queryControl.setReturnValue(query, 1);
		query.setParameter(1, new Integer(2));
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedQuery("some query name", new Object[] {"myvalue1", new Integer(2)});
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindByNamedQueryWithParametersAndTypes() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query, 1);
		query.setParameter(0, "myvalue1", Hibernate.STRING);
		queryControl.setReturnValue(query, 1);
		query.setParameter(1, new Integer(2), Hibernate.INTEGER);
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedQuery("some query name",
																			new Object[] {"myvalue1", new Integer(2)},
																			new Type[] {Hibernate.STRING, Hibernate.INTEGER});
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindByNamedQueryWithParametersAndTypesForInvalidArguments() {
		HibernateTemplate ht = new HibernateTemplate();
		try {
			ht.findByNamedQuery("some query string",
													new Object[] {"myvalue1", "myValue2"},
													new Type[] {Hibernate.STRING});
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
	}

	public void testFindByNamedQueryWithNamedParameter() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query, 1);
		query.setParameter("myparam", "myvalue");
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedQueryAndNamedParam("some query name", "myparam", "myvalue");
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindByNamedQueryWithNamedParameterAndType() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query, 1);
		query.setParameter("myparam", "myvalue", Hibernate.STRING);
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedQueryAndNamedParam("some query name", "myparam", "myvalue", Hibernate.STRING);
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindByNamedQueryWithNamedParameters() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query, 1);
		query.setParameter("myparam1", "myvalue1");
		queryControl.setReturnValue(query, 1);
		query.setParameter("myparam2", new Integer(2));
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedQueryAndNamedParam("some query name",
				new String[] {"myparam1", "myparam2"},
				new Object[] {"myvalue1", new Integer(2)});
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindByNamedQueryWithNamedParametersAndTypes() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query, 1);
		query.setParameter("myparam1", "myvalue1", Hibernate.STRING);
		queryControl.setReturnValue(query, 1);
		query.setParameter("myparam2", new Integer(2), Hibernate.INTEGER);
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedQueryAndNamedParam("some query name",
				new String[] {"myparam1", "myparam2"},
				new Object[] {"myvalue1", new Integer(2)},
				new Type[] {Hibernate.STRING, Hibernate.INTEGER});
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindByNamedQueryAndValueBean() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		TestBean tb = new TestBean();
		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query, 1);
		query.setProperties(tb);
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		List result = ht.findByNamedQueryAndValueBean("some query name", tb);
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindWithCacheable() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setCacheable(true);
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setCacheQueries(true);
		List result = ht.find("some query string");
		assertTrue("Correct list", result == list);
		sfControl.verify();
	}

	public void testFindWithCacheableAndCacheRegion() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setCacheable(true);
		queryControl.setReturnValue(query, 1);
		query.setCacheRegion("myCacheRegion");
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setCacheQueries(true);
		ht.setQueryCacheRegion("myCacheRegion");
		List result = ht.find("some query string");
		assertTrue("Correct list", result == list);
		sfControl.verify();
	}

	public void testFindByNamedQueryWithCacheable() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query, 1);
		query.setCacheable(true);
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setCacheQueries(true);
		List result = ht.findByNamedQuery("some query name");
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testFindByNamedQueryWithCacheableAndCacheRegion() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		List list = new ArrayList();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getNamedQuery("some query name");
		sessionControl.setReturnValue(query, 1);
		query.setCacheable(true);
		queryControl.setReturnValue(query, 1);
		query.setCacheRegion("myCacheRegion");
		queryControl.setReturnValue(query, 1);
		query.list();
		queryControl.setReturnValue(list, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		ht.setCacheQueries(true);
		ht.setQueryCacheRegion("myCacheRegion");
		List result = ht.findByNamedQuery("some query name");
		assertTrue("Correct list", result == list);
		queryControl.verify();
	}

	public void testIterate() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		Iterator it = Collections.EMPTY_LIST.iterator();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.iterate();
		queryControl.setReturnValue(it, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		Iterator result = ht.iterate("some query string");
		assertTrue("Correct list", result == it);
		queryControl.verify();
	}

	public void testIterateWithParameter() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		Iterator it = Collections.EMPTY_LIST.iterator();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setParameter(0, "myvalue");
		queryControl.setReturnValue(query, 1);
		query.iterate();
		queryControl.setReturnValue(it, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		Iterator result = ht.iterate("some query string", "myvalue");
		assertTrue("Correct list", result == it);
		queryControl.verify();
	}

	public void testIterateWithParameters() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		Iterator it = Collections.EMPTY_LIST.iterator();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setParameter(0, "myvalue1");
		queryControl.setReturnValue(query, 1);
		query.setParameter(1, new Integer(2));
		queryControl.setReturnValue(query, 1);
		query.iterate();
		queryControl.setReturnValue(it, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		Iterator result = ht.iterate("some query string",
				new Object[] {"myvalue1", new Integer(2)});
		assertTrue("Correct list", result == it);
		sfControl.verify();
	}

	public void testIterateWithParameterAndType() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		Iterator it = Collections.EMPTY_LIST.iterator();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setParameter(0, "myvalue");
		queryControl.setReturnValue(query, 1);
		query.iterate();
		queryControl.setReturnValue(it, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		Iterator result = ht.iterate("some query string", "myvalue");
		assertTrue("Correct list", result == it);
		queryControl.verify();
	}

	public void testIterateWithParametersAndTypes() throws HibernateException {
		MockControl queryControl = MockControl.createControl(Query.class);
		Query query = (Query) queryControl.getMock();

		Iterator it = Collections.EMPTY_LIST.iterator();
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.createQuery("some query string");
		sessionControl.setReturnValue(query, 1);
		query.setParameter(0, "myvalue1", Hibernate.STRING);
		queryControl.setReturnValue(query, 1);
		query.setParameter(1, new Integer(2), Hibernate.INTEGER);
		queryControl.setReturnValue(query, 1);
		query.iterate();
		queryControl.setReturnValue(it, 1);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		queryControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		Iterator result = ht.iterate("some query string",
				new Object[] {"myvalue1", new Integer(2)},
				new Type[] {Hibernate.STRING, Hibernate.INTEGER});
		assertTrue("Correct list", result == it);
		sfControl.verify();
	}

	public void testDeleteWithQuery() throws HibernateException {
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.delete("from example.Example");
		sessionControl.setReturnValue(2);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		assertEquals(2, ht.delete("from example.Example"));
	}

	public void testDeleteWithQueryAndValue() throws HibernateException {
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.delete("from example.Example", new String[] {"test"}, new Type[] {Hibernate.STRING});
		sessionControl.setDefaultReturnValue(2);
		sessionControl.setMatcher(new ArgumentsMatcher() {
			public boolean matches(Object[] o1, Object[] o2) {
				return Arrays.equals((byte[]) o1[2], (byte[]) o2[2]);
			}
			public String toString(Object[] objects) {
				return null;
			}
		});
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		assertEquals(2, ht.delete("from example.Example", "test", Hibernate.STRING));
	}

	public void testDeleteWithQueryAndValues() throws HibernateException {
		Object[] values = new Object[]{"test1", "test2"};
		Type[] types = new Type[] {Hibernate.STRING, Hibernate.STRING};
		sf.openSession();
		sfControl.setReturnValue(session, 1);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.getFlushMode();
		sessionControl.setReturnValue(FlushMode.AUTO);
		session.delete("from example.Example", values, types);
		sessionControl.setReturnValue(2);
		session.flush();
		sessionControl.setVoidCallable(1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();

		HibernateTemplate ht = new HibernateTemplate(sf);
		assertEquals(2, ht.delete("from example.Example", values, types));
	}

	public void testExceptions() throws HibernateException {
		final SQLException sqlex = new SQLException("argh", "27");
		try {
			createTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					throw new JDBCException("mymsg", sqlex);
				}
			});
			fail("Should have thrown DataIntegrityViolationException");
		}
		catch (DataIntegrityViolationException ex) {
			// expected
			assertEquals(sqlex, ex.getCause());
			assertTrue(ex.getMessage().indexOf("mymsg") != -1);
		}

		try {
			createTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					throw new PersistentObjectException("");
				}
			});
			fail("Should have thrown InvalidDataAccessApiUsageException");
		}
		catch (InvalidDataAccessApiUsageException ex) {
			// expected
		}

		try {
			createTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					throw new TransientObjectException("");
				}
			});
			fail("Should have thrown InvalidDataAccessApiUsageException");
		}
		catch (InvalidDataAccessApiUsageException ex) {
			// expected
		}

		final ObjectDeletedException odex = new ObjectDeletedException("msg", "id", TestBean.class);
		try {
			createTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					throw odex;
				}
			});
			fail("Should have thrown InvalidDataAccessApiUsageException");
		}
		catch (InvalidDataAccessApiUsageException ex) {
			// expected
			assertEquals(odex, ex.getCause());
		}

		final QueryException qex = new QueryException("msg");
		qex.setQueryString("query");
		try {
			createTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					throw qex;
				}
			});
			fail("Should have thrown InvalidDataAccessResourceUsageException");
		}
		catch (HibernateQueryException ex) {
			// expected
			assertEquals(qex, ex.getCause());
			assertEquals("query", ex.getQueryString());
		}

		final UnresolvableObjectException uoex = new UnresolvableObjectException("id", TestBean.class);
		try {
			createTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					throw uoex;
				}
			});
			fail("Should have thrown HibernateObjectRetrievalFailureException");
		}
		catch (HibernateObjectRetrievalFailureException ex) {
			// expected
			assertEquals(TestBean.class.getName(), ex.getPersistentClassName());
			assertEquals("id", ex.getIdentifier());
			assertEquals(uoex, ex.getCause());
		}

		final ObjectNotFoundException onfe = new ObjectNotFoundException("id", TestBean.class);
		try {
			createTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					throw onfe;
				}
			});
			fail("Should have thrown HibernateObjectRetrievalFailureException");
		}
		catch (HibernateObjectRetrievalFailureException ex) {
			// expected
			assertEquals(TestBean.class.getName(), ex.getPersistentClassName());
			assertEquals("id", ex.getIdentifier());
			assertEquals(onfe, ex.getCause());
		}

		final WrongClassException wcex = new WrongClassException("msg", "id", TestBean.class);
		try {
			createTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					throw wcex;
				}
			});
			fail("Should have thrown HibernateObjectRetrievalFailureException");
		}
		catch (HibernateObjectRetrievalFailureException ex) {
			// expected
			assertEquals(TestBean.class, ex.getPersistentClass());
			assertEquals("id", ex.getIdentifier());
			assertEquals(wcex, ex.getCause());
		}

		final StaleObjectStateException sosex = new StaleObjectStateException(TestBean.class, "id");
		try {
			createTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					throw sosex;
				}
			});
			fail("Should have thrown HibernateOptimisticLockingFailureException");
		}
		catch (HibernateOptimisticLockingFailureException ex) {
			// expected
			assertEquals(TestBean.class, ex.getPersistentClass());
			assertEquals("id", ex.getIdentifier());
			assertEquals(sosex, ex.getCause());
		}

		final HibernateException hex = new HibernateException("msg");
		try {
			createTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					throw hex;
				}
			});
			fail("Should have thrown HibernateSystemException");
		}
		catch (HibernateSystemException ex) {
			// expected
			assertEquals(hex, ex.getCause());
		}
	}

	private HibernateTemplate createTemplate() throws HibernateException {
		sfControl.reset();
		sessionControl.reset();
		sf.openSession();
		sfControl.setReturnValue(session);
		session.getSessionFactory();
		sessionControl.setReturnValue(sf, 1);
		session.close();
		sessionControl.setReturnValue(null, 1);
		sfControl.replay();
		sessionControl.replay();
		return new HibernateTemplate(sf);
	}

	protected void tearDown() {
		try {
			sfControl.verify();
			sessionControl.verify();
		}
		catch (IllegalStateException ex) {
			// ignore: test method didn't call replay
		}
		assertTrue(TransactionSynchronizationManager.getResourceMap().isEmpty());
		assertFalse(TransactionSynchronizationManager.isSynchronizationActive());
	}

}
