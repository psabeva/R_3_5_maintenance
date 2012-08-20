/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal.context.orm;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.jpt.common.core.resource.xml.EmfTools;
import org.eclipse.jpt.jpa.core.context.orm.OrmAttributeMapping;
import org.eclipse.jpt.jpa.core.context.orm.OrmAttributeMappingDefinition;
import org.eclipse.jpt.jpa.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.jpa.core.context.orm.OrmXmlContextNodeFactory;
import org.eclipse.jpt.jpa.core.resource.orm.XmlAttributeMapping;
import org.eclipse.jpt.jpa.eclipselink.core.EclipseLinkMappingKeys;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.EclipseLinkOrmPackage;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlStructure;

public class OrmEclipseLinkStructureMapping2_3Definition
	implements OrmAttributeMappingDefinition
{
	// singleton
	private static final OrmEclipseLinkStructureMapping2_3Definition INSTANCE = 
			new OrmEclipseLinkStructureMapping2_3Definition();


	/**
	 * Return the singleton
	 */
	public static OrmAttributeMappingDefinition instance() {
		return INSTANCE;
	}


	/**
	 * Enforce singleton usage
	 */
	private OrmEclipseLinkStructureMapping2_3Definition() {
		super();
	}


	public String getKey() {
		return EclipseLinkMappingKeys.STRUCTURE_ATTRIBUTE_MAPPING_KEY;
	}

	public XmlAttributeMapping buildResourceMapping(EFactory factory) {
		return EmfTools.create(
				factory, 
				EclipseLinkOrmPackage.eINSTANCE.getXmlStructure(), 
				XmlStructure.class);
	}

	public OrmAttributeMapping buildContextMapping(
			OrmPersistentAttribute parent, 
			XmlAttributeMapping resourceMapping, 
			OrmXmlContextNodeFactory factory) {
		return new OrmEclipseLinkStructureMapping2_3(parent, (XmlStructure) resourceMapping);
	}
	
	public boolean isSingleRelationshipMapping() {
		return false;
	}
	
	public boolean isCollectionMapping() {
		return false;
	}
}
