/*******************************************************************************
 * Copyright (c) 2009, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.jpa2.details.orm;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.jpa2.context.OneToManyMapping2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.OneToManyRelationship2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.OrphanRemovable2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.orm.OrmCascade2_0;
import org.eclipse.jpt.jpa.ui.internal.JptUiMessages;
import org.eclipse.jpt.jpa.ui.internal.details.AbstractOneToManyMappingComposite;
import org.eclipse.jpt.jpa.ui.internal.details.AccessTypeComboViewer;
import org.eclipse.jpt.jpa.ui.internal.details.FetchTypeComboViewer;
import org.eclipse.jpt.jpa.ui.internal.details.JptUiDetailsMessages;
import org.eclipse.jpt.jpa.ui.internal.details.TargetEntityClassChooser;
import org.eclipse.jpt.jpa.ui.internal.details.orm.JptUiDetailsOrmMessages;
import org.eclipse.jpt.jpa.ui.internal.details.orm.OrmMappingNameText;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.CascadePane2_0;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.OneToManyJoiningStrategy2_0Pane;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.Ordering2_0Composite;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.OrphanRemoval2_0TriStateCheckBox;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Hyperlink;

public class OrmOneToManyMapping2_0Composite
	extends AbstractOneToManyMappingComposite<OneToManyMapping2_0, OneToManyRelationship2_0, OrmCascade2_0>
{
	public OrmOneToManyMapping2_0Composite(
			PropertyValueModel<? extends OneToManyMapping2_0> mappingModel,
			PropertyValueModel<Boolean> enabledModel,
			Composite parentComposite,
			WidgetFactory widgetFactory,
			ResourceManager resourceManager) {
		super(mappingModel, enabledModel, parentComposite, widgetFactory, resourceManager);
	}
	
	
	@Override
	protected Control initializeOneToManySection(Composite container) {
		container = this.addSubPane(container, 2, 0, 0, 0, 0);

		// Target entity widgets
		Hyperlink targetEntityHyperlink = this.addHyperlink(container, JptUiDetailsMessages.TargetEntityChooser_label);
		new TargetEntityClassChooser(this, container, targetEntityHyperlink);

		// Name widgets
		this.addLabel(container, JptUiDetailsOrmMessages.OrmMappingNameChooser_name);
		new OrmMappingNameText(this, getSubjectHolder(), container);

		// Access type widgets
		this.addLabel(container, JptUiMessages.AccessTypeComposite_access);
		new AccessTypeComboViewer(this, this.buildAccessReferenceModel(), container);

		// Fetch type widgets
		this.addLabel(container, JptUiDetailsMessages.BasicGeneralSection_fetchLabel);
		new FetchTypeComboViewer(this, container);

		// Orphan removal widgets
		PropertyValueModel<OrphanRemovable2_0> orphanRemovableHolder = buildOrphanRemovableModel();
		new OrphanRemoval2_0TriStateCheckBox(this, orphanRemovableHolder, container);

		// Cascade widgets
		CascadePane2_0 cascadePane = new CascadePane2_0(this, this.buildCascadeModel(), container);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		cascadePane.getControl().setLayoutData(gridData);

		return container;
	}

	@Override
	protected void initializeJoiningStrategyCollapsibleSection(Composite container) {
		new OneToManyJoiningStrategy2_0Pane(this, this.buildRelationshipModel(), container);
	}

	@Override
	protected Control initializeOrderingSection(Composite container) {
		return new Ordering2_0Composite(this, container).getControl();
	}
	
	protected PropertyValueModel<OrphanRemovable2_0> buildOrphanRemovableModel() {
		return new PropertyAspectAdapter<OneToManyMapping2_0, OrphanRemovable2_0>(this.getSubjectHolder()) {
			@Override
			protected OrphanRemovable2_0 buildValue_() {
				return this.subject.getOrphanRemoval();
			}
		};
	}
}
