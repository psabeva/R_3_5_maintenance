/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.tests.internal.context;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IType;
import org.eclipse.jpt.core.internal.IJpaProject;
import org.eclipse.jpt.core.internal.JptCorePlugin;
import org.eclipse.jpt.core.internal.context.base.IBaseJpaContent;
import org.eclipse.jpt.core.internal.context.base.IClassRef;
import org.eclipse.jpt.core.internal.context.base.IEntity;
import org.eclipse.jpt.core.internal.context.base.IPersistenceUnit;
import org.eclipse.jpt.core.internal.context.java.IJavaPersistentType;
import org.eclipse.jpt.core.internal.context.orm.EntityMappings;
import org.eclipse.jpt.core.internal.resource.orm.OrmArtifactEdit;
import org.eclipse.jpt.core.internal.resource.orm.OrmResource;
import org.eclipse.jpt.core.internal.resource.persistence.PersistenceArtifactEdit;
import org.eclipse.jpt.core.internal.resource.persistence.PersistenceFactory;
import org.eclipse.jpt.core.internal.resource.persistence.PersistenceResource;
import org.eclipse.jpt.core.internal.resource.persistence.XmlJavaClassRef;
import org.eclipse.jpt.core.internal.resource.persistence.XmlPersistence;
import org.eclipse.jpt.core.internal.resource.persistence.XmlPersistenceUnit;
import org.eclipse.jpt.core.tests.internal.jdtutility.AnnotationTestCase;
import org.eclipse.jpt.core.tests.internal.projects.TestJavaProject;
import org.eclipse.jpt.core.tests.internal.projects.TestJpaProject;
import org.eclipse.wst.common.internal.emfworkbench.WorkbenchResourceHelper;

public abstract class ContextModelTestCase extends AnnotationTestCase
{
	protected static final String BASE_PROJECT_NAME = "ContextModelTestProject";
		
	protected PersistenceArtifactEdit persistenceArtifactEdit;
	protected OrmArtifactEdit ormArtifactEdit;
	
	
	protected ContextModelTestCase(String name) {
		super(name);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.persistenceArtifactEdit = PersistenceArtifactEdit.getArtifactEditForWrite(getJavaProject().getProject());
		this.ormArtifactEdit = OrmArtifactEdit.getArtifactEditForWrite(getJavaProject().getProject());
		waitForWorkspaceJobs();
	}
	
	@Override
	protected void deleteAllProjects()  throws Exception{
		//don't delete projects, creating a new one with a new name
		//workspace will be deleted next time tests are run.
		//not saying this is the ultimate solution, but it will work for now
		//until we can figure out how to properly delete projects in tests
	}
	
	@Override
	protected void tearDown() throws Exception {
		//at least delete the project from the workspace since, deleting from the file system doesn't work well.
		//tests run too slow otherwise because so many projects are created in the workspace
		this.persistenceArtifactEdit.dispose();
		this.persistenceArtifactEdit = null;
		this.ormArtifactEdit.dispose();
		this.ormArtifactEdit = null;
		getJavaProject().getProject().delete(false, true, null);
		super.tearDown();
	}
	
	@Override
	protected TestJavaProject buildJavaProject(boolean autoBuild) throws Exception {
		return buildJpaProject(BASE_PROJECT_NAME, autoBuild);
	}
	
	protected TestJpaProject buildJpaProject(String projectName, boolean autoBuild) 
			throws Exception {
		return TestJpaProject.buildJpaProject(projectName, autoBuild);
	}
	
	protected IJpaProject jpaProject() {
		return getJavaProject().getJpaProject();
	}
	
	protected void waitForWorkspaceJobs() {
		// This job will not finish running until the workspace jobs are done
		Job waitJob = new Job("Wait job") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					return Status.OK_STATUS;
				}
			};
		waitJob.setRule(ResourcesPlugin.getWorkspace().getRoot());
		waitJob.schedule();
		try {
			waitJob.join();
		} catch (InterruptedException ex) {
			// the job thread was interrupted during a wait - ignore
		}
	}
	
	protected PersistenceResource persistenceResource() {
		return this.persistenceArtifactEdit.getResource();
	}
	
	protected OrmResource ormResource() {
		return this.ormArtifactEdit.getResource(JptCorePlugin.ormXmlDeploymentURI(jpaProject().project()));
	}
	
	protected XmlPersistence xmlPersistence() {
		return persistenceResource().getPersistence();
	}
	
	protected EntityMappings entityMappings() {
		return persistenceUnit().mappingFileRefs().next().getOrmXml().getEntityMappings();
	}
	
	protected XmlPersistenceUnit xmlPersistenceUnit() {
		return persistenceResource().getPersistence().getPersistenceUnits().get(0);
	}
	
	protected IPersistenceUnit persistenceUnit() {
		return jpaContent().getPersistenceXml().getPersistence().persistenceUnits().next();
	}
	
	protected IClassRef classRef() {
		return persistenceUnit().classRefs().next();
	}
	
	protected IJavaPersistentType javaPersistentType() {
		return classRef().getJavaPersistentType();
	}
	
	protected IEntity javaEntity() {
		return (IEntity) javaPersistentType().getMapping();
	}
	
	protected void addXmlClassRef(String className) {
		XmlPersistenceUnit xmlPersistenceUnit = xmlPersistenceUnit();
		
		XmlJavaClassRef xmlClassRef = PersistenceFactory.eINSTANCE.createXmlJavaClassRef();
		xmlClassRef.setJavaClass(className);
		xmlPersistenceUnit.getClasses().add(xmlClassRef);
	}
	
	protected void removeXmlClassRef(String className) {
		XmlPersistenceUnit xmlPersistenceUnit = xmlPersistenceUnit();
		XmlJavaClassRef xmlJavaClassRefToRemove  = null;
		for (XmlJavaClassRef xmlJavaClassRef : xmlPersistenceUnit.getClasses()) {
			if (xmlJavaClassRef.getJavaClass().equals(className)) {
				xmlJavaClassRefToRemove = xmlJavaClassRef;
			}
		}
		if (xmlJavaClassRefToRemove == null) {
			throw new IllegalArgumentException();
		}
		xmlPersistenceUnit.getClasses().remove(xmlJavaClassRefToRemove);
	}

	protected IBaseJpaContent jpaContent() {
		return (IBaseJpaContent) getJavaProject().getJpaProject().contextModel();
	}
	
	@Override
	protected TestJpaProject getJavaProject() {
		return (TestJpaProject) super.getJavaProject();
	}
	
	protected void deleteResource(Resource resource) throws CoreException {
		System.gc();
		WorkbenchResourceHelper.deleteResource(resource);
		waitForWorkspaceJobs();
	}
	
	protected IType createAnnotationAndMembers(String annotationName, String annotationBody) throws Exception {
		return this.javaProject.createType("javax.persistence", annotationName + ".java", "public @interface " + annotationName + " { " + annotationBody + " }");
	}
	
	protected IType createEnumAndMembers(String enumName, String enumBody) throws Exception {
		return this.javaProject.createType("javax.persistence", enumName + ".java", "public enum " + enumName + " { " + enumBody + " }");
	}
}
