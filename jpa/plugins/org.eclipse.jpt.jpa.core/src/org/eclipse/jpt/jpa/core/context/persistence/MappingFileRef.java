/*******************************************************************************
 * Copyright (c) 2007, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.context.persistence;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jpt.jpa.core.JpaStructureNode;
import org.eclipse.jpt.jpa.core.context.MappingFile;
import org.eclipse.jpt.jpa.core.context.MappingFilePersistenceUnitMetadata;
import org.eclipse.jpt.jpa.core.context.XmlContextNode;
import org.eclipse.jpt.jpa.core.resource.persistence.XmlMappingFileRef;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.ReplaceEdit;

/**
 * Context model corresponding to the
 * XML resource model {@link XmlMappingFileRef},
 * which corresponds to the <code>mapping-file</code> element
 * in the <code>persistence.xml</code> file.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 3.0
 * @since 2.0
 */
public interface MappingFileRef
	extends XmlContextNode, JpaStructureNode, PersistentTypeContainer
{
	/**
	 * Covariant override.
	 */
	PersistenceUnit getParent();


	// ********** file name **********

	/**
	 * String constant associated with changes to the file name.
	 */
	String FILE_NAME_PROPERTY = "fileName"; //$NON-NLS-1$

	/**
	 * Return the file name of the mapping file ref.
	 */
	String getFileName();

	/**
	 * Set the file name of the mapping file ref.
	 */
	void setFileName(String fileName);

	/**
	 * Return whether the mapping file ref is a reference to the specified file.
	 */
	boolean isFor(IFile file);


	// ********** mapping file (orm.xml) **********

	/**
	 * String constant associated with changes to the mapping file.
	 */
	String MAPPING_FILE_PROPERTY = "mappingFile"; //$NON-NLS-1$

	/**
	 * Return mapping file corresponding to the mapping file ref's file name.
	 * This can be <code>null</code> if the file name is invalid.
	 */
	MappingFile getMappingFile();


	// ********** persistence unit metadata **********

	/**
	 * Return the mapping file's persistence unit metadata.
	 */
	MappingFilePersistenceUnitMetadata getPersistenceUnitMetadata();

	/**
	 * Return whether the mapping file's persistence unit metadata exist.
	 */
	boolean persistenceUnitMetadataExists();


	// ********** misc **********

	/**
	 * Return the mapping file ref's corresponding resource mapping file ref.
	 */
	XmlMappingFileRef getXmlMappingFileRef();

	/**
	 * Return whether the mapping file ref is "implied" by, or explicitly
	 * specified in, the <code>persistence.xml</code>.
	 */
	boolean isImplied();

	/**
	 * Return whether the specified text offset is within
	 * the text representation of the mapping file.
	 */
	boolean containsOffset(int textOffset);


	// ********** refactoring **********

	/**
	 * Create DeleteEdits for deleting any references to the given type 
	 * that is about to be deleted.
	 * Return an EmptyIterable if there are not any references to the given type.
	 */
	Iterable<DeleteEdit> createDeleteTypeEdits(IType type);

	/**
	 * If this {@link #isFor(IFile)} the given IFile, create a text 
	 * DeleteEdit for deleting the mapping file element and any text that precedes it
	 * from the persistence.xml.
	 * Otherwise return an EmptyIterable.
	 * Though this will contain 1 or 0 DeleteEdits, using an Iterable
	 * for ease of use with other createDeleteEdit API.
	 */
	Iterable<DeleteEdit> createDeleteMappingFileEdits(IFile file);

	/**
	 * Create ReplaceEdits for renaming any references to the originalType to the newName.
	 * The originalType has not yet been renamed, the newName is the new short name.
	 */
	Iterable<ReplaceEdit> createRenameTypeEdits(IType originalType, String newName);

	/**
	 * Create ReplaceEdits for moving any references to the originalType to the newPackage.
	 * The originalType has not yet been moved.
	 */
	Iterable<ReplaceEdit> createMoveTypeEdits(IType originalType, IPackageFragment newPackage);

	/**
	 * Create ReplaceEdits for renaming any references to the originalPackage to the newName.
	 * The originalPackage has not yet been renamed.
	 */
	Iterable<ReplaceEdit> createRenamePackageEdits(IPackageFragment originalPackage, String newName);

	/**
	 * Create ReplaceEdits for renaming any references to the originalFolder to the newName.
	 * The originalFolder has not yet been renamed.
	 */
	Iterable<ReplaceEdit> createRenameFolderEdits(IFolder originalFolder, String newName);

	/**
	 * If this {@link #isFor(IFile)} the given IFile, create a text 
	 * ReplaceEdit for renaming the mapping file element to the new name.
	 * Otherwise return an EmptyIterable.
	 * Though this will contain 1 or 0 ReplaceEdits, using an Iterable
	 * for ease of use with other createReplaceMappingFileEdits API.
	 */
	Iterable<ReplaceEdit> createRenameMappingFileEdits(IFile originalFile, String newName);

	/**
	 * If this {@link #isFor(IFile)} the given IFile create a text
	 * ReplaceEdit for moving the originalFile to the destination.
	 * Otherwise return an EmptyIterable.
	 * The originalFile has not been moved yet.
	 */
	Iterable<ReplaceEdit> createMoveMappingFileEdits(IFile originalFile, IPath runtineDestination);

	/**
	 * Create ReplaceEdits for moving any references to the originalFolder to the runtimeDestination.
	 * The runtimeDestination already includes the original folder name.
	 */
	Iterable<ReplaceEdit> createMoveFolderEdits(IFolder originalFolder, IPath runtimeDestination);
}