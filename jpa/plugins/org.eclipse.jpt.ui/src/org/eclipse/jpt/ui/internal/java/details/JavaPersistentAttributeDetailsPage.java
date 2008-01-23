/*******************************************************************************
 * Copyright (c) 2006, 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.java.details;

import java.util.ListIterator;
import org.eclipse.jpt.core.internal.context.base.IAttributeMapping;
import org.eclipse.jpt.core.internal.context.base.IPersistentAttribute;
import org.eclipse.jpt.ui.internal.details.PersistentAttributeDetailsPage;
import org.eclipse.jpt.ui.internal.java.mappings.properties.NullAttributeMappingUiProvider;
import org.eclipse.jpt.ui.internal.platform.base.BaseJpaPlatformUi;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.model.value.PropertyValueModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public class JavaPersistentAttributeDetailsPage
	extends PersistentAttributeDetailsPage
{
	public JavaPersistentAttributeDetailsPage(PropertyValueModel<? extends IPersistentAttribute> subjectHolder,
	                                          Composite parent,
	                                          TabbedPropertySheetWidgetFactory widgetFactory) {

		super(subjectHolder, parent, widgetFactory);
	}

	@Override
	protected ListIterator<IAttributeMappingUiProvider<? extends IAttributeMapping>> attributeMappingUiProviders() {
		// TODO
		return ((BaseJpaPlatformUi) jpaPlatformUi()).javaAttributeMappingUiProviders();
	}

	protected IAttributeMappingUiProvider<IAttributeMapping> nullAttributeMappingUiProvider() {
		return NullAttributeMappingUiProvider.instance();
	}

	@Override
	protected ListIterator<IAttributeMappingUiProvider<? extends IAttributeMapping>> defaultAttributeMappingUiProviders() {
		// TODO
//		return jpaPlatformUi().defaultJavaAttributeMappingUiProviders();
		return ((BaseJpaPlatformUi) jpaPlatformUi()).defaultJavaAttributeMappingUiProviders();
	}

	/**
	 * These IAtttributeMappingUiProviders will be used as elements in the attributeMapping combo
	 * The first element in the combo will be one of the defaultAttributeMappingUiProviders or
	 * if none of those apply the nullAttributeMappingUiProvider will be used. The rest of the elements
	 * will be the attributeMappingUiProviders.  The defaultAttributeMappingUiProvider is
	 * determined by matching its key with the key of the current attributeMapping.
	 */
	@Override
	protected IAttributeMappingUiProvider<? extends IAttributeMapping>[] attributeMappingUiProvidersFor(IPersistentAttribute persistentAttribute) {
		IAttributeMappingUiProvider<? extends IAttributeMapping>[] providers = new IAttributeMappingUiProvider<?>[CollectionTools.size(attributeMappingUiProviders()) + 1];
		providers[0] =  defaultAttributeMappingUiProvider(persistentAttribute.defaultMappingKey());
		int i = 1;
		for (ListIterator<IAttributeMappingUiProvider<? extends IAttributeMapping>> iterator = attributeMappingUiProviders(); iterator.hasNext(); ) {
			providers[i++] = iterator.next();
		}
		return providers;
	}

	@Override
	protected IAttributeMappingUiProvider<? extends IAttributeMapping> defaultAttributeMappingUiProvider(String key) {
		for (ListIterator<IAttributeMappingUiProvider<? extends IAttributeMapping>> i = defaultAttributeMappingUiProviders(); i.hasNext(); ) {
			IAttributeMappingUiProvider<? extends IAttributeMapping> provider = i.next();
			if (provider.attributeMappingKey() == key) {
				return provider;
			}
		}
		return this.nullAttributeMappingUiProvider();
	}

	@Override
	protected void initializeLayout(Composite container) {

		buildLabeledComposite(
			container,
			buildMappingLabel(container),
			buildMappingCombo(container).getControl()
		);

		PageBook mappingPane = buildMappingPageBook(container);

		GridData gridData = new GridData();
		gridData.horizontalAlignment       = SWT.FILL;
		gridData.verticalAlignment         = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace   = true;

		mappingPane.setLayoutData(gridData);
	}
}