/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.selection;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jpt.jpa.ui.internal.views.structure.JpaStructureView;

public class JpaStructureSelectionParticipant
	implements JpaSelectionParticipant 
{
	final JpaStructureView structureView;
	
	
	public JpaStructureSelectionParticipant(JpaSelectionManager selectionManager, JpaStructureView structureView) {
		super();
		this.structureView = structureView;
		structureView.addSelectionChangedListener(new StructureViewSelectionListener(selectionManager, structureView));
	}

	public JpaSelection getSelection() {
		return this.structureView.getJpaSelection();
	}
	
	public void selectionChanged(JpaSelectionEvent evt) {
		if (evt.getSource() != this) {
			this.structureView.select(evt.getSelection());
		}
	}

	public boolean disposeOnHide() {
		return false;
	}
	
	public void dispose() {
		// NOP
	}
	

	// ********** listener **********

	private class StructureViewSelectionListener 
		implements ISelectionChangedListener
	{
		private final JpaSelectionManager selectionManager;

		StructureViewSelectionListener(JpaSelectionManager selectionManager, JpaStructureView structureView) {
			super();
			this.selectionManager = selectionManager;
		}

		public void selectionChanged(SelectionChangedEvent event) {
			if (structureView.getViewSite().getWorkbenchWindow().getPartService().getActivePart() == structureView) {
				selectionManager.select(this.structureViewSelection(), JpaStructureSelectionParticipant.this);
			}
		}

		private JpaSelection structureViewSelection() {
			return JpaStructureSelectionParticipant.this.structureView.getJpaSelection();
		}
	}
}