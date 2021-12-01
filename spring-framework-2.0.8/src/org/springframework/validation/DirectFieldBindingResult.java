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

package org.springframework.validation;

import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.util.Assert;

/**
 * Special implementation of the Errors and BindingResult interfaces,
 * supporting registration and evaluation of binding errors on value objects.
 * Performs direct field access instead of going through JavaBean getters.
 *
 * <p>This implementation just supports fields in the actual target object.
 * It is not able to traverse nested fields.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see DataBinder#getBindingResult()
 * @see DataBinder#initDirectFieldAccess()
 * @see BeanPropertyBindingResult
 */
public class DirectFieldBindingResult extends AbstractPropertyBindingResult {

	private final Object target;

	private transient DirectFieldAccessor directFieldAccessor;


	/**
	 * Create a new DirectFieldBindingResult instance.
	 * @param target the target object to bind onto
	 * @param objectName the name of the target object
	 */
	public DirectFieldBindingResult(Object target, String objectName) {
		super(objectName);
		Assert.notNull(target, "Target bean must not be null");
		this.target = target;
	}


	public final Object getTarget() {
		return target;
	}

	/**
	 * Returns the DirectFieldAccessor that this instance uses.
	 * Creates a new one if none existed before.
	 * @see #createDirectFieldAccessor()
	 */
	public final ConfigurablePropertyAccessor getPropertyAccessor() {
		if (this.directFieldAccessor == null) {
			this.directFieldAccessor = createDirectFieldAccessor();
		}
		return this.directFieldAccessor;
	}

	/**
	 * Create a new DirectFieldAccessor for the underlying target object.
	 * @see #getTarget()
	 */
	protected DirectFieldAccessor createDirectFieldAccessor() {
		return new DirectFieldAccessor(getTarget());
	}

}
