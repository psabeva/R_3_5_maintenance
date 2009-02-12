/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.context.orm;

import org.eclipse.jpt.core.context.java.JavaBasicMapping;
import org.eclipse.jpt.core.context.orm.OrmTypeMapping;
import org.eclipse.jpt.core.resource.orm.AccessType;
import org.eclipse.jpt.core.resource.orm.XmlGeneratedValue;
import org.eclipse.jpt.core.resource.orm.XmlSequenceGenerator;
import org.eclipse.jpt.core.resource.orm.XmlTableGenerator;

/**
 * VirtualBasic is an implementation of Basic used when there is 
 * no tag in the orm.xml and an underlying javaBasicMapping exists.
 */
public class EclipseLink1_1VirtualXmlBasic extends EclipseLinkVirtualXmlBasic implements org.eclipse.jpt.eclipselink1_1.core.resource.orm.XmlBasic
{
		
	public EclipseLink1_1VirtualXmlBasic(OrmTypeMapping ormTypeMapping, JavaBasicMapping javaBasicMapping) {
		super(ormTypeMapping, javaBasicMapping);
	}
	
	public AccessType getAccess() {
		return org.eclipse.jpt.core.context.AccessType.toOrmResourceModel(this.javaAttributeMapping.getPersistentAttribute().getAccess());
	}
	
	public void setAccess(AccessType value) {
		throw new UnsupportedOperationException("cannot set values on a virtual mapping"); //$NON-NLS-1$
	}
	
	public XmlTableGenerator getTableGenerator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setTableGenerator(XmlTableGenerator value) {
		// TODO Auto-generated method stub
		
	}
	
	public XmlSequenceGenerator getSequenceGenerator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setSequenceGenerator(XmlSequenceGenerator value) {
		// TODO Auto-generated method stub
		
	}
	
	public XmlGeneratedValue getGeneratedValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setGeneratedValue(XmlGeneratedValue value) {
		// TODO Auto-generated method stub
		
	}
	
}
