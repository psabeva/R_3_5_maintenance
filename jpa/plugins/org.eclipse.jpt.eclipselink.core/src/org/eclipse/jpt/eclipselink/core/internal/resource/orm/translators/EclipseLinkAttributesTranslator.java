/*******************************************************************************
 *  Copyright (c) 2008, 2009  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.resource.orm.translators;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jpt.core.internal.resource.orm.translators.AttributesTranslator;
import org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmFactory;
import org.eclipse.wst.common.internal.emf.resource.Translator;

public class EclipseLinkAttributesTranslator extends AttributesTranslator
	implements EclipseLinkOrmXmlMapper
{
	public EclipseLinkAttributesTranslator(String domNameAndPath, EStructuralFeature aFeature) {
		super(domNameAndPath, aFeature);
	}
	
	@Override
	public EObject createEMFObject(String nodeName, String readAheadName) {
		return EclipseLinkOrmFactory.eINSTANCE.createAttributes();
		
	}
	@Override
	protected Translator[] createChildren() {
		return new Translator[] {
			createIdTranslator(),
			createEmbeddedIdTranslator(),
			createBasicTranslator(),
			createBasicCollectionTranslator(),
			createBasicMapTranslator(),
			createVersionTranslator(),
			createManyToOneTranslator(),
			createOneToManyTranslator(),
			createOneToOneTranslator(),
			createVariableOneToOneTranslator(),
			createManyToManyTranslator(),
			createEmbeddedTranslator(),
			createTransformationTranslator(),
			createTransientTranslator()
		};
	}
	
	@Override
	protected Translator createIdTranslator() {
		return new EclipseLinkIdTranslator(ID, ORM_PKG.getAttributes_Ids());
	}
	
	@Override
	protected Translator createEmbeddedIdTranslator() {
		return new EclipseLinkEmbeddedIdTranslator(EMBEDDED_ID, ORM_PKG.getAttributes_EmbeddedIds());
	}
	
	@Override
	protected Translator createBasicTranslator() {
		return new EclipseLinkBasicTranslator(BASIC, ORM_PKG.getAttributes_Basics());
	}
	
	@Override
	protected Translator createVersionTranslator() {
		return new EclipseLinkVersionTranslator(VERSION, ORM_PKG.getAttributes_Versions());
	}
	
	@Override
	protected Translator createManyToOneTranslator() {
		return new EclipseLinkManyToOneTranslator(MANY_TO_ONE, ORM_PKG.getAttributes_ManyToOnes());
	}
	
	@Override
	protected Translator createOneToOneTranslator() {
		return new EclipseLinkOneToOneTranslator(ONE_TO_ONE, ORM_PKG.getAttributes_OneToOnes());
	}
	
	@Override
	protected Translator createOneToManyTranslator() {
		return new EclipseLinkOneToManyTranslator(ONE_TO_MANY, ORM_PKG.getAttributes_OneToManys());
	}
	
	@Override
	protected Translator createManyToManyTranslator() {
		return new EclipseLinkManyToManyTranslator(MANY_TO_MANY, ORM_PKG.getAttributes_ManyToManys());
	}
	
	@Override
	protected Translator createEmbeddedTranslator() {
		return new EclipseLinkEmbeddedTranslator(EMBEDDED, ORM_PKG.getAttributes_Embeddeds());
	}
	
	protected Translator createVariableOneToOneTranslator() {
		return new VariableOneToOneTranslator(VARIABLE_ONE_TO_ONE, ECLIPSELINK_ORM_PKG.getAttributes_VariableOneToOnes());
	}

	protected Translator createBasicCollectionTranslator() {
		return new BasicCollectionTranslator(BASIC_COLLECTION, ECLIPSELINK_ORM_PKG.getAttributes_BasicCollections());
	}
	
	protected Translator createBasicMapTranslator() {
		return new BasicMapTranslator(BASIC_MAP, ECLIPSELINK_ORM_PKG.getAttributes_BasicMaps());
	}
	
	protected Translator createTransformationTranslator() {
		return new TransformationTranslator(TRANSFORMATION, ECLIPSELINK_ORM_PKG.getAttributes_Transformations());
	}
}
