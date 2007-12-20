/*******************************************************************************
 * Copyright (c) 2006, 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.jpt.core.internal.IMappingKeys;
import org.eclipse.jpt.core.internal.context.base.IAttributeMapping;
import org.eclipse.jpt.core.internal.context.base.IPersistentAttribute;
import org.eclipse.jpt.core.internal.context.base.JpaContextNode;
import org.eclipse.jpt.core.internal.resource.orm.AttributeMapping;
import org.eclipse.jpt.core.internal.resource.orm.Basic;
import org.eclipse.jpt.core.internal.resource.orm.Embedded;
import org.eclipse.jpt.core.internal.resource.orm.EmbeddedId;
import org.eclipse.jpt.core.internal.resource.orm.Id;
import org.eclipse.jpt.core.internal.resource.orm.Transient;
import org.eclipse.jpt.core.internal.resource.orm.Version;


public class XmlPersistentAttribute extends JpaContextNode
	implements IPersistentAttribute
{

	protected List<IXmlAttributeMappingProvider> attributeMappingProviders;

	protected XmlAttributeMapping<? extends AttributeMapping> attributeMapping;
	
	protected AttributeMapping attributeMappingResource;
	
	public XmlPersistentAttribute(XmlPersistentType parent, String mappingKey) {
		super(parent);
		this.attributeMappingProviders = buildAttributeMappingProviders();
		this.attributeMapping = buildAttributeMapping(mappingKey);
	}

	protected List<IXmlAttributeMappingProvider> buildAttributeMappingProviders() {
		List<IXmlAttributeMappingProvider> list = new ArrayList<IXmlAttributeMappingProvider>();
		list.add(XmlEmbeddedMappingProvider.instance()); //bug 190344 need to test default embedded before basic
		list.add(XmlBasicMappingProvider.instance());
		list.add(XmlTransientMappingProvider.instance());
		list.add(XmlIdMappingProvider.instance());
//		list.add(XmlManyToManyMappingProvider.instance());
//		list.add(XmlOneToManyMappingProvider.instance());
//		list.add(XmlManyToOneMappingProvider.instance());
//		list.add(XmlOneToOneMappingProvider.instance());
		list.add(XmlVersionMappingProvider.instance());
		list.add(XmlEmbeddedIdMappingProvider.instance());
		return list;
	}
	
	protected IXmlAttributeMappingProvider attributeMappingProvider(String key) {
		for (IXmlAttributeMappingProvider provider : this.attributeMappingProviders) {
			if (provider.key().equals(key)) {
				return provider;
			}
		}
		throw new IllegalArgumentException();
	}

	protected XmlAttributeMapping<? extends AttributeMapping> buildAttributeMapping(String key) {
		return this.attributeMappingProvider(key).buildAttributeMapping(jpaFactory(), this);
	}
	

	public String getName() {
		return getMapping().getName();
	}

	protected void nameChanged(String oldName, String newName) {
		firePropertyChanged(NAME_PROPERTY, oldName, newName);
	}

	public IAttributeMapping getSpecifiedMapping() {
		return this.attributeMapping;
	}
	
	public XmlAttributeMapping<? extends AttributeMapping> getMapping() {
		return this.attributeMapping;
	}

	public String mappingKey() {
		return this.getMapping().getKey();
	}

	public String defaultMappingKey() {
		return null;
	}

	public void setSpecifiedMappingKey(String newMappingKey) {
		if (this.mappingKey() == newMappingKey) {
			return;
		}
		XmlAttributeMapping<? extends AttributeMapping> oldMapping = getMapping();
		this.attributeMapping = buildAttributeMapping(newMappingKey);
		//TODO persistentType().changeMapping(this, oldMapping, this.attributeMapping);
		firePropertyChanged(SPECIFIED_MAPPING_PROPERTY, oldMapping, this.attributeMapping);
	}
	
	protected void setSpecifiedMappingKey_(String newMappingKey) {
		if (this.mappingKey() == newMappingKey) {
			return;
		}
		XmlAttributeMapping<? extends AttributeMapping> oldMapping = getMapping();
		this.attributeMapping = buildAttributeMapping(newMappingKey);
		firePropertyChanged(SPECIFIED_MAPPING_PROPERTY, oldMapping, this.attributeMapping);
	}
//
//	public Object getId() {
//		return IXmlContentNodes.PERSISTENT_ATTRIBUTE_ID;
//	}

	public Collection<IXmlAttributeMappingProvider> attributeMappingProviders() {
		return this.attributeMappingProviders;
	}

	public XmlPersistentType persistentType() {
		return (XmlPersistentType) parent();
	}

	public XmlTypeMapping<?> typeMapping() {
		return persistentType().getMapping();
	}
//
//	public boolean isVirtual() {
//		return getMapping().isVirtual();
//	}
//
//	public void setVirtual(boolean virtual) {
//		getMapping().setVirtual(virtual);
//	}
//
//	public Attribute getAttribute() {
//		JavaPersistentType javaPersistentType = typeMapping().getPersistentType().findJavaPersistentType();
//		if (javaPersistentType == null) {
//			return null;
//		}
//		for (Iterator<JavaPersistentAttribute> i = javaPersistentType.attributes(); i.hasNext();) {
//			JavaPersistentAttribute persistentAttribute = i.next();
//			if (persistentAttribute.getName().equals(getName())) {
//				return persistentAttribute.getAttribute();
//			}
//		}
//		return null;
//	}

	public String primaryKeyColumnName() {
		return getMapping().primaryKeyColumnName();
	}

//	@Override
//	public ITextRange fullTextRange() {
//		return (this.isVirtual()) ? null : super.fullTextRange();
//	}
//
//	@Override
//	public ITextRange validationTextRange() {
//		return (this.isVirtual()) ? this.persistentType().attributesTextRange() : this.getMapping().validationTextRange();
//	}
//
//	@Override
//	public ITextRange selectionTextRange() {
//		return (isVirtual()) ? null : this.getMapping().selectionTextRange();
//	}
//
//	public ITextRange nameTextRange() {
//		return getMapping().nameTextRange();
//	}

	public boolean isOverridableAttribute() {
		return this.getMapping().isOverridableAttributeMapping();
	}

	public boolean isOverridableAssociation() {
		return this.getMapping().isOverridableAssociationMapping();
	}

	public boolean isIdAttribute() {
		return this.getMapping().isIdMapping();
	}
	
	public void initialize(Basic basic) {
		this.attributeMappingResource = basic;
		((XmlBasicMapping) getMapping()).initialize(basic);
	}
	
	public void initialize(Embedded embedded) {
		this.attributeMappingResource = embedded;
		((XmlEmbeddedMapping) getMapping()).initialize(embedded);
	}
	
	public void initialize(Version version) {
		this.attributeMappingResource = version;
		((XmlVersionMapping) getMapping()).initialize(version);
	}
	
	public void initialize(Id id) {
		this.attributeMappingResource = id;
		((XmlIdMapping) getMapping()).initialize(id);
	}
	
	public void initialize(EmbeddedId embeddedId) {
		this.attributeMappingResource = embeddedId;
		((XmlEmbeddedIdMapping) getMapping()).initialize(embeddedId);
	}
	
	public void initialize(Transient transientResource) {
		this.attributeMappingResource = transientResource;
		((XmlTransientMapping) getMapping()).initialize(transientResource);
	}
		
	public void update(Id id) {
		this.attributeMappingResource = id;
		if (mappingKey() == IMappingKeys.ID_ATTRIBUTE_MAPPING_KEY) {
			((XmlIdMapping) getMapping()).update(id);
		}
		else {
			setSpecifiedMappingKey_(IMappingKeys.ID_ATTRIBUTE_MAPPING_KEY);
			((XmlIdMapping) getMapping()).initialize(id);				
		}
	}
	
	public void update(EmbeddedId embeddedId) {
		this.attributeMappingResource = embeddedId;
		if (mappingKey() == IMappingKeys.EMBEDDED_ID_ATTRIBUTE_MAPPING_KEY) {
			((XmlEmbeddedIdMapping) getMapping()).update(embeddedId);
		}
		else {
			setSpecifiedMappingKey_(IMappingKeys.EMBEDDED_ID_ATTRIBUTE_MAPPING_KEY);
			((XmlEmbeddedIdMapping) getMapping()).initialize(embeddedId);				
		}
	}

	public void update(Basic basic) {
		this.attributeMappingResource = basic;
		if (mappingKey() == IMappingKeys.BASIC_ATTRIBUTE_MAPPING_KEY) {
			((XmlBasicMapping) getMapping()).update(basic);
		}
		else {
			setSpecifiedMappingKey_(IMappingKeys.BASIC_ATTRIBUTE_MAPPING_KEY);
			((XmlBasicMapping) getMapping()).initialize(basic);				
		}
	}
	
	public void update(Version version) {
		this.attributeMappingResource = version;
		if (mappingKey() == IMappingKeys.VERSION_ATTRIBUTE_MAPPING_KEY) {
			((XmlVersionMapping) getMapping()).update(version);
		}
		else {
			setSpecifiedMappingKey_(IMappingKeys.VERSION_ATTRIBUTE_MAPPING_KEY);
			((XmlVersionMapping) getMapping()).initialize(version);				
		}
	}

	public void update(Embedded embedded) {
		this.attributeMappingResource = embedded;
		if (mappingKey() == IMappingKeys.EMBEDDED_ATTRIBUTE_MAPPING_KEY) {
			((XmlEmbeddedMapping) getMapping()).update(embedded);
		}
		else {
			setSpecifiedMappingKey_(IMappingKeys.EMBEDDED_ATTRIBUTE_MAPPING_KEY);
			((XmlEmbeddedMapping) getMapping()).initialize(embedded);				
		}
	}
	
	public void update(Transient transientResource) {
		this.attributeMappingResource = transientResource;
		if (mappingKey() == IMappingKeys.TRANSIENT_ATTRIBUTE_MAPPING_KEY) {
			((XmlTransientMapping) getMapping()).update(transientResource);
		}
		else {
			setSpecifiedMappingKey_(IMappingKeys.TRANSIENT_ATTRIBUTE_MAPPING_KEY);
			((XmlTransientMapping) getMapping()).initialize(transientResource);				
		}
	}

}
