/*******************************************************************************
 * Copyright (c) 2008, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal;

import java.util.List;

import org.eclipse.jpt.core.internal.platform.GenericJpaAnnotationProvider;
import org.eclipse.jpt.core.resource.java.AnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.BasicCollectionImpl.BasicCollectionAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.BasicMapImpl.BasicMapAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.CacheImpl.CacheAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.ChangeTrackingImpl.ChangeTrackingAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.ConvertImpl.ConvertAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.ConverterImpl.ConverterAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.CustomizerImpl.CustomizerAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.ExistenceCheckingImpl.ExistenceCheckingAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.JoinFetchImpl.JoinFetchAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.MutableImpl.MutableAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.ObjectTypeConverterImpl.ObjectTypeConverterAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.PrivateOwnedImpl.PrivateOwnedAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.ReadOnlyImpl.ReadOnlyAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.ReadTransformerImpl.ReadTransformerAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.StructConverterImpl.StructConverterAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.TransformationImpl.TransformationAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.TypeConverterImpl.TypeConverterAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.VariableOneToOneImpl.VariableOneToOneAnnotationDefinition;
import org.eclipse.jpt.eclipselink.core.internal.resource.java.WriteTransformerImpl.WriteTransformerAnnotationDefinition;

public class EclipseLinkJpaAnnotationProvider
	extends GenericJpaAnnotationProvider
{
	
	@Override
	protected void addTypeSupportingAnnotationDefinitionsTo(List<AnnotationDefinition> definitions) {
		super.addTypeSupportingAnnotationDefinitionsTo(definitions);
		definitions.add(CacheAnnotationDefinition.instance());
		definitions.add(ChangeTrackingAnnotationDefinition.instance());
		definitions.add(ConverterAnnotationDefinition.instance());
		definitions.add(CustomizerAnnotationDefinition.instance());
		definitions.add(ExistenceCheckingAnnotationDefinition.instance());
		definitions.add(ObjectTypeConverterAnnotationDefinition.instance());
		definitions.add(ReadOnlyAnnotationDefinition.instance());
		definitions.add(StructConverterAnnotationDefinition.instance());		
		definitions.add(TypeConverterAnnotationDefinition.instance());		
	}

	// 245996 addresses how the attribute mapping annotations should be ordered
	@Override
	protected void addAttributeMappingAnnotationDefinitionsTo(List<AnnotationDefinition> definitions) {
		super.addAttributeMappingAnnotationDefinitionsTo(definitions);
		definitions.add(BasicCollectionAnnotationDefinition.instance());
		definitions.add(BasicMapAnnotationDefinition.instance());
		definitions.add(TransformationAnnotationDefinition.instance());
		definitions.add(VariableOneToOneAnnotationDefinition.instance());
	}
	
	@Override
	protected void addAttributeSupportingAnnotationDefinitionsTo(List<AnnotationDefinition> definitions) {
		super.addAttributeSupportingAnnotationDefinitionsTo(definitions);
		definitions.add(ConvertAnnotationDefinition.instance());
		definitions.add(ConverterAnnotationDefinition.instance());
		definitions.add(JoinFetchAnnotationDefinition.instance());
		definitions.add(MutableAnnotationDefinition.instance());
		definitions.add(ObjectTypeConverterAnnotationDefinition.instance());
		definitions.add(PrivateOwnedAnnotationDefinition.instance());
		definitions.add(ReadTransformerAnnotationDefinition.instance());		
		definitions.add(StructConverterAnnotationDefinition.instance());		
		definitions.add(TypeConverterAnnotationDefinition.instance());		
		definitions.add(WriteTransformerAnnotationDefinition.instance());		
	}
	
}
