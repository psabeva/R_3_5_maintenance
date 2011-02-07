/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.selection;

import org.eclipse.jpt.ui.internal.views.JpaDetailsView;


public class JpaDetailsSelectionParticipant
	implements JpaSelectionParticipant 
{
	private final JpaDetailsView detailsView;
	
	
	public JpaDetailsSelectionParticipant(JpaDetailsView detailsView) {
		super();
		this.detailsView = detailsView;
	}

	public JpaSelection getSelection() {
		return this.detailsView.getSelection();
	}

	public void selectionChanged(JpaSelectionEvent evt) {
		this.detailsView.select(evt.getSelection());
	}

	public boolean disposeOnHide() {
		return false;
	}

	public void dispose() {
		// NOP
	}

}