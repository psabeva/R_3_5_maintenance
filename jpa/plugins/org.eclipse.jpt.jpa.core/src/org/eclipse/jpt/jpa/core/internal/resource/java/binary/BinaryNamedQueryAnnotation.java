/*******************************************************************************
 * Copyright (c) 2009, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.resource.java.binary;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jpt.common.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.jpa.core.resource.java.JPA;
import org.eclipse.jpt.jpa.core.resource.java.NamedQueryAnnotation;

/**
 * javax.persistence.NamedQuery
 */
public abstract class BinaryNamedQueryAnnotation
	extends BinaryQueryAnnotation
	implements NamedQueryAnnotation
{
	public BinaryNamedQueryAnnotation(JavaResourceNode parent, IAnnotation jdtAnnotation) {
		super(parent, jdtAnnotation);
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}


	// ********** BinaryBaseNamedQueryAnnotation implementation **********

	@Override
	String getNameElementName() {
		return JPA.NAMED_QUERY__NAME;
	}

	@Override
	String getQueryElementName() {
		return JPA.NAMED_QUERY__QUERY;
	}

	@Override
	String getHintsElementName() {
		return JPA.NAMED_QUERY__HINTS;
	}

}