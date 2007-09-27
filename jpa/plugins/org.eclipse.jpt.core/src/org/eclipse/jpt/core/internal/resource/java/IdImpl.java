/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.resource.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.jdtutility.Attribute;


public class IdImpl extends AbstractAnnotationResource<Attribute> implements Id
{
	
	public IdImpl(Attribute attribute, JpaPlatform jpaPlatform) {
		super(attribute, jpaPlatform, DECLARATION_ANNOTATION_ADAPTER);
	}
		
	public String getAnnotationName() {
		return JPA.ID;
	}

	public void updateFromJava(CompilationUnit astRoot) {
	}

}
