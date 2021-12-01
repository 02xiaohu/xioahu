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

package org.springframework.scheduling.support;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
import org.springframework.util.ClassUtils;

/**
 * Adapter that implements the Runnable interface as a configurable
 * method invocation based on Spring's MethodInvoker.
 *
 * <p>Inherits common configuration properties from
 * {@link org.springframework.util.MethodInvoker}.
 *
 * <p>Useful to generically encapsulate a method invocation as timer task
 * for <code>java.util.Timer</code>, in combination with a
 * {@link org.springframework.scheduling.timer.DelegatingTimerTask} adapter.
 * Can also be used with JDK 1.5's <code>java.util.concurrent.Executor</code>
 * abstraction, which works with plain Runnables.
 *
 * <p>Extended by Spring's
 * {@link org.springframework.scheduling.timer.MethodInvokingTimerTaskFactoryBean}
 * adapter for <code>java.util.TimerTask</code>. Note that you can populate a
 * ScheduledTimerTask object with a plain MethodInvokingRunnable instance
 * as well, which will automatically get wrapped with a DelegatingTimerTask.
 *
 * @author Juergen Hoeller
 * @since 1.2.4
 * @see org.springframework.scheduling.timer.ScheduledTimerTask#setRunnable(Runnable)
 * @see java.util.concurrent.Executor#execute(Runnable)
 */
public class MethodInvokingRunnable extends ArgumentConvertingMethodInvoker
		implements Runnable, BeanClassLoaderAware, InitializingBean {

	protected final Log logger = LogFactory.getLog(getClass());

	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();


	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

	protected Class resolveClassName(String className) throws ClassNotFoundException {
		return ClassUtils.forName(className, this.beanClassLoader);
	}

	public void afterPropertiesSet() throws ClassNotFoundException, NoSuchMethodException {
		prepare();
	}


	public void run() {
		try {
			invoke();
		}
		catch (InvocationTargetException ex) {
			logger.warn(getInvocationFailureMessage(), ex);
			// Do not throw exception, else the main loop of the scheduler might stop!
		}
		catch (Throwable ex) {
			logger.warn(getInvocationFailureMessage(), ex);
			// Do not throw exception, else the main loop of the scheduler might stop!
		}
	}

	/**
	 * Build a message for an invocation failure exception.
	 * @return the error message, including the target method name etc
	 */
	protected String getInvocationFailureMessage() {
		return "Invocation of method '" + getTargetMethod() +
				"' on target class [" + getTargetClass() + "] failed";
	}

}
