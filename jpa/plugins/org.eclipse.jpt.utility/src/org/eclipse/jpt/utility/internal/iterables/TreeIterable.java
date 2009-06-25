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

import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.jpt.utility.internal.iterators.TreeIterator;

/**
 * A <code>TreeIterable</code> simplifies the traversal of a
 * tree of objects, where the objects' protocol(s) provides
 * a method for getting the immediate children of the given
 * node but does not provide a method for getting all the
 * descendants (children, grandchildren, etc.) of the given node.
 * <p>
 * To use, supply:<ul>
 * <li> either the root element of the tree or, if the tree has
 * multiple roots, an <code>Iterable</code> of the set of roots
 * <li> a <code>Midwife</code> that delivers the children
 * of each child
 * (alternatively, subclass <code>TreeIterable</code>
 * and override the <code>children(Object)</code> method)
 * </ul>
 * <p>
 * 
 * @see TreeIterator
 */
public class TreeIterable<E>
	implements Iterable<E>
{
	private final Iterable<? extends E> roots;
	private final TreeIterator.Midwife<E> midwife;


	/**
	 * Construct an iterable containing the nodes of a tree with the specified root
	 * and a default midwife that calls back to the iterable.
	 * Use this constructor if you want to override the
	 * <code>children(Object)</code> method instead of building
	 * a <code>TreeIterator.Midwife</code>.
	 */
	public TreeIterable(E root) {
		this(new SingleElementIterable<E>(root));
	}

	/**
	 * Construct an iterable containing the nodes of a tree with the specified root
	 * and midwife.
	 */
	public TreeIterable(E root, TreeIterator.Midwife<E> midwife) {
		this(new SingleElementIterable<E>(root), midwife);
	}

	/**
	 * Construct an iterable containing the nodes of a tree with the specified roots
	 * and a default midwife that calls back to the iterable.
	 * Use this constructor if you want to override the
	 * <code>children(Object)</code> method instead of building
	 * a <code>TreeIterator.Midwife</code>.
	 */
	public TreeIterable(E... roots) {
		this(Arrays.asList(roots));
	}

	/**
	 * Construct an iterable containing the nodes of a tree with the specified roots
	 * and midwife.
	 */
	public TreeIterable(E[] roots, TreeIterator.Midwife<E> midwife) {
		this(Arrays.asList(roots), midwife);
	}

	/**
	 * Construct an iterable containing the nodes of a tree with the specified roots
	 * and a default midwife that calls back to the iterable.
	 * Use this constructor if you want to override the
	 * <code>children(Object)</code> method instead of building
	 * a <code>TreeIterator.Midwife</code>.
	 */
	public TreeIterable(Iterable<? extends E> roots) {
		super();
		this.roots = roots;
		this.midwife = this.buildDefaultMidwife();
	}

	/**
	 * Construct an iterable containing the nodes of a tree with the specified roots
	 * and midwife.
	 */
	public TreeIterable(Iterable<? extends E> roots, TreeIterator.Midwife<E> midwife) {
		super();
		this.roots = roots;
		this.midwife = midwife;
	}

	protected TreeIterator.Midwife<E> buildDefaultMidwife() {
		return new DefaultMidwife();
	}

	public Iterator<E> iterator() {
		return new TreeIterator<E>(this.roots, this.midwife);
	}

	/**
	 * Return the immediate children of the specified object.
	 * <p>
	 * This method can be overridden by a subclass as an
	 * alternative to building a <code>TreeIterator.Midwife</code>.
	 */
	protected Iterator<? extends E> children(@SuppressWarnings("unused") E next) {
		// TreeIterable.children(Object) was not overridden
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return StringTools.buildToStringFor(this, this.roots);
	}


	//********** default midwife **********

	protected class DefaultMidwife implements TreeIterator.Midwife<E> {
		public Iterator<? extends E> children(E node) {
			return TreeIterable.this.children(node);
		}
	}

}
