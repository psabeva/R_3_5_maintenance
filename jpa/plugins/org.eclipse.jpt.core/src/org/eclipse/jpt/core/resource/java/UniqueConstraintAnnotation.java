/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.resource.java;

import java.util.ListIterator;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Corresponds to the javax.persistence.UniqueConstraint annotation
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface UniqueConstraintAnnotation extends JavaResourceNode
{
	String ANNOTATION_NAME = JPA.UNIQUE_CONSTRAINT;

	ListIterator<String> columnNames();

	int columnNamesSize();
	
	void addColumnName(String columnName);

	void addColumnName(int index, String columnName);
	
	void removeColumnName(String columnName);
	
	void removeColumnName(int index);
	
	void moveColumnName(int targetIndex, int sourceIndex);
		String COLUMN_NAMES_LIST = "columnNames"; //$NON-NLS-1$
		
	/**
	 * Return whether the specified postition touches the columnNames element.
	 * Return false if the columnNames element does not exist.
	 */
	boolean columnNamesTouches(int pos, CompilationUnit astRoot);

}
