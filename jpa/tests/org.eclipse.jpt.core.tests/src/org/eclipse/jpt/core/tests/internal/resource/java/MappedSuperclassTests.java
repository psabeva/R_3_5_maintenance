/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.tests.internal.resource.java;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.core.resource.java.EntityAnnotation;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.resource.java.MappedSuperclassAnnotation;

@SuppressWarnings("nls")
public class MappedSuperclassTests extends JpaJavaResourceModelTestCase {

	public MappedSuperclassTests(String name) {
		super(name);
	}

	private ICompilationUnit createTestMappedSuperclass() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.MAPPED_SUPERCLASS);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@MappedSuperclass");
			}
		});
	}
	
	private ICompilationUnit createTestMappedSuperclassAndEntity() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.MAPPED_SUPERCLASS, JPA.ENTITY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@MappedSuperclass");
				sb.append("@Entity");
			}
		});
	}

	public void testMappedSuperclass() throws Exception {
		ICompilationUnit cu = this.createTestMappedSuperclass();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		
		JavaResourceNode mappingAnnotation = typeResource.getAnnotation(MappedSuperclassAnnotation.ANNOTATION_NAME);
		assertTrue(mappingAnnotation instanceof MappedSuperclassAnnotation);
	}
	
	public void testMappedSuperclassAndEntity() throws Exception {
		ICompilationUnit cu = this.createTestMappedSuperclassAndEntity();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		
		JavaResourceNode mappingAnnotation = typeResource.getAnnotation(EntityAnnotation.ANNOTATION_NAME);
		assertTrue(mappingAnnotation instanceof EntityAnnotation);
		
		MappedSuperclassAnnotation mappedSuperclass = (MappedSuperclassAnnotation) typeResource.getAnnotation(JPA.MAPPED_SUPERCLASS);
		assertNotNull(mappedSuperclass);
	}

}