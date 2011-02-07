/*******************************************************************************
 * Copyright (c) 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa1.context;

import org.eclipse.jpt.core.context.BaseJoinColumn;
import org.eclipse.jpt.core.context.PersistentAttribute;
import org.eclipse.jpt.core.internal.context.BaseJoinColumnTextRangeResolver;
import org.eclipse.jpt.core.internal.jpa1.context.BaseColumnTableValidator.NullTableDescriptionProvider;
import org.eclipse.jpt.core.internal.validation.JpaValidationMessages;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

public abstract class PrimaryKeyJoinColumnValidator
	extends BaseJoinColumnValidator<BaseJoinColumn, BaseJoinColumnTextRangeResolver>
{
	protected PrimaryKeyJoinColumnValidator(
				BaseJoinColumn column,
				BaseJoinColumn.Owner owner,
				BaseJoinColumnTextRangeResolver textRangeResolver) {
		super(column, owner, textRangeResolver, new NullTableDescriptionProvider());
	}

	protected PrimaryKeyJoinColumnValidator(
				PersistentAttribute persistentAttribute,
				BaseJoinColumn column,
				BaseJoinColumn.Owner owner,
				BaseJoinColumnTextRangeResolver textRangeResolver) {
		super(persistentAttribute, column, owner, textRangeResolver, new NullTableDescriptionProvider());
	}

	@Override
	public IMessage buildUnresolvedNameMessage() {
		return this.column.isVirtual() ?
				this.buildUnresolvedNameMessage(this.getVirtualPKJoinColumnUnresolvedNameMessage()) :
				super.buildUnresolvedNameMessage();
	}

	protected String getVirtualPKJoinColumnUnresolvedNameMessage() {
		return JpaValidationMessages.VIRTUAL_PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_NAME;
	}

	@Override
	protected IMessage buildUnresolvedReferencedColumnNameMessage() {
		return this.column.isVirtual() ?
				this.buildUnresolvedReferencedColumnNameMessage(this.getVirtualPKJoinColumnUnresolvedReferencedColumnNameMessage()) :
				super.buildUnresolvedReferencedColumnNameMessage();
	}

	protected String getVirtualPKJoinColumnUnresolvedReferencedColumnNameMessage() {
		return JpaValidationMessages.VIRTUAL_PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
	}

	@Override
	protected IMessage buildUnspecifiedNameMultipleJoinColumnsMessage() {
		return this.column.isVirtual() ?
				this.buildUnspecifiedNameMultipleJoinColumnsMessage(this.getVirtualPKJoinColumnUnspecifiedNameMultipleJoinColumnsMessage()) :
				super.buildUnspecifiedNameMultipleJoinColumnsMessage();
	}

	protected String getVirtualPKJoinColumnUnspecifiedNameMultipleJoinColumnsMessage() {
		return JpaValidationMessages.VIRTUAL_PRIMARY_KEY_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	}

	@Override
	protected IMessage buildUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
		return this.column.isVirtual() ?
				this.buildUnspecifiedNameMultipleJoinColumnsMessage(this.getVirtualPKJoinColumnUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage()) :
				super.buildUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage();
	}

	protected String getVirtualPKJoinColumnUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
		return JpaValidationMessages.VIRTUAL_PRIMARY_KEY_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	}

	@Override
	protected String getUnresolvedNameMessage() {
		return JpaValidationMessages.PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_NAME;
	}

	@Override
	protected String getUnresolvedReferencedColumnNameMessage() {
		return JpaValidationMessages.PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
	}

	@Override
	protected String getUnspecifiedNameMultipleJoinColumnsMessage() {
		return JpaValidationMessages.PRIMARY_KEY_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	}

	@Override
	protected String getUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
		return JpaValidationMessages.PRIMARY_KEY_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	}
}