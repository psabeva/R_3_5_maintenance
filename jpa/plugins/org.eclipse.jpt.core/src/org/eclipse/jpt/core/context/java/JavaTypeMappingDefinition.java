/*******************************************************************************
 * Copyright (c) 2006, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context.java;

import org.eclipse.jpt.core.JpaFactory;
import org.eclipse.jpt.core.resource.java.Annotation;

/**
 * Map a string key to a type mapping and its corresponding
 * Java annotations.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 *
 * @version 2.3
 * @since 2.3
 */
public interface JavaTypeMappingDefinition
{
	/**
	 * Return the type mapping's key.
	 */
	String getKey();

	/**
	 * Return the name of the type mapping's annotation.
	 */
	String getAnnotationName();

	/**
	 * Return the names of the type mapping's "supporting" annotations.
	 */
	Iterable<String> getSupportingAnnotationNames();

	/**
	 * Build a Java type mapping for the specified persistent type and
	 * annotation.
	 * Use the specified factory for creation so extenders can simply override
	 * the appropriate factory method instead of building a definition for the
	 * same key.
	 */
	JavaTypeMapping buildMapping(JavaPersistentType persistentType, Annotation annotation, JpaFactory factory);
}