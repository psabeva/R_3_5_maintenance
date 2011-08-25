/*******************************************************************************
 * Copyright (c) 2006, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jpt.common.core.JptCommonCorePlugin;
import org.eclipse.jpt.common.core.JptResourceModel;
import org.eclipse.jpt.common.core.JptResourceModelListener;
import org.eclipse.jpt.common.core.internal.resource.java.binary.BinaryTypeCache;
import org.eclipse.jpt.common.core.internal.resource.java.source.SourceTypeCompilationUnit;
import org.eclipse.jpt.common.core.internal.utility.PlatformTools;
import org.eclipse.jpt.common.core.resource.ResourceLocator;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAbstractType;
import org.eclipse.jpt.common.core.resource.java.JavaResourceCompilationUnit;
import org.eclipse.jpt.common.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.common.core.resource.java.JavaResourcePackage;
import org.eclipse.jpt.common.core.resource.java.JavaResourcePackageFragmentRoot;
import org.eclipse.jpt.common.core.resource.java.JavaResourcePackageInfoCompilationUnit;
import org.eclipse.jpt.common.core.resource.java.JavaResourceTypeCache;
import org.eclipse.jpt.common.utility.Command;
import org.eclipse.jpt.common.utility.CommandExecutor;
import org.eclipse.jpt.common.utility.Filter;
import org.eclipse.jpt.common.utility.internal.BitTools;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.StringTools;
import org.eclipse.jpt.common.utility.internal.ThreadLocalCommandExecutor;
import org.eclipse.jpt.common.utility.internal.iterables.ArrayIterable;
import org.eclipse.jpt.common.utility.internal.iterables.CompositeIterable;
import org.eclipse.jpt.common.utility.internal.iterables.EmptyIterable;
import org.eclipse.jpt.common.utility.internal.iterables.FilteringIterable;
import org.eclipse.jpt.common.utility.internal.iterables.LiveCloneIterable;
import org.eclipse.jpt.common.utility.internal.iterables.SnapshotCloneIterable;
import org.eclipse.jpt.common.utility.internal.iterables.TransformationIterable;
import org.eclipse.jpt.common.utility.internal.synchronizers.CallbackSynchronousSynchronizer;
import org.eclipse.jpt.common.utility.internal.synchronizers.SynchronousSynchronizer;
import org.eclipse.jpt.common.utility.synchronizers.CallbackSynchronizer;
import org.eclipse.jpt.common.utility.synchronizers.Synchronizer;
import org.eclipse.jpt.jpa.core.JpaDataSource;
import org.eclipse.jpt.jpa.core.JpaFacet;
import org.eclipse.jpt.jpa.core.JpaFile;
import org.eclipse.jpt.jpa.core.JpaPlatform;
import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.jpa.core.JptJpaCorePlugin;
import org.eclipse.jpt.jpa.core.context.JpaRootContextNode;
import org.eclipse.jpt.jpa.core.context.java.JavaTypeMappingDefinition;
import org.eclipse.jpt.jpa.core.internal.validation.DefaultJpaValidationMessages;
import org.eclipse.jpt.jpa.core.internal.validation.JpaValidationMessages;
import org.eclipse.jpt.jpa.core.jpa2.JpaProject2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.JpaRootContextNode2_0;
import org.eclipse.jpt.jpa.core.libprov.JpaLibraryProviderInstallOperationConfig;
import org.eclipse.jpt.jpa.core.resource.xml.JpaXmlResource;
import org.eclipse.jpt.jpa.db.Catalog;
import org.eclipse.jpt.jpa.db.ConnectionProfile;
import org.eclipse.jpt.jpa.db.Database;
import org.eclipse.jpt.jpa.db.Schema;
import org.eclipse.jpt.jpa.db.SchemaContainer;
import org.eclipse.jst.common.project.facet.core.libprov.ILibraryProvider;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryProviderFramework;
import org.eclipse.jst.j2ee.model.internal.validation.ValidationCancelledException;
import org.eclipse.wst.common.internal.emfworkbench.WorkbenchResourceHelper;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

/**
 * JPA project. Holds all the JPA stuff.
 * 
 * The JPA platform provides the hooks for vendor-specific stuff.
 * 
 * The JPA files are the "resource" model (i.e. objects that correspond directly
 * to Eclipse resources; e.g. Java source code files, XML files, JAR files).
 * 
 * The root context node is the "context"model (i.e. objects that attempt to
 * model the JPA spec, using the "resource" model as an adapter to the Eclipse
 * resources).
 * 
 * The data source is an adapter to the DTP meta-data model.
 */
public abstract class AbstractJpaProject
	extends AbstractJpaNode
	implements JpaProject2_0
{
	/**
	 * The Eclipse project corresponding to the JPA project.
	 */
	protected final IProject project;

	/**
	 * The vendor-specific JPA platform that builds the JPA project
	 * and all its contents.
	 */
	protected final JpaPlatform jpaPlatform;

	/**
	 * The JPA files associated with the JPA project:
	 *     persistence.xml
	 *     orm.xml
	 *     java
	 */
	protected final Vector<JpaFile> jpaFiles = new Vector<JpaFile>();

	/**
	 * The "external" Java resource compilation units (source). Populated upon demand.
	 */
	protected final Vector<JavaResourceCompilationUnit> externalJavaResourceCompilationUnits = new Vector<JavaResourceCompilationUnit>();

	/**
	 * The "external" Java resource types (binary). Populated upon demand.
	 */
	protected final JavaResourceTypeCache externalJavaResourceTypeCache;

	/**
	 * Resource models notify this listener when they change. A project update
	 * should occur any time a resource model changes.
	 */
	protected final JptResourceModelListener resourceModelListener;

	/**
	 * The root of the model representing the collated resources associated with 
	 * the JPA project.
	 */
	protected final JpaRootContextNode rootContextNode;

	/**
	 * A pluggable synchronizer that keeps the JPA
	 * project's context model synchronized with its resource model, either
	 * synchronously or asynchronously (or not at all). A synchronous synchronizer
	 * is the default. For performance reasons, a UI should
	 * immediately change this to an asynchronous synchronizer. A synchronous
	 * synchronizer can be used when the project is being manipulated by a "batch"
	 * (or non-UI) client (e.g. when testing "synchronization"). A null updater
	 * can used during tests that do not care whether "synchronization" occur.
	 * Clients will need to explicitly configure the synchronizer if they require
	 * an asynchronous synchronizer.
	 */
	protected volatile Synchronizer contextModelSynchronizer;
	protected volatile boolean synchronizingContextModel = false;

	/**
	 * A pluggable synchronizer that "updates" the JPA project, either
	 * synchronously or asynchronously (or not at all). A synchronous updater
	 * is the default, allowing a newly-constructed JPA project to be "updated"
	 * upon return from the constructor. For performance reasons, a UI should
	 * immediately change this to an asynchronous updater. A synchronous
	 * updater can be used when the project is being manipulated by a "batch"
	 * (or non-UI) client (e.g. when testing the "update" behavior). A null
	 * updater can used during tests that do not care whether "synchronization"
	 * occur.
	 * Clients will need to explicitly configure the updater if they require
	 * an asynchronous updater.
	 */
	protected volatile CallbackSynchronizer updateSynchronizer;
	protected final CallbackSynchronizer.Listener updateSynchronizerListener;

	/**
	 * The data source that wraps the DTP model.
	 */
	// TODO move to persistence unit... :-(
	protected final JpaDataSource dataSource;
	
	/**
	 * A catalog name used to override the connection's default catalog.
	 */
	protected volatile String userOverrideDefaultCatalog;

	/**
	 * A schema name used to override the connection's default schema.
	 */
	protected volatile String userOverrideDefaultSchema;

	/**
	 * Flag indicating whether the project should "discover" annotated
	 * classes automatically, as opposed to requiring the classes to be
	 * listed in <code>persistence.xml</code>. Stored as a preference.
	 * @see #setDiscoversAnnotatedClasses(boolean)
	 */
	protected volatile boolean discoversAnnotatedClasses;

	/**
	 * Support for modifying documents shared with the UI.
	 */
	protected final ThreadLocalCommandExecutor modifySharedDocumentCommandExecutor;

	/**
	 * The name of the Java project source folder that holds the generated
	 * metamodel. If the name is <code>null</code> the metamodel is not
	 * generated.
	 */
	protected volatile String metamodelSourceFolderName;


	// ********** constructor/initialization **********

	protected AbstractJpaProject(JpaProject.Config config) {
		super(null);  // JPA project is the root of the containment tree
		if ((config.getProject() == null) || (config.getJpaPlatform() == null)) {
			throw new NullPointerException();
		}
		this.project = config.getProject();
		this.jpaPlatform = config.getJpaPlatform();
		this.dataSource = this.getJpaFactory().buildJpaDataSource(this, config.getConnectionProfileName());
		this.userOverrideDefaultCatalog = config.getUserOverrideDefaultCatalog();
		this.userOverrideDefaultSchema = config.getUserOverrideDefaultSchema();
		this.discoversAnnotatedClasses = config.discoverAnnotatedClasses();

		this.modifySharedDocumentCommandExecutor = this.buildModifySharedDocumentCommandExecutor();

		this.resourceModelListener = this.buildResourceModelListener();
		// build the JPA files corresponding to the Eclipse project's files
		InitialResourceProxyVisitor visitor = this.buildInitialResourceProxyVisitor();
		visitor.visitProject(this.project);

		this.externalJavaResourceTypeCache = this.buildExternalJavaResourceTypeCache();

		if (this.isJpa2_0Compatible()) {
			this.metamodelSourceFolderName = ((JpaProject2_0.Config) config).getMetamodelSourceFolderName();
			if (this.metamodelSoureFolderNameIsInvalid()) {
				this.metamodelSourceFolderName = null;
			}
		}

		this.rootContextNode = this.buildRootContextNode();

		// there *shouldn't* be any changes to the resource model...
		this.setContextModelSynchronizer_(this.buildSynchronousContextModelSynchronizer());

		this.updateSynchronizerListener = this.buildUpdateSynchronizerListener();
		// "update" the project before returning
		this.setUpdateSynchronizer_(this.buildSynchronousUpdateSynchronizer());

		// start listening to this cache once the context model has been built
		// and all the external types are faulted in
		this.externalJavaResourceTypeCache.addResourceModelListener(this.resourceModelListener);
	}

	@Override
	protected boolean requiresParent() {
		return false;
	}
	
	@Override
	public IResource getResource() {
		return this.project;
	}

	protected JavaResourceTypeCache buildExternalJavaResourceTypeCache() {
		return new BinaryTypeCache(this.jpaPlatform.getAnnotationProvider());
	}

	protected JpaRootContextNode buildRootContextNode() {
		return this.getJpaFactory().buildRootContextNode(this);
	}


	// ********** initial resource proxy visitor **********

	protected InitialResourceProxyVisitor buildInitialResourceProxyVisitor() {
		return new InitialResourceProxyVisitor();
	}

	protected class InitialResourceProxyVisitor implements IResourceProxyVisitor {
		protected InitialResourceProxyVisitor() {
			super();
		}
		protected void visitProject(IProject p) {
			try {
				p.accept(this, IResource.NONE);
			} catch (CoreException ex) {
				// shouldn't happen - we don't throw any CoreExceptions
				throw new RuntimeException(ex);
			}
		}
		// add a JPA file for every [appropriate] file encountered by the visitor
		public boolean visit(IResourceProxy resource) {
			switch (resource.getType()) {
				case IResource.ROOT :  // shouldn't happen
					return true;  // visit children
				case IResource.PROJECT :
					return true;  // visit children
				case IResource.FOLDER :
					return true;  // visit children
				case IResource.FILE :
					AbstractJpaProject.this.addJpaFile_((IFile) resource.requestResource());
					return false;  // no children
				default :
					return false;  // no children
			}
		}
	}


	// ********** misc **********

	/**
	 * Ignore changes to this collection. Adds can be ignored since they are triggered
	 * by requests that will, themselves, trigger updates (typically during the
	 * update of an object that calls a setter with the newly-created resource
	 * type). Deletes will be accompanied by manual updates.
	 */
	@Override
	protected void addNonUpdateAspectNamesTo(Set<String> nonUpdateAspectNames) {
		super.addNonUpdateAspectNamesTo(nonUpdateAspectNames);
		nonUpdateAspectNames.add(EXTERNAL_JAVA_RESOURCE_COMPILATION_UNITS_COLLECTION);
	}


	// ********** general queries **********

	@Override
	public JpaProject getJpaProject() {
		return this;
	}

	public String getName() {
		return this.project.getName();
	}
	
	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.getName());
	}

	public IProject getProject() {
		return this.project;
	}

	public IJavaProject getJavaProject() {
		return JavaCore.create(this.project);
	}

	@Override
	public JpaPlatform getJpaPlatform() {
		return this.jpaPlatform;
	}

	@SuppressWarnings("unchecked")
	protected Iterable<JavaResourceCompilationUnit> getCombinedJavaResourceCompilationUnits() {
		return new CompositeIterable<JavaResourceCompilationUnit>(
					this.getInternalJavaResourceCompilationUnits(),
					this.getExternalJavaResourceCompilationUnits()
				);
	}


	// ********** database **********

	@Override
	public JpaDataSource getDataSource() {
		return this.dataSource;
	}

	public ConnectionProfile getConnectionProfile() {
		return this.dataSource.getConnectionProfile();
	}

	/**
	 * If we don't have a catalog (i.e. we don't even have a <em>default</em>
	 * catalog), then the database probably does not support catalogs.
	 */
	public Catalog getDefaultDbCatalog() {
		String catalog = this.getDefaultCatalog();
		return (catalog == null) ? null : this.resolveDbCatalog(catalog);
	}

	public String getDefaultCatalog() {
		String catalog = this.getUserOverrideDefaultCatalog();
		return (catalog != null) ? catalog : this.getDatabaseDefaultCatalog();
	}

	protected String getDatabaseDefaultCatalog() {
		Database db = this.getDatabase();
		return (db == null ) ? null : db.getDefaultCatalogIdentifier();
	}

	/**
	 * If we don't have a catalog (i.e. we don't even have a <em>default</em> catalog),
	 * then the database probably does not support catalogs; and we need to
	 * get the schema directly from the database.
	 */
	public SchemaContainer getDefaultDbSchemaContainer() {
		String catalog = this.getDefaultCatalog();
		return (catalog != null) ? this.resolveDbCatalog(catalog) : this.getDatabase();
	}

	public Schema getDefaultDbSchema() {
		SchemaContainer sc = this.getDefaultDbSchemaContainer();
		return (sc == null) ? null : sc.getSchemaForIdentifier(this.getDefaultSchema());
	}

	public String getDefaultSchema() {
		String schema = this.getUserOverrideDefaultSchema();
		if (schema != null) {
			return schema;
		}

		String catalog = this.getDefaultCatalog();
		if (catalog == null) {
			// if there is no default catalog (either user-override or database-determined),
			// the database probably does not support catalogs;
			// return the database's default schema
			return this.getDatabaseDefaultSchema();
		}

		Catalog dbCatalog = this.resolveDbCatalog(catalog);
		if (dbCatalog != null) {
			return dbCatalog.getDefaultSchemaIdentifier();
		}

		// if we could not find a catalog on the database that matches the default
		// catalog name, return the database's default schema(?) - hmmm
		return this.getDatabaseDefaultSchema();
	}

	protected String getDatabaseDefaultSchema() {
		Database db = this.getDatabase();
		return (db == null ) ? null : db.getDefaultSchemaIdentifier();
	}


	// ********** user override default catalog **********
	
	public String getUserOverrideDefaultCatalog() {
		return this.userOverrideDefaultCatalog;
	}
	
	public void setUserOverrideDefaultCatalog(String catalog) {
		String old = this.userOverrideDefaultCatalog;
		this.userOverrideDefaultCatalog = catalog;
		JptJpaCorePlugin.setUserOverrideDefaultCatalog(this.project, catalog);
		this.firePropertyChanged(USER_OVERRIDE_DEFAULT_CATALOG_PROPERTY, old, catalog);
	}
	
	
	// ********** user override default schema **********
	
	public String getUserOverrideDefaultSchema() {
		return this.userOverrideDefaultSchema;
	}
	
	public void setUserOverrideDefaultSchema(String schema) {
		String old = this.userOverrideDefaultSchema;
		this.userOverrideDefaultSchema = schema;
		JptJpaCorePlugin.setUserOverrideDefaultSchema(this.project, schema);
		this.firePropertyChanged(USER_OVERRIDE_DEFAULT_SCHEMA_PROPERTY, old, schema);
	}
	
	
	// ********** discover annotated classes **********
	
	public boolean discoversAnnotatedClasses() {
		return this.discoversAnnotatedClasses;
	}
	
	public void setDiscoversAnnotatedClasses(boolean discoversAnnotatedClasses) {
		boolean old = this.discoversAnnotatedClasses;
		this.discoversAnnotatedClasses = discoversAnnotatedClasses;
		JptJpaCorePlugin.setDiscoverAnnotatedClasses(this.project, discoversAnnotatedClasses);
		this.firePropertyChanged(DISCOVERS_ANNOTATED_CLASSES_PROPERTY, old, discoversAnnotatedClasses);
	}
	
	
	// ********** JPA files **********

	public Iterable<JpaFile> getJpaFiles() {
		return new LiveCloneIterable<JpaFile>(this.jpaFiles);  // read-only
	}

	public int getJpaFilesSize() {
		return this.jpaFiles.size();
	}

	protected Iterable<JpaFile> getJpaFiles(final IContentType contentType) {
		return new FilteringIterable<JpaFile>(this.getJpaFiles()) {
			@Override
			protected boolean accept(JpaFile jpaFile) {
				return jpaFile.getContentType().isKindOf(contentType);
			}
		};
	}

	@Override
	public JpaFile getJpaFile(IFile file) {
		for (JpaFile jpaFile : this.getJpaFiles()) {
			if (jpaFile.getFile().equals(file)) {
				return jpaFile;
			}
		}
		return null;
	}

	/**
	 * Add a JPA file for the specified file, if appropriate.
	 * Return true if a JPA File was created and added, false otherwise
	 */
	protected boolean addJpaFile(IFile file) {
		JpaFile jpaFile = this.addJpaFile_(file);
		if (jpaFile != null) {
			this.fireItemAdded(JPA_FILES_COLLECTION, jpaFile);
			return true;
		}
		return false;
	}

	/**
	 * Add a JPA file for the specified file, if appropriate, without firing
	 * an event; useful during construction.
	 * Return the new JPA file, null if it was not created.
	 */
	protected JpaFile addJpaFile_(IFile file) {
		if (this.fileIsJavaRelated(file)) {
			if ( ! this.getJavaProject().isOnClasspath(file)) {
				return null;  // java-related files must be on the Java classpath
			}
		}
		else if ( ! this.fileResourceLocationIsValid(file)) {
			return null;  
		}

		JpaFile jpaFile = this.buildJpaFile(file);
		if (jpaFile == null) {
			return null;
		}
		jpaFile.getResourceModel().addResourceModelListener(this.resourceModelListener);
		this.jpaFiles.add(jpaFile);
		return jpaFile;
	}

	/**
	 * <code>.java</code> or <code>.jar</code>
	 */
	protected boolean fileIsJavaRelated(IFile file) {
		IContentType contentType = PlatformTools.getContentType(file);
		return (contentType != null) && this.contentTypeIsJavaRelated(contentType);
	}

	/**
	 * pre-condition: content type is not <code>null</code>
	 */
	protected boolean contentTypeIsJavaRelated(IContentType contentType) {
		return contentType.isKindOf(JptCommonCorePlugin.JAVA_SOURCE_CONTENT_TYPE) ||
				contentType.isKindOf(JptCommonCorePlugin.JAR_CONTENT_TYPE);
	}

	protected boolean fileResourceLocationIsValid(IFile file) {
		ResourceLocator resourceLocator = JptCommonCorePlugin.getResourceLocator(this.getProject());
		return resourceLocator.acceptResourceLocation(this.getProject(), file.getParent());
	}

	/**
	 * Log any developer exceptions and don't build a JPA file rather
	 * than completely failing to build the JPA Project.
	 */
	protected JpaFile buildJpaFile(IFile file) {
		try {
			return this.getJpaPlatform().buildJpaFile(this, file);
		} catch (Exception ex) {
			JptJpaCorePlugin.log(ex);
			return null;
		}
	}

	/**
	 * Remove the JPA file corresponding to the specified IFile, if it exists.
	 * Return true if a JPA File was removed, false otherwise
	 */
	protected boolean removeJpaFile(IFile file) {
		JpaFile jpaFile = this.getJpaFile(file);
		if (jpaFile != null) {  // a JpaFile is not added for every IFile
			this.removeJpaFile(jpaFile);
			return true;
		}
		return false;
	}

	/**
	 * Stop listening to the JPA file and remove it.
	 */
	protected void removeJpaFile(JpaFile jpaFile) {
		jpaFile.getResourceModel().removeResourceModelListener(this.resourceModelListener);
		if ( ! this.removeItemFromCollection(jpaFile, this.jpaFiles, JPA_FILES_COLLECTION)) {
			throw new IllegalArgumentException(jpaFile.toString());
		}
	}


	// ********** external Java resource types (source or binary) **********

	protected JavaResourceAbstractType buildExternalJavaResourceType(String typeName) {
		IType jdtType = this.findType(typeName);
		return (jdtType == null) ? null : this.buildExternalJavaResourceType(jdtType);
	}

	protected IType findType(String typeName) {
		try {
			return this.getJavaProject().findType(typeName);
		} catch (JavaModelException ex) {
			return null;  // ignore exception?
		}
	}

	protected JavaResourceAbstractType buildExternalJavaResourceType(IType jdtType) {
		return jdtType.isBinary() ?
				this.buildBinaryExternalJavaResourceType(jdtType) :
				this.buildSourceExternalJavaResourceType(jdtType);
	}

	protected JavaResourceAbstractType buildBinaryExternalJavaResourceType(IType jdtType) {
		return this.externalJavaResourceTypeCache.addType(jdtType);
	}

	protected JavaResourceAbstractType buildSourceExternalJavaResourceType(IType jdtType) {
		JavaResourceCompilationUnit jrcu = this.getExternalJavaResourceCompilationUnit(jdtType.getCompilationUnit());
		String jdtTypeName = jdtType.getFullyQualifiedName('.');  // JDT member type names use '$'
		for (JavaResourceAbstractType jrat : jrcu.getTypes()) {
			if (jrat.getQualifiedName().equals(jdtTypeName)) {
				return jrat;
			}
		}
		// we can get here if the project JRE is removed;
		// see SourceCompilationUnit#getPrimaryType(CompilationUnit)
		// bug 225332
		return null;
	}


	// ********** external Java resource persistent types (binary) **********

	public JavaResourceTypeCache getExternalJavaResourceTypeCache() {
		return this.externalJavaResourceTypeCache;
	}


	// ********** external Java resource compilation units (source) **********

	public Iterable<JavaResourceCompilationUnit> getExternalJavaResourceCompilationUnits() {
		return new LiveCloneIterable<JavaResourceCompilationUnit>(this.externalJavaResourceCompilationUnits);  // read-only
	}

	public int getExternalJavaResourceCompilationUnitsSize() {
		return this.externalJavaResourceCompilationUnits.size();
	}

	/**
	 * Return the resource model compilation unit corresponding to the specified
	 * JDT compilation unit. If it does not exist, build it.
	 */
	protected JavaResourceCompilationUnit getExternalJavaResourceCompilationUnit(ICompilationUnit jdtCompilationUnit) {
		for (JavaResourceCompilationUnit jrcu : this.getExternalJavaResourceCompilationUnits()) {
			if (jrcu.getCompilationUnit().equals(jdtCompilationUnit)) {
				// we will get here if the JRCU could not build its persistent type...
				return jrcu;
			}
		}
		return this.addExternalJavaResourceCompilationUnit(jdtCompilationUnit);
	}

	/**
	 * Add an external Java resource compilation unit.
	 */
	protected JavaResourceCompilationUnit addExternalJavaResourceCompilationUnit(ICompilationUnit jdtCompilationUnit) {
		JavaResourceCompilationUnit jrcu = this.buildJavaResourceCompilationUnit(jdtCompilationUnit);
		this.addItemToCollection(jrcu, this.externalJavaResourceCompilationUnits, EXTERNAL_JAVA_RESOURCE_COMPILATION_UNITS_COLLECTION);
		jrcu.addResourceModelListener(this.resourceModelListener);
		return jrcu;
	}

	protected JavaResourceCompilationUnit buildJavaResourceCompilationUnit(ICompilationUnit jdtCompilationUnit) {
		return new SourceTypeCompilationUnit(
					jdtCompilationUnit,
					this.jpaPlatform.getAnnotationProvider(),
					this.jpaPlatform.getAnnotationEditFormatter(),
					this.modifySharedDocumentCommandExecutor
				);
	}

	protected boolean removeExternalJavaResourceCompilationUnit(IFile file) {
		for (JavaResourceCompilationUnit jrcu : this.getExternalJavaResourceCompilationUnits()) {
			if (jrcu.getFile().equals(file)) {
				this.removeExternalJavaResourceCompilationUnit(jrcu);
				return true;
			}
		}
		return false;
	}

	protected void removeExternalJavaResourceCompilationUnit(JavaResourceCompilationUnit jrcu) {
		jrcu.removeResourceModelListener(this.resourceModelListener);
		this.removeItemFromCollection(jrcu, this.externalJavaResourceCompilationUnits, EXTERNAL_JAVA_RESOURCE_COMPILATION_UNITS_COLLECTION);
	}


	// ********** context model **********

	public JpaRootContextNode getRootContextNode() {
		return this.rootContextNode;
	}


	// ********** utility **********

	public IFile getPlatformFile(IPath runtimePath) {
		return JptCommonCorePlugin.getPlatformFile(this.project, runtimePath);
	}


	// ********** XML files **********

	public JpaXmlResource getPersistenceXmlResource() {
		return (JpaXmlResource) this.getResourceModel(
				JptJpaCorePlugin.DEFAULT_PERSISTENCE_XML_RUNTIME_PATH,
				JptJpaCorePlugin.PERSISTENCE_XML_CONTENT_TYPE
			);
	}

	public JpaXmlResource getDefaultOrmXmlResource() {
		return this.getMappingFileXmlResource(JptJpaCorePlugin.DEFAULT_ORM_XML_RUNTIME_PATH);
	}

	public JpaXmlResource getMappingFileXmlResource(IPath runtimePath) {
		return (JpaXmlResource) this.getResourceModel(runtimePath, JptJpaCorePlugin.MAPPING_FILE_CONTENT_TYPE);
	}

	/**
	 * If the specified file exists, is significant to the JPA project, and its
	 * content is a "kind of" the specified content type, return the JPA
	 * resource model corresponding to the file; otherwise, return null.
	 */
	protected JptResourceModel getResourceModel(IPath runtimePath, IContentType contentType) {
		IFile file = this.getPlatformFile(runtimePath);
		return (file != null && file.exists()) ? this.getResourceModel(file, contentType) :  null;
	}

	/**
	 * If the specified file is significant to the JPA project and its content
	 * is a "kind of" the specified content type, return the JPA resource model
	 * corresponding to the file; otherwise, return null.
	 */
	protected JptResourceModel getResourceModel(IFile file, IContentType contentType) {
		JpaFile jpaFile = this.getJpaFile(file);
		return (jpaFile == null) ? null : jpaFile.getResourceModel(contentType);
	}


	// ********** annotated Java source classes **********

	public Iterable<String> getAnnotatedJavaSourceClassNames() {
		return new TransformationIterable<JavaResourceAbstractType, String>(this.getInternalAnnotatedSourceJavaResourceTypes()) {
			@Override
			protected String transform(JavaResourceAbstractType jraType) {
				return jraType.getQualifiedName();
			}
		};
	}

	/**
	 * Return only those valid annotated Java resource persistent types that are
	 * directly part of the JPA project, ignoring those in JARs referenced in
	 * <code>persistence.xml</code>.
	 * @see org.eclipse.jpt.common.core.internal.utility.jdt.JPTTools#typeIsPersistable(org.eclipse.jpt.common.core.internal.utility.jdt.JPTTools.TypeAdapter)
	 */
	protected Iterable<JavaResourceAbstractType> getInternalAnnotatedSourceJavaResourceTypes() {
		return new FilteringIterable<JavaResourceAbstractType>(this.getInternalSourceJavaResourceTypes()) {
			@Override
			protected boolean accept(JavaResourceAbstractType jraType) {
				return jraType.isAnnotated();  // i.e. the type has a valid type annotation
			}
		};
	}

	/**
	 * Return only the names of those valid <em>mapped</em> (i.e. annotated with
	 * <code>@Entity</code>, <code>@Embeddable</code>, etc.) Java resource
	 * persistent types that are directly part of the JPA project, ignoring
	 * those in JARs referenced in <code>persistence.xml</code>.
	 */
	public Iterable<String> getMappedJavaSourceClassNames() {
		return new TransformationIterable<JavaResourceAbstractType, String>(this.getInternalMappedSourceJavaResourceTypes()) {
			@Override
			protected String transform(JavaResourceAbstractType jraType) {
				return jraType.getQualifiedName();
			}
		};
	}

	/**
	 * Return only those valid <em>mapped</em> (i.e. annotated with
	 * <code>@Entity</code>, <code>@Embeddable</code>, etc.) Java resource
	 * persistent types that are directly part of the JPA project, ignoring
	 * those in JARs referenced in <code>persistence.xml</code>.
	 */
	protected Iterable<JavaResourceAbstractType> getInternalMappedSourceJavaResourceTypes() {
		return new FilteringIterable<JavaResourceAbstractType>(this.getInternalAnnotatedSourceJavaResourceTypes()) {
			@Override
			protected boolean accept(JavaResourceAbstractType jraType) {
				return jraType.isAnnotatedWith(getTypeMappingAnnotations());
			}
		};
	}

	public Iterable<String> getTypeMappingAnnotations() {
		return new TransformationIterable<JavaTypeMappingDefinition, String>(getJpaPlatform().getJavaTypeMappingDefinitions()) {
			@Override
			protected String transform(JavaTypeMappingDefinition o) {
				return o.getAnnotationName();
			}
		};
	}

	/**
	 * Return only those Java resource persistent types that are directly
	 * part of the JPA project, ignoring those in JARs referenced in
	 * <code>persistence.xml</code>
	 */
	protected Iterable<JavaResourceAbstractType> getInternalSourceJavaResourceTypes() {
		return new CompositeIterable<JavaResourceAbstractType>(this.getInternalSourceJavaResourceTypeLists());
	}

	/**
	 * Return only those Java resource persistent types that are directly
	 * part of the JPA project, ignoring those in JARs referenced in
	 * <code>persistence.xml</code>
	 */
	protected Iterable<Iterable<JavaResourceAbstractType>> getInternalSourceJavaResourceTypeLists() {
		return new TransformationIterable<JavaResourceCompilationUnit, Iterable<JavaResourceAbstractType>>(this.getInternalJavaResourceCompilationUnits()) {
			@Override
			protected Iterable<JavaResourceAbstractType> transform(final JavaResourceCompilationUnit compilationUnit) {
				return compilationUnit.getTypes();  // *all* the types in the compilation unit
			}
		};
	}

	/**
	 * Return the JPA project's resource compilation units.
	 */
	protected Iterable<JavaResourceCompilationUnit> getInternalJavaResourceCompilationUnits() {
		return new TransformationIterable<JpaFile, JavaResourceCompilationUnit>(this.getJavaSourceJpaFiles()) {
			@Override
			protected JavaResourceCompilationUnit transform(JpaFile jpaFile) {
				return (JavaResourceCompilationUnit) jpaFile.getResourceModel();
			}
		};
	}

	/**
	 * Return the JPA project's JPA files with Java source <em>content</em>.
	 */
	protected Iterable<JpaFile> getJavaSourceJpaFiles() {
		return this.getJpaFiles(JptCommonCorePlugin.JAVA_SOURCE_CONTENT_TYPE);
	}


	// ********** Java resource persistent type look-up **********

	public JavaResourceAbstractType getJavaResourceType(String typeName) {
		for (JavaResourceAbstractType jraType : this.getJavaResourceTypes()) {
			if (jraType.getQualifiedName().equals(typeName)) {
				return jraType;
			}
		}
		// if we don't have a type already, try to build new one from the project classpath
		return this.buildExternalJavaResourceType(typeName);
	}

	public JavaResourceAbstractType getJavaResourceType(String typeName, JavaResourceAbstractType.Kind kind) {
		JavaResourceAbstractType resourceType = getJavaResourceType(typeName);
		if (resourceType == null || resourceType.getKind() != kind) {
			return null;
		}
		return resourceType;
	}


	/**
	 * return *all* the Java resource persistent types, including those in JARs referenced in
	 * persistence.xml
	 */
	protected Iterable<JavaResourceAbstractType> getJavaResourceTypes() {
		return new CompositeIterable<JavaResourceAbstractType>(this.getJavaResourceTypeSets());
	}

	/**
	 * return *all* the Java resource persistent types, including those in JARs referenced in
	 * persistence.xml
	 */
	protected Iterable<Iterable<JavaResourceAbstractType>> getJavaResourceTypeSets() {
		return new TransformationIterable<JavaResourceNode.Root, Iterable<JavaResourceAbstractType>>(this.getJavaResourceNodeRoots()) {
			@Override
			protected Iterable<JavaResourceAbstractType> transform(final JavaResourceNode.Root root) {
				return root.getTypes();  // *all* the types held by the root
			}
		};
	}

	@SuppressWarnings("unchecked")
	protected Iterable<JavaResourceNode.Root> getJavaResourceNodeRoots() {
		return new CompositeIterable<JavaResourceNode.Root>(
					this.getInternalJavaResourceCompilationUnits(),
					this.getInternalJavaResourcePackageFragmentRoots(),
					this.getExternalJavaResourceCompilationUnits(),
					Collections.singleton(this.externalJavaResourceTypeCache)
				);
	}


	// ********** Java resource persistent package look-up **********

	public JavaResourcePackage getJavaResourcePackage(String packageName) {
		for (JavaResourcePackage jrp : this.getJavaResourcePackages()) {
			if (jrp.getName().equals(packageName)) {
				return jrp;
			}
		}
		return null;
	}

	public Iterable<JavaResourcePackage> getJavaResourcePackages(){
		return new FilteringIterable<JavaResourcePackage>( 
				new TransformationIterable<JpaFile, JavaResourcePackage>(this.getPackageInfoSourceJpaFiles()) {
				@Override
				protected JavaResourcePackage transform(JpaFile jpaFile) {
					return ((JavaResourcePackageInfoCompilationUnit) jpaFile.getResourceModel()).getPackage();
				}
			}) 
			{
			@Override
			protected boolean accept(JavaResourcePackage packageInfo) {
				return packageInfo != null;
			}
		};
	}

	/**
	 * return JPA files with package-info source "content"
	 */
	protected Iterable<JpaFile> getPackageInfoSourceJpaFiles() {
		return this.getJpaFiles(JptCommonCorePlugin.JAVA_SOURCE_PACKAGE_INFO_CONTENT_TYPE);
	}


	// ********** JARs **********

	// TODO
	public JavaResourcePackageFragmentRoot getJavaResourcePackageFragmentRoot(String jarFileName) {
//		return this.getJarResourcePackageFragmentRoot(this.convertToPlatformFile(jarFileName));
		return this.getJavaResourcePackageFragmentRoot(this.getProject().getFile(jarFileName));
	}

	protected JavaResourcePackageFragmentRoot getJavaResourcePackageFragmentRoot(IFile jarFile) {
		for (JavaResourcePackageFragmentRoot pfr : this.getInternalJavaResourcePackageFragmentRoots()) {
			if (pfr.getFile().equals(jarFile)) {
				return pfr;
			}
		}
		return null;
	}

	protected Iterable<JavaResourcePackageFragmentRoot> getInternalJavaResourcePackageFragmentRoots() {
		return new TransformationIterable<JpaFile, JavaResourcePackageFragmentRoot>(this.getJarJpaFiles()) {
			@Override
			protected JavaResourcePackageFragmentRoot transform(JpaFile jpaFile) {
				return (JavaResourcePackageFragmentRoot) jpaFile.getResourceModel();
			}
		};
	}

	/**
	 * return JPA files with JAR "content"
	 */
	public Iterable<JpaFile> getJarJpaFiles() {
		return this.getJpaFiles(JptCommonCorePlugin.JAR_CONTENT_TYPE);
	}


	// ********** metamodel **********

	public Iterable<JavaResourceAbstractType> getGeneratedMetamodelTopLevelTypes() {
		if (this.metamodelSourceFolderName == null) {
			return EmptyIterable.instance();
		}
		final IPackageFragmentRoot genSourceFolder = this.getMetamodelPackageFragmentRoot();
		return new FilteringIterable<JavaResourceAbstractType>(this.getInternalSourceJavaResourceTypes()) {
			@Override
			protected boolean accept(JavaResourceAbstractType jrat) {
				return MetamodelTools.isGeneratedMetamodelTopLevelType(jrat, genSourceFolder);
			}
		};
	}

	public JavaResourceAbstractType getGeneratedMetamodelTopLevelType(IFile file) {
		JavaResourceCompilationUnit jrcu = this.getJavaResourceCompilationUnit(file);
		if (jrcu == null) {
			return null;  // hmmm...
		}
		JavaResourceAbstractType primaryType = jrcu.getPrimaryType();
		if (primaryType == null) {
			return null;  // no types in the file
		}
		return MetamodelTools.isGeneratedMetamodelTopLevelType(primaryType) ? primaryType : null;
	}

	protected JavaResourceCompilationUnit getJavaResourceCompilationUnit(IFile file) {
		return (JavaResourceCompilationUnit) this.getResourceModel(file, JptCommonCorePlugin.JAVA_SOURCE_CONTENT_TYPE);
	}

	public String getMetamodelSourceFolderName() {
		return this.metamodelSourceFolderName;
	}

	public void setMetamodelSourceFolderName(String folderName) {
		if (this.setMetamodelSourceFolderName_(folderName)) {
			JptJpaCorePlugin.setMetamodelSourceFolderName(this.project, folderName);
			if (folderName == null) {
				this.disposeMetamodel();
			} else {
				this.initializeMetamodel();
			}
		}
	}

	protected boolean setMetamodelSourceFolderName_(String folderName) {
		String old = this.metamodelSourceFolderName;
		this.metamodelSourceFolderName = folderName;
		return this.firePropertyChanged(METAMODEL_SOURCE_FOLDER_NAME_PROPERTY, old, folderName);
	}

	public void initializeMetamodel() {
		if (this.isJpa2_0Compatible()) {
			((JpaRootContextNode2_0) this.rootContextNode).initializeMetamodel();
		}
	}

	/**
	 * Synchronize the metamodel for 2.0-compatible JPA projects.
	 */
	public void synchronizeMetamodel() {
		if (this.isJpa2_0Compatible()) {
			if (this.metamodelSourceFolderName != null) {
				((JpaRootContextNode2_0) this.rootContextNode).synchronizeMetamodel();
			}
		}
	}

	public void disposeMetamodel() {
		if (this.isJpa2_0Compatible()) {
			((JpaRootContextNode2_0) this.rootContextNode).disposeMetamodel();
		}
	}

	public IPackageFragmentRoot getMetamodelPackageFragmentRoot() {
		return this.getJavaProject().getPackageFragmentRoot(this.getMetaModelSourceFolder());
	}

	protected IFolder getMetaModelSourceFolder() {
		return this.getProject().getFolder(this.metamodelSourceFolderName);
	}

	/**
	 * If the metamodel source folder is no longer a Java project source
	 * folder, clear it out.
	 */
	protected void checkMetamodelSourceFolderName() {
		if (this.metamodelSoureFolderNameIsInvalid()) {
			this.setMetamodelSourceFolderName(null);
		}
	}

	protected boolean metamodelSoureFolderNameIsInvalid() {
		return ! this.metamodelSourceFolderNameIsValid();
	}

	protected boolean metamodelSourceFolderNameIsValid() {
		return CollectionTools.contains(this.getJavaSourceFolderNames(), this.metamodelSourceFolderName);
	}


	// ********** Java source folder names **********

	public Iterable<String> getJavaSourceFolderNames() {
		try {
			return this.getJavaSourceFolderNames_();
		} catch (JavaModelException ex) {
			JptJpaCorePlugin.log(ex);
			return EmptyIterable.instance();
		}
	}

	protected Iterable<String> getJavaSourceFolderNames_() throws JavaModelException {
		return new TransformationIterable<IPackageFragmentRoot, String>(this.getJavaSourceFolders()) {
			@Override
			protected String transform(IPackageFragmentRoot pfr) {
				try {
					return this.transform_(pfr);
				} catch (JavaModelException ex) {
					return "Error: " + pfr.getPath(); //$NON-NLS-1$
				}
			}
			private String transform_(IPackageFragmentRoot pfr) throws JavaModelException {
				return pfr.getUnderlyingResource().getProjectRelativePath().toString();
			}
		};
	}

	protected Iterable<IPackageFragmentRoot> getJavaSourceFolders() throws JavaModelException {
		return new FilteringIterable<IPackageFragmentRoot>(
				this.getPackageFragmentRoots(),
				SOURCE_PACKAGE_FRAGMENT_ROOT_FILTER
			);
	}

	protected static final Filter<IPackageFragmentRoot> SOURCE_PACKAGE_FRAGMENT_ROOT_FILTER =
		new Filter<IPackageFragmentRoot>() {
			public boolean accept(IPackageFragmentRoot pfr) {
				try {
					return this.accept_(pfr);
				} catch (JavaModelException ex) {
					return false;
				}
			}
			private boolean accept_(IPackageFragmentRoot pfr) throws JavaModelException {
				return pfr.exists() && (pfr.getKind() == IPackageFragmentRoot.K_SOURCE);
			}
		};

	protected Iterable<IPackageFragmentRoot> getPackageFragmentRoots() throws JavaModelException {
		return new ArrayIterable<IPackageFragmentRoot>(this.getJavaProject().getPackageFragmentRoots());
	}


	// ********** Java events **********

	// TODO handle changes to external projects
	public void javaElementChanged(ElementChangedEvent event) {
		this.processJavaDelta(event.getDelta());
	}

	/**
	 * We recurse back here from {@link #processJavaDeltaChildren(IJavaElementDelta)}.
	 */
	protected void processJavaDelta(IJavaElementDelta delta) {
		switch (delta.getElement().getElementType()) {
			case IJavaElement.JAVA_MODEL :
				this.processJavaModelDelta(delta);
				break;
			case IJavaElement.JAVA_PROJECT :
				this.processJavaProjectDelta(delta);
				break;
			case IJavaElement.PACKAGE_FRAGMENT_ROOT :
				this.processJavaPackageFragmentRootDelta(delta);
				break;
			case IJavaElement.PACKAGE_FRAGMENT :
				this.processJavaPackageFragmentDelta(delta);
				break;
			case IJavaElement.COMPILATION_UNIT :
				this.processJavaCompilationUnitDelta(delta);
				break;
			default :
				break; // ignore the elements inside a compilation unit
		}
	}

	protected void processJavaDeltaChildren(IJavaElementDelta delta) {
		for (IJavaElementDelta child : delta.getAffectedChildren()) {
			this.processJavaDelta(child); // recurse
		}
	}

	/**
	 * Return whether the specified Java element delta is for a
	 * {@link IJavaElementDelta#CHANGED CHANGED}
	 * (as opposed to {@link IJavaElementDelta#ADDED ADDED} or
	 * {@link IJavaElementDelta#REMOVED REMOVED}) Java element
	 * and the specified flag is set.
	 * (The delta flags are only significant if the delta
	 * {@link IJavaElementDelta#getKind() kind} is
	 * {@link IJavaElementDelta#CHANGED CHANGED}.)
	 */
	protected boolean deltaFlagIsSet(IJavaElementDelta delta, int flag) {
		return (delta.getKind() == IJavaElementDelta.CHANGED) &&
				BitTools.flagIsSet(delta.getFlags(), flag);
	}

	// ***** model
	protected void processJavaModelDelta(IJavaElementDelta delta) {
		// process the Java model's projects
		this.processJavaDeltaChildren(delta);
	}

	// ***** project
	protected void processJavaProjectDelta(IJavaElementDelta delta) {
		// process the Java project's package fragment roots
		this.processJavaDeltaChildren(delta);

		// a classpath change can have pretty far-reaching effects...
		if (this.classpathHasChanged(delta)) {
			this.rebuild((IJavaProject) delta.getElement());
		}
	}

	/**
	 * The specified Java project's classpath changed. Rebuild the JPA project
	 * as appropriate.
	 */
	protected void rebuild(IJavaProject javaProject) {
		// if the classpath has changed, we need to update everything since
		// class references could now be resolved (or not) etc.
		if (javaProject.equals(this.getJavaProject())) {
			this.removeDeadJpaFiles();
			this.checkMetamodelSourceFolderName();
			this.synchronizeWithJavaSource(this.getInternalJavaResourceCompilationUnits());
		} else {
			// TODO see if changed project is on our classpath?
			this.synchronizeWithJavaSource(this.getExternalJavaResourceCompilationUnits());
		}
	}

	/**
	 * Loop through all our JPA files, remove any that are no longer on the
	 * classpath.
	 */
	protected void removeDeadJpaFiles() {
		for (JpaFile jpaFile : this.getJpaFiles()) {
			if (this.jpaFileIsDead(jpaFile)) {
				this.removeJpaFile(jpaFile);
			}
		}
	}

	protected boolean jpaFileIsDead(JpaFile jpaFile) {
		return ! this.jpaFileIsAlive(jpaFile);
	}

	/**
	 * Sometimes (e.g. during tests), when a project has been deleted, we get a
	 * Java change event that indicates the Java project is CHANGED (as
	 * opposed to REMOVED, which is what typically happens). The event's delta
	 * indicates that everything in the Java project has been deleted and the
	 * classpath has changed. All entries in the classpath have been removed;
	 * but single entry for the Java project's root folder has been added. (!)
	 * This means any file in the project is on the Java project's classpath.
	 * This classpath change is what triggers us to rebuild the JPA project; so
	 * we put an extra check here to make sure the JPA file's resource file is
	 * still present.
	 * <p>
	 * This would not be a problem if Dali received the resource change event
	 * <em>before</em> JDT and simply removed the JPA project; but JDT receives
	 * the resource change event first and converts it into the problematic
	 * Java change event.... 
	 */
	protected boolean jpaFileIsAlive(JpaFile jpaFile) {
		IFile file = jpaFile.getFile();
		return this.getJavaProject().isOnClasspath(file) &&
				file.exists();
	}

	/**
	 * pre-condition:
	 * delta.getElement().getElementType() == IJavaElement.JAVA_PROJECT
	 */
	protected boolean classpathHasChanged(IJavaElementDelta delta) {
		return this.deltaFlagIsSet(delta, IJavaElementDelta.F_RESOLVED_CLASSPATH_CHANGED);
	}

	protected void synchronizeWithJavaSource(Iterable<JavaResourceCompilationUnit> javaResourceCompilationUnits) {
		for (JavaResourceCompilationUnit javaResourceCompilationUnit : javaResourceCompilationUnits) {
			javaResourceCompilationUnit.synchronizeWithJavaSource();
		}
	}

	// ***** package fragment root
	protected void processJavaPackageFragmentRootDelta(IJavaElementDelta delta) {
		// process the Java package fragment root's package fragments
		this.processJavaDeltaChildren(delta);

		if (this.classpathEntryHasBeenAdded(delta)) {
			// TODO bug 277218
		} else if (this.classpathEntryHasBeenRemoved(delta)) {  // should be mutually-exclusive w/added (?)
			// TODO bug 277218
		}
	}

	/**
	 * pre-condition:
	 * delta.getElement().getElementType() == IJavaElement.PACKAGE_FRAGMENT_ROOT
	 */
	protected boolean classpathEntryHasBeenAdded(IJavaElementDelta delta) {
		return this.deltaFlagIsSet(delta, IJavaElementDelta.F_ADDED_TO_CLASSPATH);
	}

	/**
	 * pre-condition:
	 * delta.getElement().getElementType() == IJavaElement.PACKAGE_FRAGMENT_ROOT
	 */
	protected boolean classpathEntryHasBeenRemoved(IJavaElementDelta delta) {
		return this.deltaFlagIsSet(delta, IJavaElementDelta.F_REMOVED_FROM_CLASSPATH);
	}

	// ***** package fragment
	protected void processJavaPackageFragmentDelta(IJavaElementDelta delta) {
		// process the java package fragment's compilation units
		this.processJavaDeltaChildren(delta);
	}

	// ***** compilation unit
	protected void processJavaCompilationUnitDelta(IJavaElementDelta delta) {
		if (this.javaCompilationUnitDeltaIsRelevant(delta)) {
			ICompilationUnit compilationUnit = (ICompilationUnit) delta.getElement();
			for (JavaResourceCompilationUnit jrcu : this.getCombinedJavaResourceCompilationUnits()) {
				if (jrcu.getCompilationUnit().equals(compilationUnit)) {
					jrcu.synchronizeWithJavaSource();
					// TODO ? this.resolveJavaTypes();  // might have new member types now...
					break;  // there *shouldn't* be any more...
				}
			}
		}
		// ignore the java compilation unit's children
	}

	protected boolean javaCompilationUnitDeltaIsRelevant(IJavaElementDelta delta) {
		// ignore changes to/from primary working copy - no content has changed;
		// and make sure there are no other flags set that indicate *both* a
		// change to/from primary working copy *and* content has changed
		if (BitTools.onlyFlagIsSet(delta.getFlags(), IJavaElementDelta.F_PRIMARY_WORKING_COPY)) {
			return false;
		}

		// ignore java notification for ADDED or REMOVED;
		// these are handled via resource notification
		return delta.getKind() == IJavaElementDelta.CHANGED;
	}


	// ********** validation **********
	
	public Iterable<IMessage> getValidationMessages(IReporter reporter) {
		List<IMessage> messages = new ArrayList<IMessage>();
		this.validate(messages, reporter);
		return new SnapshotCloneIterable<IMessage>(messages);
	}
	
	protected void validate(List<IMessage> messages, IReporter reporter) {
		if (reporter.isCancelled()) {
			throw new ValidationCancelledException();
		}
		this.validateLibraryProvider(messages);
		this.validateConnection(messages);
		this.rootContextNode.validate(messages, reporter);
	}

	protected void validateLibraryProvider(List<IMessage> messages) {
		try {
			this.validateLibraryProvider_(messages);
		} catch (CoreException ex) {
			JptJpaCorePlugin.log(ex);
		}
	}

	protected void validateLibraryProvider_(List<IMessage> messages) throws CoreException {
		Map<String, Object> enablementVariables = new HashMap<String, Object>();
		enablementVariables.put(JpaLibraryProviderInstallOperationConfig.JPA_PLATFORM_ENABLEMENT_EXP, getJpaPlatform().getId());
		enablementVariables.put(JpaLibraryProviderInstallOperationConfig.JPA_PLATFORM_DESCRIPTION_ENABLEMENT_EXP, getJpaPlatform().getDescription());
		
		ILibraryProvider libraryProvider = LibraryProviderFramework.getCurrentProvider(getProject(), JpaFacet.FACET);
		IFacetedProject facetedProject = ProjectFacetsManager.create(getProject());
		IProjectFacetVersion facetVersion = facetedProject.getInstalledVersion(JpaFacet.FACET);
		if ( ! libraryProvider.isEnabledFor(facetedProject, facetVersion, enablementVariables)) {
			messages.add(
				DefaultJpaValidationMessages.buildMessage(
					IMessage.HIGH_SEVERITY,
					JpaValidationMessages.PROJECT_INVALID_LIBRARY_PROVIDER,
					this
				)
			);
		}
	}

	protected void validateConnection(List<IMessage> messages) {
		String cpName = this.dataSource.getConnectionProfileName();
		if (StringTools.stringIsEmpty(cpName)) {
			messages.add(
				DefaultJpaValidationMessages.buildMessage(
					IMessage.NORMAL_SEVERITY,
					JpaValidationMessages.PROJECT_NO_CONNECTION,
					this
				)
			);
			return;
		}
		ConnectionProfile cp = this.dataSource.getConnectionProfile();
		if (cp == null) {
			messages.add(
				DefaultJpaValidationMessages.buildMessage(
					IMessage.NORMAL_SEVERITY,
					JpaValidationMessages.PROJECT_INVALID_CONNECTION,
					new String[] {cpName},
					this
				)
			);
			return;
		}
		if (cp.isInactive()) {
			messages.add(
				DefaultJpaValidationMessages.buildMessage(
					IMessage.NORMAL_SEVERITY,
					JpaValidationMessages.PROJECT_INACTIVE_CONNECTION,
					new String[] {cpName},
					this
				)
			);
		}
	}
	
	
	// ********** dispose **********

	public void dispose() {
		this.contextModelSynchronizer.stop();
		this.updateSynchronizer.stop();
		this.updateSynchronizer.removeListener(this.updateSynchronizerListener);
		this.dataSource.dispose();
		// the XML resources are held indefinitely by the WTP translator framework,
		// so we better remove our listener or the JPA project will not be GCed
		for (JpaFile jpaFile : this.getJpaFiles()) {
			jpaFile.getResourceModel().removeResourceModelListener(this.resourceModelListener);
		}
	}
	
	
	// ********** resource model listener **********
	
	protected JptResourceModelListener buildResourceModelListener() {
		return new DefaultResourceModelListener();
	}

	protected class DefaultResourceModelListener 
		implements JptResourceModelListener 
	{
		protected DefaultResourceModelListener() {
			super();
		}
		
		public void resourceModelChanged(JptResourceModel jpaResourceModel) {
//			String msg = Thread.currentThread() + " resource model change: " + jpaResourceModel;
//			System.out.println(msg);
//			new Exception(msg).printStackTrace(System.out);
			AbstractJpaProject.this.synchronizeContextModel(jpaResourceModel);
		}
		
		public void resourceModelReverted(JptResourceModel jpaResourceModel) {
			IFile file = WorkbenchResourceHelper.getFile((JpaXmlResource)jpaResourceModel);
			AbstractJpaProject.this.removeJpaFile(file);
			AbstractJpaProject.this.addJpaFile(file);
		}
		
		public void resourceModelUnloaded(JptResourceModel jpaResourceModel) {
			IFile file = WorkbenchResourceHelper.getFile((JpaXmlResource)jpaResourceModel);
			AbstractJpaProject.this.removeJpaFile(file);
		}
	}
	
	protected void synchronizeContextModel(@SuppressWarnings("unused") JptResourceModel jpaResourceModel) {
		this.synchronizeContextModel();
	}

	
	// ********** resource events **********
	
	// TODO need to do the same thing for external projects and compilation units
	public void projectChanged(IResourceDelta delta) {
		if (delta.getResource().equals(this.getProject())) {
			this.internalProjectChanged(delta);
		} else {
			this.externalProjectChanged(delta);
		}
	}

	protected void internalProjectChanged(IResourceDelta delta) {
		if (delta.getKind() == IResourceDelta.REMOVED) {
			this.setContextModelSynchronizer(Synchronizer.Null.instance());
			this.setUpdateSynchronizer(CallbackSynchronizer.Null.instance());
		}
		ResourceDeltaVisitor resourceDeltaVisitor = this.buildInternalResourceDeltaVisitor();
		resourceDeltaVisitor.visitDelta(delta);
		// at this point, if we have added and/or removed JpaFiles, an "update" will have been triggered;
		// any changes to the resource model during the "resolve" will trigger further "updates";
		// there should be no need to "resolve" external Java types (they can't have references to
		// the internal Java types)
		if (resourceDeltaVisitor.encounteredSignificantChange()) {
			this.resolveInternalJavaTypes();
		}
	}

	protected ResourceDeltaVisitor buildInternalResourceDeltaVisitor() {
		return new InternalResourceDeltaVisitor();
	}

	protected class InternalResourceDeltaVisitor extends ResourceDeltaVisitor {
		protected InternalResourceDeltaVisitor() {
			super();
		}
		@Override
		public boolean fileChangeIsSignificant(IFile file, int deltaKind) {
			return AbstractJpaProject.this.synchronizeJpaFiles(file, deltaKind);
		}
	}

	/**
	 * Internal resource delta visitor callback.
	 * Return true if a JpaFile was either added or removed.
	 */
	protected boolean synchronizeJpaFiles(IFile file, int deltaKind) {
		switch (deltaKind) {
			case IResourceDelta.ADDED :
				return this.addJpaFile(file);
			case IResourceDelta.REMOVED :
				return this.removeJpaFile(file);
			case IResourceDelta.CHANGED :
				return this.checkForChangedFileContent(file);
			case IResourceDelta.ADDED_PHANTOM :
				break;  // ignore
			case IResourceDelta.REMOVED_PHANTOM :
				break;  // ignore
			default :
				break;  // only worried about added/removed/changed files
		}

		return false;
	}

	protected boolean checkForChangedFileContent(IFile file) {
		JpaFile jpaFile = this.getJpaFile(file);
		if (jpaFile == null) {
			// the file might have changed its content to something that we are interested in
			return this.addJpaFile(file);
		}

		if (jpaFile.getContentType().equals(PlatformTools.getContentType(file))) {
			// content has not changed - ignore
			return false;
		}

		// the content type changed, we need to build a new JPA file
		// (e.g. the schema of an orm.xml file changed from JPA to EclipseLink)
		this.removeJpaFile(jpaFile);
		this.addJpaFile(file);
		return true;  // at the least, we have removed a JPA file
	}

	protected void resolveInternalJavaTypes() {
		for (JavaResourceCompilationUnit jrcu : this.getInternalJavaResourceCompilationUnits()) {
			jrcu.resolveTypes();
		}
	}

	protected void externalProjectChanged(IResourceDelta delta) {
		if (this.getJavaProject().isOnClasspath(delta.getResource())) {
			ResourceDeltaVisitor resourceDeltaVisitor = this.buildExternalResourceDeltaVisitor();
			resourceDeltaVisitor.visitDelta(delta);
			// force an "update" here since adding and/or removing an external Java type
			// will only trigger an "update" if the "resolve" causes something in the resource model to change
			if (resourceDeltaVisitor.encounteredSignificantChange()) {
				this.update();
				this.resolveExternalJavaTypes();
				this.resolveInternalJavaTypes();
			}
		}
	}

	protected ResourceDeltaVisitor buildExternalResourceDeltaVisitor() {
		return new ExternalResourceDeltaVisitor();
	}

	protected class ExternalResourceDeltaVisitor extends ResourceDeltaVisitor {
		protected ExternalResourceDeltaVisitor() {
			super();
		}
		@Override
		public boolean fileChangeIsSignificant(IFile file, int deltaKind) {
			return AbstractJpaProject.this.synchronizeExternalFiles(file, deltaKind);
		}
	}

	/**
	 * external resource delta visitor callback
	 * Return true if an "external" Java resource compilation unit
	 * was added or removed.
	 */
	protected boolean synchronizeExternalFiles(IFile file, int deltaKind) {
		switch (deltaKind) {
			case IResourceDelta.ADDED :
				return this.externalFileAdded(file);
			case IResourceDelta.REMOVED :
				return this.externalFileRemoved(file);
			case IResourceDelta.CHANGED :
				break;  // ignore
			case IResourceDelta.ADDED_PHANTOM :
				break;  // ignore
			case IResourceDelta.REMOVED_PHANTOM :
				break;  // ignore
			default :
				break;  // only worried about added/removed/changed files
		}

		return false;
	}

	protected boolean externalFileAdded(IFile file) {
		IContentType contentType = PlatformTools.getContentType(file);
		if (contentType == null) {
			return false;
		}
		if (contentType.equals(JptCommonCorePlugin.JAVA_SOURCE_CONTENT_TYPE)) {
			return true;
		}
		if (contentType.equals(JptCommonCorePlugin.JAR_CONTENT_TYPE)) {
			return true;
		}
		return false;
	}

	protected boolean externalFileRemoved(IFile file) {
		IContentType contentType = PlatformTools.getContentType(file);
		if (contentType == null) {
			return false;
		}
		if (contentType.equals(JptCommonCorePlugin.JAVA_SOURCE_CONTENT_TYPE)) {
			return this.removeExternalJavaResourceCompilationUnit(file);
		}
		if (contentType.equals(JptCommonCorePlugin.JAR_CONTENT_TYPE)) {
			return this.externalJavaResourceTypeCache.removeTypes(file);
		}
		return false;
	}

	protected void resolveExternalJavaTypes() {
		for (JavaResourceCompilationUnit jrcu : this.getExternalJavaResourceCompilationUnits()) {
			jrcu.resolveTypes();
		}
	}

	// ***** resource delta visitors
	/**
	 * add or remove a JPA file for every [appropriate] file encountered by the visitor
	 */
	protected abstract class ResourceDeltaVisitor implements IResourceDeltaVisitor {
		protected boolean encounteredSignificantChange = false;
		
		protected ResourceDeltaVisitor() {
			super();
		}

		protected void visitDelta(IResourceDelta delta) {
			try {
				delta.accept(this);
			} catch (CoreException ex) {
				// shouldn't happen - we don't throw any CoreExceptions
				throw new RuntimeException(ex);
			}
		}

		public boolean visit(IResourceDelta delta) {
			IResource res = delta.getResource();
			switch (res.getType()) {
				case IResource.ROOT :
					return true;  // visit children
				case IResource.PROJECT :
					return true;  // visit children
				case IResource.FOLDER :
					return true;  // visit children
				case IResource.FILE :
					this.fileChanged((IFile) res, delta.getKind());
					return false;  // no children
				default :
					return false;  // no children (probably shouldn't get here...)
			}
		}

		protected void fileChanged(IFile file, int deltaKind) {
			if (this.fileChangeIsSignificant(file, deltaKind)) {
				this.encounteredSignificantChange = true;
			}
		}

		protected abstract boolean fileChangeIsSignificant(IFile file, int deltaKind);

		/**
		 * Return whether the visitor encountered some sort of "significant"
		 * change while traversing the IResourceDelta
		 * (e.g. a JPA file was added or removed).
		 */
		protected boolean encounteredSignificantChange() {
			return this.encounteredSignificantChange;
		}

	}


	// ********** support for modifying documents shared with the UI **********

	public void setThreadLocalModifySharedDocumentCommandExecutor(CommandExecutor commandExecutor) {
		this.modifySharedDocumentCommandExecutor.set(commandExecutor);
	}

	public CommandExecutor getModifySharedDocumentCommandExecutor() {
		return this.modifySharedDocumentCommandExecutor;
	}

	protected ThreadLocalCommandExecutor buildModifySharedDocumentCommandExecutor() {
		return new ThreadLocalCommandExecutor();
	}


	// ********** synchronize context model with resource model **********

	public Synchronizer getContextModelSynchronizer() {
		return this.contextModelSynchronizer;
	}

	public void setContextModelSynchronizer(Synchronizer synchronizer) {
		if (synchronizer == null) {
			throw new NullPointerException();
		}
		this.contextModelSynchronizer.stop();
		this.setContextModelSynchronizer_(synchronizer);
	}

	protected void setContextModelSynchronizer_(Synchronizer synchronizer) {
		this.contextModelSynchronizer = synchronizer;
		this.contextModelSynchronizer.start();
	}

	/**
	 * Delegate to the context model synchronizer so clients can configure how
	 * synchronizations occur.
	 */
	public void synchronizeContextModel() {
		this.synchronizingContextModel = true;
		this.contextModelSynchronizer.synchronize();
		this.synchronizingContextModel = false;

		// There are some changes to the resource model that will not change
		// the existing context model and trigger an update (e.g. adding an
		// @Entity annotation when the JPA project is automatically
		// discovering annotated classes); so we explicitly execute an update
		// here to discover those changes.
		this.update();
	}

	/**
	 * Called by the context model synchronizer.
	 */
	public IStatus synchronizeContextModel(IProgressMonitor monitor) {
		this.rootContextNode.synchronizeWithResourceModel();
		return Status.OK_STATUS;
	}

	public void synchronizeContextModelAndWait() {
		Synchronizer temp = this.contextModelSynchronizer;
		this.setContextModelSynchronizer(this.buildSynchronousContextModelSynchronizer());
		this.synchronizeContextModel();
		this.setContextModelSynchronizer(temp);
	}


	// ********** default context model synchronizer (synchronous) **********

	protected Synchronizer buildSynchronousContextModelSynchronizer() {
		return new SynchronousSynchronizer(this.buildSynchronousContextModelSynchronizerCommand());
	}

	protected Command buildSynchronousContextModelSynchronizerCommand() {
		return new SynchronousContextModelSynchronizerCommand();
	}

	protected class SynchronousContextModelSynchronizerCommand
		implements Command
	{
		public void execute() {
			AbstractJpaProject.this.synchronizeContextModel(new NullProgressMonitor());
		}
	}


	// ********** project "update" **********

	public CallbackSynchronizer getUpdateSynchronizer() {
		return this.updateSynchronizer;
	}

	public void setUpdateSynchronizer(CallbackSynchronizer synchronizer) {
		if (synchronizer == null) {
			throw new NullPointerException();
		}
		this.updateSynchronizer.stop();
		this.updateSynchronizer.removeListener(this.updateSynchronizerListener);
		this.setUpdateSynchronizer_(synchronizer);
	}

	protected void setUpdateSynchronizer_(CallbackSynchronizer synchronizer) {
		this.updateSynchronizer = synchronizer;
		this.updateSynchronizer.addListener(this.updateSynchronizerListener);
		this.updateSynchronizer.start();
	}

	@Override
	public void stateChanged() {
		super.stateChanged();
		this.update();
	}

	/**
	 * The JPA project's state has changed, "update" those parts of the
	 * JPA project that are dependent on other parts of the JPA project.
	 * <p>
	 * Delegate to the update synchronizer so clients can configure how
	 * updates occur.
	 * <p>
	 * Ignore any <em>updates</em> that occur while we are synchronizing
	 * the context model with the resource model because we will <em>update</em>
	 * the context model at the completion of the <em>sync</em>. This is really
	 * only useful for synchronous <em>syncs</em> and <em>updates</em>; since
	 * the job scheduling rules will prevent the <em>sync</em> and
	 * <em>update</em> jobs from running concurrently.
	 * 
	 * @see #updateAndWait()
	 */
	protected void update() {
		if ( ! this.synchronizingContextModel) {
			this.updateSynchronizer.synchronize();
		}
	}

	/**
	 * Called by the update synchronizer.
	 */
	public IStatus update(IProgressMonitor monitor) {
		this.rootContextNode.update();
		return Status.OK_STATUS;
	}

	/**
	 * This is the callback used by the update synchronizer to notify the JPA
	 * project that the "update" has quiesced (i.e. the "update" has completed
	 * and there are no outstanding requests for further "updates").
	 */
 	public void updateQuiesced() {
		this.synchronizeMetamodel();
	}

	public void updateAndWait() {
		CallbackSynchronizer temp = this.updateSynchronizer;
		this.setUpdateSynchronizer(this.buildSynchronousUpdateSynchronizer());
		this.update();
		this.setUpdateSynchronizer(temp);
	}


	// ********** default update synchronizer (synchronous) **********

	protected CallbackSynchronizer buildSynchronousUpdateSynchronizer() {
		return new CallbackSynchronousSynchronizer(this.buildSynchronousUpdateSynchronizerCommand());
	}

	protected Command buildSynchronousUpdateSynchronizerCommand() {
		return new SynchronousUpdateSynchronizerCommand();
	}

	protected class SynchronousUpdateSynchronizerCommand
		implements Command
	{
		public void execute() {
			AbstractJpaProject.this.update(new NullProgressMonitor());
		}
	}


	// ********** update synchronizer listener **********

	protected CallbackSynchronizer.Listener buildUpdateSynchronizerListener() {
		return new UpdateSynchronizerListener();
	}

	protected class UpdateSynchronizerListener
		implements CallbackSynchronizer.Listener
	{
		public void synchronizationQuiesced(CallbackSynchronizer synchronizer) {
			AbstractJpaProject.this.updateQuiesced();
		}
	}
}