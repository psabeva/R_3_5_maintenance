/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.resource.java;

import java.util.Iterator;
import org.eclipse.jpt.core.internal.content.java.mappings.JPA;
import org.eclipse.jpt.core.internal.jdtutility.DeclarationAnnotationAdapter;
import org.eclipse.jpt.core.internal.jdtutility.Type;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;

public class JavaEntityProvider implements MappingAnnotationProvider
{
	// singleton
	private static final JavaEntityProvider INSTANCE = new JavaEntityProvider();

	/**
	 * Return the singleton.
	 */
	public static MappingAnnotationProvider instance() {
		return INSTANCE;
	}

	/**
	 * Ensure non-instantiability.
	 */
	private JavaEntityProvider() {
		super();
	}

	public Entity buildAnnotation(Type type, JpaPlatform jpaPlatform) {
		return new EntityImpl(type, jpaPlatform);
	}

	public Iterator<String> correspondingAnnotationNames() {
		return new ArrayIterator<String>(
			JPA.TABLE,
			JPA.SECONDARY_TABLE,
			JPA.SECONDARY_TABLES,
			JPA.PRIMARY_KEY_JOIN_COLUMN,
			JPA.PRIMARY_KEY_JOIN_COLUMNS,
			JPA.ID_CLASS,
			JPA.INHERITANCE,
			JPA.DISCRIMINATOR_VALUE,
			JPA.DISCRIMINATOR_COLUMN,
			JPA.SEQUENCE_GENERATOR,
			JPA.TABLE_GENERATOR,
			JPA.NAMED_QUERY,
			JPA.NAMED_QUERIES,
			JPA.NAMED_NATIVE_QUERY,
			JPA.NAMED_NATIVE_QUERIES,
			JPA.SQL_RESULT_SET_MAPPING,
			JPA.EXCLUDE_DEFAULT_LISTENERS,
			JPA.EXCLUDE_SUPERCLASS_LISTENERS,
			JPA.ENTITY_LISTENERS,
			JPA.PRE_PERSIST,
			JPA.POST_PERSIST,
			JPA.PRE_REMOVE,
			JPA.POST_REMOVE,
			JPA.PRE_UPDATE,
			JPA.POST_UPDATE,
			JPA.POST_LOAD,
			JPA.ATTRIBUTE_OVERRIDE,
			JPA.ATTRIBUTE_OVERRIDES,
			JPA.ASSOCIATION_OVERRIDE,
			JPA.ASSOCIATION_OVERRIDES);
	}

	public DeclarationAnnotationAdapter getDeclarationAnnotationAdapter() {
		return Entity.DECLARATION_ANNOTATION_ADAPTER;
	}

	public String getAnnotationName() {
		return JPA.ENTITY;
	}
}
