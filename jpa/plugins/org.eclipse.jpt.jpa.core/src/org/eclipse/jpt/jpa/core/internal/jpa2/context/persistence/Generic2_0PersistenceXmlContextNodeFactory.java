/*******************************************************************************
 * Copyright (c) 2009, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa2.context.persistence;

import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnitProperties;
import org.eclipse.jpt.jpa.core.internal.context.persistence.AbstractPersistenceXmlContextNodeFactory;
import org.eclipse.jpt.jpa.core.internal.jpa2.context.persistence.connection.GenericConnection2_0;
import org.eclipse.jpt.jpa.core.internal.jpa2.context.persistence.options.GenericOptions2_0;
import org.eclipse.jpt.jpa.core.internal.jpa2_1.context.persistence.schemagen.NullGenericSchemaGeneration2_1;
import org.eclipse.jpt.jpa.core.jpa2.context.persistence.connection.JpaConnection2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.persistence.options.JpaOptions2_0;


public class Generic2_0PersistenceXmlContextNodeFactory extends AbstractPersistenceXmlContextNodeFactory
{
		
	public JpaConnection2_0 buildConnection(PersistenceUnit parent) {
		return new GenericConnection2_0(parent);
	}
	
	public JpaOptions2_0 buildOptions(PersistenceUnit parent) {
		return new GenericOptions2_0(parent);
	}
	
	public PersistenceUnitProperties buildSchemaGeneration(PersistenceUnit parent) {
		return new NullGenericSchemaGeneration2_1(parent);
	}

}
