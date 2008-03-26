/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.tests.internal.utility.jdt;

import java.util.Arrays;

import org.eclipse.jpt.core.internal.utility.jdt.ASTNodeTextRange;
import org.eclipse.jpt.core.internal.utility.jdt.AnnotationStringArrayExpressionConverter;
import org.eclipse.jpt.core.internal.utility.jdt.BooleanExpressionConverter;
import org.eclipse.jpt.core.internal.utility.jdt.CharacterStringExpressionConverter;
import org.eclipse.jpt.core.internal.utility.jdt.ConversionDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.EnumArrayDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.EnumDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.MemberAnnotationElementAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.NestedDeclarationAnnotationAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.NestedIndexedDeclarationAnnotationAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.NumberIntegerExpressionConverter;
import org.eclipse.jpt.core.internal.utility.jdt.PrimitiveTypeStringExpressionConverter;
import org.eclipse.jpt.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.core.internal.utility.jdt.SimpleTypeStringExpressionConverter;
import org.eclipse.jpt.core.internal.utility.jdt.StringExpressionConverter;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.core.utility.jdt.AnnotationElementAdapter;
import org.eclipse.jpt.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.core.utility.jdt.DeclarationAnnotationElementAdapter;
import org.eclipse.jpt.core.utility.jdt.ExpressionConverter;

public class MemberAnnotationElementAdapterTests extends AnnotationTestCase {

	public MemberAnnotationElementAdapterTests(String name) {
		super(name);
	}

	private void createAnnotationAndMembers(String annotationName, String annotationBody) throws Exception {
		this.javaProject.createType("annot", annotationName + ".java", "public @interface " + annotationName + " { " + annotationBody + " }");
	}

	private void createEnum(String enumName, String enumBody) throws Exception {
		this.javaProject.createType("enums", enumName + ".java", "public enum " + enumName + " { " + enumBody + " }");
	}

	public void testValue1() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar();");
		this.createTestType("@annot.Foo(bar=\"xxx\")");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertEquals("xxx", aea.value());
	}

	public void testValue2() throws Exception {
		this.createAnnotationAndMembers("Foo", "int bar();");
		this.createTestType("@annot.Foo(bar=48)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<Integer> daea = new ConversionDeclarationAnnotationElementAdapter<Integer>(daa, "bar", NumberIntegerExpressionConverter.instance());
		AnnotationElementAdapter<Integer> aea = new MemberAnnotationElementAdapter<Integer>(this.idField(), daea);
		assertEquals(Integer.valueOf(48), aea.value());
	}

	public void testValue3() throws Exception {
		this.createAnnotationAndMembers("Foo", "char bar();");
		this.createTestType("@annot.Foo(bar='c')");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new ConversionDeclarationAnnotationElementAdapter<String>(daa, "bar", CharacterStringExpressionConverter.instance());
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertEquals("c", aea.value());
	}

	public void testValue4() throws Exception {
		this.createAnnotationAndMembers("Foo", "boolean bar();");
		this.createTestType("@annot.Foo(bar=false)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<Boolean> daea = new ConversionDeclarationAnnotationElementAdapter<Boolean>(daa, "bar", BooleanExpressionConverter.instance());
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);
		assertEquals(Boolean.FALSE, aea.value());
	}

	public void testValue5() throws Exception {
		this.createAnnotationAndMembers("Baz", "boolean fred();");
		this.createAnnotationAndMembers("Bar", "annot.Baz jimmy();");
		this.createAnnotationAndMembers("Foo", "annot.Bar value();");
		this.createTestType("@annot.Foo(@annot.Bar(jimmy=@annot.Baz(fred=false)))");
		DeclarationAnnotationAdapter daa1 = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationAdapter daa2 = new NestedDeclarationAnnotationAdapter(daa1, "value", "annot.Bar");
		DeclarationAnnotationAdapter daa3 = new NestedDeclarationAnnotationAdapter(daa2, "jimmy", "annot.Baz");
		DeclarationAnnotationElementAdapter<Boolean> daea = new ConversionDeclarationAnnotationElementAdapter<Boolean>(daa3, "fred", BooleanExpressionConverter.instance());
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);
		assertEquals(Boolean.FALSE, aea.value());
	}

	public void testValue6() throws Exception {
		this.createAnnotationAndMembers("Foo", "boolean value();");
		this.createTestType("@annot.Foo(false)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<Boolean> daea = new ConversionDeclarationAnnotationElementAdapter<Boolean>(daa, BooleanExpressionConverter.instance());
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);
		assertEquals(Boolean.FALSE, aea.value());
	}

	public void testValueNull1() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar();");
		this.createTestType("@annot.Foo");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertNull(aea.value());
	}

	public void testValueNull2() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar();");
		this.createTestType();
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertNull(aea.value());
	}

	public void testValueNull3() throws Exception {
		this.createAnnotationAndMembers("Baz", "String fred();");
		this.createAnnotationAndMembers("Bar", "annot.Baz jimmy();");
		this.createAnnotationAndMembers("Foo", "annot.Bar value();");
		this.createTestType("@annot.Foo(@annot.Bar(jimmy=@annot.Baz))");
		DeclarationAnnotationAdapter daa1 = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationAdapter daa2 = new NestedDeclarationAnnotationAdapter(daa1, "value", "annot.Bar");
		DeclarationAnnotationAdapter daa3 = new NestedDeclarationAnnotationAdapter(daa2, "jimmy", "annot.Baz");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa3, "fred");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertNull(aea.value());
	}

	public void testValueStringConcatenation() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar();");
		this.createTestType("@annot.Foo(bar=\"xxx\" + \"yyy\" + \"zzz\")");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertEquals("xxxyyyzzz", aea.value());
	}

	public void testValueStringConstant() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar();");
		// just a bit hacky:
		this.createTestType("private static final String FOO_BAR = \"xxx\"; @annot.Foo(bar=FOO_BAR + \"yyy\" + \"zzz\")");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertEquals("xxxyyyzzz", aea.value());
	}

	public void testValueNumberArithmetic() throws Exception {
		this.createAnnotationAndMembers("Foo", "int bar();");
		this.createTestType("@annot.Foo(bar=47 - 7 + 2 * 1 / 1)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<Integer> daea = ConversionDeclarationAnnotationElementAdapter.forNumbers(daa, "bar");
		AnnotationElementAdapter<Integer> aea = new MemberAnnotationElementAdapter<Integer>(this.idField(), daea);
		assertEquals(Integer.valueOf(42), aea.value());
	}

	public void testValueNumberShift() throws Exception {
		this.createAnnotationAndMembers("Foo", "int bar();");
		this.createTestType("@annot.Foo(bar=2 << 2)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<Integer> daea = ConversionDeclarationAnnotationElementAdapter.forNumbers(daa, "bar");
		AnnotationElementAdapter<Integer> aea = new MemberAnnotationElementAdapter<Integer>(this.idField(), daea);
		assertEquals(Integer.valueOf(8), aea.value());
	}

	public void testValueNumberConstant() throws Exception {
		this.createAnnotationAndMembers("Foo", "int bar();");
		// just a bit hacky:
		this.createTestType("private static final int FOO_BAR = 77; @annot.Foo(bar=FOO_BAR)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<Integer> daea = ConversionDeclarationAnnotationElementAdapter.forNumbers(daa, "bar");
		AnnotationElementAdapter<Integer> aea = new MemberAnnotationElementAdapter<Integer>(this.idField(), daea);
		assertEquals(Integer.valueOf(77), aea.value());
	}

	public void testValueCharacterConstant() throws Exception {
		this.createAnnotationAndMembers("Foo", "char bar();");
		// just a bit hacky:
		this.createTestType("private static final char FOO_BAR = 'Q'; @annot.Foo(bar=FOO_BAR)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forCharacters(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertEquals("Q", aea.value());
	}

	public void testValueCharacterCast() throws Exception {
		this.createAnnotationAndMembers("Foo", "char bar();");
		this.createTestType("@annot.Foo(bar=(char) 0x41)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forCharacters(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertEquals("A", aea.value());
	}

	public void testValueBooleanOperator1() throws Exception {
		this.createAnnotationAndMembers("Foo", "boolean bar();");
		this.createTestType("@annot.Foo(bar=7 > 2)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<Boolean> daea = ConversionDeclarationAnnotationElementAdapter.forBooleans(daa, "bar");
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);
		assertEquals(Boolean.TRUE, aea.value());
	}

	public void testValueBooleanOperator2() throws Exception {
		this.createAnnotationAndMembers("Foo", "boolean bar();");
		this.createTestType("@annot.Foo(bar=7 == 2)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<Boolean> daea = ConversionDeclarationAnnotationElementAdapter.forBooleans(daa, "bar");
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);
		assertEquals(Boolean.FALSE, aea.value());
	}

	public void testValueBooleanOperator3() throws Exception {
		this.createAnnotationAndMembers("Foo", "boolean bar();");
		this.createTestType("@annot.Foo(bar=(7 != 2) && false)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<Boolean> daea = ConversionDeclarationAnnotationElementAdapter.forBooleans(daa, "bar");
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);
		assertEquals(Boolean.FALSE, aea.value());
	}

	public void testValueBooleanOperator4() throws Exception {
		this.createAnnotationAndMembers("Foo", "boolean bar();");
		this.createTestType("@annot.Foo(bar=(7 != 2) ? false : true)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<Boolean> daea = ConversionDeclarationAnnotationElementAdapter.forBooleans(daa, "bar");
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);
		assertEquals(Boolean.FALSE, aea.value());
	}

	public void testValueInvalidValue1() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar();");
		this.createTestType("@annot.Foo(bar=77)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertNull(aea.value());
	}

	public void testValueInvalidValue2() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar();");
		this.createTestType("@annot.Foo(bar=bazzzz)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertNull(aea.value());
	}

	public void testValueInvalidValue3() throws Exception {
		this.createAnnotationAndMembers("Foo", "boolean bar();");
		this.createTestType("@annot.Foo(bar=bazzzz)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<Boolean> daea = ConversionDeclarationAnnotationElementAdapter.forBooleans(daa, "bar");
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);
		assertNull(aea.value());
	}

	public void testValueInvalidValue4() throws Exception {
		this.createAnnotationAndMembers("Foo", "char bar();");
		this.createTestType("@annot.Foo(bar=\"bazzzz\")");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forCharacters(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertNull(aea.value());
	}

	public void testValueInvalidValue5() throws Exception {
		this.createAnnotationAndMembers("Foo", "Class bar();");
		this.createTestType("@annot.Foo(bar=\"java.lang.Object\")");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new ConversionDeclarationAnnotationElementAdapter<String>(daa, "bar", SimpleTypeStringExpressionConverter.instance());
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertNull(aea.value());
	}

	public void testValueInvalidValue6() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum bar();");
		this.createTestType("@annot.Foo(bar=enums.TestEnum.class)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new EnumDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertNull(aea.value());
	}

	public void testValueInvalidValue7() throws Exception {
		this.createAnnotationAndMembers("Baz", "boolean fred();");
		this.createAnnotationAndMembers("Bar", "annot.Baz jimmy();");
		this.createAnnotationAndMembers("Foo", "annot.Bar value();");
		String value = "\"false\"";
		String element = "fred=" + value;
		String annotation = "@annot.Foo(@annot.Bar(jimmy=@annot.Baz(" + element + ")))";
		this.createTestType(annotation);
		DeclarationAnnotationAdapter daa1 = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationAdapter daa2 = new NestedDeclarationAnnotationAdapter(daa1, "value", "annot.Bar");
		DeclarationAnnotationAdapter daa3 = new NestedDeclarationAnnotationAdapter(daa2, "jimmy", "annot.Baz");
		DeclarationAnnotationElementAdapter<Boolean> daea = new ConversionDeclarationAnnotationElementAdapter<Boolean>(daa3, "fred", BooleanExpressionConverter.instance());
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);
		assertNull(aea.value());
	}

	public void testValueInvalidValue8() throws Exception {
		this.createAnnotationAndMembers("Foo", "String[] bar();");
		this.createTestType("@annot.Foo(bar={true, false})");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new ConversionDeclarationAnnotationElementAdapter<String[]>(daa, "bar", AnnotationStringArrayExpressionConverter.forStrings());
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		assertTrue(Arrays.equals(new String[] {null, null}, aea.value()));
	}

	public void testValueInvalidValue9() throws Exception {
		this.createAnnotationAndMembers("Foo", "String[] bar();");
		this.createTestType("@annot.Foo(bar=77)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new ConversionDeclarationAnnotationElementAdapter<String[]>(daa, "bar", AnnotationStringArrayExpressionConverter.forStrings());
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		assertTrue(Arrays.equals(new String[] {null}, aea.value()));
	}

	public void testASTNode1() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar();");
		String value = "\"xxx\"";
		String element = "bar=" + value;
		String annotation = "@annot.Foo(" + element + ")";
		this.createTestType(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);

		TextRange textRange = new ASTNodeTextRange(aea.astNode());
		assertEquals(this.source().indexOf(value), textRange.getOffset());
		assertEquals(value.length(), textRange.getLength());
		assertEquals(8, textRange.getLineNumber());
	}

	public void testASTNode2() throws Exception {
		this.createAnnotationAndMembers("Baz", "boolean fred();");
		this.createAnnotationAndMembers("Bar", "annot.Baz jimmy();");
		this.createAnnotationAndMembers("Foo", "annot.Bar value();");
		String value = "false";
		String element = "fred=" + value;
		String annotation = "@annot.Foo(@annot.Bar(jimmy=@annot.Baz(" + element + ")))";
		this.createTestType(annotation);
		DeclarationAnnotationAdapter daa1 = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationAdapter daa2 = new NestedDeclarationAnnotationAdapter(daa1, "value", "annot.Bar");
		DeclarationAnnotationAdapter daa3 = new NestedDeclarationAnnotationAdapter(daa2, "jimmy", "annot.Baz");
		DeclarationAnnotationElementAdapter<Boolean> daea = new ConversionDeclarationAnnotationElementAdapter<Boolean>(daa3, "fred", BooleanExpressionConverter.instance());
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);
		assertEquals(Boolean.FALSE, aea.value());
		TextRange textRange = new ASTNodeTextRange(aea.astNode());
		assertEquals(value.length(), textRange.getLength());
	}

	public void testASTNode3() throws Exception {
		this.createAnnotationAndMembers("Foo", "String value();");
		String element = "\"xxx\"";
		String annotation = "@annot.Foo(" + element + ")";
		this.createTestType(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa);
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);

		TextRange textRange = new ASTNodeTextRange(aea.astNode());
		assertEquals(this.source().indexOf(element), textRange.getOffset());
		assertEquals(element.length(), textRange.getLength());
	}

	public void testASTNode4() throws Exception {
		this.createAnnotationAndMembers("Foo", "String value();");
		String annotation = "@annot.Foo";
		this.createTestType(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa);
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);

		TextRange textRange = new ASTNodeTextRange(aea.astNode());
		assertEquals(this.source().indexOf(annotation), textRange.getOffset());
		assertEquals(annotation.length(), textRange.getLength());
	}

	public void testSetValue1() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar();");
		String annotation = "@annot.Foo(bar=\"xxx\")";
		this.createTestType(annotation);
		this.assertSourceContains(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);

		aea.setValue(null);
		this.assertSourceDoesNotContain("Foo");
	}

	public void testSetValue2() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar();");
		String annotation = "@annot.Foo(bar=\"xxx\")";
		this.createTestType(annotation);
		this.assertSourceContains(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar", false);
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);

		aea.setValue(null);
		this.assertSourceDoesNotContain(annotation);
		this.assertSourceContains("@Foo");
	}

	public void testSetValue3() throws Exception {
		this.createAnnotationAndMembers("Baz", "boolean fred();");
		this.createAnnotationAndMembers("Bar", "annot.Baz jimmy();");
		this.createAnnotationAndMembers("Foo", "annot.Bar value();");
		String annotation = "@annot.Foo(@annot.Bar(jimmy=@annot.Baz(fred=false)))";
		this.createTestType(annotation);
		this.assertSourceContains(annotation);
		DeclarationAnnotationAdapter daa1 = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationAdapter daa2 = new NestedDeclarationAnnotationAdapter(daa1, "value", "annot.Bar");
		DeclarationAnnotationAdapter daa3 = new NestedDeclarationAnnotationAdapter(daa2, "jimmy", "annot.Baz");
		DeclarationAnnotationElementAdapter<Boolean> daea = new ConversionDeclarationAnnotationElementAdapter<Boolean>(daa3, "fred", BooleanExpressionConverter.instance());
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);

		aea.setValue(null);
		this.assertSourceDoesNotContain(annotation);
		this.assertSourceDoesNotContain("Foo");
		this.assertSourceDoesNotContain("Bar");
	}

	public void testSetValue3a() throws Exception {
		this.createAnnotationAndMembers("Baz", "boolean fred();");
		this.createAnnotationAndMembers("Bar", "annot.Baz jimmy();");
		this.createAnnotationAndMembers("Foo", "annot.Bar value();");
		String annotation = "@annot.Foo(@annot.Bar(jimmy=@annot.Baz(fred=false)))";
		this.createTestType(annotation);
		this.assertSourceContains(annotation);
		DeclarationAnnotationAdapter daa1 = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationAdapter daa2 = new NestedDeclarationAnnotationAdapter(daa1, "value", "annot.Bar", false);
		DeclarationAnnotationAdapter daa3 = new NestedDeclarationAnnotationAdapter(daa2, "jimmy", "annot.Baz", false);
		DeclarationAnnotationElementAdapter<Boolean> daea = new ConversionDeclarationAnnotationElementAdapter<Boolean>(daa3, "fred", BooleanExpressionConverter.instance());
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);

		aea.setValue(null);
		this.assertSourceDoesNotContain(annotation);
		this.assertSourceContains("@annot.Foo(@Bar)");
	}

	public void testSetValue4() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar();");
		this.createTestType();
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);

		aea.setValue("xxx");
		this.assertSourceContains("@Foo(bar=\"xxx\")");
	}

	public void testSetValue5() throws Exception {
		this.createAnnotationAndMembers("Baz", "boolean fred();");
		this.createAnnotationAndMembers("Bar", "annot.Baz jimmy();");
		this.createAnnotationAndMembers("Foo", "annot.Bar value();");
		String annotation = "@annot.Foo(@annot.Bar(jimmy=@annot.Baz(fred=false)))";
		this.createTestType(annotation);
		this.assertSourceContains(annotation);
		DeclarationAnnotationAdapter daa1 = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationAdapter daa2 = new NestedDeclarationAnnotationAdapter(daa1, "value", "annot.Bar");
		DeclarationAnnotationAdapter daa3 = new NestedDeclarationAnnotationAdapter(daa2, "jimmy", "annot.Baz");
		DeclarationAnnotationElementAdapter<Boolean> daea = new ConversionDeclarationAnnotationElementAdapter<Boolean>(daa3, "fred", BooleanExpressionConverter.instance());
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);

		aea.setValue(Boolean.TRUE);
		this.assertSourceDoesNotContain(annotation);
		this.assertSourceContains("@annot.Foo(@annot.Bar(jimmy=@annot.Baz(fred=true)))");
	}

	public void testSetValue6() throws Exception {
		this.createAnnotationAndMembers("Baz", "boolean fred();");
		this.createAnnotationAndMembers("Bar", "annot.Baz jimmy();");
		this.createAnnotationAndMembers("Foo", "annot.Bar value();");
		this.createTestType();
		DeclarationAnnotationAdapter daa1 = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationAdapter daa2 = new NestedDeclarationAnnotationAdapter(daa1, "value", "annot.Bar");
		DeclarationAnnotationAdapter daa3 = new NestedDeclarationAnnotationAdapter(daa2, "jimmy", "annot.Baz");
		DeclarationAnnotationElementAdapter<Boolean> daea = new ConversionDeclarationAnnotationElementAdapter<Boolean>(daa3, "fred", BooleanExpressionConverter.instance());
		AnnotationElementAdapter<Boolean> aea = new MemberAnnotationElementAdapter<Boolean>(this.idField(), daea);

		aea.setValue(Boolean.TRUE);
		this.assertSourceContains("@Foo(@Bar(jimmy=@Baz(fred=true)))");
	}

	public void testSetValue7() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar();");
		String annotation = "@annot.Foo(bar=\"xxx\")";
		this.createTestType(annotation);
		this.assertSourceContains(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);

		aea.setValue("yyy");
		this.assertSourceDoesNotContain(annotation);
		this.assertSourceContains("@annot.Foo(bar=\"yyy\")");
	}

	public void testSetValue8() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar();");
		String annotation = "@annot.Foo";
		this.createTestType(annotation);
		this.assertSourceContains(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);

		aea.setValue("xxx");
		this.assertSourceContains("@Foo(bar=\"xxx\")");
	}

	public void testSetValue9() throws Exception {
		this.createAnnotationAndMembers("Foo", "String value(); String bar();");
		String annotation = "@annot.Foo(\"zzz\")";
		this.createTestType(annotation);
		this.assertSourceContains(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);

		aea.setValue("xxx");
		this.assertSourceDoesNotContain(annotation);
		this.assertSourceContains("@Foo(value=\"zzz\", bar=\"xxx\")");
	}

	public void testSetValue10() throws Exception {
		this.createAnnotationAndMembers("Foo", "String bar(); String baz();");
		String annotation = "@annot.Foo(bar=\"xxx\")";
		this.createTestType(annotation);
		this.assertSourceContains(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "baz");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);

		aea.setValue("yyy");
		this.assertSourceDoesNotContain(annotation);
		this.assertSourceContains("@annot.Foo(bar=\"xxx\", baz = \"yyy\")");
	}

	public void testSetValue11() throws Exception {
		this.createAnnotationAndMembers("Baz", "int fred();");
		this.createAnnotationAndMembers("Bar", "annot.Baz[] jimmy();");
		this.createAnnotationAndMembers("Foo", "annot.Bar value();");
		String annotation = "@annot.Foo(@annot.Bar(jimmy={@annot.Baz(fred=0), @annot.Baz(fred=1), @annot.Baz(fred=2), @annot.Baz(fred=3)}))";
		this.createTestType(annotation);
		this.assertSourceContains(annotation);
		DeclarationAnnotationAdapter daa1 = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationAdapter daa2 = new NestedDeclarationAnnotationAdapter(daa1, "value", "annot.Bar");
		DeclarationAnnotationAdapter daa3 = new NestedIndexedDeclarationAnnotationAdapter(daa2, "jimmy", 2, "annot.Baz");
		DeclarationAnnotationElementAdapter<Integer> daea = new ConversionDeclarationAnnotationElementAdapter<Integer>(daa3, "fred", NumberIntegerExpressionConverter.instance());
		AnnotationElementAdapter<Integer> aea = new MemberAnnotationElementAdapter<Integer>(this.idField(), daea);

		assertEquals(Integer.valueOf(2), aea.value());
		aea.setValue(Integer.valueOf(48));
		this.assertSourceContains("@annot.Foo(@annot.Bar(jimmy={@annot.Baz(fred=0), @annot.Baz(fred=1), @annot.Baz(fred=48), @annot.Baz(fred=3)}))");
	}

	public void testSetValue12() throws Exception {
		this.createAnnotationAndMembers("Foo", "String value();");
		String annotation = "@annot.Foo";
		this.createTestType(annotation);
		this.assertSourceContains(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "value");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);

		aea.setValue("xxx");
		this.assertSourceContains("@Foo(\"xxx\")");
	}

	public void testSetValue13() throws Exception {
		this.createAnnotationAndMembers("Foo", "String value();");
		String annotation = "@annot.Foo(\"zzz\")";
		this.createTestType(annotation);
		this.assertSourceContains(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = ConversionDeclarationAnnotationElementAdapter.forStrings(daa, "value");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);

		aea.setValue("xxx");
		this.assertSourceDoesNotContain(annotation);
		this.assertSourceContains("@annot.Foo(\"xxx\")");
	}

	public void testSimpleTypeLiteral1() throws Exception {
		this.createAnnotationAndMembers("Foo", "Class bar();");
		this.createTestType("@annot.Foo(bar=java.lang.Object.class)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new ConversionDeclarationAnnotationElementAdapter<String>(daa, "bar", SimpleTypeStringExpressionConverter.instance());
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertEquals("java.lang.Object", aea.value());
	}

	public void testSimpleTypeLiteral2() throws Exception {
		this.createAnnotationAndMembers("Foo", "Class bar();");
		this.createTestType();
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new ConversionDeclarationAnnotationElementAdapter<String>(daa, "bar", SimpleTypeStringExpressionConverter.instance());
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		aea.setValue("java.lang.Object");
		this.assertSourceContains("@Foo(bar=java.lang.Object.class)");
	}

	public void testSimpleTypeLiteral3() throws Exception {
		this.createAnnotationAndMembers("Foo", "Class bar();");
		this.createTestType("@annot.Foo(bar=int.class)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new ConversionDeclarationAnnotationElementAdapter<String>(daa, "bar", SimpleTypeStringExpressionConverter.instance());
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertNull(aea.value());
	}

	public void testSimpleTypeLiteral4() throws Exception {
		this.createAnnotationAndMembers("Foo", "Class bar();");
		this.createTestType("@annot.Foo(bar=java.util.Map.Entry.class)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new ConversionDeclarationAnnotationElementAdapter<String>(daa, "bar", SimpleTypeStringExpressionConverter.instance());
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertEquals("java.util.Map.Entry", aea.value());
	}

	public void testPrimitiveTypeLiteral1() throws Exception {
		this.createAnnotationAndMembers("Foo", "Class bar();");
		this.createTestType("@annot.Foo(bar=int.class)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new ConversionDeclarationAnnotationElementAdapter<String>(daa, "bar", PrimitiveTypeStringExpressionConverter.instance());
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertEquals("int", aea.value());
	}

	public void testPrimitiveTypeLiteral2() throws Exception {
		this.createAnnotationAndMembers("Foo", "Class bar();");
		this.createTestType();
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new ConversionDeclarationAnnotationElementAdapter<String>(daa, "bar", PrimitiveTypeStringExpressionConverter.instance());
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		aea.setValue("int");
		this.assertSourceContains("@Foo(bar=int.class)");
	}

	public void testPrimitiveTypeLiteral3() throws Exception {
		this.createAnnotationAndMembers("Foo", "Class bar();");
		this.createTestType("@annot.Foo(bar=java.lang.Object.class)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new ConversionDeclarationAnnotationElementAdapter<String>(daa, "bar", PrimitiveTypeStringExpressionConverter.instance());
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertNull(aea.value());
	}

	public void testPrimitiveTypeLiteral4() throws Exception {
		this.createAnnotationAndMembers("Foo", "Class bar();");
		this.createTestType("@annot.Foo(bar=void.class)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new ConversionDeclarationAnnotationElementAdapter<String>(daa, "bar", PrimitiveTypeStringExpressionConverter.instance());
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertEquals("void", aea.value());
	}

	public void testValueEnum1() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum bar();");
		this.createTestType("@annot.Foo(bar=enums.TestEnum.XXX)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new EnumDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertEquals("enums.TestEnum.XXX", aea.value());
	}

	public void testValueEnum2() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum bar();");
		this.createTestType("static enums.TestEnum.XXX", "@annot.Foo(bar=XXX)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new EnumDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertEquals("enums.TestEnum.XXX", aea.value());
	}

	public void testValueEnum3() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum bar();");
		this.createTestType("@annot.Foo");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new EnumDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertNull(aea.value());
	}

	public void testValueEnum4() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum bar();");
		this.createTestType("enums.TestEnum", "@annot.Foo(bar=TestEnum.XXX)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new EnumDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		assertEquals("enums.TestEnum.XXX", aea.value());
	}

	public void testSetValueEnum1() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum bar();");
		String annotation = "@annot.Foo(bar=XXX)";
		this.createTestType("static enums.TestEnum.XXX", annotation);
		this.assertSourceContains(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new EnumDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		aea.setValue(null);
		this.assertSourceDoesNotContain("Foo");
	}

	public void testSetValueEnum2() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum bar();");
		String annotation = "@Foo(bar=XXX)";
		this.createTestType();
		this.assertSourceDoesNotContain(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String> daea = new EnumDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String> aea = new MemberAnnotationElementAdapter<String>(this.idField(), daea);
		aea.setValue("enums.TestEnum.XXX");
		this.assertSourceContains("import static enums.TestEnum.XXX;");
		this.assertSourceContains(annotation);
	}

	public void testValueStringArray() throws Exception {
		this.createAnnotationAndMembers("Foo", "String[] bar();");
		this.createTestType("@annot.Foo(bar={\"string0\", \"string1\"})");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new ConversionDeclarationAnnotationElementAdapter<String[]>(daa, "bar", AnnotationStringArrayExpressionConverter.forStrings());
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		assertTrue(Arrays.equals(new String[] {"string0", "string1"}, aea.value()));
	}

	public void testValueStringArrayConcatenation() throws Exception {
		this.createAnnotationAndMembers("Foo", "String[] bar();");
		this.createTestType("@annot.Foo(bar={\"stri\" + \"ng0\", \"s\" + \"tring1\"})");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new ConversionDeclarationAnnotationElementAdapter<String[]>(daa, "bar", AnnotationStringArrayExpressionConverter.forStrings());
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		assertTrue(Arrays.equals(new String[] {"string0", "string1"}, aea.value()));
	}

	public void testValueStringArrayEmpty() throws Exception {
		this.createAnnotationAndMembers("Foo", "String[] bar();");
		this.createTestType("@annot.Foo(bar={})");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new ConversionDeclarationAnnotationElementAdapter<String[]>(daa, "bar", AnnotationStringArrayExpressionConverter.forStrings());
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		assertTrue(Arrays.equals(new String[0], aea.value()));
	}

	public void testValueStringArraySingleElement() throws Exception {
		this.createAnnotationAndMembers("Foo", "String[] bar();");
		this.createTestType("@annot.Foo(bar=\"string0\")");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new ConversionDeclarationAnnotationElementAdapter<String[]>(daa, "bar", AnnotationStringArrayExpressionConverter.forStrings());
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		assertTrue(Arrays.equals(new String[] {"string0"}, aea.value()));
	}

	public void testValueNullStringArray() throws Exception {
		this.createAnnotationAndMembers("Foo", "String[] bar();");
		this.createTestType("@annot.Foo()");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new ConversionDeclarationAnnotationElementAdapter<String[]>(daa, "bar", AnnotationStringArrayExpressionConverter.forStrings());
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		assertTrue(Arrays.equals(new String[0], aea.value()));
	}

	public void testSetValueStringArray() throws Exception {
		this.createAnnotationAndMembers("Foo", "String[] bar();");
		String annotation = "@Foo(bar={\"string0\",\"string1\"})";
		this.createTestType();
		this.assertSourceDoesNotContain(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new ConversionDeclarationAnnotationElementAdapter<String[]>(daa, "bar", AnnotationStringArrayExpressionConverter.forStrings());
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		aea.setValue(new String[] {"string0", "string1"});
		this.assertSourceContains(annotation);
	}

	public void testSetValueStringArrayEmptyRemove() throws Exception {
		this.createAnnotationAndMembers("Foo", "String[] bar();");
		String annotation = "@Foo";
		this.createTestType();
		this.assertSourceDoesNotContain(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new ConversionDeclarationAnnotationElementAdapter<String[]>(daa, "bar", AnnotationStringArrayExpressionConverter.forStrings());
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		aea.setValue(new String[0]);
		this.assertSourceDoesNotContain(annotation);
	}

	public void testSetValueStringArrayEmpty() throws Exception {
		this.createAnnotationAndMembers("Foo", "String[] bar();");
		String annotation = "@Foo(bar={})";
		this.createTestType();
		this.assertSourceDoesNotContain(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		ExpressionConverter<String[]> expressionConverter = new AnnotationStringArrayExpressionConverter(StringExpressionConverter.instance(), false);
		DeclarationAnnotationElementAdapter<String[]> daea = new ConversionDeclarationAnnotationElementAdapter<String[]>(daa, "bar", expressionConverter);
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		aea.setValue(new String[0]);
		this.assertSourceContains(annotation);
	}

	public void testSetValueStringArraySingleElement() throws Exception {
		this.createAnnotationAndMembers("Foo", "String[] bar();");
		String annotation = "@Foo(bar=\"string0\")";
		this.createTestType();
		this.assertSourceDoesNotContain(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new ConversionDeclarationAnnotationElementAdapter<String[]>(daa, "bar", AnnotationStringArrayExpressionConverter.forStrings());
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		aea.setValue(new String[] {"string0"});
		this.assertSourceContains(annotation);
	}

	public void testValueEnumArray() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum[] bar();");
		this.createTestType("@annot.Foo(bar={enums.TestEnum.XXX, enums.TestEnum.YYY})");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new EnumArrayDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		assertTrue(Arrays.equals(new String[] {"enums.TestEnum.XXX", "enums.TestEnum.YYY"}, aea.value()));
	}

	public void testValueEnumArrayInvalidEntry() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum[] bar();");
		this.createTestType("@annot.Foo(bar={enums.TestEnum.XXX, 88})");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new EnumArrayDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		assertTrue(Arrays.equals(new String[] {"enums.TestEnum.XXX", null}, aea.value()));
	}

	public void testValueEnumArrayEmpty() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum[] bar();");
		this.createTestType("@annot.Foo(bar={})");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new EnumArrayDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		assertTrue(Arrays.equals(new String[0], aea.value()));
	}

	public void testValueEnumArraySingleElement() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum[] bar();");
		this.createTestType("@annot.Foo(bar=enums.TestEnum.XXX)");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new EnumArrayDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		assertTrue(Arrays.equals(new String[] {"enums.TestEnum.XXX"}, aea.value()));
	}

	public void testValueEnumArraySingleElementInvalid() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum[] bar();");
		this.createTestType("@annot.Foo(bar=\"\")");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new EnumArrayDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		assertTrue(Arrays.equals(new String[] {null}, aea.value()));
	}

	public void testValueNullEnumArray() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum[] bar();");
		this.createTestType("@annot.Foo()");
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new EnumArrayDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		assertTrue(Arrays.equals(new String[0], aea.value()));
	}

	public void testSetValueEnumArray() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum[] bar();");
		String annotation = "@Foo(bar={XXX,YYY})";
		this.createTestType();
		this.assertSourceDoesNotContain(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new EnumArrayDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		aea.setValue(new String[] {"enums.TestEnum.XXX", "enums.TestEnum.YYY"});
		this.assertSourceContains("import static enums.TestEnum.XXX;");
		this.assertSourceContains("import static enums.TestEnum.YYY;");
		this.assertSourceContains(annotation);
	}

	public void testSetValueEnumArrayEmptyRemove() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum[] bar();");
		String annotation = "@Foo";
		this.createTestType();
		this.assertSourceDoesNotContain(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new EnumArrayDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		aea.setValue(new String[0]);
		this.assertSourceDoesNotContain(annotation);
	}

	public void testSetValueEnumArrayEmpty() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum[] bar();");
		String annotation = "@Foo(bar={})";
		this.createTestType();
		this.assertSourceDoesNotContain(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new EnumArrayDeclarationAnnotationElementAdapter(daa, "bar", true, false);
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		aea.setValue(new String[0]);
		this.assertSourceContains(annotation);
	}

	public void testSetValueEnumArraySingleElement() throws Exception {
		this.createEnum("TestEnum", "XXX, YYY, ZZZ");
		this.createAnnotationAndMembers("Foo", "enums.TestEnum[] bar();");
		String annotation = "@Foo(bar=XXX)";
		this.createTestType();
		this.assertSourceDoesNotContain(annotation);
		DeclarationAnnotationAdapter daa = new SimpleDeclarationAnnotationAdapter("annot.Foo");
		DeclarationAnnotationElementAdapter<String[]> daea = new EnumArrayDeclarationAnnotationElementAdapter(daa, "bar");
		AnnotationElementAdapter<String[]> aea = new MemberAnnotationElementAdapter<String[]>(this.idField(), daea);
		aea.setValue(new String[] {"enums.TestEnum.XXX"});
		this.assertSourceContains("import static enums.TestEnum.XXX;");
		this.assertSourceContains(annotation);
	}

}
