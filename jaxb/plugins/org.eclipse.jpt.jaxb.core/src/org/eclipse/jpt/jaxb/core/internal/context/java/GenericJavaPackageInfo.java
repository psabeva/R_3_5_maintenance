/*******************************************************************************
 * Copyright (c) 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jaxb.core.internal.context.java;

import org.eclipse.jpt.jaxb.core.context.JaxbPackage;
import org.eclipse.jpt.jaxb.core.context.JaxbPackageInfo;
import org.eclipse.jpt.jaxb.core.context.XmlAccessOrder;
import org.eclipse.jpt.jaxb.core.context.XmlAccessType;
import org.eclipse.jpt.jaxb.core.context.XmlSchema;
import org.eclipse.jpt.jaxb.core.context.XmlSchemaType;
import org.eclipse.jpt.jaxb.core.internal.context.AbstractJaxbContextNode;
import org.eclipse.jpt.jaxb.core.resource.java.JavaResourcePackage;
import org.eclipse.jpt.jaxb.core.resource.java.XmlAccessorOrderAnnotation;
import org.eclipse.jpt.jaxb.core.resource.java.XmlAccessorTypeAnnotation;
import org.eclipse.jpt.jaxb.core.resource.java.XmlSchemaTypeAnnotation;
import org.eclipse.jpt.jaxb.core.resource.java.XmlSchemaTypesAnnotation;
import org.eclipse.jpt.utility.internal.iterables.ListIterable;

public class GenericJavaPackageInfo
	extends AbstractJaxbContextNode
	implements JaxbPackageInfo
{

	protected final JavaResourcePackage resourcePackage;

	protected final XmlSchema xmlSchema;

	protected XmlAccessType specifiedAccessType;

	protected XmlAccessOrder specifiedAccessOrder;

	protected final XmlSchemaTypeContainer xmlSchemaTypeContainer;

	public GenericJavaPackageInfo(JaxbPackage parent, JavaResourcePackage resourcePackage) {
		super(parent);
		this.resourcePackage = resourcePackage;
		this.xmlSchema = getFactory().buildJavaXmlSchema(this);
		this.specifiedAccessType = getResourceAccessType();
		this.specifiedAccessOrder = getResourceAccessOrder();
		this.xmlSchemaTypeContainer = new XmlSchemaTypeContainer();
	}


	// ********** synchronize/update **********

	public void synchronizeWithResourceModel() {
		this.xmlSchema.synchronizeWithResourceModel();
		this.setSpecifiedAccessType_(this.getResourceAccessType());
		this.setSpecifiedAccessOrder_(this.getResourceAccessOrder());
		this.syncXmlSchemaTypes();
	}

	public void update() {
		this.xmlSchema.update();
	}


	// ********** JaxbPackageInfo implementation **********

	public JavaResourcePackage getResourcePackage() {
		return this.resourcePackage;
	}

	// ********** xml schema **********

	public XmlSchema getXmlSchema() {
		return this.xmlSchema;
	}

	// ********** access type **********

	public XmlAccessType getAccessType() {
		return (this.specifiedAccessType != null) ? this.specifiedAccessType : this.getDefaultAccessType();
	}

	public XmlAccessType getSpecifiedAccessType() {
		return this.specifiedAccessType;
	}
	
	public void setSpecifiedAccessType(XmlAccessType access) {
		this.getAccessorTypeAnnotation().setValue(XmlAccessType.toJavaResourceModel(access));
		this.setSpecifiedAccessType_(access);
	}

	protected void setSpecifiedAccessType_(XmlAccessType access) {
		XmlAccessType old = this.specifiedAccessType;
		this.specifiedAccessType = access;
		this.firePropertyChanged(SPECIFIED_ACCESS_TYPE_PROPERTY, old, access);
	}

	public XmlAccessType getDefaultAccessType() {
		return XmlAccessType.PUBLIC_MEMBER;
	}
	
	protected XmlAccessType getResourceAccessType() {
		return XmlAccessType.fromJavaResourceModel(this.getAccessorTypeAnnotation().getValue());
	}

	protected XmlAccessorTypeAnnotation getAccessorTypeAnnotation() {
		return (XmlAccessorTypeAnnotation) this.resourcePackage.getNonNullAnnotation(XmlAccessorTypeAnnotation.ANNOTATION_NAME);
	}


	// ********** access order **********

	public XmlAccessOrder getAccessOrder() {
		return (this.specifiedAccessOrder != null) ? this.specifiedAccessOrder : this.getDefaultAccessOrder();
	}

	public XmlAccessOrder getSpecifiedAccessOrder() {
		return this.specifiedAccessOrder;
	}
	
	public void setSpecifiedAccessOrder(XmlAccessOrder accessOrder) {
		this.getAccessorOrderAnnotation().setValue(XmlAccessOrder.toJavaResourceModel(accessOrder));
		this.setSpecifiedAccessOrder_(accessOrder);
	}

	protected void setSpecifiedAccessOrder_(XmlAccessOrder accessOrder) {
		XmlAccessOrder old = this.specifiedAccessOrder;
		this.specifiedAccessOrder = accessOrder;
		this.firePropertyChanged(SPECIFIED_ACCESS_ORDER_PROPERTY, old, accessOrder);
	}

	public XmlAccessOrder getDefaultAccessOrder() {
		return XmlAccessOrder.UNDEFINED;
	}
	
	protected XmlAccessOrder getResourceAccessOrder() {
		return XmlAccessOrder.fromJavaResourceModel(this.getAccessorOrderAnnotation().getValue());
	}

	protected XmlAccessorOrderAnnotation getAccessorOrderAnnotation() {
		return (XmlAccessorOrderAnnotation) this.resourcePackage.getNonNullAnnotation(XmlAccessorOrderAnnotation.ANNOTATION_NAME);
	}


	// ********** xml schema types **********

	public ListIterable<XmlSchemaType> getXmlSchemaTypes() {
		return this.xmlSchemaTypeContainer.getContextElements();
	}

	public int getXmlSchemaTypesSize() {
		return this.xmlSchemaTypeContainer.getContextElementsSize();
	}

	public XmlSchemaType addXmlSchemaType(int index) {
		XmlSchemaTypeAnnotation annotation = (XmlSchemaTypeAnnotation) this.resourcePackage.addAnnotation(index, XmlSchemaTypeAnnotation.ANNOTATION_NAME, XmlSchemaTypesAnnotation.ANNOTATION_NAME);
		return this.xmlSchemaTypeContainer.addContextElement(index, annotation);
	}

	public void removeXmlSchemaType(XmlSchemaType xmlSchemaType) {
		this.removeXmlSchemaType(this.xmlSchemaTypeContainer.indexOfContextElement(xmlSchemaType));
	}

	public void removeXmlSchemaType(int index) {
		this.resourcePackage.removeAnnotation(index, XmlSchemaTypeAnnotation.ANNOTATION_NAME, XmlSchemaTypesAnnotation.ANNOTATION_NAME);
		this.xmlSchemaTypeContainer.removeContextElement(index);
	}

	public void moveXmlSchemaType(int targetIndex, int sourceIndex) {
		this.resourcePackage.moveAnnotation(targetIndex, sourceIndex, XmlSchemaTypesAnnotation.ANNOTATION_NAME);
		this.xmlSchemaTypeContainer.moveContextElement(targetIndex, sourceIndex);
	}

	protected XmlSchemaType buildXmlSchemaType(XmlSchemaTypeAnnotation xmlSchemaTypeAnnotation) {
		return this.getFactory().buildJavaXmlSchemaType(this, xmlSchemaTypeAnnotation);
	}

	protected void syncXmlSchemaTypes() {
		this.xmlSchemaTypeContainer.synchronizeWithResourceModel();
	}

	@SuppressWarnings("unchecked")
	protected Iterable<XmlSchemaTypeAnnotation> getXmlSchemaTypeAnnotations() {
		return (Iterable<XmlSchemaTypeAnnotation>) this.resourcePackage.getAnnotations(XmlSchemaTypeAnnotation.ANNOTATION_NAME, XmlSchemaTypesAnnotation.ANNOTATION_NAME);
	}


	/**
	 * xml schema type container
	 */
	protected class XmlSchemaTypeContainer
		extends ListContainer<XmlSchemaType, XmlSchemaTypeAnnotation>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return XML_SCHEMA_TYPES_LIST;
		}
		@Override
		protected XmlSchemaType buildContextElement(XmlSchemaTypeAnnotation resourceElement) {
			return GenericJavaPackageInfo.this.buildXmlSchemaType(resourceElement);
		}
		@Override
		protected Iterable<XmlSchemaTypeAnnotation> getResourceElements() {
			return GenericJavaPackageInfo.this.getXmlSchemaTypeAnnotations();
		}
		@Override
		protected XmlSchemaTypeAnnotation getResourceElement(XmlSchemaType contextElement) {
			return contextElement.getResourceXmlSchemaType();
		}
	}
}
