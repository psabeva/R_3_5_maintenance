/*******************************************************************************
 * Copyright (c) 2005, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility;

import java.io.Serializable;
import org.eclipse.jpt.common.utility.internal.ObjectTools;

/**
 * Simple interface for allowing clients to pass an exception handler to a
 * service (e.g. to log the exception). This is particularly helpful if the
 * service executes on another, possibly inaccessible, thread.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface MultiThreadedExceptionHandler
	extends ExceptionHandler
{
	/**
	 * The specified exception was thrown while the specified thread was
	 * executing. Handle it appropriately.
	 */
	void handleException(Thread thread, Throwable t);

	/**
	 * Singleton implementation of the multi-threaded exception handler
	 * interface that does nothing with the exception.
	 */
	final class Null
		implements MultiThreadedExceptionHandler, Serializable
	{
		public static final ExceptionHandler INSTANCE = new Null();
		public static ExceptionHandler instance() {
			return INSTANCE;
		}
		// ensure single instance
		private Null() {
			super();
		}
		public void handleException(Thread thread, Throwable t) {
			// do nothing
		}
		public void handleException(Throwable t) {
			// do nothing
		}
		@Override
		public String toString() {
			return ObjectTools.singletonToString(this);
		}
		private static final long serialVersionUID = 1L;
		private Object readResolve() {
			// replace this object with the singleton
			return INSTANCE;
		}
	}

	/**
	 * Singleton implementation of the multi-threaded exception handler
	 * interface that wraps the exception in a runtime exception and
	 * throws the runtime exception.
	 */
	final class Runtime
		implements MultiThreadedExceptionHandler, Serializable
	{
		public static final ExceptionHandler INSTANCE = new Runtime();
		public static ExceptionHandler instance() {
			return INSTANCE;
		}
		// ensure single instance
		private Runtime() {
			super();
		}
		public void handleException(Thread thread, Throwable t) {
			this.handleException(t);
		}
		public void handleException(Throwable t) {
			t.printStackTrace();
			// re-throw the exception unchecked
			if (t instanceof RuntimeException) {
				throw (RuntimeException) t;
			}
			throw new RuntimeException(t);
		}
		@Override
		public String toString() {
			return ObjectTools.singletonToString(this);
		}
		private static final long serialVersionUID = 1L;
		private Object readResolve() {
			// replace this object with the singleton
			return INSTANCE;
		}
	}

	/**
	 * Singleton implementation of the multi-threaded exception handler
	 * interface that, like what happens with an unhandled exception
	 * (see {@link ThreadGroup#uncaughtException(Thread, Throwable)}),
	 * prints the exception's stack trace to {@link System#err the
	 * "standard" error output stream}.
	 */
	final class Default
		implements MultiThreadedExceptionHandler, Serializable
	{
		public static final ExceptionHandler INSTANCE = new Default();
		public static ExceptionHandler instance() {
			return INSTANCE;
		}
		// ensure single instance
		private Default() {
			super();
		}
		public void handleException(Thread thread, Throwable t) {
			this.handleException(t);
		}
		public void handleException(Throwable t) {
			t.printStackTrace();
		}
		@Override
		public String toString() {
			return ObjectTools.singletonToString(this);
		}
		private static final long serialVersionUID = 1L;
		private Object readResolve() {
			// replace this object with the singleton
			return INSTANCE;
		}
	}
}