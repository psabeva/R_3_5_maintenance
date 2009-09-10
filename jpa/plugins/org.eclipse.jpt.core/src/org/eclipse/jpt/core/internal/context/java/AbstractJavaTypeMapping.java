/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import java.util.Iterator;
import java.util.ListIterator;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.context.AttributeMapping;
import org.eclipse.jpt.core.context.ColumnMapping;
import org.eclipse.jpt.core.context.RelationshipMapping;
import org.eclipse.jpt.core.context.Table;
import org.eclipse.jpt.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.core.context.java.JavaColumnMapping;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.context.java.JavaPersistentType;
import org.eclipse.jpt.core.context.java.JavaRelationshipMapping;
import org.eclipse.jpt.core.context.java.JavaTypeMapping;
import org.eclipse.jpt.core.resource.java.Annotation;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.db.Schema;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;
import org.eclipse.jpt.utility.internal.iterators.TransformationIterator;
import org.eclipse.jpt.utility.internal.iterators.TransformationListIterator;


public abstract class AbstractJavaTypeMapping extends AbstractJavaJpaContextNode
	implements JavaTypeMapping
{
	protected JavaResourcePersistentType javaResourcePersistentType;
	

	protected AbstractJavaTypeMapping(JavaPersistentType parent) {
		super(parent);
	}
	
	@Override
	public JavaPersistentType getParent() {
		return (JavaPersistentType) super.getParent();
	}
	
	protected Annotation getResourceMappingAnnotation() {
		return this.javaResourcePersistentType.getAnnotation(getAnnotationName());
	}

	//***************** TypeMapping impl ***************************************
	
	public JavaPersistentType getPersistentType() {
		return getParent();
	}

	public String getPrimaryTableName() {
		return null;
	}

	public org.eclipse.jpt.db.Table getPrimaryDbTable() {
		return null;
	}

	public org.eclipse.jpt.db.Table getDbTable(String tableName) {
		return null;
	}

	public Schema getDbSchema() {
		return null;
	}

	public boolean attributeMappingKeyAllowed(String attributeMappingKey) {
		return true;
	}

	public Iterator<Table> associatedTables() {
		return EmptyIterator.instance();
	}

	public Iterator<String> associatedTableNamesIncludingInherited() {
		return EmptyIterator.instance();
	}

	public Iterator<Table> associatedTablesIncludingInherited() {
		return EmptyIterator.instance();
	}

	public ListIterator<JavaAttributeMapping> attributeMappings() {
		return new TransformationListIterator<JavaPersistentAttribute, JavaAttributeMapping>(getPersistentType().attributes()) {
			@Override
			protected JavaAttributeMapping transform(JavaPersistentAttribute next) {
				return next.getMapping();
			}
		};
	}

	public Iterator<JavaColumnMapping> overridableAttributes() {
		return EmptyIterator.instance();
	}
	
	public Iterator<ColumnMapping> allOverridableAttributes() {
		return EmptyIterator.instance();
	}
	
	public Iterator<String> overridableAttributeNames() {
		return this.namesOf(this.overridableAttributes());
	}

	public Iterator<String> allOverridableAttributeNames() {
		return this.namesOf(this.allOverridableAttributes());
	}

	public Iterator<JavaRelationshipMapping> overridableAssociations() {
		return EmptyIterator.instance();
	}
	
	public Iterator<String> overridableAssociationNames() {
		return this.namesOf(this.overridableAssociations());
	}

	public Iterator<RelationshipMapping> allOverridableAssociations() {
		return EmptyIterator.instance();
	}
	
	public Iterator<String> allOverridableAssociationNames() {
		return this.namesOf(this.allOverridableAssociations());
	}
	
	protected Iterator<String> namesOf(Iterator<? extends AttributeMapping> attributeMappings) {
		return new TransformationIterator<AttributeMapping, String>(attributeMappings) {
			@Override
			protected String transform(AttributeMapping attributeMapping) {
				return attributeMapping.getName();
			}
		};
	}
	
	
	//******************** updating *********************
	public void initialize(JavaResourcePersistentType jrpt) {
		this.javaResourcePersistentType = jrpt;
	}

	public void update(JavaResourcePersistentType jrpt) {
		this.javaResourcePersistentType = jrpt;
	}
	
	//******************** validation *********************
	
	public boolean shouldValidateAgainstDatabase() {
		return getPersistenceUnit().shouldValidateAgainstDatabase();
	}
	
	public TextRange getValidationTextRange(CompilationUnit astRoot) {
		TextRange textRange = this.getResourceMappingAnnotation().getTextRange(astRoot);
		return (textRange != null) ? textRange : this.getPersistentType().getValidationTextRange(astRoot);
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.getPersistentType().getName());
	}
}
