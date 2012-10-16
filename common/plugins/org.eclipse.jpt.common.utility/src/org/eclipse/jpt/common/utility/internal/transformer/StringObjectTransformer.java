/*******************************************************************************
 * Copyright (c) 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal.transformer;

import java.io.Serializable;
import org.eclipse.jpt.common.utility.transformer.Transformer;

/**
 * Transform an object into the string returned by its {@link Object#toString()}
 * method. A <code>null</code> object is transformed into a <code>null</code>
 * string.
 */
public final class StringObjectTransformer<T>
	extends AbstractTransformer<T, String>
	implements Serializable
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final Transformer<?, String> INSTANCE = new StringObjectTransformer();

	@SuppressWarnings("unchecked")
	public static <R> Transformer<R, String> instance() {
		return (Transformer<R, String>) INSTANCE;
	}

	// ensure single instance
	private StringObjectTransformer() {
		super();
	}

	@Override
	protected String transform_(T o) {
		return o.toString();
	}

	private static final long serialVersionUID = 1L;
	private Object readResolve() {
		// replace this object with the singleton
		return INSTANCE;
	}
}