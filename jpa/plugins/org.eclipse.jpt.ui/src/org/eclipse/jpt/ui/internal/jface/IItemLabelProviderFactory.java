/*******************************************************************************
 *  Copyright (c) 2008 Oracle. 
 *  All rights reserved.  This program and the accompanying materials 
 *  are made available under the terms of the Eclipse Public License v1.0 
 *  which accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.ui.internal.jface;

/**
 * Factory interface used to describe how to build IItemLabelProviders
 * for a DelegatingLabelProvider
 */
public interface IItemLabelProviderFactory
{
	IItemLabelProvider buildItemLabelProvider(Object element, DelegatingLabelProvider labelProvider);
}
