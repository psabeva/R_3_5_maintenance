/*******************************************************************************
 * Copyright (c) 2008, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.details;

/**
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface DefaultMappingUiDefinition<M, T>
	extends MappingUiDefinition<M, T>
{
	/**
	 * Returns a unique string that corresponds to the key of the mapping in the
	 * core.  For a default mapping the method getKey() will return null since
	 * that is for the specified mapping.  This will return the default mapping key, not null
	 */
	String getDefaultKey();
}