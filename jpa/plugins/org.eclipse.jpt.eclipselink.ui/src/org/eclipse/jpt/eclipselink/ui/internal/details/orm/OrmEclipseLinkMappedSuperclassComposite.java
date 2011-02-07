/*******************************************************************************
 *  Copyright (c) 2008, 2010 Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.details.orm;

import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.eclipselink.core.context.orm.OrmEclipseLinkMappedSuperclass;
import org.eclipse.swt.widgets.Composite;

public class OrmEclipseLinkMappedSuperclassComposite<T extends OrmEclipseLinkMappedSuperclass>
	extends AbstractOrmEclipseLinkMappedSuperclassComposite<T>
{
	public OrmEclipseLinkMappedSuperclassComposite(
			PropertyValueModel<? extends T> subjectHolder,
			Composite parent, WidgetFactory widgetFactory) {
		
		super(subjectHolder, parent, widgetFactory);
	}
}