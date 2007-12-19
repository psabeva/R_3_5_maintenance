/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.resource.java;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.ITextRange;
import org.eclipse.jpt.core.internal.jdtutility.AnnotationAdapter;
import org.eclipse.jpt.core.internal.jdtutility.ConversionDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.jdtutility.DeclarationAnnotationAdapter;
import org.eclipse.jpt.core.internal.jdtutility.DeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.jdtutility.IndexedAnnotationAdapter;
import org.eclipse.jpt.core.internal.jdtutility.IndexedDeclarationAnnotationAdapter;
import org.eclipse.jpt.core.internal.jdtutility.Member;
import org.eclipse.jpt.core.internal.jdtutility.MemberAnnotationAdapter;
import org.eclipse.jpt.core.internal.jdtutility.MemberIndexedAnnotationAdapter;
import org.eclipse.jpt.core.internal.jdtutility.NestedIndexedDeclarationAnnotationAdapter;
import org.eclipse.jpt.core.internal.jdtutility.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterators.CloneListIterator;

public class SecondaryTableImpl extends AbstractTableResource implements NestableSecondaryTable
{	
	private static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(SecondaryTable.ANNOTATION_NAME);
	
	private final List<NestablePrimaryKeyJoinColumn> pkJoinColumns;
	
	private final PkJoinColumnsContainerAnnotation pkJoinColumnsContainerAnnotation;
	

	protected SecondaryTableImpl(JavaResource parent, Member member, DeclarationAnnotationAdapter daa, AnnotationAdapter annotationAdapter) {
		super(parent, member, daa, annotationAdapter);
		this.pkJoinColumns = new ArrayList<NestablePrimaryKeyJoinColumn>();
		this.pkJoinColumnsContainerAnnotation = new PkJoinColumnsContainerAnnotation();
	}
	
	@Override
	public void initialize(CompilationUnit astRoot) {
		super.initialize(astRoot);
		ContainerAnnotationTools.initializeNestedAnnotations(astRoot, this.pkJoinColumnsContainerAnnotation);
	}

	@Override
	protected DeclarationAnnotationElementAdapter<String> catalogAdapter(DeclarationAnnotationAdapter declarationAnnotationAdapter) {
		return ConversionDeclarationAnnotationElementAdapter.forStrings(declarationAnnotationAdapter, JPA.SECONDARY_TABLE__CATALOG);
	}

	@Override
	protected DeclarationAnnotationElementAdapter<String> nameAdapter(DeclarationAnnotationAdapter declarationAnnotationAdapter) {
		return ConversionDeclarationAnnotationElementAdapter.forStrings(declarationAnnotationAdapter, JPA.SECONDARY_TABLE__NAME);
	}

	@Override
	protected DeclarationAnnotationElementAdapter<String> schemaAdapter(DeclarationAnnotationAdapter declarationAnnotationAdapter) {
		return ConversionDeclarationAnnotationElementAdapter.forStrings(declarationAnnotationAdapter, JPA.SECONDARY_TABLE__SCHEMA);
	}

	public IndexedAnnotationAdapter getIndexedAnnotationAdapter() {
		return (IndexedAnnotationAdapter) super.getAnnotationAdapter();
	}
	
	public String getAnnotationName() {
		return SecondaryTable.ANNOTATION_NAME;
	}
	
	public void moveAnnotation(int newIndex) {
		getIndexedAnnotationAdapter().moveAnnotation(newIndex);
	}
	
	public void initializeFrom(NestableAnnotation oldAnnotation) {
		SecondaryTable oldSecondaryTable = (SecondaryTable) oldAnnotation;
		setName(oldSecondaryTable.getName());
		setCatalog(oldSecondaryTable.getCatalog());
		setSchema(oldSecondaryTable.getSchema());
		for (UniqueConstraint uniqueConstraint : CollectionTools.iterable(oldSecondaryTable.uniqueConstraints())) {
			NestableUniqueConstraint newUniqueConstraint = addUniqueConstraint(oldSecondaryTable.indexOfUniqueConstraint(uniqueConstraint));
			newUniqueConstraint.initializeFrom((NestableAnnotation) uniqueConstraint);
		}
	}
	
	@Override
	protected NestableUniqueConstraint createUniqueConstraint(int index) {
		return UniqueConstraintImpl.createSecondaryTableUniqueConstraint(this, this.getMember(), this.getDeclarationAnnotationAdapter(), index);
	}
	
	// ************* SecondaryTable implementation *******************
	
	
	public ListIterator<PrimaryKeyJoinColumn> pkJoinColumns() {
		return new CloneListIterator<PrimaryKeyJoinColumn>(this.pkJoinColumns);
	}
	
	public int pkJoinColumnsSize() {
		return this.pkJoinColumns.size();
	}
	
	public NestablePrimaryKeyJoinColumn pkJoinColumnAt(int index) {
		return this.pkJoinColumns.get(index);
	}
	
	public int indexOfPkJoinColumn(PrimaryKeyJoinColumn joinColumn) {
		return this.pkJoinColumns.indexOf(joinColumn);
	}

	
	public PrimaryKeyJoinColumn addPkJoinColumn(int index) {
		NestablePrimaryKeyJoinColumn pkJoinColumn = (NestablePrimaryKeyJoinColumn) ContainerAnnotationTools.addNestedAnnotation(index, this.pkJoinColumnsContainerAnnotation);
		fireItemAdded(SecondaryTable.PK_JOIN_COLUMNS_LIST, index, pkJoinColumn);
		return pkJoinColumn;
	}
	
	protected void addPkJoinColumn(int index, NestablePrimaryKeyJoinColumn pkJoinColumn) {
		addItemToList(index, pkJoinColumn, this.pkJoinColumns, PK_JOIN_COLUMNS_LIST);
	}
	
	public void removePkJoinColumn(int index) {
		NestablePrimaryKeyJoinColumn pkJoinColumn = this.pkJoinColumns.get(index);
		removePkJoinColumn(pkJoinColumn);
		pkJoinColumn.removeAnnotation();
		ContainerAnnotationTools.synchAnnotationsAfterRemove(index, this.pkJoinColumnsContainerAnnotation);
	}
	
	protected void removePkJoinColumn(NestablePrimaryKeyJoinColumn pkJoinColumn) {
		removeItemFromList(pkJoinColumn, this.pkJoinColumns, SecondaryTable.PK_JOIN_COLUMNS_LIST);
	}

	public void movePkJoinColumn(int targetIndex, int sourceIndex) {
		movePkJoinColumnInternal(targetIndex, sourceIndex);
		ContainerAnnotationTools.synchAnnotationsAfterMove(targetIndex, sourceIndex, this.pkJoinColumnsContainerAnnotation);
		fireItemMoved(SecondaryTable.PK_JOIN_COLUMNS_LIST, targetIndex, sourceIndex);
	}
	
	protected void movePkJoinColumnInternal(int targetIndex, int sourceIndex) {
		CollectionTools.move(this.pkJoinColumns, targetIndex, sourceIndex);
	}


	protected NestablePrimaryKeyJoinColumn createPrimaryKeyJoinColumn(int index) {
		return PrimaryKeyJoinColumnImpl.createSecondaryTablePrimaryKeyJoinColumn(getDeclarationAnnotationAdapter(), this, getMember(), index);
	}

	@Override
	public void updateFromJava(CompilationUnit astRoot) {
		super.updateFromJava(astRoot);
		this.updatePkJoinColumnsFromJava(astRoot);
	}
	
	private void updatePkJoinColumnsFromJava(CompilationUnit astRoot) {
		ContainerAnnotationTools.updateNestedAnnotationsFromJava(astRoot, this.pkJoinColumnsContainerAnnotation);
	}

	// ********** static methods **********
	static SecondaryTableImpl createSecondaryTable(JavaResource parent, Member member) {
		return new SecondaryTableImpl(parent, member, DECLARATION_ANNOTATION_ADAPTER, new MemberAnnotationAdapter(member, DECLARATION_ANNOTATION_ADAPTER));
	}

	static SecondaryTableImpl createNestedSecondaryTable(JavaResource parent, Member member, int index, DeclarationAnnotationAdapter secondaryTablesAdapter) {
		IndexedDeclarationAnnotationAdapter idaa = buildNestedDeclarationAnnotationAdapter(index, secondaryTablesAdapter);
		IndexedAnnotationAdapter annotationAdapter = new MemberIndexedAnnotationAdapter(member, idaa);
		return new SecondaryTableImpl(parent, member, idaa, annotationAdapter);
	}

	private static IndexedDeclarationAnnotationAdapter buildNestedDeclarationAnnotationAdapter(int index, DeclarationAnnotationAdapter secondaryTablesAdapter) {
		return new NestedIndexedDeclarationAnnotationAdapter(secondaryTablesAdapter, index, JPA.SECONDARY_TABLE);
	}
	
	
	private class PkJoinColumnsContainerAnnotation extends AbstractResource 
		implements ContainerAnnotation<NestablePrimaryKeyJoinColumn> 
	{
		public PkJoinColumnsContainerAnnotation() {
			super(SecondaryTableImpl.this);
		}
		
		public void initialize(CompilationUnit astRoot) {
			//nothing to initialize
		}
		
		public NestablePrimaryKeyJoinColumn addInternal(int index) {
			NestablePrimaryKeyJoinColumn pKJoinColumn = SecondaryTableImpl.this.createPrimaryKeyJoinColumn(index);
			SecondaryTableImpl.this.pkJoinColumns.add(index, pKJoinColumn);
			return pKJoinColumn;
		}
		
		public NestablePrimaryKeyJoinColumn add(int index) {
			NestablePrimaryKeyJoinColumn pKJoinColumn = SecondaryTableImpl.this.createPrimaryKeyJoinColumn(index);
			SecondaryTableImpl.this.addPkJoinColumn(index, pKJoinColumn);
			return pKJoinColumn;
		}

		public int indexOf(NestablePrimaryKeyJoinColumn pkJoinColumn) {
			return SecondaryTableImpl.this.indexOfPkJoinColumn(pkJoinColumn);
		}

		public void move(int targetIndex, int sourceIndex) {
			SecondaryTableImpl.this.movePkJoinColumn(targetIndex, sourceIndex);
		}
		
		public void moveInternal(int targetIndex, int sourceIndex) {
			SecondaryTableImpl.this.movePkJoinColumnInternal(targetIndex, sourceIndex);
		}

		public NestablePrimaryKeyJoinColumn nestedAnnotationAt(int index) {
			return SecondaryTableImpl.this.pkJoinColumnAt(index);
		}

		public ListIterator<NestablePrimaryKeyJoinColumn> nestedAnnotations() {
			return new CloneListIterator<NestablePrimaryKeyJoinColumn>(SecondaryTableImpl.this.pkJoinColumns);
		}

		public int nestedAnnotationsSize() {
			return pkJoinColumnsSize();
		}

		public String getAnnotationName() {
			return SecondaryTableImpl.this.getAnnotationName();
		}

		public String getNestableAnnotationName() {
			return JPA.PRIMARY_KEY_JOIN_COLUMN;
		}

		public NestablePrimaryKeyJoinColumn nestedAnnotationFor(org.eclipse.jdt.core.dom.Annotation jdtAnnotation) {
			for (NestablePrimaryKeyJoinColumn pkJoinColumn : CollectionTools.iterable(nestedAnnotations())) {
				if (jdtAnnotation == pkJoinColumn.jdtAnnotation((CompilationUnit) jdtAnnotation.getRoot())) {
					return pkJoinColumn;
				}
			}
			return null;
		}

		public void remove(int index) {
			this.remove(nestedAnnotationAt(index));	
		}		

		public void remove(NestablePrimaryKeyJoinColumn pkJoinColumn) {
			SecondaryTableImpl.this.removePkJoinColumn(pkJoinColumn);
		}

		public org.eclipse.jdt.core.dom.Annotation jdtAnnotation(CompilationUnit astRoot) {
			return SecondaryTableImpl.this.jdtAnnotation(astRoot);
		}

		public void newAnnotation() {
			SecondaryTableImpl.this.newAnnotation();
		}

		public void removeAnnotation() {
			SecondaryTableImpl.this.removeAnnotation();
		}

		public void updateFromJava(CompilationUnit astRoot) {
			SecondaryTableImpl.this.updateFromJava(astRoot);
		}
		
		public ITextRange textRange(CompilationUnit astRoot) {
			return SecondaryTableImpl.this.textRange(astRoot);
		}
	}

	public static class SecondaryTableAnnotationDefinition implements AnnotationDefinition
	{
		// singleton
		private static final SecondaryTableAnnotationDefinition INSTANCE = new SecondaryTableAnnotationDefinition();

		/**
		 * Return the singleton.
		 */
		public static AnnotationDefinition instance() {
			return INSTANCE;
		}

		/**
		 * Ensure non-instantiability.
		 */
		private SecondaryTableAnnotationDefinition() {
			super();
		}

		public Annotation buildAnnotation(JavaResource parent, Member member) {
			return SecondaryTableImpl.createSecondaryTable(parent, member);
		}
		
		public Annotation buildNullAnnotation(JavaResource parent, Member member) {
			return null;
		}
		
		public String getAnnotationName() {
			return SecondaryTable.ANNOTATION_NAME;
		}
	}

}
