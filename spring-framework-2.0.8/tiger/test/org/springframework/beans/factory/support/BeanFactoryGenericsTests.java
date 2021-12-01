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

package org.springframework.beans.factory.support;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.springframework.beans.GenericBean;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;

/**
 * @author Juergen Hoeller
 * @since 20.01.2006
 */
public class BeanFactoryGenericsTests extends TestCase {

	public void testGenericSetProperty() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);

		Set input = new HashSet();
		input.add("4");
		input.add("5");
		rbd.getPropertyValues().addPropertyValue("integerSet", input);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertTrue(gb.getIntegerSet().contains(new Integer(4)));
		assertTrue(gb.getIntegerSet().contains(new Integer(5)));
	}

	public void testGenericListProperty() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);

		List input = new ArrayList();
		input.add("http://localhost:8080");
		input.add("http://localhost:9090");
		rbd.getPropertyValues().addPropertyValue("resourceList", input);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertEquals(new UrlResource("http://localhost:8080"), gb.getResourceList().get(0));
		assertEquals(new UrlResource("http://localhost:9090"), gb.getResourceList().get(1));
	}

	public void testGenericMapProperty() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);

		Map input = new HashMap();
		input.put("4", "5");
		input.put("6", "7");
		rbd.getPropertyValues().addPropertyValue("shortMap", input);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertEquals(new Integer(5), gb.getShortMap().get(new Short("4")));
		assertEquals(new Integer(7), gb.getShortMap().get(new Short("6")));
	}

	public void testGenericListOfArraysProperty() throws MalformedURLException {
		XmlBeanFactory bf = new XmlBeanFactory(new ClassPathResource("genericBeanTests.xml", getClass()));
		GenericBean gb = (GenericBean) bf.getBean("listOfArrays");

		assertEquals(1, gb.getListOfArrays().size());
		String[] array = gb.getListOfArrays().get(0);
		assertEquals(2, array.length);
		assertEquals("value1", array[0]);
		assertEquals("value2", array[1]);
	}


	public void testGenericSetConstructor() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);

		Set input = new HashSet();
		input.add("4");
		input.add("5");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertTrue(gb.getIntegerSet().contains(new Integer(4)));
		assertTrue(gb.getIntegerSet().contains(new Integer(5)));
	}

	public void testGenericSetListConstructor() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);

		Set input = new HashSet();
		input.add("4");
		input.add("5");
		List input2 = new ArrayList();
		input2.add("http://localhost:8080");
		input2.add("http://localhost:9090");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input2);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertTrue(gb.getIntegerSet().contains(new Integer(4)));
		assertTrue(gb.getIntegerSet().contains(new Integer(5)));
		assertEquals(new UrlResource("http://localhost:8080"), gb.getResourceList().get(0));
		assertEquals(new UrlResource("http://localhost:9090"), gb.getResourceList().get(1));
	}

	public void testGenericSetMapConstructor() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);

		Set input = new HashSet();
		input.add("4");
		input.add("5");
		Map input2 = new HashMap();
		input2.put("4", "5");
		input2.put("6", "7");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input2);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertTrue(gb.getIntegerSet().contains(new Integer(4)));
		assertTrue(gb.getIntegerSet().contains(new Integer(5)));
		assertEquals(new Integer(5), gb.getShortMap().get(new Short("4")));
		assertEquals(new Integer(7), gb.getShortMap().get(new Short("6")));
	}

	public void testGenericMapResourceConstructor() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);

		Map input = new HashMap();
		input.put("4", "5");
		input.put("6", "7");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);
		rbd.getConstructorArgumentValues().addGenericArgumentValue("http://localhost:8080");

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertEquals(new Integer(5), gb.getShortMap().get(new Short("4")));
		assertEquals(new Integer(7), gb.getShortMap().get(new Short("6")));
		assertEquals(new UrlResource("http://localhost:8080"), gb.getResourceList().get(0));
	}

	public void testGenericMapMapConstructor() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);

		Map input = new HashMap();
		input.put("1", "0");
		input.put("2", "3");
		Map input2 = new HashMap();
		input2.put("4", "5");
		input2.put("6", "7");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input2);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertNotSame(gb.getPlainMap(), gb.getShortMap());
		assertEquals(2, gb.getPlainMap().size());
		assertEquals("0", gb.getPlainMap().get("1"));
		assertEquals("3", gb.getPlainMap().get("2"));
		assertEquals(2, gb.getShortMap().size());
		assertEquals(new Integer(5), gb.getShortMap().get(new Short("4")));
		assertEquals(new Integer(7), gb.getShortMap().get(new Short("6")));
	}

	public void testGenericMapMapConstructorWithSameRefAndConversion() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);

		Map input = new HashMap();
		input.put("1", "0");
		input.put("2", "3");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertNotSame(gb.getPlainMap(), gb.getShortMap());
		assertEquals(2, gb.getPlainMap().size());
		assertEquals("0", gb.getPlainMap().get("1"));
		assertEquals("3", gb.getPlainMap().get("2"));
		assertEquals(2, gb.getShortMap().size());
		assertEquals(new Integer(0), gb.getShortMap().get(new Short("1")));
		assertEquals(new Integer(3), gb.getShortMap().get(new Short("2")));
	}

	public void testGenericMapMapConstructorWithSameRefAndNoConversion() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);

		Map input = new HashMap();
		input.put(new Short((short) 1), new Integer(0));
		input.put(new Short((short) 2), new Integer(3));
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertSame(gb.getPlainMap(), gb.getShortMap());
		assertEquals(2, gb.getShortMap().size());
		assertEquals(new Integer(0), gb.getShortMap().get(new Short("1")));
		assertEquals(new Integer(3), gb.getShortMap().get(new Short("2")));
	}

	public void testGenericMapWithKeyTypeConstructor() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);

		Map input = new HashMap();
		input.put("4", "5");
		input.put("6", "7");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertEquals("5", gb.getLongMap().get(new Long("4")));
		assertEquals("7", gb.getLongMap().get(new Long("6")));
	}

	public void testGenericMapWithCollectionValueConstructor() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		bf.registerCustomEditor(Number.class, new CustomNumberEditor(Integer.class, false));
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);

		Map input = new HashMap();
		HashSet value1 = new HashSet();
		value1.add(new Integer(1));
		input.put("1", value1);
		ArrayList value2 = new ArrayList();
		value2.add(Boolean.TRUE);
		input.put("2", value2);
		rbd.getConstructorArgumentValues().addGenericArgumentValue(Boolean.TRUE);
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertTrue(gb.getCollectionMap().get(new Integer(1)) instanceof HashSet);
		assertTrue(gb.getCollectionMap().get(new Integer(2)) instanceof ArrayList);
	}


	public void testGenericSetFactoryMethod() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);
		rbd.setFactoryMethodName("createInstance");

		Set input = new HashSet();
		input.add("4");
		input.add("5");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertTrue(gb.getIntegerSet().contains(new Integer(4)));
		assertTrue(gb.getIntegerSet().contains(new Integer(5)));
	}

	public void testGenericSetListFactoryMethod() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);
		rbd.setFactoryMethodName("createInstance");

		Set input = new HashSet();
		input.add("4");
		input.add("5");
		List input2 = new ArrayList();
		input2.add("http://localhost:8080");
		input2.add("http://localhost:9090");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input2);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertTrue(gb.getIntegerSet().contains(new Integer(4)));
		assertTrue(gb.getIntegerSet().contains(new Integer(5)));
		assertEquals(new UrlResource("http://localhost:8080"), gb.getResourceList().get(0));
		assertEquals(new UrlResource("http://localhost:9090"), gb.getResourceList().get(1));
	}

	public void testGenericSetMapFactoryMethod() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);
		rbd.setFactoryMethodName("createInstance");

		Set input = new HashSet();
		input.add("4");
		input.add("5");
		Map input2 = new HashMap();
		input2.put("4", "5");
		input2.put("6", "7");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input2);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertTrue(gb.getIntegerSet().contains(new Integer(4)));
		assertTrue(gb.getIntegerSet().contains(new Integer(5)));
		assertEquals(new Integer(5), gb.getShortMap().get(new Short("4")));
		assertEquals(new Integer(7), gb.getShortMap().get(new Short("6")));
	}

	public void testGenericMapResourceFactoryMethod() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);
		rbd.setFactoryMethodName("createInstance");

		Map input = new HashMap();
		input.put("4", "5");
		input.put("6", "7");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);
		rbd.getConstructorArgumentValues().addGenericArgumentValue("http://localhost:8080");

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertEquals(new Integer(5), gb.getShortMap().get(new Short("4")));
		assertEquals(new Integer(7), gb.getShortMap().get(new Short("6")));
		assertEquals(new UrlResource("http://localhost:8080"), gb.getResourceList().get(0));
	}

	public void testGenericMapMapFactoryMethod() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);
		rbd.setFactoryMethodName("createInstance");

		Map input = new HashMap();
		input.put("1", "0");
		input.put("2", "3");
		Map input2 = new HashMap();
		input2.put("4", "5");
		input2.put("6", "7");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input2);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertEquals("0", gb.getPlainMap().get("1"));
		assertEquals("3", gb.getPlainMap().get("2"));
		assertEquals(new Integer(5), gb.getShortMap().get(new Short("4")));
		assertEquals(new Integer(7), gb.getShortMap().get(new Short("6")));
	}

	public void testGenericMapWithKeyTypeFactoryMethod() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);
		rbd.setFactoryMethodName("createInstance");

		Map input = new HashMap();
		input.put("4", "5");
		input.put("6", "7");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertEquals("5", gb.getLongMap().get(new Long("4")));
		assertEquals("7", gb.getLongMap().get(new Long("6")));
	}

	public void testGenericMapWithCollectionValueFactoryMethod() throws MalformedURLException {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		bf.registerCustomEditor(Number.class, new CustomNumberEditor(Integer.class, false));
		RootBeanDefinition rbd = new RootBeanDefinition(GenericBean.class);
		rbd.setFactoryMethodName("createInstance");

		Map input = new HashMap();
		HashSet value1 = new HashSet();
		value1.add(new Integer(1));
		input.put("1", value1);
		ArrayList value2 = new ArrayList();
		value2.add(Boolean.TRUE);
		input.put("2", value2);
		rbd.getConstructorArgumentValues().addGenericArgumentValue(Boolean.TRUE);
		rbd.getConstructorArgumentValues().addGenericArgumentValue(input);

		bf.registerBeanDefinition("genericBean", rbd);
		GenericBean gb = (GenericBean) bf.getBean("genericBean");

		assertTrue(gb.getCollectionMap().get(new Integer(1)) instanceof HashSet);
		assertTrue(gb.getCollectionMap().get(new Integer(2)) instanceof ArrayList);
	}

	public void testGenericListBean() throws Exception {
		XmlBeanFactory bf = new XmlBeanFactory(new ClassPathResource("genericBeanTests.xml", getClass()));
		List list = (List) bf.getBean("list");
		assertEquals(1, list.size());
		assertEquals(new URL("http://localhost:8080"), list.get(0));
	}

	public void testGenericSetBean() throws Exception {
		XmlBeanFactory bf = new XmlBeanFactory(new ClassPathResource("genericBeanTests.xml", getClass()));
		Set set = (Set) bf.getBean("set");
		assertEquals(1, set.size());
		assertEquals(new URL("http://localhost:8080"), set.iterator().next());
	}

	public void testGenericMapBean() throws Exception {
		XmlBeanFactory bf = new XmlBeanFactory(new ClassPathResource("genericBeanTests.xml", getClass()));
		Map map = (Map) bf.getBean("map");
		assertEquals(1, map.size());
		assertEquals(new Integer(10), map.keySet().iterator().next());
		assertEquals(new URL("http://localhost:8080"), map.values().iterator().next());
	}


	public static class NamedUrlList extends LinkedList<URL> {
	}


	public static class NamedUrlSet extends HashSet<URL> {
	}


	public static class NamedUrlMap extends HashMap<Integer, URL> {
	}


	public static class CollectionDependentBean {

		public CollectionDependentBean(NamedUrlList list, NamedUrlSet set, NamedUrlMap map) {
			assertEquals(1, list.size());
			assertEquals(1, set.size());
			assertEquals(1, map.size());
		}
	}

}
