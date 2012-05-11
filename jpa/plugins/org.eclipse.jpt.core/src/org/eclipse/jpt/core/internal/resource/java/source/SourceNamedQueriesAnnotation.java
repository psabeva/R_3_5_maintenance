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

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.core.resource.java.NamedQueriesAnnotation;
import org.eclipse.jpt.core.resource.java.NamedQueryAnnotation;
import org.eclipse.jpt.core.resource.java.NestableNamedQueryAnnotation;
import org.eclipse.jpt.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.core.utility.jdt.Type;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterables.LiveCloneIterable;

/**
 * javax.persistence.NamedQueries
 */
public abstract class SourceNamedQueriesAnnotation
	extends SourceAnnotation<Type>
	implements NamedQueriesAnnotation
{
	public static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);

	private final Vector<NestableNamedQueryAnnotation> namedQueries = new Vector<NestableNamedQueryAnnotation>();


	public SourceNamedQueriesAnnotation(JavaResourceNode parent, Type type) {
		super(parent, type, DECLARATION_ANNOTATION_ADAPTER);
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
		sb.append(this.namedQueries);
	}


	// ********** AnnotationContainer implementation **********

	public String getElementName() {
		return JPA.NAMED_QUERIES__VALUE;
	}

	public String getNestedAnnotationName() {
		return NamedQueryAnnotation.ANNOTATION_NAME;
	}

	public Iterable<NestableNamedQueryAnnotation> getNestedAnnotations() {
		return new LiveCloneIterable<NestableNamedQueryAnnotation>(this.namedQueries);
	}

	public int getNestedAnnotationsSize() {
		return this.namedQueries.size();
	}

	public NestableNamedQueryAnnotation addNestedAnnotation() {
		return this.addNestedAnnotation(this.namedQueries.size());
	}

	private NestableNamedQueryAnnotation addNestedAnnotation(int index) {
		NestableNamedQueryAnnotation namedQuery = this.buildNamedQuery(index);
		this.namedQueries.add(namedQuery);
		return namedQuery;
	}

	public void syncAddNestedAnnotation(Annotation astAnnotation) {
		int index = this.namedQueries.size();
		NestableNamedQueryAnnotation namedQuery = this.addNestedAnnotation(index);
		namedQuery.initialize((CompilationUnit) astAnnotation.getRoot());
		this.fireItemAdded(NAMED_QUERIES_LIST, index, namedQuery);
	}

	protected abstract NestableNamedQueryAnnotation buildNamedQuery(int index);

	public NestableNamedQueryAnnotation moveNestedAnnotation(int targetIndex, int sourceIndex) {
		return CollectionTools.move(this.namedQueries, targetIndex, sourceIndex).get(targetIndex);
	}

	public NestableNamedQueryAnnotation removeNestedAnnotation(int index) {
		return this.namedQueries.remove(index);
	}

	public void syncRemoveNestedAnnotations(int index) {
		this.removeItemsFromList(index, this.namedQueries, NAMED_QUERIES_LIST);
	}

}
