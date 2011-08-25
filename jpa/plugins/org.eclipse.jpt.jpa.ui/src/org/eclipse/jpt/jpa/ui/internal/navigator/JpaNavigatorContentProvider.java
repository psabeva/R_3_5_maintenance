/*******************************************************************************
 *  Copyright (c) 2007, 2010 Oracle. 
 *  All rights reserved.  This program and the accompanying materials 
 *  are made available under the terms of the Eclipse Public License v1.0 
 *  which accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.navigator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jpt.jpa.core.JpaFacet;
import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.jpa.core.JptJpaCorePlugin;
import org.eclipse.jpt.jpa.ui.JpaPlatformUi;
import org.eclipse.jpt.jpa.ui.JptJpaUiPlugin;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentProvider;
import org.eclipse.wst.common.project.facet.core.FacetedProjectFramework;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectEvent;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectListener;
import org.eclipse.wst.common.project.facet.core.events.IProjectFacetActionEvent;

/**
 * This extension of navigator content provider delegates to the platform UI
 * (see the org.eclipse.jpt.jpa.ui.jpaPlatform extension point) for navigator content.
 * 
 * If there is a platform UI for the given project, this content provider will
 * provide a root "JPA Content" node (child of the project), otherwise there
 * will be no content.  For children of the "JPA Content" node (or for any other
 * sub-node), this provider will delegate to the content provider returned by the 
 * platform UI implementation.
 */
public class JpaNavigatorContentProvider
		implements ICommonContentProvider {
	
	private JpaNavigatorContentAndLabelProvider delegate;
	
	private IFacetedProjectListener facetListener;
	
	private StructuredViewer viewer;
	
	
	public JpaNavigatorContentProvider() {
		super();
		facetListener = new FacetListener();
		FacetedProjectFramework.addListener(
				facetListener, 
				IFacetedProjectEvent.Type.POST_INSTALL,
				IFacetedProjectEvent.Type.POST_UNINSTALL,
				IFacetedProjectEvent.Type.PROJECT_MODIFIED);
	}
	
	
	public JpaNavigatorContentAndLabelProvider delegate() {
		return delegate;
	}
	
	
	// **************** IContentProvider implementation ************************
	
	public void dispose() {
		FacetedProjectFramework.removeListener(facetListener);
		if (delegate != null) {
			delegate.dispose();
		}
	}
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (delegate != null) {
			delegate.inputChanged(viewer, oldInput, newInput);
		}
		this.viewer = (StructuredViewer) viewer;
	}
	
	
	// **************** IStructuredContentProvider implementation **************
	
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}
	
	
	// **************** ITreeContentProvider implementation ********************
	
	public Object getParent(Object element) {
		if (delegate != null) {
			return delegate.getParent(element);
		}
		
		return null;
	}
	
	public boolean hasChildren(Object element) {
		if (element instanceof IAdaptable) {
			IProject project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
			
			if (project != null) {
				JpaProject jpaProject = JptJpaCorePlugin.getJpaProject(project);
				if (jpaProject != null) {
					JpaPlatformUi platformUi = JptJpaUiPlugin.instance().getJpaPlatformUi(jpaProject.getJpaPlatform());
					
					return platformUi != null;
				}	
			}
		}
		
		if (delegate != null) {
			return delegate.hasChildren(element);
		}
		
		return false;
	}
	
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IAdaptable) {
			IProject project = (IProject) ((IAdaptable) parentElement).getAdapter(IProject.class);
			
			if (project != null) {
				JpaProject jpaProject = JptJpaCorePlugin.getJpaProject(project);
				if (jpaProject != null) {
					JpaPlatformUi platformUi = JptJpaUiPlugin.instance().getJpaPlatformUi(jpaProject.getJpaPlatform());
					
					if (platformUi != null) {
						return new Object[] {jpaProject.getRootContextNode()};
					}
				}	
			}
		}
		
		if (delegate != null) {
			return delegate.getChildren(parentElement);
		}
			
		return new Object[0];
	}
	
	
	// **************** IMementoAware implementation ***************************
	
	public void saveState(IMemento memento) {
		// no op
	}
	
	public void restoreState(IMemento memento) {
		// no op
	}
	
	
	// **************** ICommonContentProvider implementation ******************
	
	public void init(ICommonContentExtensionSite config) {
		if (delegate == null) {
			JpaNavigatorLabelProvider labelProvider = (JpaNavigatorLabelProvider) config.getExtension().getLabelProvider();
			if (labelProvider != null && labelProvider.delegate() != null) {
				delegate = labelProvider.delegate();
			}
			else {
				delegate = new JpaNavigatorContentAndLabelProvider();
			}
		}
	}
	
	
	// **************** member classes *****************************************
	
	private class FacetListener
		implements IFacetedProjectListener
	{
		public void handleEvent(IFacetedProjectEvent event) {
			if (event.getType() == IFacetedProjectEvent.Type.PROJECT_MODIFIED) {
				refreshViewer(event.getProject().getProject());
			}
			else if (event.getType() == IFacetedProjectEvent.Type.POST_INSTALL
					|| event.getType() == IFacetedProjectEvent.Type.POST_UNINSTALL) {
				IProjectFacetActionEvent ipaEvent = (IProjectFacetActionEvent) event;
				if (ipaEvent.getProjectFacet().equals(JpaFacet.FACET)) {
					refreshViewer(ipaEvent.getProject().getProject());
				}
			}
		}
		
		private void refreshViewer(final IProject project) {
			if (viewer != null 
					&& viewer.getControl() != null 
					&& !viewer.getControl().isDisposed()) {
				// Using job here so that project model update (which also uses
				//  a job) will complete first
				Job refreshJob = new Job("Refresh viewer") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						// Using runnable here so that refresh will go on correct thread
						viewer.getControl().getDisplay().asyncExec(new Runnable() {
							public void run() {
								viewer.refresh(project);
							}
						});
						return Status.OK_STATUS;
					}
				};
				refreshJob.setRule(project);
				refreshJob.schedule();
			}
		}
	}
}