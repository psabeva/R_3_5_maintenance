/*******************************************************************************
 *  Copyright (c) 2010  Oracle. All rights reserved.
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0, which accompanies this distribution
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jaxb.core;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jpt.common.core.JptResourceType;
import org.eclipse.jpt.jaxb.core.internal.JptJaxbCoreMessages;
import org.eclipse.jpt.jaxb.core.internal.platform.JaxbPlatformManagerImpl;
import org.eclipse.jpt.jaxb.core.platform.JaxbPlatformDescription;
import org.eclipse.jpt.jaxb.core.platform.JaxbPlatformManager;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * The Dali JAXB core plug-in lifecycle implementation.
 * A number of globally-available constants and methods.
 * <p>
 * Provisional API: This class is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 3.0
 * @since 3.0
 */
public class JptJaxbCorePlugin
		extends Plugin {
	
	private static volatile boolean flushPreferences = true;
	
	/**
	 * The plug-in identifier of Dali JAXB core
	 * (value <code>"org.eclipse.jpt.jaxb.core"</code>).
	 */
	public static final String PLUGIN_ID = "org.eclipse.jpt.jaxb.core";  //$NON-NLS-1$
	public static final String PLUGIN_ID_ = PLUGIN_ID + '.';
	
	/**
	 * The node for storing a JAXB project's platform in the project's preferences.
	 */
	public static final String PLATFORM_PREF_NODE = "platform";  //$NON-NLS-1$
	
	/**
	 * The key for storing the platform id
	 */
	public static final String PLATFORM_ID_PREF_KEY = "platform-id";  //$NON-NLS-1$
	
	/**
	 * The node for storing a JAXB project's schemas in the project's preferences.
	 */
	public static final String SCHEMAS_PREF_NODE = "schemas";  //$NON-NLS-1$
	
	/**
	 * The node prefix for storing a particular JAXB project schema in the project's preferences.
	 * Specific schema nodes are followed by integers ("schema-1", "schema-2", etc.)
	 */
	public static final String SCHEMA_PREF_NODE_PREFIX = "schema-";  //$NON-NLS-1$
	
	/**
	 * The key for storing a schema namespace in the project's preferences
	 */
	public static final String SCHEMA_NAMESPACE_PREF_KEY = "namespace";  //$NON-NLS-1$
	
	/**
	 * The key for storing a schema location (such as a uri or catalog key) in the project's preferences
	 */
	public static final String SCHEMA_LOCATION_PREF_KEY = "location";  //$NON-NLS-1$
	
	/**
	 * The key for storing the default JAXB platform ID for JAXB 2.1 in the workspace preferences.
	 */
	public static final String DEFAULT_JAXB_PLATFORM_2_1_PREF_KEY = 
			"defaultJaxbPlatform_" + JaxbFacet.VERSION_2_1.getVersionString(); //$NON-NLS-1$

	/**
	 * The key for storing the default JAXB platform ID for JAXB 2.2 in the workspace preferences.
	 */
	public static final String DEFAULT_JAXB_PLATFORM_2_2_PREF_KEY = 
			"defaultJaxbPlatform_" + JaxbFacet.VERSION_2_2.getVersionString(); //$NON-NLS-1$
	
	/**
	 * The identifier for the JAXB validation marker
	 * (value <code>"org.eclipse.jpt.jaxb.core.jaxbProblemMarker"</code>).
	 */
	public static final String VALIDATION_MARKER_ID = PLUGIN_ID + ".jaxbProblemMarker";  //$NON-NLS-1$

	/**
	 * The content type for jaxb.index files
	 */
	public static final IContentType JAXB_INDEX_CONTENT_TYPE = getJaxbContentType("jaxbIndex");
	
	/**
	 * The resource type for jaxb.index files
	 */
	public static final JptResourceType JAXB_INDEX_RESOURCE_TYPE = new JptResourceType(JAXB_INDEX_CONTENT_TYPE);
	
	/**
	 * The content type for jaxb.properties files
	 */
	public static final IContentType JAXB_PROPERTIES_CONTENT_TYPE = getJaxbContentType("jaxbProperties");
	
	/**
	 * The resource type for jaxb.properties files
	 */
	public static final JptResourceType JAXB_PROPERTIES_RESOURCE_TYPE = new JptResourceType(JAXB_PROPERTIES_CONTENT_TYPE);
	
	private static IContentType getJaxbContentType(String contentType) {
		return getContentType(CONTENT_PREFIX_ + contentType);
	}
	
	public static final String CONTENT_PREFIX = PLUGIN_ID_ + "content"; //$NON-NLS-1$
	
	public static final String CONTENT_PREFIX_ = CONTENT_PREFIX + '.';
	
	private static IContentType getContentType(String contentType) {
		return Platform.getContentTypeManager().getContentType(contentType);
	}
	
	
	// **************** fields ************************************************
	
	private volatile GenericJaxbProjectManager projectManager;
	
	
	// **************** singleton *********************************************
	
	private static JptJaxbCorePlugin INSTANCE;
	
	/**
	 * Return the singleton plug-in
	 */
	public static JptJaxbCorePlugin instance() {
		return INSTANCE;
	}
	
	
	// ********** public static methods **********

	/**
	 * Return the singular JAXB project manager corresponding to the current workspace.
	 */
	public static JaxbProjectManager getProjectManager() {
		return INSTANCE.getProjectManager_();
	}

	/**
	 * Return the JAXB project corresponding to the specified Eclipse project,
	 * or <code>null</code> if unable to associate the specified project with a
	 * JAXB project.
	 */
	public static JaxbProject getJaxbProject(IProject project) {
		return getProjectManager().getJaxbProject(project);
	}
	
	public static JaxbPlatformManager getJaxbPlatformManager() {
		return JaxbPlatformManagerImpl.instance();
	}
	
	/**
	 * Return the default Dali preferences
	 * @see JpaPreferenceInitializer
	 */
	public static IEclipsePreferences getDefaultPreferences() {
		return getPreferences(DefaultScope.INSTANCE);
	}

	/**
	 * Return the Dali preferences for the current workspace instance.
	 */
	public static IEclipsePreferences getWorkspacePreferences() {
		return getPreferences(InstanceScope.INSTANCE);
	}
	
	/**
	 * Return the Dali preferences for the specified context.
	 */
	private static IEclipsePreferences getPreferences(IScopeContext context) {
		return context.getNode(PLUGIN_ID);
	}
	
	/**
	 * Set the workspace preference.
	 */
	public static void setWorkspacePreference(String preferenceKey, String preferenceValue) {
		IEclipsePreferences prefs = getWorkspacePreferences();
		prefs.put(preferenceKey, preferenceValue);
		flush(prefs);
	}

	/**
	 * This method is called (via reflection) when the test plug-in is loaded.
	 * The preferences end up getting flushed after the test case has deleted
	 * its project, resulting in resource exceptions in the log, e.g.
	 * <pre>
	 *     Resource '/JpaProjectManagerTests' is not open.
	 * </pre>
	 * See <code>JptJaxbCoreTestsPlugin.start(BundleContext)</code>
	 */
	@SuppressWarnings("unused")
	private static void doNotFlushPreferences() {
		flushPreferences = false;
	}

	/**
	 * Flush preferences in an asynchronous Job because the flush request will
	 * trigger a lock on the project, which can cause us some deadlocks (e.g.
	 * when deleting the metamodel source folder).
	 * Note: the flush will also remove the prefs node if it is empty
	 */
	private static void flush(IEclipsePreferences prefs) {
		if (flushPreferences) {
			new PreferencesFlushJob(prefs).schedule();
		}
	}

	private static class PreferencesFlushJob extends Job {
		private final IEclipsePreferences prefs;
		PreferencesFlushJob(IEclipsePreferences prefs) {
			super(NLS.bind(JptJaxbCoreMessages.PREFERENCES_FLUSH_JOB_NAME, prefs.absolutePath()));
			this.prefs = prefs;
		}
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			try {
				prefs.flush();
			} catch(BackingStoreException ex) {
				log(ex);
			}
			return Status.OK_STATUS;
		}
	}
	
	public static Preferences getProjectPreferences(IProject project) {
		try {
			IFacetedProject fproj = ProjectFacetsManager.create(project);
			return fproj.getPreferences(JaxbFacet.FACET);
		}
		catch (BackingStoreException bse) {
			log(bse);
		}
		catch (CoreException ce) {
			log(ce);
		}
		return null;
	}
	
	/**
	 * Set the default {@link JaxbPlatformDescription} for new JAXB projects with the given
	 * JAXB facet version.
	 */
	public static void setDefaultJaxbPlatform(IProjectFacetVersion jaxbFacetVersion, JaxbPlatformDescription platform) {
		String preferenceKey = null;
		if (JaxbFacet.VERSION_2_1.equals(jaxbFacetVersion)) {
			preferenceKey = DEFAULT_JAXB_PLATFORM_2_1_PREF_KEY;
		}
		else if (JaxbFacet.VERSION_2_2.equals(jaxbFacetVersion)) {
			preferenceKey = DEFAULT_JAXB_PLATFORM_2_2_PREF_KEY;
		}
		else {
			throw new IllegalArgumentException("Illegal JAXB facet version: " + jaxbFacetVersion); //$NON-NLS-1$
		}
		setWorkspacePreference(preferenceKey, platform.getId());
	}
	
	/**
	 * Return the default {@link JaxbPlatformDescription} for new JAXB projects with the given 
	 * JAXB facet version.
	 */
	public static JaxbPlatformDescription getDefaultPlatform(IProjectFacetVersion jaxbFacetVersion) {
		
		JaxbPlatformDescription defaultPlatform = 
				getDefaultPlatform(
					jaxbFacetVersion, 
					getWorkspacePreferences(), 
					getDefaultPreferences());
		if (defaultPlatform == null) {
			// if the platform ID stored in the workspace prefs is invalid (i.e. null), look in the default prefs
			defaultPlatform = getDefaultPlatform(jaxbFacetVersion, getDefaultPreferences());
		}
		return defaultPlatform;
	}
	
	private static JaxbPlatformDescription getDefaultPlatform(
				IProjectFacetVersion jaxbFacetVersion, Preferences ... nodes) {
		
		JaxbPlatformDescription defaultDefaultPlatform;
		String preferenceKey;
		if (jaxbFacetVersion.equals(JaxbFacet.VERSION_2_1)) {
			defaultDefaultPlatform = GenericJaxbPlatform.VERSION_2_1;
			preferenceKey = DEFAULT_JAXB_PLATFORM_2_1_PREF_KEY; 
		}
		else if (jaxbFacetVersion.equals(JaxbFacet.VERSION_2_2)) {
			defaultDefaultPlatform = GenericJaxbPlatform.VERSION_2_2;
			preferenceKey = DEFAULT_JAXB_PLATFORM_2_2_PREF_KEY; 
		}
		else {
			throw new IllegalArgumentException("Illegal JAXB facet version: " + jaxbFacetVersion); //$NON-NLS-1$
		}
		return getDefaultPlatform(jaxbFacetVersion, preferenceKey, defaultDefaultPlatform, nodes);
	}
	
	private static JaxbPlatformDescription getDefaultPlatform(
			IProjectFacetVersion jaxbFacetVersion, String preferenceKey, 
			JaxbPlatformDescription defaultDefault, Preferences ... nodes) {	
		
		String defaultDefaultId = (defaultDefault == null) ? null : defaultDefault.getId();
		String defaultPlatformId = Platform.getPreferencesService().get(preferenceKey, defaultDefaultId, nodes);
		JaxbPlatformDescription defaultPlatform = getJaxbPlatformManager().getJaxbPlatform(defaultPlatformId);
		if (defaultPlatform != null && defaultPlatform.supportsJaxbFacetVersion(jaxbFacetVersion)) {
			return defaultPlatform;
		}
		else if (defaultDefault != null && defaultDefault.supportsJaxbFacetVersion(jaxbFacetVersion)) {
			return defaultDefault;
		}
		return null;
	}

	/**
	 * Return the JAXB platform ID associated with the specified Eclipse project.
	 */
	public static String getJaxbPlatformId(IProject project) {
		Preferences prefs = getProjectPreferences(project);
		Preferences platformPrefs = prefs.node(PLATFORM_PREF_NODE);
		return platformPrefs.get(PLATFORM_ID_PREF_KEY, GenericJaxbPlatform.VERSION_2_1.getId());
	}

	/**
	 * Return the {@link JaxbPlatformDescription} associated with the specified Eclipse project.
	 */
	public static JaxbPlatformDescription getJaxbPlatformDescription(IProject project) {
		String jpaPlatformId = getJaxbPlatformId(project);
		return getJaxbPlatformManager().getJaxbPlatform(jpaPlatformId);
	}
	
	/**
	 * Set the {@link JaxbPlatformDescription} associated with the specified Eclipse project.
	 */
	public static void setJaxbPlatform(IProject project, JaxbPlatformDescription platform) {
		Preferences prefs = getProjectPreferences(project);
		Preferences platformPrefs = prefs.node(PLATFORM_PREF_NODE);
		platformPrefs.put(PLATFORM_ID_PREF_KEY, platform.getId());
		try {
			platformPrefs.flush();
		}
		catch (BackingStoreException bse) {
			log(bse);
		}
	}
	
	public static Map<String, String> getSchemaLocationMap(IProject project) {
		Map<String, String> schemaLocationMap = new HashMap<String, String>();
		Preferences prefs = getProjectPreferences(project);
		Preferences schemasPrefs = prefs.node(SCHEMAS_PREF_NODE);
		try {
			boolean checkAnotherNode = true;
			for (int i = 1; checkAnotherNode; i++ ) {
				String nodeName = SCHEMA_PREF_NODE_PREFIX + String.valueOf(i);
				if (schemasPrefs.nodeExists(nodeName)) {
					Preferences schemaPrefs = schemasPrefs.node(nodeName);
					String namespace = schemaPrefs.get(SCHEMA_NAMESPACE_PREF_KEY, null);
					String location = schemaPrefs.get(SCHEMA_LOCATION_PREF_KEY, null);
					if (namespace != null) {
						schemaLocationMap.put(namespace, location);
					}
				}
				else {
					checkAnotherNode = false;
				}
			}
		}
		catch (BackingStoreException bse) {
			// this means that the prefs are corrupted, in which case reading anything is unlikely
			JptJaxbCorePlugin.log(bse);
		}
		return schemaLocationMap;
	}
	
	public static void setSchemaLocationMap(IProject project, Map<String, String> schemaLocationMap) {
		Preferences prefs = getProjectPreferences(project);
		Preferences schemasPrefs = prefs.node(SCHEMAS_PREF_NODE);
		try {
			int i = 1;
			for (String namespace : schemaLocationMap.keySet()) {
				String nodeName = SCHEMA_PREF_NODE_PREFIX + String.valueOf(i);
				Preferences schemaPref = schemasPrefs.node(nodeName);
				schemaPref.put(SCHEMA_NAMESPACE_PREF_KEY, namespace);
				schemaPref.put(SCHEMA_LOCATION_PREF_KEY, schemaLocationMap.get(namespace));
				i ++;
			}
			boolean checkAnotherNode = true;
			for ( ; checkAnotherNode; i++ ) {
				String nodeName = SCHEMA_PREF_NODE_PREFIX + String.valueOf(i);
				if (schemasPrefs.nodeExists(nodeName)) {
					schemasPrefs.node(nodeName).removeNode();
				}
				else {
					checkAnotherNode = false;
				}
			}
			schemasPrefs.flush();
		}
		catch (BackingStoreException bse) {
			// this means that the prefs are corrupted, in which case reading anything is unlikely
			JptJaxbCorePlugin.log(bse);
		}
	}
	
	/**
	 * Log the specified status.
	 */
	public static void log(IStatus status) {
        INSTANCE.getLog().log(status);
    }
	
	/**
	 * Log the specified message.
	 */
	public static void log(String msg) {
        log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, msg, null));
    }
	
	/**
	 * Log the specified exception or error.
	 */
	public static void log(Throwable throwable) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, throwable.getLocalizedMessage(), throwable));
	}
	
	
	// ********** plug-in implementation **********

	public JptJaxbCorePlugin() {
		super();
		if (INSTANCE != null) {
			throw new IllegalStateException();
		}
		// this convention is *wack*...  ~bjv
		INSTANCE = this;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		// nothing yet...
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		try {
			if (this.projectManager != null) {
				this.projectManager.stop();
				this.projectManager = null;
			}
		} finally {
			super.stop(context);
		}
	}
	
	private synchronized GenericJaxbProjectManager getProjectManager_() {
		if (this.projectManager == null) {
			this.projectManager = this.buildProjectManager();
			this.projectManager.start();
		}
		return this.projectManager;
	}

	private GenericJaxbProjectManager buildProjectManager() {
		return new GenericJaxbProjectManager();
	}
}