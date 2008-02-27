/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import org.eclipse.jpt.core.context.JoinColumn;
import org.eclipse.jpt.core.context.JpaContextNode;
import org.eclipse.jpt.core.context.orm.OrmJoinColumn;
import org.eclipse.jpt.core.resource.orm.XmlJoinColumn;
import org.eclipse.jpt.db.internal.Column;
import org.eclipse.jpt.db.internal.Table;

public class GenericOrmJoinColumn extends AbstractOrmColumn<XmlJoinColumn> implements OrmJoinColumn
{

	protected String specifiedReferencedColumnName;

	protected String defaultReferencedColumnName;

	protected XmlJoinColumn joinColumn;

	public GenericOrmJoinColumn(JpaContextNode parent, JoinColumn.Owner owner) {
		super(parent, owner);
	}

	public void initializeFrom(JoinColumn oldColumn) {
		super.initializeFrom(oldColumn);
		setSpecifiedReferencedColumnName(oldColumn.getSpecifiedReferencedColumnName());
	}
	
	public String getReferencedColumnName() {
		return (this.getSpecifiedReferencedColumnName() == null) ? getDefaultReferencedColumnName() : this.getSpecifiedReferencedColumnName();
	}

	public String getSpecifiedReferencedColumnName() {
		return this.specifiedReferencedColumnName;
	}

	public void setSpecifiedReferencedColumnName(String newSpecifiedReferencedColumnName) {
		String oldSpecifiedReferencedColumnName = this.specifiedReferencedColumnName;
		this.specifiedReferencedColumnName = newSpecifiedReferencedColumnName;
		columnResource().setReferencedColumnName(newSpecifiedReferencedColumnName);
		firePropertyChanged(SPECIFIED_REFERENCED_COLUMN_NAME_PROPERTY, oldSpecifiedReferencedColumnName, newSpecifiedReferencedColumnName);
	}
	
	public void setSpecifiedReferencedColumnName_(String newSpecifiedReferencedColumnName) {
		String oldSpecifiedReferencedColumnName = this.specifiedReferencedColumnName;
		this.specifiedReferencedColumnName = newSpecifiedReferencedColumnName;
		firePropertyChanged(SPECIFIED_REFERENCED_COLUMN_NAME_PROPERTY, oldSpecifiedReferencedColumnName, newSpecifiedReferencedColumnName);
	}

	public String getDefaultReferencedColumnName() {
		return this.defaultReferencedColumnName;
	}

	protected void setDefaultReferencedColumnName(String newDefaultReferencedColumnName) {
		String oldDefaultReferencedColumnName = this.defaultReferencedColumnName;
		this.defaultReferencedColumnName = newDefaultReferencedColumnName;
		firePropertyChanged(DEFAULT_REFERENCED_COLUMN_NAME_PROPERTY, oldDefaultReferencedColumnName, newDefaultReferencedColumnName);
	}

	public boolean isVirtual() {
		return owner().isVirtual(this);
	}
	
	@Override
	public JoinColumn.Owner owner() {
		return (JoinColumn.Owner) this.owner;
	}

	public Table dbReferencedColumnTable() {
		return owner().dbReferencedColumnTable();
	}

	public Column dbReferencedColumn() {
		Table table = dbReferencedColumnTable();
		return (table == null) ? null : table.columnNamed(getReferencedColumnName());
	}

	public boolean isReferencedColumnResolved() {
		return dbReferencedColumn() != null;
	}
//
//	public ITextRange referencedColumnNameTextRange() {
//		if (node == null) {
//			return owner.validationTextRange();
//		}
//		IDOMNode referencedColumnNameNode = (IDOMNode) DOMUtilities.getChildAttributeNode(node, OrmXmlMapper.REFERENCED_COLUMN_NAME);
//		return (referencedColumnNameNode == null) ? validationTextRange() : buildTextRange(referencedColumnNameNode);
//	}
//
//	public void refreshDefaults(DefaultsContext defaultsContext) {
//		setDefaultReferencedColumnName((String) defaultsContext.getDefault(GenericJpaPlatform.DEFAULT_JOIN_COLUMN_REFERENCED_COLUMN_NAME_KEY));
//		setDefaultName((String) defaultsContext.getDefault(GenericJpaPlatform.DEFAULT_JOIN_COLUMN_NAME_KEY));
//		setDefaultTable((String) defaultsContext.getDefault(GenericJpaPlatform.DEFAULT_JOIN_COLUMN_TABLE_KEY));
//	}

	@Override
	protected XmlJoinColumn columnResource() {
		return this.joinColumn;
	}

	@Override
	protected void addColumnResource() {
		//joinColumns are part of a collection, the join-column element will be removed/added
		//when the XmlJoinColumn is removed/added to the XmlEntity collection
	}
	
	@Override
	protected void removeColumnResource() {
		//joinColumns are part of a collection, the pk-join-column element will be removed/added
		//when the XmlJoinColumn is removed/added to the XmlEntity collection
	}
	
	
	@Override
	public void initialize(XmlJoinColumn column) {
		this.joinColumn = column;
		super.initialize(column);
		this.specifiedReferencedColumnName = column.getReferencedColumnName();
		this.defaultReferencedColumnName = defaultReferencedColumnName();
	}
	
	@Override
	public void update(XmlJoinColumn column) {
		this.joinColumn = column;
		super.update(column);
		this.setSpecifiedReferencedColumnName_(column.getReferencedColumnName());
		this.setDefaultReferencedColumnName(defaultReferencedColumnName());
	}
	
	protected String defaultReferencedColumnName() {
//		if (!owner().relationshipMapping().isRelationshipOwner()) {
//			return null;
//		}
		//TODO
		return null;
	}
	
	@Override
	protected String defaultName() {
//		if (!owner().relationshipMapping().isRelationshipOwner()) {
//			return null;
//		}
		//TODO
		return super.defaultName();
	}
	
	@Override
	protected String defaultTable() {
//		if (!owner().relationshipMapping().isRelationshipOwner()) {
//			return null;
//		}
		//TODO
		return super.defaultTable();
	}

}
