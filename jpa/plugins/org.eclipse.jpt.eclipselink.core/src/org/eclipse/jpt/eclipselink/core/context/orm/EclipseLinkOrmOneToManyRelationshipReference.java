/*******************************************************************************
 * Copyright (c) 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.context.orm;

import org.eclipse.jpt.core.context.orm.OrmJoinColumnEnabledRelationshipReference;
import org.eclipse.jpt.core.context.orm.OrmOneToManyRelationshipReference;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkOneToManyRelationshipReference;

/**
 * EclipseLink <code>orm.xml</code> 1:m relationship
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface EclipseLinkOrmOneToManyRelationshipReference
	extends EclipseLinkOneToManyRelationshipReference,
			OrmOneToManyRelationshipReference,
			OrmJoinColumnEnabledRelationshipReference
{
	// combine various interfaces
}
