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

package org.springframework.transaction.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Inherits fallback behavior from AbstractFallbackTransactionAttributeSource.
 *
 * @author Rod Johnson
 */
public class MapTransactionAttributeSource extends AbstractFallbackTransactionAttributeSource {
	
	/** Map from Method or Clazz to TransactionAttribute */
	private final Map attributeMap = new HashMap();
	
	public void register(Method m, TransactionAttribute txAtt) {
		this.attributeMap.put(m, txAtt);
	}
	
	public void register(Class clazz, TransactionAttribute txAtt) {
		this.attributeMap.put(clazz, txAtt);
	}
	
	protected Collection findAllAttributes(Class clazz) {
		return doFindAllAttributes(clazz);
	}
	
	protected Collection findAllAttributes(Method m) {
		return doFindAllAttributes(m);
	}
	
	private Collection doFindAllAttributes(Object what) {
		Object att = this.attributeMap.get(what);
		return (att != null ? Collections.singleton(att) : null);
	}
	
}
