/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.internal.iterables;

import org.eclipse.jpt.utility.internal.iterators.CloneIterator;

/**
 * Pull together remover state and behavior for subclasses.
 * 
 * @param <E> the type of elements returned by the iterable's iterator
 * 
 * @see SnapshotCloneIterable
 * @see LiveCloneIterable
 */
public abstract class CloneIterable<E>
	implements Iterable<E>
{
	final CloneIterator.Remover<E> remover;


	// ********** constructors **********

	protected CloneIterable() {
		super();
		this.remover = this.buildDefaultRemover();
	}

	protected CloneIterable(CloneIterator.Remover<E> remover) {
		super();
		this.remover = remover;
	}

	protected CloneIterator.Remover<E> buildDefaultRemover() {
		return new DefaultRemover();
	}


	// ********** default removal **********

	/**
	 * Remove the specified element from the original collection.
	 * <p>
	 * This method can be overridden by a subclass as an
	 * alternative to building a {@link CloneIterator.Remover}.
	 */
	protected void remove(@SuppressWarnings("unused") E element) {
		throw new RuntimeException("This method was not overridden."); //$NON-NLS-1$
	}


	//********** default mutator **********

	protected class DefaultRemover implements CloneIterator.Remover<E> {
		public void remove(E element) {
			CloneIterable.this.remove(element);
		}
	}

}