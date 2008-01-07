/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.internal.model.value;

import java.util.Iterator;

import org.eclipse.jpt.utility.internal.ClassTools;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;
import org.eclipse.jpt.utility.internal.model.AbstractModel;

/**
 * A read-only collection value model for when you
 * don't need to support a collection. In particular, this
 * is useful for the leaf nodes of a tree that never have
 * children.
 * 
 * We don't use a singleton because we hold on to listeners.
 */
public final class NullCollectionValueModel
	extends AbstractModel
	implements CollectionValueModel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public NullCollectionValueModel() {
		super();
	}
	

	// ********** CollectionValueModel implementation **********

	public int size() {
		return 0;
	}

	public Iterator iterator() {
		return EmptyIterator.instance();
	}


	// ********** Object overrides **********

    @Override
	public String toString() {
    	return ClassTools.shortClassNameForObject(this);
	}

}
