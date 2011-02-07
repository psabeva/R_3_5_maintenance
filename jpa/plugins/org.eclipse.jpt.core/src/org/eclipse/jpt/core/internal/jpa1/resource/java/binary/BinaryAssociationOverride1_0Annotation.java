/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa1.resource.java.binary;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jpt.core.internal.resource.java.binary.BinaryAssociationOverrideAnnotation;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;

/**
 * javax.persistence.AssociationOverride
 */
public final class BinaryAssociationOverride1_0Annotation
	extends BinaryAssociationOverrideAnnotation
{

	public BinaryAssociationOverride1_0Annotation(JavaResourceNode parent, IAnnotation jdtAnnotation) {
		super(parent, jdtAnnotation);
	}
	
}