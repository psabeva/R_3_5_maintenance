/*******************************************************************************
 * Copyright (c) 2010, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.jpa2.details;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.jpa2.context.EmbeddedIdMapping2_0;
import org.eclipse.jpt.jpa.ui.internal.details.AbstractEmbeddedIdMappingComposite;
import org.eclipse.jpt.jpa.ui.internal.details.EmbeddedMappingOverridesComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class EmbeddedIdMapping2_0Composite
	extends AbstractEmbeddedIdMappingComposite<EmbeddedIdMapping2_0>
{
	public EmbeddedIdMapping2_0Composite(
			PropertyValueModel<? extends EmbeddedIdMapping2_0> mappingModel,
			PropertyValueModel<Boolean> enabledModel,
			Composite parentComposite,
			WidgetFactory widgetFactory,
			ResourceManager resourceManager) {
		super(mappingModel, enabledModel, parentComposite, widgetFactory, resourceManager);
	}
	
	
	@Override
	protected Control initializeEmbeddedIdSection(Composite container) {
		container = this.addSubPane(container);

		new EmbeddedIdMapping2_0MappedByRelationshipPane(this, getSubjectHolder(), container);
		new EmbeddedMappingOverridesComposite(this, container);

		return container;
	}
}
