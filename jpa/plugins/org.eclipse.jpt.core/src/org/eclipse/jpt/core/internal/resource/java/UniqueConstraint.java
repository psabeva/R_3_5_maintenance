/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.resource.java;

import java.util.Iterator;
import java.util.ListIterator;

/**
 * Corresponds to the javax.persistence.UniqueConstraint annotation
 */
public interface UniqueConstraint extends JavaResource
{
	ListIterator<String> columnNames();

	void addColumnName(String columnName);
	
	void removeColumnName(String columnName);

	/**
	 * All containers must implement this interface.
	 */
	interface Owner
	{
		Iterator<String> candidateUniqueConstraintColumnNames();
		
		JavaResource javaResource();
	}
}
