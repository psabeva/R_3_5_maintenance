/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.tests.internal.iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterables.LiveCloneIterable;

@SuppressWarnings("nls")
public class LiveCloneIterableTests extends TestCase {

	public LiveCloneIterableTests(String name) {
		super(name);
	}

	public void testIterator() {
		Collection<String> c = new ArrayList<String>();
		c.add("0");
		c.add("1");
		c.add("2");
		c.add("3");
		assertEquals(4, c.size());
		Iterable<String> iterable = new LiveCloneIterable<String>(c);
		int i = 0;
		for (String s : iterable) {
			assertEquals(String.valueOf(i++), s);
			c.remove("3");
		}
		assertEquals(4, i);
		assertEquals(3, c.size());

		// iterable should now return only 3 strings (since it's "live")
		i = 0;
		for (String s : iterable) {
			assertEquals(String.valueOf(i++), s);
		}
		assertEquals(3, i);
	}

	public void testRemove() {
		final Collection<String> collection = this.buildCollection();
		Iterable<String> iterable = new LiveCloneIterable<String>(collection) {
			@Override
			protected void remove(String current) {
				collection.remove(current);
			}
		};

		Object removed = "three";
		assertTrue(CollectionTools.contains(iterable, removed));
		for (Iterator<String> iterator = iterable.iterator(); iterator.hasNext(); ) {
			if (iterator.next().equals(removed)) {
				iterator.remove();
			}
		}
		assertFalse(collection.contains(removed));
		// "live" clone iterable will no longer contain the element removed from the
		// original collection
		assertFalse(CollectionTools.contains(iterable, "three"));
	}

	private Collection<String> buildCollection() {
		Collection<String> c = new ArrayList<String>();
		c.add("one");
		c.add("two");
		c.add("three");
		c.add("four");
		c.add("five");
		c.add("six");
		c.add("seven");
		c.add("eight");
		return c;
	}

}
