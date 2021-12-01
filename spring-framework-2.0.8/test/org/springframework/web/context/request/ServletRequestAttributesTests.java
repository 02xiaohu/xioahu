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

package org.springframework.web.context.request;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;
import org.easymock.MockControl;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.AssertThrows;

/**
 * @author Rick Evans
 * @author Juergen Hoeller
 */
public final class ServletRequestAttributesTests extends TestCase {

	private static final String KEY = "ThatThingThatThing";


	private static final Serializable VALUE = new Serializable() {
	};


	public void testCtorRejectsNullArg() throws Exception {
		new AssertThrows(IllegalArgumentException.class) {
			public void test() throws Exception {
				new ServletRequestAttributes(null);
			}
		}.runTest();
	}

	public void testUpdateAccessedAttributes() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(KEY, VALUE);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		ServletRequestAttributes attrs = new ServletRequestAttributes(request);
		Object value = attrs.getAttribute(KEY, RequestAttributes.SCOPE_SESSION);
		assertSame(VALUE, value);
		attrs.requestCompleted();
	}

	public void testSetRequestScopedAttribute() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		ServletRequestAttributes attrs = new ServletRequestAttributes(request);
		attrs.setAttribute(KEY, VALUE, RequestAttributes.SCOPE_REQUEST);
		Object value = request.getAttribute(KEY);
		assertSame(VALUE, value);
	}

	public void testSetRequestScopedAttributeAfterCompletion() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		ServletRequestAttributes attrs = new ServletRequestAttributes(request);
		request.close();
		try {
			attrs.setAttribute(KEY, VALUE, RequestAttributes.SCOPE_REQUEST);
			fail("Should have thrown IllegalStateException");
		}
		catch (IllegalStateException ex) {
			// expected
		}
	}

	public void testSetSessionScopedAttribute() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(KEY, VALUE);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		ServletRequestAttributes attrs = new ServletRequestAttributes(request);
		attrs.setAttribute(KEY, VALUE, RequestAttributes.SCOPE_SESSION);
		Object value = session.getAttribute(KEY);
		assertSame(VALUE, value);
	}

	public void testSetSessionScopedAttributeAfterCompletion() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(KEY, VALUE);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		ServletRequestAttributes attrs = new ServletRequestAttributes(request);
		request.close();
		attrs.setAttribute(KEY, VALUE, RequestAttributes.SCOPE_SESSION);
		Object value = session.getAttribute(KEY);
		assertSame(VALUE, value);
	}

	public void testSetGlobalSessionScopedAttribute() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(KEY, VALUE);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		ServletRequestAttributes attrs = new ServletRequestAttributes(request);
		attrs.setAttribute(KEY, VALUE, RequestAttributes.SCOPE_GLOBAL_SESSION);
		Object value = session.getAttribute(KEY);
		assertSame(VALUE, value);
	}

	public void testSetGlobalSessionScopedAttributeAfterCompletion() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(KEY, VALUE);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		ServletRequestAttributes attrs = new ServletRequestAttributes(request);
		request.close();
		attrs.setAttribute(KEY, VALUE, RequestAttributes.SCOPE_GLOBAL_SESSION);
		Object value = session.getAttribute(KEY);
		assertSame(VALUE, value);
	}

	public void testGetSessionScopedAttributeDoesNotForceCreationOfSession() throws Exception {
		MockControl mockRequest = MockControl.createControl(HttpServletRequest.class);
		HttpServletRequest request = (HttpServletRequest) mockRequest.getMock();
		request.getSession(false);
		mockRequest.setReturnValue(null, 2);
		mockRequest.replay();

		ServletRequestAttributes attrs = new ServletRequestAttributes(request);
		Object value = attrs.getAttribute(KEY, RequestAttributes.SCOPE_SESSION);
		assertNull(value);

		mockRequest.verify();
	}

	public void testRemoveSessionScopedAttribute() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(KEY, VALUE);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		ServletRequestAttributes attrs = new ServletRequestAttributes(request);
		attrs.removeAttribute(KEY, RequestAttributes.SCOPE_SESSION);
		Object value = session.getAttribute(KEY);
		assertNull(value);
	}

	public void testRemoveSessionScopedAttributeDoesNotForceCreationOfSession() throws Exception {
		MockControl mockRequest = MockControl.createControl(HttpServletRequest.class);
		HttpServletRequest request = (HttpServletRequest) mockRequest.getMock();
		request.getSession(false);
		mockRequest.setReturnValue(null, 2);
		mockRequest.replay();

		ServletRequestAttributes attrs = new ServletRequestAttributes(request);
		attrs.removeAttribute(KEY, RequestAttributes.SCOPE_SESSION);

		mockRequest.verify();
	}

}
