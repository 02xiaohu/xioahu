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

package org.springframework.aop.aspectj.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author Juergen Hoeller
 */
@Aspect
public class PointcutWithAnnotationArgument {

	@Around(value = "execution(* org.springframework..*.*(..)) && @annotation(transaction)")
	public Object around(ProceedingJoinPoint pjp, Transactional transaction) throws Throwable {
		System.out.println("Invoked with transaction " + transaction);
		throw new IllegalStateException();
	}

}
