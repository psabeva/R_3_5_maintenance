/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.resource.java.source;

import java.util.Vector;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.common.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.utility.jdt.Member;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.iterables.LiveCloneIterable;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.core.resource.java.JoinColumnAnnotation;
import org.eclipse.jpt.core.resource.java.JoinColumnsAnnotation;
import org.eclipse.jpt.core.resource.java.NestableAnnotation;
import org.eclipse.jpt.core.resource.java.NestableJoinColumnAnnotation;

/**
 * <code>javax.persistence.JoinColumns</code>
 */
public final class SourceJoinColumnsAnnotation
	extends SourceAnnotation<Member>
	implements JoinColumnsAnnotation
{
	public static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);

	private final Vector<NestableJoinColumnAnnotation> joinColumns = new Vector<NestableJoinColumnAnnotation>();


	public SourceJoinColumnsAnnotation(JavaResourceNode parent, Member member) {
		super(parent, member, DECLARATION_ANNOTATION_ADAPTER);
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	public void initialize(CompilationUnit astRoot) {
		AnnotationContainerTools.initialize(this, astRoot);
	}

	public void synchronizeWith(CompilationUnit astRoot) {
		AnnotationContainerTools.synchronize(this, astRoot);
	}

	@Override
	public boolean isUnset() {
		return super.isUnset() &&
				this.joinColumns.isEmpty();
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.joinColumns);
	}


	// ********** AnnotationContainer implementation **********

	public String getElementName() {
		return JPA.JOIN_COLUMNS__VALUE;
	}

	public String getNestedAnnotationName() {
		return JoinColumnAnnotation.ANNOTATION_NAME;
	}

	public Iterable<NestableJoinColumnAnnotation> getNestedAnnotations() {
		return new LiveCloneIterable<NestableJoinColumnAnnotation>(this.joinColumns);
	}

	public int getNestedAnnotationsSize() {
		return this.joinColumns.size();
	}

	public void nestStandAloneAnnotation(NestableAnnotation standAloneAnnotation) {
		this.nestStandAloneAnnotation(standAloneAnnotation, this.joinColumns.size());
	}

	private void nestStandAloneAnnotation(NestableAnnotation standAloneAnnotation, int index) {
		standAloneAnnotation.convertToNested(this, this.daa, index);
	}

	public void addNestedAnnotation(int index, NestableAnnotation annotation) {
		this.joinColumns.add(index, (NestableJoinColumnAnnotation) annotation);
	}

	public void convertLastNestedAnnotationToStandAlone() {
		this.joinColumns.remove(0).convertToStandAlone();
	}

	public NestableJoinColumnAnnotation addNestedAnnotation() {
		return this.addNestedAnnotation(this.joinColumns.size());
	}

	private NestableJoinColumnAnnotation addNestedAnnotation(int index) {
		NestableJoinColumnAnnotation joinColumn = this.buildJoinColumn(index);
		this.joinColumns.add(index, joinColumn);
		return joinColumn;
	}

	public void syncAddNestedAnnotation(org.eclipse.jdt.core.dom.Annotation astAnnotation) {
		int index = this.joinColumns.size();
		NestableJoinColumnAnnotation joinColumn = this.addNestedAnnotation(index);
		joinColumn.initialize((CompilationUnit) astAnnotation.getRoot());
		this.fireItemAdded(JOIN_COLUMNS_LIST, index, joinColumn);
	}

	private NestableJoinColumnAnnotation buildJoinColumn(int index) {
		// pass the Java resource persistent member as the nested annotation's parent
		// since the nested annotation can be converted to stand-alone
		return SourceJoinColumnAnnotation.createNestedJoinColumn(this.parent, this.annotatedElement, index, this.daa);
	}

	public NestableJoinColumnAnnotation moveNestedAnnotation(int targetIndex, int sourceIndex) {
		return CollectionTools.move(this.joinColumns, targetIndex, sourceIndex).get(targetIndex);
	}

	public NestableJoinColumnAnnotation removeNestedAnnotation(int index) {
		return this.joinColumns.remove(index);
	}

	public void syncRemoveNestedAnnotations(int index) {
		this.removeItemsFromList(index, this.joinColumns, JOIN_COLUMNS_LIST);
	}
}