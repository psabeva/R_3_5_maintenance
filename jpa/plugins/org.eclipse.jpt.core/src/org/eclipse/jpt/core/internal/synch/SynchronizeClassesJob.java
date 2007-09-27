/*******************************************************************************
 *  Copyright (c) 2006, 2007 Oracle. All rights reserved. This
 *  program and the accompanying materials are made available under the terms of
 *  the Eclipse Public License v1.0 which accompanies this distribution, and is
 *  available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: Oracle. - initial API and implementation
 *  
 *******************************************************************************/
package org.eclipse.jpt.core.internal.synch;

import java.io.IOException;
import java.util.Iterator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jpt.core.internal.IJpaFile;
import org.eclipse.jpt.core.internal.JptCoreMessages;
import org.eclipse.jpt.core.internal.JptCorePlugin;
import org.eclipse.jpt.core.internal.resource.persistence.JavaClassRef;
import org.eclipse.jpt.core.internal.resource.persistence.Persistence;
import org.eclipse.jpt.core.internal.resource.persistence.PersistenceFactory;
import org.eclipse.jpt.core.internal.resource.persistence.PersistenceResourceModel;
import org.eclipse.jpt.core.internal.resource.persistence.PersistenceUnit;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;

/**
 * Synchronizes the lists of persistent classes in a persistence unit and a 
 * persistence project.
 */
public class SynchronizeClassesJob extends Job
{
	private IFile persistenceXmlFile;
	
	
	public SynchronizeClassesJob(IFile file) {
		super(JptCoreMessages.SYNCHRONIZE_CLASSES_JOB);
		setRule(file.getProject());
		this.persistenceXmlFile = file;
	}
	
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(JptCoreMessages.SYNCHRONIZING_CLASSES_TASK, 150);
		
		if (monitor.isCanceled()) {
			return Status.CANCEL_STATUS;
		}
		
		IJpaFile jpaFile = JptCorePlugin.getJpaFile(this.persistenceXmlFile);
		PersistenceResourceModel resource;
		try {
			resource = (PersistenceResourceModel) jpaFile.getResourceModel();
		}
		catch (ClassCastException cce) {
			return new Status(IStatus.ERROR, JptCorePlugin.PLUGIN_ID, JptCoreMessages.INVALID_PERSISTENCE_XML_CONTENT);
		}
		
		Persistence persistence = resource.getPersistence();
		
		if (persistence == null) {
			persistence = PersistenceFactory.eINSTANCE.createPersistence();
			resource.getContents().add(persistence);
		}
		
		PersistenceUnit persistenceUnit;
		
		if (persistence.getPersistenceUnits().size() > 0) {
			persistenceUnit = persistence.getPersistenceUnits().get(0);
		}
		else {
			persistenceUnit = PersistenceFactory.eINSTANCE.createPersistenceUnit();
			persistenceUnit.setName(this.persistenceXmlFile.getProject().getName());
			persistence.getPersistenceUnits().add(persistenceUnit);
		}
		
		persistenceUnit.getClasses().clear();
		for (Iterator<String> stream = this.sortedMappedTypeNames(persistenceUnit); stream.hasNext(); ) {
			JavaClassRef classRef = PersistenceFactory.eINSTANCE.createJavaClassRef();
			classRef.setJavaClass(stream.next());
			persistenceUnit.getClasses().add(classRef);
		}
		
		monitor.worked(50);
		
		try {
			resource.save(null);
		}
		catch (IOException ioe) {
			return new Status(IStatus.ERROR, JptCorePlugin.PLUGIN_ID, JptCoreMessages.ERROR_WRITING_FILE, ioe);
		}
		
		return Status.OK_STATUS;
	}
	
	private Iterator<String> sortedMappedTypeNames(PersistenceUnit persistenceUnit) {
		return EmptyIterator.instance();
//		return CollectionTools.sort(this.mappedTypeNames(persistenceUnit));
	}
//	
//	private Iterator<String> mappedTypeNames(PersistenceUnit persistenceUnit) {
//		return new TransformationIterator<IPersistentType, String>(this.mappedTypes(persistenceUnit)) {
//			@Override
//			protected String transform(IPersistentType pType) {
//				return pType.findJdtType().getFullyQualifiedName();
//			}
//		};
//	}
//	
//	private Iterator<IPersistentType> mappedTypes(PersistenceUnit persistenceUnit) {
//		return new FilteringIterator<IPersistentType>(allJavaTypes(persistenceUnit.getJpaProject()), filter(persistenceUnit));
//	}
//	
//	private Iterator<IPersistentType> allJavaTypes(IJpaProject jpaProject) {
//		return new TransformationIterator<IJpaFile, IPersistentType>(jpaProject.jpaFiles(JptCorePlugin.JAVA_CONTENT_TYPE).iterator()) {
//			@Override
//			protected IPersistentType transform(IJpaFile next) {
//				JpaCompilationUnit jcu = (JpaCompilationUnit) next.getContent();
//				return (jcu.getTypes().isEmpty()) ? null : jcu.getTypes().get(0);
//			}
//		};
//	}
//	
//	private Filter<IPersistentType> filter(final PersistenceUnit persistenceUnit) {
//		return new Filter<IPersistentType>() {
//			public boolean accept(IPersistentType o) {
//				if (o == null) {
//					return false;
//				}
//				if (o.getMappingKey() == IMappingKeys.NULL_TYPE_MAPPING_KEY) {
//					return false;
//				}
//				IType jdtType = o.findJdtType();
//				if (jdtType == null) {
//					return false;
//				}
//				for (MappingFileRef mappingFileRef : persistenceUnit.getMappingFiles()) {
//					if (containsType(mappingFileRef, jdtType)) {
//						return false;
//					}
//				}
//				return true;
//			}
//		};
//	}
//	
//	private boolean containsType(MappingFileRef mappingFileRef, IType jdtType) {
//		IJpaFile mappingFile = mappingFileRef.getMappingFile();
//		if (mappingFile == null) {
//			return false;
//		}
//		
//		XmlRootContentNode root;
//		try {
//			root = (XmlRootContentNode) mappingFile.getContent();
//		}
//		catch (ClassCastException cce) {
//			return false;
//		}
//		
//		EntityMappingsInternal entityMappings = root.getEntityMappings();
//		
//		if (entityMappings == null) {
//			return false;
//		}
//		
//		for (IPersistentType persistentType : entityMappings.getPersistentTypes()) {
//			IType otherJdtType = persistentType.findJdtType();
//			if (otherJdtType != null && otherJdtType.equals(jdtType)) {
//				return true;
//			}
//		}
//		
//		return false;
//	}
}
