/*******************************************************************************
 * Copyright (c) 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.core.context.JpaContextNode;
import org.eclipse.jpt.core.context.NamedColumn;
import org.eclipse.jpt.core.context.ReadOnlyNamedColumn;
import org.eclipse.jpt.core.context.VirtualNamedColumn;

/**
 * <code>orm.xml</code> virtual<ul>
 * <li>column
 * <li>join column
 * </ul>
 * <strong>NB:</strong> all state is sync'ed/updated in {@link #update()}
 * because <em>all</em> of its derived from the context model (i.e. none of it
 * is derived from the resource model).
 */
public abstract class AbstractOrmVirtualNamedColumn<O extends ReadOnlyNamedColumn.Owner, C extends NamedColumn>
	extends AbstractOrmXmlContextNode
	implements VirtualNamedColumn
{
	protected final O owner;

	protected String specifiedName;
	protected String defaultName;

	protected String columnDefinition;


	protected AbstractOrmVirtualNamedColumn(JpaContextNode parent, O owner) {
		super(parent);
		this.owner = owner;
	}


	// ********** synchronize/update **********

	@Override
	public void update() {
		super.update();

		this.setSpecifiedName(this.buildSpecifiedName());
		this.setDefaultName(this.buildDefaultName());

		this.setColumnDefinition(this.buildColumnDefinition());
	}


	// ********** column **********

	/**
	 * This should never return <code>null</code>.
	 */
	public abstract C getOverriddenColumn();


	// ********** name **********

	public String getName() {
		return (this.specifiedName != null) ? this.specifiedName : this.defaultName;
	}

	public String getSpecifiedName() {
		return this.specifiedName;
	}

	protected void setSpecifiedName(String name) {
		String old = this.specifiedName;
		this.specifiedName = name;
		this.firePropertyChanged(SPECIFIED_NAME_PROPERTY, old, name);
	}

	protected String buildSpecifiedName() {
		return this.getOverriddenColumn().getSpecifiedName();
	}

	public String getDefaultName() {
		return this.defaultName;
	}

	protected void setDefaultName(String name) {
		String old = this.defaultName;
		this.defaultName = name;
		this.firePropertyChanged(DEFAULT_NAME_PROPERTY, old, name);
	}

	protected String buildDefaultName() {
		return this.owner.getDefaultColumnName();
	}


	// ********** column definition **********

	public String getColumnDefinition() {
		return this.columnDefinition;
	}

	protected void setColumnDefinition(String columnDefinition) {
		String old = this.columnDefinition;
		this.columnDefinition = columnDefinition;
		this.firePropertyChanged(COLUMN_DEFINITION_PROPERTY, old, columnDefinition);
	}

	protected String buildColumnDefinition() {
		return this.getOverriddenColumn().getColumnDefinition();
	}


	// ********** validation **********

	public TextRange getValidationTextRange() {
		return null;  // not sure this column is validated...
	}


	// ********** misc **********

	public boolean isVirtual() {
		return true;
	}

	@Override
	public void toString(StringBuilder sb) {
		String table = this.getTable();
		if (table != null) {
			sb.append(table);
			sb.append('.');
		}
		sb.append(this.getName());
	}
}