/*******************************************************************************
 * Copyright (c) 2011, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jaxb.core.xsd;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jpt.common.utility.internal.iterable.FilteringIterable;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.common.utility.internal.transformer.TransformerAdapter;
import org.eclipse.jpt.common.utility.transformer.Transformer;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDContentTypeCategory;
import org.eclipse.xsd.XSDElementDeclaration;


public class XsdComplexTypeDefinition
		extends XsdTypeDefinition<XSDComplexTypeDefinition> {
	
	XsdComplexTypeDefinition(XSDComplexTypeDefinition xsdComplexTypeDefinition) {
		super(xsdComplexTypeDefinition);
	}
	
	
	@Override
	public org.eclipse.jpt.jaxb.core.xsd.XsdTypeDefinition.Kind getKind() {
		return Kind.COMPLEX;
	}
	
	@Override
	public boolean hasTextContent() {
		return getXSDComponent().getContentTypeCategory() == XSDContentTypeCategory.SIMPLE_LITERAL;
	}
	
	@Override
	public XsdAttributeUse getAttribute(String namespace, String name) {
		for (XsdAttributeUse attrUse : getAttributeUses(namespace)) {
			if (attrUse.getXSDComponent().getAttributeDeclaration().getName().equals(name)) {
				return attrUse;
			}
		}
		return null;
	}
	
	@Override
	public Iterable<String> getAttributeNames(String namespace) {
		return IterableTools.transform(getAttributeUses(namespace), XSD_ATTRIBUTE_USE_TRANSFORMER);
	}

	protected static final Transformer<XsdAttributeUse, String> XSD_ATTRIBUTE_USE_TRANSFORMER = new XsdAttributeUseTransformer();
	public static class XsdAttributeUseTransformer
		extends TransformerAdapter<XsdAttributeUse, String>
	{
		@Override
		public String transform(XsdAttributeUse attrUse) {
			return attrUse.getXSDComponent().getAttributeDeclaration().getName();
		}
	}
	
	protected Iterable<XsdAttributeUse> getAttributeUses(final String namespace) {
		return IterableTools.transform(
				new FilteringIterable<XSDAttributeUse>(getXSDComponent().getAttributeUses()) {
					@Override
					protected boolean accept(XSDAttributeUse attrUse) {
						return XsdUtil.namespaceEquals(attrUse.getAttributeDeclaration(), namespace);
					}
				}, XsdUtil.<XsdAttributeUse>adapterTransformer());
	}
	
	@Override
	public XsdElementDeclaration getElement(String namespace, String name, boolean recurseChildren) {
		for (XsdElementDeclaration element : getElementDeclarations(namespace, recurseChildren)) {
			if (element.getXSDComponent().getName().equals(name)) {
				return element;
			}
		}
		return null;
	}
	
	@Override
	public Iterable<String> getElementNames(String namespace, boolean recurseChildren) {
		return IterableTools.transform(getElementDeclarations(namespace, recurseChildren), XSD_ELEMENT_DECLARATION_TRANSFORMER);
	}

	protected static final Transformer<XsdElementDeclaration, String> XSD_ELEMENT_DECLARATION_TRANSFORMER = new XsdElementDeclarationTransformer();
	public static class XsdElementDeclarationTransformer
		extends TransformerAdapter<XsdElementDeclaration, String>
	{
		@Override
		public String transform(XsdElementDeclaration element) {
			return element.getXSDComponent().getName();
		}
	}
	
	protected Iterable<XsdElementDeclaration> getElementDeclarations(final String namespace, boolean recurseChildren) {
		return IterableTools.transform(
				new FilteringIterable<XSDElementDeclaration>(getXSDElementDeclarations(recurseChildren)) {
					@Override
					protected boolean accept(XSDElementDeclaration element) {
						return XsdUtil.namespaceEquals(element, namespace);
					}
				}, XsdUtil.<XsdElementDeclaration>adapterTransformer());
	}
	
	protected Iterable<XSDElementDeclaration> getXSDElementDeclarations(boolean recurseChildren) {
		ElementFinder elementFinder = new ElementFinder(recurseChildren);
		elementFinder.visitNode(getXSDComponent());
		return elementFinder.getElements();
	}
	
	
	private class ElementFinder
			extends XSDNodeVisitor {
		
		private boolean recurseChildren;
		
		private List<XSDElementDeclaration> elements = new ArrayList<XSDElementDeclaration>();
		
		
		private ElementFinder(boolean recurseChildren) {
			this.recurseChildren = recurseChildren;
		}
		
		
		@Override
		protected boolean visitChildren() {
			return super.visitChildren() || this.recurseChildren;
		}
		
		@Override
		public void visitXSDElementDeclaration(XSDElementDeclaration node) {
			boolean cachedVisitChildren = this.visitChildren;
			this.visitChildren = false;
			super.visitXSDElementDeclaration(node);
			if (! this.elements.contains(node)) {
				elements.add(node);
			}
			this.visitChildren = cachedVisitChildren;
		}
		
		public Iterable<XSDElementDeclaration> getElements() {
			return this.elements;
		}
	}
}
