/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa1.context.persistence;

import org.eclipse.jpt.core.context.persistence.PersistenceXml;
import org.eclipse.jpt.core.internal.context.persistence.AbstractPersistence;
import org.eclipse.jpt.core.resource.persistence.XmlPersistence;

public class GenericPersistence
	extends AbstractPersistence
{	
	public GenericPersistence(PersistenceXml parent, XmlPersistence xmlPersistence) {
		super(parent, xmlPersistence);
	}

}
