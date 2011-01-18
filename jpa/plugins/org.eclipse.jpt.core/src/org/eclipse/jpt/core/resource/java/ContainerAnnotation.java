/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.resource.java;

/**
 * Common behavior for all "container" annotations.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 2.2
 * @since 2.0
 */
public interface ContainerAnnotation<T extends NestableAnnotation>
	extends Annotation, AnnotationContainer<T>
{
	/**
	 * Convert the specified stand-alone annotation to an annotation nested
	 * within the container without firing change notification.
	 */
	void nestStandAloneAnnotation(NestableAnnotation standAloneAnnotation);

	/**
	 * In preparation for a just-nested annotation being written to the source
	 * file, add the just-nested annotation to the container annotation at the
	 * specified index without firing change notification.
	 * 
	 * @see #nestStandAloneAnnotation(NestableAnnotation)
	 */
	void addNestedAnnotation(int index, NestableAnnotation annotation);

	/**
	 * Convert the container's last nested annotation to a stand-alone
	 * annotation without firing change notification.
	 */
	void convertLastNestedAnnotationToStandAlone();
}
