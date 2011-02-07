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
import org.eclipse.jpt.core.resource.java.EmbeddedIdAnnotation;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;

@SuppressWarnings("nls")
public class EmbeddedIdTests extends JpaJavaResourceModelTestCase {

	public EmbeddedIdTests(String name) {
		super(name);
	}

	private ICompilationUnit createTestEmbeddedId() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.EMBEDDED_ID);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@EmbeddedId");
			}
		});
	}
	
	public void testEmbeddedId() throws Exception {
		ICompilationUnit cu = this.createTestEmbeddedId();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		
		JavaResourceNode mappingAnnotation = attributeResource.getAnnotation(EmbeddedIdAnnotation.ANNOTATION_NAME);
		assertTrue(mappingAnnotation instanceof EmbeddedIdAnnotation);
	}

}