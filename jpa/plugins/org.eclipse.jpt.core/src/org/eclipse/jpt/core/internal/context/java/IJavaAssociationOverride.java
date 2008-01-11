/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import java.util.ListIterator;
import org.eclipse.jpt.core.internal.context.base.IAssociationOverride;
import org.eclipse.jpt.core.internal.resource.java.AssociationOverride;

public interface IJavaAssociationOverride extends IAssociationOverride, IJavaJpaContextNode
{
	@SuppressWarnings("unchecked")
	ListIterator<IJavaJoinColumn> joinColumns();
	@SuppressWarnings("unchecked")
	ListIterator<IJavaJoinColumn> specifiedJoinColumns();
	@SuppressWarnings("unchecked")
	ListIterator<IJavaJoinColumn> defaultJoinColumns();
	IJavaJoinColumn addSpecifiedJoinColumn(int index);
	
	void initializeFromResource(AssociationOverride associationOverride);
	
	void update(AssociationOverride associationOverride);
	
}
