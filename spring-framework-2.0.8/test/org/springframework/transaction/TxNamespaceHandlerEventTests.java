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

package org.springframework.transaction;

import junit.framework.TestCase;

import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CollectingReaderEventListener;
import org.springframework.beans.factory.parsing.ComponentDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Torsten Juergeleit
 * @author Juergen Hoeller
 */
public class TxNamespaceHandlerEventTests extends TestCase {

	private CollectingReaderEventListener eventListener = new CollectingReaderEventListener();

	private XmlBeanDefinitionReader reader;

	private DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();


	public void setUp() throws Exception {
		this.reader = new XmlBeanDefinitionReader(this.beanFactory);
		this.reader.setEventListener(this.eventListener);
		this.reader.loadBeanDefinitions(new ClassPathResource("txNamespaceHandlerTests.xml", getClass()));
	}

	public void testComponentEventReceived() {
		ComponentDefinition component = this.eventListener.getComponentDefinition("txAdvice");
		assertTrue(component instanceof BeanComponentDefinition);
	}

}
