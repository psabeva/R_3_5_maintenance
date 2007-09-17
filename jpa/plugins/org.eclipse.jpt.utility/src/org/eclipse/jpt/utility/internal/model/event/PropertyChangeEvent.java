/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.internal.model.event;

/**
 * A "property change" event gets delivered whenever a model changes a "bound"
 * or "constrained" property. A PropertyChangeEvent is sent as an
 * argument to the PropertyChangeListener.
 */
public class PropertyChangeEvent extends ChangeEvent {

	/** Name of the property that changed. */
	private final String propertyName;

	/** The property's old value, before the change. */
	private final Object oldValue;

	/** The property's new value, after the change. */
	private final Object newValue;

	private static final long serialVersionUID = 1L;


	/**
	 * Construct a new property change event.
	 *
	 * @param source The object on which the event initially occurred.
	 * @param propertyName The programmatic name of the property that was changed.
	 * @param oldValue The old value of the property.
	 * @param newValue The new value of the property.
	 */
	public PropertyChangeEvent(Object source, String propertyName, Object oldValue, Object newValue) {
		super(source);
		if (propertyName == null) {
			throw new NullPointerException();
		}
		this.propertyName = propertyName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Return the programmatic name of the property that was changed.
	 */
	public String propertyName() {
		return this.propertyName;
	}

	/**
	 * Return the old value of the property.
	 */
	public Object oldValue() {
		return this.oldValue;
	}

	/**
	 * Return the new value of the property.
	 */
	public Object newValue() {
		return this.newValue;
	}

	@Override
	public String aspectName() {
		return this.propertyName;
	}

	@Override
	public PropertyChangeEvent cloneWithSource(Object newSource) {
		return new PropertyChangeEvent(newSource, this.propertyName, this.oldValue, this.newValue);
	}

	/**
	 * Return a copy of the event with the specified source
	 * replacing the current source and the property name.
	 */
	public PropertyChangeEvent cloneWithSource(Object newSource, String newPropertyName) {
		return new PropertyChangeEvent(newSource, newPropertyName, this.oldValue, this.newValue);
	}

}
