/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa2.resource.java;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.Attribute;
import org.eclipse.jpt.core.internal.jpa2.resource.java.binary.BinaryMapKeyJoinColumns2_0Annotation;
import org.eclipse.jpt.core.jpa2.resource.java.MapKeyJoinColumns2_0Annotation;
import org.eclipse.jpt.core.resource.java.Annotation;
import org.eclipse.jpt.core.resource.java.AnnotationDefinition;
import org.eclipse.jpt.core.resource.java.JavaResourceAnnotatedElement;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;

/**
 * javax.persistence.MapKeyJoinColumns
 */
public final class MapKeyJoinColumns2_0AnnotationDefinition
	implements AnnotationDefinition
{
	// singleton
	private static final AnnotationDefinition INSTANCE = new MapKeyJoinColumns2_0AnnotationDefinition();

	/**
	 * Return the singleton.
	 */
	public static AnnotationDefinition instance() {
		return INSTANCE;
	}

	/**
	 * Ensure single instance.
	 */
	private MapKeyJoinColumns2_0AnnotationDefinition() {
		super();
	}

	public MapKeyJoinColumns2_0Annotation buildAnnotation(JavaResourceAnnotatedElement parent, AnnotatedElement annotatedElement) {
		return new SourceMapKeyJoinColumns2_0Annotation((JavaResourcePersistentAttribute) parent, (Attribute) annotatedElement);
	}

	public MapKeyJoinColumns2_0Annotation buildNullAnnotation(JavaResourceAnnotatedElement parent) {
		throw new UnsupportedOperationException();
	}

	public Annotation buildAnnotation(JavaResourceAnnotatedElement parent, IAnnotation jdtAnnotation) {
		return new BinaryMapKeyJoinColumns2_0Annotation((JavaResourcePersistentAttribute) parent, jdtAnnotation);
	}

	public String getAnnotationName() {
		return MapKeyJoinColumns2_0Annotation.ANNOTATION_NAME;
	}

}