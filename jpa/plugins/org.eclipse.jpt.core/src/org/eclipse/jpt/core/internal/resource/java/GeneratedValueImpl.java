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
import org.eclipse.jpt.core.internal.content.java.mappings.JPA;
import org.eclipse.jpt.core.internal.jdtutility.Member;

public class GeneratedValueImpl extends AbstractAnnotationResource<Member> implements GeneratedValue
{

	public GeneratedValueImpl(Member member, JpaPlatform jpaPlatform) {
		super(member, jpaPlatform, DECLARATION_ANNOTATION_ADAPTER);
	}
	
	public String getAnnotationName() {
		return JPA.GENERATED_VALUE;
	}

	public void updateFromJava(CompilationUnit astRoot) {
	}
}
