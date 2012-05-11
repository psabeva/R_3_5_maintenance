/*******************************************************************************
 *  Copyright (c) 2009  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.internal.resource.xml;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jpt.core.JpaFile;
import org.eclipse.jpt.core.JpaResourceModel;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.resource.xml.JpaXmlResource;

public class JpaXmlResourceAdapterFactory
	implements IAdapterFactory
{
	private static final Class<?>[] ADAPTER_LIST = new Class[] { JpaXmlResource.class };
	
	public Class<?>[] getAdapterList() {
		return ADAPTER_LIST;
	}
	
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("unchecked") Class adapterType) {
		if (adaptableObject instanceof IFile) {
			return this.getAdapter((IFile) adaptableObject, adapterType);
		}
		return null;
	}
	
	private Object getAdapter(final IFile file, Class <?>adapterType) {
		if (adapterType == JpaXmlResource.class) {
			JpaFile jpaFile = JptCorePlugin.getJpaFile(file);
			if (jpaFile != null) {
				JpaResourceModel resourceModel = jpaFile.getResourceModel();
				if (resourceModel instanceof JpaXmlResource) {
					return resourceModel;
				}
			}
		}
		return null;
	}
}
