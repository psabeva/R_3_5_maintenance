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
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.common.utility.internal.iterables.EmptyIterable;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.AccessType;
import org.eclipse.jpt.core.context.BasicMapping;
import org.eclipse.jpt.core.context.EmbeddedIdMapping;
import org.eclipse.jpt.core.context.EmbeddedMapping;
import org.eclipse.jpt.core.context.EnumType;
import org.eclipse.jpt.core.context.EnumeratedConverter;
import org.eclipse.jpt.core.context.FetchType;
import org.eclipse.jpt.core.context.IdMapping;
import org.eclipse.jpt.core.context.LobConverter;
import org.eclipse.jpt.core.context.ManyToManyMapping;
import org.eclipse.jpt.core.context.ManyToOneMapping;
import org.eclipse.jpt.core.context.OneToManyMapping;
import org.eclipse.jpt.core.context.OneToOneMapping;
import org.eclipse.jpt.core.context.PersistentAttribute;
import org.eclipse.jpt.core.context.TemporalConverter;
import org.eclipse.jpt.core.context.TemporalType;
import org.eclipse.jpt.core.context.TransientMapping;
import org.eclipse.jpt.core.context.VersionMapping;
import org.eclipse.jpt.core.resource.java.BasicAnnotation;
import org.eclipse.jpt.core.resource.java.ColumnAnnotation;
import org.eclipse.jpt.core.resource.java.EmbeddedAnnotation;
import org.eclipse.jpt.core.resource.java.EmbeddedIdAnnotation;
import org.eclipse.jpt.core.resource.java.EnumeratedAnnotation;
import org.eclipse.jpt.core.resource.java.IdAnnotation;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.resource.java.LobAnnotation;
import org.eclipse.jpt.core.resource.java.ManyToOneAnnotation;
import org.eclipse.jpt.core.resource.java.OneToOneAnnotation;
import org.eclipse.jpt.core.resource.java.TemporalAnnotation;
import org.eclipse.jpt.core.resource.java.TransientAnnotation;
import org.eclipse.jpt.core.resource.java.VersionAnnotation;
import org.eclipse.jpt.core.resource.persistence.PersistenceFactory;
import org.eclipse.jpt.core.resource.persistence.XmlMappingFileRef;
import org.eclipse.jpt.core.tests.internal.context.ContextModelTestCase;

@SuppressWarnings("nls")
public class JavaBasicMappingTests extends ContextModelTestCase
{

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

	private ICompilationUnit createTestEntityWithBasicMapping() throws Exception {	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.BASIC);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Basic").append(CR);
			}
		});
	}
	private ICompilationUnit createTestEntityWithBasicMappingFetchOptionalSpecified() throws Exception {	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.BASIC, JPA.FETCH_TYPE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Basic(fetch=FetchType.EAGER, optional=false)").append(CR);
			}
		});
	}

	private ICompilationUnit createTestEntityWithLob() throws Exception {	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.LOB);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Lob").append(CR);
			}
		});
	}

	private ICompilationUnit createTestEntityWithEnumerated() throws Exception {	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.ENUMERATED, JPA.ENUM_TYPE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Enumerated(EnumType.STRING)").append(CR);
			}
		});
	}
	
	private ICompilationUnit createTestEntityWithTemporal() throws Exception {	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.TEMPORAL, JPA.TEMPORAL_TYPE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Temporal(TemporalType.TIMESTAMP)").append(CR);
			}
		});
	}
		
	public JavaBasicMappingTests(String name) {
		super(name);
	}
	
	public void testDefaultBasicGetDefaultFetch() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertEquals(FetchType.EAGER, basicMapping.getDefaultFetch());
	}
	
	public void testSpecifiedBasicGetDefaultFetch() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertEquals(FetchType.EAGER, basicMapping.getDefaultFetch());
	}
	
	public void testGetFetch() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertEquals(FetchType.EAGER, basicMapping.getFetch());
		
		basicMapping.setSpecifiedFetch(FetchType.LAZY);		
		assertEquals(FetchType.LAZY, basicMapping.getFetch());
	}
	
	public void testGetSpecifiedFetch() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertNull(basicMapping.getSpecifiedFetch());
		
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		BasicAnnotation basic = (BasicAnnotation) attributeResource.getAnnotation(JPA.BASIC);
		basic.setFetch(org.eclipse.jpt.core.resource.java.FetchType.LAZY);
		getJpaProject().synchronizeContextModel();
		
		assertEquals(FetchType.LAZY, basicMapping.getSpecifiedFetch());
	}
	
	public void testGetSpecifiedFetch2() throws Exception {
		createTestEntityWithBasicMappingFetchOptionalSpecified();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertEquals(FetchType.EAGER, basicMapping.getSpecifiedFetch());
	}

	public void testSetSpecifiedFetch() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertNull(basicMapping.getSpecifiedFetch());
		
		basicMapping.setSpecifiedFetch(FetchType.LAZY);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		BasicAnnotation basic = (BasicAnnotation) attributeResource.getAnnotation(JPA.BASIC);
		
		assertEquals(org.eclipse.jpt.core.resource.java.FetchType.LAZY, basic.getFetch());
		
		basicMapping.setSpecifiedFetch(null);
		assertNotNull(attributeResource.getAnnotation(JPA.BASIC));
	}
	
	public void testSetSpecifiedFetch2() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		createOrmXmlFile();
		getEntityMappings().getPersistenceUnitMetadata().getPersistenceUnitDefaults().setAccess(AccessType.PROPERTY);
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertNull(basicMapping.getSpecifiedFetch());
		assertTrue(basicMapping.isDefault());
		
		basicMapping.setSpecifiedFetch(FetchType.LAZY);
		
		JavaResourcePersistentType resourceType = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute resourceAttribute = resourceType.persistableProperties().next();
		BasicAnnotation basic = (BasicAnnotation) resourceAttribute.getAnnotation(JPA.BASIC);
		
		assertEquals(org.eclipse.jpt.core.resource.java.FetchType.LAZY, basic.getFetch());
		
		basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertEquals(FetchType.LAZY, basicMapping.getSpecifiedFetch());
		assertFalse(basicMapping.isDefault());

		basicMapping.setSpecifiedFetch(null);
		assertNotNull(resourceAttribute.getAnnotation(JPA.BASIC));
		
		basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertFalse(basicMapping.isDefault());
	}
	
	protected void createOrmXmlFile() throws Exception {
		XmlMappingFileRef mappingFileRef = PersistenceFactory.eINSTANCE.createXmlMappingFileRef();
		mappingFileRef.setFileName(JptCorePlugin.DEFAULT_ORM_XML_RUNTIME_PATH.toString());
		getXmlPersistenceUnit().getMappingFiles().add(mappingFileRef);
		getPersistenceXmlResource().save(null);
	}

	public void testSetBasicToDefault() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		
		assertFalse(basicMapping.isDefault());
		
		basicMapping.getColumn().setSpecifiedName("FOO");
		attributeResource.addAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(LobAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(EnumeratedAnnotation.ANNOTATION_NAME);
		
		assertFalse(basicMapping.isDefault());
		
		persistentAttribute.setMappingKey(MappingKeys.NULL_ATTRIBUTE_MAPPING_KEY);
		
		assertNotSame(basicMapping, persistentAttribute.getMapping());
		
		basicMapping = (BasicMapping) persistentAttribute.getMapping();
		
		assertTrue(basicMapping.isDefault());
		assertEquals("FOO", basicMapping.getColumn().getSpecifiedName());
		assertNotNull(attributeResource.getAnnotation(ColumnAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(LobAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
	}
	
	public void testBasicMorphToDefaultBasic() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		assertFalse(basicMapping.isDefault());
		basicMapping.getColumn().setSpecifiedName("FOO");
		basicMapping.setConverter(EnumeratedConverter.class);
		((EnumeratedConverter) basicMapping.getConverter()).setSpecifiedEnumType(EnumType.STRING);
		attributeResource.addAnnotation(LobAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		basicMapping.setSpecifiedFetch(FetchType.EAGER);
		basicMapping.setSpecifiedOptional(Boolean.FALSE);
		assertFalse(basicMapping.isDefault());
		
		persistentAttribute.setMappingKey(MappingKeys.NULL_ATTRIBUTE_MAPPING_KEY);
		assertEquals("FOO", ((BasicMapping) persistentAttribute.getMapping()).getColumn().getSpecifiedName());
		assertEquals(EnumType.STRING, ((EnumeratedConverter) ((BasicMapping) persistentAttribute.getMapping()).getConverter()).getEnumType());
		
		assertNull(((BasicMapping) persistentAttribute.getMapping()).getSpecifiedFetch());
		assertNull(((BasicMapping) persistentAttribute.getMapping()).getSpecifiedOptional());
		assertNull(attributeResource.getAnnotation(BasicAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(ColumnAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(LobAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
	}
	
	public void testBasicMorphToId() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		assertFalse(basicMapping.isDefault());
		basicMapping.getColumn().setSpecifiedName("FOO");
		basicMapping.setConverter(TemporalConverter.class);
		((TemporalConverter) basicMapping.getConverter()).setTemporalType(TemporalType.TIME);
		attributeResource.addAnnotation(LobAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(EnumeratedAnnotation.ANNOTATION_NAME);
		basicMapping.setSpecifiedFetch(FetchType.EAGER);
		basicMapping.setSpecifiedOptional(Boolean.FALSE);
		assertFalse(basicMapping.isDefault());
		
		persistentAttribute.setMappingKey(MappingKeys.ID_ATTRIBUTE_MAPPING_KEY);
		assertEquals("FOO", ((IdMapping) persistentAttribute.getMapping()).getColumn().getSpecifiedName());
		assertEquals(TemporalType.TIME, ((TemporalConverter) ((IdMapping) persistentAttribute.getMapping()).getConverter()).getTemporalType());
		
		assertNull(attributeResource.getAnnotation(BasicAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(IdAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(ColumnAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(LobAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
	}
	
	public void testBasicMorphToVersion() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		assertFalse(basicMapping.isDefault());
		basicMapping.getColumn().setSpecifiedName("FOO");
		basicMapping.setConverter(TemporalConverter.class);
		((TemporalConverter) basicMapping.getConverter()).setTemporalType(TemporalType.TIME);
		attributeResource.addAnnotation(LobAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(EnumeratedAnnotation.ANNOTATION_NAME);
		assertFalse(basicMapping.isDefault());
		
		persistentAttribute.setMappingKey(MappingKeys.VERSION_ATTRIBUTE_MAPPING_KEY);
		assertEquals("FOO", ((VersionMapping) persistentAttribute.getMapping()).getColumn().getSpecifiedName());
		assertEquals(TemporalType.TIME, ((TemporalConverter) ((VersionMapping) persistentAttribute.getMapping()).getConverter()).getTemporalType());
		
		assertNull(attributeResource.getAnnotation(BasicAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(VersionAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(ColumnAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(LobAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
	}
	
	public void testBasicMorphToEmbedded() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		assertFalse(basicMapping.isDefault());
		basicMapping.getColumn().setSpecifiedName("FOO");
		attributeResource.addAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(LobAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(EnumeratedAnnotation.ANNOTATION_NAME);
		assertFalse(basicMapping.isDefault());
		
		persistentAttribute.setMappingKey(MappingKeys.EMBEDDED_ATTRIBUTE_MAPPING_KEY);
		assertTrue(persistentAttribute.getMapping() instanceof EmbeddedMapping);
		
		assertNull(attributeResource.getAnnotation(BasicAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(EmbeddedAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(ColumnAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(LobAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
	}
	
	public void testBasicMorphToEmbeddedId() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		assertFalse(basicMapping.isDefault());
		basicMapping.getColumn().setSpecifiedName("FOO");
		attributeResource.addAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(LobAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(EnumeratedAnnotation.ANNOTATION_NAME);
		assertFalse(basicMapping.isDefault());
		
		persistentAttribute.setMappingKey(MappingKeys.EMBEDDED_ID_ATTRIBUTE_MAPPING_KEY);
		assertTrue(persistentAttribute.getMapping() instanceof EmbeddedIdMapping);
		
		assertNull(attributeResource.getAnnotation(BasicAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(EmbeddedIdAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(ColumnAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(LobAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
	}

	public void testBasicMorphToTransient() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		assertFalse(basicMapping.isDefault());
		basicMapping.getColumn().setSpecifiedName("FOO");
		attributeResource.addAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(LobAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(EnumeratedAnnotation.ANNOTATION_NAME);
		assertFalse(basicMapping.isDefault());
		
		persistentAttribute.setMappingKey(MappingKeys.TRANSIENT_ATTRIBUTE_MAPPING_KEY);
		assertTrue(persistentAttribute.getMapping() instanceof TransientMapping);
		
		assertNull(attributeResource.getAnnotation(BasicAnnotation.ANNOTATION_NAME));
		assertNotNull(attributeResource.getAnnotation(TransientAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(ColumnAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(LobAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
	}
	
	public void testBasicMorphToOneToOne() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		assertFalse(basicMapping.isDefault());
		basicMapping.getColumn().setSpecifiedName("FOO");
		attributeResource.addAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(LobAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(EnumeratedAnnotation.ANNOTATION_NAME);
		basicMapping.setSpecifiedFetch(FetchType.EAGER);
		basicMapping.setSpecifiedOptional(Boolean.FALSE);
		assertFalse(basicMapping.isDefault());
		
		persistentAttribute.setMappingKey(MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY);
		assertTrue(persistentAttribute.getMapping() instanceof OneToOneMapping);
		
//TODO		assertEquals(FetchType.EAGER, ((IOneToOneMapping) persistentAttribute.getMapping()).getSpecifiedFetch());
//		assertEquals(Boolean.FALSE, ((IOneToOneMapping) persistentAttribute.getMapping()).getSpecifiedOptional());
		assertNotNull(attributeResource.getAnnotation(OneToOneAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(BasicAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(ColumnAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(LobAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
	}

	public void testBasicMorphToOneToMany() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		assertFalse(basicMapping.isDefault());
		basicMapping.getColumn().setSpecifiedName("FOO");
		attributeResource.addAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(LobAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(EnumeratedAnnotation.ANNOTATION_NAME);
		basicMapping.setSpecifiedFetch(FetchType.EAGER);
		basicMapping.setSpecifiedOptional(Boolean.FALSE);
		assertFalse(basicMapping.isDefault());
		
		persistentAttribute.setMappingKey(MappingKeys.ONE_TO_MANY_ATTRIBUTE_MAPPING_KEY);
		assertTrue(persistentAttribute.getMapping() instanceof OneToManyMapping);
		
//TODO		assertEquals(FetchType.EAGER, ((IOneToManyMapping) persistentAttribute.getMapping()).getSpecifiedFetch());
//		assertNotNull(attributeResource.mappingAnnotation(OneToMany.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(BasicAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(ColumnAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(LobAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
	}
	public void testBasicMorphToManyToOne() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		assertFalse(basicMapping.isDefault());
		basicMapping.getColumn().setSpecifiedName("FOO");
		attributeResource.addAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(LobAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(EnumeratedAnnotation.ANNOTATION_NAME);
		basicMapping.setSpecifiedFetch(FetchType.EAGER);
		basicMapping.setSpecifiedOptional(Boolean.FALSE);
		assertFalse(basicMapping.isDefault());
		
		persistentAttribute.setMappingKey(MappingKeys.MANY_TO_ONE_ATTRIBUTE_MAPPING_KEY);
		assertTrue(persistentAttribute.getMapping() instanceof ManyToOneMapping);
		
//TODO		assertEquals(FetchType.EAGER, ((IManyToOneMapping) persistentAttribute.getMapping()).getSpecifiedFetch());
//		assertEquals(Boolean.FALSE, ((IManyToOneMapping) persistentAttribute.getMapping()).getSpecifiedOptional());
		assertNotNull(attributeResource.getAnnotation(ManyToOneAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(BasicAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(ColumnAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(LobAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
	}
	
	public void testBasicMorphToManyToMany() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		assertFalse(basicMapping.isDefault());
		basicMapping.getColumn().setSpecifiedName("FOO");
		attributeResource.addAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(LobAnnotation.ANNOTATION_NAME);
		attributeResource.addAnnotation(EnumeratedAnnotation.ANNOTATION_NAME);
		basicMapping.setSpecifiedFetch(FetchType.EAGER);
		basicMapping.setSpecifiedOptional(Boolean.FALSE);
		assertFalse(basicMapping.isDefault());
		
		persistentAttribute.setMappingKey(MappingKeys.MANY_TO_MANY_ATTRIBUTE_MAPPING_KEY);
		assertTrue(persistentAttribute.getMapping() instanceof ManyToManyMapping);
		
//TODO		assertEquals(FetchType.EAGER, ((IManyToManyMapping) persistentAttribute.getMapping()).getSpecifiedFetch());
//		assertNotNull(attributeResource.mappingAnnotation(ManyToMany.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(BasicAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(ColumnAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(LobAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
	}

	public void testDefaultBasicGetDefaultOptional() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertEquals(true, basicMapping.isDefaultOptional());
	}
	
	public void testSpecifiedBasicGetDefaultOptional() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertEquals(true, basicMapping.isDefaultOptional());
	}
	
	public void testGetOptional() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertEquals(true, basicMapping.isOptional());
		
		basicMapping.setSpecifiedOptional(Boolean.TRUE);
		assertEquals(true, basicMapping.isOptional());
	}
	
	public void testGetSpecifiedOptional() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertNull(basicMapping.getSpecifiedOptional());
		
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		BasicAnnotation basic = (BasicAnnotation) attributeResource.getAnnotation(JPA.BASIC);
		basic.setOptional(Boolean.FALSE);
		getJpaProject().synchronizeContextModel();
		
		assertEquals(Boolean.FALSE, basicMapping.getSpecifiedOptional());
	}
	
	public void testGetSpecifiedOptional2() throws Exception {
		createTestEntityWithBasicMappingFetchOptionalSpecified();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertEquals(Boolean.FALSE, basicMapping.getSpecifiedOptional());
	}

	public void testSetSpecifiedOptional() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertNull(basicMapping.getSpecifiedOptional());
		
		basicMapping.setSpecifiedOptional(Boolean.FALSE);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		BasicAnnotation basic = (BasicAnnotation) attributeResource.getAnnotation(JPA.BASIC);
		
		assertEquals(Boolean.FALSE, basic.getOptional());
		
		basicMapping.setSpecifiedOptional(null);
		assertNotNull(attributeResource.getAnnotation(JPA.BASIC));
	}
	
	public void testSetSpecifiedOptional2() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertNull(basicMapping.getSpecifiedOptional());
		assertTrue(basicMapping.isDefault());
		
		basicMapping.setSpecifiedOptional(Boolean.TRUE);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		BasicAnnotation basic = (BasicAnnotation) attributeResource.getAnnotation(JPA.BASIC);
		
		assertEquals(Boolean.TRUE, basic.getOptional());
		
		basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertEquals(Boolean.TRUE, basicMapping.getSpecifiedOptional());
		assertFalse(basicMapping.isDefault());

		basicMapping.setSpecifiedOptional(null);
		assertNotNull(attributeResource.getAnnotation(JPA.BASIC));
		
		basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertFalse(basicMapping.isDefault());
	}

	
	public void testGetSpecifiedOptionalUpdatesFromResourceModelChange() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertNull(basicMapping.getSpecifiedOptional());
		
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		BasicAnnotation basic = (BasicAnnotation) attributeResource.getAnnotation(JPA.BASIC);
		basic.setOptional(Boolean.FALSE);
		getJpaProject().synchronizeContextModel();
		
		assertEquals(Boolean.FALSE, basicMapping.getSpecifiedOptional());
		
		basic.setOptional(null);
		getJpaProject().synchronizeContextModel();
		assertNull(basicMapping.getSpecifiedOptional());
		assertFalse(basicMapping.isDefault());
		assertSame(basicMapping, persistentAttribute.getMapping());
		
		basic.setOptional(Boolean.FALSE);
		attributeResource.setPrimaryAnnotation(null, EmptyIterable.<String>instance());
		getJpaProject().synchronizeContextModel();
		
		assertTrue(persistentAttribute.getMapping().isDefault());
		assertEquals(true, ((BasicMapping) persistentAttribute.getMapping()).isOptional());
	}
	
	
	public void testIsLob() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertFalse(basicMapping.getConverter().getType() == LobConverter.class);
	}
	
	public void testIsLob2() throws Exception {
		createTestEntityWithLob();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertTrue(basicMapping.getConverter().getType() == LobConverter.class);
	}
	
	public void testSetLob() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		basicMapping.setConverter(LobConverter.class);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		assertNotNull(attributeResource.getAnnotation(LobAnnotation.ANNOTATION_NAME));
		
		basicMapping.setConverter(null);
		assertNull(attributeResource.getAnnotation(LobAnnotation.ANNOTATION_NAME));
	}
	
	public void testIsLobUpdatesFromResourceModelChange() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertFalse(basicMapping.getConverter().getType() == LobConverter.class);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		attributeResource.addAnnotation(LobAnnotation.ANNOTATION_NAME);
		getJpaProject().synchronizeContextModel();
		
		assertTrue(basicMapping.getConverter().getType() == LobConverter.class);
	
		attributeResource.removeAnnotation(LobAnnotation.ANNOTATION_NAME);
		getJpaProject().synchronizeContextModel();
		
		assertFalse(basicMapping.getConverter().getType() == LobConverter.class);
	}
	
	public void testDefaultBasicGetDefaultConverter() throws Exception {
		createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertNull(basicMapping.getConverter().getType());
	}
	
	public void testSpecifiedBasicGetDefaultConverter() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertNull(basicMapping.getConverter().getType());
	}
	
	public void testGetEnumerated() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertNull(basicMapping.getConverter().getType());
	
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EnumeratedAnnotation enumeratedAnnotation = (EnumeratedAnnotation) attributeResource.addAnnotation(EnumeratedAnnotation.ANNOTATION_NAME);
		getJpaProject().synchronizeContextModel();
		assertEquals(EnumType.ORDINAL, ((EnumeratedConverter) basicMapping.getConverter()).getDefaultEnumType());
		
		enumeratedAnnotation.setValue(org.eclipse.jpt.core.resource.java.EnumType.STRING);		
		getJpaProject().synchronizeContextModel();
		assertEquals(EnumType.STRING, ((EnumeratedConverter) basicMapping.getConverter()).getSpecifiedEnumType());
	}
	
	public void testGetSpecifiedEnumerated() throws Exception {
		createTestEntityWithEnumerated();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertEquals(EnumType.STRING, ((EnumeratedConverter) basicMapping.getConverter()).getSpecifiedEnumType());
	}

	public void testSetSpecifiedEnumerated() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertNull(basicMapping.getConverter().getType());
		
		basicMapping.setConverter(EnumeratedConverter.class);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EnumeratedAnnotation enumerated = (EnumeratedAnnotation) attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME);
		
		assertNotNull(enumerated);
		assertEquals(null, enumerated.getValue());
		
		((EnumeratedConverter) basicMapping.getConverter()).setSpecifiedEnumType(EnumType.STRING);
		assertEquals(org.eclipse.jpt.core.resource.java.EnumType.STRING, enumerated.getValue());
		
		((EnumeratedConverter) basicMapping.getConverter()).setSpecifiedEnumType(null);
		assertNotNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
		assertNull(enumerated.getValue());
		
		basicMapping.setConverter(null);
		assertNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
	}
	
	public void testGetSpecifiedEnumeratedUpdatesFromResourceModelChange() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertNull(basicMapping.getConverter().getType());
		
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EnumeratedAnnotation enumerated = (EnumeratedAnnotation) attributeResource.addAnnotation(EnumeratedAnnotation.ANNOTATION_NAME);
		enumerated.setValue(org.eclipse.jpt.core.resource.java.EnumType.STRING);
		getJpaProject().synchronizeContextModel();
		
		assertEquals(EnumType.STRING, ((EnumeratedConverter) basicMapping.getConverter()).getSpecifiedEnumType());
		
		enumerated.setValue(null);
		getJpaProject().synchronizeContextModel();
		assertNotNull(attributeResource.getAnnotation(EnumeratedAnnotation.ANNOTATION_NAME));
		assertNull(((EnumeratedConverter) basicMapping.getConverter()).getSpecifiedEnumType());
		assertFalse(basicMapping.isDefault());
		assertSame(basicMapping, persistentAttribute.getMapping());
	}
	
	public void testGetTemporal() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		attributeResource.addAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		getJpaProject().synchronizeContextModel();
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertEquals(TemporalConverter.class, basicMapping.getConverter().getType());
	}
	
	public void testGetTemporal2() throws Exception {
		createTestEntityWithTemporal();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertEquals(TemporalConverter.class, basicMapping.getConverter().getType());
		assertEquals(TemporalType.TIMESTAMP, ((TemporalConverter) basicMapping.getConverter()).getTemporalType());
	}

	public void testSetTemporal() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		assertNull(basicMapping.getConverter().getType());
		
		basicMapping.setConverter(TemporalConverter.class);
		((TemporalConverter) basicMapping.getConverter()).setTemporalType(TemporalType.TIME);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		TemporalAnnotation temporal = (TemporalAnnotation) attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		
		assertEquals(org.eclipse.jpt.core.resource.java.TemporalType.TIME, temporal.getValue());
		
		basicMapping.setConverter(null);
		assertNull(attributeResource.getAnnotation(TemporalAnnotation.ANNOTATION_NAME));
	}
	
	public void testGetTemporalUpdatesFromResourceModelChange() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();

		assertNull(basicMapping.getConverter().getType());
		
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		TemporalAnnotation temporal = (TemporalAnnotation) attributeResource.addAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		temporal.setValue(org.eclipse.jpt.core.resource.java.TemporalType.DATE);
		getJpaProject().synchronizeContextModel();
		
		assertEquals(TemporalConverter.class, basicMapping.getConverter().getType());
		assertEquals(TemporalType.DATE, ((TemporalConverter) basicMapping.getConverter()).getTemporalType());
		
		attributeResource.removeAnnotation(TemporalAnnotation.ANNOTATION_NAME);
		getJpaProject().synchronizeContextModel();
		
		assertNull(basicMapping.getConverter().getType());
		assertFalse(basicMapping.isDefault());
		assertSame(basicMapping, persistentAttribute.getMapping());
	}
	
	public void testGetColumn() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		
		assertNull(basicMapping.getColumn().getSpecifiedName());
		assertEquals("id", basicMapping.getColumn().getName());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		ColumnAnnotation column = (ColumnAnnotation) attributeResource.addAnnotation(JPA.COLUMN);
		column.setName("foo");
		getJpaProject().synchronizeContextModel();
		
		assertEquals("foo", basicMapping.getColumn().getSpecifiedName());
		assertEquals("foo", basicMapping.getColumn().getName());
		assertEquals("id", basicMapping.getColumn().getDefaultName());
	}
}