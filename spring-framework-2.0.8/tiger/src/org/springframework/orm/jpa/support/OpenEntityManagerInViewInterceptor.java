/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springframework.orm.jpa.support;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryAccessor;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * Spring web request interceptor that binds a JPA EntityManager to the
 * thread for the entire processing of the request. Intended for the "Open
 * EntityManager in View" pattern, i.e. to allow for lazy loading in
 * web views despite the original transactions already being completed.
 *
 * <p>This interceptor makes JPA EntityManagers available via the current thread,
 * which will be autodetected by transaction managers. It is suitable for service
 * layer transactions via {@link org.springframework.orm.jpa.JpaTransactionManager}
 * or {@link org.springframework.transaction.jta.JtaTransactionManager} as well
 * as for non-transactional read-only execution.
 *
 * <p>In contrast to {@link OpenEntityManagerInViewFilter}, this interceptor
 * is set up in a Spring application context and can thus take advantage of
 * bean wiring. It inherits common JPA configuration properties from
 * {@link org.springframework.orm.jpa.JpaAccessor}, to be configured in a
 * bean definition.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see OpenEntityManagerInViewFilter
 * @see org.springframework.orm.jpa.JpaInterceptor
 * @see org.springframework.orm.jpa.JpaTransactionManager
 * @see org.springframework.orm.jpa.JpaTemplate#execute
 * @see org.springframework.orm.jpa.SharedEntityManagerCreator
 * @see org.springframework.transaction.support.TransactionSynchronizationManager
 */
public class OpenEntityManagerInViewInterceptor extends EntityManagerFactoryAccessor implements WebRequestInterceptor {

	/**
	 * Suffix that gets appended to the EntityManagerFactory toString
	 * representation for the "participate in existing entity manager
	 * handling" request attribute.
	 * @see #getParticipateAttributeName
	 */
	public static final String PARTICIPATE_SUFFIX = ".PARTICIPATE";


	public void preHandle(WebRequest request) throws DataAccessException {
		if (TransactionSynchronizationManager.hasResource(getEntityManagerFactory())) {
			// do not modify the EntityManager: just mark the request accordingly
			String participateAttributeName = getParticipateAttributeName();
			Integer count = (Integer) request.getAttribute(participateAttributeName, WebRequest.SCOPE_REQUEST);
			int newCount = (count != null) ? count.intValue() + 1 : 1;
			request.setAttribute(getParticipateAttributeName(), new Integer(newCount), WebRequest.SCOPE_REQUEST);
		}
		else {
			logger.debug("Opening JPA EntityManager in OpenEntityManagerInViewInterceptor");
			try {
				EntityManager em = createEntityManager();
				TransactionSynchronizationManager.bindResource(getEntityManagerFactory(), new EntityManagerHolder(em));
			}
			catch (PersistenceException ex) {
				throw new DataAccessResourceFailureException("Could not create JPA EntityManager", ex);
			}
		}
	}

	public void postHandle(WebRequest request, ModelMap model) {
	}

	public void afterCompletion(WebRequest request, Exception ex) throws DataAccessException {
		String participateAttributeName = getParticipateAttributeName();
		Integer count = (Integer) request.getAttribute(participateAttributeName, WebRequest.SCOPE_REQUEST);
		if (count != null) {
			// Do not modify the EntityManager: just clear the marker.
			if (count.intValue() > 1) {
				request.setAttribute(participateAttributeName, new Integer(count.intValue() - 1), WebRequest.SCOPE_REQUEST);
			}
			else {
				request.removeAttribute(participateAttributeName, WebRequest.SCOPE_REQUEST);
			}
		}
		else {
			EntityManagerHolder emHolder = (EntityManagerHolder)
					TransactionSynchronizationManager.unbindResource(getEntityManagerFactory());
			logger.debug("Closing JPA EntityManager in OpenEntityManagerInViewInterceptor");
			emHolder.getEntityManager().close();
		}
	}

	/**
	 * Return the name of the request attribute that identifies that a request is
	 * already filtered. Default implementation takes the toString representation
	 * of the EntityManagerFactory instance and appends ".FILTERED".
	 * @see #PARTICIPATE_SUFFIX
	 */
	protected String getParticipateAttributeName() {
		return getEntityManagerFactory().toString() + PARTICIPATE_SUFFIX;
	}

}
