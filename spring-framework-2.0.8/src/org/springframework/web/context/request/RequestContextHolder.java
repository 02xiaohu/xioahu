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

package org.springframework.web.context.request;

/**
 * Holder class to expose the web request in the form of a thread-bound
 * {@link RequestAttributes} object.
 *
 * <p>Use {@link RequestContextListener} or
 * {@link org.springframework.web.filter.RequestContextFilter} to expose
 * the current web request. Note that
 * {@link org.springframework.web.servlet.DispatcherServlet} and
 * {@link org.springframework.web.portlet.DispatcherPortlet} already
 * expose the current request by default.
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @since 2.0
 * @see RequestContextListener
 * @see org.springframework.web.filter.RequestContextFilter
 * @see org.springframework.web.servlet.DispatcherServlet
 * @see org.springframework.web.portlet.DispatcherPortlet
 */
public abstract class RequestContextHolder  {
	
	private static final ThreadLocal requestAttributesHolder = new ThreadLocal();

	private static final ThreadLocal inheritableRequestAttributesHolder = new InheritableThreadLocal();


	/**
	 * Reset the RequestAttributes for the current thread.
	 */
	public static void resetRequestAttributes() {
		requestAttributesHolder.set(null);
		inheritableRequestAttributesHolder.set(null);
	}

	/**
	 * Bind the given RequestAttributes to the current thread,
	 * <i>not</i> exposing it as inheritable for child threads.
	 * @param attributes the RequestAttributes to expose
	 * @see #setRequestAttributes(RequestAttributes, boolean)
	 */
	public static void setRequestAttributes(RequestAttributes attributes) {
		setRequestAttributes(attributes, false);
	}

	/**
	 * Bind the given RequestAttributes to the current thread.
	 * @param attributes the RequestAttributes to expose
	 * @param inheritable whether to expose the RequestAttributes as inheritable
	 * for child threads (using an {@link java.lang.InheritableThreadLocal})
	 */
	public static void setRequestAttributes(RequestAttributes attributes, boolean inheritable) {
		if (inheritable) {
			inheritableRequestAttributesHolder.set(attributes);
			requestAttributesHolder.set(null);
		}
		else {
			requestAttributesHolder.set(attributes);
			inheritableRequestAttributesHolder.set(null);
		}
	}

	/**
	 * Return the RequestAttributes currently bound to the thread.
	 * @return the RequestAttributes currently bound to the thread,
	 * or <code>null</code> if none bound
	 */
	public static RequestAttributes getRequestAttributes() {
		RequestAttributes attributes = (RequestAttributes) requestAttributesHolder.get();
		if (attributes == null) {
			attributes = (RequestAttributes) inheritableRequestAttributesHolder.get();
		}
		return attributes;
	}

	/**
	 * Return the RequestAttributes currently bound to the thread.
	 * @return the RequestAttributes currently bound to the thread
	 * @throws IllegalStateException if no RequestAttributes object
	 * is bound to the current thread
	 */
	public static RequestAttributes currentRequestAttributes() throws IllegalStateException {
		RequestAttributes attributes = getRequestAttributes();
		if (attributes == null) {
			throw new IllegalStateException("No thread-bound request found: " +
					"Are you referring to request attributes outside of an actual web request? " +
					"If you are actually operating within a web request and still receive this message," +
					"your code is probably running outside of DispatcherServlet/DispatcherPortlet: " +
					"In this case, use RequestContextListener or RequestContextFilter to expose the current request.");
		}
		return attributes;
	}

}
