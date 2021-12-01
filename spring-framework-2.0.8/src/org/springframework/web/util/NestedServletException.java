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

package org.springframework.web.util;

import java.io.PrintStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import org.springframework.core.NestedExceptionUtils;

/**
 * Subclass of ServletException that properly handles a root cause in terms
 * of message and stacktrace, just like NestedChecked/RuntimeException does.
 * Note that the plain ServletException doesn't expose its root cause at all,
 * neither in the exception message nor in printed stack traces!
 *
 * <p>The similarity between this class and the NestedChecked/RuntimeException
 * class is unavoidable, as this class needs to derive from ServletException
 * and cannot derive from NestedCheckedException.
 *
 * @author Juergen Hoeller
 * @since 1.2.5
 * @see #getMessage
 * @see #printStackTrace
 * @see org.springframework.core.NestedCheckedException
 * @see org.springframework.core.NestedRuntimeException
 */
public class NestedServletException extends ServletException {

	/** Use serialVersionUID from Spring 1.2 for interoperability */
	private static final long serialVersionUID = -5292377985529381145L;


	/**
	 * Construct a <code>NestedServletException</code> with the specified detail message.
	 * @param msg the detail message
	 */
	public NestedServletException(String msg) {
		super(msg);
	}

	/**
	 * Construct a <code>NestedServletException</code> with the specified detail message
	 * and nested exception.
	 * @param msg the detail message
	 * @param cause the nested exception
	 */
	public NestedServletException(String msg, Throwable cause) {
		super(msg, cause);
	}


	/**
	 * Return the detail message, including the message from the nested exception
	 * if there is one.
	 */
	public String getMessage() {
		return NestedExceptionUtils.buildMessage(super.getMessage(), getRootCause());
	}

	/**
	 * Print the composite message and the embedded stack trace to the specified stream.
	 * @param ps the print stream
	 */
	public void printStackTrace(PrintStream ps) {
		Throwable cause = getRootCause();
		if (cause == null) {
			super.printStackTrace(ps);
		}
		else {
			ps.println(this);
			ps.print("Caused by: ");
			cause.printStackTrace(ps);
		}
	}

	/**
	 * Print the composite message and the embedded stack trace to the specified print writer.
	 * @param pw the print writer
	 */
	public void printStackTrace(PrintWriter pw) {
		Throwable cause = getRootCause();
		if (cause == null) {
			super.printStackTrace(pw);
		}
		else {
			pw.println(this);
			pw.print("Caused by: ");
			cause.printStackTrace(pw);
		}
	}

}
