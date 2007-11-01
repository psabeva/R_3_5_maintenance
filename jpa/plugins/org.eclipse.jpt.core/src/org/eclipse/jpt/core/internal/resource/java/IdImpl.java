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
import org.eclipse.jpt.core.internal.jdtutility.Attribute;
import org.eclipse.jpt.core.internal.jdtutility.DeclarationAnnotationAdapter;
import org.eclipse.jpt.core.internal.jdtutility.Member;
import org.eclipse.jpt.core.internal.jdtutility.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;


public class IdImpl extends AbstractAnnotationResource<Attribute> implements Id
{	
	private static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);

	protected IdImpl(JavaPersistentAttributeResource parent, Attribute attribute) {
		super(parent, attribute, DECLARATION_ANNOTATION_ADAPTER);
	}
	
	public void initialize(CompilationUnit astRoot) {
		//nothing to initialize
	}
	
	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	public void updateFromJava(CompilationUnit astRoot) {
		//no annotation members
	}
	
	public static class IdAnnotationDefinition implements MappingAnnotationDefinition
	{
		// singleton
		private static final IdAnnotationDefinition INSTANCE = new IdAnnotationDefinition();

		/**
		 * Return the singleton.
		 */
		public static IdAnnotationDefinition instance() {
			return INSTANCE;
		}

		/**
		 * Ensure non-instantiability.
		 */
		private IdAnnotationDefinition() {
			super();
		}

		public Annotation buildAnnotation(JavaResource parent, Member member) {
			return new IdImpl((JavaPersistentAttributeResource) parent, (Attribute) member);
		}
		
		public Iterator<String> correspondingAnnotationNames() {
			return new ArrayIterator<String>(
				JPA.COLUMN,
				JPA.GENERATED_VALUE,
				JPA.TEMPORAL,
				JPA.TABLE_GENERATOR,
				JPA.SEQUENCE_GENERATOR);
		}

		public String getAnnotationName() {
			return ANNOTATION_NAME;
		}
	}

}
