package org.eclipse.jpt.core.tests.internal.resource;

import junit.framework.TestCase;
import org.eclipse.jpt.core.internal.resource.persistence.PersistenceArtifactEdit;
import org.eclipse.jpt.core.internal.resource.persistence.PersistenceResourceModel;
import org.eclipse.jpt.core.tests.internal.ProjectUtility;
import org.eclipse.jpt.core.tests.internal.projects.TestJpaProject;

public class PersistenceModelTests extends TestCase
{
	static final String BASE_PROJECT_NAME = PersistenceModelTests.class.getSimpleName();
	
	TestJpaProject jpaProject;

	
	public PersistenceModelTests(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ProjectUtility.deleteAllProjects();
		jpaProject = TestJpaProject.buildJpaProject(BASE_PROJECT_NAME, false); // false = no auto-build
	}
	
	@Override
	protected void tearDown() throws Exception {
		jpaProject = null;
		super.tearDown();
	}
	
	public void testModelLoad() {
		PersistenceArtifactEdit artifactEdit = PersistenceArtifactEdit.getArtifactEditForRead(jpaProject.getProject(), "META-INF/persistence.xml");
		assertNotNull(artifactEdit);
		PersistenceResourceModel resource = artifactEdit.getPersistenceResource();
		assertNotNull(resource);
	}
	
	public void testModelLoad2() {
		PersistenceArtifactEdit artifactEdit = PersistenceArtifactEdit.getArtifactEditForRead(jpaProject.getProject(), "META-INF/persistence.xml");
		assertNotNull(artifactEdit);
		PersistenceResourceModel resource = artifactEdit.getPersistenceResource();
		assertNotNull(resource);
	}
}
