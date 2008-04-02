/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.context;

import org.eclipse.jpt.core.context.persistence.Persistence;
import org.eclipse.jpt.core.internal.context.persistence.GenericPersistenceUnit;
import org.eclipse.jpt.core.resource.persistence.XmlPersistenceUnit;
import org.eclipse.jpt.eclipselink.core.internal.context.caching.Caching;
import org.eclipse.jpt.eclipselink.core.internal.context.customization.Customization;
import org.eclipse.jpt.eclipselink.core.internal.context.schema.generation.SchemaGeneration;

/**
 * EclipseLinkPersistenceUnit
 */
public class EclipseLinkPersistenceUnit extends GenericPersistenceUnit
{
	private EclipseLinkProperties eclipseLinkProperties;

	// ********** constructors/initialization **********
	public EclipseLinkPersistenceUnit(Persistence parent, XmlPersistenceUnit persistenceUnit) {
		super(parent, persistenceUnit);
	}

	protected void initialize(XmlPersistenceUnit xmlPersistenceUnit) {
		super.initialize(xmlPersistenceUnit);
		this.eclipseLinkProperties = new EclipseLinkJpaProperties(this);
	}

	// ******** Behavior *********
	public SchemaGeneration getSchemaGeneration() {
		return this.eclipseLinkProperties.getSchemaGeneration();
	}

	public Caching getCaching() {
		return this.eclipseLinkProperties.getCaching();
	}

	public Customization getCustomization() {
		return this.eclipseLinkProperties.getCustomization();
	}

}
