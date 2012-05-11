/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import org.eclipse.jpt.core.JpaFactory;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.java.JavaMappedSuperclass;
import org.eclipse.jpt.core.context.java.JavaPersistentType;
import org.eclipse.jpt.core.context.java.JavaTypeMappingDefinition;
import org.eclipse.jpt.core.resource.java.MappedSuperclassAnnotation;

public class JavaMappedSuperclassDefinition
	extends AbstractJavaTypeMappingDefinition
{
	// singleton
	private static final JavaMappedSuperclassDefinition INSTANCE = 
			new JavaMappedSuperclassDefinition();
	
	
	/**
	 * Return the singleton
	 */
	public static JavaTypeMappingDefinition instance() {
		return INSTANCE;
	}
	
	
	/**
	 * Enforce singleton usage
	 */
	private JavaMappedSuperclassDefinition() {
		super();
	}
	
	
	public String getKey() {
		return MappingKeys.MAPPED_SUPERCLASS_TYPE_MAPPING_KEY;
	}
	
	public String getAnnotationName() {
		return MappedSuperclassAnnotation.ANNOTATION_NAME;
	}
	
	public JavaMappedSuperclass buildMapping(JavaPersistentType parent, JpaFactory factory) {
		return factory.buildJavaMappedSuperclass(parent);
	}
}
