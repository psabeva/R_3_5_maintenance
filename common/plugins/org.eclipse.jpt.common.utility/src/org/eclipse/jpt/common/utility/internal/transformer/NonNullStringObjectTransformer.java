/*******************************************************************************
 * Copyright (c) 2012, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal.transformer;

import java.io.Serializable;
import org.eclipse.jpt.common.utility.internal.ObjectTools;
import org.eclipse.jpt.common.utility.transformer.Transformer;

/**
 * Transform an object into the string returned by its {@link Object#toString()}
 * method. A <code>null</code> object is transformed into a client-configured
 * non-<code>null</code> string.
 * 
 * @param <I> input: the type of the object passed to the transformer
 */
public class NonNullStringObjectTransformer<I>
	implements Transformer<I, String>, Serializable
{
	// not null
	private String nullString;

	private static final long serialVersionUID = 1L;

	public NonNullStringObjectTransformer(String nullString) {
		super();
		if (nullString == null) {
			throw new NullPointerException();
		}
		this.nullString = nullString;
	}

	public String transform(I o) {
		return (o == null) ? this.nullString : o.toString();
	}

	@Override
	public String toString() {
		return ObjectTools.toString(this, this.nullString);
	}
}