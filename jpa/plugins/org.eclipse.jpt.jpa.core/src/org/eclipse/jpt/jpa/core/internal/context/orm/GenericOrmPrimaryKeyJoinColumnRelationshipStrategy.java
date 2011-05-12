/*******************************************************************************
 * Copyright (c) 2009, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.context.orm;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.utility.internal.iterables.ListIterable;
import org.eclipse.jpt.common.utility.internal.iterables.LiveCloneIterable;
import org.eclipse.jpt.common.utility.internal.iterables.LiveCloneListIterable;
import org.eclipse.jpt.jpa.core.context.BaseJoinColumn;
import org.eclipse.jpt.jpa.core.context.Entity;
import org.eclipse.jpt.jpa.core.context.NamedColumn;
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;
import org.eclipse.jpt.jpa.core.context.PrimaryKeyJoinColumn;
import org.eclipse.jpt.jpa.core.context.ReadOnlyBaseJoinColumn;
import org.eclipse.jpt.jpa.core.context.RelationshipMapping;
import org.eclipse.jpt.jpa.core.context.TypeMapping;
import org.eclipse.jpt.jpa.core.context.orm.OrmJoinColumn;
import org.eclipse.jpt.jpa.core.context.orm.OrmPrimaryKeyJoinColumn;
import org.eclipse.jpt.jpa.core.context.orm.OrmPrimaryKeyJoinColumnRelationship;
import org.eclipse.jpt.jpa.core.context.orm.OrmPrimaryKeyJoinColumnRelationshipStrategy;
import org.eclipse.jpt.jpa.core.internal.context.BaseJoinColumnTextRangeResolver;
import org.eclipse.jpt.jpa.core.internal.context.ContextContainerTools;
import org.eclipse.jpt.jpa.core.internal.context.JptValidator;
import org.eclipse.jpt.jpa.core.internal.context.NamedColumnTextRangeResolver;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.OneToOnePrimaryKeyJoinColumnValidator;
import org.eclipse.jpt.jpa.core.internal.validation.JpaValidationDescriptionMessages;
import org.eclipse.jpt.jpa.core.resource.orm.OrmFactory;
import org.eclipse.jpt.jpa.core.resource.orm.XmlPrimaryKeyJoinColumn;
import org.eclipse.jpt.jpa.core.resource.orm.XmlPrimaryKeyJoinColumnContainer;
import org.eclipse.jpt.jpa.db.Table;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public class GenericOrmPrimaryKeyJoinColumnRelationshipStrategy
	extends AbstractOrmXmlContextNode
	implements OrmPrimaryKeyJoinColumnRelationshipStrategy
{
	protected final Vector<OrmPrimaryKeyJoinColumn> primaryKeyJoinColumns = new Vector<OrmPrimaryKeyJoinColumn>();
	protected final PrimaryKeyJoinColumnContainerAdapter primaryKeyJoinColumnContainerAdapter;
	protected final OrmJoinColumn.Owner primaryKeyJoinColumnOwner;


	public GenericOrmPrimaryKeyJoinColumnRelationshipStrategy(OrmPrimaryKeyJoinColumnRelationship parent) {
		super(parent);
		this.primaryKeyJoinColumnContainerAdapter = this.buildPrimaryKeyJoinColumnContainerAdapter();
		this.primaryKeyJoinColumnOwner = this.buildPrimaryKeyJoinColumnOwner();
		this.initializePrimaryKeyJoinColumns();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.syncPrimaryKeyJoinColumns();
	}

	@Override
	public void update() {
		super.update();
		this.updateNodes(this.getPrimaryKeyJoinColumns());
	}


	// ********** primary key join columns **********

	public ListIterator<OrmPrimaryKeyJoinColumn> primaryKeyJoinColumns() {
		return this.getPrimaryKeyJoinColumns().iterator();
	}

	protected ListIterable<OrmPrimaryKeyJoinColumn> getPrimaryKeyJoinColumns() {
		return new LiveCloneListIterable<OrmPrimaryKeyJoinColumn>(this.primaryKeyJoinColumns);
	}

	public int primaryKeyJoinColumnsSize() {
		return this.primaryKeyJoinColumns.size();
	}

	public boolean hasPrimaryKeyJoinColumns() {
		return this.primaryKeyJoinColumns.size() != 0;
	}

	public OrmPrimaryKeyJoinColumn getPrimaryKeyJoinColumn(int index) {
		return this.primaryKeyJoinColumns.get(index);
	}

	public OrmPrimaryKeyJoinColumn addPrimaryKeyJoinColumn() {
		return this.addPrimaryKeyJoinColumn(this.primaryKeyJoinColumns.size());
	}

	public OrmPrimaryKeyJoinColumn addPrimaryKeyJoinColumn(int index) {
		XmlPrimaryKeyJoinColumn xmlJoinColumn = this.buildXmlPrimaryKeyJoinColumn();
		OrmPrimaryKeyJoinColumn joinColumn = this.addPrimaryKeyJoinColumn_(index, xmlJoinColumn);
		this.getXmlPrimaryKeyJoinColumnContainer().getPrimaryKeyJoinColumns().add(index, xmlJoinColumn);
		return joinColumn;
	}

	protected XmlPrimaryKeyJoinColumn buildXmlPrimaryKeyJoinColumn() {
		return OrmFactory.eINSTANCE.createXmlPrimaryKeyJoinColumn();
	}

	public void removePrimaryKeyJoinColumn(PrimaryKeyJoinColumn joinColumn) {
		this.removePrimaryKeyJoinColumn(this.primaryKeyJoinColumns.indexOf(joinColumn));
	}

	public void removePrimaryKeyJoinColumn(int index) {
		this.removePrimaryKeyJoinColumn_(index);
		this.getXmlPrimaryKeyJoinColumnContainer().getPrimaryKeyJoinColumns().remove(index);
	}

	protected void removePrimaryKeyJoinColumn_(int index) {
		this.removeItemFromList(index, this.primaryKeyJoinColumns, PRIMARY_KEY_JOIN_COLUMNS_LIST);
	}

	public void movePrimaryKeyJoinColumn(int targetIndex, int sourceIndex) {
		this.moveItemInList(targetIndex, sourceIndex, this.primaryKeyJoinColumns, PRIMARY_KEY_JOIN_COLUMNS_LIST);
		this.getXmlPrimaryKeyJoinColumnContainer().getPrimaryKeyJoinColumns().move(targetIndex, sourceIndex);
	}

	protected void initializePrimaryKeyJoinColumns() {
		for (XmlPrimaryKeyJoinColumn xmlJoinColumn : this.getXmlPrimaryKeyJoinColumns()) {
			this.primaryKeyJoinColumns.add(this.buildPrimaryKeyJoinColumn(xmlJoinColumn));
		}
	}

	protected void syncPrimaryKeyJoinColumns() {
		ContextContainerTools.synchronizeWithResourceModel(this.primaryKeyJoinColumnContainerAdapter);
	}

	protected Iterable<XmlPrimaryKeyJoinColumn> getXmlPrimaryKeyJoinColumns() {
		// clone to reduce chance of concurrency problems
		return new LiveCloneIterable<XmlPrimaryKeyJoinColumn>(this.getXmlPrimaryKeyJoinColumnContainer().getPrimaryKeyJoinColumns());
	}

	protected void movePrimaryKeyJoinColumn_(int index, OrmPrimaryKeyJoinColumn joinColumn) {
		this.moveItemInList(index, joinColumn, this.primaryKeyJoinColumns, PRIMARY_KEY_JOIN_COLUMNS_LIST);
	}

	protected OrmPrimaryKeyJoinColumn addPrimaryKeyJoinColumn_(int index, XmlPrimaryKeyJoinColumn xmlJoinColumn) {
		OrmPrimaryKeyJoinColumn joinColumn = this.buildPrimaryKeyJoinColumn(xmlJoinColumn);
		this.addItemToList(index, joinColumn, this.primaryKeyJoinColumns, PRIMARY_KEY_JOIN_COLUMNS_LIST);
		return joinColumn;
	}

	protected void removePrimaryKeyJoinColumn_(OrmPrimaryKeyJoinColumn joinColumn) {
		this.removePrimaryKeyJoinColumn_(this.primaryKeyJoinColumns.indexOf(joinColumn));
	}

	protected PrimaryKeyJoinColumnContainerAdapter buildPrimaryKeyJoinColumnContainerAdapter() {
		return new PrimaryKeyJoinColumnContainerAdapter();
	}

	/**
	 * primary key join column container adapter
	 */
	protected class PrimaryKeyJoinColumnContainerAdapter
		implements ContextContainerTools.Adapter<OrmPrimaryKeyJoinColumn, XmlPrimaryKeyJoinColumn>
	{
		public Iterable<OrmPrimaryKeyJoinColumn> getContextElements() {
			return GenericOrmPrimaryKeyJoinColumnRelationshipStrategy.this.getPrimaryKeyJoinColumns();
		}
		public Iterable<XmlPrimaryKeyJoinColumn> getResourceElements() {
			return GenericOrmPrimaryKeyJoinColumnRelationshipStrategy.this.getXmlPrimaryKeyJoinColumns();
		}
		public XmlPrimaryKeyJoinColumn getResourceElement(OrmPrimaryKeyJoinColumn contextElement) {
			return contextElement.getXmlColumn();
		}
		public void moveContextElement(int index, OrmPrimaryKeyJoinColumn element) {
			GenericOrmPrimaryKeyJoinColumnRelationshipStrategy.this.movePrimaryKeyJoinColumn_(index, element);
		}
		public void addContextElement(int index, XmlPrimaryKeyJoinColumn resourceElement) {
			GenericOrmPrimaryKeyJoinColumnRelationshipStrategy.this.addPrimaryKeyJoinColumn_(index, resourceElement);
		}
		public void removeContextElement(OrmPrimaryKeyJoinColumn element) {
			GenericOrmPrimaryKeyJoinColumnRelationshipStrategy.this.removePrimaryKeyJoinColumn_(element);
		}
	}

	protected OrmJoinColumn.Owner buildPrimaryKeyJoinColumnOwner() {
		return new PrimaryKeyJoinColumnOwner();
	}

	protected OrmPrimaryKeyJoinColumn buildPrimaryKeyJoinColumn(XmlPrimaryKeyJoinColumn xmlJoinColumn) {
		return this.getContextNodeFactory().buildOrmPrimaryKeyJoinColumn(this, this.primaryKeyJoinColumnOwner, xmlJoinColumn);
	}


	// ********** misc **********

	@Override
	public OrmPrimaryKeyJoinColumnRelationship getParent() {
		return (OrmPrimaryKeyJoinColumnRelationship) super.getParent();
	}

	public OrmPrimaryKeyJoinColumnRelationship getRelationship() {
		return this.getParent();
	}

	protected RelationshipMapping getRelationshipMapping() {
		return this.getRelationship().getMapping();
	}

	protected XmlPrimaryKeyJoinColumnContainer getXmlPrimaryKeyJoinColumnContainer() {
		return this.getRelationship().getXmlContainer();
	}

	public String getTableName() {
		return this.getTypeMapping().getPrimaryTableName();
	}

	public Table resolveDbTable(String tableName) {
		return this.getTypeMapping().resolveDbTable(tableName);
	}

	public boolean tableNameIsInvalid(String tableName) {
		return this.getTypeMapping().tableNameIsInvalid(tableName);
	}

	public String getColumnTableNotValidDescription() {
		return JpaValidationDescriptionMessages.NOT_VALID_FOR_THIS_ENTITY;
	}

	protected TypeMapping getTypeMapping() {
		return this.getRelationshipMapping().getTypeMapping();
	}

	public void addStrategy() {
		if (this.primaryKeyJoinColumns.size() == 0) {
			this.addPrimaryKeyJoinColumn();
		}
	}

	public void removeStrategy() {
		for (int i = this.primaryKeyJoinColumns.size(); i-- > 0; ) {
			this.removePrimaryKeyJoinColumn(i);
		}
	}

	public boolean isOverridable() {
		return false;
	}


	// ********** validation **********

	public TextRange getValidationTextRange() {
		return this.getRelationship().getValidationTextRange();
	}

	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		for (OrmPrimaryKeyJoinColumn pkJoinColumn : this.getPrimaryKeyJoinColumns()) {
			pkJoinColumn.validate(messages, reporter);
		}
	}


	// ********** join column owner **********

	protected class PrimaryKeyJoinColumnOwner
		implements OrmJoinColumn.Owner
	{
		protected PrimaryKeyJoinColumnOwner() {
			super();
		}

		/**
		 * by default, the join column is in the type mapping's primary table
		 */
		public String getDefaultTableName() {
			return GenericOrmPrimaryKeyJoinColumnRelationshipStrategy.this.getTableName();
		}

		public Entity getRelationshipTarget() {
			return this.getRelationshipMapping().getResolvedTargetEntity();
		}

		public String getAttributeName() {
			return this.getRelationshipMapping().getName();
		}

		protected PersistentAttribute getPersistentAttribute() {
			return this.getRelationshipMapping().getPersistentAttribute();
		}

		public boolean tableNameIsInvalid(String tableName) {
			return this.getTypeMapping().tableNameIsInvalid(tableName);
		}

		public Iterator<String> candidateTableNames() {
			return this.getTypeMapping().allAssociatedTableNames();
		}

		public TypeMapping getTypeMapping() {
			return GenericOrmPrimaryKeyJoinColumnRelationshipStrategy.this.getTypeMapping();
		}

		public Table resolveDbTable(String tableName) {
			return this.getTypeMapping().resolveDbTable(tableName);
		}

		public Table getReferencedColumnDbTable() {
			Entity relationshipTarget = this.getRelationshipTarget();
			return (relationshipTarget == null) ? null : relationshipTarget.getPrimaryDbTable();
		}

		public boolean joinColumnIsDefault(ReadOnlyBaseJoinColumn joinColumn) {
			return false;
		}

		public String getDefaultColumnName() {
			return null;
		}

		public int joinColumnsSize() {
			return GenericOrmPrimaryKeyJoinColumnRelationshipStrategy.this.primaryKeyJoinColumnsSize();
		}

		public TextRange getValidationTextRange() {
			return GenericOrmPrimaryKeyJoinColumnRelationshipStrategy.this.getValidationTextRange();
		}

		public JptValidator buildColumnValidator(NamedColumn column, NamedColumnTextRangeResolver textRangeResolver) {
			return new OneToOnePrimaryKeyJoinColumnValidator(this.getPersistentAttribute(), (BaseJoinColumn) column, this, (BaseJoinColumnTextRangeResolver) textRangeResolver);
		}

		protected RelationshipMapping getRelationshipMapping() {
			return GenericOrmPrimaryKeyJoinColumnRelationshipStrategy.this.getRelationshipMapping();
		}
	}
}