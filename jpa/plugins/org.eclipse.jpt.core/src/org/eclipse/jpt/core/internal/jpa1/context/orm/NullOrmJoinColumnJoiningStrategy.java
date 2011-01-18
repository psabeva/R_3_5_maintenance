/*******************************************************************************
 * Copyright (c) 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa1.context.orm;

import java.util.ListIterator;
import org.eclipse.jpt.core.context.JoinColumn;
import org.eclipse.jpt.core.context.ReadOnlyJoinColumnJoiningStrategy;
import org.eclipse.jpt.core.context.RelationshipMapping;
import org.eclipse.jpt.core.context.TypeMapping;
import org.eclipse.jpt.core.context.orm.OrmJoinColumn;
import org.eclipse.jpt.core.context.orm.OrmJoinColumnEnabledRelationshipReference;
import org.eclipse.jpt.core.context.orm.OrmJoinColumnJoiningStrategy;
import org.eclipse.jpt.core.internal.context.orm.AbstractOrmXmlContextNode;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.db.Table;
import org.eclipse.jpt.utility.internal.iterators.EmptyListIterator;

/**
 * Used by
 * {@link org.eclipse.jpt.core.internal.context.orm.GenericOrmOneToManyRelationshipReference#buildJoinColumnStrategy()}
 * in a JPA 1.0 project.
 */
public class NullOrmJoinColumnJoiningStrategy
	extends AbstractOrmXmlContextNode
	implements OrmJoinColumnJoiningStrategy
{
	public NullOrmJoinColumnJoiningStrategy(OrmJoinColumnEnabledRelationshipReference parent) {
		super(parent);
	}


	// ********** join columns **********

	public ListIterator<OrmJoinColumn> joinColumns() {
		return EmptyListIterator.<OrmJoinColumn>instance();
	}

	public int joinColumnsSize() {
		return 0;
	}


	// ********** specified join columns **********

	public ListIterator<OrmJoinColumn> specifiedJoinColumns() {
		return EmptyListIterator.<OrmJoinColumn>instance();
	}

	public int specifiedJoinColumnsSize() {
		return 0;
	}

	public boolean hasSpecifiedJoinColumns() {
		return false;
	}

	public OrmJoinColumn getSpecifiedJoinColumn(int index) {
		throw new UnsupportedOperationException();
	}

	public OrmJoinColumn addSpecifiedJoinColumn() {
		throw new UnsupportedOperationException();
	}

	public OrmJoinColumn addSpecifiedJoinColumn(int index) {
		throw new UnsupportedOperationException();
	}

	public void removeSpecifiedJoinColumn(JoinColumn joinColumn) {
		throw new UnsupportedOperationException();
	}

	public void removeSpecifiedJoinColumn(int index) {
		throw new UnsupportedOperationException();
	}

	public void moveSpecifiedJoinColumn(int targetIndex, int sourceIndex) {
		throw new UnsupportedOperationException();
	}


	// ********** default join column **********

	public OrmJoinColumn getDefaultJoinColumn() {
		return null;
	}


	// ********** validation **********

	public TextRange getValidationTextRange() {
		return null;
	}

	public String getColumnTableNotValidDescription() {
		return null;
	}


	// ********** misc **********

	@Override
	public OrmJoinColumnEnabledRelationshipReference getParent() {
		return (OrmJoinColumnEnabledRelationshipReference) super.getParent();
	}

	public OrmJoinColumnEnabledRelationshipReference getRelationshipReference() {
		return this.getParent();
	}

	public void initializeFrom(ReadOnlyJoinColumnJoiningStrategy oldStrategy) {
		// NOP
	}

	public void initializeFromVirtual(ReadOnlyJoinColumnJoiningStrategy oldStrategy) {
		// NOP
	}

	public void addStrategy() {
		// NOP
	}

	public void removeStrategy() {
		// NOP
	}

	public boolean isTargetForeignKey() {
		return false;
	}

	public TypeMapping getRelationshipTarget() {
		return null;
	}

	public String getTableName() {
		return null;
	}

	public boolean isOverridable() {
		return false;
	}

	public boolean tableNameIsInvalid(String tableName) {
		return true;
	}

	public Table resolveDbTable(String tableName) {
		return null;
	}

	public TypeMapping getRelationshipSource() {
		return this.getRelationshipMapping().getTypeMapping();
	}

	protected RelationshipMapping getRelationshipMapping() {
		return this.getRelationshipReference().getMapping();
	}
}
