/*******************************************************************************
 * Copyright (c) 2006, 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.ITextRange;
import org.eclipse.jpt.core.internal.context.base.IColumn;
import org.eclipse.jpt.core.internal.context.base.ITypeMapping;
import org.eclipse.jpt.core.internal.resource.java.JavaPersistentAttributeResource;
import org.eclipse.jpt.db.internal.Table;


public abstract class JavaAttributeMapping extends JavaContextModel
	implements IJavaAttributeMapping
{
	protected JavaPersistentAttributeResource persistentAttributeResource;
	

	protected JavaAttributeMapping(IJavaPersistentAttribute parent) {
		super(parent);
	}

	public void initializeFromResource(JavaPersistentAttributeResource persistentAttributeResource) {
		this.persistentAttributeResource = persistentAttributeResource;
	}

	protected IColumn.Owner buildColumnOwner() {
		return new ColumnOwner();
	}

	public JavaPersistentAttribute getPersistentAttribute() {
		return (JavaPersistentAttribute) this.parent();
	}

	/**
	 * the persistent attribute can tell whether there is a "specified" mapping
	 * or a "default" one
	 */
	public boolean isDefault() {
		return this.getPersistentAttribute().mappingIsDefault();
	}

	public ITypeMapping typeMapping() {
		return this.getPersistentAttribute().typeMapping();
	}

	public String attributeName() {
		return this.getPersistentAttribute().getName();
	}
	
	public Table dbTable(String tableName) {
		return typeMapping().dbTable(tableName);
	}
	
	public ITextRange validationTextRange(CompilationUnit astRoot) {
		ITextRange textRange = this.persistentAttributeResource.textRange(astRoot);
		return (textRange != null) ? textRange : this.getPersistentAttribute().validationTextRange(astRoot);
	}

	public void update(JavaPersistentAttributeResource persistentAttributeResource) {
		// do nothing - override as appropriate
	}

	public String primaryKeyColumnName() {
		return null;
	}

	public boolean isOverridableAttributeMapping() {
		return false;
	}

	public boolean isOverridableAssociationMapping() {
		return false;
	}

	public boolean isIdMapping() {
		return false;
	}
	
	/**
	 * mapping implementation of column owner
	 */
	protected class ColumnOwner implements IColumn.Owner
	{
		public ITypeMapping typeMapping() {
			return JavaAttributeMapping.this.typeMapping();
		}

		public ITextRange validationTextRange(CompilationUnit astRoot) {
			return JavaAttributeMapping.this.validationTextRange(astRoot);
		}

		public Table dbTable(String tableName) {
			return this.typeMapping().dbTable(tableName);
		}
		
		public String attributeName() {
			return JavaAttributeMapping.this.attributeName();
		}
	}
}
