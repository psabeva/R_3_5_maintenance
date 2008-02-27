/*******************************************************************************
 * Copyright (c) 2006, 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.TextRange;
import org.eclipse.jpt.core.context.GeneratedValue;
import org.eclipse.jpt.core.resource.java.GeneratedValueAnnotation;

public interface JavaGeneratedValue extends GeneratedValue, JavaJpaContextNode
{

	/**
	 * Return the (best guess) text location of the generator.
	 */
	TextRange generatorTextRange(CompilationUnit astRoot);
	
	void initializeFromResource(GeneratedValueAnnotation generatedValue);
	
	void update(GeneratedValueAnnotation generatedValue);

}