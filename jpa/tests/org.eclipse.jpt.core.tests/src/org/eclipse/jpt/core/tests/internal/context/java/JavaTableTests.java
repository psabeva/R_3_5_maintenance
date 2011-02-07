/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.tests.internal.context.java;

import java.util.Iterator;
import java.util.ListIterator;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.Entity;
import org.eclipse.jpt.core.context.InheritanceType;
import org.eclipse.jpt.core.context.Table;
import org.eclipse.jpt.core.context.java.JavaEntity;
import org.eclipse.jpt.core.context.java.JavaTable;
import org.eclipse.jpt.core.context.java.JavaUniqueConstraint;
import org.eclipse.jpt.core.context.orm.OrmEntity;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.context.persistence.ClassRef;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.resource.java.TableAnnotation;
import org.eclipse.jpt.core.resource.java.UniqueConstraintAnnotation;
import org.eclipse.jpt.core.resource.persistence.PersistenceFactory;
import org.eclipse.jpt.core.resource.persistence.XmlMappingFileRef;
import org.eclipse.jpt.core.tests.internal.context.ContextModelTestCase;

@SuppressWarnings("nls")
public class JavaTableTests extends ContextModelTestCase
{
	private static final String TABLE_NAME = "MY_TABLE";

	private ICompilationUnit createTestEntity() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
		});
	}

	private ICompilationUnit createTestEntityWithTable() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.TABLE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
				sb.append("@Table(name=\"" + TABLE_NAME + "\")");
			}
		});
	}

	private ICompilationUnit createTestSubType() throws Exception {
		return this.createTestType(PACKAGE_NAME, "AnnotationTestTypeChild.java", "AnnotationTestTypeChild", new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY);
			}
			@Override
			public void appendExtendsImplementsTo(StringBuilder sb) {
				sb.append("extends " + TYPE_NAME + " ");
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}

		});
	}

	private ICompilationUnit createAbstractTestEntity() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.INHERITANCE, JPA.INHERITANCE_TYPE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
				sb.append("@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)").append(CR);
				sb.append("abstract");
			}
		});
	}

		
	public JavaTableTests(String name) {
		super(name);
	}

	public void testGetSpecifiedNameNull() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		assertNull(getJavaEntity().getTable().getSpecifiedName());
	}

	public void testGetSpecifiedName() throws Exception {
		createTestEntityWithTable();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		assertEquals(TABLE_NAME, getJavaEntity().getTable().getSpecifiedName());
	}
	
	public void testGetDefaultNameSpecifiedNameNull() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		assertEquals(TYPE_NAME, getJavaEntity().getTable().getDefaultName());
	}

	public void testGetDefaultName() throws Exception {
		createTestEntityWithTable();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		assertEquals(TYPE_NAME, getJavaEntity().getTable().getDefaultName());
		
		//test that setting the java entity name will change the table default name
		getJavaEntity().setSpecifiedName("foo");
		assertEquals("foo", getJavaEntity().getTable().getDefaultName());
	}
	
	public void testGetDefaultNameSingleTableInheritance() throws Exception {
		createTestEntity();
		createTestSubType();
		
		addXmlClassRef(PACKAGE_NAME + ".AnnotationTestTypeChild");
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		ListIterator<ClassRef> specifiedClassRefs = getPersistenceUnit().specifiedClassRefs();
		Entity childEntity = (Entity) specifiedClassRefs.next().getJavaPersistentType().getMapping();
		Entity rootEntity = (Entity) specifiedClassRefs.next().getJavaPersistentType().getMapping();
		
		assertNotSame(getJavaEntity(), rootEntity);
		assertEquals(TYPE_NAME, childEntity.getTable().getDefaultName());
		assertEquals(TYPE_NAME, rootEntity.getTable().getDefaultName());
		
		//test that setting the root java entity name will change the table default name of the child
		rootEntity.setSpecifiedName("foo");
		assertEquals("foo", childEntity.getTable().getDefaultName());
	}

	public void testUpdateDefaultSchemaFromPersistenceUnitDefaults() throws Exception {
		XmlMappingFileRef mappingFileRef = PersistenceFactory.eINSTANCE.createXmlMappingFileRef();
		mappingFileRef.setFileName(JptCorePlugin.DEFAULT_ORM_XML_RUNTIME_PATH.toString());
		getXmlPersistenceUnit().getMappingFiles().add(mappingFileRef);

		createTestEntity();
		
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmEntity ormEntity = (OrmEntity) ormPersistentType.getMapping();
		JavaEntity javaEntity = ormEntity.getJavaTypeMapping();
		
		assertNull(javaEntity.getTable().getDefaultSchema());
		
		getEntityMappings().getPersistenceUnitMetadata().getPersistenceUnitDefaults().setSpecifiedSchema("FOO");
		assertEquals("FOO", javaEntity.getTable().getDefaultSchema());
		
		getEntityMappings().setSpecifiedSchema("BAR");
		assertEquals("BAR", javaEntity.getTable().getDefaultSchema());
		
		ormEntity.getTable().setSpecifiedSchema("XML_SCHEMA");
		assertEquals("BAR", javaEntity.getTable().getDefaultSchema());

		getEntityMappings().removePersistentType(0);
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		//default schema taken from persistence-unit-defaults not entity-mappings since the entity is not in an orm.xml file
		assertEquals("FOO", getJavaEntity().getTable().getDefaultSchema());

		IFile file = getOrmXmlResource().getFile();
		//remove the mapping file reference from the persistence.xml.  default schema 
		//should still come from persistence-unit-defaults because of implied mapped-file-ref
		getXmlPersistenceUnit().getMappingFiles().remove(mappingFileRef);
		assertEquals("FOO", getJavaEntity().getTable().getDefaultSchema());
	
		file.delete(true, null);
		assertNull(getJavaEntity().getTable().getDefaultSchema());
	}
	
	public void testGetNameSpecifiedNameNull() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		assertEquals(TYPE_NAME, getJavaEntity().getTable().getName());
	}
	
	public void testGetName() throws Exception {
		createTestEntityWithTable();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		assertEquals(TABLE_NAME, getJavaEntity().getTable().getName());
	}

	public void testSetSpecifiedName() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		getJavaEntity().getTable().setSpecifiedName("foo");
		
		assertEquals("foo", getJavaEntity().getTable().getSpecifiedName());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation table = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		
		assertEquals("foo", table.getName());
	}
	
	public void testSetSpecifiedNameNull() throws Exception {
		createTestEntityWithTable();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		getJavaEntity().getTable().setSpecifiedName(null);
		
		assertNull(getJavaEntity().getTable().getSpecifiedName());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation table = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
	
		assertNull(table);
	}
	
	public void testUpdateFromSpecifiedNameChangeInResourceModel() throws Exception {
		createTestEntityWithTable();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation table = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		table.setName("foo");
		getJpaProject().synchronizeContextModel();
		
		assertEquals("foo", getJavaEntity().getTable().getSpecifiedName());
		
		typeResource.removeAnnotation(JPA.TABLE);
		getJpaProject().synchronizeContextModel();
		assertNull(getJavaEntity().getTable().getSpecifiedName());
	}
	
	public void testGetCatalog() throws Exception {
		createTestEntityWithTable();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation table = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		
		table.setCatalog("myCatalog");
		getJpaProject().synchronizeContextModel();
		
		assertEquals("myCatalog", getJavaEntity().getTable().getSpecifiedCatalog());
		assertEquals("myCatalog", getJavaEntity().getTable().getCatalog());
	}
	
	public void testGetDefaultCatalog() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		assertNull(getJavaEntity().getTable().getDefaultCatalog());
		
		getJavaEntity().getTable().setSpecifiedCatalog("myCatalog");
		
		assertNull(getJavaEntity().getTable().getDefaultCatalog());
	}
	
	public void testUpdateDefaultCatalogFromPersistenceUnitDefaults() throws Exception {
		XmlMappingFileRef mappingFileRef = PersistenceFactory.eINSTANCE.createXmlMappingFileRef();
		mappingFileRef.setFileName(JptCorePlugin.DEFAULT_ORM_XML_RUNTIME_PATH.toString());
		getXmlPersistenceUnit().getMappingFiles().add(mappingFileRef);

		createTestEntity();
		
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmEntity ormEntity = (OrmEntity) ormPersistentType.getMapping();
		JavaEntity javaEntity = ormEntity.getJavaTypeMapping();
		
		assertNull(javaEntity.getTable().getDefaultCatalog());
		
		getEntityMappings().getPersistenceUnitMetadata().getPersistenceUnitDefaults().setSpecifiedCatalog("FOO");
		assertEquals("FOO", javaEntity.getTable().getDefaultCatalog());
		
		getEntityMappings().setSpecifiedCatalog("BAR");
		assertEquals("BAR", javaEntity.getTable().getDefaultCatalog());
		
		ormEntity.getTable().setSpecifiedCatalog("XML_CATALOG");
		assertEquals("BAR", javaEntity.getTable().getDefaultCatalog());

		getEntityMappings().removePersistentType(0);
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		//default catalog taken from persistence-unite-defaults not entity-mappings since the entity is not in an orm.xml file
		assertEquals("FOO", getJavaEntity().getTable().getDefaultCatalog());

		IFile file = getOrmXmlResource().getFile();
		//remove the mapping file reference from the persistence.xml.  default schema 
		//should still come from persistence-unit-defaults because of implied mapped-file-ref
		getXmlPersistenceUnit().getMappingFiles().remove(mappingFileRef);
		assertEquals("FOO", getJavaEntity().getTable().getDefaultCatalog());
	
		file.delete(true, null);
		assertNull(getJavaEntity().getTable().getDefaultCatalog());
	}

	public void testSetSpecifiedCatalog() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		Table table = getJavaEntity().getTable();
		table.setSpecifiedCatalog("myCatalog");
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation tableResource = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		
		assertEquals("myCatalog", tableResource.getCatalog());
		
		table.setSpecifiedCatalog(null);
		assertNull(typeResource.getAnnotation(JPA.TABLE));
	}
	
	public void testGetSchema() throws Exception {
		createTestEntityWithTable();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation table = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		
		table.setSchema("mySchema");
		getJpaProject().synchronizeContextModel();
		
		assertEquals("mySchema", getJavaEntity().getTable().getSpecifiedSchema());
		assertEquals("mySchema", getJavaEntity().getTable().getSchema());
	}
	
	public void testGetDefaultSchema() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		assertNull(getJavaEntity().getTable().getDefaultSchema());
		
		getJavaEntity().getTable().setSpecifiedSchema("mySchema");
		
		assertNull(getJavaEntity().getTable().getDefaultSchema());
	}
	
	public void testSetSpecifiedSchema() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		Table table = getJavaEntity().getTable();
		table.setSpecifiedSchema("mySchema");
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation tableResource = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		
		assertEquals("mySchema", tableResource.getSchema());
		
		table.setSpecifiedSchema(null);
		assertNull(typeResource.getAnnotation(JPA.TABLE));
	}

	public void testUniqueConstraints() throws Exception {
		createTestEntityWithTable();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		ListIterator<JavaUniqueConstraint> uniqueConstraints = getJavaEntity().getTable().uniqueConstraints();
		assertFalse(uniqueConstraints.hasNext());

		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation tableAnnotation = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		tableAnnotation.addUniqueConstraint(0).addColumnName(0, "foo");
		tableAnnotation.addUniqueConstraint(0).addColumnName(0, "bar");
		getJpaProject().synchronizeContextModel();
		
		uniqueConstraints = getJavaEntity().getTable().uniqueConstraints();
		assertTrue(uniqueConstraints.hasNext());
		assertEquals("bar", uniqueConstraints.next().getColumnNames().iterator().next());
		assertEquals("foo", uniqueConstraints.next().getColumnNames().iterator().next());
		assertFalse(uniqueConstraints.hasNext());
	}
	
	public void testUniqueConstraintsSize() throws Exception {
		createTestEntityWithTable();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		assertEquals(0,  getJavaEntity().getTable().uniqueConstraintsSize());

		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation tableAnnotation = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		tableAnnotation.addUniqueConstraint(0).addColumnName(0, "foo");
		tableAnnotation.addUniqueConstraint(1).addColumnName(0, "bar");
		
		getJpaProject().synchronizeContextModel();
		assertEquals(2,  getJavaEntity().getTable().uniqueConstraintsSize());
	}

	public void testAddUniqueConstraint() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		Table table = getJavaEntity().getTable();
		table.addUniqueConstraint(0).addColumnName(0, "FOO");
		table.addUniqueConstraint(0).addColumnName(0, "BAR");
		table.addUniqueConstraint(0).addColumnName(0, "BAZ");
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation tableAnnotation = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		ListIterator<UniqueConstraintAnnotation> uniqueConstraints = tableAnnotation.uniqueConstraints();
		
		assertEquals("BAZ", uniqueConstraints.next().columnNames().next());
		assertEquals("BAR", uniqueConstraints.next().columnNames().next());
		assertEquals("FOO", uniqueConstraints.next().columnNames().next());
		assertFalse(uniqueConstraints.hasNext());
	}
	
	public void testAddUniqueConstraint2() throws Exception {
		createTestEntityWithTable();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		Table table = getJavaEntity().getTable();
		table.addUniqueConstraint(0).addColumnName(0, "FOO");
		table.addUniqueConstraint(1).addColumnName(0, "BAR");
		table.addUniqueConstraint(0).addColumnName(0, "BAZ");
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation tableAnnotation = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		ListIterator<UniqueConstraintAnnotation> uniqueConstraints = tableAnnotation.uniqueConstraints();
		
		assertEquals("BAZ", uniqueConstraints.next().columnNames().next());
		assertEquals("FOO", uniqueConstraints.next().columnNames().next());
		assertEquals("BAR", uniqueConstraints.next().columnNames().next());
		assertFalse(uniqueConstraints.hasNext());
	}
	
	public void testRemoveUniqueConstraint() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		JavaTable table = getJavaEntity().getTable();
		table.addUniqueConstraint(0).addColumnName(0, "FOO");
		table.addUniqueConstraint(1).addColumnName(0, "BAR");
		table.addUniqueConstraint(2).addColumnName(0, "BAZ");
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation tableAnnotation = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		
		assertEquals(3, tableAnnotation.uniqueConstraintsSize());

		table.removeUniqueConstraint(1);
		
		ListIterator<UniqueConstraintAnnotation> uniqueConstraintAnnotations = tableAnnotation.uniqueConstraints();
		assertEquals("FOO", uniqueConstraintAnnotations.next().columnNames().next());		
		assertEquals("BAZ", uniqueConstraintAnnotations.next().columnNames().next());
		assertFalse(uniqueConstraintAnnotations.hasNext());
		
		Iterator<JavaUniqueConstraint> uniqueConstraints = table.uniqueConstraints();
		assertEquals("FOO", uniqueConstraints.next().getColumnNames().iterator().next());		
		assertEquals("BAZ", uniqueConstraints.next().getColumnNames().iterator().next());
		assertFalse(uniqueConstraints.hasNext());
	
		
		table.removeUniqueConstraint(1);
		uniqueConstraintAnnotations = tableAnnotation.uniqueConstraints();
		assertEquals("FOO", uniqueConstraintAnnotations.next().columnNames().next());		
		assertFalse(uniqueConstraintAnnotations.hasNext());

		uniqueConstraints = table.uniqueConstraints();
		assertEquals("FOO", uniqueConstraints.next().getColumnNames().iterator().next());		
		assertFalse(uniqueConstraints.hasNext());

		
		table.removeUniqueConstraint(0);
		uniqueConstraintAnnotations = tableAnnotation.uniqueConstraints();
		assertFalse(uniqueConstraintAnnotations.hasNext());
		uniqueConstraints = table.uniqueConstraints();
		assertFalse(uniqueConstraints.hasNext());
	}
	
	public void testMoveUniqueConstraint() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		JavaTable table = getJavaEntity().getTable();
		table.addUniqueConstraint(0).addColumnName(0, "FOO");
		table.addUniqueConstraint(1).addColumnName(0, "BAR");
		table.addUniqueConstraint(2).addColumnName(0, "BAZ");
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation tableAnnotation = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
		
		assertEquals(3, tableAnnotation.uniqueConstraintsSize());
		
		
		table.moveUniqueConstraint(2, 0);
		ListIterator<JavaUniqueConstraint> uniqueConstraints = table.uniqueConstraints();
		assertEquals("BAR", uniqueConstraints.next().getColumnNames().iterator().next());
		assertEquals("BAZ", uniqueConstraints.next().getColumnNames().iterator().next());
		assertEquals("FOO", uniqueConstraints.next().getColumnNames().iterator().next());

		ListIterator<UniqueConstraintAnnotation> uniqueConstraintAnnotations = tableAnnotation.uniqueConstraints();
		assertEquals("BAR", uniqueConstraintAnnotations.next().columnNames().next());
		assertEquals("BAZ", uniqueConstraintAnnotations.next().columnNames().next());
		assertEquals("FOO", uniqueConstraintAnnotations.next().columnNames().next());


		table.moveUniqueConstraint(0, 1);
		uniqueConstraints = table.uniqueConstraints();
		assertEquals("BAZ", uniqueConstraints.next().getColumnNames().iterator().next());
		assertEquals("BAR", uniqueConstraints.next().getColumnNames().iterator().next());
		assertEquals("FOO", uniqueConstraints.next().getColumnNames().iterator().next());

		uniqueConstraintAnnotations = tableAnnotation.uniqueConstraints();
		assertEquals("BAZ", uniqueConstraintAnnotations.next().columnNames().next());
		assertEquals("BAR", uniqueConstraintAnnotations.next().columnNames().next());
		assertEquals("FOO", uniqueConstraintAnnotations.next().columnNames().next());
	}
	
	public void testUpdateUniqueConstraints() throws Exception {
		createTestEntityWithTable();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		JavaTable table = getJavaEntity().getTable();
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		TableAnnotation tableAnnotation = (TableAnnotation) typeResource.getAnnotation(JPA.TABLE);
	
		tableAnnotation.addUniqueConstraint(0).addColumnName("FOO");
		tableAnnotation.addUniqueConstraint(1).addColumnName("BAR");
		tableAnnotation.addUniqueConstraint(2).addColumnName("BAZ");
		getJpaProject().synchronizeContextModel();
		
		ListIterator<JavaUniqueConstraint> uniqueConstraints = table.uniqueConstraints();
		assertEquals("FOO", uniqueConstraints.next().getColumnNames().iterator().next());
		assertEquals("BAR", uniqueConstraints.next().getColumnNames().iterator().next());
		assertEquals("BAZ", uniqueConstraints.next().getColumnNames().iterator().next());
		assertFalse(uniqueConstraints.hasNext());
		
		tableAnnotation.moveUniqueConstraint(2, 0);
		getJpaProject().synchronizeContextModel();
		uniqueConstraints = table.uniqueConstraints();
		assertEquals("BAR", uniqueConstraints.next().getColumnNames().iterator().next());
		assertEquals("BAZ", uniqueConstraints.next().getColumnNames().iterator().next());
		assertEquals("FOO", uniqueConstraints.next().getColumnNames().iterator().next());
		assertFalse(uniqueConstraints.hasNext());
	
		tableAnnotation.moveUniqueConstraint(0, 1);
		getJpaProject().synchronizeContextModel();
		uniqueConstraints = table.uniqueConstraints();
		assertEquals("BAZ", uniqueConstraints.next().getColumnNames().iterator().next());
		assertEquals("BAR", uniqueConstraints.next().getColumnNames().iterator().next());
		assertEquals("FOO", uniqueConstraints.next().getColumnNames().iterator().next());
		assertFalse(uniqueConstraints.hasNext());
	
		tableAnnotation.removeUniqueConstraint(1);
		getJpaProject().synchronizeContextModel();
		uniqueConstraints = table.uniqueConstraints();
		assertEquals("BAZ", uniqueConstraints.next().getColumnNames().iterator().next());
		assertEquals("FOO", uniqueConstraints.next().getColumnNames().iterator().next());
		assertFalse(uniqueConstraints.hasNext());
	
		tableAnnotation.removeUniqueConstraint(1);
		getJpaProject().synchronizeContextModel();
		uniqueConstraints = table.uniqueConstraints();
		assertEquals("BAZ", uniqueConstraints.next().getColumnNames().iterator().next());
		assertFalse(uniqueConstraints.hasNext());
		
		tableAnnotation.removeUniqueConstraint(0);
		getJpaProject().synchronizeContextModel();
		uniqueConstraints = table.uniqueConstraints();
		assertFalse(uniqueConstraints.hasNext());
	}
	
	public void testAbstractEntityGetDefaultNameTablePerClassInheritance() throws Exception {
		createAbstractTestEntity();
		createTestSubType();
		
		addXmlClassRef(PACKAGE_NAME + ".AnnotationTestTypeChild");
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		ListIterator<ClassRef> specifiedClassRefs = getPersistenceUnit().specifiedClassRefs();
		Entity concreteEntity = (Entity) specifiedClassRefs.next().getJavaPersistentType().getMapping();
		assertEquals("AnnotationTestTypeChild", concreteEntity.getName());
		
		Entity abstractEntity = (Entity) specifiedClassRefs.next().getJavaPersistentType().getMapping();
		assertEquals(TYPE_NAME, abstractEntity.getName());
		
		
		assertEquals(InheritanceType.TABLE_PER_CLASS, abstractEntity.getSpecifiedInheritanceStrategy());
		assertEquals(null, concreteEntity.getSpecifiedInheritanceStrategy());
		assertEquals(InheritanceType.TABLE_PER_CLASS, concreteEntity.getDefaultInheritanceStrategy());
		
		
		assertEquals(null, abstractEntity.getTable().getDefaultName());
		assertEquals(null, abstractEntity.getTable().getDefaultCatalog());
		assertEquals(null, abstractEntity.getTable().getDefaultSchema());
		
		
		assertEquals("AnnotationTestTypeChild", concreteEntity.getTable().getDefaultName());
		assertEquals(null, concreteEntity.getTable().getDefaultCatalog());
		assertEquals(null, concreteEntity.getTable().getDefaultSchema());
	}

}