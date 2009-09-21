/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa1.context.orm;

import org.eclipse.jpt.core.context.XmlContextNode;
import org.eclipse.jpt.core.internal.context.orm.AbstractOrmSequenceGenerator;
import org.eclipse.jpt.core.resource.orm.XmlSequenceGenerator;

/**
 * 
 */
public class GenericOrmSequenceGenerator
	extends AbstractOrmSequenceGenerator
{

	public GenericOrmSequenceGenerator(XmlContextNode parent, XmlSequenceGenerator resourceSequenceGenerator) {
		super(parent, resourceSequenceGenerator);
	}

	// ********** database stuff **********

	/**
	 * The JPA spec does not allow a sequence to have a schema.
	 */
	@Override
	protected String getSchema() {
		return this.getContextDefaultSchema();
	}

	/**
	 * The JPA spec does not allow a sequence to have a catalog.
	 */
	@Override
	protected String getCatalog() {
		return this.getContextDefaultCatalog();
	}

}
