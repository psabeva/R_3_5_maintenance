/*******************************************************************************
 * Copyright (c) 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.tests.internal.resource.java;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.common.core.resource.java.JavaResourceField;
import org.eclipse.jpt.common.core.resource.java.JavaResourceType;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLink;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkMapKeyConvertAnnotation2_0;

@SuppressWarnings("nls")
public class MapKeyConvertAnnotation2_0Tests extends EclipseLink2_0JavaResourceModelTestCase {
	
	public MapKeyConvertAnnotation2_0Tests(String name) {
		super(name);
	}

	private ICompilationUnit createTestMapKeyConvert() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(EclipseLink.MAP_KEY_CONVERT);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@MapKeyConvert");
			}
		});
	}
	
	private ICompilationUnit createTestConverterWithValue() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(EclipseLink.MAP_KEY_CONVERT);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@MapKeyConvert(value=\"myConverter\")");
			}
		});
	}

	public void testMapKeyConvertAnnotation() throws Exception {
		ICompilationUnit cu = this.createTestMapKeyConvert();
		JavaResourceType resourceType = buildJavaResourceType(cu); 
		JavaResourceField resourceField = CollectionTools.get(resourceType.getFields(), 0);
		
		assertNotNull(resourceField.getAnnotation(EclipseLink.MAP_KEY_CONVERT));
		
		resourceField.removeAnnotation(EclipseLink.MAP_KEY_CONVERT);
		assertNull(resourceField.getAnnotation(EclipseLink.MAP_KEY_CONVERT));
		
		resourceField.addAnnotation(EclipseLink.MAP_KEY_CONVERT);
		assertNotNull(resourceField.getAnnotation(EclipseLink.MAP_KEY_CONVERT));
	}

	public void testGetValue() throws Exception {
		ICompilationUnit cu = this.createTestConverterWithValue();
		JavaResourceType resourceType = buildJavaResourceType(cu); 
		JavaResourceField resourceField = CollectionTools.get(resourceType.getFields(), 0);
		
		EclipseLinkMapKeyConvertAnnotation2_0 convert = (EclipseLinkMapKeyConvertAnnotation2_0) resourceField.getAnnotation(EclipseLink.MAP_KEY_CONVERT);
		assertEquals("myConverter", convert.getValue());
	}

	public void testSetValue() throws Exception {
		ICompilationUnit cu = this.createTestConverterWithValue();
		JavaResourceType resourceType = buildJavaResourceType(cu); 
		JavaResourceField resourceField = CollectionTools.get(resourceType.getFields(), 0);
		
		EclipseLinkMapKeyConvertAnnotation2_0 convert = (EclipseLinkMapKeyConvertAnnotation2_0) resourceField.getAnnotation(EclipseLink.MAP_KEY_CONVERT);
		assertEquals("myConverter", convert.getValue());
		
		convert.setValue("Bar");
		assertEquals("Bar", convert.getValue());
		
		assertSourceContains("@MapKeyConvert(value=\"Bar\")", cu);
	}
	
	public void testSetValueNull() throws Exception {
		ICompilationUnit cu = this.createTestConverterWithValue();
		JavaResourceType resourceType = buildJavaResourceType(cu); 
		JavaResourceField resourceField = CollectionTools.get(resourceType.getFields(), 0);
		
		EclipseLinkMapKeyConvertAnnotation2_0 convert = (EclipseLinkMapKeyConvertAnnotation2_0) resourceField.getAnnotation(EclipseLink.MAP_KEY_CONVERT);
		assertEquals("myConverter", convert.getValue());
		
		convert.setValue(null);
		assertNull(convert.getValue());
		
		assertSourceContains("@MapKeyConvert", cu);
		assertSourceDoesNotContain("value", cu);
	}

}