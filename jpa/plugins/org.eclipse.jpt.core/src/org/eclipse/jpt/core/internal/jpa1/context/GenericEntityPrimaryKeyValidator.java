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
package org.eclipse.jpt.core.internal.jpa1.context;

import org.eclipse.jpt.core.context.Entity;
import org.eclipse.jpt.core.internal.context.PrimaryKeyTextRangeResolver;

public class GenericEntityPrimaryKeyValidator
	extends AbstractEntityPrimaryKeyValidator
{
	public GenericEntityPrimaryKeyValidator(Entity entity, PrimaryKeyTextRangeResolver textRangeResolver) {
		super(entity, textRangeResolver);
	}
	
	@Override
	protected boolean idClassIsRequired() {
		//Short circuit check for idClassIsRequired if any part of the primary key is defined
		//in a superclass for Generic types.  Other validation will exist and needs to be
		//addressed first
		if(definesPrimaryKeyOnAncestor(typeMapping())){
			return false;
		}
		return super.idClassIsRequired();
	}
	

}
