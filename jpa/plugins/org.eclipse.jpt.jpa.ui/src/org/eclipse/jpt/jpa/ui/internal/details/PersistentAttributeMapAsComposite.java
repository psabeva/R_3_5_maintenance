/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.details;

import java.util.Collection;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.MappingKeys;
import org.eclipse.jpt.jpa.core.context.AttributeMapping;
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;
import org.eclipse.jpt.jpa.core.context.ReadOnlyPersistentAttribute;
import org.eclipse.jpt.jpa.ui.details.DefaultMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.details.MappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.orm.UnsupportedOrmAttributeMappingUiDefinition;
import org.eclipse.swt.widgets.Composite;

/**
 * This "Map As" composite is responsible for showing the mapping name and
 * mapping type for an attribute.
 */
public class PersistentAttributeMapAsComposite 
	extends MapAsComposite<ReadOnlyPersistentAttribute>
{
	public PersistentAttributeMapAsComposite(Pane<? extends ReadOnlyPersistentAttribute> parentPane, Composite parent) {
		super(parentPane, parent);
	}

	public PersistentAttributeMapAsComposite(Pane<? extends ReadOnlyPersistentAttribute> parentPane, Composite parent, PropertyValueModel<Boolean> enabledModel) {
		super(parentPane, parent, enabledModel);
	}
	
	protected String getMappingKey() {
		return getSubject().getMappingKey();
	}

	@Override
	protected MappingChangeHandler buildMappingChangeHandler() {
		return new AttributeMappingChangeHandler();
	}

	protected class AttributeMappingChangeHandler
		implements MappingChangeHandler
	{
		public String getLabelText() {
			String mappingKey = getMappingKey();

			if (mappingKey != MappingKeys.NULL_ATTRIBUTE_MAPPING_KEY) {
				return JptUiDetailsMessages.MapAsComposite_mappedAttributeText;
			}
			if (getSubject().isVirtual()) {
				return JptUiDetailsMessages.MapAsComposite_virtualAttributeText;
			}

			return JptUiDetailsMessages.MapAsComposite_unmappedAttributeText;
		}

		public String getMappingText() {
			AttributeMapping mapping = getSubject().getMapping();
			String mappingKey = mapping.getKey();

			return mapping.isDefault() ? 
                          getDefaultDefinition(mappingKey).getLinkLabel() : 
                          getMappingUiDefinition().getLinkLabel();
		}
		
		public void morphMapping(MappingUiDefinition definition) {
			((PersistentAttribute) getSubject()).setMappingKey(definition.getKey());
		}
		
		public String getName() {
			return getSubject().getName();
		}
		
		public Iterable<MappingUiDefinition> getMappingUiDefinitions() {
			return getAttributeMappingUiDefinitions();
		}

		public MappingUiDefinition getMappingUiDefinition() {
			return getAttributeMappingUiDefinition();
		}
	}
	
	protected Iterable<MappingUiDefinition> getAttributeMappingUiDefinitions() {
		return getJpaPlatformUi().getAttributeMappingUiDefinitions(getSubject());
	}
	
	protected MappingUiDefinition getAttributeMappingUiDefinition() {
		return getJpaPlatformUi().getAttributeMappingUiDefinition(getSubject().getResourceType(), getMappingKey());
	}
	
	@Override
	protected DefaultMappingUiDefinition getDefaultDefinition() {
		return getDefaultDefinition(getSubject().getDefaultMappingKey());
	}
	
	@Override
	protected DefaultMappingUiDefinition getDefaultDefinition(String mappingKey) {
		return getJpaPlatformUi().getDefaultAttributeMappingUiDefinition(getSubject().getMapping().getResourceType(), mappingKey);
	}

	@Override
	protected MappingUiDefinition getMappingUiDefinition() {
		MappingUiDefinition definition = super.getMappingUiDefinition();
		return (definition != null) ? definition : UnsupportedOrmAttributeMappingUiDefinition.instance();
	}
	
	@Override
	protected void addPropertyNames(Collection<String> propertyNames) {
		super.addPropertyNames(propertyNames);
		propertyNames.add(ReadOnlyPersistentAttribute.DEFAULT_MAPPING_KEY_PROPERTY);
		propertyNames.add(ReadOnlyPersistentAttribute.MAPPING_PROPERTY);
		propertyNames.add(ReadOnlyPersistentAttribute.NAME_PROPERTY);
	}

	@Override
	protected void propertyChanged(String propertyName) {
		super.propertyChanged(propertyName);

		if (propertyName == ReadOnlyPersistentAttribute.MAPPING_PROPERTY ||
		    propertyName == ReadOnlyPersistentAttribute.DEFAULT_MAPPING_KEY_PROPERTY   ||
		    propertyName == ReadOnlyPersistentAttribute.NAME_PROPERTY) {

			updateDescription();
		}
	}
}
