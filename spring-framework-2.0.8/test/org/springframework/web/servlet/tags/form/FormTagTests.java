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

package org.springframework.web.servlet.tags.form;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.AssertThrows;

/**
 * @author Rob Harrop
 * @author Rick Evans
 * @author Juergen Hoeller
 */
public class FormTagTests extends AbstractHtmlElementTagTests {
	
	private static final String REQUEST_URI = "/my/form";

	private static final String QUERY_STRING = "foo=bar";


	private FormTag tag;
	
	private MockHttpServletRequest request;


	protected void onSetUp() {
		this.tag = new FormTag() {
			protected TagWriter createTagWriter() {
				return new TagWriter(getWriter());
			}
		};
		this.tag.setPageContext(getPageContext());
	}


	protected void extendRequest(MockHttpServletRequest request) {
		request.setRequestURI(REQUEST_URI);
		request.setQueryString(QUERY_STRING);
		this.request = request;
	}


	public void testWriteForm() throws Exception {
		String action = "/form.html";
		String commandName = "myCommand";
		String name = "formName";
		String enctype = "my/enctype";
		String method = "POST";
		String onsubmit = "onsubmit";
		String onreset = "onreset";
		String cssClass = "myClass";
		String cssStyle = "myStyle";
		String acceptCharset = "iso-8859-1";

		this.tag.setName(name);
		this.tag.setCssClass(cssClass);
		this.tag.setCssStyle(cssStyle);
		this.tag.setAction(action);
		this.tag.setCommandName(commandName);
		this.tag.setEnctype(enctype);
		this.tag.setMethod(method);
		this.tag.setOnsubmit(onsubmit);
		this.tag.setOnreset(onreset);
		this.tag.setAcceptCharset(acceptCharset);

		int result = this.tag.doStartTag();
		assertEquals(Tag.EVAL_BODY_INCLUDE, result);
		assertEquals("Command name not exposed", commandName,
				getPageContext().getRequest().getAttribute(FormTag.COMMAND_NAME_VARIABLE_NAME));

		result = this.tag.doEndTag();
		assertEquals(Tag.EVAL_PAGE, result);

		this.tag.doFinally();
		assertNull("Command name not cleared after tag ends",
				getPageContext().getRequest().getAttribute(FormTag.COMMAND_NAME_VARIABLE_NAME));

		String output = getWriter().toString();
		assertFormTagOpened(output);
		assertFormTagClosed(output);

		assertContainsAttribute(output, "class", cssClass);
		assertContainsAttribute(output, "style", cssStyle);
		assertContainsAttribute(output, "action", action);
		assertContainsAttribute(output, "enctype", enctype);
		assertContainsAttribute(output, "method", method);
		assertContainsAttribute(output, "onsubmit", onsubmit);
		assertContainsAttribute(output, "onreset", onreset);
		assertContainsAttribute(output, "id", commandName);
		assertContainsAttribute(output, "name", name);
		assertContainsAttribute(output, "accept-charset", acceptCharset);
	}

	public void testWithActionFromRequest() throws Exception {
		String commandName = "myCommand";
		String enctype = "my/enctype";
		String method = "POST";
		String onsubmit = "onsubmit";
		String onreset = "onreset";

		this.tag.setCommandName(commandName);
		this.tag.setEnctype(enctype);
		this.tag.setMethod(method);
		this.tag.setOnsubmit(onsubmit);
		this.tag.setOnreset(onreset);

		int result = this.tag.doStartTag();
		assertEquals(Tag.EVAL_BODY_INCLUDE, result);
		assertEquals("Command name not exposed", commandName,
				getPageContext().getAttribute(FormTag.COMMAND_NAME_VARIABLE_NAME, PageContext.REQUEST_SCOPE));

		result = this.tag.doEndTag();
		assertEquals(Tag.EVAL_PAGE, result);

		this.tag.doFinally();
		assertNull("Command name not cleared after tag ends",
				getPageContext().getAttribute(FormTag.COMMAND_NAME_VARIABLE_NAME, PageContext.REQUEST_SCOPE));

		String output = getWriter().toString();
		assertFormTagOpened(output);
		assertFormTagClosed(output);

		assertContainsAttribute(output, "action", REQUEST_URI + "?" + QUERY_STRING);
		assertContainsAttribute(output, "enctype", enctype);
		assertContainsAttribute(output, "method", method);
		assertContainsAttribute(output, "onsubmit", onsubmit);
		assertContainsAttribute(output, "onreset", onreset);
		assertAttributeNotPresent(output, "name");
	}

	public void testWithNullResolvedCommand() throws Exception {
		new AssertThrows(IllegalArgumentException.class,
				"Must not be able to have a command name that resolves to null") {
			public void test() throws Exception {
				tag.setCommandName("${null}");
				tag.doStartTag();
			}
		}.runTest();
	}

	/*
	 * See http://opensource.atlassian.com/projects/spring/browse/SPR-2645
	 */
	public void testXSSScriptingExploitWhenActionIsResolvedFromQueryString() throws Exception {
		String xssQueryString = QUERY_STRING + "&stuff=\"><script>alert('XSS!')</script>";
		request.setQueryString(xssQueryString);
		tag.doStartTag();
		assertEquals("<form id=\"command\" method=\"post\" action=\"/my/form?foo=bar&amp;stuff=&quot;&gt;&lt;script&gt;alert('XSS!')&lt;/script&gt;\">",
				getWriter().toString());
	}


	private static void assertFormTagOpened(String output) {
		assertTrue(output.startsWith("<form "));
	}

	private static void assertFormTagClosed(String output) {
		assertTrue(output.endsWith("</form>"));
	}

}
