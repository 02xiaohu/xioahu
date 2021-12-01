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

package org.springframework.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Bootstrap listener to start up Spring's root {@link WebApplicationContext}.
 * Simply delegates to {@link ContextLoader}.
 *
 * <p>This listener should be registered after
 * {@link org.springframework.web.util.Log4jConfigListener}
 * in <code>web.xml</code>, if the latter is used.
 *
 * <p>For Servlet 2.2 containers and Servlet 2.3 ones that do not initalize
 * listeners before servlets, use {@link ContextLoaderServlet}.
 * See the latter's Javadoc for details.
 *
 * @author Juergen Hoeller
 * @since 17.02.2003
 * @see ContextLoaderServlet
 * @see org.springframework.web.util.Log4jConfigListener
 */
public class ContextLoaderListener implements ServletContextListener {

	private ContextLoader contextLoader;


	/**
	 * Initialize the root web application context.
	 */
	public void contextInitialized(ServletContextEvent event) {
		this.contextLoader = createContextLoader();
		this.contextLoader.initWebApplicationContext(event.getServletContext());
	}

	/**
	 * Create the ContextLoader to use. Can be overridden in subclasses.
	 * @return the new ContextLoader
	 */
	protected ContextLoader createContextLoader() {
		return new ContextLoader();
	}

	/**
	 * Return the ContextLoader used by this listener.
	 * @return the current ContextLoader
	 */
	public ContextLoader getContextLoader() {
		return this.contextLoader;
	}


	/**
	 * Close the root web application context.
	 */
	public void contextDestroyed(ServletContextEvent event) {
		if (this.contextLoader != null) {
			this.contextLoader.closeWebApplicationContext(event.getServletContext());
		}
	}

}
