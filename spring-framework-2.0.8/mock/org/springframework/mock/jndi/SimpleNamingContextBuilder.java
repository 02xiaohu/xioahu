/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springframework.mock.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple implementation of a JNDI naming context builder.
 *
 * <p>Mainly targeted at test environments, where each test case can
 * configure JNDI appropriately, so that <code>new InitialContext()</code>
 * will expose the required objects. Also usable for standalone applications,
 * e.g. for binding a JDBC DataSource to a well-known JNDI location, to be
 * able to use traditional J2EE data access code outside of a J2EE container.
 *
 * <p>There are various choices for DataSource implementations:
 * <ul>
 * <li>SingleConnectionDataSource (using the same Connection for all getConnection calls);
 * <li>DriverManagerDataSource (creating a new Connection on each getConnection call);
 * <li>Apache's Jakarta Commons DBCP offers BasicDataSource (a real pool).
 * </ul>
 *
 * <p>Typical usage in bootstrap code:
 *
 * <pre>
 * SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
 * DataSource ds = new DriverManagerDataSource(...);
 * builder.bind("java:comp/env/jdbc/myds", ds);
 * builder.activate();</pre>
 * 
 * Note that it's impossible to activate multiple builders within the same JVM,
 * due to JNDI restrictions. Thus to configure a fresh builder repeatedly, use
 * the following code to get a reference to either an already activated builder
 * or a newly activated one:
 *
 * <pre>
 * SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
 * DataSource ds = new DriverManagerDataSource(...);
 * builder.bind("java:comp/env/jdbc/myds", ds);</pre>
 *
 * Note that you <i>should not</i> call activate() on a builder from this
 * factory method, as there will already be an activated one in any case.
 *
 * <p>An instance of this class is only necessary at setup time.
 * An application does not need to keep a reference to it after activation.
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @see #emptyActivatedContextBuilder()
 * @see #bind(String, Object)
 * @see #activate()
 * @see org.springframework.mock.jndi.SimpleNamingContext
 * @see org.springframework.jdbc.datasource.SingleConnectionDataSource
 * @see org.springframework.jdbc.datasource.DriverManagerDataSource
 * @see org.apache.commons.dbcp.BasicDataSource
 */
public class SimpleNamingContextBuilder implements InitialContextFactoryBuilder {
	
	/** An instance of this class bound to JNDI */
	private static SimpleNamingContextBuilder activated;


	/**
	 * Checks if a SimpleNamingContextBuilder is active.
	 * @return the current SimpleNamingContextBuilder instance,
	 * or <code>null</code> if none
	 */
	public static SimpleNamingContextBuilder getCurrentContextBuilder() {
		return activated;
	}

	/**
	 * If no SimpleNamingContextBuilder is already configuring JNDI,
	 * create and activate one. Otherwise take the existing activate
	 * SimpleNamingContextBuilder, clear it and return it.
	 * <p>This is mainly intended for test suites that want to
	 * reinitialize JNDI bindings from scratch repeatedly.
	 * @return an empty SimpleNamingContextBuilder that can be used
	 * to control JNDI bindings
	 */
	public static SimpleNamingContextBuilder emptyActivatedContextBuilder() throws NamingException {
		if (activated != null) {
			// Clear already activated context builder.
			activated.clear();
		}
		else {
			// Create and activate new context builder.
			SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
			// The activate() call will cause an assigment to the activated variable.
			builder.activate();
		}
		return activated;
	}


	private final Log logger = LogFactory.getLog(getClass());

	private final Hashtable boundObjects = new Hashtable();


	/**
	 * Register the context builder by registering it with the JNDI NamingManager.
	 * Note that once this has been done, <code>new InitialContext()</code> will always
	 * return a context from this factory. Use the <code>emptyActivatedContextBuilder()</code>
	 * static method to get an empty context (for example, in test methods).
	 * @throws IllegalStateException if there's already a naming context builder
	 * registered with the JNDI NamingManager
	 */
	public void activate() throws IllegalStateException, NamingException {
		logger.info("Activating simple JNDI environment");
		if (NamingManager.hasInitialContextFactoryBuilder()) {
			throw new IllegalStateException(
					"Cannot activate SimpleNamingContextBuilder: there is already a JNDI provider registered. " +
					"Note that JNDI is a JVM-wide service, shared at the JVM system class loader level, " +
					"with no reset option. As a consequence, a JNDI provider must only be registered once per JVM.");
		}
		NamingManager.setInitialContextFactoryBuilder(this);
		activated = this;
	}

	/**
	 * Clear all bindings in this context builder.
	 */
	public void clear() {
		this.boundObjects.clear();
	}

	/**
	 * Bind the given object under the given name, for all naming contexts
	 * that this context builder will generate.
	 * @param name the JNDI name of the object (e.g. "java:comp/env/jdbc/myds")
	 * @param obj the object to bind (e.g. a DataSource implementation)
	 */
	public void bind(String name, Object obj) {
		if (logger.isInfoEnabled()) {
			logger.info("Static JNDI binding: [" + name + "] = [" + obj + "]");
		}
		this.boundObjects.put(name, obj);
	}

	/**
	 * Simple InitialContextFactoryBuilder implementation,
	 * creating a new SimpleNamingContext instance.
	 * @see SimpleNamingContext
	 */
	public InitialContextFactory createInitialContextFactory(Hashtable environment) {
		return new InitialContextFactory() {
			public Context getInitialContext(Hashtable environment) {
				return new SimpleNamingContext("", boundObjects, environment);
			}
		};
	}

}
