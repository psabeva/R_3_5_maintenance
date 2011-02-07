/*******************************************************************************
 * Copyright (c) 2007, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa1.context.orm;

import java.util.Iterator;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.iterators.CompositeIterator;
import org.eclipse.jpt.common.utility.internal.iterators.EmptyIterator;
import org.eclipse.jpt.common.utility.internal.iterators.FilteringIterator;
import org.eclipse.jpt.common.utility.internal.iterators.SubIteratorWrapper;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.AssociationOverride;
import org.eclipse.jpt.core.context.AssociationOverrideContainer;
import org.eclipse.jpt.core.context.AttributeMapping;
import org.eclipse.jpt.core.context.BaseColumn;
import org.eclipse.jpt.core.context.JoinColumn;
import org.eclipse.jpt.core.context.JoinColumn.Owner;
import org.eclipse.jpt.core.context.JoinTable;
import org.eclipse.jpt.core.context.OverrideContainer;
import org.eclipse.jpt.core.context.Override_;
import org.eclipse.jpt.core.context.RelationshipMapping;
import org.eclipse.jpt.core.context.Relationship;
import org.eclipse.jpt.core.context.Table;
import org.eclipse.jpt.core.context.TypeMapping;
import org.eclipse.jpt.core.context.java.JavaAssociationOverride;
import org.eclipse.jpt.core.context.orm.OrmAssociationOverrideContainer;
import org.eclipse.jpt.core.context.orm.OrmAttributeMapping;
import org.eclipse.jpt.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.core.context.orm.OrmTypeMapping;
import org.eclipse.jpt.core.internal.context.AttributeMappingTools;
import org.eclipse.jpt.core.internal.context.BaseColumnTextRangeResolver;
import org.eclipse.jpt.core.internal.context.JoinColumnTextRangeResolver;
import org.eclipse.jpt.core.internal.context.JptValidator;
import org.eclipse.jpt.core.internal.context.MappingTools;
import org.eclipse.jpt.core.internal.context.OverrideTextRangeResolver;
import org.eclipse.jpt.core.internal.context.TableTextRangeResolver;
import org.eclipse.jpt.core.internal.context.orm.AbstractOrmBaseEmbeddedMapping;
import org.eclipse.jpt.core.internal.jpa1.context.AssociationOverrideInverseJoinColumnValidator;
import org.eclipse.jpt.core.internal.jpa1.context.AssociationOverrideJoinColumnValidator;
import org.eclipse.jpt.core.internal.jpa1.context.AssociationOverrideJoinTableValidator;
import org.eclipse.jpt.core.internal.jpa1.context.AssociationOverrideValidator;
import org.eclipse.jpt.core.internal.jpa1.context.EmbeddableOverrideDescriptionProvider;
import org.eclipse.jpt.core.internal.jpa1.context.EntityTableDescriptionProvider;
import org.eclipse.jpt.core.internal.jpa1.context.JoinTableTableDescriptionProvider;
import org.eclipse.jpt.core.jpa2.context.java.JavaEmbeddedMapping2_0;
import org.eclipse.jpt.core.jpa2.context.orm.OrmEmbeddedMapping2_0;
import org.eclipse.jpt.core.resource.orm.Attributes;
import org.eclipse.jpt.core.resource.orm.XmlAssociationOverride;
import org.eclipse.jpt.core.resource.orm.XmlEmbedded;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

/**
 * <code>orm.xml</code> embedded mapping
 */
public class GenericOrmEmbeddedMapping
	extends AbstractOrmBaseEmbeddedMapping<XmlEmbedded>
	implements OrmEmbeddedMapping2_0
{
	protected final OrmAssociationOverrideContainer associationOverrideContainer;


	public GenericOrmEmbeddedMapping(OrmPersistentAttribute parent, XmlEmbedded xmlMapping) {
		super(parent, xmlMapping);
		this.associationOverrideContainer = this.buildAssociationOverrideContainer();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.associationOverrideContainer.synchronizeWithResourceModel();
	}

	@Override
	public void update() {
		super.update();
		this.associationOverrideContainer.update();
	}


	// ********** association override container **********

	public OrmAssociationOverrideContainer getAssociationOverrideContainer() {
		return this.associationOverrideContainer;
	}

	protected OrmAssociationOverrideContainer buildAssociationOverrideContainer() {
		return this.isJpa2_0Compatible() ?
				this.getContextNodeFactory2_0().buildOrmAssociationOverrideContainer(this, this.buildAssociationOverrideContainerOwner()) :
				new GenericOrmAssociationOverrideContainer(this, null);
	}

	protected OrmAssociationOverrideContainer.Owner buildAssociationOverrideContainerOwner() {
		return new AssociationOverrideContainerOwner();
	}


	// ********** embedded mappings **********

	/**
	 * This is only to build the choices for a "mapped by" setting in a
	 * relationship mapping. JPA 2.0 does not support relationship mappings
	 * in an embedded ID class; so we only put this logic here.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Iterator<String> allMappingNames() {
		return this.isJpa2_0Compatible() ?
				new CompositeIterator<String>(super.allMappingNames(), this.allEmbeddableAttributeMappingNames()) :
				super.allMappingNames();
	}

	protected Iterator<String> allEmbeddableAttributeMappingNames() {
		return this.qualifiedEmbeddableOverridableMappingNames(AttributeMappingTools.ALL_MAPPING_NAMES_TRANSFORMER);
	}

	protected Iterator<RelationshipMapping> allOverridableAssociations() {
		return (this.targetEmbeddable != null) ?
				new SubIteratorWrapper<AttributeMapping, RelationshipMapping>(this.allOverridableAssociations_()) :
				EmptyIterator.<RelationshipMapping>instance();
	}

	protected Iterator<AttributeMapping> allOverridableAssociations_() {
		return new FilteringIterator<AttributeMapping>(this.targetEmbeddable.attributeMappings()) {
			@Override
			protected boolean accept(AttributeMapping attributeMapping) {
				return attributeMapping.isOverridableAssociationMapping();
			}
		};
	}

	@Override
	public AttributeMapping resolveAttributeMapping(String attributeName) {
		AttributeMapping resolvedMapping = super.resolveAttributeMapping(attributeName);
		if (resolvedMapping != null) {
			return resolvedMapping;
		}
		return this.isJpa2_0Compatible() ? this.resolveAttributeMapping_(attributeName) : null;
	}

	protected AttributeMapping resolveAttributeMapping_(String attributeName) {
		attributeName = this.unqualify(attributeName);
		if (attributeName == null) {
			return null;
		}
		// recurse into the embeddable mappings
		for (AttributeMapping mapping : CollectionTools.iterable(this.embeddableAttributeMappings())) {
			AttributeMapping resolvedMapping = mapping.resolveAttributeMapping(attributeName);
			if (resolvedMapping != null) {
				return resolvedMapping;
			}
		}
		return null;
	}


	// ********** misc **********

	public String getKey() {
		return MappingKeys.EMBEDDED_ATTRIBUTE_MAPPING_KEY;
	}

	public int getXmlSequence() {
		return 80;
	}

	protected JavaEmbeddedMapping2_0 getJavaEmbeddedMapping2_0() {
		return (JavaEmbeddedMapping2_0) this.getJavaEmbeddedMapping();
	}

	public void initializeOn(OrmAttributeMapping newMapping) {
		newMapping.initializeFromOrmEmbeddedMapping(this);
	}

	public void addXmlAttributeMappingTo(Attributes xmlAttributes) {
		xmlAttributes.getEmbeddeds().add(this.xmlAttributeMapping);
	}

	public void removeXmlAttributeMappingFrom(Attributes xmlAttributes) {
		xmlAttributes.getEmbeddeds().remove(this.xmlAttributeMapping);
	}

	protected JavaAssociationOverride getSpecifiedJavaAssociationOverrideNamed(String attributeName) {
		JavaEmbeddedMapping2_0 javaMapping = this.getJavaEmbeddedMapping2_0();
		return (javaMapping == null) ? null : javaMapping.getAssociationOverrideContainer().getSpecifiedOverrideNamed(attributeName);
	}

	@Override
	public Relationship resolveOverriddenRelationship(String attributeName) {
		return this.isJpa2_0Compatible() ? this.resolveOverriddenRelationship_(attributeName) : null;
	}

	protected Relationship resolveOverriddenRelationship_(String attributeName) {
		attributeName = this.unqualify(attributeName);
		if (attributeName == null) {
			return null;
		}
		AssociationOverride override = this.associationOverrideContainer.getSpecifiedOverrideNamed(attributeName);
		// recurse into the target embeddable if necessary
		return (override != null) ? override.getRelationship() : this.resolveOverriddenRelationshipInTargetEmbeddable(attributeName);
	}

	protected Relationship resolveOverriddenRelationshipInTargetEmbeddable(String attributeName) {
		return (this.targetEmbeddable == null) ? null : this.targetEmbeddable.resolveOverriddenRelationship(attributeName);
	}


	// ********** validation **********

	@Override
	protected void validateOverrides(List<IMessage> messages, IReporter reporter) {
		super.validateOverrides(messages, reporter);
		this.associationOverrideContainer.validate(messages, reporter);
	}


	// ********** association override container owner **********

	protected class AssociationOverrideContainerOwner
		implements OrmAssociationOverrideContainer.Owner
	{
		protected String getMappingName() {
			return GenericOrmEmbeddedMapping.this.getName();
		}

		public OrmTypeMapping getTypeMapping() {
			return GenericOrmEmbeddedMapping.this.getTypeMapping();
		}

		public TypeMapping getOverridableTypeMapping() {
			return GenericOrmEmbeddedMapping.this.getTargetEmbeddable();
		}

		public Iterator<String> allOverridableNames() {
			TypeMapping typeMapping = this.getOverridableTypeMapping();
			return (typeMapping != null) ? typeMapping.allOverridableAssociationNames() : EmptyIterator.<String>instance();
		}

		public EList<XmlAssociationOverride> getXmlOverrides() {
			return GenericOrmEmbeddedMapping.this.getXmlAttributeMapping().getAssociationOverrides();
		}

		public Relationship resolveOverriddenRelationship(String attributeName) {
			return MappingTools.resolveOverriddenRelationship(this.getOverridableTypeMapping(), attributeName);
		}

		public boolean tableNameIsInvalid(String tableName) {
			return this.getTypeMapping().tableNameIsInvalid(tableName);
		}

		public Iterator<String> candidateTableNames() {
			return this.getTypeMapping().allAssociatedTableNames();
		}

		public org.eclipse.jpt.jpa.db.Table resolveDbTable(String tableName) {
			return this.getTypeMapping().resolveDbTable(tableName);
		}

		public String getDefaultTableName() {
			return this.getTypeMapping().getPrimaryTableName();
		}

		public TextRange getValidationTextRange() {
			return GenericOrmEmbeddedMapping.this.getValidationTextRange();
		}

		public JptValidator buildValidator(Override_ override, OverrideContainer container, OverrideTextRangeResolver textRangeResolver) {
			return new AssociationOverrideValidator(this.getPersistentAttribute(), (AssociationOverride) override, (AssociationOverrideContainer) container, textRangeResolver, new EmbeddableOverrideDescriptionProvider());
		}

		public JptValidator buildColumnValidator(Override_ override, BaseColumn column, BaseColumn.Owner owner, BaseColumnTextRangeResolver textRangeResolver) {
			return new AssociationOverrideJoinColumnValidator(this.getPersistentAttribute(), (AssociationOverride) override, (JoinColumn) column, (JoinColumn.Owner) owner, (JoinColumnTextRangeResolver) textRangeResolver, new EntityTableDescriptionProvider());
		}

		public JptValidator buildJoinTableJoinColumnValidator(AssociationOverride override, JoinColumn column, JoinColumn.Owner owner, JoinColumnTextRangeResolver textRangeResolver) {
			return new AssociationOverrideJoinColumnValidator(this.getPersistentAttribute(), override, column, owner, textRangeResolver, new JoinTableTableDescriptionProvider());
		}

		public JptValidator buildJoinTableInverseJoinColumnValidator(AssociationOverride override, JoinColumn column, Owner owner, JoinColumnTextRangeResolver textRangeResolver) {
			return new AssociationOverrideInverseJoinColumnValidator(this.getPersistentAttribute(), override, column, owner, textRangeResolver, new JoinTableTableDescriptionProvider());
		}

		public JptValidator buildTableValidator(AssociationOverride override, Table table, TableTextRangeResolver textRangeResolver) {
			return new AssociationOverrideJoinTableValidator(this.getPersistentAttribute(), override, (JoinTable) table, textRangeResolver);
		}

		protected OrmPersistentAttribute getPersistentAttribute() {
			return GenericOrmEmbeddedMapping.this.getPersistentAttribute();
		}
	}
}