/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.model.value;

import java.util.Iterator;
import org.eclipse.jpt.utility.model.Model;

/**
 * Interface used to abstract tree accessing and
 * change notification and make it more pluggable.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @param <E> the type of values held by the model
 */
public interface TreeValueModel<E>
	extends Model
{
	/**
	 * Return the tree's nodes.
	 */
	Iterator<E> nodes();
		String NODES = "nodes"; //$NON-NLS-1$

}