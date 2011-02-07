/*******************************************************************************
 * Copyright (c) 2006, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.core.tests.internal.utility.jdt;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jpt.common.core.internal.utility.jdt.ASTTools;
import org.eclipse.jpt.common.core.internal.utility.jdt.ConversionDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.JDTFieldAttribute;
import org.eclipse.jpt.common.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationElementAdapter;

@SuppressWarnings("nls")
public class ASTToolsTests extends AnnotationTestCase {

	public ASTToolsTests(String name) {
		super(name);
	}

	private void createEnumAndMembers(String enumName, String enumBody) throws Exception {
		this.javaProject.createCompilationUnit("enums", enumName + ".java", "public enum " + enumName + " { " + enumBody + " }");
	}

	private void createAnnotationAndMembers(String annotationName, String annotationBody) throws Exception {
		this.javaProject.createCompilationUnit("annot", annotationName + ".java", "public @interface " + annotationName + " { " + annotationBody + " }");
	}

	public void testResolveEnum1() throws Exception {
		this.createEnumAndMembers("TestEnum", "FOO, BAR, BAZ");
		this.createAnnotationAndMembers("TestAnnotation", "TestEnum foo();");

		ICompilationUnit cu = this.createTestType("@annot.TestAnnotation(foo=enums.TestEnum.BAZ)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.TestAnnotation");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "foo");
		JDTFieldAttribute field = this.idField(cu);

		String actual = ASTTools.resolveEnum((Name) daea.getExpression(field.getModifiedDeclaration(this.buildASTRoot(cu))));
		assertEquals("enums.TestEnum.BAZ", actual);
	}

	public void testResolveEnum2() throws Exception {
		this.createEnumAndMembers("TestEnum", "FOO, BAR, BAZ");
		this.createAnnotationAndMembers("TestAnnotation", "TestEnum foo();");

		ICompilationUnit cu = this.createTestType("static enums.TestEnum.BAZ", "@annot.TestAnnotation(foo=BAZ)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.TestAnnotation");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "foo");
		JDTFieldAttribute field = this.idField(cu);

		String actual = ASTTools.resolveEnum((Name) daea.getExpression(field.getModifiedDeclaration(this.buildASTRoot(cu))));
		assertEquals("enums.TestEnum.BAZ", actual);
	}

	public void testResolveEnum3() throws Exception {
		this.createEnumAndMembers("TestEnum", "FOO, BAR, BAZ");
		this.createAnnotationAndMembers("TestAnnotation", "TestEnum foo();");

		ICompilationUnit cu = this.createTestType("static enums.TestEnum.*", "@annot.TestAnnotation(foo=BAZ)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.TestAnnotation");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "foo");
		JDTFieldAttribute field = this.idField(cu);

		String actual = ASTTools.resolveEnum((Name)daea.getExpression(field.getModifiedDeclaration(this.buildASTRoot(cu))));
		assertEquals("enums.TestEnum.BAZ", actual);
	}

	public void testResolveEnum4() throws Exception {
		this.createEnumAndMembers("TestEnum", "FOO, BAR, BAZ");
		this.createAnnotationAndMembers("TestAnnotation", "TestEnum foo();");

		ICompilationUnit cu = this.createTestType("enums.TestEnum", "@annot.TestAnnotation(foo=TestEnum.BAZ)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.TestAnnotation");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "foo");
		JDTFieldAttribute field = this.idField(cu);

		String actual = ASTTools.resolveEnum((Name) daea.getExpression(field.getModifiedDeclaration(this.buildASTRoot(cu))));
		assertEquals("enums.TestEnum.BAZ", actual);
	}

}