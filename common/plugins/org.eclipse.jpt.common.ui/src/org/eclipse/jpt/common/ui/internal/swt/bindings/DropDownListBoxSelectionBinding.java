/*******************************************************************************
 * Copyright (c) 2009, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.ui.internal.swt.bindings;

import java.util.ArrayList;
import org.eclipse.jpt.common.ui.internal.listeners.SWTListenerWrapperTools;
import org.eclipse.jpt.common.ui.internal.swt.events.SelectionAdapter;
import org.eclipse.jpt.common.utility.internal.ObjectTools;
import org.eclipse.jpt.common.utility.model.event.PropertyChangeEvent;
import org.eclipse.jpt.common.utility.model.listener.PropertyChangeAdapter;
import org.eclipse.jpt.common.utility.model.listener.PropertyChangeListener;
import org.eclipse.jpt.common.utility.model.value.ModifiablePropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;

/**
 * This binding can be used to keep a drop-down list box's selection
 * synchronized with a model.
 * <p>
 * <strong>NB:</strong> This binding is bi-directional.
 * <p>
 * <strong>NB2:</strong> A selected item value of <code>null</code> can be used
 * to clear the drop-down list box's selection. If <code>null</code> is a
 * valid item in the model list, an invalid selected item can be used to clear
 * the selection.
 * <p>
 * <strong>NB3:</strong> See the class comment for
 * {@link ListBoxSelectionBinding}.
 * 
 * @see ModifiablePropertyValueModel
 * @see DropDownListBox
 * @see SWTBindingTools
 */
final class DropDownListBoxSelectionBinding<E>
	implements ListWidgetModelBinding.SelectionBinding
{
	// ***** model
	/**
	 * The underlying list model.
	 */
	private final ArrayList<E> list;

	/**
	 * A modifiable value model on the underlying model selection.
	 */
	private final ModifiablePropertyValueModel<E> selectedItemModel;

	/**
	 * Cache of model selection.
	 */
	private E selectedItem;

	/**
	 * A listener that allows us to synchronize the drop-down list box's
	 * selection with the model selection.
	 */
	private final PropertyChangeListener selectedItemListener;

	// ***** UI
	/**
	 * The drop-down list box whose selection we keep synchronized
	 * with the model selection.
	 */
	private final DropDownListBox dropdownListBox;

	/**
	 * A listener that allows us to synchronize our selected item model
	 * with the drop-down list box's selection.
	 */
	private final SelectionListener dropdownListBoxSelectionListener;


	/**
	 * Constructor - all parameters are required.
	 */
	DropDownListBoxSelectionBinding(
			ArrayList<E> list,
			ModifiablePropertyValueModel<E> selectedItemModel,
			DropDownListBox dropdownListBox
	) {
		super();
		if ((list == null) || (selectedItemModel == null) || (dropdownListBox == null)) {
			throw new NullPointerException();
		}
		this.list = list;
		this.selectedItemModel = selectedItemModel;
		this.dropdownListBox = dropdownListBox;

		this.selectedItemListener = this.buildSelectedItemListener();
		this.selectedItemModel.addPropertyChangeListener(PropertyValueModel.VALUE, this.selectedItemListener);

		this.dropdownListBoxSelectionListener = this.buildDropDownListBoxSelectionListener();
		this.dropdownListBox.addSelectionListener(this.dropdownListBoxSelectionListener);

		this.selectedItem = this.selectedItemModel.getValue();
	}


	// ********** initialization **********

	private PropertyChangeListener buildSelectedItemListener() {
		return SWTListenerWrapperTools.wrap(new SelectedItemListener(), this.dropdownListBox.getDisplay());
	}

	/* CU private */ class SelectedItemListener
		extends PropertyChangeAdapter
	{
		@Override
		public void propertyChanged(PropertyChangeEvent event) {
			DropDownListBoxSelectionBinding.this.selectedItemChanged(event);
		}
	}

	private SelectionListener buildDropDownListBoxSelectionListener() {
		return new DropDownListBoxSelectionListener();
	}

	/* CU private */ class DropDownListBoxSelectionListener
		extends SelectionAdapter
	{
		@Override
		public void widgetSelected(SelectionEvent event) {
			DropDownListBoxSelectionBinding.this.dropDownListBoxSelectionChanged();
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent event) {
			DropDownListBoxSelectionBinding.this.dropDownListBoxDoubleClicked();
		}
	}


	// ********** ListWidgetModelBinding.SelectionBinding implementation **********

	/**
	 * <strong>NB:</strong> The elements in the selection model may be out of
	 * sync with the underlying list model. (See the class comment.)
	 * <p>
	 * Modifying the drop-down list box's selected item programmatically does
	 * not trigger a {@link SelectionEvent}.
	 * <p>
	 * Pre-condition: The drop-down list box is not disposed.
	 */
	public void synchronizeListWidgetSelection() {
		int oldIndex = this.dropdownListBox.getSelectionIndex();
		int newIndex = this.indexOf(this.selectedItem);
		if (newIndex == -1) {
			if (oldIndex != -1) {
				this.dropdownListBox.deselectAll();
			}
		} else {
			if (newIndex != oldIndex) {
				if (oldIndex != -1) {
					this.dropdownListBox.deselect(oldIndex);
				}
				this.dropdownListBox.select(newIndex);
			}
		}
	}

	public void dispose() {
		this.dropdownListBox.removeSelectionListener(this.dropdownListBoxSelectionListener);
		this.selectedItemModel.removePropertyChangeListener(PropertyValueModel.VALUE, this.selectedItemListener);
		this.selectedItem = null;
	}


	// ********** selected item **********

	void selectedItemChanged(PropertyChangeEvent event) {
		if ( ! this.dropdownListBox.isDisposed()) {
			this.selectedItemChanged_(event);
		}
	}

	/**
	 * Modifying the drop-down list box's selected item programmatically does
	 * not trigger a {@link SelectionEvent}.
	 */
	private void selectedItemChanged_(PropertyChangeEvent event) {
		@SuppressWarnings("unchecked")
		E item = (E) event.getNewValue();
		this.selectedItem = item;
		this.synchronizeListWidgetSelection();
	}

	/**
	 * <strong>NB:</strong> an index of <code>-1</code> is ignored by
	 * {@link org.eclipse.swt.widgets.Combo} (lucky for us).
	 */
	private int indexOf(E item) {
		int i = 0;
		for (E each : this.list) {
			if (ObjectTools.equals(each, item)) {
				return i;
			}
			i++;
		}
		return -1;
	}


	// ********** combo-box events **********

	void dropDownListBoxSelectionChanged() {
		this.selectedItemModel.setValue(this.getDropDownListBoxSelectedItem());
	}

	void dropDownListBoxDoubleClicked() {
		this.dropDownListBoxSelectionChanged();
	}

	private E getDropDownListBoxSelectedItem() {
		int index = this.dropdownListBox.getSelectionIndex();
		return (index == -1) ? null : this.list.get(index);
	}


	// ********** misc **********

	@Override
	public String toString() {
		return ObjectTools.toString(this, this.selectedItem);
	}


	// ********** adapter interface **********

	/**
	 * Adapter used by the drop-down list box selection binding to query and manipulate
	 * the drop-down list box.
	 */
	interface DropDownListBox {
		/**
		 * Return the list widget's display.
		 */
		Display getDisplay();

		/**
		 * Return whether the drop-down list box is <em>disposed</em>.
		 */
		boolean isDisposed();

		/**
		 * Add the specified selection listener to the drop-down list box.
		 */
		void addSelectionListener(SelectionListener listener);

		/**
		 * Remove the specified selection listener from the drop-down list box.
		 */
		void removeSelectionListener(SelectionListener listener);

		/**
		 * Return the index of the drop-down list box's selection.
		 */
		int getSelectionIndex();

		/**
		 * Select the item at the specified index in the drop-down list box.
		 */
		void select(int index);

		/**
		 * Deselect the item at the specified index in the drop-down list box.
		 */
		void deselect(int index);

		/**
		 * Clear the drop-down list box's selection.
		 */
		void deselectAll();
	}
}
