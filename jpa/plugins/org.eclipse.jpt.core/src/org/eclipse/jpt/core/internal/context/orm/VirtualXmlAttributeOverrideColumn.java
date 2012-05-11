/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import org.eclipse.jpt.core.context.Column;

/**
 * javax.persistence.Column
 */
public class VirtualXmlAttributeOverrideColumn
	extends VirtualXmlColumn
{

	public VirtualXmlAttributeOverrideColumn(Column column) {
		super(null, column);
	}

	@Override
	public String getName() {
		return this.column.getSpecifiedName();
	}

	@Override
	public String getColumnDefinition() {
		return this.column.getColumnDefinition();
	}

	@Override
	public String getTable() {
		return this.column.getSpecifiedTable();
	}

	@Override
	public Boolean getInsertable() {
		return this.column.getSpecifiedInsertable();
	}

	@Override
	public Boolean getUpdatable() {
		return this.column.getSpecifiedUpdatable();
	}

	@Override
	public Boolean getNullable() {
		return this.column.getSpecifiedNullable();
	}

	@Override
	public Boolean getUnique() {
		return this.column.getSpecifiedUnique();
	}

	@Override
	public Integer getLength() {
		return this.column.getSpecifiedLength();
	}

	@Override
	public Integer getScale() {
		return this.column.getSpecifiedScale();
	}

	@Override
	public Integer getPrecision() {
		return this.column.getSpecifiedPrecision();
	}

}
