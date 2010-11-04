/*******************************************************************************
 * Copyright (c) 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jaxb.core.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jpt.core.JpaResourceModel;
import org.eclipse.jpt.jaxb.core.JaxbFactory;
import org.eclipse.jpt.jaxb.core.JaxbFile;
import org.eclipse.jpt.jaxb.core.JaxbProject;
import org.eclipse.jpt.jaxb.core.JaxbProject.Config;
import org.eclipse.jpt.jaxb.core.context.JaxbPackage;
import org.eclipse.jpt.jaxb.core.context.JaxbPackageInfo;
import org.eclipse.jpt.jaxb.core.context.JaxbRootContextNode;
import org.eclipse.jpt.jaxb.core.context.XmlNs;
import org.eclipse.jpt.jaxb.core.context.XmlSchema;
import org.eclipse.jpt.jaxb.core.context.XmlSchemaType;
import org.eclipse.jpt.jaxb.core.internal.context.GenericPackage;
import org.eclipse.jpt.jaxb.core.internal.context.GenericRootContextNode;
import org.eclipse.jpt.jaxb.core.internal.context.java.GenericJavaPackageInfo;
import org.eclipse.jpt.jaxb.core.internal.context.java.GenericJavaXmlNs;
import org.eclipse.jpt.jaxb.core.internal.context.java.GenericJavaXmlSchema;
import org.eclipse.jpt.jaxb.core.internal.context.java.GenericJavaXmlSchemaType;
import org.eclipse.jpt.jaxb.core.resource.java.JavaResourcePackage;
import org.eclipse.jpt.jaxb.core.resource.java.XmlNsAnnotation;
import org.eclipse.jpt.jaxb.core.resource.java.XmlSchemaTypeAnnotation;

/**
 * Central class that allows extenders to easily replace implementations of
 * various Dali interfaces.
 */
public abstract class AbstractJaxbFactory
	implements JaxbFactory
{
	protected AbstractJaxbFactory() {
		super();
	}
	
	
	// ********** Core Model **********
	
	public JaxbProject buildJaxbProject(Config config) {
		return new GenericJaxbProject(config);
	}

	public JaxbFile buildJaxbFile(JaxbProject jaxbProject, IFile file, IContentType contentType, JpaResourceModel resourceModel) {
		return new GenericJaxbFile(jaxbProject, file, contentType, resourceModel);
	}

	// ********** Context Nodes **********

	public JaxbRootContextNode buildRootContextNode(JaxbProject parent) {
		return new GenericRootContextNode(parent);
	}

	public JaxbPackage buildPackage(JaxbRootContextNode parent, JavaResourcePackage resourcePackage) {
		return new GenericPackage(parent, resourcePackage);
	}

	// ********** Java Context Nodes **********

	public JaxbPackageInfo buildJavaPackageInfo(JaxbPackage parent, JavaResourcePackage resourcePackage) {
		return new GenericJavaPackageInfo(parent, resourcePackage);
	}

	public XmlSchema buildJavaXmlSchema(JaxbPackageInfo parent) {
		return new GenericJavaXmlSchema(parent);
	}

	public XmlSchemaType buildJavaXmlSchemaType(JaxbPackageInfo parent, XmlSchemaTypeAnnotation xmlSchemaTypeAnnotation) {
		return new GenericJavaXmlSchemaType(parent, xmlSchemaTypeAnnotation);
	}

	public XmlNs buildJavaXmlNs(XmlSchema parent, XmlNsAnnotation xmlNsAnnotation) {
		return new GenericJavaXmlNs(parent, xmlNsAnnotation);
	}
}
