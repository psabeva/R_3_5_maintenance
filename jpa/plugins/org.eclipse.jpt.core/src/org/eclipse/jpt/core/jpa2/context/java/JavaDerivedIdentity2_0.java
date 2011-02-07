/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.jpa2.context.java;

import java.util.List;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.context.java.JavaJpaContextNode;
import org.eclipse.jpt.core.jpa2.context.DerivedIdentity2_0;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

/**
 * Java derived identity
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
public interface JavaDerivedIdentity2_0
	extends DerivedIdentity2_0, JavaJpaContextNode
{
	JavaSingleRelationshipMapping2_0 getMapping();

	JavaIdDerivedIdentityStrategy2_0 getIdDerivedIdentityStrategy();

	JavaMapsIdDerivedIdentityStrategy2_0 getMapsIdDerivedIdentityStrategy();

	void validate(List<IMessage> messages, IReporter reporter, CompilationUnit astRoot);
}