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

public class EclipseLinkManyToOneTranslator extends org.eclipse.jpt.eclipselink.core.internal.resource.orm.translators.EclipseLinkManyToOneTranslator
	implements EclipseLink1_1OrmXmlMapper
{
	public EclipseLinkManyToOneTranslator(String domNameAndPath, EStructuralFeature aFeature) {
		super(domNameAndPath, aFeature);
	}
	
	
	@Override
	public EObject createEMFObject(String nodeName, String readAheadName) {
		return EclipseLink1_1OrmFactory.eINSTANCE.createXmlManyToOneImpl();
	}
		
	@Override
	protected Translator[] createChildren() {
		return new Translator[] {
			createNameTranslator(),
			createTargetEntityTranslator(),
			createFetchTranslator(),
			createOptionalTranslator(),
			createAccessTranslator(),
			createJoinColumnTranslator(),
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
