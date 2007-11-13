/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.tests.internal.resource.java;

import java.util.Iterator;
import org.eclipse.jdt.core.IType;
import org.eclipse.jpt.core.internal.resource.java.JPA;
import org.eclipse.jpt.core.internal.resource.java.JavaPersistentAttributeResource;
import org.eclipse.jpt.core.internal.resource.java.JavaPersistentTypeResource;
import org.eclipse.jpt.core.internal.resource.java.JoinColumn;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;

public class JoinColumnTests extends JavaResourceModelTestCase {
	
	private static final String COLUMN_NAME = "MY_COLUMN";
	private static final String COLUMN_TABLE = "MY_TABLE";
	private static final String COLUMN_COLUMN_DEFINITION = "COLUMN_DEFINITION";
	private static final String COLUMN_REFERENCED_COLUMN_NAME = "MY_REF_COLUMN_NAME";
	
	public JoinColumnTests(String name) {
		super(name);
	}
	
	private void createJoinColumnAnnotation() throws Exception {
		this.createAnnotationAndMembers("JoinColumn", 
			"String name() default \"\"; " +
			"String referencedColumnName() default \"\"; " +
			"boolean unique() default false; " +
			"boolean nullable() default true; " +
			"boolean insertable() default true; " +
			"boolean updatable() default true; " +
			"String columnDefinition() default \"\"; " +
			"String table() default \"\"; ");
	}

	private IType createTestJoinColumn() throws Exception {
		createJoinColumnAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.JOIN_COLUMN);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@JoinColumn");
			}
		});
	}
	
	private IType createTestJoinColumnWithName() throws Exception {
		createJoinColumnAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.JOIN_COLUMN);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@JoinColumn(name=\"" + COLUMN_NAME + "\")");
			}
		});
	}
	
	private IType createTestJoinColumnWithTable() throws Exception {
		createJoinColumnAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.JOIN_COLUMN);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@JoinColumn(table=\"" + COLUMN_TABLE + "\")");
			}
		});
	}
	
	private IType createTestJoinColumnWithReferencedColumnName() throws Exception {
		createJoinColumnAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.JOIN_COLUMN);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@JoinColumn(referencedColumnName=\"" + COLUMN_REFERENCED_COLUMN_NAME + "\")");
			}
		});
	}
	
	private IType createTestJoinColumnWithColumnDefinition() throws Exception {
		createJoinColumnAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.JOIN_COLUMN);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@JoinColumn(columnDefinition=\"" + COLUMN_COLUMN_DEFINITION + "\")");
			}
		});
	}
	
	private IType createTestJoinColumnWithBooleanElement(final String booleanElement) throws Exception {
		createJoinColumnAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.JOIN_COLUMN);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@JoinColumn(" + booleanElement + "=true)");
			}
		});
	}

	public void testGetName() throws Exception {
		IType testType = this.createTestJoinColumnWithName();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);
		assertNotNull(column);
		assertEquals(COLUMN_NAME, column.getName());
	}

	public void testGetNull() throws Exception {
		IType testType = this.createTestJoinColumn();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);
		assertNotNull(column);
		assertNull(column.getName());
		assertNull(column.isNullable());
		assertNull(column.isInsertable());
		assertNull(column.isUnique());
		assertNull(column.isUpdatable());
		assertNull(column.getTable());
		assertNull(column.getReferencedColumnName());
		assertNull(column.getColumnDefinition());
	}

	public void testSetName() throws Exception {
		IType testType = this.createTestJoinColumn();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);

		assertNotNull(column);
		assertNull(column.getName());

		column.setName("Foo");
		assertEquals("Foo", column.getName());
		
		assertSourceContains("@JoinColumn(name=\"Foo\")");
	}
	
	public void testSetNameNull() throws Exception {
		IType testType = this.createTestJoinColumnWithName();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);

		assertEquals(COLUMN_NAME, column.getName());
		
		column.setName(null);
		assertNull(column.getName());
		
		assertSourceDoesNotContain("@JoinColumn");
	}
	
	public void testGetTable() throws Exception {
		IType testType = this.createTestJoinColumnWithTable();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);
		assertEquals(COLUMN_TABLE, column.getTable());
	}

	public void testSetTable() throws Exception {
		IType testType = this.createTestJoinColumn();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);

		assertNotNull(column);
		assertNull(column.getTable());

		column.setTable("Foo");
		assertEquals("Foo", column.getTable());
		
		assertSourceContains("@JoinColumn(table=\"Foo\")");

		
		column.setTable(null);
		assertSourceDoesNotContain("@JoinColumn");
	}
	
	public void testGetReferencedColumnName() throws Exception {
		IType testType = this.createTestJoinColumnWithReferencedColumnName();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);
		assertEquals(COLUMN_REFERENCED_COLUMN_NAME, column.getReferencedColumnName());
	}

	public void testSetReferencedColumnName() throws Exception {
		IType testType = this.createTestJoinColumn();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);

		assertNotNull(column);
		assertNull(column.getReferencedColumnName());

		column.setReferencedColumnName("Foo");
		assertEquals("Foo", column.getReferencedColumnName());
		
		assertSourceContains("@JoinColumn(referencedColumnName=\"Foo\")");

		
		column.setReferencedColumnName(null);
		assertSourceDoesNotContain("@JoinColumn");
	}

	public void testGetColumnDefinition() throws Exception {
		IType testType = this.createTestJoinColumnWithColumnDefinition();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);
		assertEquals(COLUMN_COLUMN_DEFINITION, column.getColumnDefinition());
	}

	public void testSetColumnDefinition() throws Exception {
		IType testType = this.createTestJoinColumn();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);

		assertNotNull(column);
		assertNull(column.getColumnDefinition());

		column.setColumnDefinition("Foo");
		assertEquals("Foo", column.getColumnDefinition());
		
		assertSourceContains("@JoinColumn(columnDefinition=\"Foo\")");

		
		column.setColumnDefinition(null);
		assertSourceDoesNotContain("@JoinColumn");
	}

	public void testGetUnique() throws Exception {
		IType testType = this.createTestJoinColumnWithBooleanElement("unique");
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);

		assertTrue(column.isUnique());
	}
	
	public void testSetUnique() throws Exception {
		IType testType = this.createTestJoinColumn();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);

		assertNotNull(column);
		assertNull(column.isUnique());

		column.setUnique(false);
		assertFalse(column.isUnique());
		
		assertSourceContains("@JoinColumn(unique=false)");
		
		column.setUnique(null);
		assertSourceDoesNotContain("@JoinColumn");
	}
	
	public void testGetNullable() throws Exception {
		IType testType = this.createTestJoinColumnWithBooleanElement("nullable");
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);

		assertTrue(column.isNullable());
	}
	
	public void testSetNullable() throws Exception {
		IType testType = this.createTestJoinColumn();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);

		assertNotNull(column);
		assertNull(column.isNullable());

		column.setNullable(false);
		assertFalse(column.isNullable());
		
		assertSourceContains("@JoinColumn(nullable=false)");
		
		column.setNullable(null);
		assertSourceDoesNotContain("@JoinColumn");
	}

	public void testGetInsertable() throws Exception {
		IType testType = this.createTestJoinColumnWithBooleanElement("insertable");
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);

		assertTrue(column.isInsertable());
	}
	
	public void testSetInsertable() throws Exception {
		IType testType = this.createTestJoinColumn();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);

		assertNotNull(column);
		assertNull(column.isInsertable());

		column.setInsertable(false);
		assertFalse(column.isInsertable());
		
		assertSourceContains("@JoinColumn(insertable=false)");
		
		column.setInsertable(null);
		assertSourceDoesNotContain("@JoinColumn");
	}
	
	public void testGetUpdatable() throws Exception {
		IType testType = this.createTestJoinColumnWithBooleanElement("updatable");
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);

		assertTrue(column.isUpdatable());
	}
	
	public void testSetUpdatable() throws Exception {
		IType testType = this.createTestJoinColumn();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		JoinColumn column = (JoinColumn) attributeResource.annotation(JPA.JOIN_COLUMN);

		assertNotNull(column);
		assertNull(column.isUpdatable());

		column.setUpdatable(false);
		assertFalse(column.isUpdatable());
		
		assertSourceContains("@JoinColumn(updatable=false)");
		
		column.setUpdatable(null);
		assertSourceDoesNotContain("@JoinColumn");
	}
}
