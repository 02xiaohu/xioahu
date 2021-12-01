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

package org.springframework.jdbc.core.namedparam;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.AssertThrows;

/**
 * @author Thomas Risberg
 * @author Juergen Hoeller
 * @author Rick Evans
 */
public class NamedParameterUtilsTests extends TestCase {

	public void testParseSql() {
		String sql = "xxx :a yyyy :b :c :a zzzzz";
		String sql2 = "xxx &a yyyy ? zzzzz";
		String sql3 = "xxx &a+:b" + '\t' + ":c%10 yyyy ? zzzzz";
		ParsedSql psql = NamedParameterUtils.parseSqlStatement(sql);
		assertEquals("xxx ? yyyy ? ? ? zzzzz", NamedParameterUtils.substituteNamedParameters(psql, null));
		assertEquals("a", psql.getParameterNames().get(0));
		assertEquals("c", psql.getParameterNames().get(2));
		assertEquals("a", psql.getParameterNames().get(3));
		assertEquals(4, psql.getTotalParameterCount());
		assertEquals(3, psql.getNamedParameterCount());
		ParsedSql psql2 = NamedParameterUtils.parseSqlStatement(sql2);
		assertEquals("xxx ? yyyy ? zzzzz", NamedParameterUtils.substituteNamedParameters(psql2, null));
		assertEquals("a", psql2.getParameterNames().get(0));
		assertEquals(2, psql2.getTotalParameterCount());
		assertEquals(1, psql2.getNamedParameterCount());
		ParsedSql psql3 = NamedParameterUtils.parseSqlStatement(sql3);
		assertEquals("a", psql3.getParameterNames().get(0));
		assertEquals("b", psql3.getParameterNames().get(1));
		assertEquals("c", psql3.getParameterNames().get(2));
	}

	public void testSubstituteNamedParameters() {
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("a", "a").addValue("b", "b").addValue("c", "c");
		assertEquals("xxx ? ? ?", NamedParameterUtils.substituteNamedParameters("xxx :a :b :c", namedParams));
		assertEquals("xxx ? ? ? xx ? ?", NamedParameterUtils.substituteNamedParameters("xxx :a :b :c xx :a :a", namedParams));
	}

	public void testConvertParamMapToArray() {
		Map paramMap = new HashMap();
		paramMap.put("a", "a");
		paramMap.put("b", "b");
		paramMap.put("c", "c");
		assertTrue(3 == NamedParameterUtils.buildValueArray("xxx :a :b :c", paramMap).length);
		assertTrue(5 == NamedParameterUtils.buildValueArray("xxx :a :b :c xx :a :b", paramMap).length);
		assertTrue(5 == NamedParameterUtils.buildValueArray("xxx :a :a :a xx :a :a", paramMap).length);
		assertEquals("b", NamedParameterUtils.buildValueArray("xxx :a :b :c xx :a :b", paramMap)[4]);
		try {
			NamedParameterUtils.buildValueArray("xxx :a :b ?", paramMap);
			fail("mixed named parameters and ? placeholders not detected");
		}
		catch (InvalidDataAccessApiUsageException expected) {
		}
	}

	public void testConvertTypeMapToArray() {
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("a", "a", 1).addValue("b", "b", 2).addValue("c", "c", 3);
		assertTrue(3 == NamedParameterUtils.buildSqlTypeArray(NamedParameterUtils.parseSqlStatement("xxx :a :b :c"), namedParams).length);
		assertTrue(5 == NamedParameterUtils.buildSqlTypeArray(NamedParameterUtils.parseSqlStatement("xxx :a :b :c xx :a :b"), namedParams).length);
		assertTrue(5 == NamedParameterUtils.buildSqlTypeArray(NamedParameterUtils.parseSqlStatement("xxx :a :a :a xx :a :a"), namedParams).length);
		assertEquals(2, NamedParameterUtils.buildSqlTypeArray(NamedParameterUtils.parseSqlStatement("xxx :a :b :c xx :a :b"), namedParams)[4]);
	}

	public void testBuildValueArrayWithMissingParameterValue() throws Exception {
		new AssertThrows(InvalidDataAccessApiUsageException.class) {
			public void test() throws Exception {
				String sql = "select count(0) from foo where id = :id";
				NamedParameterUtils.buildValueArray(sql, new HashMap());
			}
		}.runTest();
	}

	public void testSubstituteNamedParametersWithStringContainingQuotes() throws Exception {
		String expectedSql = "select 'first name' from artists where id = ? and quote = 'exsqueeze me?'";
		String sql = "select 'first name' from artists where id = :id and quote = 'exsqueeze me?'";
		String newSql = NamedParameterUtils.substituteNamedParameters(sql, new MapSqlParameterSource());
		assertEquals(expectedSql, newSql);
	}

	public void testParseSqlStatementWithStringContainingQuotes() throws Exception {
		String expectedSql = "select 'first name' from artists where id = ? and quote = 'exsqueeze me?'";
		String sql = "select 'first name' from artists where id = :id and quote = 'exsqueeze me?'";
		ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
		assertEquals(expectedSql, NamedParameterUtils.substituteNamedParameters(parsedSql, null));
	}

	/*
	 * SPR-2544
	 */
	public void testParseSqlStatementWithLogicalAnd() {
		String expectedSql = "xxx & yyyy";
		ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(expectedSql);
		assertEquals(expectedSql, NamedParameterUtils.substituteNamedParameters(parsedSql, null));
	}

	/*
	 * SPR-2544
	 */
	public void testSubstituteNamedParametersWithLogicalAnd() throws Exception {
		String expectedSql = "xxx & yyyy";
		String newSql = NamedParameterUtils.substituteNamedParameters(expectedSql, new MapSqlParameterSource());
		assertEquals(expectedSql, newSql);
	}

	/*
	 * SPR-3173
	 */
	public void testVariableAssignmentOperator() throws Exception {
		String expectedSql = "x := 1";
		String newSql = NamedParameterUtils.substituteNamedParameters(expectedSql, new MapSqlParameterSource());
		assertEquals(expectedSql, newSql);
	}

}
