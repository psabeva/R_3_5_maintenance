/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa2.context.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.context.java.AbstractJavaJoinTableJoiningStrategy;
import org.eclipse.jpt.core.jpa2.context.java.JavaAssociationOverrideRelationshipReference2_0;
import org.eclipse.jpt.core.jpa2.context.java.JavaJoinTableInAssociationOverrideJoiningStrategy2_0;
import org.eclipse.jpt.core.jpa2.resource.java.AssociationOverride2_0Annotation;
import org.eclipse.jpt.core.resource.java.JoinTableAnnotation;
import org.eclipse.jpt.core.utility.TextRange;

public class GenericJavaJoinTableInAssociationOverrideJoiningStrategy2_0 
	extends AbstractJavaJoinTableJoiningStrategy
	implements JavaJoinTableInAssociationOverrideJoiningStrategy2_0
{
	protected transient AssociationOverride2_0Annotation associationOverrideAnnotation;
	
	public GenericJavaJoinTableInAssociationOverrideJoiningStrategy2_0(JavaAssociationOverrideRelationshipReference2_0 parent) {
		super(parent);
	}
	
	public boolean isOverridableAssociation() {
		return false;
	}
	
	@Override
	public JavaAssociationOverrideRelationshipReference2_0 getRelationshipReference() {
		return (JavaAssociationOverrideRelationshipReference2_0) super.getRelationshipReference();
	}
	
	public boolean shouldValidateAgainstDatabase() {
		return getRelationshipReference().getTypeMapping().shouldValidateAgainstDatabase();
	}
	
	// **************** join table *********************************************
	
	public JoinTableAnnotation getAnnotation() {
		return this.associationOverrideAnnotation.getNonNullJoinTable();
	}
	@Override
	protected JoinTableAnnotation addAnnotation() {
		return this.associationOverrideAnnotation.addJoinTable();
	}
	
	@Override
	protected void removeAnnotation() {
		this.associationOverrideAnnotation.removeJoinTable();
	}

	
	public void initialize(AssociationOverride2_0Annotation associationOverride) {
		this.associationOverrideAnnotation = associationOverride;
		super.initialize();
	}
	
	public void update(AssociationOverride2_0Annotation associationOverride) {
		this.associationOverrideAnnotation = associationOverride;
		super.update();
	}
	
	public TextRange getValidationTextRange(CompilationUnit astRoot) {
		return this.getRelationshipReference().getValidationTextRange(astRoot);
	}

}
