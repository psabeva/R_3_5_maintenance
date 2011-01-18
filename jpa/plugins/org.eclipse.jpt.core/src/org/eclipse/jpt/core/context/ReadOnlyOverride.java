/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context;

/**
 * Read-only<ul>
 * <li>attribute override
 * <li>association override
 * </ul>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface ReadOnlyOverride
	extends JpaContextNode
{
	OverrideContainer getContainer();

	String getName();
		String NAME_PROPERTY = "name"; //$NON-NLS-1$

	/**
	 * Return <code>true</code> if the override is not explicitly specified on
	 * the owning object (i.e. it occurs by default); return <code>false</code>
	 * if the override is explicitly specified on the owning object.
	 * 
	 * @see Override_#convertToVirtual()
	 * @see VirtualOverride#convertToSpecified()
	 */
	boolean isVirtual();
}
