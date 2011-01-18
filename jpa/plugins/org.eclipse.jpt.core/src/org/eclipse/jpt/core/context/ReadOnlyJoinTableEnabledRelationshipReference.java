/*******************************************************************************
 * Copyright (c) 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context;

/**
 * Join table relationship (1:1 (JPA 2.0), 1:m, m:1 (JPA 2.0), m:m,
 * and association override (JPA 2.0))
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface ReadOnlyJoinTableEnabledRelationshipReference
	extends ReadOnlyRelationshipReference
{
	/**
	 * Return the aggregate (never null) object used to configure the join table 
	 * joining strategy
	 */
	ReadOnlyJoinTableJoiningStrategy getJoinTableJoiningStrategy();

	/**
	 * Return whether the join table joining strategy is currently the 
	 * predominant joining strategy
	 */
	boolean usesJoinTableJoiningStrategy();

	/**
	 * Return whether this reference may potentially have a default join table.
	 * (For example, a M-M mapping may have one if it does not specify a 
	 * mappedBy)
	 */
	boolean mayHaveDefaultJoinTable();
}
