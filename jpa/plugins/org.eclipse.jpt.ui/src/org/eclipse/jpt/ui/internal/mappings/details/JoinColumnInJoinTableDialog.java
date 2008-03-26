/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.mappings.details;

import org.eclipse.jpt.core.context.JoinColumn;
import org.eclipse.jpt.core.context.JoinTable;
import org.eclipse.jpt.ui.internal.widgets.AbstractDialogPane;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * This dialog is used to either create or edit a joing column that is located
 * on a join table.
 *
 * @see JoinColumn
 * @see JoinTable
 * @see JoinColumnInJoinTableStateObject
 * @see AbstractJoinColumnDialogPane
 *
 * @version 2.0
 * @since 1.0
 */
public class JoinColumnInJoinTableDialog extends BaseJoinColumnDialog<JoinColumnInJoinTableStateObject> {

	/**
	 * Creates a new <code>JoinColumnInJoinTableDialog</code>.
	 *
	 * @param parent The parent shell
	 * @param joinTable The parent of the join column to edit or to create
	 * @param joinColumn Either the join column to edit or <code>null</code> if
	 * this state object is used to create a new one
	 */
	public JoinColumnInJoinTableDialog(Shell parent,
	                                   JoinTable joinTable,
	                                   JoinColumn joinColumn) {

		super(parent, joinTable, joinColumn);
	}

	/*
	 * non-Javadoc)
	 */
	@Override
	protected AbstractDialogPane<JoinColumnInJoinTableStateObject> buildLayout(Composite container) {
		return new BaseJoinColumnDialogPane<JoinColumnInJoinTableStateObject>(
			subjectHolder(),
			container
		);
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected JoinColumnInJoinTableStateObject buildStateObject() {
		return new JoinColumnInJoinTableStateObject(
			getOwner(),
			getJoinColumn()
		);
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public JoinColumn getJoinColumn() {
		return (JoinColumn) super.getJoinColumn();
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected JoinTable getOwner() {
		return (JoinTable) super.getOwner();
	}
}