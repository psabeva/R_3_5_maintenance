/*******************************************************************************
 * Copyright (c) 2010, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal.v2_1.context.orm;

import org.eclipse.jpt.jpa.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.jpa.eclipselink.core.internal.context.orm.AbstractOrmEclipseLinkBasicCollectionMapping;
import org.eclipse.jpt.jpa.eclipselink.core.internal.context.orm.AbstractOrmEclipseLinkBasicMapMapping;
import org.eclipse.jpt.jpa.eclipselink.core.internal.v2_0.context.orm.EclipseLinkOrmXml2_0ContextNodeFactory;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlBasicCollection;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlBasicMap;

public class EclipseLinkOrmXml2_1ContextNodeFactory
	extends EclipseLinkOrmXml2_0ContextNodeFactory
{

	@Override
	public AbstractOrmEclipseLinkBasicCollectionMapping buildOrmEclipseLinkBasicCollectionMapping(OrmPersistentAttribute parent, XmlBasicCollection resourceMapping) {
		return new OrmEclipseLinkBasicCollectionMapping2_1(parent, resourceMapping);
	}

	@Override
	public AbstractOrmEclipseLinkBasicMapMapping buildOrmEclipseLinkBasicMapMapping(OrmPersistentAttribute parent, XmlBasicMap resourceMapping) {
		return new OrmEclipseLinkBasicMapMapping2_1(parent, resourceMapping);
	}
}