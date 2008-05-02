/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.internal.resource.persistence.translators;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.wst.common.internal.emf.resource.Translator;

public class PropertyTranslator extends Translator
	implements PersistenceXmlMapper
{
	private Translator[] children;
	
	
	public PropertyTranslator(String domNameAndPath, EStructuralFeature aFeature) {
		super(domNameAndPath, aFeature, END_TAG_NO_INDENT);
	}
	
	@Override	
	public Translator[] getChildren(Object target, int versionID) {
		if (children == null) {
			children = createChildren();
		}
		return children;
	}
	
	protected Translator[] createChildren() {
		return new Translator[] {
			createNameTranslator(),
			createValueTranslator()
		};
	}
	
	private Translator createNameTranslator() {
		return new Translator(NAME, PERSISTENCE_PKG.getXmlProperty_Name(), DOM_ATTRIBUTE);
	}
	
	private Translator createValueTranslator() {
		return new Translator(VALUE, PERSISTENCE_PKG.getXmlProperty_Value(), DOM_ATTRIBUTE);
	}
}
