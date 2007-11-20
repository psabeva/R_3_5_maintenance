/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import java.util.Iterator;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.ITextRange;
import org.eclipse.jpt.core.internal.context.base.INamedColumn;
import org.eclipse.jpt.core.internal.resource.java.JPA;
import org.eclipse.jpt.core.internal.resource.java.JavaPersistentResource;
import org.eclipse.jpt.core.internal.resource.java.NamedColumn;
import org.eclipse.jpt.db.internal.Column;
import org.eclipse.jpt.db.internal.Table;
import org.eclipse.jpt.utility.internal.Filter;
import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;
import org.eclipse.jpt.utility.internal.iterators.FilteringIterator;


public abstract class JavaNamedColumn<T extends NamedColumn> extends JavaContextModel
	implements INamedColumn
{

	protected Owner owner;
	
	protected String specifiedName;

	protected String defaultName;

	protected String columnDefinition;
	
	protected JavaPersistentResource persistentResource;

	protected JavaNamedColumn(IJavaJpaContextNode parent, Owner owner) {
		super(parent);
		this.owner = owner;
	}

	// ******************* initialization from java resource model ********************
	public void initializeFromResource(JavaPersistentResource persistentResource) {
		this.persistentResource = persistentResource;
		this.initializeFromResource(this.columnResource());
	}
	
	protected void initializeFromResource(T column) {
		this.specifiedName = column.getName();
		this.defaultName = this.defaultName();
		this.columnDefinition = column.getColumnDefinition();	
	}
	
	//query for the column resource every time on setters since more than one setter 
	//could be called before this object receives notification from the java resource model.
	@SuppressWarnings("unchecked")
	protected T columnResource() {
		return (T) this.persistentResource.nonNullAnnotation(annotationName());
	}
	
	/**
	 * Return the fully qualfied java annotation name that corresponds to this column.
	 * @see JPA
	 */
	protected abstract String annotationName();

	
	//************** INamedColumn implementation *****************
	public String getName() {
		return (this.getSpecifiedName() == null) ? getDefaultName() : this.getSpecifiedName();
	}

	public String getSpecifiedName() {
		return this.specifiedName;
	}

	public void setSpecifiedName(String newSpecifiedName) {
		String oldSpecifiedName = this.specifiedName;
		this.specifiedName = newSpecifiedName;
		columnResource().setName(newSpecifiedName);
		firePropertyChanged(SPECIFIED_NAME_PROPERTY, oldSpecifiedName, newSpecifiedName);
	}

	public String getDefaultName() {
		return this.defaultName;
	}

	protected void setDefaultName(String newDefaultName) {
		String oldDefaultName = this.defaultName;
		this.defaultName = newDefaultName;
		firePropertyChanged(DEFAULT_NAME_PROPERTY, oldDefaultName, newDefaultName);
	}

	public String getColumnDefinition() {
		return this.columnDefinition;
	}
	
	public void setColumnDefinition(String newColumnDefinition) {
		String oldColumnDefinition = this.columnDefinition;
		this.columnDefinition = newColumnDefinition;
		columnResource().setColumnDefinition(newColumnDefinition);
		firePropertyChanged(COLUMN_DEFINITION_PROPERTY, oldColumnDefinition, newColumnDefinition);
	}

	protected Owner owner() {
		return this.owner;
	}
	
	public ITextRange validationTextRange(CompilationUnit astRoot) {
		ITextRange textRange = this.persistentResource.textRange(astRoot);
		return (textRange != null) ? textRange : this.owner().validationTextRange(astRoot);
	}

	public ITextRange nameTextRange(CompilationUnit astRoot) {
		return this.columnResource().nameTextRange(astRoot);
	}

	public boolean nameTouches(int pos, CompilationUnit astRoot) {
		return this.columnResource().nameTouches(pos, astRoot);
	}
	
	public Column dbColumn() {
		Table table = this.dbTable();
		return (table == null) ? null : table.columnNamed(this.getName());
	}

	public Table dbTable() {
		return owner().dbTable(this.tableName());
	}

	/**
	 * Return the name of the column's table.
	 */
	protected abstract String tableName();

	public boolean isResolved() {
		return this.dbColumn() != null;
	}

	@Override
	public Iterator<String> connectedCandidateValuesFor(int pos, Filter<String> filter, CompilationUnit astRoot) {
		Iterator<String> result = super.connectedCandidateValuesFor(pos, filter, astRoot);
		if (result != null) {
			return result;
		}
		if (this.nameTouches(pos, astRoot)) {
			return this.quotedCandidateNames(filter);
		}
		return null;
	}

	private Iterator<String> candidateNames() {
		Table dbTable = this.dbTable();
		return (dbTable != null) ? dbTable.columnNames() : EmptyIterator.<String> instance();
	}

	private Iterator<String> candidateNames(Filter<String> filter) {
		return new FilteringIterator<String>(this.candidateNames(), filter);
	}

	private Iterator<String> quotedCandidateNames(Filter<String> filter) {
		return StringTools.quote(this.candidateNames(filter));
	}
	
	
	// ******************* update from java resource model ********************

	public void update(JavaPersistentResource persistentResource) {
		this.persistentResource = persistentResource;
		this.update(this.columnResource());
	}

	protected void update(T column) {
		this.setSpecifiedName(column.getName());
		this.setDefaultName(this.defaultName());
		this.setColumnDefinition(column.getColumnDefinition());
	}
	
	/**
	 * Return the default column name.
	 */
	protected abstract String defaultName();

}
