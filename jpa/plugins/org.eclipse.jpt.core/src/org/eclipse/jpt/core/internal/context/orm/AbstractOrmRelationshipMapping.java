/*******************************************************************************
 * Copyright (c) 2006, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jpt.core.context.AttributeMapping;
import org.eclipse.jpt.core.context.Entity;
import org.eclipse.jpt.core.context.FetchType;
import org.eclipse.jpt.core.context.PersistentAttribute;
import org.eclipse.jpt.core.context.PersistentType;
import org.eclipse.jpt.core.context.ReadOnlyPersistentAttribute;
import org.eclipse.jpt.core.context.RelationshipMapping;
import org.eclipse.jpt.core.context.TypeMapping;
import org.eclipse.jpt.core.context.orm.OrmCascade;
import org.eclipse.jpt.core.context.orm.OrmMappingRelationshipReference;
import org.eclipse.jpt.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.core.context.orm.OrmRelationshipMapping;
import org.eclipse.jpt.core.internal.context.AttributeMappingTools;
import org.eclipse.jpt.core.internal.jpa1.context.orm.GenericOrmCascade;
import org.eclipse.jpt.core.internal.validation.DefaultJpaValidationMessages;
import org.eclipse.jpt.core.internal.validation.JpaValidationMessages;
import org.eclipse.jpt.core.jpa2.context.MetamodelField;
import org.eclipse.jpt.core.resource.orm.AbstractXmlRelationshipMapping;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterables.CompositeIterable;
import org.eclipse.jpt.utility.internal.iterables.EmptyIterable;
import org.eclipse.jpt.utility.internal.iterables.SingleElementIterable;
import org.eclipse.jpt.utility.internal.iterators.CompositeIterator;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;
import org.eclipse.jpt.utility.internal.iterators.TransformationIterator;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

/**
 * <code>orm.xml</code> relationship mapping (1:1, 1:m, m:1, m:m)
 */
public abstract class AbstractOrmRelationshipMapping<X extends AbstractXmlRelationshipMapping>
	extends AbstractOrmAttributeMapping<X>
	implements OrmRelationshipMapping
{
	protected String specifiedTargetEntity;
	protected String defaultTargetEntity;

	protected final OrmMappingRelationshipReference relationshipReference;

	protected final OrmCascade cascade;

	protected FetchType specifiedFetch;
	protected FetchType defaultFetch;


	protected AbstractOrmRelationshipMapping(OrmPersistentAttribute parent, X xmlMapping) {
		super(parent, xmlMapping);
		this.specifiedTargetEntity = xmlMapping.getTargetEntity();
		this.relationshipReference = this.buildRelationshipReference();
		this.cascade = this.buildCascade();
		this.specifiedFetch = this.buildSpecifiedFetch();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.setSpecifiedTargetEntity_(this.xmlAttributeMapping.getTargetEntity());
		this.relationshipReference.synchronizeWithResourceModel();
		this.cascade.synchronizeWithResourceModel();
		this.setSpecifiedFetch_(this.buildSpecifiedFetch());
	}

	@Override
	public void update() {
		super.update();
		this.setDefaultTargetEntity(this.buildDefaultTargetEntity());
		this.relationshipReference.update();
		this.cascade.update();
		this.setDefaultFetch(this.buildDefaultFetch());
	}


	// ********** target entity **********

	public String getTargetEntity() {
		return (this.specifiedTargetEntity != null) ? this.specifiedTargetEntity : this.defaultTargetEntity;
	}

	public String getSpecifiedTargetEntity() {
		return this.specifiedTargetEntity;
	}

	public void setSpecifiedTargetEntity(String entity) {
		this.setSpecifiedTargetEntity_(entity);
		this.xmlAttributeMapping.setTargetEntity(entity);
	}

	protected void setSpecifiedTargetEntity_(String entity) {
		String old = this.specifiedTargetEntity;
		this.specifiedTargetEntity = entity;
		this.firePropertyChanged(SPECIFIED_TARGET_ENTITY_PROPERTY, old, entity);
	}

	public String getDefaultTargetEntity() {
		return this.defaultTargetEntity;
	}

	protected void setDefaultTargetEntity(String entity) {
		String old = this.defaultTargetEntity;
		this.defaultTargetEntity = entity;
		this.firePropertyChanged(DEFAULT_TARGET_ENTITY_PROPERTY, old, entity);
	}

	protected String buildDefaultTargetEntity() {
		return (this.getJavaPersistentAttribute() == null) ? null : this.getJavaTargetType();
	}

	/**
	 * pre-condition: the mapping's Java persistent attribute is not
	 * <code>null</code>.
	 */
	protected abstract String getJavaTargetType();

	public Entity getResolvedTargetEntity() {
		TypeMapping typeMapping = this.getResolvedTargetTypeMapping();
		return (typeMapping instanceof Entity) ? (Entity) typeMapping : null;
	}

	protected TypeMapping getResolvedTargetTypeMapping() {
		PersistentType resolvedTargetType = this.getResolvedTargetType();
		return (resolvedTargetType == null) ? null : resolvedTargetType.getMapping();
	}

	// sub-classes like this to be public
	public PersistentType getResolvedTargetType() {
		return this.resolvePersistentType(this.getTargetEntity());
	}

	public char getTargetEntityEnclosingTypeSeparator() {
		return '$';
	}


	// ********** relationship reference **********

	public OrmMappingRelationshipReference getRelationshipReference() {
		return this.relationshipReference;
	}

	protected abstract OrmMappingRelationshipReference buildRelationshipReference();


	// ********** cascade **********

	public OrmCascade getCascade() {
		return this.cascade;
	}

	protected OrmCascade buildCascade() {
		// NB: we don't use the platform
		return new GenericOrmCascade(this);
	}


	// ********** fetch **********

	public FetchType getFetch() {
		return (this.specifiedFetch != null) ? this.specifiedFetch : this.defaultFetch;
	}

	public FetchType getSpecifiedFetch() {
		return this.specifiedFetch;
	}

	public void setSpecifiedFetch(FetchType fetch) {
		this.setSpecifiedFetch_(fetch);
		this.xmlAttributeMapping.setFetch(FetchType.toOrmResourceModel(fetch));
	}

	protected void setSpecifiedFetch_(FetchType fetch) {
		FetchType old = this.specifiedFetch;
		this.specifiedFetch = fetch;
		this.firePropertyChanged(SPECIFIED_FETCH_PROPERTY, old, fetch);
	}

	protected FetchType buildSpecifiedFetch() {
		return FetchType.fromOrmResourceModel(this.xmlAttributeMapping.getFetch());
	}

	public FetchType getDefaultFetch() {
		return this.defaultFetch;
	}

	protected void setDefaultFetch(FetchType fetch) {
		FetchType old = this.defaultFetch;
		this.defaultFetch = fetch;
		this.firePropertyChanged(DEFAULT_FETCH_PROPERTY, old, fetch);
	}

	protected abstract FetchType buildDefaultFetch();


	// ********** misc **********

	@Override
	public boolean isRelationshipOwner() {
		return this.relationshipReference.isOwner();
	}

	@Override
	public boolean isOwnedBy(AttributeMapping mapping) {
		return mapping.isRelationshipOwner() &&
			this.relationshipReference.isOwnedBy((RelationshipMapping) mapping);
	}

	public RelationshipMapping getRelationshipOwner() {
		Entity entity = this.getResolvedTargetEntity();
		if (entity == null) {
			return null;
		}
		for (ReadOnlyPersistentAttribute attribute : CollectionTools.iterable(entity.getPersistentType().allAttributes())) {
			AttributeMapping mapping = attribute.getMapping();
			if (this.isOwnedBy(mapping)) {
				return (RelationshipMapping) mapping;
			}
		}
		return null;
	}

	@Override
	public boolean isOverridableAssociationMapping() {
		return this.relationshipReference.isOverridable();
	}

	@Override
	protected void initializeFromOrmRelationshipMapping(OrmRelationshipMapping oldMapping) {
		super.initializeFromOrmRelationshipMapping(oldMapping);
		this.setSpecifiedTargetEntity(oldMapping.getSpecifiedTargetEntity());
		this.setSpecifiedFetch(oldMapping.getSpecifiedFetch());
		oldMapping.getRelationshipReference().initializeOn(this.relationshipReference);
		this.cascade.initializeFrom(oldMapping.getCascade());
		//TODO should we set the fetch type from a BasicMapping??
	}

	public Iterator<String> allTargetEntityAttributeNames() {
		return new CompositeIterator<String>(this.allTargetEntityAttributeNamesLists());
	}

	protected Iterator<Iterator<String>> allTargetEntityAttributeNamesLists() {
		return new TransformationIterator<AttributeMapping, Iterator<String>>(this.allTargetEntityAttributeMappings(), AttributeMappingTools.ALL_MAPPING_NAMES_TRANSFORMER);
	}

	protected Iterator<AttributeMapping> allTargetEntityAttributeMappings() {
		Entity entity = this.getResolvedTargetEntity();
		return (entity != null) ? entity.allAttributeMappings() : EmptyIterator.<AttributeMapping>instance();
	}

	protected String getTargetEntityIdAttributeName() {
		PersistentAttribute attribute = this.getTargetEntityIdAttribute();
		return (attribute == null) ? null : attribute.getName();
	}

	protected PersistentAttribute getTargetEntityIdAttribute() {
		Entity entity = this.getResolvedTargetEntity();
		return (entity == null) ? null : entity.getIdAttribute();
	}


	//************ refactoring ************

	@Override
	@SuppressWarnings("unchecked")
	public Iterable<ReplaceEdit> createRenameTypeEdits(IType originalType, String newName) {
		return new CompositeIterable<ReplaceEdit>(
				super.createRenameTypeEdits(originalType, newName),
				this.createTargetEntityRenameTypeEdits(originalType, newName)
			);
	}

	protected Iterable<ReplaceEdit> createTargetEntityRenameTypeEdits(IType originalType, String newName) {
		if (this.specifiedTargetEntity != null) {
			PersistentType targetType = this.getResolvedTargetType();
			if ((targetType != null) && targetType.isFor(originalType.getFullyQualifiedName('.'))) {
				return new SingleElementIterable<ReplaceEdit>(this.createTargetEntityRenameTypeEdit(originalType, newName));
			}
		}
		return EmptyIterable.instance();
	}

	protected ReplaceEdit createTargetEntityRenameTypeEdit(IType originalType, String newName) {
		return this.xmlAttributeMapping.createRenameTargetEntityEdit(originalType, newName);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Iterable<ReplaceEdit> createMoveTypeEdits(IType originalType, IPackageFragment newPackage) {
		return new CompositeIterable<ReplaceEdit>(
				super.createMoveTypeEdits(originalType, newPackage),
				this.createTargetEntityMoveTypeEdits(originalType, newPackage)
			);
	}

	protected Iterable<ReplaceEdit> createTargetEntityMoveTypeEdits(IType originalType, IPackageFragment newPackage) {
		if (this.specifiedTargetEntity != null) {
			PersistentType targetType = this.getResolvedTargetType();
			if ((targetType != null) && targetType.isFor(originalType.getFullyQualifiedName('.'))) {
				return new SingleElementIterable<ReplaceEdit>(this.createTargetEntityRenamePackageEdit(newPackage.getElementName()));
			}
		}
		return EmptyIterable.instance();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Iterable<ReplaceEdit> createRenamePackageEdits(IPackageFragment originalPackage, String newName) {
		return new CompositeIterable<ReplaceEdit>(
				super.createRenamePackageEdits(originalPackage, newName),
				this.createTargetEntityRenamePackageEdits(originalPackage, newName)
			);
	}

	protected Iterable<ReplaceEdit> createTargetEntityRenamePackageEdits(IPackageFragment originalPackage, String newName) {
		if (this.specifiedTargetEntity != null) {
			PersistentType targetType = this.getResolvedTargetType();
			if ((targetType != null) && targetType.isIn(originalPackage)) {
				return new SingleElementIterable<ReplaceEdit>(this.createTargetEntityRenamePackageEdit(newName));
			}
		}
		return EmptyIterable.instance();
	}

	protected ReplaceEdit createTargetEntityRenamePackageEdit(String newName) {
		return this.xmlAttributeMapping.createRenameTargetEntityPackageEdit(newName);
	}


	// ********** validation **********

	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		this.validateTargetEntity(messages);
		this.relationshipReference.validate(messages, reporter);
	}

	protected void validateTargetEntity(List<IMessage> messages) {
		if (this.getTargetEntity() == null) {
			String msg = this.isVirtual() ?
						JpaValidationMessages.VIRTUAL_ATTRIBUTE_TARGET_ENTITY_NOT_DEFINED :
						JpaValidationMessages.TARGET_ENTITY_NOT_DEFINED;
			messages.add(
				DefaultJpaValidationMessages.buildMessage(
					IMessage.HIGH_SEVERITY,
					msg,
					new String[] {this.name},
					this,
					this.getValidationTextRange()
				)
			);
		}
		else if (this.getResolvedTargetEntity() == null) {
			if (this.isVirtual()) {
				messages.add(
					DefaultJpaValidationMessages.buildMessage(
						IMessage.HIGH_SEVERITY,
						JpaValidationMessages.VIRTUAL_ATTRIBUTE_TARGET_ENTITY_IS_NOT_AN_ENTITY,
						new String[] {this.name, this.getTargetEntity()},
						this,
						this.getValidationTextRange()
					)
				);
			} else {
				messages.add(
					DefaultJpaValidationMessages.buildMessage(
						IMessage.HIGH_SEVERITY,
						JpaValidationMessages.TARGET_ENTITY_IS_NOT_AN_ENTITY,
						new String[] {this.getTargetEntity(), this.name},
						this,
						this.getTargetEntityTextRange()
					)
				);
			}
		}
	}

	protected TextRange getTextRange(TextRange textRange) {
		return (textRange != null) ? textRange : this.getPersistentAttribute().getValidationTextRange();
	}

	protected TextRange getTargetEntityTextRange() {
		return this.getTextRange(this.xmlAttributeMapping.getTargetEntityTextRange());
	}


	// ********** metamodel **********

	@Override
	public String getMetamodelTypeName() {
		PersistentType resolvedTargetType = this.getResolvedTargetType();
		if (resolvedTargetType == null) {
			return MetamodelField.DEFAULT_TYPE_NAME;
		}
		String targetTypeName = resolvedTargetType.getName();
		return (targetTypeName != null) ? targetTypeName : MetamodelField.DEFAULT_TYPE_NAME;
	}

}
