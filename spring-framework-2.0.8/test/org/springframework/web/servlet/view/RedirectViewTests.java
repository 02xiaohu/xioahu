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

package org.springframework.web.servlet.view;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.easymock.MockControl;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Tests for redirect view, and query string construction.
 * Doesn't test URL encoding, although it does check that it's called.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 27.05.2003
 */
public class RedirectViewTests extends TestCase {

	public void testNoUrlSet() throws Exception {
		RedirectView rv = new RedirectView();
		try {
			rv.afterPropertiesSet();
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
	}

	public void testHttp11() throws Exception {
		RedirectView rv = new RedirectView();
		rv.setUrl("http://url.somewhere.com");
		rv.setHttp10Compatible(false);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		rv.render(new HashMap(), request, response);
		assertEquals(303, response.getStatus());
		assertEquals("http://url.somewhere.com", response.getHeader("Location"));
	}

	public void testEmptyMap() throws Exception {
		String url = "/myUrl";
		doTest(new HashMap(), url, false, url);
	}

	public void testEmptyMapWithContextRelative() throws Exception {
		String url = "/myUrl";
		doTest(new HashMap(), url, true, url);
	}

	public void testSingleParam() throws Exception {
		String url = "http://url.somewhere.com";
		String key = "foo";
		String val = "bar";
		Map m = new HashMap();
		m.put(key, val);
		String expectedUrlForEncoding = url + "?" + key + "=" + val;
		doTest(m, url, false, expectedUrlForEncoding);
	}

	public void testSingleParamWithoutExposingModelAttributes() throws Exception {
		String url = "http://url.somewhere.com";
		String key = "foo";
		String val = "bar";
		Map m = new HashMap();
		m.put(key, val);
		String expectedUrlForEncoding = url; // + "?" + key + "=" + val;
		doTest(m, url, false, false, expectedUrlForEncoding);
	}

	public void testParamWithAnchor() throws Exception {
		String url = "http://url.somewhere.com/test.htm#myAnchor";
		String key = "foo";
		String val = "bar";
		Map m = new HashMap();
		m.put(key, val);
		String expectedUrlForEncoding = "http://url.somewhere.com/test.htm" + "?" + key + "=" + val + "#myAnchor";
		doTest(m, url, false, expectedUrlForEncoding);
	}

	public void testTwoParams() throws Exception {
		String url = "http://url.somewhere.com";
		String key = "foo";
		String val = "bar";
		String key2 = "thisIsKey2";
		String val2 = "andThisIsVal2";
		Map m = new HashMap();
		m.put(key, val);
		m.put(key2, val2);
		try {
			String expectedUrlForEncoding = "http://url.somewhere.com?" + key + "=" + val + "&" + key2 + "=" + val2;
			doTest(m, url, false, expectedUrlForEncoding);
		}
		catch (AssertionFailedError err) {
			// OK, so it's the other order... probably on Sun JDK 1.6 or IBM JDK 1.5
			String expectedUrlForEncoding = "http://url.somewhere.com?" + key2 + "=" + val2 + "&" + key + "=" + val;
			doTest(m, url, false, expectedUrlForEncoding);
		}
	}

	public void testObjectConversion() throws Exception {
		String url = "http://url.somewhere.com";
		String key = "foo";
		String val = "bar";
		String key2 = "int2";
		Object val2 = new Long(611);
		Map m = new HashMap();
		m.put(key, val);
		m.put(key2, val2);
		try {
			String expectedUrlForEncoding = "http://url.somewhere.com?" + key + "=" + val + "&" + key2 + "=" + val2;
			doTest(m, url, false, expectedUrlForEncoding);
		}
		catch (AssertionFailedError err) {
			// OK, so it's the other order... probably on Sun JDK 1.6 or IBM JDK 1.5
			String expectedUrlForEncoding = "http://url.somewhere.com?" + key2 + "=" + val2 + "&" + key + "=" + val;
			doTest(m, url, false, expectedUrlForEncoding);
		}
	}

	private void doTest(final Map map, final String url, final boolean contextRelative, String expectedUrlForEncoding)
			throws Exception {
		doTest(map, url, contextRelative, true, expectedUrlForEncoding);
	}

	private void doTest(final Map map, final String url, final boolean contextRelative,
			final boolean exposeModelAttributes, String expectedUrlForEncoding) throws Exception {

		class TestRedirectView extends RedirectView {

			public boolean queryPropertiesCalled = false;

			/**
			 * Test whether this callback method is called with correct args
			 */
			protected Map queryProperties(Map model) {
				// They may not be the same model instance, but they're still equal
				assertTrue("Map and model must be equal.", map.equals(model));
				this.queryPropertiesCalled = true;
				return super.queryProperties(model);
			}
		}

		TestRedirectView rv = new TestRedirectView();
		rv.setUrl(url);
		rv.setContextRelative(contextRelative);
		rv.setExposeModelAttributes(exposeModelAttributes);

		MockControl rc = MockControl.createControl(HttpServletRequest.class);
		HttpServletRequest request = (HttpServletRequest) rc.getMock();
		if (contextRelative) {
			expectedUrlForEncoding = "/context" + expectedUrlForEncoding;
			request.getContextPath();
			rc.setReturnValue("/context");
		}
		rc.replay();

		MockControl mc = MockControl.createControl(HttpServletResponse.class);
		HttpServletResponse resp = (HttpServletResponse) mc.getMock();
		resp.encodeRedirectURL(expectedUrlForEncoding);
		mc.setReturnValue(expectedUrlForEncoding);
		resp.sendRedirect(expectedUrlForEncoding);
		mc.setVoidCallable(1);
		mc.replay();

		rv.render(map, request, resp);
		if (exposeModelAttributes) {
			assertTrue("queryProperties() should have been called.", rv.queryPropertiesCalled);
		}
		mc.verify();
	}

}
