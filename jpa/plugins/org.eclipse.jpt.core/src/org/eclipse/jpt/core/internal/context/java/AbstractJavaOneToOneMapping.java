/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import java.util.Vector;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.internal.jpa2.context.java.NullJavaOrphanRemoval2_0;
import org.eclipse.jpt.core.jpa2.JpaFactory2_0;
import org.eclipse.jpt.core.jpa2.context.java.JavaOneToOneMapping2_0;
import org.eclipse.jpt.core.jpa2.context.java.JavaOneToOneRelationshipReference2_0;
import org.eclipse.jpt.core.jpa2.context.java.JavaOrphanRemovable2_0;
import org.eclipse.jpt.core.jpa2.context.java.JavaOrphanRemovalHolder2_0;
import org.eclipse.jpt.core.jpa2.resource.java.OneToOne2_0Annotation;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.OneToOneAnnotation;


public abstract class AbstractJavaOneToOneMapping
	extends AbstractJavaSingleRelationshipMapping<OneToOne2_0Annotation>
	implements JavaOneToOneMapping2_0, JavaOrphanRemovalHolder2_0
{
	protected final JavaOrphanRemovable2_0 orphanRemoval;
	
	// ********** constructor **********
	protected AbstractJavaOneToOneMapping(JavaPersistentAttribute parent) {
		super(parent);
		this.orphanRemoval = this.buildOrphanRemoval();
	}

	// ********** initialize/update **********
	
	@Override
	protected void initialize() {
		super.initialize();
		this.orphanRemoval.initialize(this.getResourcePersistentAttribute());
	}
	
	@Override
	protected void update() {
		super.update();
		this.orphanRemoval.update(this.getResourcePersistentAttribute());
	}
	
	public String getAnnotationName() {
		return OneToOneAnnotation.ANNOTATION_NAME;
	}
	
	@Override
	protected void addSupportingAnnotationNamesTo(Vector<String> names) {
		super.addSupportingAnnotationNamesTo(names);
		names.add(JPA.PRIMARY_KEY_JOIN_COLUMN);
		names.add(JPA.PRIMARY_KEY_JOIN_COLUMNS);
	}
	
	public String getKey() {
		return MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY;
	}
	
	@Override
	protected Boolean getResourceOptional() {
		return this.mappingAnnotation.getOptional();
	}
	
	@Override
	protected void setResourceOptional(Boolean newOptional) {
		this.mappingAnnotation.setOptional(newOptional);
	}

	// ********** JavaOneToOneMapping implementation **********
	
	@Override
	public JavaOneToOneRelationshipReference2_0 getRelationshipReference() {
		return (JavaOneToOneRelationshipReference2_0) super.getRelationshipReference();
	}

	// ********** JavaOrphanRemovalHolder2_0 implementation **********

	protected JavaOrphanRemovable2_0 buildOrphanRemoval() {
		return this.isJpa2_0Compatible() ? 
			((JpaFactory2_0) this.getJpaFactory()).buildJavaOrphanRemoval(this) : 
			new NullJavaOrphanRemoval2_0(this);
	}

	public JavaOrphanRemovable2_0 getOrphanRemoval() {
		return this.orphanRemoval;
	}
}