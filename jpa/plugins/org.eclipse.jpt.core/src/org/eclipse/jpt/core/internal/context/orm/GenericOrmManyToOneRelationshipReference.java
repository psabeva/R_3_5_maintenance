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
package org.eclipse.jpt.core.internal.context.orm;

import org.eclipse.jpt.core.context.orm.OrmJoinTableJoiningStrategy;
import org.eclipse.jpt.core.context.orm.OrmJoiningStrategy;
import org.eclipse.jpt.core.context.orm.OrmManyToOneMapping;
import org.eclipse.jpt.core.resource.orm.XmlManyToOne;

public class GenericOrmManyToOneRelationshipReference
	extends AbstractOrmManyToOneRelationshipReference
{
	
	public GenericOrmManyToOneRelationshipReference(
			OrmManyToOneMapping parent, XmlManyToOne resource) {
		super(parent, resource);
	}

	@Override
	protected OrmJoinTableJoiningStrategy buildJoinTableJoiningStrategy() {
		return new NullOrmJoinTableJoiningStrategy(this);
	}

	@Override
	protected OrmJoiningStrategy calculatePredominantJoiningStrategy() {
		// the only joining strategy
		return this.joinColumnJoiningStrategy;
	}

}
