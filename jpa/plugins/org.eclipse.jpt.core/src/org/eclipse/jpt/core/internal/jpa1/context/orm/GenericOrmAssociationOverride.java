/*******************************************************************************
 * Copyright (c) 2008, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa1.context.orm;

import java.util.List;
import org.eclipse.jpt.core.context.ReadOnlyAssociationOverride;
import org.eclipse.jpt.core.context.RelationshipMapping;
import org.eclipse.jpt.core.context.orm.OrmAssociationOverride;
import org.eclipse.jpt.core.context.orm.OrmAssociationOverrideContainer;
import org.eclipse.jpt.core.context.orm.OrmAssociationOverrideRelationshipReference;
import org.eclipse.jpt.core.context.orm.OrmVirtualAssociationOverride;
import org.eclipse.jpt.core.resource.orm.XmlAssociationOverride;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

/**
 * Specified <code>orm.xml</code> association override
 */
public class GenericOrmAssociationOverride
	extends AbstractOrmOverride<OrmAssociationOverrideContainer, XmlAssociationOverride>
	implements OrmAssociationOverride
{
	protected final OrmAssociationOverrideRelationshipReference relationship;


	public GenericOrmAssociationOverride(OrmAssociationOverrideContainer parent, XmlAssociationOverride xmlOverride) {
		super(parent, xmlOverride);
		this.relationship = this.buildRelationshipReference();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.relationship.synchronizeWithResourceModel();
	}

	@Override
	public void update() {
		super.update();
		this.relationship.update();
	}


	// ********** specified/virtual **********

	@Override
	public OrmVirtualAssociationOverride convertToVirtual() {
		return (OrmVirtualAssociationOverride) super.convertToVirtual();
	}


	// ********** relationship **********

	public OrmAssociationOverrideRelationshipReference getRelationshipReference() {
		return this.relationship;
	}

	protected OrmAssociationOverrideRelationshipReference buildRelationshipReference() {
		return this.getContextNodeFactory().buildOrmAssociationOverrideRelationshipReference(this);
	}


	// ********** misc **********

	public RelationshipMapping getMapping() {
		return this.getContainer().getRelationshipMapping(this.name);
	}

	public void initializeFrom(ReadOnlyAssociationOverride oldOverride) {
		super.initializeFrom(oldOverride);
		this.relationship.initializeFrom(oldOverride.getRelationshipReference());
	}

	public void initializeFromVirtual(ReadOnlyAssociationOverride virtualOverride) {
		super.initializeFromVirtual(virtualOverride);
		this.relationship.initializeFromVirtual(virtualOverride.getRelationshipReference());
	}


	// ********** validation **********

	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		this.relationship.validate(messages, reporter);
	}
}
