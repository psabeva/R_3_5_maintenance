/*******************************************************************************
 * Copyright (c) 2008, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import java.util.Vector;
import org.eclipse.jpt.common.utility.internal.iterables.LiveCloneIterable;
import org.eclipse.jpt.core.context.java.JavaJpaContextNode;
import org.eclipse.jpt.core.context.java.JavaReadOnlyUniqueConstraint;

public abstract class AbstractJavaReadOnlyUniqueConstraint
	extends AbstractJavaJpaContextNode
	implements JavaReadOnlyUniqueConstraint
{
	protected final Vector<String> columnNames = new Vector<String>();


	public AbstractJavaReadOnlyUniqueConstraint(JavaJpaContextNode parent) {
		super(parent);
	}


	// ********** column names **********

	public Iterable<String> getColumnNames() {
		return new LiveCloneIterable<String>(this.columnNames);
	}

	public int getColumnNamesSize() {
		return this.columnNames.size();
	}

	public String getColumnName(int index) {
		return this.columnNames.get(index);
	}

	protected void syncColumnNames() {
		this.synchronizeList(this.getResourceColumnNames(), this.columnNames, COLUMN_NAMES_LIST);
	}

	protected abstract Iterable<String> getResourceColumnNames();

	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.columnNames);
	}
}