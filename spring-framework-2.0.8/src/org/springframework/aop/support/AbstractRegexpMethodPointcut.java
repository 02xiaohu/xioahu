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

package org.springframework.aop.support;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Abstract base regular expression pointcut bean. JavaBean properties are:
 * <ul>
 * <li>pattern: regular expression for the fully-qualified method names to match.
 * The exact regexp syntax will depend on the subclass (e.g. Perl5 regular expressions)
 * <li>patterns: alternative property taking a String array of patterns. The result will
 * be the union of these patterns.
 * </ul>
 *
 * <p>Note: the regular expressions must be a match. For example,
 * <code>.*get.*</code> will match com.mycom.Foo.getBar().
 * <code>get.*</code> will not.
 *
 * <p>This base class is serializable. Subclasses should declare all fields transient
 * - the initPatternRepresentation method in this class will be invoked again on the
 * client side on deserialization.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 1.1
 * @see Perl5RegexpMethodPointcut
 * @see JdkRegexpMethodPointcut
 */
public abstract class AbstractRegexpMethodPointcut extends StaticMethodMatcherPointcut
		implements Serializable {

	/** Regular expressions to match */
	private String[] patterns = new String[0];

	/** Regaular expressions <strong>not</strong> to match */
	private String[] excludedPatterns = new String[0];


	/**
	 * Convenience method when we have only a single pattern.
	 * Use either this method or {@link #setPatterns}, not both.
	 * @see #setPatterns
	 */
	public void setPattern(String pattern) {
		setPatterns(new String[] {pattern});
	}

	/**
	 * Set the regular expressions defining methods to match.
	 * Matching will be the union of all these; if any match,
	 * the pointcut matches.
	 */
	public void setPatterns(String[] patterns) {
		Assert.notEmpty(patterns, "'patterns' must not be empty");
		this.patterns = new String[patterns.length];
		for (int i = 0; i < patterns.length; i++) {
			this.patterns[i] = StringUtils.trimWhitespace(patterns[i]);
		}
		initPatternRepresentation(this.patterns);
	}

	/**
	 * Return the regular expressions for method matching.
	 */
	public String[] getPatterns() {
		return this.patterns;
	}

	/**
	 * Convenience method when we have only a single exclusion pattern.
	 * Use either this method or {@link #setExcludedPatterns}, not both.
	 * @see #setExcludedPatterns
	 */
	public void setExcludedPattern(String excludedPattern) {
		setExcludedPatterns(new String[] {excludedPattern});
	}

	/**
	 * Set the regular expressions defining methods to match for exclusion.
	 * Matching will be the union of all these; if any match,
	 * the pointcut matches.
	 */
	public void setExcludedPatterns(String[] excludedPatterns) {
		Assert.notEmpty(excludedPatterns, "'excludedPatterns' must not be empty");
		this.excludedPatterns = new String[excludedPatterns.length];
		for (int i = 0; i < excludedPatterns.length; i++) {
			this.excludedPatterns[i] = StringUtils.trimWhitespace(excludedPatterns[i]);
		}
		initExcludedPatternRepresentation(this.excludedPatterns);
	}

	/**
	 * Returns the regular expressions for exclusion matching.
	 */
	public String[] getExcludedPatterns() {
		return this.excludedPatterns;
	}


	/**
	 * Try to match the regular expression against the fully qualified name
	 * of the method's declaring class, plus the name of the method.
	 * Note that the declaring class is that class that originally declared
	 * the method, not necessarily the class that's currently exposing it.
	 * <p>For example, "java.lang.Object.hashCode" matches any subclass
	 * of Object's <code>hashCode()</code> method.
	 */
	public final boolean matches(Method method, Class targetClass) {
		// TODO use target class here?
		String patt = method.getDeclaringClass().getName() + "." + method.getName();
		for (int i = 0; i < this.patterns.length; i++) {
			boolean matched = matches(patt, i);
			if (matched) {
				for (int j = 0; j < this.excludedPatterns.length; j++) {
					boolean excluded = matchesExclusion(patt, j);
					if(excluded) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}


	/**
	 * Subclasses must implement this to initialize regexp pointcuts.
	 * Can be invoked multiple times.
	 * <p>This method will be invoked from the setPatterns method,
	 * and also on deserialization.
	 * @param patterns the patterns to initialize
	 * @throws IllegalArgumentException in case of an invalid pattern
	 */
	protected abstract void initPatternRepresentation(String[] patterns) throws IllegalArgumentException;

	protected abstract void initExcludedPatternRepresentation(String[] excludedPatterns) throws IllegalArgumentException;

	/**
	 * Does the pattern at the given index match this string?
	 * @param pattern <code>String</code> pattern to match
	 * @param patternIndex index of pattern from 0
	 * @return <code>true</code> if there is a match, else <code>false</code>.
	 */
	protected abstract boolean matches(String pattern, int patternIndex);

	/**
	 * Does the exclusion pattern at the given index match this string?
	 * @param pattern <code>String</code> pattern to match.
	 * @param patternIndex index of pattern starting from 0.
	 * @return <code>true</code> if there is a match, else <code>false</code>.
	 */
	protected abstract boolean matchesExclusion(String pattern, int patternIndex);


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AbstractRegexpMethodPointcut)) {
			return false;
		}
		AbstractRegexpMethodPointcut otherPointcut = (AbstractRegexpMethodPointcut) other;
		return (Arrays.equals(this.patterns, otherPointcut.patterns) &&
				Arrays.equals(this.excludedPatterns, otherPointcut.excludedPatterns));
	}

	public int hashCode() {
		int result = 27;
		for (int i = 0; i < this.patterns.length; i++) {
			String pattern = this.patterns[i];
			result = 13 * result + pattern.hashCode();
		}
		for (int i = 0; i < this.excludedPatterns.length; i++) {
			String excludedPattern = this.excludedPatterns[i];
			result = 13 * result + excludedPattern.hashCode();
		}
		return result;
	}

	public String toString() {
		return getClass().getName() + ": patterns " + ObjectUtils.nullSafeToString(this.patterns) +
				", excluded patterns " + ObjectUtils.nullSafeToString(this.excludedPatterns);
	}


	//---------------------------------------------------------------------
	// Serialization support
	//---------------------------------------------------------------------

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		// Rely on default serialization; just initialize state after deserialization.
		ois.defaultReadObject();

		// Ask subclass to reinitialize.
		initPatternRepresentation(this.patterns);
		initExcludedPatternRepresentation(this.excludedPatterns);
	}

}
