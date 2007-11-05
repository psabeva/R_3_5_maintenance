/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.internal.model.value;

import org.eclipse.jpt.utility.internal.BidiFilter;
import org.eclipse.jpt.utility.internal.model.event.PropertyChangeEvent;

/**
 * A <code>FilteringPropertyValueModel</code> wraps another
 * <code>PropertyValueModel</code> and uses a <code>BidiFilter</code>
 * to determine when the wrapped value is to be returned by calls
 * to <code>value()</code> and modified by calls to
 * <code>setValue(Object)</code>
 * <p>
 * As an alternative to building a <code>BidiFilter</code>, a subclass
 * of <code>FilteringPropertyValueModel</code> can override the
 * <code>accept(Object)</code> and <code>reverseAccept(Object)</code>
 * methods.
 * <p>
 * One, possibly undesirable, side-effect of using this value model is that
 * it must return *something* as the value. The default behavior is
 * to return <code>null</code> whenever the wrapped value is not "accepted",
 * which can be configured and/or overridden.
 * <p>
 * Likewise, if an incoming value is not "reverseAccepted", *nothing* will passed
 * through to the wrapped value holder, not even <code>null</code>.
 */
public class FilteringPropertyValueModel extends PropertyValueModelWrapper {
	private BidiFilter filter;
	private Object defaultValue;


	// ********** constructors **********

	/**
	 * Construct a property value model with the specified nested
	 * property value model and a disabled filter.
	 * Use this constructor if you want to override the
	 * <code>accept(Object)</code> and <code>reverseAccept(Object)</code>
	 * methods instead of building a <code>BidiFilter</code>.
	 * The default value will be <code>null</code>.
	 */
	public FilteringPropertyValueModel(PropertyValueModel valueHolder) {
		this(valueHolder, BidiFilter.Disabled.instance(), null);
	}

	/**
	 * Construct a property value model with the specified nested
	 * property value model, specified default value, and a disabled filter.
	 * Use this constructor if you want to override the
	 * <code>accept(Object)</code> and <code>reverseAccept(Object)</code>
	 * methods instead of building a <code>BidiFilter</code>
	 * <em>and</em> you need to specify
	 * a default value other than <code>null</code>.
	 */
	public FilteringPropertyValueModel(PropertyValueModel valueHolder, Object defaultValue) {
		this(valueHolder, BidiFilter.Disabled.instance(), defaultValue);
	}

	/**
	 * Construct an property value model with the specified nested
	 * property value model and filter.
	 * The default value will be <code>null</code>.
	 */
	public FilteringPropertyValueModel(PropertyValueModel valueHolder, BidiFilter filter) {
		this(valueHolder, filter, null);
	}

	/**
	 * Construct an property value model with the specified nested
	 * property value model, filter, and default value.
	 */
	public FilteringPropertyValueModel(PropertyValueModel valueHolder, BidiFilter filter, Object defaultValue) {
		super(valueHolder);
		this.filter = filter;
		this.defaultValue = defaultValue;
	}


	// ********** ValueModel implementation **********

	public Object value() {
		return this.filterValue(this.valueHolder.value());
	}


	// ********** PropertyValueModel implementation **********

	public void setValue(Object value) {
		if (this.reverseAccept(value)) {
			this.valueHolder.setValue(value);
		}
	}


	// ********** PropertyValueModelWrapper implementation **********

	@Override
	protected void valueChanged(PropertyChangeEvent e) {
		// filter the values before propagating the change event
		Object oldValue = this.filterValue(e.oldValue());
		Object newValue = this.filterValue(e.newValue());
		this.firePropertyChanged(VALUE, oldValue, newValue);
	}


	// ********** queries **********

	/**
	 * If the specified value is "accepted" simply return it,
	 * otherwise return the default value.
	 */
	protected Object filterValue(Object value) {
		return this.accept(value) ? value : this.defaultValue();
	}

	/**
	 * Return whether the <code>FilteringPropertyValueModel</code> should
	 * return the specified value from a call to the
	 * <code>value()</code> method; the value came
	 * from the nested property value model
	 * <p>
	 * This method can be overridden by a subclass as an
	 * alternative to building a <code>BidiFilter</code>.
	 */
	protected boolean accept(Object value) {
		return this.filter.accept(value);
	}

	/**
	 * Return whether the <code>FilteringPropertyValueModel</code>
	 * should pass through the specified value to the nested
	 * property value model in a call to the
	 * <code>setValue(Object)</code> method
	 * <p>
	 * This method can be overridden by a subclass as an
	 * alternative to building a <code>BidiFilter</code>.
	 */
	protected boolean reverseAccept(Object value) {
		return this.filter.reverseAccept(value);
	}

	/**
	 * Return the object that should be returned if
	 * the nested value was rejected by the filter.
	 * The default is <code>null</code>.
	 */
	protected Object defaultValue() {
		return this.defaultValue;
	}

}
