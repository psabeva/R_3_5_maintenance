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
package org.eclipse.jpt.eclipselink1_1.core.internal.resource.orm.translators;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jpt.eclipselink1_1.core.resource.orm.EclipseLink1_1OrmFactory;
import org.eclipse.wst.common.internal.emf.resource.Translator;

public class EclipseLinkManyToManyTranslator extends org.eclipse.jpt.eclipselink.core.internal.resource.orm.translators.EclipseLinkManyToManyTranslator
	implements EclipseLink1_1OrmXmlMapper
{
	public EclipseLinkManyToManyTranslator(String domNameAndPath, EStructuralFeature aFeature) {
		super(domNameAndPath, aFeature);
	}
	
	
	@Override
	public EObject createEMFObject(String nodeName, String readAheadName) {
		return EclipseLink1_1OrmFactory.eINSTANCE.createXmlManyToManyImpl();
	}
		
	@Override
	protected Translator[] createChildren() {
		return new Translator[] {
			createNameTranslator(),
			createTargetEntityTranslator(),
			createFetchTranslator(),
			createAccessTranslator(),
			createMappedByTranslator(),
			createOrderByTranslator(),
			createMapKeyTranslator(),
			createJoinTableTranslator(),
			createCascadeTranslator(),
			createJoinFetchTranslator(),
			createPropertyTranslator(),
			createAccessMethodsTranslator()
		};
	}
	protected Translator createAccessTranslator() {
		return new Translator(ACCESS, ECLIPSELINK1_1_ORM_PKG.getXmlAttributeMapping_Access(), DOM_ATTRIBUTE);
	}
}
