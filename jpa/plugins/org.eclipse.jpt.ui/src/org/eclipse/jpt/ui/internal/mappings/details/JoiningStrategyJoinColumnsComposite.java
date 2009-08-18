/*******************************************************************************
 * Copyright (c) 2005, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.mappings.details;

import java.util.ListIterator;
import org.eclipse.jpt.core.context.JoinColumn;
import org.eclipse.jpt.core.context.JoinColumnEnabledRelationshipReference;
import org.eclipse.jpt.core.context.JoinColumnJoiningStrategy;
import org.eclipse.jpt.ui.internal.mappings.details.JoinColumnsComposite.JoinColumnsEditor;
import org.eclipse.jpt.ui.internal.widgets.FormPane;
import org.eclipse.jpt.ui.internal.widgets.PostExecution;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.widgets.Composite;

/**
 * Here is the layout of this pane:
 * <pre>                                            
 * ---------------------------------------------------------------------
 * |                                                                   |
 * | JoinColumnsComposite                                              |
 * |                                                                   |
 * ---------------------------------------------------------------------
 * -------------------------------------------------------------------------</pre>
 *
 * @see JoinColumnEnabledRelationshipReference
 * @see JoinColumnJoiningStrategy
 * @see JoinColumnJoiningStrategyPane
 * @see JoinColumnInJoiningStrategyDialog
 *
 * @version 3.0
 * @since 2.0
 */
public class JoiningStrategyJoinColumnsComposite 
	extends FormPane<JoinColumnJoiningStrategy>
{
	
	private JoinColumnsComposite<JoinColumnJoiningStrategy> joinColumnsComposite;
	
	public JoiningStrategyJoinColumnsComposite(
			FormPane<?> parentPane,
			PropertyValueModel<JoinColumnJoiningStrategy> subjectHolder,
			Composite parent) {
		super(parentPane, subjectHolder, parent);
	}


	@Override
	protected void initializeLayout(Composite container) {		
		this.joinColumnsComposite = new JoinColumnsComposite<JoinColumnJoiningStrategy>(this, container, buildJoinColumnsProvider());
	}
	
	private JoinColumnsEditor<JoinColumnJoiningStrategy> buildJoinColumnsProvider() {
		return new JoinColumnsEditor<JoinColumnJoiningStrategy>() {

			public void addJoinColumn(JoinColumnJoiningStrategy subject) {
				JoiningStrategyJoinColumnsComposite.this.addJoinColumn(subject);
			}

			public boolean hasSpecifiedJoinColumns(JoinColumnJoiningStrategy subject) {
				return subject.hasSpecifiedJoinColumns();
			}

			public void editJoinColumn(JoinColumnJoiningStrategy subject, JoinColumn joinColumn) {
				JoiningStrategyJoinColumnsComposite.this.editJoinColumn(subject, joinColumn);
			}

			public JoinColumn getDefaultJoinColumn(JoinColumnJoiningStrategy subject) {
				return subject.getDefaultJoinColumn();
			}

			public String getDefaultPropertyName() {
				return JoinColumnJoiningStrategy.DEFAULT_JOIN_COLUMN_PROPERTY;
			}

			public String getSpecifiedJoinColumnsListPropertyName() {
				return JoinColumnJoiningStrategy.SPECIFIED_JOIN_COLUMNS_LIST;
			}

			public void removeJoinColumns(JoinColumnJoiningStrategy subject, int[] selectedIndices) {
				for (int index = selectedIndices.length; --index >= 0; ) {
					subject.removeSpecifiedJoinColumn(selectedIndices[index]);
				}				
			}

			public ListIterator<JoinColumn> specifiedJoinColumns(JoinColumnJoiningStrategy subject) {
				return subject.specifiedJoinColumns();
			}

			public int specifiedJoinColumnsSize(JoinColumnJoiningStrategy subject) {
				return subject.specifiedJoinColumnsSize();
			}
		};
	}
	
	private void addJoinColumn(JoinColumnJoiningStrategy joiningStrategy) {
		JoinColumnInJoiningStrategyDialog dialog =
			new JoinColumnInJoiningStrategyDialog(getShell(), joiningStrategy, null);
		dialog.openDialog(buildAddJoinColumnPostExecution());
	}
	
	private PostExecution<JoinColumnInJoiningStrategyDialog> buildAddJoinColumnPostExecution() {
		return new PostExecution<JoinColumnInJoiningStrategyDialog>() {
			public void execute(JoinColumnInJoiningStrategyDialog dialog) {
				if (dialog.wasConfirmed()) {
					addJoinColumn(dialog.getSubject());
				}
			}
		};
	}
	
	private void addJoinColumn(JoinColumnInJoiningStrategyStateObject stateObject) {
		JoinColumnJoiningStrategy subject = getSubject();
		int index = subject.specifiedJoinColumnsSize();
		
		JoinColumn joinColumn = subject.addSpecifiedJoinColumn(index);
		stateObject.updateJoinColumn(joinColumn);
		this.setSelectedJoinColumn(joinColumn);
	}

	public void setSelectedJoinColumn(JoinColumn joinColumn) {
		this.joinColumnsComposite.setSelectedJoinColumn(joinColumn);
	}
	
	private void editJoinColumn(JoinColumnJoiningStrategy joiningStrategy, JoinColumn joinColumn) {
		JoinColumnInJoiningStrategyDialog dialog =
			new JoinColumnInJoiningStrategyDialog(getShell(), joiningStrategy, joinColumn);
		dialog.openDialog(buildEditJoinColumnPostExecution());
	}
	
	private PostExecution<JoinColumnInJoiningStrategyDialog> buildEditJoinColumnPostExecution() {
		return new PostExecution<JoinColumnInJoiningStrategyDialog>() {
			public void execute(JoinColumnInJoiningStrategyDialog dialog) {
				if (dialog.wasConfirmed()) {
					updateJoinColumn(dialog.getSubject());
				}
			}
		};
	}
	
	private void updateJoinColumn(JoinColumnInJoiningStrategyStateObject stateObject) {
		stateObject.updateJoinColumn(stateObject.getJoinColumn());
	}
}
