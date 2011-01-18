/*******************************************************************************
 *  Copyright (c) 2009, 2010  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink2_0.core.tests.internal.context.orm;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.FetchType;
import org.eclipse.jpt.core.context.JoinColumn;
import org.eclipse.jpt.core.context.JoinTable;
import org.eclipse.jpt.core.context.java.JavaEntity;
import org.eclipse.jpt.core.context.java.JavaJoinColumn;
import org.eclipse.jpt.core.context.java.JavaJoinTable;
import org.eclipse.jpt.core.context.orm.OrmEntity;
import org.eclipse.jpt.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.context.orm.OrmReadOnlyPersistentAttribute;
import org.eclipse.jpt.core.jpa2.context.Cascade2_0;
import org.eclipse.jpt.core.jpa2.context.ManyToOneMapping2_0;
import org.eclipse.jpt.core.jpa2.context.ManyToOneRelationshipReference2_0;
import org.eclipse.jpt.core.jpa2.context.java.JavaManyToOneMapping2_0;
import org.eclipse.jpt.core.jpa2.context.java.JavaManyToOneRelationshipReference2_0;
import org.eclipse.jpt.core.jpa2.context.orm.OrmManyToOneMapping2_0;
import org.eclipse.jpt.core.jpa2.context.orm.OrmManyToOneRelationshipReference2_0;
import org.eclipse.jpt.core.jpa2.context.orm.OrmOneToOneMapping2_0;
import org.eclipse.jpt.core.jpa2.resource.java.JPA2_0;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.orm.OrmFactory;
import org.eclipse.jpt.core.resource.orm.XmlEntity;
import org.eclipse.jpt.core.resource.orm.XmlManyToOne;
import org.eclipse.jpt.core.resource.orm.XmlOneToOne;
import org.eclipse.jpt.core.resource.orm.v2_0.XmlDerivedId_2_0;
import org.eclipse.jpt.core.resource.orm.v2_0.XmlMapsId_2_0;
import org.eclipse.jpt.core.tests.internal.projects.TestJavaProject.SourceWriter;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkJoinFetchMapping;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkJoinFetchType;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLink;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;

@SuppressWarnings("nls")
public class Eclipselink2_0OrmManyToOneMappingTests
	extends EclipseLink2_0OrmContextModelTestCase
{
	public Eclipselink2_0OrmManyToOneMappingTests(String name) {
		super(name);
	}
	
	
	private void createTestEntityWithIdDerivedIdentity() throws Exception {
		createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.MANY_TO_ONE, JPA.ID);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append(CR);
				sb.append("    @ManyToOne @Id").append(CR);				
				sb.append("    private " + TYPE_NAME + " manyToOne;").append(CR);
				sb.append(CR);				
			}
		});
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		for (OrmReadOnlyPersistentAttribute each : CollectionTools.iterable(ormPersistentType.attributes())) {
			each.convertToSpecified();
		}
	}
	
	private void createTestEntityWithMapsIdDerivedIdentity() throws Exception {
		createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.MANY_TO_ONE, JPA2_0.MAPS_ID);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append(CR);
				sb.append("    @ManyToOne @MapsId").append(CR);				
				sb.append("    private " + TYPE_NAME + " manyToOne;").append(CR);
				sb.append(CR);				
			}
		});
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		for (OrmReadOnlyPersistentAttribute each : CollectionTools.iterable(ormPersistentType.attributes())) {
			each.convertToSpecified();
		}
	}
	
	private ICompilationUnit createTestEntityWithValidManyToOneMapping() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.MANY_TO_ONE, JPA.ID);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append(CR);
				sb.append("    @ManyToOne").append(CR);				
				sb.append("    private Address address;").append(CR);
				sb.append(CR);
				sb.append("    @Id").append(CR);				
			}
		});
	}
	
	private void createTestTargetEntityAddress() throws Exception {
		SourceWriter sourceWriter = new SourceWriter() {
			public void appendSourceTo(StringBuilder sb) {
				sb.append(CR);
					sb.append("import ");
					sb.append(JPA.ENTITY);
					sb.append(";");
					sb.append(CR);
					sb.append("import ");
					sb.append(JPA.ID);
					sb.append(";");
					sb.append(CR);
				sb.append(CR);
				sb.append("@Entity");
				sb.append(CR);
				sb.append("public class ").append("Address").append(" ");
				sb.append("{").append(CR);
				sb.append(CR);
				sb.append("    @Id").append(CR);
				sb.append("    private int id;").append(CR);
				sb.append(CR);
				sb.append("    private String city;").append(CR);
				sb.append(CR);
				sb.append("    private State state;").append(CR);
				sb.append(CR);
				sb.append("    private int zip;").append(CR);
				sb.append(CR);
				sb.append("}").append(CR);
		}
		};
		this.javaProject.createCompilationUnit(PACKAGE_NAME, "Address.java", sourceWriter);
	}

	private void createTestEntityWithManyToOneMapping() throws Exception {
		createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.MANY_TO_ONE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@ManyToOne").append(CR);
			}
		});
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		for (OrmReadOnlyPersistentAttribute each : CollectionTools.iterable(ormPersistentType.attributes())) {
			each.convertToSpecified();
		}
	}
	
	private ICompilationUnit createTestEntityManyToOneMapping() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.MANY_TO_ONE, JPA.JOIN_COLUMN, JPA.FETCH_TYPE, JPA.CASCADE_TYPE, EclipseLink.JOIN_FETCH, EclipseLink.JOIN_FETCH_TYPE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}

			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append(CR);
				sb.append("    @ManyToOne(fetch=FetchType.LAZY, optional=false, targetEntity=Address.class, cascade={CascadeType.ALL, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH})");
				sb.append(CR);
				sb.append("    @JoinColumn(name=\"MY_COLUMN\", referencedColumnName=\"MY_REFERENCED_COLUMN\", unique=true, nullable=false, insertable=false, updatable=false, columnDefinition=\"COLUMN_DEFINITION\", table=\"MY_TABLE\")");
				sb.append(CR);
				sb.append("    @JoinFetch(JoinFetchType.INNER)");
				sb.append(CR);
				sb.append("    private Address address;").append(CR);
				sb.append(CR);
				sb.append("    @Id");				
			}
		});
	}	

	public void testUpdateDerivedId() throws Exception {
		createTestEntityWithIdDerivedIdentity();
		OrmPersistentType contextType = getEntityMappings().getPersistentType(FULLY_QUALIFIED_TYPE_NAME);
		OrmReadOnlyPersistentAttribute contextAttribute = contextType.getAttributeNamed("manyToOne");
		OrmManyToOneMapping2_0 contextMapping = (OrmManyToOneMapping2_0) contextAttribute.getMapping();
		XmlEntity resourceEntity = (XmlEntity) contextType.getMapping().getXmlTypeMapping();
		XmlDerivedId_2_0 resourceMapping = resourceEntity.getAttributes().getManyToOnes().get(0);
		
		assertNull(resourceMapping.getId());
		assertFalse(contextMapping.getDerivedIdentity().getIdDerivedIdentityStrategy().getValue());
		
		resourceMapping.setId(Boolean.FALSE);
		assertEquals(Boolean.FALSE, resourceMapping.getId());
		assertFalse(contextMapping.getDerivedIdentity().getIdDerivedIdentityStrategy().getValue());
		
		resourceMapping.setId(Boolean.TRUE);
		assertEquals(Boolean.TRUE, resourceMapping.getId());
		assertTrue(contextMapping.getDerivedIdentity().getIdDerivedIdentityStrategy().getValue());
		
		resourceMapping.setId(null);
		assertNull(resourceMapping.getId());
		assertFalse(contextMapping.getDerivedIdentity().getIdDerivedIdentityStrategy().getValue());
	}
	
	public void testSetDerivedId() throws Exception {
		createTestEntityWithIdDerivedIdentity();
		OrmPersistentType contextType = getEntityMappings().getPersistentType(FULLY_QUALIFIED_TYPE_NAME);
		OrmReadOnlyPersistentAttribute contextAttribute = contextType.getAttributeNamed("manyToOne");
		OrmManyToOneMapping2_0 contextMapping = (OrmManyToOneMapping2_0) contextAttribute.getMapping();
		XmlEntity resourceEntity = (XmlEntity) contextType.getMapping().getXmlTypeMapping();
		XmlDerivedId_2_0 resourceMapping = resourceEntity.getAttributes().getManyToOnes().get(0);
		
		assertNull(resourceMapping.getId());
		assertFalse(contextMapping.getDerivedIdentity().getIdDerivedIdentityStrategy().getValue());
		
		contextMapping.getDerivedIdentity().getIdDerivedIdentityStrategy().setValue(true);
		assertEquals(Boolean.TRUE, resourceMapping.getId());
		assertTrue(contextMapping.getDerivedIdentity().getIdDerivedIdentityStrategy().getValue());
		
		contextMapping.getDerivedIdentity().getIdDerivedIdentityStrategy().setValue(false);
		assertNull(resourceMapping.getId());
		assertFalse(contextMapping.getDerivedIdentity().getIdDerivedIdentityStrategy().getValue());
	}
	
	public void testUpdateMapsId() throws Exception {
		createTestEntityWithMapsIdDerivedIdentity();
		OrmPersistentType contextType = getEntityMappings().getPersistentType(FULLY_QUALIFIED_TYPE_NAME);
		OrmReadOnlyPersistentAttribute contextAttribute = contextType.getAttributeNamed("manyToOne");
		OrmManyToOneMapping2_0 contextMapping = (OrmManyToOneMapping2_0) contextAttribute.getMapping();
		XmlEntity resourceEntity = (XmlEntity) contextType.getMapping().getXmlTypeMapping();
		XmlMapsId_2_0 resourceMapping = resourceEntity.getAttributes().getManyToOnes().get(0);
		
		assertNull(resourceMapping.getMapsId());
		assertNull(contextMapping.getDerivedIdentity().getMapsIdDerivedIdentityStrategy().getSpecifiedValue());
		
		resourceMapping.setMapsId("foo");
		assertEquals("foo", resourceMapping.getMapsId());
		assertEquals("foo", contextMapping.getDerivedIdentity().getMapsIdDerivedIdentityStrategy().getSpecifiedValue());
		
		resourceMapping.setMapsId("bar");
		assertEquals("bar", resourceMapping.getMapsId());
		assertEquals("bar", contextMapping.getDerivedIdentity().getMapsIdDerivedIdentityStrategy().getSpecifiedValue());
		
		resourceMapping.setMapsId(null);
		assertNull(resourceMapping.getMapsId());
		assertNull(contextMapping.getDerivedIdentity().getMapsIdDerivedIdentityStrategy().getSpecifiedValue());
	}
	
	public void testSetMapsId() throws Exception {
		createTestEntityWithMapsIdDerivedIdentity();
		OrmPersistentType contextType = getEntityMappings().getPersistentType(FULLY_QUALIFIED_TYPE_NAME);
		OrmReadOnlyPersistentAttribute contextAttribute = contextType.getAttributeNamed("manyToOne");
		OrmManyToOneMapping2_0 contextMapping = (OrmManyToOneMapping2_0) contextAttribute.getMapping();
		XmlEntity resourceEntity = (XmlEntity) contextType.getMapping().getXmlTypeMapping();
		XmlMapsId_2_0 resourceMapping = resourceEntity.getAttributes().getManyToOnes().get(0);
		
		assertNull(resourceMapping.getMapsId());
		assertNull(contextMapping.getDerivedIdentity().getMapsIdDerivedIdentityStrategy().getSpecifiedValue());
		
		contextMapping.getDerivedIdentity().getMapsIdDerivedIdentityStrategy().setSpecifiedValue("foo");
		assertEquals("foo", resourceMapping.getMapsId());
		assertEquals("foo", contextMapping.getDerivedIdentity().getMapsIdDerivedIdentityStrategy().getSpecifiedValue());
		
		contextMapping.getDerivedIdentity().getMapsIdDerivedIdentityStrategy().setSpecifiedValue("bar");
		assertEquals("bar", resourceMapping.getMapsId());
		assertEquals("bar", contextMapping.getDerivedIdentity().getMapsIdDerivedIdentityStrategy().getSpecifiedValue());
		
		contextMapping.getDerivedIdentity().getMapsIdDerivedIdentityStrategy().setSpecifiedValue(null);
		assertNull(resourceMapping.getMapsId());
		assertNull(contextMapping.getDerivedIdentity().getMapsIdDerivedIdentityStrategy().getSpecifiedValue());
	}
	
	public void testMorphMapping() throws Exception {
		createTestEntityWithIdDerivedIdentity();
		OrmPersistentType contextType = getEntityMappings().getPersistentType(FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentAttribute contextAttribute = (OrmPersistentAttribute) contextType.getAttributeNamed("manyToOne");
		XmlEntity resourceEntity = (XmlEntity) contextType.getMapping().getXmlTypeMapping();
		
		XmlManyToOne resourceManyToOne = resourceEntity.getAttributes().getManyToOnes().get(0);
		resourceManyToOne.setId(Boolean.TRUE);
		assertEquals(Boolean.TRUE, resourceManyToOne.getId());
		assertTrue(((OrmManyToOneMapping2_0) contextAttribute.getMapping()).
				getDerivedIdentity().getIdDerivedIdentityStrategy().getValue());
		assertNull(resourceManyToOne.getMapsId());
		assertNull(((OrmManyToOneMapping2_0) contextAttribute.getMapping()).
				getDerivedIdentity().getMapsIdDerivedIdentityStrategy().getSpecifiedValue());
		
		contextAttribute.setMappingKey(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY);
		XmlOneToOne resourceOneToOne = resourceEntity.getAttributes().getOneToOnes().get(0);
		assertEquals(Boolean.TRUE, resourceOneToOne.getId());
		assertTrue(((OrmOneToOneMapping2_0) contextAttribute.getMapping()).
				getDerivedIdentity().getIdDerivedIdentityStrategy().getValue());
		assertNull(resourceOneToOne.getMapsId());
		assertNull(((OrmOneToOneMapping2_0) contextAttribute.getMapping()).
				getDerivedIdentity().getMapsIdDerivedIdentityStrategy().getSpecifiedValue());

		contextAttribute.setMappingKey(MappingKeys.MANY_TO_ONE_ATTRIBUTE_MAPPING_KEY);
		resourceManyToOne.setId(Boolean.TRUE);
		assertEquals(Boolean.TRUE, resourceManyToOne.getId());
		assertTrue(((OrmManyToOneMapping2_0) contextAttribute.getMapping()).
				getDerivedIdentity().getIdDerivedIdentityStrategy().getValue());
		assertNull(resourceManyToOne.getMapsId());
		assertNull(((OrmManyToOneMapping2_0) contextAttribute.getMapping()).
				getDerivedIdentity().getMapsIdDerivedIdentityStrategy().getSpecifiedValue());
	}

	public void testVirtualJoinTable() throws Exception {
		createTestEntityWithValidManyToOneMapping();

		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);

		OrmReadOnlyPersistentAttribute ormPersistentAttribute = ormPersistentType.getAttributeNamed("address");
		JavaManyToOneMapping2_0 javaManyToOneMapping = ((JavaManyToOneMapping2_0) ormPersistentAttribute.getJavaPersistentAttribute().getMapping());
		ManyToOneMapping2_0 virtualManyToOneMapping = (ManyToOneMapping2_0) ormPersistentAttribute.getMapping();
		((ManyToOneRelationshipReference2_0) javaManyToOneMapping.getRelationshipReference()).setJoinTableJoiningStrategy();
		JoinTable ormJoinTable = ((ManyToOneRelationshipReference2_0) virtualManyToOneMapping.getRelationshipReference()).getJoinTableJoiningStrategy().getJoinTable();

		assertTrue(ormPersistentAttribute.isVirtual());
		assertEquals(null, ormJoinTable.getSpecifiedName());

		createTestTargetEntityAddress();
		getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, PACKAGE_NAME + ".Address");

		assertEquals(TYPE_NAME + "_Address", ormJoinTable.getName());
		assertNull(ormJoinTable.getSpecifiedCatalog());
		assertNull(ormJoinTable.getSpecifiedSchema());
		assertEquals(0, ormJoinTable.specifiedJoinColumnsSize());
		assertEquals(0, ormJoinTable.specifiedInverseJoinColumnsSize());
		JoinColumn ormJoinColumn = ormJoinTable.getDefaultJoinColumn();
		assertEquals(TYPE_NAME + "_Address", ormJoinColumn.getDefaultTable());
		assertEquals(TYPE_NAME + "_id", ormJoinColumn.getDefaultName());
		assertEquals("id", ormJoinColumn.getDefaultReferencedColumnName());
		JoinColumn inverseOrmJoinColumn = ormJoinTable.getDefaultInverseJoinColumn();
		assertEquals(TYPE_NAME + "_Address", inverseOrmJoinColumn.getDefaultTable());
		assertEquals("address_id", inverseOrmJoinColumn.getDefaultName());
		assertEquals("id", inverseOrmJoinColumn.getDefaultReferencedColumnName());

		JavaJoinTable javaJoinTable = ((JavaManyToOneRelationshipReference2_0) javaManyToOneMapping.getRelationshipReference()).getJoinTableJoiningStrategy().getJoinTable();
		javaJoinTable.setSpecifiedName("FOO");
		javaJoinTable.setSpecifiedCatalog("CATALOG");
		javaJoinTable.setSpecifiedSchema("SCHEMA");
		JavaJoinColumn javaJoinColumn = javaJoinTable.addSpecifiedJoinColumn(0);
		javaJoinColumn.setSpecifiedName("NAME");
		javaJoinColumn.setSpecifiedReferencedColumnName("REFERENCED_NAME");
		JavaJoinColumn inverseJavaJoinColumn = javaJoinTable.addSpecifiedInverseJoinColumn(0);
		inverseJavaJoinColumn.setSpecifiedName("INVERSE_NAME");
		inverseJavaJoinColumn.setSpecifiedReferencedColumnName("INVERSE_REFERENCED_NAME");

		assertEquals("FOO", ormJoinTable.getSpecifiedName());
		assertEquals("CATALOG", ormJoinTable.getSpecifiedCatalog());
		assertEquals("SCHEMA", ormJoinTable.getSpecifiedSchema());
		assertEquals(1, ormJoinTable.specifiedJoinColumnsSize());
		assertEquals(1, ormJoinTable.specifiedInverseJoinColumnsSize());
		ormJoinColumn = ormJoinTable.specifiedJoinColumns().next();
		assertEquals("NAME", ormJoinColumn.getSpecifiedName());
		assertEquals("REFERENCED_NAME", ormJoinColumn.getSpecifiedReferencedColumnName());
		inverseOrmJoinColumn = ormJoinTable.specifiedInverseJoinColumns().next();
		assertEquals("INVERSE_NAME", inverseOrmJoinColumn.getSpecifiedName());
		assertEquals("INVERSE_REFERENCED_NAME", inverseOrmJoinColumn.getSpecifiedReferencedColumnName());
	}

	public void testUpdateDefaultNameFromJavaTable() throws Exception {
		createTestEntityWithValidManyToOneMapping();

		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);

		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedAttribute(MappingKeys.MANY_TO_ONE_ATTRIBUTE_MAPPING_KEY, "address");
		OrmManyToOneMapping2_0 ormManyToOneMapping = (OrmManyToOneMapping2_0) ormPersistentAttribute.getMapping();
		((ManyToOneRelationshipReference2_0) ormManyToOneMapping.getRelationshipReference()).setJoinTableJoiningStrategy();
		JoinTable ormJoinTable = ((OrmManyToOneRelationshipReference2_0) ormManyToOneMapping.getRelationshipReference()).getJoinTableJoiningStrategy().getJoinTable();
		assertEquals(null, ormJoinTable.getDefaultName());

		createTestTargetEntityAddress();
		OrmPersistentType targetPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, PACKAGE_NAME + ".Address");
		assertEquals(TYPE_NAME + "_Address", ormJoinTable.getDefaultName());


		((JavaEntity) targetPersistentType.getJavaPersistentType().getMapping()).getTable().setSpecifiedName("FOO");
		assertEquals(TYPE_NAME + "_FOO", ormJoinTable.getDefaultName());

		((JavaEntity) ormPersistentType.getJavaPersistentType().getMapping()).getTable().setSpecifiedName("BAR");
		assertEquals("BAR_FOO", ormJoinTable.getDefaultName());

		JavaManyToOneMapping2_0 javaManyToOneMapping = (JavaManyToOneMapping2_0) ormPersistentType.getJavaPersistentType().getAttributeNamed("address").getMapping();
		((ManyToOneRelationshipReference2_0) javaManyToOneMapping.getRelationshipReference()).setJoinTableJoiningStrategy();
		((ManyToOneRelationshipReference2_0) javaManyToOneMapping.getRelationshipReference()).getJoinTableJoiningStrategy().getJoinTable().setSpecifiedName("JAVA_JOIN_TABLE");

		assertEquals("BAR_FOO", ormJoinTable.getDefaultName());


		//set metadata-complete to true, will ignore java annotation settings
		getEntityMappings().getPersistenceUnitMetadata().setXmlMappingMetadataComplete(true);
		//ormPersistentType.getMapping().setSpecifiedMetadataComplete(Boolean.TRUE);
		assertEquals(TYPE_NAME + "_Address", ormJoinTable.getDefaultName());


		getEntityMappings().getPersistenceUnitMetadata().setXmlMappingMetadataComplete(false);
		//remove m-m mapping from the orm.xml file
		ormPersistentAttribute.convertToVirtual();
		//ormPersistentType.getMapping().setSpecifiedMetadataComplete(null);
		OrmReadOnlyPersistentAttribute ormPersistentAttribute2 = ormPersistentType.getAttributeNamed("address");
		ManyToOneMapping2_0 virtualManyToOneMapping = (ManyToOneMapping2_0) ormPersistentAttribute2.getMapping();
		ormJoinTable = ((ManyToOneRelationshipReference2_0) virtualManyToOneMapping.getRelationshipReference()).getJoinTableJoiningStrategy().getJoinTable();
		assertTrue(ormPersistentAttribute2.isVirtual());
		assertEquals("JAVA_JOIN_TABLE", ormJoinTable.getSpecifiedName());//specifiedName since this is a virtual mapping now

		((ManyToOneRelationshipReference2_0) javaManyToOneMapping.getRelationshipReference()).getJoinTableJoiningStrategy().removeStrategy();
		ormJoinTable = ((ManyToOneRelationshipReference2_0) virtualManyToOneMapping.getRelationshipReference()).getJoinTableJoiningStrategy().getJoinTable();
		assertNull(ormJoinTable);
		((ManyToOneRelationshipReference2_0) javaManyToOneMapping.getRelationshipReference()).setJoinTableJoiningStrategy();
		ormJoinTable = ((ManyToOneRelationshipReference2_0) virtualManyToOneMapping.getRelationshipReference()).getJoinTableJoiningStrategy().getJoinTable();
		assertEquals("BAR_FOO", ormJoinTable.getName());
		assertEquals("BAR_FOO", ormJoinTable.getDefaultName());

		((OrmEntity) ormPersistentType.getMapping()).getTable().setSpecifiedName("ORM_TABLE_NAME");
		assertEquals("ORM_TABLE_NAME_FOO", ormJoinTable.getDefaultName());

		((OrmEntity) targetPersistentType.getMapping()).getTable().setSpecifiedName("ORM_TARGET");
		assertEquals("ORM_TABLE_NAME_ORM_TARGET", ormJoinTable.getDefaultName());
	}

	public void testModifyPredominantJoiningStrategy() throws Exception {
		createTestEntityWithManyToOneMapping();
		OrmPersistentType contextType = getEntityMappings().getPersistentType(FULLY_QUALIFIED_TYPE_NAME);
		OrmReadOnlyPersistentAttribute contextAttribute = contextType.getAttributeNamed("id");
		OrmManyToOneMapping2_0 contextMapping = (OrmManyToOneMapping2_0) contextAttribute.getMapping();
		OrmManyToOneRelationshipReference2_0 relationshipReference = (OrmManyToOneRelationshipReference2_0) contextMapping.getRelationshipReference();
		XmlEntity resourceEntity = (XmlEntity) contextType.getMapping().getXmlTypeMapping();
		XmlManyToOne resourceMapping = resourceEntity.getAttributes().getManyToOnes().get(0);

		assertTrue(resourceMapping.getJoinColumns().isEmpty());
		assertNull(resourceMapping.getJoinTable());
		assertTrue(relationshipReference.usesJoinColumnJoiningStrategy());
		assertFalse(relationshipReference.usesJoinTableJoiningStrategy());

		relationshipReference.setJoinColumnJoiningStrategy();
		assertTrue(resourceMapping.getJoinColumns().isEmpty());
		assertNull(resourceMapping.getJoinTable());
		assertTrue(relationshipReference.usesJoinColumnJoiningStrategy());
		assertFalse(relationshipReference.usesJoinTableJoiningStrategy());

		relationshipReference.setJoinTableJoiningStrategy();
		assertTrue(resourceMapping.getJoinColumns().isEmpty());
		assertNotNull(resourceMapping.getJoinTable());
		assertFalse(relationshipReference.usesJoinColumnJoiningStrategy());
		assertTrue(relationshipReference.usesJoinTableJoiningStrategy());

		relationshipReference.setJoinColumnJoiningStrategy();
		assertTrue(resourceMapping.getJoinColumns().isEmpty());
		assertNull(resourceMapping.getJoinTable());
		assertTrue(relationshipReference.usesJoinColumnJoiningStrategy());
		assertFalse(relationshipReference.usesJoinTableJoiningStrategy());
	}

	public void testUpdatePredominantJoiningStrategy() throws Exception {
		createTestEntityWithManyToOneMapping();
		OrmPersistentType contextType = getEntityMappings().getPersistentType(FULLY_QUALIFIED_TYPE_NAME);
		OrmReadOnlyPersistentAttribute contextAttribute = contextType.getAttributeNamed("id");
		OrmManyToOneMapping2_0 contextMapping = (OrmManyToOneMapping2_0) contextAttribute.getMapping();
		OrmManyToOneRelationshipReference2_0 relationshipReference = (OrmManyToOneRelationshipReference2_0) contextMapping.getRelationshipReference();
		XmlEntity resourceEntity = (XmlEntity) contextType.getMapping().getXmlTypeMapping();
		XmlManyToOne resourceMapping = resourceEntity.getAttributes().getManyToOnes().get(0);

		assertTrue(resourceMapping.getJoinColumns().isEmpty());
		assertNull(resourceMapping.getJoinTable());
		assertTrue(relationshipReference.usesJoinColumnJoiningStrategy());
		assertFalse(relationshipReference.usesJoinTableJoiningStrategy());

		resourceMapping.getJoinColumns().add(OrmFactory.eINSTANCE.createXmlJoinColumn());
		getJpaProject().synchronizeContextModel();
		assertFalse(resourceMapping.getJoinColumns().isEmpty());
		assertNull(resourceMapping.getJoinTable());
		assertTrue(relationshipReference.usesJoinColumnJoiningStrategy());
		assertFalse(relationshipReference.usesJoinTableJoiningStrategy());

		resourceMapping.getJoinColumns().clear();
		getJpaProject().synchronizeContextModel();
		assertTrue(resourceMapping.getJoinColumns().isEmpty());
		assertNull(resourceMapping.getJoinTable());
		assertTrue(relationshipReference.usesJoinColumnJoiningStrategy());
		assertFalse(relationshipReference.usesJoinTableJoiningStrategy());

		resourceMapping.setJoinTable(OrmFactory.eINSTANCE.createXmlJoinTable());
		getJpaProject().synchronizeContextModel();
		assertTrue(resourceMapping.getJoinColumns().isEmpty());
		assertNotNull(resourceMapping.getJoinTable());
		assertFalse(relationshipReference.usesJoinColumnJoiningStrategy());
		assertTrue(relationshipReference.usesJoinTableJoiningStrategy());
	}

	public void testVirtualMappingMetadataCompleteFalse() throws Exception {
		createTestEntityManyToOneMapping();
		createTestTargetEntityAddress();

		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, PACKAGE_NAME + ".Address");
		assertEquals(3, ormPersistentType.virtualAttributesSize());		
		OrmReadOnlyPersistentAttribute virtualPersistentAttribute = ormPersistentType.virtualAttributes().next();

		ManyToOneMapping2_0 virtualManyToOneMapping = (ManyToOneMapping2_0) virtualPersistentAttribute.getMapping();	
		assertEquals("address", virtualManyToOneMapping.getName());
		assertEquals(FetchType.LAZY, virtualManyToOneMapping.getSpecifiedFetch());
		assertEquals(Boolean.FALSE, virtualManyToOneMapping.getSpecifiedOptional());
		assertEquals("Address", virtualManyToOneMapping.getSpecifiedTargetEntity());

		JoinColumn virtualJoinColumn = virtualManyToOneMapping.getRelationshipReference().getJoinColumnJoiningStrategy().specifiedJoinColumns().next();
		assertEquals("MY_COLUMN", virtualJoinColumn.getSpecifiedName());
		assertEquals("MY_REFERENCED_COLUMN", virtualJoinColumn.getSpecifiedReferencedColumnName());
		assertEquals(Boolean.TRUE, virtualJoinColumn.getSpecifiedUnique());
		assertEquals(Boolean.FALSE, virtualJoinColumn.getSpecifiedNullable());
		assertEquals(Boolean.FALSE, virtualJoinColumn.getSpecifiedInsertable());
		assertEquals(Boolean.FALSE, virtualJoinColumn.getSpecifiedUpdatable());
		assertEquals("COLUMN_DEFINITION", virtualJoinColumn.getColumnDefinition());
		assertEquals("MY_TABLE", virtualJoinColumn.getSpecifiedTable());

		Cascade2_0 cascade = (Cascade2_0) virtualManyToOneMapping.getCascade();
		assertTrue(cascade.isAll());
		assertTrue(cascade.isMerge());
		assertTrue(cascade.isPersist());
		assertTrue(cascade.isRemove());
		assertTrue(cascade.isRefresh());
		assertTrue(cascade.isDetach());

		assertEquals(EclipseLinkJoinFetchType.INNER, ((EclipseLinkJoinFetchMapping) virtualManyToOneMapping).getJoinFetch().getValue());
	}

	public void testVirtualMappingMetadataCompleteTrue() throws Exception {
		createTestEntityManyToOneMapping();
		createTestTargetEntityAddress();

		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, PACKAGE_NAME + ".Address");
		ormPersistentType.getMapping().setSpecifiedMetadataComplete(Boolean.TRUE);
		assertEquals(3, ormPersistentType.virtualAttributesSize());		
		OrmReadOnlyPersistentAttribute virtualPersistentAttribute = ormPersistentType.getAttributeNamed("address");

		assertEquals(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY, virtualPersistentAttribute.getMappingKey());
		assertTrue(virtualPersistentAttribute.isVirtual());

		virtualPersistentAttribute.convertToSpecified(MappingKeys.MANY_TO_ONE_ATTRIBUTE_MAPPING_KEY);
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.specifiedAttributes().next();

		OrmManyToOneMapping2_0 ormManyToOneMapping = (OrmManyToOneMapping2_0) ormPersistentAttribute.getMapping();	
		assertEquals("address", ormManyToOneMapping.getName());
		assertEquals(FetchType.EAGER, ormManyToOneMapping.getDefaultFetch());
		assertEquals(true, ormManyToOneMapping.isDefaultOptional());
		assertEquals("test.Address", ormManyToOneMapping.getDefaultTargetEntity());

		Cascade2_0 cascade = (Cascade2_0) ormManyToOneMapping.getCascade();
		assertFalse(cascade.isAll());
		assertFalse(cascade.isMerge());
		assertFalse(cascade.isPersist());
		assertFalse(cascade.isRemove());
		assertFalse(cascade.isRefresh());
		assertFalse(cascade.isDetach());

		assertEquals(null, ((EclipseLinkJoinFetchMapping) ormManyToOneMapping).getJoinFetch().getValue());
	}
}
