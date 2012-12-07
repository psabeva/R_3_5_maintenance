/*******************************************************************************
 *  Copyright (c) 2011  Oracle. All rights reserved.
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0, which accompanies this distribution
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jaxb.core.context;

/**
 * Holds the attributes represented by a particular JavaResourceType and XmlAccessType.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 3.3
 * @since 3.0
 */
public interface JaxbAttributesContainer
		extends JaxbContextNode {
	
	// ***** attributes *****
	
	Iterable<JaxbPersistentAttribute> getAttributes();
	
	int getAttributesSize();
	
	
	interface Context {
		
		/**
		 * Return the access type of the owner, to be used in determining which attributes to build
		 */
		XmlAccessType getAccessType();

		/**
		 * called after an attribute was added to the container
		 */
		void attributeAdded(JaxbPersistentAttribute attribute);
		
		/**
		 * called after an attribute was removed from the container
		 */
		void attributeRemoved(JaxbPersistentAttribute attribute);
	}
}
