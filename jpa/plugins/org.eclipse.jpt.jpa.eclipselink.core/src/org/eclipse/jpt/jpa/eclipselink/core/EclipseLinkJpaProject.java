/*******************************************************************************
 * Copyright (c) 2009, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core;

import org.eclipse.jpt.common.core.resource.xml.JptXmlResource;
import org.eclipse.jpt.jpa.core.JpaProject;

/**
 * EclipseLink JPA project.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface EclipseLinkJpaProject
	extends JpaProject
{

	/**
	 * Return the resource model object that corresponds to the file
	 * <code>META-INF/eclipselink-orm.xml</code> if that file has the
	 * EclipseLink content type.
	 * 
	 * @see org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlEntityMappings#DEFAULT_RUNTIME_PATH
	 * @see org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlEntityMappings#CONTENT_TYPE
	 */
	JptXmlResource getDefaultEclipseLinkOrmXmlResource();

}
