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

import org.eclipse.jpt.core.context.java.JavaAttributeMappingDefinition;

public class JavaElementCollectionMappingDefinition2_0
	extends AbstractJavaElementCollectionMappingDefinition2_0
{
	// singleton
	private static final JavaAttributeMappingDefinition INSTANCE = new JavaElementCollectionMappingDefinition2_0();

	/**
	 * Return the singleton.
	 */
	public static JavaAttributeMappingDefinition instance() {
		return INSTANCE;
	}


	/**
	 * Enforce singleton usage
	 */
	private JavaElementCollectionMappingDefinition2_0() {
		super();
	}
}
