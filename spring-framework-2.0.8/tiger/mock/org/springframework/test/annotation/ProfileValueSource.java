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

package org.springframework.test.annotation;

/**
 * Strategy interface for retrieving <em>profile values</em> for a given
 * testing environment.
 *
 * <p>Spring provides the following out-of-the-box implementations:
 *
 * <ul>
 * <li>{@link SystemProfileValueSource}</li>
 * </ul>
 *
 * @author Rod Johnson
 * @author Sam Brannen
 * @since 2.0
 * @see AbstractAnnotationAwareTransactionalTests
 */
public interface ProfileValueSource {

	/**
	 * Get the <em>profile value</em> indicated by the specified key.
	 * @param key The name of the <em>profile value</em>.
	 * @return The string value of the <em>profile value</em>, or <code>null</code>
	 * if there is no <em>profile value</em> with that key
	 * @exception IllegalArgumentException if <code>key</code> is
	 * <code>null</code> or empty
	 */
	String get(String key);

}
