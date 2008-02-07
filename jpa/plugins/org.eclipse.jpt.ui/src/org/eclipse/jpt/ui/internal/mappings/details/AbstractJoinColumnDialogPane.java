/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.mappings.details;

import java.util.Collection;
import java.util.Iterator;
import org.eclipse.jpt.db.internal.Table;
import org.eclipse.jpt.ui.internal.IJpaHelpContextIds;
import org.eclipse.jpt.ui.internal.mappings.JptUiMappingsMessages;
import org.eclipse.jpt.ui.internal.widgets.AbstractDialogPane;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.model.value.PropertyValueModel;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * |                         ------------------------------------------------- |
 * | Name:                   |                                             |v| |
 * |                         ------------------------------------------------- |
 * |                         ------------------------------------------------- |
 * | Referenced Column Name: |                                             |v| |
 * |                         ------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see AbstractJoinColumnStateObject
 * @see JoinColumnInJoinTableDialog - A container of this pane
 * @see PrimaryKeyJoinColumnInSecondaryTableDialog - A container of this pane
 *
 * @version 2.0
 * @since 2.0
 */
public class AbstractJoinColumnDialogPane<T extends AbstractJoinColumnStateObject> extends AbstractDialogPane<T>
{
	private Combo nameCombo;
	private Combo referencedColumnNameCombo;

	/**
	 * Creates a new <code>AbstractJoinColumnDialogPane</code>.
	 *
	 * @param subjectHolder The holder of this pane's subject
	 * @param parent The parent container
	 */
	public AbstractJoinColumnDialogPane(PropertyValueModel<? extends T> subjectHolder,
	                                    Composite parent)
	{
		super(subjectHolder, parent);
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void addPropertyNames(Collection<String> propertyNames) {
		super.addPropertyNames(propertyNames);
		propertyNames.add(AbstractJoinColumnStateObject.REFERENCED_COLUMN_NAME_PROPERTY);
		propertyNames.add(AbstractJoinColumnStateObject.NAME_PROPERTY);
	}

	private ModifyListener buildNameComboListener() {
		return new ModifyListener() {

			private boolean isDefaultName(int selectedIndex, String value) {
				return (selectedIndex == 0) && value.equals(subject().defaultName());
			}

			public void modifyText(ModifyEvent e) {

				if (!isPopulating()) {
					setPopulating(true);

					try {
						Combo combo = (Combo) e.widget;
						String name = combo.getText();
						boolean defaultName = isDefaultName(combo.getSelectionIndex(), name);

						subject().setName(name);
						subject().setDefaultNameSelected(defaultName);
					}
					finally {
						setPopulating(false);
					}
				}
			}
		};
	}

	private ModifyListener buildReferencedColumnNameComboListener() {
		return new ModifyListener() {

			private boolean isDefaultReferencedColumnName(int selectedIndex, String value) {
				return (selectedIndex == 0) && value.equals(subject().defaultReferencedColumnName());
			}

			public void modifyText(ModifyEvent e) {

				if (!isPopulating()) {
					setPopulating(true);

					try {
						Combo combo = (Combo) e.widget;
						String referencedColumnName = combo.getText();
						boolean defaultReferencedColumnName = isDefaultReferencedColumnName(combo.getSelectionIndex(), referencedColumnName);

						subject().setReferencedColumnName(referencedColumnName);
						subject().setDefaultReferencedColumnNameSelected(defaultReferencedColumnName);
					}
					finally {
						setPopulating(false);
					}
				}
			}
		};
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void doPopulate() {
		super.doPopulate();
		populateNameCombo();
		populateReferencedNameCombo();
	}

	protected final Combo getNameCombo() {
		return nameCombo;
	}

	protected final Combo getReferencedColumnNameCombo() {
		return referencedColumnNameCombo;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void initializeLayout(Composite container) {

		// Name widgets
		nameCombo = buildLabeledEditableCombo(
			container,
			JptUiMappingsMessages.JoinColumnDialog_name,
			buildNameComboListener(),
			IJpaHelpContextIds.MAPPING_JOIN_COLUMN_NAME
		);

		// Referenced Column Name widgets
		referencedColumnNameCombo = buildLabeledEditableCombo(
			container,
			JptUiMappingsMessages.JoinColumnDialog_referencedColumnName,
			buildReferencedColumnNameComboListener(),
			IJpaHelpContextIds.MAPPING_JOIN_REFERENCED_COLUMN
		);
	}

	public void populateNameCombo() {

		AbstractJoinColumnStateObject subject = subject();
		nameCombo.removeAll();

		if (subject == null) {
			return;
		}

		// Add the default column name if one exists
		String defaultName = subject.defaultName();

		if (defaultName != null) {
			nameCombo.add(NLS.bind(
				JptUiMappingsMessages.JoinColumnDialog_defaultWithOneParam,
				defaultName
			));
		}

		// Populate the combo with the column names
		Table nameTable = subject.getNameTable();

		if (nameTable != null) {
			Iterator<String> columnNames = nameTable.columnNames();

			for (Iterator<String> iter = CollectionTools.sort(columnNames); iter.hasNext(); ) {
				nameCombo.add(iter.next());
			}
		}

		// Set the selected name
		String name = subject.getName();

		if ((name != null) && !subject.isDefaultNameSelected()) {
			nameCombo.setText(name);
		}
		else if (defaultName != null) {
			nameCombo.select(0);
		}
		else {
			nameCombo.select(-1);
		}
	}

	public void populateReferencedNameCombo() {

		AbstractJoinColumnStateObject subject = subject();
		referencedColumnNameCombo.removeAll();

		if (subject == null) {
			return;
		}

		// Add the default referenced column name if one exists
		String defaultReferencedColumnName = subject.defaultReferencedColumnName();

		if (defaultReferencedColumnName != null) {
			referencedColumnNameCombo.add(NLS.bind(
				JptUiMappingsMessages.JoinColumnDialog_defaultWithOneParam,
				defaultReferencedColumnName)
			);
		}

		// Populate the combo with the column names
		Table referencedNameTable = subject.getReferencedNameTable();

		if (referencedNameTable != null) {
			Iterator<String> columnNames = referencedNameTable.columnNames();

			for (Iterator<String> iter = CollectionTools.sort(columnNames); iter.hasNext(); ) {
				referencedColumnNameCombo.add(iter.next());
			}
		}

		// Set the selected referenced column name
		String referencedColumnName = subject.getReferencedColumnName();

		if ((referencedColumnName != null) && !subject.isDefaultReferencedColumnNameSelected()) {
			referencedColumnNameCombo.setText(referencedColumnName);
		}
		else if (defaultReferencedColumnName != null) {
			referencedColumnNameCombo.select(0);
		}
		else {
			referencedColumnNameCombo.select(-1);
		}
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void propertyChanged(String propertyName) {
		super.propertyChanged(propertyName);

		if (propertyName == AbstractJoinColumnStateObject.NAME_PROPERTY) {
			populateNameCombo();
		}
		else if (propertyName == AbstractJoinColumnStateObject.REFERENCED_COLUMN_NAME_PROPERTY) {
			populateReferencedNameCombo();
		}
	}
}