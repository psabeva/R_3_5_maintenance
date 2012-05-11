/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.resource.java.binary;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.resource.java.InheritanceAnnotation;
import org.eclipse.jpt.core.resource.java.InheritanceType;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.utility.TextRange;

/**
 * javax.persistence.Inheritance
 */
public final class BinaryInheritanceAnnotation
	extends BinaryAnnotation
	implements InheritanceAnnotation
{
	private InheritanceType strategy;


	public BinaryInheritanceAnnotation(JavaResourcePersistentType parent, IAnnotation jdtAnnotation) {
		super(parent, jdtAnnotation);
		this.strategy = this.buildStrategy();
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	@Override
	public void update() {
		super.update();
		this.setStrategy_(this.buildStrategy());
	}


	// ********** InheritanceAnnotation implementation **********

	// ***** strategy
	public InheritanceType getStrategy() {
		return this.strategy;
	}

	public void setStrategy(InheritanceType strategy) {
		throw new UnsupportedOperationException();
	}

	private void setStrategy_(InheritanceType strategy) {
		InheritanceType old = this.strategy;
		this.strategy = strategy;
		this.firePropertyChanged(STRATEGY_PROPERTY, old, strategy);
	}

	private InheritanceType buildStrategy() {
		return InheritanceType.fromJavaAnnotationValue(this.getJdtMemberValue(JPA.INHERITANCE__STRATEGY));
	}

	public TextRange getStrategyTextRange(CompilationUnit astRoot) {
		throw new UnsupportedOperationException();
	}

}
