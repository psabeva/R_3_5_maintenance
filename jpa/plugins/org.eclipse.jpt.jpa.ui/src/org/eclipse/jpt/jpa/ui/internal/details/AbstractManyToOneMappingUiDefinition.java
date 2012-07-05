/*******************************************************************************
 *  Copyright (c) 2008, 2009  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.details;

import org.eclipse.jpt.jpa.core.MappingKeys;
import org.eclipse.jpt.jpa.core.context.ManyToOneMapping;
import org.eclipse.jpt.jpa.ui.internal.JpaMappingImageHelper;
import org.eclipse.swt.graphics.Image;

public abstract class AbstractManyToOneMappingUiDefinition<M, T extends ManyToOneMapping>
	extends AbstractMappingUiDefinition<M, T>
{
	protected AbstractManyToOneMappingUiDefinition() {
		super();
	}
	
	
	public Image getImage() {
		return JpaMappingImageHelper.imageForAttributeMapping(getKey());
	}
	
	public String getLabel() {
		return JptUiDetailsMessages.ManyToOneMappingUiProvider_label;
	}
	
	public String getLinkLabel() {
		return JptUiDetailsMessages.ManyToOneMappingUiProvider_linkLabel;
	}
	
	public String getKey() {
		return MappingKeys.MANY_TO_ONE_ATTRIBUTE_MAPPING_KEY;
	}
}