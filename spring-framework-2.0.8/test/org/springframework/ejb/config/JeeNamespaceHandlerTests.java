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

package org.springframework.ejb.config;

import junit.framework.TestCase;

import org.springframework.beans.ITestBean;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean;
import org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean;
import org.springframework.jndi.JndiObjectFactoryBean;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public class JeeNamespaceHandlerTests extends TestCase {

	private DefaultListableBeanFactory beanFactory;

	protected void setUp() throws Exception {
		this.beanFactory = (DefaultListableBeanFactory)
				new ClassPathXmlApplicationContext("jeeNamespaceHandlerTests.xml", getClass()).getBeanFactory();
		this.beanFactory.getBeanNamesForType(ITestBean.class);
	}

	public void testSimpleDefinition() throws Exception {
		RootBeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("simple");
		assertEquals(JndiObjectFactoryBean.class, beanDefinition.getBeanClass());
		assertPropertyValue(beanDefinition, "jndiName", "jdbc/MyDataSource");
		assertNull("Property resourceRef should not have been set", beanDefinition.getPropertyValues().getPropertyValue("resourceRef"));
	}

	public void testComplexDefinition() throws Exception {
		RootBeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("complex");
		assertEquals(JndiObjectFactoryBean.class, beanDefinition.getBeanClass());
		assertPropertyValue(beanDefinition, "jndiName", "jdbc/MyDataSource");
		assertPropertyValue(beanDefinition, "resourceRef", "true");
		assertPropertyValue(beanDefinition, "cache", "true");
		assertPropertyValue(beanDefinition, "lookupOnStartup", "true");
		assertPropertyValue(beanDefinition, "expectedType", "com.myapp.DefaultFoo");
		assertPropertyValue(beanDefinition, "proxyInterface", "com.myapp.Foo");
	}

	public void testWithEnvironment() throws Exception {
		RootBeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("withEnvironment");
		assertPropertyValue(beanDefinition, "jndiEnvironment", "foo=bar");
	}

	public void testWithReferencedEnvironment() throws Exception {
		RootBeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("withReferencedEnvironment");
		assertPropertyValue(beanDefinition, "jndiEnvironment", new RuntimeBeanReference("myEnvironment"));
		assertFalse(beanDefinition.getPropertyValues().contains("environmentRef"));
	}

	public void testSimpleLocalSlsb() throws Exception {
		RootBeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("simpleLocalEjb");
		assertEquals(LocalStatelessSessionProxyFactoryBean.class, beanDefinition.getBeanClass());
		assertPropertyValue(beanDefinition, "businessInterface", ITestBean.class.getName());
		assertPropertyValue(beanDefinition, "jndiName", "ejb/MyLocalBean");
	}

	public void testSimpleRemoteSlsb() throws Exception {
		RootBeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("simpleRemoteEjb");
		assertEquals(SimpleRemoteStatelessSessionProxyFactoryBean.class, beanDefinition.getBeanClass());
		assertPropertyValue(beanDefinition, "businessInterface", ITestBean.class.getName());
		assertPropertyValue(beanDefinition, "jndiName", "ejb/MyRemoteBean");
	}

	public void testComplexLocalSlsb() throws Exception {
		RootBeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("complexLocalEjb");
		assertEquals(LocalStatelessSessionProxyFactoryBean.class, beanDefinition.getBeanClass());
		assertPropertyValue(beanDefinition, "businessInterface", ITestBean.class.getName());
		assertPropertyValue(beanDefinition, "jndiName", "ejb/MyLocalBean");
		assertPropertyValue(beanDefinition, "cacheHome", "true");
		assertPropertyValue(beanDefinition, "lookupHomeOnStartup", "true");
		assertPropertyValue(beanDefinition, "resourceRef", "true");
		assertPropertyValue(beanDefinition, "jndiEnvironment", "foo=bar");
	}

	public void testComplexRemoteSlsb() throws Exception {
		RootBeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("complexRemoteEjb");
		assertEquals(SimpleRemoteStatelessSessionProxyFactoryBean.class, beanDefinition.getBeanClass());
		assertPropertyValue(beanDefinition, "businessInterface", ITestBean.class.getName());
		assertPropertyValue(beanDefinition, "jndiName", "ejb/MyRemoteBean");
		assertPropertyValue(beanDefinition, "cacheHome", "true");
		assertPropertyValue(beanDefinition, "lookupHomeOnStartup", "true");
		assertPropertyValue(beanDefinition, "resourceRef", "true");
		assertPropertyValue(beanDefinition, "jndiEnvironment", "foo=bar");
	}

	private void assertPropertyValue(RootBeanDefinition beanDefinition, String propertyName, Object expectedValue) {
		assertEquals("Property '" + propertyName + "' incorrect",
				expectedValue, beanDefinition.getPropertyValues().getPropertyValue(propertyName).getValue());
	}

}
