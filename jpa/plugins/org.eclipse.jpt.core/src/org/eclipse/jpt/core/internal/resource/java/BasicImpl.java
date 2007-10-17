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

import java.util.Iterator;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.jdtutility.AnnotationElementAdapter;
import org.eclipse.jpt.core.internal.jdtutility.Attribute;
import org.eclipse.jpt.core.internal.jdtutility.BooleanStringExpressionConverter;
import org.eclipse.jpt.core.internal.jdtutility.ConversionDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.jdtutility.DeclarationAnnotationAdapter;
import org.eclipse.jpt.core.internal.jdtutility.DeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.jdtutility.EnumDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.jdtutility.Member;
import org.eclipse.jpt.core.internal.jdtutility.ShortCircuitAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.jdtutility.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;


public class BasicImpl extends AbstractAnnotationResource<Attribute> implements Basic
{
	private static final String ANNOTATION_NAME = JPA.BASIC;

	private static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);

	private final AnnotationElementAdapter<String> optionalAdapter;

	private final AnnotationElementAdapter<String> fetchAdapter;

	private static final DeclarationAnnotationElementAdapter<String> OPTIONAL_ADAPTER = buildOptionalAdapter();

	private static final DeclarationAnnotationElementAdapter<String> FETCH_ADAPTER = buildFetchAdapter();
	
	private Boolean optional;
	
	private FetchType fetch;
	
	protected BasicImpl(JavaPersistentAttributeResource parent, Attribute attribute) {
		super(parent, attribute, DECLARATION_ANNOTATION_ADAPTER);
		this.optionalAdapter = new ShortCircuitAnnotationElementAdapter<String>(attribute, OPTIONAL_ADAPTER);
		this.fetchAdapter = new ShortCircuitAnnotationElementAdapter<String>(attribute, FETCH_ADAPTER);
	}
	
	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}
	
	public Boolean getOptional() {
		return this.optional;
	}
	
	public void setOptional(Boolean optional) {
		this.optional = optional;
		this.optionalAdapter.setValue(BooleanUtility.toJavaAnnotationValue(optional));
	}

	public FetchType getFetch() {
		return this.fetch;
	}
	
	public void setFetch(FetchType fetch) {
		this.fetch = fetch;
		this.fetchAdapter.setValue(FetchType.toJavaAnnotationValue(fetch));
	}
	
	public void updateFromJava(CompilationUnit astRoot) {
		this.setOptional(BooleanUtility.fromJavaAnnotationValue(this.optionalAdapter.getValue(astRoot)));
		this.setFetch(FetchType.fromJavaAnnotationValue(this.fetchAdapter.getValue(astRoot)));
	}
	
	// ********** static methods **********
	private static DeclarationAnnotationElementAdapter<String> buildOptionalAdapter() {
		return new ConversionDeclarationAnnotationElementAdapter<String>(DECLARATION_ANNOTATION_ADAPTER, JPA.BASIC__OPTIONAL, false, BooleanStringExpressionConverter.instance());
	}

	private static DeclarationAnnotationElementAdapter<String> buildFetchAdapter() {
		return new EnumDeclarationAnnotationElementAdapter(DECLARATION_ANNOTATION_ADAPTER, JPA.BASIC__FETCH, false);
	}
	
	public static class BasicAnnotationDefinition implements MappingAnnotationDefinition
	{
		// singleton
		private static final BasicAnnotationDefinition INSTANCE = new BasicAnnotationDefinition();

		/**
		 * Return the singleton.
		 */
		public static BasicAnnotationDefinition instance() {
			return INSTANCE;
		}

		/**
		 * Ensure non-instantiability.
		 */
		private BasicAnnotationDefinition() {
			super();
		}

		public Annotation buildAnnotation(JavaResource parent, Member member) {
			return new BasicImpl((JavaPersistentAttributeResource) parent, (Attribute) member);
		}

		public Iterator<String> correspondingAnnotationNames() {
			return new ArrayIterator<String>(
				JPA.COLUMN,
				JPA.LOB,
				JPA.TEMPORAL,
				JPA.ENUMERATED);
		}

		public String getAnnotationName() {
			return ANNOTATION_NAME;
		}
	}

}
