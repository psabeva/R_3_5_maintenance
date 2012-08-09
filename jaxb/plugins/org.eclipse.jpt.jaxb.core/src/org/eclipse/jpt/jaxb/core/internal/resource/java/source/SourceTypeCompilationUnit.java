/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jaxb.core.internal.resource.java.source;

import java.util.List;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jpt.common.core.JptCommonCorePlugin;
import org.eclipse.jpt.common.core.JptResourceType;
import org.eclipse.jpt.common.core.utility.jdt.AnnotationEditFormatter;
import org.eclipse.jpt.common.utility.CommandExecutor;
import org.eclipse.jpt.common.utility.internal.iterables.CompositeIterable;
import org.eclipse.jpt.common.utility.internal.iterables.EmptyIterable;
import org.eclipse.jpt.jaxb.core.AnnotationProvider;
import org.eclipse.jpt.jaxb.core.resource.java.JavaResourceAbstractType;

/**
 * Java compilation unit (source file)
 * non package-info.java file
 */
public final class SourceTypeCompilationUnit
	extends SourceCompilationUnit
{

	/**
	 * The primary type of the AST compilation unit. We are not going to handle
	 * multiple types defined in a single compilation unit. Entities must have
	 * a public/protected no-arg constructor, and there is no way to access
	 * the constructor in a package class (which is what all top-level,
	 * non-primary classes must be).
	 */
	private JavaResourceAbstractType type;	


	// ********** construction **********

	public SourceTypeCompilationUnit(
			ICompilationUnit compilationUnit,
			AnnotationProvider annotationProvider, 
			AnnotationEditFormatter annotationEditFormatter,
			CommandExecutor modifySharedDocumentCommandExecutor) {
		super(compilationUnit, annotationProvider, annotationEditFormatter, modifySharedDocumentCommandExecutor);  // the compilation unit is the root of its sub-tree
		this.type = this.buildType();
	}

	private JavaResourceAbstractType buildType() {
		this.openCompilationUnit();
		CompilationUnit astRoot = this.buildASTRoot();
		this.closeCompilationUnit();
		return this.buildPersistentType(astRoot);
	}


	// ********** JavaResourceNode implementation **********

	public void synchronizeWith(CompilationUnit astRoot) {
		this.syncType(astRoot);
	}


	// ********** JavaResourceNode.Root implementation **********

	/**
	 * NB: return *all* the types since we build them all
	 */
	public Iterable<JavaResourceAbstractType> getTypes() {
		return (this.type == null) ?
				EmptyIterable.<JavaResourceAbstractType>instance() :
				new CompositeIterable<JavaResourceAbstractType>(this.type.getAllTypes(), this.type.getAllEnums());
	}
	
	
	// ********** JpaResourceModel implementation **********
	
	public JptResourceType getResourceType() {
		return JptCommonCorePlugin.JAVA_SOURCE_RESOURCE_TYPE;
	}


	// ********** JavaResourceCompilationUnit implementation **********

	public void resolveTypes() {
		if (this.type != null) {
			this.type.resolveTypes(this.buildASTRoot());
		}
	}


	// ********** persistent type **********

	private JavaResourceAbstractType buildPersistentType(CompilationUnit astRoot) {
		AbstractTypeDeclaration td = this.getPrimaryTypeDeclaration(astRoot);
		return (td == null) ? null : this.buildType(astRoot, td);
	}


	private void syncType(CompilationUnit astRoot) {
		AbstractTypeDeclaration td = this.getPrimaryTypeDeclaration(astRoot);
		if (td == null) {
			this.syncType_(null);
		} else {
			if (this.type == null || (!typeMatchesASTNodeType(this.type, td))) {
				this.syncType_(this.buildType(astRoot, td));
			} else {
				this.type.synchronizeWith(astRoot);
			}
		}
	}


	/**
	 * Must be non-null JavaResourceAbstractType and AbstractTypeDeclaration
	 */
	protected static boolean typeMatchesASTNodeType(JavaResourceAbstractType type, AbstractTypeDeclaration astType) {
		return type.getKind().getAstNodeType() == astType.getNodeType();
	}

	private void syncType_(JavaResourceAbstractType astType) {
		JavaResourceAbstractType old = this.type;
		this.type = astType;
		this.firePropertyChanged(TYPES_COLLECTION, old, astType);
	}


	// ********** internal **********

	private JavaResourceAbstractType buildType(CompilationUnit astRoot, AbstractTypeDeclaration typeDeclaration) {
		if (typeDeclaration.getNodeType() == ASTNode.TYPE_DECLARATION) {
			return SourceType.newInstance(this, (TypeDeclaration) typeDeclaration, astRoot);
		}
		else if (typeDeclaration.getNodeType() == ASTNode.ENUM_DECLARATION) {
			return SourceEnum.newInstance(this, (EnumDeclaration) typeDeclaration, astRoot);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * i.e. the type with the same name as the compilation unit;
	 * return the first class or interface (ignore annotations and enums) with
	 * the same name as the compilation unit (file);
	 * NB: this type could be in error if there is an annotation or enum
	 * with the same name preceding it in the compilation unit
	 * 
	 * Return null if the parser did not resolve the type declaration's binding.
	 * This can occur if the project JRE is removed (bug 225332).
	 */
	private AbstractTypeDeclaration getPrimaryTypeDeclaration(CompilationUnit astRoot) {
		String primaryTypeName = this.getPrimaryTypeName();
		for (AbstractTypeDeclaration atd : this.types(astRoot)) {
			if (this.nodeIsPrimaryTypeDeclaration(atd, primaryTypeName)) {
				return (atd.resolveBinding() == null) ? null : atd;
			}
		}
		return null;
	}

	private boolean nodeIsPrimaryTypeDeclaration(AbstractTypeDeclaration atd, String primaryTypeName) {
		return (atd.getNodeType() == ASTNode.TYPE_DECLARATION || atd.getNodeType() == ASTNode.ENUM_DECLARATION) && 
				(atd.getName().getFullyQualifiedName().equals(primaryTypeName));
	}

	private String getPrimaryTypeName() {
		return this.getCompilationUnitName();
	}

	// minimize scope of suppressed warnings
	@SuppressWarnings("unchecked")
	private List<AbstractTypeDeclaration> types(CompilationUnit astRoot) {
		return astRoot.types();
	}
}