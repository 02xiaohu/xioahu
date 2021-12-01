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

package org.springframework.beans;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * @author Juergen Hoeller
 * @since 18.01.2006
 */
public class BeanWrapperGenericsTests extends TestCase {

	public void testGenericSet() {
		GenericBean gb = new GenericBean();
		BeanWrapper bw = new BeanWrapperImpl(gb);
		Set input = new HashSet();
		input.add("4");
		input.add("5");
		bw.setPropertyValue("integerSet", input);
		assertTrue(gb.getIntegerSet().contains(new Integer(4)));
		assertTrue(gb.getIntegerSet().contains(new Integer(5)));
	}

	public void testGenericSetWithConversionFailure() {
		GenericBean gb = new GenericBean();
		BeanWrapper bw = new BeanWrapperImpl(gb);
		Set input = new HashSet();
		input.add(new TestBean());
		try {
			bw.setPropertyValue("integerSet", input);
			fail("Should have thrown TypeMismatchException");
		}
		catch (TypeMismatchException ex) {
			assertTrue(ex.getMessage().indexOf("java.lang.Integer") != -1);
		}
	}

	public void testGenericList() throws MalformedURLException {
		GenericBean gb = new GenericBean();
		BeanWrapper bw = new BeanWrapperImpl(gb);
		List input = new ArrayList();
		input.add("http://localhost:8080");
		input.add("http://localhost:9090");
		bw.setPropertyValue("resourceList", input);
		assertEquals(new UrlResource("http://localhost:8080"), gb.getResourceList().get(0));
		assertEquals(new UrlResource("http://localhost:9090"), gb.getResourceList().get(1));
	}

	public void testGenericListElement() throws MalformedURLException {
		GenericBean gb = new GenericBean();
		gb.setResourceList(new ArrayList<Resource>());
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("resourceList[0]", "http://localhost:8080");
		assertEquals(new UrlResource("http://localhost:8080"), gb.getResourceList().get(0));
	}

	public void testGenericMap() {
		GenericBean gb = new GenericBean();
		BeanWrapper bw = new BeanWrapperImpl(gb);
		Map input = new HashMap();
		input.put("4", "5");
		input.put("6", "7");
		bw.setPropertyValue("shortMap", input);
		assertEquals(new Integer(5), gb.getShortMap().get(new Short("4")));
		assertEquals(new Integer(7), gb.getShortMap().get(new Short("6")));
	}

	public void testGenericMapElement() {
		GenericBean gb = new GenericBean();
		gb.setShortMap(new HashMap<Short, Integer>());
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("shortMap[4]", "5");
		assertEquals(new Integer(5), bw.getPropertyValue("shortMap[4]"));
		assertEquals(new Integer(5), gb.getShortMap().get(new Short("4")));
	}

	public void testGenericMapWithKeyType() {
		GenericBean gb = new GenericBean();
		BeanWrapper bw = new BeanWrapperImpl(gb);
		Map input = new HashMap();
		input.put("4", "5");
		input.put("6", "7");
		bw.setPropertyValue("longMap", input);
		assertEquals("5", gb.getLongMap().get(new Long("4")));
		assertEquals("7", gb.getLongMap().get(new Long("6")));
	}

	public void testGenericMapElementWithKeyType() {
		GenericBean gb = new GenericBean();
		gb.setLongMap(new HashMap<Long, Integer>());
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("longMap[4]", "5");
		assertEquals("5", gb.getLongMap().get(new Long("4")));
		assertEquals("5", bw.getPropertyValue("longMap[4]"));
	}

	public void testGenericMapWithCollectionValue() {
		GenericBean gb = new GenericBean();
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.registerCustomEditor(Number.class, new CustomNumberEditor(Integer.class, false));
		Map input = new HashMap();
		HashSet value1 = new HashSet();
		value1.add(new Integer(1));
		input.put("1", value1);
		ArrayList value2 = new ArrayList();
		value2.add(Boolean.TRUE);
		input.put("2", value2);
		bw.setPropertyValue("collectionMap", input);
		assertTrue(gb.getCollectionMap().get(new Integer(1)) instanceof HashSet);
		assertTrue(gb.getCollectionMap().get(new Integer(2)) instanceof ArrayList);
	}

	public void testGenericMapElementWithCollectionValue() {
		GenericBean gb = new GenericBean();
		gb.setCollectionMap(new HashMap<Number, Collection<? extends Object>>());
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.registerCustomEditor(Number.class, new CustomNumberEditor(Integer.class, false));
		HashSet value1 = new HashSet();
		value1.add(new Integer(1));
		bw.setPropertyValue("collectionMap[1]", value1);
		assertTrue(gb.getCollectionMap().get(new Integer(1)) instanceof HashSet);
	}

	public void testGenericListOfLists() throws MalformedURLException {
		GenericBean gb = new GenericBean();
		List<List<Integer>> list = new LinkedList<List<Integer>>();
		list.add(new LinkedList<Integer>());
		gb.setListOfLists(list);
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("listOfLists[0][0]", new Integer(5));
		assertEquals(new Integer(5), bw.getPropertyValue("listOfLists[0][0]"));
		assertEquals(new Integer(5), gb.getListOfLists().get(0).get(0));
	}

	public void testGenericListOfListsWithElementConversion() throws MalformedURLException {
		GenericBean gb = new GenericBean();
		List<List<Integer>> list = new LinkedList<List<Integer>>();
		list.add(new LinkedList<Integer>());
		gb.setListOfLists(list);
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("listOfLists[0][0]", "5");
		assertEquals(new Integer(5), bw.getPropertyValue("listOfLists[0][0]"));
		assertEquals(new Integer(5), gb.getListOfLists().get(0).get(0));
	}

	public void testGenericListOfArrays() throws MalformedURLException {
		GenericBean gb = new GenericBean();
		List<String[]> list = new LinkedList<String[]>();
		list.add(new String[] {"str1", "str2"});
		gb.setListOfArrays(list);
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("listOfArrays[0][1]", "str3 ");
		assertEquals("str3 ", bw.getPropertyValue("listOfArrays[0][1]"));
		assertEquals("str3 ", gb.getListOfArrays().get(0)[1]);
	}

	public void testGenericListOfArraysWithElementConversion() throws MalformedURLException {
		GenericBean gb = new GenericBean();
		List<String[]> list = new LinkedList<String[]>();
		list.add(new String[] {"str1", "str2"});
		gb.setListOfArrays(list);
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.registerCustomEditor(String.class, new StringTrimmerEditor(false));
		bw.setPropertyValue("listOfArrays[0][1]", "str3 ");
		assertEquals("str3", bw.getPropertyValue("listOfArrays[0][1]"));
		assertEquals("str3", gb.getListOfArrays().get(0)[1]);
	}

	public void testGenericListOfMaps() throws MalformedURLException {
		GenericBean gb = new GenericBean();
		List<Map<Integer, Long>> list = new LinkedList<Map<Integer, Long>>();
		list.add(new HashMap<Integer, Long>());
		gb.setListOfMaps(list);
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("listOfMaps[0][10]", new Long(5));
		assertEquals(new Long(5), bw.getPropertyValue("listOfMaps[0][10]"));
		assertEquals(new Long(5), gb.getListOfMaps().get(0).get(10));
	}

	public void testGenericListOfMapsWithElementConversion() throws MalformedURLException {
		GenericBean gb = new GenericBean();
		List<Map<Integer, Long>> list = new LinkedList<Map<Integer, Long>>();
		list.add(new HashMap<Integer, Long>());
		gb.setListOfMaps(list);
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("listOfMaps[0][10]", "5");
		assertEquals(new Long(5), bw.getPropertyValue("listOfMaps[0][10]"));
		assertEquals(new Long(5), gb.getListOfMaps().get(0).get(10));
	}

	public void testGenericMapOfMaps() throws MalformedURLException {
		GenericBean gb = new GenericBean();
		Map<String, Map<Integer, Long>> map = new HashMap<String, Map<Integer, Long>>();
		map.put("mykey", new HashMap<Integer, Long>());
		gb.setMapOfMaps(map);
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("mapOfMaps[mykey][10]", new Long(5));
		assertEquals(new Long(5), bw.getPropertyValue("mapOfMaps[mykey][10]"));
		assertEquals(new Long(5), gb.getMapOfMaps().get("mykey").get(10));
	}

	public void testGenericMapOfMapsWithElementConversion() throws MalformedURLException {
		GenericBean gb = new GenericBean();
		Map<String, Map<Integer, Long>> map = new HashMap<String, Map<Integer, Long>>();
		map.put("mykey", new HashMap<Integer, Long>());
		gb.setMapOfMaps(map);
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("mapOfMaps[mykey][10]", "5");
		assertEquals(new Long(5), bw.getPropertyValue("mapOfMaps[mykey][10]"));
		assertEquals(new Long(5), gb.getMapOfMaps().get("mykey").get(10));
	}

	public void testGenericMapOfLists() throws MalformedURLException {
		GenericBean gb = new GenericBean();
		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		map.put(new Integer(1), new LinkedList<Integer>());
		gb.setMapOfLists(map);
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("mapOfLists[1][0]", new Integer(5));
		assertEquals(new Integer(5), bw.getPropertyValue("mapOfLists[1][0]"));
		assertEquals(new Integer(5), gb.getMapOfLists().get(new Integer(1)).get(0));
	}

	public void testGenericMapOfListsWithElementConversion() throws MalformedURLException {
		GenericBean gb = new GenericBean();
		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		map.put(new Integer(1), new LinkedList<Integer>());
		gb.setMapOfLists(map);
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("mapOfLists[1][0]", "5");
		assertEquals(new Integer(5), bw.getPropertyValue("mapOfLists[1][0]"));
		assertEquals(new Integer(5), gb.getMapOfLists().get(new Integer(1)).get(0));
	}

	public void testGenericTypeNestingMapOfInteger() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("testKey", "100");

		NestedGenericCollectionBean gb = new NestedGenericCollectionBean();
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("mapOfInteger", map);

		Object obj = gb.getMapOfInteger().get("testKey");
		assertTrue(obj instanceof Integer);
	}

	public void testGenericTypeNestingMapOfListOfInteger() throws Exception {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = Arrays.asList(new String[] {"1", "2", "3"});
		map.put("testKey", list);

		NestedGenericCollectionBean gb = new NestedGenericCollectionBean();
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("mapOfListOfInteger", map);

		Object obj = gb.getMapOfListOfInteger().get("testKey").get(0);
		assertTrue(obj instanceof Integer);
		assertEquals(1, ((Integer) obj).intValue());
	}

	public void testGenericTypeNestingListOfMapOfInteger() throws Exception {
		List<Map<String, String>> list = new LinkedList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("testKey", "5");
		list.add(map);

		NestedGenericCollectionBean gb = new NestedGenericCollectionBean();
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("listOfMapOfInteger", list);

		Object obj = gb.getListOfMapOfInteger().get(0).get("testKey");
		assertTrue(obj instanceof Integer);
		assertEquals(5, ((Integer) obj).intValue());
	}

	public void testGenericTypeNestingMapOfListOfListOfInteger() throws Exception {
		Map<String, List<List<String>>> map = new HashMap<String, List<List<String>>>();
		List<String> list = Arrays.asList(new String[] {"1", "2", "3"});
		map.put("testKey", Collections.singletonList(list));

		NestedGenericCollectionBean gb = new NestedGenericCollectionBean();
		BeanWrapper bw = new BeanWrapperImpl(gb);
		bw.setPropertyValue("mapOfListOfListOfInteger", map);

		Object obj = gb.getMapOfListOfListOfInteger().get("testKey").get(0).get(0);
		assertTrue(obj instanceof Integer);
		assertEquals(1, ((Integer) obj).intValue());
	}

	public void testComplexGenericMap() {
		Map inputMap = new HashMap();
		List inputKey = new LinkedList();
		inputKey.add("1");
		List inputValue = new LinkedList();
		inputValue.add("10");
		inputMap.put(inputKey, inputValue);

		ComplexMapHolder holder = new ComplexMapHolder();
		BeanWrapper bw = new BeanWrapperImpl(holder);
		bw.setPropertyValue("genericMap", inputMap);

		assertEquals(new Integer(1), holder.getGenericMap().keySet().iterator().next().get(0));
		assertEquals(new Long(10), holder.getGenericMap().values().iterator().next().get(0));
	}

	public void testComplexGenericMapWithCollectionConversion() {
		Map inputMap = new HashMap();
		Set inputKey = new HashSet();
		inputKey.add("1");
		Set inputValue = new HashSet();
		inputValue.add("10");
			inputMap.put(inputKey, inputValue);

		ComplexMapHolder holder = new ComplexMapHolder();
		BeanWrapper bw = new BeanWrapperImpl(holder);
		bw.setPropertyValue("genericMap", inputMap);

		assertEquals(new Integer(1), holder.getGenericMap().keySet().iterator().next().get(0));
		assertEquals(new Long(10), holder.getGenericMap().values().iterator().next().get(0));
	}

	public void testComplexGenericIndexedMapEntry() {
		List inputValue = new LinkedList();
		inputValue.add("10");

		ComplexMapHolder holder = new ComplexMapHolder();
		BeanWrapper bw = new BeanWrapperImpl(holder);
		bw.setPropertyValue("genericIndexedMap[1]", inputValue);

		assertEquals(new Integer(1), holder.getGenericIndexedMap().keySet().iterator().next());
		assertEquals(new Long(10), holder.getGenericIndexedMap().values().iterator().next().get(0));
	}

	public void testComplexGenericIndexedMapEntryWithCollectionConversion() {
		Set inputValue = new HashSet();
		inputValue.add("10");

		ComplexMapHolder holder = new ComplexMapHolder();
		BeanWrapper bw = new BeanWrapperImpl(holder);
		bw.setPropertyValue("genericIndexedMap[1]", inputValue);

		assertEquals(new Integer(1), holder.getGenericIndexedMap().keySet().iterator().next());
		assertEquals(new Long(10), holder.getGenericIndexedMap().values().iterator().next().get(0));
	}

	public void testComplexDerivedIndexedMapEntry() {
		List inputValue = new LinkedList();
		inputValue.add("10");

		ComplexMapHolder holder = new ComplexMapHolder();
		BeanWrapper bw = new BeanWrapperImpl(holder);
		bw.setPropertyValue("derivedIndexedMap[1]", inputValue);

		assertEquals(new Integer(1), holder.getDerivedIndexedMap().keySet().iterator().next());
		assertEquals(new Long(10), holder.getDerivedIndexedMap().values().iterator().next().get(0));
	}

	public void testComplexDerivedIndexedMapEntryWithCollectionConversion() {
		Set inputValue = new HashSet();
		inputValue.add("10");

		ComplexMapHolder holder = new ComplexMapHolder();
		BeanWrapper bw = new BeanWrapperImpl(holder);
		bw.setPropertyValue("derivedIndexedMap[1]", inputValue);

		assertEquals(new Integer(1), holder.getDerivedIndexedMap().keySet().iterator().next());
		assertEquals(new Long(10), holder.getDerivedIndexedMap().values().iterator().next().get(0));
	}


	private static class NestedGenericCollectionBean {

		private Map<String, Integer> mapOfInteger;

		private Map<String, List<Integer>> mapOfListOfInteger;

		private List<Map<String, Integer>> listOfMapOfInteger;

		private Map<String, List<List<Integer>>> mapOfListOfListOfInteger;

		public Map<String, Integer> getMapOfInteger() {
			return mapOfInteger;
		}

		public void setMapOfInteger(Map<String, Integer> mapOfInteger) {
			this.mapOfInteger = mapOfInteger;
		}

		public Map<String, List<Integer>> getMapOfListOfInteger() {
			return mapOfListOfInteger;
		}

		public void setMapOfListOfInteger(Map<String, List<Integer>> mapOfListOfInteger) {
			this.mapOfListOfInteger = mapOfListOfInteger;
		}

		public List<Map<String, Integer>> getListOfMapOfInteger() {
			return listOfMapOfInteger;
		}

		public void setListOfMapOfInteger(List<Map<String, Integer>> listOfMapOfInteger) {
			this.listOfMapOfInteger = listOfMapOfInteger;
		}

		public Map<String, List<List<Integer>>> getMapOfListOfListOfInteger() {
			return mapOfListOfListOfInteger;
		}

		public void setMapOfListOfListOfInteger(Map<String, List<List<Integer>>> mapOfListOfListOfInteger) {
			this.mapOfListOfListOfInteger = mapOfListOfListOfInteger;
		}
	}


	private static class ComplexMapHolder {

		private Map<List<Integer>, List<Long>> genericMap;

		private Map<Integer, List<Long>> genericIndexedMap = new HashMap<Integer, List<Long>>();

		private DerivedMap derivedIndexedMap = new DerivedMap();

		public void setGenericMap(Map<List<Integer>, List<Long>> genericMap) {
			this.genericMap = genericMap;
		}

		public Map<List<Integer>, List<Long>> getGenericMap() {
			return genericMap;
		}

		public void setGenericIndexedMap(Map<Integer, List<Long>> genericIndexedMap) {
			this.genericIndexedMap = genericIndexedMap;
		}

		public Map<Integer, List<Long>> getGenericIndexedMap() {
			return genericIndexedMap;
		}

		public void setDerivedIndexedMap(DerivedMap derivedIndexedMap) {
			this.derivedIndexedMap = derivedIndexedMap;
		}

		public DerivedMap getDerivedIndexedMap() {
			return derivedIndexedMap;
		}
	}


	private static class DerivedMap extends HashMap<Integer, List<Long>> {

	}

}
