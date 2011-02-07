/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.resource.java.binary;

import java.util.Map;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.jpa.core.JptJpaCorePlugin;
import org.eclipse.jpt.jpa.core.resource.java.Annotation;
import org.eclipse.jpt.jpa.core.resource.java.ContainerAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.jpa.core.resource.java.NestableAnnotation;

/**
 * JAR annotation
 */
public abstract class BinaryAnnotation
	extends BinaryNode
	implements Annotation
{
	final IAnnotation jdtAnnotation;

	protected BinaryAnnotation(JavaResourceNode parent, IAnnotation jdtAnnotation) {
		super(parent);
		this.jdtAnnotation = jdtAnnotation;
	}


	// ********** convenience methods **********

	/**
	 * Return the values of the JDT annotation's member with the specified name.
	 */
	protected Object[] getJdtMemberValues(String memberName) {
		Object[] values = (Object[]) this.getJdtMemberValue(memberName);
		return (values != null) ? values : EMPTY_OBJECT_ARRAY;
	}
	private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	/**
	 * Return the value of the JDT annotation's member with the specified name.
	 */
	protected Object getJdtMemberValue(String memberName) {
		IMemberValuePair pair = this.getJdtMemberValuePair(memberName);
		return (pair == null) ? null : pair.getValue();
	}

	/**
	 * Return the JDT annotation's member-value pair with the specified name.
	 */
	private IMemberValuePair getJdtMemberValuePair(String memberName) {
		for (IMemberValuePair pair : this.getJdtMemberValuePairs()) {
			if (pair.getMemberName().equals(memberName)) {
				return pair;
			}
		}
		return null;
	}

	private IMemberValuePair[] getJdtMemberValuePairs() {
		try {
			return this.jdtAnnotation.getMemberValuePairs();
		} catch (JavaModelException ex) {
			JptJpaCorePlugin.log(ex);
			return EMPTY_MEMBER_VALUE_PAIR_ARRAY;
		}
	}
	private static final IMemberValuePair[] EMPTY_MEMBER_VALUE_PAIR_ARRAY = new IMemberValuePair[0];


	// ********** Annotation implementation **********
	public org.eclipse.jdt.core.dom.Annotation getAstAnnotation(CompilationUnit astRoot) {
		throw new UnsupportedOperationException();
	}
	public void newAnnotation() {
		throw new UnsupportedOperationException();
	}
	public void removeAnnotation() {
		throw new UnsupportedOperationException();
	}
	public boolean isUnset() {
		throw new UnsupportedOperationException();
	}

	// ********** NestableAnnotation implementation **********
	public void moveAnnotation(@SuppressWarnings("unused") int index) {
		throw new UnsupportedOperationException();
	}
	public void convertToNested(@SuppressWarnings("unused") ContainerAnnotation<? extends NestableAnnotation> containerAnnotation, @SuppressWarnings("unused") DeclarationAnnotationAdapter containerAnnotationAdapter, @SuppressWarnings("unused") int index) {
		throw new UnsupportedOperationException();
	}
	public void convertToStandAlone() {
		throw new UnsupportedOperationException();
	}

	// ********** misc **********
	public void storeOn(Map<String, Object> map) {
		throw new UnsupportedOperationException();
	}
	public void restoreFrom(Map<String, Object> map) {
		throw new UnsupportedOperationException();
	}
}