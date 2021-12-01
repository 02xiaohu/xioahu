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

package org.springframework.aop.aspectj.autoproxy.benchmark;

import junit.framework.TestCase;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.ITestBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StopWatch;

/**
 * Tests for AspectJ auto proxying. Includes mixing with Spring AOP 
 * Advisors to demonstrate that existing autoproxying contract is honoured.
 * <p>
 * Change the return of the getCount() method to MEANINGFUL_NUMBER_OF_PASSES
 * to make this test actually run a meaningful benchmark. Currently it is set
 * to execute the test suite quickly, not produce meaningful results.
 * @author Rod Johnson
 */
public class BenchmarkTests extends TestCase {
	
	/**
	 * Number of passes high enough to produce meaningful results.
	 */
	private static final int MEANINGFUL_NUMBER_OF_PASSES = 1000000;
	
	private static final String ASPECTJ_CONTEXT = "/org/springframework/aop/aspectj/autoproxy/benchmark/aspectj.xml";

	/**
	 * Change the return number to MEANINGFUL_NUMBER_OF_PASSES to make this
	 * test useful
	 * @return the number of passes to runs
	 */
	protected int getCount() {
		return 10;
		//return MEANINGFUL_NUMBER_OF_PASSES;
	}
	
	public void testRepeatedAroundAdviceInvocationsWithAspectJ() {
		testRepeatedAroundAdviceInvocations(ASPECTJ_CONTEXT, getCount(), "AspectJ");
	}
	
	public void testRepeatedAroundAdviceInvocationsWithSpringAop() {
		testRepeatedAroundAdviceInvocations(SPRING_AOP_CONTEXT(), getCount(), "Spring AOP");
	}
	
	public void testRepeatedBeforeAdviceInvocationsWithAspectJ() {
		testBeforeAdviceWithoutJoinPoint(ASPECTJ_CONTEXT, getCount(), "AspectJ");
	}
	
	public void testRepeatedBeforeAdviceInvocationsWithSpringAop() {
		testBeforeAdviceWithoutJoinPoint(SPRING_AOP_CONTEXT(), getCount(), "Spring AOP");
	}
	
	public void testRepeatedAfterReturningAdviceInvocationsWithAspectJ() {
		testAfterReturningAdviceWithoutJoinPoint(ASPECTJ_CONTEXT, getCount(), "AspectJ");
	}
	
	public void testRepeatedAfterReturningAdviceInvocationsWithSpringAop() {
		testAfterReturningAdviceWithoutJoinPoint(SPRING_AOP_CONTEXT(), getCount(), "Spring AOP");
	}

	public void testRepeatedMixWithAspectJ() {
		testMix(ASPECTJ_CONTEXT, getCount(), "AspectJ");
	}
	
	public void testRepeatedMixWithSpringAop() {
		testMix(SPRING_AOP_CONTEXT(), getCount(), "Spring AOP");
	}

	private String SPRING_AOP_CONTEXT() {
		return "/org/springframework/aop/aspectj/autoproxy/benchmark/springAop.xml";
	}
	
	private long testRepeatedAroundAdviceInvocations(String file, int howmany, String technology) {
		ClassPathXmlApplicationContext bf = new ClassPathXmlApplicationContext(file);

		StopWatch sw = new StopWatch();
		sw.start(howmany + " repeated around advice invocations with " + technology);
		ITestBean adrian = (ITestBean) bf.getBean("adrian");
		
		assertTrue(AopUtils.isAopProxy(adrian));
		assertEquals(68, adrian.getAge());
		
		for (int i = 0; i < howmany; i++) {
			adrian.getAge();
		}
		
		sw.stop();
		System.out.println(sw.prettyPrint());
		return sw.getLastTaskTimeMillis();
	}
	
	private long testBeforeAdviceWithoutJoinPoint(String file, int howmany, String technology) {
		ClassPathXmlApplicationContext bf = new ClassPathXmlApplicationContext(file);

		StopWatch sw = new StopWatch();
		sw.start(howmany + " repeated before advice invocations with " + technology);
		ITestBean adrian = (ITestBean) bf.getBean("adrian");
		
		assertTrue(AopUtils.isAopProxy(adrian));
		Advised a = (Advised) adrian;
		assertTrue(a.getAdvisors().length >= 3);
		assertEquals("adrian", adrian.getName());
		
		for (int i = 0; i < howmany; i++) {
			adrian.getName();
		}
		
		sw.stop();
		System.out.println(sw.prettyPrint());
		return sw.getLastTaskTimeMillis();
	}
	
	private long testAfterReturningAdviceWithoutJoinPoint(String file, int howmany, String technology) {
		ClassPathXmlApplicationContext bf = new ClassPathXmlApplicationContext(file);

		StopWatch sw = new StopWatch();
		sw.start(howmany + " repeated after returning advice invocations with " + technology);
		ITestBean adrian = (ITestBean) bf.getBean("adrian");
		
		assertTrue(AopUtils.isAopProxy(adrian));
		Advised a = (Advised) adrian;
		assertTrue(a.getAdvisors().length >= 3);
		// Hits joinpoint
		adrian.setAge(25);
		
		for (int i = 0; i < howmany; i++) {
			adrian.setAge(i);
		}
		
		sw.stop();
		System.out.println(sw.prettyPrint());
		return sw.getLastTaskTimeMillis();
	}
	
	private long testMix(String file, int howmany, String technology) {
		ClassPathXmlApplicationContext bf = new ClassPathXmlApplicationContext(file);

		StopWatch sw = new StopWatch();
		sw.start(howmany + " repeated mixed invocations with " + technology);
		ITestBean adrian = (ITestBean) bf.getBean("adrian");
		
		assertTrue(AopUtils.isAopProxy(adrian));
		Advised a = (Advised) adrian;
		assertTrue(a.getAdvisors().length >= 3);
		
		for (int i = 0; i < howmany; i++) {
			// Hit all 3 joinpoints
			adrian.getAge();
			adrian.getName();
			adrian.setAge(i);
			
			// Invoke three non-advised methods
			adrian.getDoctor();
			adrian.getLawyer();
			adrian.getSpouse();
		}
		
		sw.stop();
		System.out.println(sw.prettyPrint());
		return sw.getLastTaskTimeMillis();
	}
	
	// TODO test mixed
	
	// TODO test regular
	
	// TODO test n proxy creation
	
	// Useful for profilers that don't allow profiling unit tests
	public static void main(String[] args) {
		new BenchmarkTests().testRepeatedAroundAdviceInvocationsWithAspectJ();
	}
	
}
