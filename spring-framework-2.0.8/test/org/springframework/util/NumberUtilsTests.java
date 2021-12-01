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

package org.springframework.util;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

import junit.framework.TestCase;

/**
 * @author Rob Harrop
 */
public class NumberUtilsTests extends TestCase {

	public void testParseNumber() {
		String aByte = "" + Byte.MAX_VALUE;
		String aShort = "" + Short.MAX_VALUE;
		String anInteger = "" + Integer.MAX_VALUE;
		String aLong = "" + Long.MAX_VALUE;
		String aFloat = "" + Float.MAX_VALUE;
		String aDouble = "" + Double.MAX_VALUE;

		assertEquals("Byte did not parse", new Byte(Byte.MAX_VALUE), NumberUtils.parseNumber(aByte, Byte.class));
		assertEquals("Short did not parse", new Short(Short.MAX_VALUE), NumberUtils.parseNumber(aShort, Short.class));
		assertEquals("Integer did not parse", new Integer(Integer.MAX_VALUE), NumberUtils.parseNumber(anInteger, Integer.class));
		assertEquals("Long did not parse", new Long(Long.MAX_VALUE), NumberUtils.parseNumber(aLong, Long.class));
		assertEquals("Float did not parse", new Float(Float.MAX_VALUE), NumberUtils.parseNumber(aFloat, Float.class));
		assertEquals("Double did not parse", new Double(Double.MAX_VALUE), NumberUtils.parseNumber(aDouble, Double.class));
	}

	public void testParseNumberUsingNumberFormat() {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		String aByte = "" + Byte.MAX_VALUE;
		String aShort = "" + Short.MAX_VALUE;
		String anInteger = "" + Integer.MAX_VALUE;
		String aLong = "" + Long.MAX_VALUE;
		String aFloat = "" + Float.MAX_VALUE;
		String aDouble = "" + Double.MAX_VALUE;

		assertEquals("Byte did not parse", new Byte(Byte.MAX_VALUE), NumberUtils.parseNumber(aByte, Byte.class, nf));
		assertEquals("Short did not parse", new Short(Short.MAX_VALUE), NumberUtils.parseNumber(aShort, Short.class, nf));
		assertEquals("Integer did not parse", new Integer(Integer.MAX_VALUE), NumberUtils.parseNumber(anInteger, Integer.class, nf));
		assertEquals("Long did not parse", new Long(Long.MAX_VALUE), NumberUtils.parseNumber(aLong, Long.class, nf));
		assertEquals("Float did not parse", new Float(Float.MAX_VALUE), NumberUtils.parseNumber(aFloat, Float.class, nf));
		assertEquals("Double did not parse", new Double(Double.MAX_VALUE), NumberUtils.parseNumber(aDouble, Double.class, nf));
	}

	public void testParseWithTrim() {
		String aByte = " " + Byte.MAX_VALUE + " ";
		String aShort = " " + Short.MAX_VALUE + " ";
		String anInteger = " " + Integer.MAX_VALUE + " ";
		String aLong = " " + Long.MAX_VALUE + " ";
		String aFloat = " " + Float.MAX_VALUE + " ";
		String aDouble = " " + Double.MAX_VALUE + " ";

		assertEquals("Byte did not parse", new Byte(Byte.MAX_VALUE), NumberUtils.parseNumber(aByte, Byte.class));
		assertEquals("Short did not parse", new Short(Short.MAX_VALUE), NumberUtils.parseNumber(aShort, Short.class));
		assertEquals("Integer did not parse", new Integer(Integer.MAX_VALUE), NumberUtils.parseNumber(anInteger, Integer.class));
		assertEquals("Long did not parse", new Long(Long.MAX_VALUE), NumberUtils.parseNumber(aLong, Long.class));
		assertEquals("Float did not parse", new Float(Float.MAX_VALUE), NumberUtils.parseNumber(aFloat, Float.class));
		assertEquals("Double did not parse", new Double(Double.MAX_VALUE), NumberUtils.parseNumber(aDouble, Double.class));
	}

	public void testParseWithTrimUsingNumberFormat() {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		String aByte = " " + Byte.MAX_VALUE + " ";
		String aShort = " " + Short.MAX_VALUE + " ";
		String anInteger = " " + Integer.MAX_VALUE + " ";
		String aLong = " " + Long.MAX_VALUE + " ";
		String aFloat = " " + Float.MAX_VALUE + " ";
		String aDouble = " " + Double.MAX_VALUE + " ";

		assertEquals("Byte did not parse", new Byte(Byte.MAX_VALUE), NumberUtils.parseNumber(aByte, Byte.class, nf));
		assertEquals("Short did not parse", new Short(Short.MAX_VALUE), NumberUtils.parseNumber(aShort, Short.class, nf));
		assertEquals("Integer did not parse", new Integer(Integer.MAX_VALUE), NumberUtils.parseNumber(anInteger, Integer.class, nf));
		assertEquals("Long did not parse", new Long(Long.MAX_VALUE), NumberUtils.parseNumber(aLong, Long.class, nf));
		assertEquals("Float did not parse", new Float(Float.MAX_VALUE), NumberUtils.parseNumber(aFloat, Float.class, nf));
		assertEquals("Double did not parse", new Double(Double.MAX_VALUE), NumberUtils.parseNumber(aDouble, Double.class, nf));
	}

	public void testParseAsHex() {
		String aByte = "0x" + Integer.toHexString(new Byte(Byte.MAX_VALUE).intValue());
		String aShort = "0x" + Integer.toHexString(new Short(Short.MAX_VALUE).intValue());
		String anInteger = "0x" + Integer.toHexString(Integer.MAX_VALUE);
		String aLong = "0x" + Long.toHexString(Long.MAX_VALUE);
		String aReallyBigInt = "FEBD4E677898DFEBFFEE44";

		assertByteEquals(aByte);
		assertShortEquals(aShort);
		assertIntegerEquals(anInteger);
		assertLongEquals(aLong);
		assertEquals("BigInteger did not parse",
				new BigInteger(aReallyBigInt, 16), NumberUtils.parseNumber("0x" + aReallyBigInt, BigInteger.class));
	}

	public void testParseAsOctal() {
		String aByte = "0" + Integer.toOctalString(Byte.MAX_VALUE);
		String aShort = "0" + Integer.toOctalString(Short.MAX_VALUE);
		String anInteger = "0" + Integer.toOctalString(Integer.MAX_VALUE);
		String aLong = "0" + Long.toOctalString(Long.MAX_VALUE);
		String aBigInteger = "7776554354453435353631212";

		assertByteEquals(aByte);
		assertShortEquals(aShort);
		assertIntegerEquals(anInteger);
		assertLongEquals(aLong);
		assertEquals("BigInteger did not parse",
				new BigInteger(aBigInteger, 8), NumberUtils.parseNumber("0" + aBigInteger, BigInteger.class));
	}

	public void testParseNegativeHex() throws Exception {
		String aByte = "-0x80";
		String aShort = "-0x8000";
		String anInteger = "-0x80000000";
		String aLong = "-0x8000000000000000";
		String aReallyBigInt = "FEBD4E677898DFEBFFEE44";

		assertNegativeByteEquals(aByte);
		assertNegativeShortEquals(aShort);
		assertNegativeIntegerEquals(anInteger);
		assertNegativeLongEquals(aLong);
		assertEquals("BigInteger did not parse",
				new BigInteger(aReallyBigInt, 16).negate(), NumberUtils.parseNumber("-0x" + aReallyBigInt, BigInteger.class));
	}

	public void testParseOverflow() {
		String aLong = "" + Long.MAX_VALUE;
		String aDouble = "" + Double.MAX_VALUE;

		try {
			NumberUtils.parseNumber(aLong, Byte.class);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException expected) {
		}

		try {
			NumberUtils.parseNumber(aLong, Short.class);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException expected) {
		}

		try {
			NumberUtils.parseNumber(aLong, Integer.class);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException expected) {
		}

		assertEquals(new Long(Long.MAX_VALUE), NumberUtils.parseNumber(aLong, Long.class));

		assertEquals(new Double(Double.MAX_VALUE), NumberUtils.parseNumber(aDouble, Double.class));
	}

	public void testParseNegativeOverflow() {
		String aLong = "" + Long.MIN_VALUE;
		String aDouble = "" + Double.MIN_VALUE;

		try {
			NumberUtils.parseNumber(aLong, Byte.class);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException expected) {
		}

		try {
			NumberUtils.parseNumber(aLong, Short.class);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException expected) {
		}

		try {
			NumberUtils.parseNumber(aLong, Integer.class);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException expected) {
		}

		assertEquals(new Long(Long.MIN_VALUE), NumberUtils.parseNumber(aLong, Long.class));

		assertEquals(new Double(Double.MIN_VALUE), NumberUtils.parseNumber(aDouble, Double.class));
	}

	public void testParseOverflowUsingNumberFormat() {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		String aLong = "" + Long.MAX_VALUE;
		String aDouble = "" + Double.MAX_VALUE;

		try {
			NumberUtils.parseNumber(aLong, Byte.class, nf);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException expected) {
		}

		try {
			NumberUtils.parseNumber(aLong, Short.class, nf);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException expected) {
		}

		try {
			NumberUtils.parseNumber(aLong, Integer.class, nf);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException expected) {
		}

		assertEquals(new Long(Long.MAX_VALUE), NumberUtils.parseNumber(aLong, Long.class, nf));

		assertEquals(new Double(Double.MAX_VALUE), NumberUtils.parseNumber(aDouble, Double.class, nf));
	}

	public void testParseNegativeOverflowUsingNumberFormat() {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		String aLong = "" + Long.MIN_VALUE;
		String aDouble = "" + Double.MIN_VALUE;

		try {
			NumberUtils.parseNumber(aLong, Byte.class, nf);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException expected) {
		}

		try {
			NumberUtils.parseNumber(aLong, Short.class, nf);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException expected) {
		}

		try {
			NumberUtils.parseNumber(aLong, Integer.class, nf);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException expected) {
		}

		assertEquals(new Long(Long.MIN_VALUE), NumberUtils.parseNumber(aLong, Long.class, nf));

		assertEquals(new Double(Double.MIN_VALUE), NumberUtils.parseNumber(aDouble, Double.class, nf));
	}

	private void assertLongEquals(String aLong) {
		assertEquals("Long did not parse", Long.MAX_VALUE, NumberUtils.parseNumber(aLong, Long.class).longValue());
	}

	private void assertIntegerEquals(String anInteger) {
		assertEquals("Integer did not parse", Integer.MAX_VALUE, NumberUtils.parseNumber(anInteger, Integer.class).intValue());
	}

	private void assertShortEquals(String aShort) {
		assertEquals("Short did not parse", Short.MAX_VALUE, NumberUtils.parseNumber(aShort, Short.class).shortValue());
	}

	private void assertByteEquals(String aByte) {
		assertEquals("Byte did not parse", Byte.MAX_VALUE, NumberUtils.parseNumber(aByte, Byte.class).byteValue());
	}

	private void assertNegativeLongEquals(String aLong) {
		assertEquals("Long did not parse", Long.MIN_VALUE, NumberUtils.parseNumber(aLong, Long.class).longValue());
	}

	private void assertNegativeIntegerEquals(String anInteger) {
		assertEquals("Integer did not parse", Integer.MIN_VALUE, NumberUtils.parseNumber(anInteger, Integer.class).intValue());
	}

	private void assertNegativeShortEquals(String aShort) {
		assertEquals("Short did not parse", Short.MIN_VALUE, NumberUtils.parseNumber(aShort, Short.class).shortValue());
	}

	private void assertNegativeByteEquals(String aByte) {
		assertEquals("Byte did not parse", Byte.MIN_VALUE, NumberUtils.parseNumber(aByte, Byte.class).byteValue());
	}

}
