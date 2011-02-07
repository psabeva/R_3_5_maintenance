/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa1.resource.java.source;

import org.eclipse.jpt.common.core.utility.jdt.Member;
import org.eclipse.jpt.core.internal.resource.java.source.SourceAssociationOverridesAnnotation;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.core.resource.java.NestableAssociationOverrideAnnotation;

/**
 * <code>javax.persistence.AssociationOverrides</code>
 */
public final class SourceAssociationOverrides1_0Annotation
	extends SourceAssociationOverridesAnnotation
{
	public SourceAssociationOverrides1_0Annotation(JavaResourceNode parent, Member member) {
		super(parent, member);
	}

	@Override
	protected NestableAssociationOverrideAnnotation buildAssociationOverride(int index) {
		// pass the Java resource persistent member as the nested annotation's parent
		// since the nested annotation can be converted to stand-alone
		return SourceAssociationOverride1_0Annotation.buildNestedAssociationOverride(this.parent, this.annotatedElement, index, this.daa);
	}
}