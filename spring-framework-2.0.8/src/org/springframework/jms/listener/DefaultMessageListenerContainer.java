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

package org.springframework.jms.listener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.springframework.core.Constants;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.support.JmsUtils;
import org.springframework.jms.support.destination.CachingDestinationResolver;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.scheduling.SchedulingAwareRunnable;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * Message listener container variant that uses plain JMS client API, specifically
 * a loop of <code>MessageConsumer.receive()</code> calls that also allow for
 * transactional reception of messages (registering them with XA transactions).
 * Designed to work in a native JMS environment as well as in a J2EE environment,
 * with only minimal differences in configuration.
 *
 * <p><b>NOTE:</b> This class requires a JMS 1.1+ provider, because it builds on
 * the domain-independent API. <b>Use the {@link DefaultMessageListenerContainer102}
 * subclass for a JMS 1.0.2 provider, e.g. when running on a J2EE 1.3 server.</b>
 *
 * <p>This is a simple but nevertheless powerful form of message listener container.
 * On startup, it obtains a fixed number of JMS Sessions to invoke the listener,
 * and optionally allows for dynamic adaptation at runtime (up until a maximum number).
 * Like {@link SimpleMessageListenerContainer}, its main advantage is its low level
 * of runtime complexity, in particular the minimal requirements on the JMS provider:
 * Not even the JMS ServerSessionPool facility is required. Beyond that, it is
 * fully self-recovering in case of the broker being temporarily unavailable,
 * and allows for stops/restarts as well as runtime changes to its configuration.
 *
 * <p>Actual MessageListener execution happens in asynchronous work units which are
 * created through Spring's {@link org.springframework.core.task.TaskExecutor}
 * abstraction. By default, the specified number of invoker tasks will be created
 * on startup, according to the {@link #setConcurrentConsumers "concurrentConsumers"}
 * setting. Specify an alternative TaskExecutor to integrate with an existing
 * thread pool facility (such as a J2EE server's), for example using a
 * {@link org.springframework.scheduling.commonj.WorkManagerTaskExecutor CommonJ WorkManager}.
 * With a native JMS setup, each of those listener threads is going to use a
 * cached JMS Session and MessageConsumer (only refreshed in case of failure),
 * using the JMS provider's resources as efficiently as possible.
 *
 * <p>Message reception and listener execution can automatically be wrapped
 * in transactions through passing a Spring
 * {@link org.springframework.transaction.PlatformTransactionManager} into the
 * {@link #setTransactionManager "transactionManager"} property. This will usually
 * be a {@link org.springframework.transaction.jta.JtaTransactionManager} in a
 * J2EE enviroment, in combination with a JTA-aware JMS ConnectionFactory obtained
 * from JNDI (check your J2EE server's documentation). Note that this listener
 * container will automatically reobtain all JMS handles for each transaction
 * in case of an external transaction manager specified, for compatibility with
 * all J2EE servers (in particular JBoss). This non-caching behavior can be
 * overridden through the {@link #setCacheLevel "cacheLevel"} /
 * {@link #setCacheLevelName "cacheLevelName"} property, enforcing caching
 * of the Connection (or also Session and MessageConsumer) even in case of
 * an external transaction manager being involved.
 *
 * <p>Dynamic scaling of the number of concurrent invokers can be activated
 * through specifying a {@link #setMaxConcurrentConsumers "maxConcurrentConsumers"}
 * value that is higher than the {@link #setConcurrentConsumers "concurrentConsumers"}
 * value. Since the latter's default is 1, you can also simply specify a
 * "maxConcurrentConsumers" of e.g. 5, which will lead to dynamic scaling up to
 * 5 concurrent consumers in case of increasing message load, as well as dynamic
 * shrinking back to the standard number of consumers once the load decreases.
 * Consider adapting the {@link #setIdleTaskExecutionLimit "idleTaskExecutionLimit"}
 * setting to control the lifespan of each new task, to avoid frequent scaling up
 * and down, in particular if the ConnectionFactory does not pool JMS Sessions
 * and/or the TaskExecutor does not pool threads (check your configuration!).
 * Note that dynamic scaling only really makes sense for a queue in the first
 * place; for a topic, you will typically stick with the default number of 1
 * consumer, else you'd receive the same message multiple times on the same node.
 *
 * <p><b>It is strongly recommended to either set {@link #setSessionTransacted
 * "sessionTransacted"} to "true" or specify an external {@link #setTransactionManager
 * "transactionManager"}.</b> See the {@link AbstractMessageListenerContainer}
 * javadoc for details on acknowledge modes and native transaction options,
 * as well as the {@link AbstractPollingMessageListenerContainer} javadoc
 * for details on configuring an external transaction manager.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see #setTransactionManager
 * @see #setCacheLevel
 * @see javax.jms.MessageConsumer#receive(long)
 * @see SimpleMessageListenerContainer
 * @see org.springframework.jms.listener.serversession.ServerSessionMessageListenerContainer
 */
public class DefaultMessageListenerContainer extends AbstractPollingMessageListenerContainer {

	/**
	 * Default thread name prefix: "DefaultMessageListenerContainer-".
	 */
	public static final String DEFAULT_THREAD_NAME_PREFIX =
			ClassUtils.getShortName(DefaultMessageListenerContainer.class) + "-";

	/**
	 * The default recovery interval: 5000 ms = 5 seconds.
	 */
	public static final long DEFAULT_RECOVERY_INTERVAL = 5000;


	/**
	 * Constant that indicates to cache no JMS resources at all.
	 * @see #setCacheLevel
	 */
	public static final int CACHE_NONE = 0;

	/**
	 * Constant that indicates to cache a shared JMS Connection.
	 * @see #setCacheLevel
	 */
	public static final int CACHE_CONNECTION = 1;

	/**
	 * Constant that indicates to cache a shared JMS Connection
	 * and a JMS Session for each listener thread.
	 * @see #setCacheLevel
	 */
	public static final int CACHE_SESSION = 2;

	/**
	 * Constant that indicates to cache a shared JMS Connection
	 * and a JMS Session for each listener thread, as well as
	 * a JMS MessageConsumer for each listener thread.
	 * @see #setCacheLevel
	 */
	public static final int CACHE_CONSUMER = 3;


	private static final Constants constants = new Constants(DefaultMessageListenerContainer.class);


	private TaskExecutor taskExecutor;

	private long recoveryInterval = DEFAULT_RECOVERY_INTERVAL;

	private Integer cacheLevel;

	private int concurrentConsumers = 1;

	private int maxConcurrentConsumers = 1;

	private int maxMessagesPerTask = Integer.MIN_VALUE;

	private int idleTaskExecutionLimit = 1;

	private final Set scheduledInvokers = new HashSet();

	private int activeInvokerCount = 0;

	private final Object activeInvokerMonitor = new Object();

	private Object currentRecoveryMarker = new Object();

	private final Object recoveryMonitor = new Object();


	/**
	 * Set the Spring TaskExecutor to use for running the listener threads.
	 * <p>Default is a {@link org.springframework.core.task.SimpleAsyncTaskExecutor},
	 * starting up a number of new threads, according to the specified number
	 * of concurrent consumers.
	 * <p>Specify an alternative TaskExecutor for integration with an existing
	 * thread pool. Note that this really only adds value if the threads are
	 * managed in a specific fashion, for example within a J2EE environment.
	 * A plain thread pool does not add much value, as this listener container
	 * will occupy a number of threads for its entire lifetime.
	 * @see #setConcurrentConsumers
	 * @see org.springframework.core.task.SimpleAsyncTaskExecutor
	 * @see org.springframework.scheduling.commonj.WorkManagerTaskExecutor
	 */
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	/**
	 * Specify the interval between recovery attempts, in <b>milliseconds</b>.
	 * The default is 5000 ms, that is, 5 seconds.
	 * @see #handleListenerSetupFailure
	 */
	public void setRecoveryInterval(long recoveryInterval) {
		this.recoveryInterval = recoveryInterval;
	}

	/**
	 * Specify the level of caching that this listener container is allowed to apply,
	 * in the form of the name of the corresponding constant: e.g. "CACHE_CONNECTION".
	 * @see #setCacheLevel
	 */
	public void setCacheLevelName(String constantName) throws IllegalArgumentException {
		if (constantName == null || !constantName.startsWith("CACHE_")) {
			throw new IllegalArgumentException("Only cache constants allowed");
		}
		setCacheLevel(constants.asNumber(constantName).intValue());
	}

	/**
	 * Specify the level of caching that this listener container is allowed to apply.
	 * <p>Default is CACHE_NONE if an external transaction manager has been specified
	 * (to reobtain all resources freshly within the scope of the external transaction),
	 * and CACHE_CONSUMER else (operating with local JMS resources).
	 * <p>Some J2EE servers only register their JMS resources with an ongoing XA
	 * transaction in case of a freshly obtained JMS Connection and Session,
	 * which is why this listener container does by default not cache any of those.
	 * However, if you want to optimize for a specific server, consider switching
	 * this setting to at least CACHE_CONNECTION or CACHE_SESSION even in
	 * conjunction with an external transaction manager.
	 * <p>Currently known servers that absolutely require CACHE_NONE for XA
	 * transaction processing: JBoss 4. For any others, consider raising the
	 * cache level.
	 * @see #CACHE_NONE
	 * @see #CACHE_CONNECTION
	 * @see #CACHE_SESSION
	 * @see #CACHE_CONSUMER
	 * @see #setCacheLevelName
	 * @see #setTransactionManager
	 */
	public void setCacheLevel(int cacheLevel) {
		this.cacheLevel = new Integer(cacheLevel);
	}

	/**
	 * Return the level of caching that this listener container is allowed to apply.
	 */
	public int getCacheLevel() {
		return (this.cacheLevel != null ? this.cacheLevel.intValue() : CACHE_NONE);
	}


	/**
	 * Specify the number of concurrent consumers to create. Default is 1.
	 * <p>Specifying a higher value for this setting will increase the standard
	 * level of scheduled concurrent consumers at runtime: This is effectively
	 * the minimum number of concurrent consumers which will be scheduled
	 * at any given time. This is a static setting; for dynamic scaling,
	 * consider specifying the "maxConcurrentConsumers" setting instead.
	 * <p>Raising the number of concurrent consumers is recommendable in order
	 * to scale the consumption of messages coming in from a queue. However,
	 * note that any ordering guarantees are lost once multiple consumers are
	 * registered. In general, stick with 1 consumer for low-volume queues.
	 * <p><b>Do not raise the number of concurrent consumers for a topic.</b>
	 * This would lead to concurrent consumption of the same message,
	 * which is hardly ever desirable.
	 * <p><b>This setting can be modified at runtime, for example through JMX.</b>
	 * @see #setMaxConcurrentConsumers
	 */
	public void setConcurrentConsumers(int concurrentConsumers) {
		Assert.isTrue(concurrentConsumers > 0, "'concurrentConsumers' value must be at least 1 (one)");
		synchronized (this.activeInvokerMonitor) {
			this.concurrentConsumers = concurrentConsumers;
			if (this.maxConcurrentConsumers < concurrentConsumers) {
				this.maxConcurrentConsumers = concurrentConsumers;
			}
		}
	}

	/**
	 * Return the "concurrentConsumer" setting.
	 * <p>This returns the currently configured "concurrentConsumers" value;
	 * the number of currently scheduled/active consumers might differ.
	 * @see #getScheduledConsumerCount()
	 * @see #getActiveConsumerCount()
	 */
	public final int getConcurrentConsumers() {
		synchronized (this.activeInvokerMonitor) {
			return this.concurrentConsumers;
		}
	}

	/**
	 * Specify the maximum number of concurrent consumers to create. Default is 1.
	 * <p>If this setting is higher than "concurrentConsumers", the listener container
	 * will dynamically schedule new consumers at runtime, provided that enough
	 * incoming messages are encountered. Once the load goes down again, the number of
	 * consumers will be reduced to the standard level ("concurrentConsumers") again.
	 * <p>Raising the number of concurrent consumers is recommendable in order
	 * to scale the consumption of messages coming in from a queue. However,
	 * note that any ordering guarantees are lost once multiple consumers are
	 * registered. In general, stick with 1 consumer for low-volume queues.
	 * <p><b>Do not raise the number of concurrent consumers for a topic.</b>
	 * This would lead to concurrent consumption of the same message,
	 * which is hardly ever desirable.
	 * <p><b>This setting can be modified at runtime, for example through JMX.</b>
	 * @see #setConcurrentConsumers
	 */
	public void setMaxConcurrentConsumers(int maxConcurrentConsumers) {
		Assert.isTrue(maxConcurrentConsumers > 0, "'maxConcurrentConsumers' value must be at least 1 (one)");
		synchronized (this.activeInvokerMonitor) {
			this.maxConcurrentConsumers =
					(maxConcurrentConsumers > this.concurrentConsumers ? maxConcurrentConsumers : this.concurrentConsumers);
		}
	}

	/**
	 * Return the "maxConcurrentConsumer" setting.
	 * <p>This returns the currently configured "maxConcurrentConsumers" value;
	 * the number of currently scheduled/active consumers might differ.
	 * @see #getScheduledConsumerCount()
	 * @see #getActiveConsumerCount()
	 */
	public final int getMaxConcurrentConsumers() {
		synchronized (this.activeInvokerMonitor) {
			return this.maxConcurrentConsumers;
		}
	}

	/**
	 * Specify the maximum number of messages to process in one task.
	 * More concretely, this limits the number of message reception attempts per
	 * task, which includes receive iterations that did not actually pick up a
	 * message until they hit their timeout (see "receiveTimeout" property).
	 * <p>Default is unlimited (-1) in case of a standard TaskExecutor,
	 * and 1 in case of a SchedulingTaskExecutor that indicates a preference for
	 * short-lived tasks. Specify a number of 10 to 100 messages to balance
	 * between extremely long-lived and extremely short-lived tasks here.
	 * <p>Long-lived tasks avoid frequent thread context switches through
	 * sticking with the same thread all the way through, while short-lived
	 * tasks allow thread pools to control the scheduling. Hence, thread
	 * pools will usually prefer short-lived tasks.
	 * <p><b>This setting can be modified at runtime, for example through JMX.</b>
	 * @see #setTaskExecutor
	 * @see #setReceiveTimeout
	 * @see org.springframework.scheduling.SchedulingTaskExecutor#prefersShortLivedTasks()
	 */
	public void setMaxMessagesPerTask(int maxMessagesPerTask) {
		Assert.isTrue(maxMessagesPerTask != 0, "'maxMessagesPerTask' must not be 0");
		synchronized (this.activeInvokerMonitor) {
			this.maxMessagesPerTask = maxMessagesPerTask;
		}
	}

	/**
	 * Return the maximum number of messages to process in one task.
	 */
	public int getMaxMessagesPerTask() {
		synchronized (this.activeInvokerMonitor) {
			return this.maxMessagesPerTask;
		}
	}

	/**
	 * Specify the limit for idle executions of a receive task, not having
	 * received any message within its execution. If this limit is reached,
	 * the task will shut down and leave receiving to other executing tasks
	 * (in case of dynamic scheduling; see the "maxConcurrentConsumers" setting).
	 * Default is 1.
	 * <p>Within each task execution, a number of message reception attempts
	 * (according to the "maxMessagesPerTask" setting) will each wait for an incoming
	 * message (according to the "receiveTimeout" setting). If all of those receive
	 * attempts in a given task return without a message, the task is considered
	 * idle with respect to received messages. Such a task may still be rescheduled;
	 * however, once it reached the specified "idleTaskExecutionLimit", it will
	 * shut down (in case of dynamic scaling).
	 * <p>Raise this limit if you encounter too frequent scaling up and down.
	 * With this limit being higher, an idle consumer will be kept around longer,
	 * avoiding the restart of a consumer once a new load of messages comes in.
	 * Alternatively, specify a higher "maxMessagePerTask" and/or "receiveTimeout" value,
	 * which will also lead to idle consumers being kept around for a longer time
	 * (while also increasing the average execution time of each scheduled task).
	 * <p><b>This setting can be modified at runtime, for example through JMX.</b>
	 * @see #setMaxMessagesPerTask
	 * @see #setReceiveTimeout
	 */
	public void setIdleTaskExecutionLimit(int idleTaskExecutionLimit) {
		Assert.isTrue(idleTaskExecutionLimit > 0, "'idleTaskExecutionLimit' must be 1 or higher");
		synchronized (this.activeInvokerMonitor) {
			this.idleTaskExecutionLimit = idleTaskExecutionLimit;
		}
	}

	/**
	 * Return the limit for idle executions of a receive task.
	 */
	public int getIdleTaskExecutionLimit() {
		synchronized (this.activeInvokerMonitor) {
			return this.idleTaskExecutionLimit;
		}
	}

	protected void validateConfiguration() {
		super.validateConfiguration();
		synchronized (this.activeInvokerMonitor) {
			if (isSubscriptionDurable() && this.concurrentConsumers != 1) {
				throw new IllegalArgumentException("Only 1 concurrent consumer supported for durable subscription");
			}
		}
	}


	//-------------------------------------------------------------------------
	// Implementation of AbstractMessageListenerContainer's template methods
	//-------------------------------------------------------------------------

	public void initialize() {
		// Adapt default cache level.
		if (getTransactionManager() != null) {
			if (this.cacheLevel == null) {
				this.cacheLevel = new Integer(CACHE_NONE);
			}
		}
		else {
			if (this.cacheLevel == null) {
				this.cacheLevel = new Integer(CACHE_CONSUMER);
			}
		}

		// Prepare taskExecutor and maxMessagesPerTask.
		synchronized (this.activeInvokerMonitor) {
			if (this.taskExecutor == null) {
				this.taskExecutor = createDefaultTaskExecutor();
			}
			else if (this.taskExecutor instanceof SchedulingTaskExecutor &&
					((SchedulingTaskExecutor) this.taskExecutor).prefersShortLivedTasks() &&
					this.maxMessagesPerTask == Integer.MIN_VALUE) {
				// TaskExecutor indicated a preference for short-lived tasks. According to
				// setMaxMessagesPerTask javadoc, we'll use 1 message per task in this case
				// unless the user specified a custom value.
				this.maxMessagesPerTask = 1;
			}
		}

		// Proceed with actual listener initialization.
		super.initialize();
	}

	/**
	 * Create a default TaskExecutor. Called if no explicit TaskExecutor has been specified.
	 * <p>The default implementation builds a {@link org.springframework.core.task.SimpleAsyncTaskExecutor}
	 * with the specified bean name (or the class name, if no bean name specified) as thread name prefix.
	 * @see org.springframework.core.task.SimpleAsyncTaskExecutor#SimpleAsyncTaskExecutor(String)
	 */
	protected TaskExecutor createDefaultTaskExecutor() {
		String beanName = getBeanName();
		String threadNamePrefix = (beanName != null ? beanName + "-" : DEFAULT_THREAD_NAME_PREFIX);
		return new SimpleAsyncTaskExecutor(threadNamePrefix);
	}

	/**
	 * Use a shared JMS Connection depending on the "cacheLevel" setting.
	 * @see #setCacheLevel
	 * @see #CACHE_CONNECTION
	 */
	protected final boolean sharedConnectionEnabled() {
		return (getCacheLevel() >= CACHE_CONNECTION);
	}

	/**
	 * Creates the specified number of concurrent consumers,
	 * in the form of a JMS Session plus associated MessageConsumer
	 * running in a separate thread.
	 * @see #scheduleNewInvoker
	 * @see #setTaskExecutor
	 */
	protected void doInitialize() throws JMSException {
		synchronized (this.activeInvokerMonitor) {
			for (int i = 0; i < this.concurrentConsumers; i++) {
				scheduleNewInvoker();
			}
		}
	}

	/**
	 * Re-executes the given task via this listener container's TaskExecutor.
	 * @see #setTaskExecutor
	 */
	protected void doRescheduleTask(Object task) {
		this.taskExecutor.execute((Runnable) task);
	}

	protected void messageReceived(Message message, Session session) {
		scheduleNewInvokerIfAppropriate();
	}

	/**
	 * Schedule a new invoker, increasing the total number of scheduled
	 * invokers for this listener container, but only if the specified
	 * "maxConcurrentConsumers" limit has not been reached yet, and only
	 * if this listener container does not currently have idle invokers
	 * that are waiting for new messages already.
	 * <p>Called once a message has been received, to scale up while
	 * processing the message in the invoker that originally received it.
	 * @see #setTaskExecutor
	 * @see #getMaxConcurrentConsumers()
	 */
	protected void scheduleNewInvokerIfAppropriate() {
		if (isRunning()) {
			synchronized (this.activeInvokerMonitor) {
				if (this.scheduledInvokers.size() < this.maxConcurrentConsumers && !hasIdleInvokers()) {
					scheduleNewInvoker();
					if (logger.isDebugEnabled()) {
						logger.debug("Raised scheduled invoker count: " + scheduledInvokers.size());
					}
				}
			}
		}
	}

	/**
	 * Schedule a new invoker, increasing the total number of scheduled
	 * invokers for this listener container.
	 */
	private void scheduleNewInvoker() {
		AsyncMessageListenerInvoker invoker = new AsyncMessageListenerInvoker();
		if (rescheduleTaskIfNecessary(invoker)) {
			// This should always be true, since we're only calling this when active.
			this.scheduledInvokers.add(invoker);
		}
	}

	/**
	 * Determine whether this listener container currently has any
	 * idle instances among its scheduled invokers.
	 */
	private boolean hasIdleInvokers() {
		for (Iterator it = this.scheduledInvokers.iterator(); it.hasNext();) {
			AsyncMessageListenerInvoker invoker = (AsyncMessageListenerInvoker) it.next();
			if (invoker.isIdle()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determine whether the current invoker should be rescheduled,
	 * given that it might not have received a message in a while.
	 * @param idleTaskExecutionCount the number of idle executions
	 * that this invoker task has already accumulated (in a row)
	 */
	private boolean shouldRescheduleInvoker(int idleTaskExecutionCount) {
		synchronized (this.activeInvokerMonitor) {
			boolean idle = (idleTaskExecutionCount >= this.idleTaskExecutionLimit);
			return (this.scheduledInvokers.size() <= (idle ? this.concurrentConsumers : this.maxConcurrentConsumers));
		}
	}

	/**
	 * Return the number of currently scheduled consumers.
	 * <p>This number will always be inbetween "concurrentConsumers" and
	 * "maxConcurrentConsumers", but might be higher than "activeConsumerCount"
	 * (in case of some consumers being scheduled but not executed at the moment).
	 * @see #getConcurrentConsumers()
	 * @see #getMaxConcurrentConsumers()
	 * @see #getActiveConsumerCount()
	 */
	public final int getScheduledConsumerCount() {
		synchronized (this.activeInvokerMonitor) {
			return this.scheduledInvokers.size();
		}
	}

	/**
	 * Return the number of currently active consumers.
	 * <p>This number will always be inbetween "concurrentConsumers" and
	 * "maxConcurrentConsumers", but might be lower than "scheduledConsumerCount".
	 * (in case of some consumers being scheduled but not executed at the moment).
	 * @see #getConcurrentConsumers()
	 * @see #getMaxConcurrentConsumers()
	 * @see #getActiveConsumerCount()
	 */
	public final int getActiveConsumerCount() {
		synchronized (this.activeInvokerMonitor) {
			return this.activeInvokerCount;
		}
	}


	/**
	 * Overridden to accept a failure in the initial setup - leaving it up to the
	 * asynchronous invokers to establish the shared Connection on first access.
	 * @see #refreshConnectionUntilSuccessful()
	 */
	protected void establishSharedConnection() {
		try {
			super.establishSharedConnection();
		}
		catch (JMSException ex) {
			logger.debug("Could not establish shared JMS Connection - " +
					"leaving it up to asynchronous invokers to establish a Connection as soon as possible", ex);
		}
	}

	/**
	 * This implementations proceeds even after an exception thrown from
	 * <code>Connection.start()</code>, relying on listeners to perform
	 * appropriate recovery.
	 */
	protected void startSharedConnection() {
		try {
			super.startSharedConnection();
		}
		catch (JMSException ex) {
			logger.debug("Connection start failed - relying on listeners to perform recovery", ex);
		}
	}

	/**
	 * This implementations proceeds even after an exception thrown from
	 * <code>Connection.stop()</code>, relying on listeners to perform
	 * appropriate recovery after a restart.
	 */
	protected void stopSharedConnection() {
		try {
			super.stopSharedConnection();
		}
		catch (JMSException ex) {
			logger.debug("Connection stop failed - relying on listeners to perform recovery after restart", ex);
		}
	}

	/**
	 * Handle the given exception that arose during setup of a listener.
	 * Called for every such exception in every concurrent listener.
	 * <p>The default implementation logs the exception at error level
	 * if not recovered yet, and at debug level if already recovered.
	 * Can be overridden in subclasses.
	 * @param ex the exception to handle
	 * @param alreadyRecovered whether a previously executing listener
	 * already recovered from the present listener setup failure
	 * (this usually indicates a follow-up failure than be ignored
	 * other than for debug log purposes)
	 * @see #recoverAfterListenerSetupFailure()
	 */
	protected void handleListenerSetupFailure(Throwable ex, boolean alreadyRecovered) {
		if (ex instanceof JMSException) {
			invokeExceptionListener((JMSException) ex);
		}
		if (ex instanceof SharedConnectionNotInitializedException) {
			if (!alreadyRecovered) {
				logger.debug("JMS message listener invoker needs to establish shared Connection");
			}
		}
		else {
			if (alreadyRecovered) {
				logger.debug("Setup of JMS message listener invoker failed - already recovered by other invoker", ex);
			}
			else {
				logger.error("Setup of JMS message listener invoker failed - trying to recover", ex);
			}
		}
	}

	/**
	 * Recover this listener container after a listener failed to set itself up,
	 * for example reestablishing the underlying Connection.
	 * <p>The default implementation delegates to DefaultMessageListenerContainer's
	 * recovery-capable {@link #refreshConnectionUntilSuccessful()} method, which will
	 * try to re-establish a Connection to the JMS provider both for the shared
	 * and the non-shared Connection case.
	 * @see #refreshConnectionUntilSuccessful()
	 * @see #refreshDestination()
	 */
	protected void recoverAfterListenerSetupFailure() {
		refreshConnectionUntilSuccessful();
		refreshDestination();
	}

	/**
	 * Refresh the underlying Connection, not returning before an attempt has been
	 * successful. Called in case of a shared Connection as well as without shared
	 * Connection, so either needs to operate on the shared Connection or on a
	 * temporary Connection that just gets established for validation purposes.
	 * <p>The default implementation retries until it successfully established a
	 * Connection, for as long as this message listener container is active.
	 * Applies the specified recovery interval between retries.
	 * @see #setRecoveryInterval
	 */
	protected void refreshConnectionUntilSuccessful() {
		while (isRunning()) {
			try {
				if (sharedConnectionEnabled()) {
					refreshSharedConnection();
					startSharedConnection();
				}
				else {
					Connection con = createConnection();
					JmsUtils.closeConnection(con);
				}
				logger.info("Successfully refreshed JMS Connection");
				break;
			}
			catch (Exception ex) {
				if (logger.isInfoEnabled()) {
					logger.info("Could not refresh JMS Connection - retrying in " + this.recoveryInterval + " ms", ex);
				}
			}
			sleepInbetweenRecoveryAttempts();
		}
	}

	/**
	 * Refresh the JMS destination that this listener container operates on.
	 * <p>Called after listener setup failure, assuming that a cached Destination
	 * object might have become invalid (a typical case on WebLogic JMS).
	 * <p>The default implementation removes the destination from a
	 * DestinationResolver's cache, in case of a CachingDestinationResolver.
	 * @see #setDestinationName
	 * @see org.springframework.jms.support.destination.CachingDestinationResolver
	 */
	protected void refreshDestination() {
		String destName = getDestinationName();
		if (destName != null) {
			DestinationResolver destResolver = getDestinationResolver();
			if (destResolver instanceof CachingDestinationResolver) {
				((CachingDestinationResolver) destResolver).removeFromCache(destName);
			}
		}
	}

	/**
	 * Sleep according to the specified recovery interval.
	 * Called inbetween recovery attempts.
	 */
	protected void sleepInbetweenRecoveryAttempts() {
		if (this.recoveryInterval > 0) {
			try {
				Thread.sleep(this.recoveryInterval);
			}
			catch (InterruptedException interEx) {
				// Re-interrupt current thread, to allow other threads to react.
				Thread.currentThread().interrupt();
			}
		}
	}


	/**
	 * Destroy the registered JMS Sessions and associated MessageConsumers.
	 */
	protected void doShutdown() throws JMSException {
		logger.debug("Waiting for shutdown of message listener invokers");
		synchronized (this.activeInvokerMonitor) {
			while (this.activeInvokerCount > 0) {
				if (logger.isDebugEnabled()) {
					logger.debug("Still waiting for shutdown of " + this.activeInvokerCount +
							" message listener invokers");
				}
				try {
					this.activeInvokerMonitor.wait();
				}
				catch (InterruptedException interEx) {
					// Re-interrupt current thread, to allow other threads to react.
					Thread.currentThread().interrupt();
				}
			}
		}
	}


	//-------------------------------------------------------------------------
	// Inner classes used as internal adapters
	//-------------------------------------------------------------------------

	/**
	 * Runnable that performs looped <code>MessageConsumer.receive()</code> calls.
	 */
	private class AsyncMessageListenerInvoker implements SchedulingAwareRunnable {

		private Session session;

		private MessageConsumer consumer;

		private Object lastRecoveryMarker;

		private boolean lastMessageSucceeded;

		private int idleTaskExecutionCount = 0;

		private volatile boolean idle = true;

		public void run() {
			synchronized (activeInvokerMonitor) {
				activeInvokerCount++;
				activeInvokerMonitor.notifyAll();
			}
			boolean messageReceived = false;
			try {
				if (maxMessagesPerTask < 0) {
					while (isActive()) {
						waitWhileNotRunning();
						if (isActive()) {
							messageReceived = invokeListener();
						}
					}
				}
				else {
					int messageCount = 0;
					while (isRunning() && messageCount < maxMessagesPerTask) {
						messageReceived = (invokeListener() || messageReceived);
						messageCount++;
					}
				}
			}
			catch (Throwable ex) {
				clearResources();
				if (!this.lastMessageSucceeded) {
					// We failed more than once in a row - sleep for recovery interval
					// even before first recovery attempt.
					sleepInbetweenRecoveryAttempts();
				}
				this.lastMessageSucceeded = false;
				boolean alreadyRecovered = false;
				synchronized (recoveryMonitor) {
					if (this.lastRecoveryMarker == currentRecoveryMarker) {
						handleListenerSetupFailure(ex, false);
						recoverAfterListenerSetupFailure();
						currentRecoveryMarker = new Object();
					}
					else {
						alreadyRecovered = true;
					}
				}
				if (alreadyRecovered) {
					handleListenerSetupFailure(ex, true);
				}
			}
			synchronized (activeInvokerMonitor) {
				activeInvokerCount--;
				activeInvokerMonitor.notifyAll();
			}
			if (!messageReceived) {
				this.idleTaskExecutionCount++;
			}
			else {
				this.idleTaskExecutionCount = 0;
			}
			if (!shouldRescheduleInvoker(this.idleTaskExecutionCount) || !rescheduleTaskIfNecessary(this)) {
				// We're shutting down completely.
				synchronized (activeInvokerMonitor) {
					scheduledInvokers.remove(this);
					if (logger.isDebugEnabled()) {
						logger.debug("Lowered scheduled invoker count: " + scheduledInvokers.size());
					}
					activeInvokerMonitor.notifyAll();
				}
				clearResources();
			}
		}

		private boolean invokeListener() throws JMSException {
			initResourcesIfNecessary();
			boolean messageReceived = receiveAndExecute(this.session, this.consumer);
			this.lastMessageSucceeded = true;
			this.idle = !messageReceived;
			return messageReceived;
		}

		private void initResourcesIfNecessary() throws JMSException {
			if (getCacheLevel() <= CACHE_CONNECTION) {
				updateRecoveryMarker();
			}
			else {
				if (this.session == null && getCacheLevel() >= CACHE_SESSION) {
					updateRecoveryMarker();
					this.session = createSession(getSharedConnection());
				}
				if (this.consumer == null && getCacheLevel() >= CACHE_CONSUMER) {
					this.consumer = createListenerConsumer(this.session);
				}
			}
		}

		private void updateRecoveryMarker() {
			synchronized (recoveryMonitor) {
				this.lastRecoveryMarker = currentRecoveryMarker;
			}
		}

		private void clearResources() {
			if (sharedConnectionEnabled()) {
				synchronized (sharedConnectionMonitor) {
					JmsUtils.closeMessageConsumer(this.consumer);
					JmsUtils.closeSession(this.session);
				}
			}
			else {
				JmsUtils.closeMessageConsumer(this.consumer);
				JmsUtils.closeSession(this.session);
			}
			this.consumer = null;
			this.session = null;
		}

		public boolean isLongLived() {
			return (maxMessagesPerTask < 0);
		}

		public boolean isIdle() {
			return this.idle;
		}
	}

}
