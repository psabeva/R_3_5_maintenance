/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.resource.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.resource.java.DiscriminatorValueAnnotation;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.utility.TextRange;

/**
 * javax.persistence.DiscriminatorValue
 */
public final class NullDiscriminatorValueAnnotation
	extends NullAnnotation
	implements DiscriminatorValueAnnotation
{

	protected NullDiscriminatorValueAnnotation(JavaResourcePersistentType parent) {
		super(parent);
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	@Override
	protected DiscriminatorValueAnnotation addAnnotation() {
		return (DiscriminatorValueAnnotation) super.addAnnotation();
	}
	
	// ***** value
	public String getValue() {
		return null;
	}

	public void setValue(String value) {
		if (value != null) {
			this.addAnnotation().setValue(value);
		}
	}

	public TextRange getValueTextRange(CompilationUnit astRoot) {
		return null;
	}

}
