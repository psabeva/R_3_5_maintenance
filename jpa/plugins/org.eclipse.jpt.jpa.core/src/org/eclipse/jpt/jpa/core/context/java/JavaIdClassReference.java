/*******************************************************************************
 *  Copyright (c) 2010  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.core.context.java;

import org.eclipse.jpt.jpa.core.context.IdClassReference;

/**
 * Java ID class reference
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 2.3
 * @since 2.3
 */
public interface JavaIdClassReference
	extends IdClassReference, JavaJpaContextNode
{
	/**
	 * Return the fully qualified name of the id class, taking into consideration the default value if applicable
	 */
	String getFullyQualifiedIdClassName();
		String FULLY_QUALIFIED_ID_CLASS_PROPERTY = "fullyQualifiedIdClass"; //$NON-NLS-1$
}