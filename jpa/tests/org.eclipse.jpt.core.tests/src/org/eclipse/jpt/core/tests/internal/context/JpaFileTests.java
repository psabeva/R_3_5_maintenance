/*******************************************************************************
 * Copyright (c) 2008, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.tests.internal.context;

import java.util.Iterator;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.core.JpaFile;
import org.eclipse.jpt.core.JpaStructureNode;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.java.JavaPersistentType;
import org.eclipse.jpt.core.context.orm.EntityMappings;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.context.persistence.ClassRef;
import org.eclipse.jpt.core.context.persistence.MappingFileRef;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.persistence.PersistenceFactory;
import org.eclipse.jpt.core.resource.persistence.XmlMappingFileRef;
import org.eclipse.jpt.core.resource.xml.JpaXmlResource;

@SuppressWarnings("nls")
public class JpaFileTests extends ContextModelTestCase
{
	public JpaFileTests(String name) {
		super(name);
	}	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		XmlMappingFileRef mappingFileRef = PersistenceFactory.eINSTANCE.createXmlMappingFileRef();
		mappingFileRef.setFileName(JptCorePlugin.DEFAULT_ORM_XML_RUNTIME_PATH.toString());
		getXmlPersistenceUnit().getMappingFiles().add(mappingFileRef);
		getPersistenceXmlResource().save(null);
	}
	
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
	
	public void testGetRootStructureNode() throws Exception {
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);

		IFile file = getOrmXmlResource().getFile();
		JpaFile ormXmlJpaFile = JptCorePlugin.getJpaFile(file);
		
		assertEquals(getEntityMappings(), ormXmlJpaFile.rootStructureNodes().next());
		
		
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		assertEquals(ormPersistentType.getJavaPersistentType(), javaJpaFile.rootStructureNodes().next());
		
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		//verify the mapping file reference "wins" as the root structure node when both
		//persistence.xml <class> tag and mapping file <entity> tag exist for a particulary java class
		assertEquals(ormPersistentType.getJavaPersistentType(), javaJpaFile.rootStructureNodes().next());

		getEntityMappings().removePersistentType(ormPersistentType);

		assertEquals(getJavaEntity().getPersistentType(), javaJpaFile.rootStructureNodes().next());

		ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		assertEquals(ormPersistentType.getJavaPersistentType(), javaJpaFile.rootStructureNodes().next());
	}
	
	public void testEntityMappingsRootStructureNodeRemoved() throws Exception {
		IFile file = getOrmXmlResource().getFile();
		JpaFile ormXmlJpaFile = JptCorePlugin.getJpaFile(file);
		assertEquals(getEntityMappings(), ormXmlJpaFile.rootStructureNodes().next());
		
		JpaXmlResource resource = (JpaXmlResource) ormXmlJpaFile.getResourceModel();
		resource.getContents().remove(resource.getRootObject());
		
		assertFalse(ormXmlJpaFile.rootStructureNodes().hasNext());
	}

	public void testImpliedEntityMappingsRootStructureNodeRemoved() throws Exception {
		IFile file = getOrmXmlResource().getFile();
		JpaFile ormXmlJpaFile = JptCorePlugin.getJpaFile(file);
		
		assertNull(getPersistenceUnit().getImpliedMappingFileRef());
		
		getXmlPersistenceUnit().getMappingFiles().remove(0);
		assertNotNull(getPersistenceUnit().getImpliedMappingFileRef());
		assertEquals(getPersistenceUnit().getImpliedMappingFileRef().getMappingFile().getRoot(), ormXmlJpaFile.rootStructureNodes().next());
		
		JpaXmlResource resource = (JpaXmlResource) ormXmlJpaFile.getResourceModel();
		resource.getContents().remove(resource.getRootObject());
		
		assertFalse(ormXmlJpaFile.rootStructureNodes().hasNext());
	}
	
	public void testEntityMappingsRootStructureNodeRemovedFromResourceModel() throws Exception {
		IFile file = getOrmXmlResource().getFile();
		JpaFile ormXmlJpaFile = JptCorePlugin.getJpaFile(file);
		assertEquals(getEntityMappings(), ormXmlJpaFile.rootStructureNodes().next());
		
		getOrmXmlResource().getContents().remove(getOrmXmlResource().getRootObject());
		
		assertFalse(ormXmlJpaFile.rootStructureNodes().hasNext());
	}

	public void testUpdatePersistenceRootStructureNodePersistenceRemoved() throws Exception {
		IFile file = getPersistenceXmlResource().getFile();
		JpaFile persistenceXmlJpaFile = JptCorePlugin.getJpaFile(file);
		assertEquals(getRootContextNode().getPersistenceXml().getPersistence(), persistenceXmlJpaFile.rootStructureNodes().next());
		
		JpaXmlResource resource = (JpaXmlResource) persistenceXmlJpaFile.getResourceModel();
		resource.getContents().remove(resource.getRootObject());
		
		assertFalse(persistenceXmlJpaFile.rootStructureNodes().hasNext());
	}
	
	public void testUpdateOrmJavaRootStructureNodePersistenceRemoved() throws Exception {		
		IFile file = getPersistenceXmlResource().getFile();
		JpaFile persistenceXmlJpaFile = JptCorePlugin.getJpaFile(file);
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		assertEquals(ormPersistentType.getJavaPersistentType(), javaJpaFile.rootStructureNodes().next());
		
		JpaXmlResource resource = (JpaXmlResource) persistenceXmlJpaFile.getResourceModel();
		resource.getContents().remove(resource.getRootObject());
		
		assertFalse(javaJpaFile.rootStructureNodes().hasNext());
	}
	
	public void testUpdateJavaRootStructureNodePersistenceRemoved() throws Exception {		
		IFile file = getPersistenceXmlResource().getFile();
		JpaFile persistenceXmlJpaFile = JptCorePlugin.getJpaFile(file);
		ICompilationUnit cu = createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		assertEquals(getJavaPersistentType(), javaJpaFile.rootStructureNodes().next());
		
		JpaXmlResource resource = (JpaXmlResource) persistenceXmlJpaFile.getResourceModel();
		resource.getContents().remove(resource.getRootObject());
		
		assertFalse(javaJpaFile.rootStructureNodes().hasNext());
	}

	public void testPersistenceRootStructureNodeRemovedFromResourceModel() throws Exception {
		IFile file = getPersistenceXmlResource().getFile();
		JpaFile persistenceXmlJpaFile = JptCorePlugin.getJpaFile(file);
		getRootContextNode().getPersistenceXml().getPersistence();
		assertEquals(getRootContextNode().getPersistenceXml().getPersistence(), persistenceXmlJpaFile.rootStructureNodes().next());
		
		getPersistenceXmlResource().getContents().remove(getXmlPersistence());
		
		assertFalse(persistenceXmlJpaFile.rootStructureNodes().hasNext());
	}
	
	public void testUpdateOrmJavaRootStructureNodePersistenceXmlRemoved() throws Exception {		
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		assertEquals(ormPersistentType.getJavaPersistentType(), javaJpaFile.rootStructureNodes().next());
		
		getPersistenceXmlResource().getContents().remove(getXmlPersistence());
		assertFalse(javaJpaFile.rootStructureNodes().hasNext());
	}
	
	public void testUpdateJavaRootStructureNodePersistenceXmlRemoved() throws Exception {		
		ICompilationUnit cu = createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		assertEquals(getJavaPersistentType(), javaJpaFile.rootStructureNodes().next());
		
		getPersistenceXmlResource().getContents().remove(getXmlPersistence());
		assertFalse(javaJpaFile.rootStructureNodes().hasNext());
	}
	
	public void testOrmJavaPersistentTypeRootStructureNodeRemoved() throws Exception {
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		JavaPersistentType javaPersistentType = ormPersistentType.getJavaPersistentType();
		assertEquals(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		assertTrue(javaJpaFile.rootStructureNodes().next().getParent() instanceof OrmPersistentType);
		
		
		getEntityMappings().removePersistentType(0);
		
		assertEquals(1, javaJpaFile.rootStructureNodesSize());
		assertTrue(javaJpaFile.rootStructureNodes().next().getParent() instanceof ClassRef);
		assertNotSame(javaPersistentType, javaJpaFile.rootStructureNodes().next());
	}
	
	public void testOrmJavaPersistentTypeRootStructureNodeRemovedFromResourceModel() throws Exception {
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		JavaPersistentType javaPersistentType = ormPersistentType.getJavaPersistentType();
		assertEquals(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		assertTrue(javaJpaFile.rootStructureNodes().next().getParent() instanceof OrmPersistentType);
		
		getXmlEntityMappings().getEntities().remove(0);
		
		assertEquals(1, javaJpaFile.rootStructureNodesSize());
		assertTrue(javaJpaFile.rootStructureNodes().next().getParent() instanceof ClassRef);
		assertNotSame(javaPersistentType, javaJpaFile.rootStructureNodes().next());
	}

	public void testJavaPersistentTypeRootStructureNodeRemoved() throws Exception {
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		JavaPersistentType javaPersistentType = getJavaPersistentType();
		assertEquals(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		assertEquals(getEntityMappings().getPersistenceUnit().specifiedClassRefs().next(), javaJpaFile.rootStructureNodes().next().getParent());
		
		getEntityMappings().getPersistenceUnit().removeSpecifiedClassRef(0);
		assertNotSame(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		assertEquals(getEntityMappings().getPersistenceUnit().impliedClassRefs().next(), javaJpaFile.rootStructureNodes().next().getParent());
	}
	
	public void testJavaPersistentTypeRootStructureNodeRemovedFromResourceModel() throws Exception {
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		JavaPersistentType javaPersistentType = getJavaPersistentType();
		Iterator<JpaStructureNode> rootStructureNodes = javaJpaFile.rootStructureNodes();
		JpaStructureNode rootStructureNode = rootStructureNodes.next();
		assertEquals(javaPersistentType, rootStructureNode);
		assertEquals(getEntityMappings().getPersistenceUnit().specifiedClassRefs().next(), rootStructureNode.getParent());
		assertFalse(rootStructureNodes.hasNext());
		
		removeXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		assertNotSame(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		assertEquals(getEntityMappings().getPersistenceUnit().impliedClassRefs().next(), javaJpaFile.rootStructureNodes().next().getParent());
	}

	public void testImpliedJavaPersistentTypeRootStructureNodeRemoved() throws Exception {
		getJpaProject().setDiscoversAnnotatedClasses(true);
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		JavaPersistentType javaPersistentType = getPersistenceUnit().impliedClassRefs().next().getJavaPersistentType();
		assertEquals(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		
		javaPersistentType.setMappingKey(MappingKeys.NULL_TYPE_MAPPING_KEY);
		
		assertFalse(javaJpaFile.rootStructureNodes().hasNext());
	}
	
	public void testJavaRootStructureNodesEntityMappingsRemoved() throws Exception {
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		JavaPersistentType javaPersistentType = ormPersistentType.getJavaPersistentType();
		assertEquals(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		
		getOrmXmlResource().getContents().remove(getXmlEntityMappings());
		assertNotSame(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		assertEquals(1, javaJpaFile.rootStructureNodesSize());
		assertEquals(getPersistenceUnit().impliedClassRefs().next(), javaJpaFile.rootStructureNodes().next().getParent());
	}
	
	public void testJavaRootStructureNodesPersistenceUnitRemovedFromResourceModel() throws Exception {
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		JavaPersistentType javaPersistentType = ormPersistentType.getJavaPersistentType();
		assertEquals(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		
		getXmlPersistence().getPersistenceUnits().remove(0);

		assertFalse(javaJpaFile.rootStructureNodes().hasNext());
		assertEquals(0, javaJpaFile.rootStructureNodesSize());
	}
	
	public void testJavaRootStructureNodesPersistenceUnitRemoved() throws Exception {
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		JavaPersistentType javaPersistentType = ormPersistentType.getJavaPersistentType();
		assertEquals(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		
		getJpaProject().getRootContextNode().getPersistenceXml().getPersistence().removePersistenceUnit(0);

		assertFalse(javaJpaFile.rootStructureNodes().hasNext());
		assertEquals(0, javaJpaFile.rootStructureNodesSize());
	}

	public void testJavaRootStructureNodesOrmPersistentTypeRemoved() throws Exception {
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		JavaPersistentType javaPersistentType = ormPersistentType.getJavaPersistentType();
		assertEquals(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		
		getEntityMappings().removePersistentType(0);
		assertNotSame(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		assertEquals(1, javaJpaFile.rootStructureNodesSize());
		assertEquals(getEntityMappings().getPersistenceUnit().impliedClassRefs().next(), javaJpaFile.rootStructureNodes().next().getParent());
	}
	
	public void testJavaRootStructureNodesOrmTypeMappingMorphed() throws Exception {
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		JavaPersistentType javaPersistentType = ormPersistentType.getJavaPersistentType();
		assertEquals(javaPersistentType, javaJpaFile.rootStructureNodes().next());
	
		ormPersistentType.setMappingKey(MappingKeys.EMBEDDABLE_TYPE_MAPPING_KEY);
		assertEquals(1, javaJpaFile.rootStructureNodesSize());
		javaPersistentType = getEntityMappings().getPersistentTypes().iterator().next().getJavaPersistentType();
		assertEquals(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		
		getEntityMappings().removePersistentType(0);
		assertNotSame(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		assertEquals(1, javaJpaFile.rootStructureNodesSize());
		assertEquals(getEntityMappings().getPersistenceUnit().impliedClassRefs().next(), javaJpaFile.rootStructureNodes().next().getParent());
	}
	
	public void testUpdateOrmJavaRootStructureNodeMappingFileRefChanged() throws Exception {		
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		assertEquals(ormPersistentType.getJavaPersistentType(), javaJpaFile.rootStructureNodes().next());
		
		MappingFileRef mappingFileRef = getPersistenceUnit().mappingFileRefs().next();
		mappingFileRef.setFileName("foo");
		
		ormPersistentType = ((EntityMappings) getPersistenceUnit().getImpliedMappingFileRef().getMappingFile().getRoot()).getPersistentTypes().iterator().next();
		assertEquals(ormPersistentType.getJavaPersistentType(), javaJpaFile.rootStructureNodes().next());
		
		IFile file = getPersistenceXmlResource().getFile();
		JpaFile ormXmlJpaFile = JptCorePlugin.getJpaFile(file);		
		
		assertEquals(1, ormXmlJpaFile.rootStructureNodesSize());
	}
	
	public void testUpdateJavaRootStructureNodeMappingFileRefChanged() throws Exception {		
		ICompilationUnit cu = createTestEntity();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		assertEquals(getJavaPersistentType(), javaJpaFile.rootStructureNodes().next());
		
		MappingFileRef mappingFileRef = getPersistenceUnit().mappingFileRefs().next();
		mappingFileRef.setFileName("foo");
		assertEquals(getJavaPersistentType(), javaJpaFile.rootStructureNodes().next());
	}

	
	public void testUpdateJavaRootStrucutreNodeDeleteOrmResource() throws Exception {
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		
		ICompilationUnit cu = createTestEntity();
		JpaFile javaJpaFile = JptCorePlugin.getJpaFile((IFile) cu.getResource());
		
		JavaPersistentType javaPersistentType = ormPersistentType.getJavaPersistentType();
		assertEquals(javaPersistentType, javaJpaFile.rootStructureNodes().next());

		
		deleteResource(getOrmXmlResource());
		
		assertNotSame(javaPersistentType, javaJpaFile.rootStructureNodes().next());
		assertEquals(1, javaJpaFile.rootStructureNodesSize());
		assertEquals(getPersistenceUnit().impliedClassRefs().next(), javaJpaFile.rootStructureNodes().next().getParent());
	}
	//TODO test rootStructureNodes with a static inner class
}