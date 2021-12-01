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

import junit.framework.TestCase;

import org.springframework.beans.DerivedTestBean;
import org.springframework.beans.TestBean;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 */
public class RequestScopeTests extends TestCase {

	private DefaultListableBeanFactory beanFactory;

	protected void setUp() throws Exception {
		this.beanFactory = new DefaultListableBeanFactory();
		this.beanFactory.registerScope("request", new RequestScope());
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this.beanFactory);
		reader.loadBeanDefinitions(new ClassPathResource("requestScopeTests.xml", getClass()));
		this.beanFactory.preInstantiateSingletons();
	}

	public void testGetFromScope() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(requestAttributes);

		try {
			String name = "requestScopedObject";
			assertNull(request.getAttribute(name));
			TestBean bean = (TestBean) this.beanFactory.getBean(name);
			assertSame(bean, request.getAttribute(name));
			assertSame(bean, this.beanFactory.getBean(name));
		}
		finally {
			RequestContextHolder.setRequestAttributes(null);
		}
	}

	public void testDestructionAtRequestCompletion() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(requestAttributes);

		try {
			String name = "requestScopedDisposableObject";
			assertNull(request.getAttribute(name));
			DerivedTestBean bean = (DerivedTestBean) this.beanFactory.getBean(name);
			assertSame(bean, request.getAttribute(name));
			assertSame(bean, this.beanFactory.getBean(name));

			requestAttributes.requestCompleted();
			assertTrue(bean.wasDestroyed());
		}
		finally {
			RequestContextHolder.setRequestAttributes(null);
		}
	}

	public void testGetFromFactoryBeanInScope() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(requestAttributes);

		try {
			String name = "requestScopedFactoryBean";
			assertNull(request.getAttribute(name));
			TestBean bean = (TestBean) this.beanFactory.getBean(name);
			assertTrue(request.getAttribute(name) instanceof FactoryBean);
			assertSame(bean, this.beanFactory.getBean(name));
		}
		finally {
			RequestContextHolder.setRequestAttributes(null);
		}
	}

	public void testCircleLeadsToException() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(requestAttributes);

		try {
			String name = "requestScopedObjectCircle1";
			assertNull(request.getAttribute(name));
			this.beanFactory.getBean(name);
			fail("Should have thrown BeanCreationException");
		}
		catch (BeanCreationException ex) {
			// expected
			assertTrue(ex.contains(BeanCurrentlyInCreationException.class));
		}
		finally {
			RequestContextHolder.setRequestAttributes(null);
		}
	}

}
