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

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.ITextRange;


public interface ManyToMany extends RelationshipMappingAnnotation
{
	String ANNOTATION_NAME = JPA.MANY_TO_MANY;

	/**
	 * Corresponds to the mappedBy element of the ManyToMany annotation. 
	 * Returns null if the mappedBy element does not exist in java.
	 */
	String getMappedBy();
	
	/**
	 * Corresponds to the mappedBy element of the ManyToMany annotation. 
	 * Set to null to remove the mappedBy element.
	 */
	void setMappedBy(String mappedBy);
		String MAPPED_BY_PROPERTY = "mappedByProperty";
		
	/**
	 * Return the ITextRange for the mappedBy element.  If the mappedBy element 
	 * does not exist return the ITextRange for the ManyToMany annotation.
	 */
	ITextRange mappedByTextRange(CompilationUnit astRoot);

	/**
	 * Return whether the specified postition touches the mappedBy element.
	 * Return false if the mappedBy element does not exist.
	 */
	boolean mappedByTouches(int pos, CompilationUnit astRoot);

}
