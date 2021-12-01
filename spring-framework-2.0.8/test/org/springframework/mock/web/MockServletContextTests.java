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

package org.springframework.mock.web;

import java.util.Set;

import junit.framework.TestCase;

/**
 * @author Juergen Hoeller
 * @since 19.02.2006
 */
public class MockServletContextTests extends TestCase {

	public void testListFiles() {
		MockServletContext sc = new MockServletContext("org/springframework/mock");
		Set paths = sc.getResourcePaths("/web");
		assertNotNull(paths);
		assertTrue(paths.contains("/web/MockServletContextTests.class"));
	}

	public void testListSubdirectories() {
		MockServletContext sc = new MockServletContext("org/springframework/mock");
		Set paths = sc.getResourcePaths("/");
		assertNotNull(paths);
		assertTrue(paths.contains("/web/"));
	}

	public void testListNonDirectory() {
		MockServletContext sc = new MockServletContext("org/springframework/mock");
		Set paths = sc.getResourcePaths("/web/MockServletContextTests.class");
		assertNull(paths);
	}

	public void testListInvalidPath() {
		MockServletContext sc = new MockServletContext("org/springframework/mock");
		Set paths = sc.getResourcePaths("/web/invalid");
		assertNull(paths);
	}

}
