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

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.context.GeneratedValue;
import org.eclipse.jpt.core.resource.java.GeneratedValueAnnotation;
import org.eclipse.jpt.core.utility.TextRange;

/**
 * 
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 2.0
 * @since 2.0
 */
public interface JavaGeneratedValue
	extends GeneratedValue, JavaJpaContextNode
{

	/**
	 * Return the (best guess) text location of the generator.
	 */
	TextRange getGeneratorTextRange(CompilationUnit astRoot);
	
	void initialize(GeneratedValueAnnotation generatedValueAnnotation);
	
	/**
	 * Update the JavaGeneratedValue context model object to match the GeneratedValueAnnotation 
	 * resource model object. see {@link org.eclipse.jpt.core.JpaProject#update()}
	 */
	void update(GeneratedValueAnnotation generatedValueAnnotation);

}
