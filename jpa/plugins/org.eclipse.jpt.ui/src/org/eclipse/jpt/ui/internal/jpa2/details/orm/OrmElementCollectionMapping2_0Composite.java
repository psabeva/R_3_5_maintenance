/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.jpa2.details.orm;

import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.core.context.AccessHolder;
import org.eclipse.jpt.core.jpa2.context.ElementCollectionMapping2_0;
import org.eclipse.jpt.ui.internal.details.AccessTypeComposite;
import org.eclipse.jpt.ui.internal.details.FetchTypeComposite;
import org.eclipse.jpt.ui.internal.details.orm.OrmMappingNameChooser;
import org.eclipse.jpt.ui.internal.jpa2.details.AbstractElementCollectionMapping2_0Composite;
import org.eclipse.jpt.ui.internal.jpa2.details.CollectionTable2_0Composite;
import org.eclipse.jpt.ui.internal.jpa2.details.TargetClassComposite;
import org.eclipse.swt.widgets.Composite;

public class OrmElementCollectionMapping2_0Composite
	extends AbstractElementCollectionMapping2_0Composite<ElementCollectionMapping2_0>
{
	/**
	 * Creates a new <code>EclipseLink1_1OrmBasicMappingComposite</code>.
	 *
	 * @param subjectHolder The holder of the subject <code>BasicMapping</code>
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create various common widgets
	 */
	public OrmElementCollectionMapping2_0Composite(PropertyValueModel<? extends ElementCollectionMapping2_0> subjectHolder,
	                               Composite parent,
	                               WidgetFactory widgetFactory) {

		super(subjectHolder, parent, widgetFactory);
	}

	@Override
	protected void initializeElementCollectionSection(Composite container) {
		new TargetClassComposite(this, container);
		new OrmMappingNameChooser(this, getSubjectHolder(), container);
		new AccessTypeComposite(this, buildAccessHolderHolder(), container);
		new FetchTypeComposite(this, container);
		new CollectionTable2_0Composite(this, buildCollectionTableHolder(), container);
	}
	
	protected PropertyValueModel<AccessHolder> buildAccessHolderHolder() {
		return new PropertyAspectAdapter<ElementCollectionMapping2_0, AccessHolder>(getSubjectHolder()) {
			@Override
			protected AccessHolder buildValue_() {
				return this.subject.getPersistentAttribute();
			}
		};
	}
}