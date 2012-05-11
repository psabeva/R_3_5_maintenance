/*******************************************************************************
 *  Copyright (c) 2009  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.internal.jpa2.context.orm;

import org.eclipse.jpt.core.internal.context.orm.AbstractOrmXmlContextNode;
import org.eclipse.jpt.core.jpa2.context.orm.OrmCacheable2_0;
import org.eclipse.jpt.core.jpa2.context.orm.OrmCacheableHolder2_0;
import org.eclipse.jpt.core.utility.TextRange;

public class NullOrmCacheable2_0
	extends AbstractOrmXmlContextNode
	implements OrmCacheable2_0
{
	public NullOrmCacheable2_0(OrmCacheableHolder2_0 parent) {
		super(parent);
	}
	
	public boolean isCacheable() {
		return false;
	}
	
	public boolean isDefaultCacheable() {
		return false;
	}
	
	public Boolean getSpecifiedCacheable() {
		return null;
	}
	
	public void setSpecifiedCacheable(Boolean newSpecifiedCacheable) {
		throw new UnsupportedOperationException();
	}
	
	public void update() {
		//no-op
	}
	
	public TextRange getValidationTextRange() {
		return null;
	}
}
