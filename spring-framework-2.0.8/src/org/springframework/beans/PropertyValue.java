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

import java.io.Serializable;

import org.springframework.core.AttributeAccessorSupport;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Object to hold information and value for an individual bean property.
 * Using an object here, rather than just storing all properties in
 * a map keyed by property name, allows for more flexibility, and the
 * ability to handle indexed properties etc in an optimized way.
 *
 * <p>Note that the value doesn't need to be the final required type:
 * A {@link BeanWrapper} implementation should handle any necessary conversion,
 * as this object doesn't know anything about the objects it will be applied to.
 *
 * @author Rod Johnson
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 13 May 2001
 * @see PropertyValues
 * @see BeanWrapper
 */
public class PropertyValue extends AttributeAccessorSupport implements BeanMetadataElement, Serializable {

	private final String name;

	private final Object value;

	private Object source;

	private boolean converted = false;

	private Object convertedValue;

	/** Package-visible field for caching the resolved property path tokens */
	volatile Object resolvedTokens;


	/**
	 * Create a new PropertyValue instance.
	 * @param name the name of the property (never <code>null</code>)
	 * @param value the value of the property (possibly before type conversion)
	 */
	public PropertyValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Copy constructor.
	 * @param original the PropertyValue to copy (never <code>null</code>)
	 */
	public PropertyValue(PropertyValue original) {
		Assert.notNull(original, "Original must not be null");
		this.name = original.getName();
		this.value = original.getValue();
		this.source = original.getSource();
		this.resolvedTokens = original.resolvedTokens;
		copyAttributesFrom(original);
	}

	/**
	 * Constructor that exposes a new value for an original value holder.
	 * The original holder will be exposed as source of the new holder.
	 * @param original the PropertyValue to link to (never <code>null</code>)
	 * @param newValue the new value to apply
	 */
	public PropertyValue(PropertyValue original, Object newValue) {
		Assert.notNull(original, "Original must not be null");
		this.name = original.getName();
		this.value = newValue;
		this.source = original;
		this.resolvedTokens = original.resolvedTokens;
		copyAttributesFrom(original);
	}


	/**
	 * Return the name of the property.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Return the value of the property.
	 * <p>Note that type conversion will <i>not</i> have occurred here.
	 * It is the responsibility of the BeanWrapper implementation to
	 * perform type conversion.
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * Set the configuration source <code>Object</code> for this metadata element.
	 * <p>The exact type of the object will depend on the configuration mechanism used.
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	public Object getSource() {
		return this.source;
	}

	/**
	 * Return the original PropertyValue instance for this value holder.
	 * @return the original PropertyValue (either a source of this
	 * value holder or this value holder itself).
	 */
	public PropertyValue getOriginalPropertyValue() {
		PropertyValue original = this;
		while (original.source instanceof PropertyValue && original.source != original) {
			original = (PropertyValue) original.source;
		}
		return original;
	}

	/**
	 * Return whether this holder contains a converted value already (<code>true</code>),
	 * or whether the value still needs to be converted (<code>false</code>).
	 */
	public synchronized boolean isConverted() {
		return this.converted;
	}

	/**
	 * Set the converted value of the constructor argument,
	 * after processed type conversion.
	 */
	public synchronized void setConvertedValue(Object value) {
		this.converted = true;
		this.convertedValue = value;
	}

	/**
	 * Return the converted value of the constructor argument,
	 * after processed type conversion.
	 */
	public synchronized Object getConvertedValue() {
		return this.convertedValue;
	}


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PropertyValue)) {
			return false;
		}
		PropertyValue otherPv = (PropertyValue) other;
		return (this.name.equals(otherPv.name) &&
				ObjectUtils.nullSafeEquals(this.value, otherPv.value) &&
				ObjectUtils.nullSafeEquals(this.source, otherPv.source));
	}

	public int hashCode() {
		return this.name.hashCode() * 29 + (this.value == null ? 0 : this.value.hashCode());
	}

	public String toString() {
		return "PropertyValue: name='" + this.name + "', value=[" + this.value + "]";
	}

}
