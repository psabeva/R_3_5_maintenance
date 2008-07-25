/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui;

import java.util.ListIterator;
import org.eclipse.jpt.core.context.BasicMapping;
import org.eclipse.jpt.core.context.Embeddable;
import org.eclipse.jpt.core.context.EmbeddedIdMapping;
import org.eclipse.jpt.core.context.EmbeddedMapping;
import org.eclipse.jpt.core.context.IdMapping;
import org.eclipse.jpt.core.context.ManyToManyMapping;
import org.eclipse.jpt.core.context.ManyToOneMapping;
import org.eclipse.jpt.core.context.MappedSuperclass;
import org.eclipse.jpt.core.context.OneToManyMapping;
import org.eclipse.jpt.core.context.OneToOneMapping;
import org.eclipse.jpt.core.context.TransientMapping;
import org.eclipse.jpt.core.context.VersionMapping;
import org.eclipse.jpt.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.core.context.java.JavaEntity;
import org.eclipse.jpt.core.context.orm.OrmEntity;
import org.eclipse.jpt.ui.details.JpaComposite;
import org.eclipse.jpt.ui.details.JpaPageComposite;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.widgets.Composite;

/**
 * Use {@link JpaFactory} to create any {@link JavaTypeMapping} or
 * {@link JavaAttributeMapping}s. This is necessary so that platforms can
 * extend the java model with their own annotations.
 * {@link JavaTypeMappingProvider} and {@link JavaAttributeMappingProvider} use
 * this factory. See {@link JpaPlatform#javaTypeMappingProviders()} and
 * {@link JpaPlatform#javaAttributeMappingProviders() for creating new mappings
 * types.
 * <p>
 * Provisional API: This interface is part of an interim API that is still under
 * development and expected to change significantly before reaching stability.
 * It is available at this early stage to solicit feedback from pioneering
 * adopters on the understanding that any code that uses this API will almost
 * certainly be broken (repeatedly) as the API evolves.
 *
 * @see org.eclipse.jpt.ui.internal.BaseJpaUiFactory
 *
 * @version 2.0
 * @since 1.0
 */
public interface JpaUiFactory
{
	/**
	 * Creates a new <code>JpaComposite</code> used to edit a <code>BasicMapping</code>.
	 *
	 * @param subjectHolder The holder of the basic mapping
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createBasicMappingComposite(
		PropertyValueModel<BasicMapping> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates a new <code>JpaComposite</code> used to edit an <code>Embeddable</code>.
	 *
	 * @param subjectHolder The holder of the basic mapping
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createEmbeddableComposite(
		PropertyValueModel<Embeddable> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates a new <code>JpaComposite</code> used to edit an <code>EmbeddedIdMapping</code>.
	 *
	 * @param subjectHolder The holder of the embedded ID mapping
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createEmbeddedIdMappingComposite(
		PropertyValueModel<EmbeddedIdMapping> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates a new <code>JpaComposite</code> used to edit an <code>EmbeddedMapping</code>.
	 *
	 * @param subjectHolder The holder of the embedded mapping
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createEmbeddedMappingComposite(
		PropertyValueModel<EmbeddedMapping> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates a new <code>JpaComposite</code> used to edit an <code>JavaEntity</code>.
	 *
	 * @param subjectHolder The holder of the java entity
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createJavaEntityComposite(
		PropertyValueModel<JavaEntity> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates a new <code>JpaComposite</code> used to edit a <code>OrmEntity</code>.
	 *
	 * @param subjectHolder The holder of the orm entity
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createOrmEntityComposite(
		PropertyValueModel<OrmEntity> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates a new <code>JpaComposite</code> used to edit an <code>IdMapping</code>.
	 *
	 * @param subjectHolder The holder of the ID mapping
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createIdMappingComposite(
		PropertyValueModel<IdMapping> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates a new <code>JpaComposite</code> used to edit a <code>ManyToManyMapping</code>.
	 *
	 * @param subjectHolder The holder of the many to many mapping
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createManyToManyMappingComposite(
		PropertyValueModel<ManyToManyMapping> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates a new <code>JpaComposite</code> used to edit a <code>ManyToOneMapping</code>.
	 *
	 * @param subjectHolder The holder of the many to one mapping
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createManyToOneMappingComposite(
		PropertyValueModel<ManyToOneMapping> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates a new <code>JpaComposite</code> used to edit a <code>MappedSuperclass</code>.
	 *
	 * @param subjectHolder The holder of the mapped superclass
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createMappedSuperclassComposite(
		PropertyValueModel<MappedSuperclass> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates a new <code>JpaComposite</code> used to edit a <code>OneToManyMapping</code>.
	 *
	 * @param subjectHolder The holder of the one to many mapping
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createOneToManyMappingComposite(
		PropertyValueModel<OneToManyMapping> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates a new <code>JpaComposite</code> used to edit a <code>OneToOneMapping</code>.
	 *
	 * @param subjectHolder The holder of the one to one mapping
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createOneToOneMappingComposite(
		PropertyValueModel<OneToOneMapping> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates the list of <code>JpaComposite</code>s used to edit a
	 * <code>PersistenceUnit</code>. The properties can be regrouped into
	 * sections that will be shown in the editor as pages.
	 *
	 * @param subjectHolder The holder of the pertistence unit
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	ListIterator<JpaPageComposite> createPersistenceUnitComposites(
		PropertyValueModel<PersistenceUnit> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates a new <code>JpaComposite</code> used to edit a <code>TransientMapping</code>.
	 *
	 * @param subjectHolder The holder of the transient mapping
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createTransientMappingComposite(
		PropertyValueModel<TransientMapping> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);

	/**
	 * Creates a new <code>JpaComposite</code> used to edit a <code>VersionMapping</code>.
	 *
	 * @param subjectHolder The holder of the version mapping
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create the widgets
	 * @return A new <code>JpaComposite</code>
	 */
	JpaComposite createVersionMappingComposite(
		PropertyValueModel<VersionMapping> subjectHolder,
		Composite parent,
		WidgetFactory widgetFactory);
}