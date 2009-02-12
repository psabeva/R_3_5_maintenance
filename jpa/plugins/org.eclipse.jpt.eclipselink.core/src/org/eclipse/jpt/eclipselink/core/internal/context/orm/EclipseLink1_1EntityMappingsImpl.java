/*******************************************************************************
 * Copyright (c) 2008, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.context.orm;

import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.resource.orm.XmlTypeMapping;
import org.eclipse.jpt.eclipselink.core.internal.EclipseLink1_1JpaFactory;
import org.eclipse.jpt.eclipselink1_1.core.resource.orm.XmlEntityMappings;

public class EclipseLink1_1EntityMappingsImpl
	extends EclipseLinkEntityMappingsImpl
{
	
	public EclipseLink1_1EntityMappingsImpl(EclipseLink1_1OrmXml parent, XmlEntityMappings xmlEntityMapping) {
		super(parent, xmlEntityMapping);
	}
	
	@Override
	protected OrmPersistentType buildPersistentType(XmlTypeMapping resourceMapping) {
		return getJpaFactory().buildEclipseLink1_1OrmPersistentType(this, resourceMapping);
	}
	
	// **************** JpaNode impl *******************************************
	
	@Override
	protected EclipseLink1_1JpaFactory getJpaFactory() {
		return (EclipseLink1_1JpaFactory) super.getJpaFactory();
	}
	
	@Override
	public EclipseLink1_1OrmXml getParent() {
		return (EclipseLink1_1OrmXml) super.getParent();
	}	
	

}
