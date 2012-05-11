/*******************************************************************************
 *  Copyright (c) 2009, 2010  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.jpa2.resource.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.resource.java.Annotation;
import org.eclipse.jpt.core.utility.TextRange;

/**
 * Corresponds to the JPA 2.0 annotation
 * javax.persistence.MapsId
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 2.3
 * @since 2.3
 */
public interface MapsId2_0Annotation
	extends Annotation
{
	String ANNOTATION_NAME = JPA2_0.MAPS_ID;
	
	/**
	 * Corresponds to the 'value' element of the MapsId annotation.
	 * Returns null if the element does not exist in Java.
	 */
	String getValue();
		String VALUE_PROPERTY = "value"; //$NON-NLS-1$
	
	/**
	 * Corresponds to the 'value' element of the MapsId annotation.
	 * Set to null to remove the element.
	 */
	void setValue(String newValue);
	
	/**
	 * Return the {@link TextRange} for the 'value' element.  If the element 
	 * does not exist return the {@link TextRange} for the MapsId annotation.
	 */
	TextRange getValueTextRange(CompilationUnit astRoot);
	
	/**
	 * Return whether the specified position touches the 'value' element.
	 * Return false if the element does not exist.
	 */
	boolean valueTouches(int pos, CompilationUnit astRoot);
}
