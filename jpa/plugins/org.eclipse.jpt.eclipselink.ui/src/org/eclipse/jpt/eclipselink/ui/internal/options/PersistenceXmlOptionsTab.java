/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.options;

import org.eclipse.jpt.eclipselink.core.internal.context.options.Options;
import org.eclipse.jpt.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.jpt.ui.WidgetFactory;
import org.eclipse.jpt.ui.details.JpaPageComposite;
import org.eclipse.jpt.ui.internal.widgets.AbstractFormPane;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * PersistenceXmlOptionsTab
 */
public class PersistenceXmlOptionsTab
	extends AbstractFormPane<Options>
	implements JpaPageComposite
{
	// ********** constructors/initialization **********
	public PersistenceXmlOptionsTab(
				PropertyValueModel<Options> subjectHolder, 
				Composite parent, 
				WidgetFactory widgetFactory) {
		
		super(subjectHolder, parent, widgetFactory);
	}

	@Override
	protected void initializeLayout(Composite container) {
		new EclipseLinkOptionsComposite(this, container);
	}

	// ********** JpaPageComposite implementation **********
	public String getHelpID() {
		return null;
	}

	public Image getPageImage() {
		return null;
	}

	public String getPageText() {
		return EclipseLinkUiMessages.PersistenceXmlOptionsTab_title;
	}

	// ********** Layout **********
	@Override
	protected Composite buildContainer(Composite parent) {
		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.marginTop = 0;
		layout.marginLeft = 0;
		layout.marginBottom = 0;
		layout.marginRight = 0;
		layout.verticalSpacing = 15;
		Composite container = this.buildPane(parent, layout);
		this.updateGridData(container);
		return container;
	}

	private void updateGridData(Composite container) {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		container.setLayoutData(gridData);
	}
}
