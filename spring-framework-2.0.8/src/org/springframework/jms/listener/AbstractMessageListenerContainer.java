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

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import org.springframework.jms.support.JmsUtils;
import org.springframework.util.Assert;

/**
 * Abstract base class for message listener containers. Can either host
 * a standard JMS {@link javax.jms.MessageListener} or a Spring-specific
 * {@link SessionAwareMessageListener}.
 *
 * <p>Usually holds a single JMS {@link Connection} that all listeners are
 * supposed to be registered on, which is the standard JMS way of managing
 * listeners. Can alternatively also be used with a fresh Connection per
 * listener, for J2EE-style XA-aware JMS messaging. The actual registration
 * process is up to concrete subclasses.
 *
 * <p><b>NOTE:</b> The default behavior of this message listener container
 * is to <b>never</b> propagate an exception thrown by a message listener up to
 * the JMS provider. Instead, it will log any such exception at the error level.
 * This means that from the perspective of the attendant JMS provider no such
 * listener will ever fail.
 *
 * <p>The listener container offers the following message acknowledgment options:
 * <ul>
 * <li>"sessionAcknowledgeMode" set to "AUTO_ACKNOWLEDGE" (default):
 * Automatic message acknowledgment <i>before</i> listener execution;
 * no redelivery in case of exception thrown.
 * <li>"sessionAcknowledgeMode" set to "CLIENT_ACKNOWLEDGE":
 * Automatic message acknowledgment <i>after</i> successful listener execution;
 * no redelivery in case of exception thrown.
 * <li>"sessionAcknowledgeMode" set to "DUPS_OK_ACKNOWLEDGE":
 * <i>Lazy</i> message acknowledgment during or after listener execution;
 * <i>potential redelivery</i> in case of exception thrown.
 * <li>"sessionTransacted" set to "true":
 * Transactional acknowledgment after successful listener execution;
 * <i>guaranteed redelivery</i> in case of exception thrown.
 * </ul>
 * The exact behavior might vary according to the concrete listener container
 * and JMS provider used.
 *
 * <b>NOTE:</b> The default behavior of this message listener container is to
 * <b>never</b> propagate an exception thrown by a message listener up to the
 * JMS provider. Instead, it will log any such exception at the error level and
 * rollback the active transaction if there is one. This means that from the
 * perspective of the attendant JMS provider no listener will ever fail.
 *
 * <p>There are two solutions to the duplicate processing problem:
 * <ul>
 * <li>Either add <i>duplicate message detection</i> to your listener, in the
 * form of a business entity existence check or a protocol table check. This
 * usually just needs to be done in case of the JMSRedelivered flag being
 * set on the incoming message (else just process straightforwardly).
 * <li>Or wrap the <i>entire processing with an XA transaction</i>, covering the
 * reception of the message as well as the execution of the message listener.
 * This is only supported by {@link DefaultMessageListenerContainer}, through
 * specifying a "transactionManager" (typically a
 * {@link org.springframework.transaction.jta.JtaTransactionManager}, with
 * a corresponding XA-aware JMS {@link javax.jms.ConnectionFactory} passed in as
 * "connectionFactory").
 * </ul>
 * Note that XA transaction coordination adds significant runtime overhead,
 * so it might be feasible to avoid it unless absolutely necessary.
 *
 * <p><b>Recommendations:</b>
 * <ul>
 * <li>The general recommendation is to set "sessionTransacted" to "true",
 * typically in combination with local database transactions triggered by the
 * listener implementation, through Spring's standard transaction facilities.
 * This will work nicely in Tomcat or in a standalone environment, often
 * combined with custom duplicate message detection (if it is unacceptable
 * to ever process the same message twice).
 * <li>Alternatively, specify a
 * {@link org.springframework.transaction.jta.JtaTransactionManager} as
 * "transactionManager" for a fully XA-aware JMS provider - typically when
 * running on a J2EE server, but also for other environments with a JTA
 * transaction manager present. This will give full "exactly-once" guarantees
 * without custom duplicate message checks, at the price of additional
 * runtime processing overhead.
 * </ul>
 *
 * <p>Note that it is also possible to specify a
 * {@link org.springframework.jms.connection.JmsTransactionManager} as external
 * "transactionManager", providing fully synchronized Spring transactions based
 * on local JMS transactions. The effect is similar to "sessionTransacted" set
 * to "true", the difference being that this external transaction management
 * will also affect independent JMS access code within the service layer
 * (e.g. based on {@link org.springframework.jms.core.JmsTemplate} or
 * {@link org.springframework.jms.connection.TransactionAwareConnectionFactoryProxy}),
 * not just direct JMS Session usage in a {@link SessionAwareMessageListener}.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see #setMessageListener
 * @see javax.jms.MessageListener
 * @see SessionAwareMessageListener
 * @see #handleListenerException
 * @see DefaultMessageListenerContainer
 * @see SimpleMessageListenerContainer
 * @see org.springframework.jms.listener.serversession.ServerSessionMessageListenerContainer
 */
public abstract class AbstractMessageListenerContainer extends AbstractJmsListeningContainer {

	private volatile Object destination;

	private volatile String messageSelector;

	private volatile Object messageListener;

	private boolean subscriptionDurable = false;

	private String durableSubscriptionName;

	private ExceptionListener exceptionListener;

	private boolean exposeListenerSession = true;

	private boolean acceptMessagesWhileStopping = false;


	/**
	 * Set the destination to receive messages from.
	 * <p>Alternatively, specify a "destinationName", to be dynamically
	 * resolved via the {@link org.springframework.jms.support.destination.DestinationResolver}.
	 * @see #setDestinationName(String)
	 */
	public void setDestination(Destination destination) {
		Assert.notNull(destination, "'destination' must not be null");
		this.destination = destination;
		if (destination instanceof Topic && !(destination instanceof Queue)) {
			// Clearly a Topic: let's se the "pubSubDomain" flag.
			setPubSubDomain(true);
		}
	}

	/**
	 * Return the destination to receive messages from. Will be <code>null</code>
	 * if the configured destination is not an actual {@link Destination} type;
	 * c.f. {@link #setDestinationName(String) when the destination is a String}.
	 */
	public Destination getDestination() {
		return (this.destination instanceof Destination ? (Destination) this.destination : null);
	}

	/**
	 * Set the name of the destination to receive messages from.
	 * <p>The specified name will be dynamically resolved via the configured
	 * {@link #setDestinationResolver destination resolver}.
	 * <p>Alternatively, specify a JMS {@link Destination} object as "destination".
	 * @param destinationName the desired destination (can be <code>null</code>)
	 * @see #setDestination(javax.jms.Destination)
	 */
	public void setDestinationName(String destinationName) {
		Assert.notNull(destinationName, "'destinationName' must not be null");
		this.destination = destinationName;
	}

	/**
	 * Return the name of the destination to receive messages from.
	 * Will be <code>null</code> if the configured destination is not a
	 * {@link String} type; c.f. {@link #setDestination(Destination) when
	 * it is an actual Destination}.
	 */
	public String getDestinationName() {
		return (this.destination instanceof String ? (String) this.destination : null);
	}

	/**
	 * Set the JMS message selector expression (or <code>null</code> if none).
	 * Default is none.
	 * <p>See the JMS specification for a detailed definition of selector expressions.
	 */
	public void setMessageSelector(String messageSelector) {
		this.messageSelector = messageSelector;
	}

	/**
	 * Return the JMS message selector expression (or <code>null</code> if none).
	 */
	public String getMessageSelector() {
		return this.messageSelector;
	}


	/**
	 * Set the message listener implementation to register.
	 * This can be either a standard JMS {@link MessageListener} object
	 * or a Spring {@link SessionAwareMessageListener} object.
	 * @throws IllegalArgumentException if the supplied listener is not a
	 * {@link MessageListener} or a {@link SessionAwareMessageListener}
	 * @see javax.jms.MessageListener
	 * @see SessionAwareMessageListener
	 */
	public void setMessageListener(Object messageListener) {
		checkMessageListener(messageListener);
		this.messageListener = messageListener;
		if (this.durableSubscriptionName == null) {
			// Use message listener class name as default name for a durable subscription.
			this.durableSubscriptionName = messageListener.getClass().getName();
		}
	}

	/**
	 * Check the given message listener, throwing an exception
	 * if it does not correspond to a supported listener type.
	 * <p>By default, only a standard JMS {@link MessageListener} object or a
	 * Spring {@link SessionAwareMessageListener} object will be accepted.
	 * @param messageListener the message listener object to check
	 * @throws IllegalArgumentException if the supplied listener is not a
	 * {@link MessageListener} or a {@link SessionAwareMessageListener}
	 * @see javax.jms.MessageListener
	 * @see SessionAwareMessageListener
	 */
	protected void checkMessageListener(Object messageListener) {
		if (!(messageListener instanceof MessageListener ||
				messageListener instanceof SessionAwareMessageListener)) {
			throw new IllegalArgumentException(
					"Message listener needs to be of type [" + MessageListener.class.getName() +
					"] or [" + SessionAwareMessageListener.class.getName() + "]");
		}
	}

	/**
	 * Return the message listener object to register.
	 */
	public Object getMessageListener() {
		return this.messageListener;
	}

	/**
	 * Set whether to make the subscription durable. The durable subscription name
	 * to be used can be specified through the "durableSubscriptionName" property.
	 * <p>Default is "false". Set this to "true" to register a durable subscription,
	 * typically in combination with a "durableSubscriptionName" value (unless
	 * your message listener class name is good enough as subscription name).
	 * <p>Only makes sense when listening to a topic (pub-sub domain).
	 * @see #setDurableSubscriptionName
	 */
	public void setSubscriptionDurable(boolean subscriptionDurable) {
		this.subscriptionDurable = subscriptionDurable;
	}

	/**
	 * Return whether to make the subscription durable.
	 */
	public boolean isSubscriptionDurable() {
		return this.subscriptionDurable;
	}

	/**
	 * Set the name of a durable subscription to create. To be applied in case
	 * of a topic (pub-sub domain) with subscription durability activated.
	 * <p>The durable subscription name needs to be unique within this client's
	 * JMS client id. Default is the class name of the specified message listener.
	 * <p>Note: Only 1 concurrent consumer (which is the default of this
	 * message listener container) is allowed for each durable subscription.
	 * @see #setSubscriptionDurable
	 * @see #setClientId
	 * @see #setMessageListener
	 */
	public void setDurableSubscriptionName(String durableSubscriptionName) {
		this.durableSubscriptionName = durableSubscriptionName;
	}

	/**
	 * Return the name of a durable subscription to create, if any.
	 */
	public String getDurableSubscriptionName() {
		return this.durableSubscriptionName;
	}

	/**
	 * Set the JMS ExceptionListener to notify in case of a JMSException thrown
	 * by the registered message listener or the invocation infrastructure.
	 */
	public void setExceptionListener(ExceptionListener exceptionListener) {
		this.exceptionListener = exceptionListener;
	}

	/**
	 * Return the JMS ExceptionListener to notify in case of a JMSException thrown
	 * by the registered message listener or the invocation infrastructure, if any.
	 */
	public ExceptionListener getExceptionListener() {
		return this.exceptionListener;
	}

	/**
	 * Set whether to expose the listener JMS Session to a registered
	 * {@link SessionAwareMessageListener}. Default is "true", reusing
	 * the listener's {@link Session}.
	 * <p>Turn this off to expose a fresh JMS Session fetched from the same
	 * underlying JMS {@link Connection} instead, which might be necessary
	 * on some JMS providers.
	 * @see SessionAwareMessageListener
	 */
	public void setExposeListenerSession(boolean exposeListenerSession) {
		this.exposeListenerSession = exposeListenerSession;
	}

	/**
	 * Return whether to expose the listener JMS {@link Session} to a
	 * registered {@link SessionAwareMessageListener}.
	 */
	public boolean isExposeListenerSession() {
		return this.exposeListenerSession;
	}

	/**
	 * Set whether to accept received messages while the listener container
	 * in the process of stopping.
	 * <p>Default is "false", rejecting such messages through aborting the
	 * receive attempt. Switch this flag on to fully process such messages
	 * even in the stopping phase, with the drawback that even newly sent
	 * messages might still get processed (if coming in before all receive
	 * timeouts have expired).
	 * <p><b>NOTE:</b> Aborting receive attempts for such incoming messages
	 * might lead to the provider's retry count decreasing for the affected
	 * messages. If you have a high number of concurrent consumers, make sure
	 * that the number of retries is higher than the number of consumers,
	 * to be on the safe side for all potential stopping scenarios.
	 */
	public void setAcceptMessagesWhileStopping(boolean acceptMessagesWhileStopping) {
		this.acceptMessagesWhileStopping = acceptMessagesWhileStopping;
	}

	/**
	 * Return whether to accept received messages while the listener container
	 * in the process of stopping.
	 */
	public boolean isAcceptMessagesWhileStopping() {
		return this.acceptMessagesWhileStopping;
	}

	protected void validateConfiguration() {
		if (this.destination == null) {
			throw new IllegalArgumentException("Property 'destination' or 'destinationName' is required");
		}
		if (isSubscriptionDurable() && !isPubSubDomain()) {
			throw new IllegalArgumentException("A durable subscription requires a topic (pub-sub domain)");
		}
	}


	//-------------------------------------------------------------------------
	// Template methods for listener execution
	//-------------------------------------------------------------------------

	/**
	 * Execute the specified listener,
	 * committing or rolling back the transaction afterwards (if necessary).
	 * @param session the JMS Session to operate on
	 * @param message the received JMS Message
	 * @see #invokeListener
	 * @see #commitIfNecessary
	 * @see #rollbackOnExceptionIfNecessary
	 * @see #handleListenerException
	 */
	protected void executeListener(Session session, Message message) {
		try {
			doExecuteListener(session, message);
		}
		catch (Throwable ex) {
			handleListenerException(ex);
		}
	}

	/**
	 * Execute the specified listener,
	 * committing or rolling back the transaction afterwards (if necessary).
	 * @param session the JMS Session to operate on
	 * @param message the received JMS Message
	 * @throws JMSException if thrown by JMS API methods
	 * @see #invokeListener
	 * @see #commitIfNecessary
	 * @see #rollbackOnExceptionIfNecessary
	 * @see #convertJmsAccessException
	 */
	protected void doExecuteListener(Session session, Message message) throws JMSException {
		if (!isAcceptMessagesWhileStopping() && !isRunning()) {
			if (logger.isWarnEnabled()) {
				logger.warn("Rejecting received message because of the listener container " +
						"having been stopped in the meantime: " + message);
			}
			rollbackIfNecessary(session);
			throw new MessageRejectedWhileStoppingException();
		}
		try {
			invokeListener(session, message);
		}
		catch (JMSException ex) {
			rollbackOnExceptionIfNecessary(session, ex);
			throw ex;
		}
		catch (RuntimeException ex) {
			rollbackOnExceptionIfNecessary(session, ex);
			throw ex;
		}
		catch (Error err) {
			rollbackOnExceptionIfNecessary(session, err);
			throw err;
		}
		commitIfNecessary(session, message);
	}

	/**
	 * Invoke the specified listener: either as standard JMS MessageListener
	 * or (preferably) as Spring SessionAwareMessageListener.
	 * @param session the JMS Session to operate on
	 * @param message the received JMS Message
	 * @throws JMSException if thrown by JMS API methods
	 * @see #setMessageListener
	 */
	protected void invokeListener(Session session, Message message) throws JMSException {
		Object listener = getMessageListener();
		if (listener instanceof SessionAwareMessageListener) {
			doInvokeListener((SessionAwareMessageListener) listener, session, message);
		}
		else if (listener instanceof MessageListener) {
			doInvokeListener((MessageListener) listener, message);
		}
		else if (listener != null) {
			throw new IllegalArgumentException(
					"Only MessageListener and SessionAwareMessageListener supported: " + listener);
		}
		else {
			throw new IllegalStateException("No message listener specified - see property 'messageListener'");
		}
	}

	/**
	 * Invoke the specified listener as Spring SessionAwareMessageListener,
	 * exposing a new JMS Session (potentially with its own transaction)
	 * to the listener if demanded.
	 * @param listener the Spring SessionAwareMessageListener to invoke
	 * @param session the JMS Session to operate on
	 * @param message the received JMS Message
	 * @throws JMSException if thrown by JMS API methods
	 * @see SessionAwareMessageListener
	 * @see #setExposeListenerSession
	 */
	protected void doInvokeListener(SessionAwareMessageListener listener, Session session, Message message)
			throws JMSException {

		Connection conToClose = null;
		Session sessionToClose = null;
		try {
			Session sessionToUse = session;
			if (!isExposeListenerSession()) {
				// We need to expose a separate Session.
				conToClose = createConnection();
				sessionToClose = createSession(conToClose);
				sessionToUse = sessionToClose;
			}
			// Actually invoke the message listener...
			if (logger.isDebugEnabled()) {
				logger.debug("Invoking listener with message of type [" + message.getClass() +
						"] and session [" + sessionToUse + "]");
			}
			listener.onMessage(message, sessionToUse);
			// Clean up specially exposed Session, if any.
			if (sessionToUse != session) {
				if (sessionToUse.getTransacted() && isSessionLocallyTransacted(sessionToUse)) {
					// Transacted session created by this container -> commit.
					JmsUtils.commitIfNecessary(sessionToUse);
				}
			}
		}
		finally {
			JmsUtils.closeSession(sessionToClose);
			JmsUtils.closeConnection(conToClose);
		}
	}

	/**
	 * Invoke the specified listener as standard JMS MessageListener.
	 * <p>Default implementation performs a plain invocation of the
	 * <code>onMessage</code> method.
	 * @param listener the JMS MessageListener to invoke
	 * @param message the received JMS Message
	 * @throws JMSException if thrown by JMS API methods
	 * @see javax.jms.MessageListener#onMessage
	 */
	protected void doInvokeListener(MessageListener listener, Message message) throws JMSException {
		listener.onMessage(message);
	}

	/**
	 * Perform a commit or message acknowledgement, as appropriate.
	 * @param session the JMS Session to commit
	 * @param message the Message to acknowledge
	 * @throws javax.jms.JMSException in case of commit failure
	 */
	protected void commitIfNecessary(Session session, Message message) throws JMSException {
		// Commit session or acknowledge message.
		if (session.getTransacted()) {
			// Commit necessary - but avoid commit call within a JTA transaction.
			if (isSessionLocallyTransacted(session)) {
				// Transacted session created by this container -> commit.
				JmsUtils.commitIfNecessary(session);
			}
		}
		else if (isClientAcknowledge(session)) {
			message.acknowledge();
		}
	}

	/**
	 * Perform a rollback, if appropriate.
	 * @param session the JMS Session to rollback
	 * @throws javax.jms.JMSException in case of a rollback error
	 */
	protected void rollbackIfNecessary(Session session) throws JMSException {
		if (session.getTransacted() && isSessionLocallyTransacted(session)) {
			// Transacted session created by this container -> rollback.
			JmsUtils.rollbackIfNecessary(session);
		}
	}

	/**
	 * Perform a rollback, handling rollback exceptions properly.
	 * @param session the JMS Session to rollback
	 * @param ex the thrown application exception or error
	 * @throws javax.jms.JMSException in case of a rollback error
	 */
	protected void rollbackOnExceptionIfNecessary(Session session, Throwable ex) throws JMSException {
		try {
			if (session.getTransacted() && isSessionLocallyTransacted(session)) {
				// Transacted session created by this container -> rollback.
				if (logger.isDebugEnabled()) {
					logger.debug("Initiating transaction rollback on application exception", ex);
				}
				JmsUtils.rollbackIfNecessary(session);
			}
		}
		catch (IllegalStateException ex2) {
			logger.debug("Could not roll back because Session already closed", ex2);
		}
		catch (JMSException ex2) {
			logger.error("Application exception overridden by rollback exception", ex);
			throw ex2;
		}
		catch (RuntimeException ex2) {
			logger.error("Application exception overridden by rollback exception", ex);
			throw ex2;
		}
		catch (Error err) {
			logger.error("Application exception overridden by rollback error", ex);
			throw err;
		}
	}

	/**
	 * Check whether the given Session is locally transacted, that is, whether
	 * its transaction is managed by this listener container's Session handling
	 * and not by an external transaction coordinator.
	 * <p>Note: The Session's own transacted flag will already have been checked
	 * before. This method is about finding out whether the Session's transaction
	 * is local or externally coordinated.
	 * @param session the Session to check
	 * @return whether the given Session is locally transacted
	 * @see #isSessionTransacted()
	 * @see org.springframework.jms.connection.ConnectionFactoryUtils#isSessionTransactional
	 */
	protected boolean isSessionLocallyTransacted(Session session) {
		return isSessionTransacted();
	}

	/**
	 * Handle the given exception that arose during listener execution.
	 * <p>The default implementation logs the exception at error level,
	 * not propagating it to the JMS provider - assuming that all handling of
	 * acknowledgement and/or transactions is done by this listener container.
	 * This can be overridden in subclasses.
	 * @param ex the exception to handle
	 */
	protected void handleListenerException(Throwable ex) {
		if (ex instanceof MessageRejectedWhileStoppingException) {
			// Internal exception - has been handled before.
			return;
		}
		if (ex instanceof JMSException) {
			invokeExceptionListener((JMSException) ex);
		}
		if (isActive()) {
			// Regular case: failed while active.
			// Log at error level.
			logger.warn("Execution of JMS message listener failed", ex);
		}
		else {
			// Rare case: listener thread failed after container shutdown.
			// Log at debug level, to avoid spamming the shutdown log.
			logger.debug("Listener exception after container shutdown", ex);
		}
	}

	/**
	 * Invoke the registered JMS ExceptionListener, if any.
	 * @param ex the exception that arose during JMS processing
	 * @see #setExceptionListener
	 */
	protected void invokeExceptionListener(JMSException ex) {
		ExceptionListener exceptionListener = getExceptionListener();
		if (exceptionListener != null) {
			exceptionListener.onException(ex);
		}
	}


	/**
	 * Internal exception class that indicates a rejected message on shutdown.
	 * Used to trigger a rollback for an external transaction manager in that case.
	 */
	private static class MessageRejectedWhileStoppingException extends RuntimeException {

	}

}
