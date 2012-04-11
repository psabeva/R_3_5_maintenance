/*******************************************************************************
 *  Copyright (c) 2009, 2011 Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.ui.internal.details.orm;

import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.context.orm.OrmMappedSuperclass;
import org.eclipse.jpt.jpa.eclipselink.core.context.orm.OrmEclipseLinkCaching;
import org.eclipse.swt.widgets.Composite;

public class OrmEclipseLinkMappedSuperclass2_0Composite
	extends AbstractOrmEclipseLinkMappedSuperclassComposite
{
	public OrmEclipseLinkMappedSuperclass2_0Composite(
			PropertyValueModel<? extends OrmMappedSuperclass> subjectHolder,
			Composite parent, WidgetFactory widgetFactory) {
		
		super(subjectHolder, parent, widgetFactory);
	}

	@Override
	protected void initializeCachingSection(Composite container, PropertyValueModel<OrmEclipseLinkCaching> cachingHolder) {
		new OrmEclipseLinkCaching2_0Composite(this, cachingHolder, container);
	}
}