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

package org.springframework.context.support;

import java.util.Locale;
import java.util.Properties;

import junit.framework.TestCase;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.JdkVersion;

/**
 * @author Juergen Hoeller
 * @since 03.02.2004
 */
public class ResourceBundleMessageSourceTests extends TestCase {

	protected void doTestMessageAccess(
			boolean reloadable, boolean fallbackToSystemLocale,
			boolean expectGermanFallback, boolean useCodeAsDefaultMessage, boolean alwaysUseMessageFormat) {

		StaticApplicationContext ac = new StaticApplicationContext();
		if (reloadable) {
			StaticApplicationContext parent = new StaticApplicationContext();
			parent.refresh();
			ac.setParent(parent);
		}

		MutablePropertyValues pvs = new MutablePropertyValues();
		String basepath = "org/springframework/context/support/";
		String[] basenames = null;
		if (reloadable) {
			basenames = new String[] {
				"classpath:" + basepath + "messages",
				"classpath:" + basepath + "more-messages"};
		}
		else {
			basenames = new String[] {
				basepath + "messages",
				basepath + "more-messages"};
		}
		pvs.addPropertyValue("basenames", basenames);
		if (!fallbackToSystemLocale) {
			pvs.addPropertyValue("fallbackToSystemLocale", Boolean.FALSE);
		}
		if (useCodeAsDefaultMessage) {
			pvs.addPropertyValue("useCodeAsDefaultMessage", Boolean.TRUE);
		}
		if (alwaysUseMessageFormat) {
			pvs.addPropertyValue("alwaysUseMessageFormat", Boolean.TRUE);
		}
		Class clazz = reloadable ?
				(Class) ReloadableResourceBundleMessageSource.class : ResourceBundleMessageSource.class;
		ac.registerSingleton("messageSource", clazz, pvs);
		ac.refresh();

		expectGermanFallback = expectGermanFallback && (JdkVersion.getMajorJavaVersion() > JdkVersion.JAVA_13);

		Locale.setDefault(expectGermanFallback ? Locale.GERMAN : Locale.CANADA);
		assertEquals("message1", ac.getMessage("code1", null, Locale.ENGLISH));
		assertEquals(fallbackToSystemLocale && expectGermanFallback ? "nachricht2" : "message2",
				ac.getMessage("code2", null, Locale.ENGLISH));

		assertEquals("nachricht2", ac.getMessage("code2", null, Locale.GERMAN));
		assertEquals("nochricht2", ac.getMessage("code2", null, new Locale("DE", "at")));
		assertEquals("noochricht2", ac.getMessage("code2", null, new Locale("DE", "at", "oo")));

		if (reloadable && JdkVersion.getMajorJavaVersion() >= JdkVersion.JAVA_15) {
			assertEquals("nachricht2xml", ac.getMessage("code2", null, Locale.GERMANY));
		}

		MessageSourceAccessor accessor = new MessageSourceAccessor(ac);
		LocaleContextHolder.setLocale(new Locale("DE", "at"));
		try {
			assertEquals("nochricht2", accessor.getMessage("code2"));
		}
		finally {
			LocaleContextHolder.setLocale(null);
		}

		assertEquals("message3", ac.getMessage("code3", null, Locale.ENGLISH));
		MessageSourceResolvable resolvable = new DefaultMessageSourceResolvable("code3");
		assertEquals("message3", ac.getMessage(resolvable, Locale.ENGLISH));
		resolvable = new DefaultMessageSourceResolvable(new String[] {"code4", "code3"});
		assertEquals("message3", ac.getMessage(resolvable, Locale.ENGLISH));

		assertEquals("message3", ac.getMessage("code3", null, Locale.ENGLISH));
		resolvable = new DefaultMessageSourceResolvable(new String[] {"code4", "code3"});
		assertEquals("message3", ac.getMessage(resolvable, Locale.ENGLISH));

		Object[] args = new Object[] {"Hello", new DefaultMessageSourceResolvable(new String[] {"code1"})};
		assertEquals("Hello, message1", ac.getMessage("hello", args, Locale.ENGLISH));

		// test default message without and with args
		assertEquals("default", ac.getMessage(null, null, "default", Locale.ENGLISH));
		assertEquals("default", ac.getMessage(null, args, "default", Locale.ENGLISH));
		assertEquals("{0}, default", ac.getMessage(null, null, "{0}, default", Locale.ENGLISH));
		assertEquals("Hello, default", ac.getMessage(null, args, "{0}, default", Locale.ENGLISH));

		// test resolvable with default message, without and with args
		resolvable = new DefaultMessageSourceResolvable(null, null, "default");
		assertEquals("default", ac.getMessage(resolvable, Locale.ENGLISH));
		resolvable = new DefaultMessageSourceResolvable(null, args, "default");
		assertEquals("default", ac.getMessage(resolvable, Locale.ENGLISH));
		resolvable = new DefaultMessageSourceResolvable(null, null, "{0}, default");
		assertEquals("{0}, default", ac.getMessage(resolvable, Locale.ENGLISH));
		resolvable = new DefaultMessageSourceResolvable(null, args, "{0}, default");
		assertEquals("Hello, default", ac.getMessage(resolvable, Locale.ENGLISH));

		// test message args
		assertEquals("Arg1, Arg2", ac.getMessage("hello", new Object[] {"Arg1", "Arg2"}, Locale.ENGLISH));
		assertEquals("{0}, {1}", ac.getMessage("hello", null, Locale.ENGLISH));

		if (alwaysUseMessageFormat) {
			assertEquals("I'm", ac.getMessage("escaped", null, Locale.ENGLISH));
		}
		else {
			assertEquals("I''m", ac.getMessage("escaped", null, Locale.ENGLISH));
		}
		assertEquals("I'm", ac.getMessage("escaped", new Object[] {"some arg"}, Locale.ENGLISH));

		try {
			assertEquals("code4", ac.getMessage("code4", null, Locale.GERMAN));
			if (!useCodeAsDefaultMessage) {
				fail("Should have thrown NoSuchMessageException");
			}
		}
		catch (NoSuchMessageException ex) {
			if (useCodeAsDefaultMessage) {
				fail("Should have returned code as default message");
			}
		}
	}

	public void testMessageAccessWithDefaultMessageSource() {
		doTestMessageAccess(false, true, false, false, false);
	}

	public void testMessageAccessWithDefaultMessageSourceAndMessageFormat() {
		doTestMessageAccess(false, true, false, false, true);
	}

	public void testMessageAccessWithDefaultMessageSourceAndFallbackToGerman() {
		doTestMessageAccess(false, true, true, true, false);
	}

	public void testMessageAccessWithReloadableMessageSource() {
		doTestMessageAccess(true, true, false, false, false);
	}

	public void testMessageAccessWithReloadableMessageSourceAndMessageFormat() {
		doTestMessageAccess(true, true, false, false, true);
	}

	public void testMessageAccessWithReloadableMessageSourceAndFallbackToGerman() {
		doTestMessageAccess(true, true, true, true, false);
	}

	public void testMessageAccessWithReloadableMessageSourceAndFallbackTurnedOff() {
		doTestMessageAccess(true, false, false, false, false);
	}

	public void testMessageAccessWithReloadableMessageSourceAndFallbackTurnedOffAndFallbackToGerman() {
		doTestMessageAccess(true, false, true, true, false);
	}

	public void testResourceBundleMessageSourceStandalone() {
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		ms.setBasename("org/springframework/context/support/messages");
		assertEquals("message1", ms.getMessage("code1", null, Locale.ENGLISH));
		assertEquals("nachricht2", ms.getMessage("code2", null, Locale.GERMAN));
	}

	public void testResourceBundleMessageSourceWithWhitespaceInBasename() {
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		ms.setBasename("  org/springframework/context/support/messages  ");
		assertEquals("message1", ms.getMessage("code1", null, Locale.ENGLISH));
		assertEquals("nachricht2", ms.getMessage("code2", null, Locale.GERMAN));
	}

	public void testReloadableResourceBundleMessageSourceStandalone() {
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setBasename("org/springframework/context/support/messages");
		assertEquals("message1", ms.getMessage("code1", null, Locale.ENGLISH));
		assertEquals("nachricht2", ms.getMessage("code2", null, Locale.GERMAN));
	}

	public void testReloadableResourceBundleMessageSourceWithWhitespaceInBasename() {
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setBasename("  org/springframework/context/support/messages  ");
		assertEquals("message1", ms.getMessage("code1", null, Locale.ENGLISH));
		assertEquals("nachricht2", ms.getMessage("code2", null, Locale.GERMAN));
	}

	public void testReloadableResourceBundleMessageSourceWithDefaultCharset() {
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setBasename("org/springframework/context/support/messages");
		ms.setDefaultEncoding("ISO-8859-1");
		assertEquals("message1", ms.getMessage("code1", null, Locale.ENGLISH));
		assertEquals("nachricht2", ms.getMessage("code2", null, Locale.GERMAN));
	}

	public void testReloadableResourceBundleMessageSourceWithInappropriateDefaultCharset() {
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setBasename("org/springframework/context/support/messages");
		ms.setDefaultEncoding("unicode");
		Properties fileCharsets = new Properties();
		fileCharsets.setProperty("org/springframework/context/support/messages_de", "unicode");
		ms.setFileEncodings(fileCharsets);
		ms.setFallbackToSystemLocale(false);
		try {
			ms.getMessage("code1", null, Locale.ENGLISH);
			fail("Should have thrown NoSuchMessageException");
		}
		catch (NoSuchMessageException ex) {
			// expected
		}
	}

	public void testReloadableResourceBundleMessageSourceWithInappropriateEnglishCharset() {
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setBasename("org/springframework/context/support/messages");
		ms.setFallbackToSystemLocale(false);
		Properties fileCharsets = new Properties();
		fileCharsets.setProperty("org/springframework/context/support/messages", "unicode");
		ms.setFileEncodings(fileCharsets);
		try {
			ms.getMessage("code1", null, Locale.ENGLISH);
			fail("Should have thrown NoSuchMessageException");
		}
		catch (NoSuchMessageException ex) {
			// expected
		}
	}

	public void testReloadableResourceBundleMessageSourceWithInappropriateGermanCharset() {
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setBasename("org/springframework/context/support/messages");
		ms.setFallbackToSystemLocale(false);
		Properties fileCharsets = new Properties();
		fileCharsets.setProperty("org/springframework/context/support/messages_de", "unicode");
		ms.setFileEncodings(fileCharsets);
		assertEquals("message1", ms.getMessage("code1", null, Locale.ENGLISH));
		assertEquals("message2", ms.getMessage("code2", null, Locale.GERMAN));
	}

}
