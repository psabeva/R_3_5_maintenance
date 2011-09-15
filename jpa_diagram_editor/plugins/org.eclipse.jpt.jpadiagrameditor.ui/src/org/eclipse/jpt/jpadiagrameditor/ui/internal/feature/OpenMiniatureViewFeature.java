/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2005, 2010 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Stefan Dimov - initial API, implementation and documentation
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.jpt.jpadiagrameditor.ui.internal.feature;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.JPADiagramEditorPlugin;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.i18n.JPAEditorMessages;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;


public class OpenMiniatureViewFeature extends AbstractCustomFeature {
	
	public OpenMiniatureViewFeature(IFeatureProvider fp) {
		super(fp);
	}

	public boolean canExecute(ICustomContext context) {
		return true;
	}	
	
	public void execute(ICustomContext context) {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().
					showView(JPAEditorConstants.ID_VIEW_MINIATURE);
		} catch (PartInitException e) {
			JPADiagramEditorPlugin.logError("Can't open Miniature view", e);  //$NON-NLS-1$		 			
		}
	}
	
	@Override
	public String getName() {
		return JPAEditorMessages.JPAEditorToolBehaviorProvider_openMiniatureView;
	}

}
