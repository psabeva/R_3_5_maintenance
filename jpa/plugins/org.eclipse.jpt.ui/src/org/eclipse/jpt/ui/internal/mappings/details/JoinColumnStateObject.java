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

import org.eclipse.jpt.core.internal.context.base.IAbstractJoinColumn;
import org.eclipse.jpt.core.internal.context.base.IJoinColumn;
import org.eclipse.jpt.db.internal.Schema;
import org.eclipse.jpt.db.internal.Table;

/**
 * @version 2.0
 * @since 2.0
 */
@SuppressWarnings("nls")
public abstract class JoinColumnStateObject extends AbstractJoinColumnStateObject
{
	private Boolean defaultInsertable;
	private boolean defaultTableSelected;
	private Boolean defaultUpdatable;
	private Boolean insertable;
	private String table;
	private Boolean updatable;

	public static final String INSERTABLE_PROPERTY = "insertable";
	public static final String TABLE_PROPERTY = "table";
	public static final String UPDATABLE_PROPERTY = "updatable";

	/**
	 * Creates a new <code>JoinColumnStateObject</code>.
	 */
	public JoinColumnStateObject() {
		super();
	}

	/**
	 * Creates a new <code>JoinColumnStateObject</code>.
	 *
	 * @param joinColumn Either the join column to edit or <code>null</code> if
	 * this state object is used to create a new one
	 */
	public JoinColumnStateObject(IJoinColumn joinColumn) {
		super(joinColumn);
	}

	public abstract String defaultTableName();

	public Boolean getDefaultInsertable() {
		return defaultInsertable;
	}

	public Boolean getDefaultUpdatable() {
		return defaultUpdatable;
	}

	public Boolean getInsertable() {
		return insertable;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public IJoinColumn getJoinColumn() {
		return (IJoinColumn) super.getJoinColumn();
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public Table getNameTable() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public Table getReferencedNameTable() {
		return null;
	}

	public abstract Schema getSchema();

	public String getTable() {
		return table;
	}

	public Boolean getUpdatable() {
		return updatable;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void initialize(IAbstractJoinColumn abstractJoinColumn) {
		super.initialize(abstractJoinColumn);

		if (abstractJoinColumn != null) {
			IJoinColumn joinColumn = (IJoinColumn) abstractJoinColumn;

			defaultTableSelected = joinColumn.getSpecifiedTable() == null;
			table                = joinColumn.getTable();
			insertable           = joinColumn.getInsertable();
			updatable            = joinColumn.getUpdatable();
			defaultInsertable    = joinColumn.getDefaultInsertable();
			defaultUpdatable     = joinColumn.getDefaultInsertable();
		}
		else {
			defaultInsertable    = Boolean.TRUE;
			defaultUpdatable     = Boolean.TRUE;
		}
	}

	public boolean isDefaultTableSelected() {
		return defaultTableSelected;
	}

	public void setDefaultTableSelected(boolean defaultTableSelected) {
		this.defaultTableSelected = defaultTableSelected;
	}

	public void setInsertable(Boolean insertable) {
		Boolean oldInsertable = this.insertable;
		this.insertable = insertable;
		firePropertyChanged(INSERTABLE_PROPERTY, oldInsertable, insertable);
	}

	public void setTable(String table) {
		String oldTable = this.table;
		this.table = table;
		firePropertyChanged(TABLE_PROPERTY, oldTable, table);
	}

	public void setUpdatable(Boolean updatable) {
		Boolean oldUpdatable = this.updatable;
		this.updatable = updatable;
		firePropertyChanged(UPDATABLE_PROPERTY, oldUpdatable, updatable);
	}

	public String specifiedTableName() {

		if (getJoinColumn() != null) {
			return getJoinColumn().getSpecifiedTable();
		}

		return null;
	}

	public String tableName() {
		return (specifiedTableName() == null) ? defaultTableName() : specifiedTableName();
	}
}