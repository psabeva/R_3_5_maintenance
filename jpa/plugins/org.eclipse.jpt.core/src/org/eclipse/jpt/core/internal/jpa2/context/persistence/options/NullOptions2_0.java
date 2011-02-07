/*******************************************************************************
* Copyright (c) 2009, 2010 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.core.internal.jpa2.context.persistence.options;

import java.util.ListIterator;
import java.util.Map;

import org.eclipse.jpt.common.utility.internal.iterators.EmptyListIterator;
import org.eclipse.jpt.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.core.internal.context.persistence.AbstractPersistenceUnitProperties;
import org.eclipse.jpt.core.jpa2.context.persistence.options.JpaOptions2_0;

/**
 * JPA 2.0 options
 */
public class NullOptions2_0 extends AbstractPersistenceUnitProperties
	implements JpaOptions2_0
{
	

	// ********** constructors **********
	public NullOptions2_0(PersistenceUnit parent) {
		super(parent);
	}

	// ********** initialization **********
	/**
	 * Initializes properties with values from the persistence unit.
	 */
	@Override
	protected void initializeProperties() {
		//do nothing
	}

	// ********** behavior **********
	
	public void propertyValueChanged(String propertyName, String newValue) {
		//do nothing
	}

	public void propertyRemoved(String propertyName) {
		//do nothing
	}

	/**
	 * Adds property names key/value pairs, where: 
	 * 		key = PU property key
	 * 		value = property id
	 */
	@Override
	protected void addPropertyNames(Map<String, String> propertyNames) {
		//do nothing
	}

	// ********** LockTimeout **********
	public Integer getLockTimeout() {
		return null;
	}

	public void setLockTimeout(Integer newLockTimeout) {
		throw new UnsupportedOperationException();
	}

	public Integer getDefaultLockTimeout() {
		return DEFAULT_LOCK_TIMEOUT;
	}

	// ********** QueryTimeout **********
	public Integer getQueryTimeout() {
		return null;
	}

	public void setQueryTimeout(Integer newQueryTimeout) {
		throw new UnsupportedOperationException();
	}

	public Integer getDefaultQueryTimeout() {
		return DEFAULT_QUERY_TIMEOUT;
	}


	// ********** ValidationGroupPrePersists **********
	public ListIterator<String> validationGroupPrePersists() {
		return EmptyListIterator.instance();
	}
	
	public int validationGroupPrePersistsSize() {
		return 0;
	}
	
	public boolean validationGroupPrePersistExists(String validationGroupPrePersistClassName) {
		return false;
	}
	
	public String addValidationGroupPrePersist(String newValidationGroupPrePersistClassName) {
		throw new UnsupportedOperationException();
	}
	
	public void removeValidationGroupPrePersist(String validationGroupPrePersistClassName) {
		throw new UnsupportedOperationException();
	}

	// ********** ValidationGroupPreUpdates **********
	public ListIterator<String> validationGroupPreUpdates() {
		return EmptyListIterator.instance();
	}
	
	public int validationGroupPreUpdatesSize() {
		return 0;
	}
	
	public boolean validationGroupPreUpdateExists(String validationGroupPreUpdateClassName) {
		return false;
	}
	
	public String addValidationGroupPreUpdate(String newValidationGroupPreUpdateClassName) {
		throw new UnsupportedOperationException();
	}
	
	public void removeValidationGroupPreUpdate(String validationGroupPreUpdateClassName) {
		throw new UnsupportedOperationException();
	}

	// ********** ValidationGroupPreRemoves **********
	public ListIterator<String> validationGroupPreRemoves() {
		return EmptyListIterator.instance();
	}
	
	public int validationGroupPreRemovesSize() {
		return 0;
	}
	
	public boolean validationGroupPreRemoveExists(String validationGroupPreRemoveClassName) {
		return false;
	}
	
	public String addValidationGroupPreRemove(String newValidationGroupPreRemoveClassName) {
		throw new UnsupportedOperationException();
	}
	
	public void removeValidationGroupPreRemove(String validationGroupPreRemoveClassName) {
		throw new UnsupportedOperationException();
	}

}