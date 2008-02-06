/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	 Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.mappings.details;

import java.util.Collection;
import java.util.Iterator;
import org.eclipse.jpt.core.internal.context.base.INonOwningMapping;
import org.eclipse.jpt.ui.internal.IJpaHelpContextIds;
import org.eclipse.jpt.ui.internal.mappings.JptUiMappingsMessages;
import org.eclipse.jpt.ui.internal.widgets.AbstractFormPane;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.jpt.utility.internal.model.value.PropertyValueModel;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

/**
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * |            -------------------------------------------------------------- |
 * | Mapped By: |                                                          |v| |
 * |            -------------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see INonOwningMapping
 * @see ManyToManyMappingComposite - A container of this pane
 * @see OneToManyMappingComposite - A container of this pane
 * @see OneToOneMappingComposite - A container of this pane
 *
 * @version 2.0
 * @since 1.0
 */
public class MappedByComposite extends AbstractFormPane<INonOwningMapping>
{
	private CCombo combo;

	/**
	 * Creates a new <code>MappedByComposite</code>.
	 *
	 * @param parentPane The parent container of this one
	 * @param parent The parent container
	 */
	public MappedByComposite(AbstractFormPane<? extends INonOwningMapping> parentPane,
	                         Composite parent) {

		super(parentPane, parent);
	}

	/**
	 * Creates a new <code>MappedByComposite</code>.
	 *
	 * @param subjectHolder The holder of the subject <code>INonOwningMapping</code>
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create various common widgets
	 */
	public MappedByComposite(PropertyValueModel<? extends INonOwningMapping> subjectHolder,
	                         Composite parent,
	                         IWidgetFactory widgetFactory) {

		super(subjectHolder, parent, widgetFactory);
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void addPropertyNames(Collection<String> propertyNames) {
		super.addPropertyNames(propertyNames);
		propertyNames.add(INonOwningMapping.MAPPED_BY_PROPERTY);
	}

	private ModifyListener buildComboModifyListener() {
		return new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!isPopulating()) {
					CCombo combo = (CCombo) e.widget;
					valueChanged(combo.getText());
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

		combo.removeAll();
		populateCombo();
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void initializeLayout(Composite container) {

		combo = buildEditableCCombo(container);
		combo.addModifyListener(buildComboModifyListener());

		buildLabeledComposite(
			container,
			JptUiMappingsMessages.NonOwningMapping_mappedByLabel,
			combo.getParent(),
			IJpaHelpContextIds.MAPPING_MAPPED_BY
		);
	}

	private void populateCombo() {

		INonOwningMapping subject = subject();

		if (subject != null) {
			Iterator<String> iter = subject.candidateMappedByAttributeNames();

			for (iter = CollectionTools.sort(iter); iter.hasNext(); ) {
				combo.add(iter.next());
			}
		}

		updateSelectedItem();
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void propertyChanged(String propertyName) {
		super.propertyChanged(propertyName);

		if (propertyName == INonOwningMapping.MAPPED_BY_PROPERTY) {
			populateCombo();
		}
	}

	/**
	 * Updates the selected item by selected the current value, if not
	 * <code>null</code>, or select the default value if one is available,
	 * otherwise remove the selection.
	 * <p>
	 * <b>Note:</b> It seems the text can be shown as truncated, changing the
	 * selection to (0, 0) makes the entire text visible.
	 */
	private void updateSelectedItem() {

		INonOwningMapping subject = subject();
		String value = (subject != null) ? subject.getMappedBy() : null;

		if (value != null) {
			combo.setText(value);
			combo.setSelection(new Point(0, 0));
		}
		else {
			combo.select(-1);
		}
	}

	private void valueChanged(String value) {

		INonOwningMapping subject = subject();
		String oldValue = (subject != null) ? subject.getMappedBy() : null;

		// Check for null value
		if (StringTools.stringIsEmpty(value)) {
			value = null;

			if (StringTools.stringIsEmpty(oldValue)) {
				return;
			}
		}

		// The default value
		if (value != null &&
		    combo.getItemCount() > 0 &&
		    value.equals(combo.getItem(0)))
		{
			value = null;
		}

		// Nothing to change
		if ((oldValue == value) && value == null) {
			return;
		}

		// Set the new value
		if ((value != null) && (oldValue == null) ||
		   ((oldValue != null) && !oldValue.equals(value))) {

			setPopulating(true);

			try {
				subject.setMappedBy(value);
			}
			finally {
				setPopulating(false);
			}
		}
	}
}