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

package org.springframework.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Superclass for JUnit test cases using Spring
 * {@link org.springframework.context.ApplicationContext ApplicationContexts}.
 *
 * <p>Maintains a static cache of contexts by key. This has significant performance
 * benefit if initializing the context would take time. While initializing a
 * Spring context itself is very quick, some beans in a context, such as
 * a LocalSessionFactoryBean for working with Hibernate, may take some time
 * to initialize. Hence it often makes sense to do that initializing once.
 *
 * <p>Any ApplicationContext created by this class will be asked to register a JVM
 * shutdown hook for itself. Unless the context gets closed early, all context
 * instances will be automatically closed on JVM shutdown. This allows for freeing
 * external resources held by beans within the context, e.g. temporary files.
 *
 * <p>Normally you won't extend this class directly but rather extend one of
 * its subclasses.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 1.1.1
 * @see AbstractDependencyInjectionSpringContextTests
 * @see AbstractTransactionalSpringContextTests
 * @see AbstractTransactionalDataSourceSpringContextTests
 */
public abstract class AbstractSpringContextTests extends ConditionalTestCase {

	/**
	 * Map of context keys returned by subclasses of this class, to
	 * Spring contexts. This needs to be static, as JUnit tests are
	 * destroyed and recreated between running individual test methods.
	 */
	private static Map contextKeyToContextMap = new HashMap();


	/**
	 * Default constructor for AbstractSpringContextTests.
	 */
	public AbstractSpringContextTests() {
	}

	/**
	 * Constructor for AbstractSpringContextTests with a JUnit name.
	 */
	public AbstractSpringContextTests(String name) {
		super(name);
	}


	/**
	 * Explicitly add an ApplicationContext instance under a given key.
	 * <p>This is not meant to be used by subclasses. It is rather exposed
	 * for special test suite environments.
	 * @param key the context key
	 * @param context the ApplicationContext instance
	 */
	public final void addContext(Object key, ConfigurableApplicationContext context) {
		Assert.notNull(context, "ApplicationContext must not be null");
		contextKeyToContextMap.put(contextKeyString(key), context);
	}

	/**
	 * Return whether there is a cached context for the given key.
	 * @param key the context key
	 */
	protected final boolean hasCachedContext(Object key) {
		return contextKeyToContextMap.containsKey(contextKeyString(key));
	}

	/**
	 * Obtain an ApplicationContext for the given key, potentially cached.
	 * @param key the context key
	 * @return the corresponding ApplicationContext instance (potentially cached)
	 */
	protected final ConfigurableApplicationContext getContext(Object key) throws Exception {
		String keyString = contextKeyString(key);
		ConfigurableApplicationContext ctx =
				(ConfigurableApplicationContext) contextKeyToContextMap.get(keyString);
		if (ctx == null) {
			ctx = loadContext(key);
			ctx.registerShutdownHook();
			contextKeyToContextMap.put(keyString, ctx);
		}
		return ctx;
	}

	/**
	 * Mark the context with the given key as dirty. This will cause the
	 * cached context to be reloaded before the next test case is executed.
	 * <p>Call this method only if you change the state of a singleton
	 * bean, potentially affecting future tests.
	 */
	protected final void setDirty(Object contextKey) {
		String keyString = contextKeyString(contextKey);
		ConfigurableApplicationContext ctx =
				(ConfigurableApplicationContext) contextKeyToContextMap.remove(keyString);
		if (ctx != null) {
			ctx.close();
		}
	}


	/**
	 * Subclasses can override this to return a String representation of
	 * their context key for use in logging.
	 * @param contextKey the context key
	 */
	protected String contextKeyString(Object contextKey) {
		return ObjectUtils.nullSafeToString(contextKey);
	}

	/**
	 * Load a new ApplicationContext for the given key.
	 * <p>To be implemented by subclasses.
	 * @param key the context key
	 * @return the corresponding ApplicationContext instance (new)
	 */
	protected abstract ConfigurableApplicationContext loadContext(Object key) throws Exception;

}
