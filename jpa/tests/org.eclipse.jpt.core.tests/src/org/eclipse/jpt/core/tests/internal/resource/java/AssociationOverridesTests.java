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
import java.util.ListIterator;
import org.eclipse.jdt.core.IType;
import org.eclipse.jpt.core.internal.jdtutility.JDTTools;
import org.eclipse.jpt.core.internal.resource.java.AssociationOverride;
import org.eclipse.jpt.core.internal.resource.java.AssociationOverrides;
import org.eclipse.jpt.core.internal.resource.java.JPA;
import org.eclipse.jpt.core.internal.resource.java.JavaPersistentAttributeResource;
import org.eclipse.jpt.core.internal.resource.java.JavaPersistentTypeResource;
import org.eclipse.jpt.core.internal.resource.java.JoinColumn;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;

public class AssociationOverridesTests extends JavaResourceModelTestCase {
	
	private static final String ASSOCIATION_OVERRIDE_NAME = "MY_ASSOCIATION_OVERRIDE";
	
	public AssociationOverridesTests(String name) {
		super(name);
	}
	
	private void createAssociationOverrideAnnotation() throws Exception {
		createJoinColumnAnnotation();
		this.createAnnotationAndMembers("AssociationOverride", 
			"String name(); " +
			"Column column(); ");
	}
	
	private void createAssociationOverridesAnnotation() throws Exception {
		createAssociationOverrideAnnotation();
		this.createAnnotationAndMembers("AssociationOverrides", 
			"AssociationOverride[] value();");
	}

	private void createJoinColumnAnnotation() throws Exception {
		this.createAnnotationAndMembers("JoinColumn", "String name() default \"\";" +
				"String referencedColumnName() default \"\";" +
				"boolean unique() default false;" +
				"boolean nullable() default true;" +
				"boolean insertable() default true;" +
				"boolean updatable() default true;" +
				"String columnDefinition() default \"\";" +
				"String table() default \"\";");
		
	}

	private IType createTestAssociationOverrideOnField() throws Exception {
		createAssociationOverridesAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@AssociationOverrides(@AssociationOverride(name=\"" + ASSOCIATION_OVERRIDE_NAME + "\"))");
			}
		});
	}
	private IType createTestAssociationOverrideWithJoinColumns() throws Exception {
		createAssociationOverridesAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES, JPA.JOIN_COLUMN);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@AssociationOverrides(@AssociationOverride(name=\"" + ASSOCIATION_OVERRIDE_NAME + "\", joinColumns={@JoinColumn(name=\"BAR\"), @JoinColumn}))");
			}
		});
	}
	
	private IType createTestAssociationOverride() throws Exception {
		createAssociationOverrideAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.ASSOCIATION_OVERRIDE, JPA.JOIN_COLUMN);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
				sb.append(CR);
				sb.append("@AssociationOverride(name=\"FOO\", joinColumns=@JoinColumn(name=\"FOO\", columnDefinition=\"BAR\", referencedColumnName=\"BAZ\"))");
			}
		});
	}

	public void testGetName() throws Exception {
		IType testType = this.createTestAssociationOverrideOnField();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		AssociationOverrides associationOverrides = (AssociationOverrides) attributeResource.annotation(JPA.ASSOCIATION_OVERRIDES);
		AssociationOverride associationOverride = associationOverrides.nestedAnnotations().next();

		assertNotNull(associationOverride);
		assertEquals(ASSOCIATION_OVERRIDE_NAME, associationOverride.getName());
	}

	public void testSetName() throws Exception {
		IType testType = this.createTestAssociationOverrideOnField();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		AssociationOverrides associationOverrides = (AssociationOverrides) attributeResource.annotation(JPA.ASSOCIATION_OVERRIDES);
		AssociationOverride associationOverride = associationOverrides.nestedAnnotations().next();

		assertNotNull(associationOverride);
		assertEquals(ASSOCIATION_OVERRIDE_NAME, associationOverride.getName());

		associationOverride.setName("Foo");
		assertEquals("Foo", associationOverride.getName());
		assertSourceContains("@AssociationOverrides(@AssociationOverride(name=\"Foo\"))");
	}
	
	public void testSetNameNull() throws Exception {
		IType testType = this.createTestAssociationOverrideOnField();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		AssociationOverrides associationOverrides = (AssociationOverrides) attributeResource.annotation(JPA.ASSOCIATION_OVERRIDES);
		AssociationOverride associationOverride = associationOverrides.nestedAnnotations().next();
		assertEquals(ASSOCIATION_OVERRIDE_NAME, associationOverride.getName());
		
		associationOverride.setName(null);
		assertNull(associationOverride.getName());
		
		assertSourceDoesNotContain("@AssociationOverride(name=\"" + ASSOCIATION_OVERRIDE_NAME + "\")");
		assertSourceContains("@AssociationOverride");
		assertSourceContains("@AssociationOverrides");
	}
	
	public void testAddAssociationOverrideCopyExisting() throws Exception {
		IType jdtType = createTestAssociationOverride();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(jdtType);
		
		AssociationOverride associationOverride = (AssociationOverride) typeResource.addAnnotation(1, JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES);
		associationOverride.setName("BAR");
		assertSourceContains("@AssociationOverrides({@AssociationOverride(name=\"FOO\", joinColumns = @JoinColumn(name=\"FOO\", columnDefinition = \"BAR\", referencedColumnName = \"BAZ\")),@AssociationOverride(name=\"BAR\")})");
		
		assertNull(typeResource.annotation(JPA.ASSOCIATION_OVERRIDE));
		assertNotNull(typeResource.annotation(JPA.ASSOCIATION_OVERRIDES));
		assertEquals(2, CollectionTools.size(typeResource.annotations(JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES)));
	}

	public void testRemoveAssociationOverrideCopyExisting() throws Exception {
		IType jdtType = createTestAssociationOverride();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(jdtType);
		
		AssociationOverride associationOverride = (AssociationOverride) typeResource.addAnnotation(1, JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES);
		associationOverride.setName("BAR");
		assertSourceContains("@AssociationOverrides({@AssociationOverride(name=\"FOO\", joinColumns = @JoinColumn(name=\"FOO\", columnDefinition = \"BAR\", referencedColumnName = \"BAZ\")),@AssociationOverride(name=\"BAR\")})");
		
		typeResource.removeAnnotation(1, JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES);
		assertSourceContains("@AssociationOverride(name=\"FOO\", joinColumns = @JoinColumn(name=\"FOO\", columnDefinition = \"BAR\", referencedColumnName = \"BAZ\"))");
	}
	
	public void testJoinColumns() throws Exception {
		IType testType = this.createTestAssociationOverrideOnField();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		
		AssociationOverride associationOverride = (AssociationOverride) attributeResource.annotations(JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES).next();
		
		ListIterator<JoinColumn> iterator = associationOverride.joinColumns();
		
		assertEquals(0, CollectionTools.size(iterator));
	}
	
	public void testJoinColumns2() throws Exception {
		IType testType = this.createTestAssociationOverrideOnField();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		
		AssociationOverride associationOverride = (AssociationOverride) attributeResource.annotations(JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES).next();
		
		associationOverride.addJoinColumn(0);
		associationOverride.addJoinColumn(1);
		associationOverride.updateFromJava(JDTTools.buildASTRoot(testType));
		
		ListIterator<JoinColumn> iterator = associationOverride.joinColumns();
		
		assertEquals(2, CollectionTools.size(iterator));
	}
	
	public void testJoinColumns3() throws Exception {
		IType testType = this.createTestAssociationOverrideWithJoinColumns();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		
		AssociationOverride associationOverride = (AssociationOverride) attributeResource.annotations(JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES).next();
				
		ListIterator<JoinColumn> iterator = associationOverride.joinColumns();
		
		assertEquals(2, CollectionTools.size(iterator));
	}
	
	public void testAddJoinColumn() throws Exception {
		IType testType = this.createTestAssociationOverrideOnField();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		
		AssociationOverride associationOverride = (AssociationOverride) attributeResource.annotations(JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES).next();
		
		associationOverride.addJoinColumn(0).setName("FOO");
		associationOverride.addJoinColumn(1);
		associationOverride.addJoinColumn(0).setName("BAR");

		assertSourceContains("@AssociationOverrides(@AssociationOverride(name=\"" + ASSOCIATION_OVERRIDE_NAME + "\", joinColumns = {@JoinColumn(name=\"BAR\"),@JoinColumn(name=\"FOO\"), @JoinColumn}))");
	}
	
	public void testRemoveJoinColumn() throws Exception {
		IType testType = this.createTestAssociationOverrideWithJoinColumns();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		
		AssociationOverride associationOverride = (AssociationOverride) attributeResource.annotations(JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES).next();
		
		associationOverride.removeJoinColumn(1);
		assertSourceContains("@AssociationOverrides(@AssociationOverride(name=\"" + ASSOCIATION_OVERRIDE_NAME + "\", joinColumns=@JoinColumn(name=\"BAR\")))");	

		associationOverride.setName(null);
		associationOverride.removeJoinColumn(0);
		assertSourceDoesNotContain("@AssociationOverride");
	}
	
	public void testMoveJoinColumn() throws Exception {
		IType testType = this.createTestAssociationOverrideWithJoinColumns();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		
		AssociationOverride associationOverride = (AssociationOverride) attributeResource.annotations(JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES).next();
		JoinColumn joinColumn = associationOverride.joinColumnAt(0);
		joinColumn.setReferencedColumnName("REF_NAME");
		joinColumn.setUnique(false);
		joinColumn.setNullable(false);
		joinColumn.setInsertable(false);
		joinColumn.setUpdatable(false);
		joinColumn.setColumnDefinition("COLUMN_DEF");
		joinColumn.setTable("TABLE");
		associationOverride.moveJoinColumn(0, 1);
		assertSourceContains("@AssociationOverrides(@AssociationOverride(name=\"" + ASSOCIATION_OVERRIDE_NAME + "\", joinColumns={@JoinColumn, @JoinColumn(name=\"BAR\", referencedColumnName = \"REF_NAME\", unique = false, nullable = false, insertable = false, updatable = false, columnDefinition = \"COLUMN_DEF\", table = \"TABLE\")}))");
	}
	
	public void testMoveJoinColumn2() throws Exception {
		IType testType = this.createTestAssociationOverrideWithJoinColumns();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType);
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		
		AssociationOverride associationOverride = (AssociationOverride) attributeResource.annotations(JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES).next();
		
		JoinColumn joinColumn = associationOverride.joinColumnAt(0);
		joinColumn.setReferencedColumnName("REF_NAME");
		joinColumn.setUnique(false);
		joinColumn.setNullable(false);
		joinColumn.setInsertable(false);
		joinColumn.setUpdatable(false);
		joinColumn.setColumnDefinition("COLUMN_DEF");
		joinColumn.setTable("TABLE");
		associationOverride.moveJoinColumn(1, 0);
		assertSourceContains("@AssociationOverrides(@AssociationOverride(name=\"" + ASSOCIATION_OVERRIDE_NAME + "\", joinColumns={@JoinColumn, @JoinColumn(name=\"BAR\", referencedColumnName = \"REF_NAME\", unique = false, nullable = false, insertable = false, updatable = false, columnDefinition = \"COLUMN_DEF\", table = \"TABLE\")}))");
	}
	
	public void testSetJoinColumnName() throws Exception {
		IType testType = this.createTestAssociationOverrideWithJoinColumns();
		JavaPersistentTypeResource typeResource = buildJavaTypeResource(testType); 
		JavaPersistentAttributeResource attributeResource = typeResource.fields().next();
		
		AssociationOverride associationOverride = (AssociationOverride) attributeResource.annotations(JPA.ASSOCIATION_OVERRIDE, JPA.ASSOCIATION_OVERRIDES).next();
				
		ListIterator<JoinColumn> iterator = associationOverride.joinColumns();
		assertEquals(2, CollectionTools.size(iterator));
		
		JoinColumn joinColumn = associationOverride.joinColumns().next();
		
		assertEquals("BAR", joinColumn.getName());
		
		joinColumn.setName("foo");
		assertEquals("foo", joinColumn.getName());
		
		assertSourceContains("@AssociationOverrides(@AssociationOverride(name=\"" + ASSOCIATION_OVERRIDE_NAME + "\", joinColumns={@JoinColumn(name=\"foo\"), @JoinColumn}))");
	}
}
