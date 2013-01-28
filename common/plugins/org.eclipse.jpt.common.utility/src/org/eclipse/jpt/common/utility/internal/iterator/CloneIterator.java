/*******************************************************************************
 * Copyright (c) 2005, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal.iterator;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import org.eclipse.jpt.common.utility.internal.ObjectTools;

/**
 * A <code>CloneIterator</code> iterates over a copy of a collection,
 * allowing for concurrent access to the original collection.
 * <p>
 * The original collection passed to the <code>CloneIterator</code>'s
 * constructor should be synchronized (e.g. {@link java.util.Vector});
 * otherwise you run the risk of a corrupted collection.
 * <p>
 * By default, a <code>CloneIterator</code> does not support the
 * {@link #remove()} operation; this is because it does not have
 * access to the original collection. But if the <code>CloneIterator</code>
 * is supplied with an {@link Remover} it will delegate the
 * {@link #remove()} operation to the {@link Remover}.
 * 
 * @param <E> the type of elements returned by the iterator
 * 
 * @see org.eclipse.jpt.common.utility.internal.iterable.IterableTools#liveCloneIterable(Collection)
 * @see org.eclipse.jpt.common.utility.internal.iterable.IterableTools#liveCloneIterable(Collection, Remover)
 * @see org.eclipse.jpt.common.utility.internal.iterable.IterableTools#snapshotCloneIterable(Collection)
 * @see org.eclipse.jpt.common.utility.internal.iterable.IterableTools#snapshotCloneIterable(Collection, Remover)
 */
public class CloneIterator<E>
	implements Iterator<E>
{
	private final Iterator<Object> iterator;
	private E current;
	private final Remover<? super E> remover;
	private boolean removeAllowed;


	// ********** constructors **********

	/**
	 * Construct an iterator on a copy of the specified collection.
	 * The {@link #remove()} method will not be supported.
	 */
	public CloneIterator(Collection<? extends E> collection) {
		this(collection, Remover.ReadOnly.<E>instance());
	}

	/**
	 * Construct an iterator on a copy of the specified array.
	 * The {@link #remove()} method will not be supported.
	 */
	public CloneIterator(E[] array) {
		this(array, Remover.ReadOnly.<E>instance());
	}

	/**
	 * Construct an iterator on a copy of the specified collection.
	 * Use the specified remover to remove objects from the
	 * original collection.
	 */
	public CloneIterator(Collection<? extends E> collection, Remover<? super E> remover) {
		this(remover, collection.toArray());
	}

	/**
	 * Construct an iterator on a copy of the specified array.
	 * Use the specified remover to remove objects from the
	 * original array.
	 */
	public CloneIterator(E[] array, Remover<? super E> remover) {
		this(remover, array.clone());
	}

	/**
	 * Internal constructor used by subclasses.
	 * Swap order of arguments to prevent collision with other constructor.
	 * The passed in array will *not* be cloned.
	 */
	protected CloneIterator(Remover<? super E> remover, Object... array) {
		super();
		if (remover == null) {
			throw new NullPointerException();
		}
		this.iterator = new ArrayIterator<Object>(array);
		this.current = null;
		this.remover = remover;
		this.removeAllowed = false;
	}


	// ********** Iterator implementation **********

	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	public E next() {
		this.current = this.nestedNext();
		this.removeAllowed = true;
		return this.current;
	}

	public void remove() {
		if ( ! this.removeAllowed) {
			throw new IllegalStateException();
		}
		this.remover.remove(this.current);
		this.removeAllowed = false;
	}


	// ********** internal methods **********

	/**
	 * The collection passed in during construction held elements of type <code>E</code>,
	 * so this cast is not a problem. We need this cast because
	 * all the elements of the original collection were copied into
	 * an object array (<code>Object[]</code>).
	 */
	@SuppressWarnings("unchecked")
	protected E nestedNext() {
		return (E) this.iterator.next();
	}

	@Override
	public String toString() {
		return ObjectTools.toString(this);
	}


	//********** member interface **********

	/**
	 * Used by {@link CloneIterator} to remove
	 * elements from the original collection; since the iterator
	 * does not have direct access to the original collection.
	 */
	public interface Remover<T> {

		/**
		 * Remove the specified object from the original collection.
		 */
		void remove(T element);


		final class ReadOnly<S>
			implements Remover<S>, Serializable
		{
			@SuppressWarnings("rawtypes")
			public static final Remover INSTANCE = new ReadOnly();
			@SuppressWarnings("unchecked")
			public static <R> Remover<R> instance() {
				return INSTANCE;
			}
			// ensure single instance
			private ReadOnly() {
				super();
			}
			// remove is not supported
			public void remove(Object element) {
				throw new UnsupportedOperationException();
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
}
