/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.resource.java;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jpt.core.internal.resource.java.binary.BinaryLobAnnotation;
import org.eclipse.jpt.core.internal.resource.java.source.SourceLobAnnotation;
import org.eclipse.jpt.core.resource.java.Annotation;
import org.eclipse.jpt.core.resource.java.AnnotationDefinition;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentMember;
import org.eclipse.jpt.core.resource.java.LobAnnotation;
import org.eclipse.jpt.core.utility.jdt.Attribute;
import org.eclipse.jpt.core.utility.jdt.Member;

/**
 * javax.persistence.Lob
 */
public final class LobAnnotationDefinition
	implements AnnotationDefinition
{
	// singleton
	private static final AnnotationDefinition INSTANCE = new LobAnnotationDefinition();

	/**
	 * Return the singleton.
	 */
	public static AnnotationDefinition instance() {
		return INSTANCE;
	}

	/**
	 * Ensure single instance.
	 */
	private LobAnnotationDefinition() {
		super();
	}

	public Annotation buildAnnotation(JavaResourcePersistentMember parent, Member member) {
		return new SourceLobAnnotation((JavaResourcePersistentAttribute) parent, (Attribute) member);
	}

	public Annotation buildNullAnnotation(JavaResourcePersistentMember parent) {
		throw new UnsupportedOperationException();
	}

	public Annotation buildAnnotation(JavaResourcePersistentMember parent, IAnnotation jdtAnnotation) {
		return new BinaryLobAnnotation((JavaResourcePersistentAttribute) parent, jdtAnnotation);
	}

	public String getAnnotationName() {
		return LobAnnotation.ANNOTATION_NAME;
	}

}
