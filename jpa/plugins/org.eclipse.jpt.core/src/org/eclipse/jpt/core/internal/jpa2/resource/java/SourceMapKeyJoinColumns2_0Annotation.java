/*******************************************************************************
 * Copyright (c) 2009, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa2.resource.java;

import java.util.Vector;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.resource.java.source.AnnotationContainerTools;
import org.eclipse.jpt.core.internal.resource.java.source.SourceAnnotation;
import org.eclipse.jpt.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.core.jpa2.resource.java.JPA2_0;
import org.eclipse.jpt.core.jpa2.resource.java.MapKeyJoinColumn2_0Annotation;
import org.eclipse.jpt.core.jpa2.resource.java.MapKeyJoinColumns2_0Annotation;
import org.eclipse.jpt.core.jpa2.resource.java.NestableMapKeyJoinColumnAnnotation;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.resource.java.NestableJoinColumnAnnotation;
import org.eclipse.jpt.core.utility.jdt.Attribute;
import org.eclipse.jpt.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterables.LiveCloneIterable;

/**
 * javax.persistence.MapKeyJoinColumns
 */
public final class SourceMapKeyJoinColumns2_0Annotation
	extends SourceAnnotation<Attribute>
	implements MapKeyJoinColumns2_0Annotation
{
	public static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);

	private final Vector<NestableMapKeyJoinColumnAnnotation> mapKeyJoinColumns = new Vector<NestableMapKeyJoinColumnAnnotation>();


	public SourceMapKeyJoinColumns2_0Annotation(JavaResourcePersistentAttribute parent, Attribute attribute) {
		super(parent, attribute, DECLARATION_ANNOTATION_ADAPTER);
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
	public void toString(StringBuilder sb) {
		sb.append(this.mapKeyJoinColumns);
	}


	// ********** AnnotationContainer implementation **********

	public String getElementName() {
		return JPA2_0.MAP_KEY_JOIN_COLUMNS__VALUE;
	}

	public String getNestedAnnotationName() {
		return MapKeyJoinColumn2_0Annotation.ANNOTATION_NAME;
	}

	public Iterable<NestableMapKeyJoinColumnAnnotation> getNestedAnnotations() {
		return new LiveCloneIterable<NestableMapKeyJoinColumnAnnotation>(this.mapKeyJoinColumns);
	}

	public int getNestedAnnotationsSize() {
		return this.mapKeyJoinColumns.size();
	}

	public NestableMapKeyJoinColumnAnnotation addNestedAnnotation() {
		return this.addNestedAnnotation(this.mapKeyJoinColumns.size());
	}

	private NestableMapKeyJoinColumnAnnotation addNestedAnnotation(int index) {
		NestableMapKeyJoinColumnAnnotation joinColumn = this.buildMapKeyJoinColumn(index);
		this.mapKeyJoinColumns.add(joinColumn);
		return joinColumn;
	}

	public void syncAddNestedAnnotation(Annotation astAnnotation) {
		int index = this.mapKeyJoinColumns.size();
		NestableMapKeyJoinColumnAnnotation joinColumn = this.addNestedAnnotation(index);
		joinColumn.initialize((CompilationUnit) astAnnotation.getRoot());
		this.fireItemAdded(MAP_KEY_JOIN_COLUMNS_LIST, index, joinColumn);
	}

	private NestableMapKeyJoinColumnAnnotation buildMapKeyJoinColumn(int index) {
		return SourceMapKeyJoinColumn2_0Annotation.createNestedMapKeyJoinColumn(this, this.member, index, this.daa);
	}

	public NestableMapKeyJoinColumnAnnotation moveNestedAnnotation(int targetIndex, int sourceIndex) {
		return CollectionTools.move(this.mapKeyJoinColumns, targetIndex, sourceIndex).get(targetIndex);
	}

	public NestableMapKeyJoinColumnAnnotation removeNestedAnnotation(int index) {
		return this.mapKeyJoinColumns.remove(index);
	}

	public void syncRemoveNestedAnnotations(int index) {
		this.removeItemsFromList(index, this.mapKeyJoinColumns, MAP_KEY_JOIN_COLUMNS_LIST);
	}

}
