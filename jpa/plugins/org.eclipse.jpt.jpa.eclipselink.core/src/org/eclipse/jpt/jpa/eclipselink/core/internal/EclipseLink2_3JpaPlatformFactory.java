/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal;

import org.eclipse.jpt.common.core.AnnotationProvider;
import org.eclipse.jpt.common.core.JptResourceType;
import org.eclipse.jpt.jpa.core.JpaPlatform;
import org.eclipse.jpt.jpa.core.JpaPlatformFactory;
import org.eclipse.jpt.jpa.core.JpaPlatformVariation;
import org.eclipse.jpt.jpa.core.context.AccessType;
import org.eclipse.jpt.jpa.core.internal.GenericJpaPlatform;
import org.eclipse.jpt.jpa.core.internal.JpaAnnotationProvider;
import org.eclipse.jpt.jpa.core.internal.jpa2.Generic2_0JpaAnnotationDefinitionProvider;
import org.eclipse.jpt.jpa.core.jpa2.JpaProject2_0;
import org.eclipse.jpt.jpa.eclipselink.core.internal.EclipseLinkJpaPlatformFactory.EclipseLinkJpaPlatformVersion;
import org.eclipse.jpt.jpa.eclipselink.core.internal.context.orm.EclipseLinkOrmXml2_1Definition;
import org.eclipse.persistence.jpa.jpql.parser.EclipseLinkJPQLGrammar2_3;

public class EclipseLink2_3JpaPlatformFactory
	implements JpaPlatformFactory
{
	/**
	 * Version string for EclipseLink platform version 2.3
	 */
	public static final String VERSION = "2.3";  //$NON-NLS-1$

	/**
	 * zero-argument constructor
	 */
	public EclipseLink2_3JpaPlatformFactory() {
		super();
	}

	public JpaPlatform buildJpaPlatform(String id) {
		return new GenericJpaPlatform(
			id,
			buildJpaVersion(),
			new EclipseLink2_0JpaFactory(),
			buildAnnotationProvider(),
			EclipseLink2_3JpaPlatformProvider.instance(),
			buildJpaVariation(),
			EclipseLinkJPQLGrammar2_3.instance());
	}

	protected JpaPlatform.Version buildJpaVersion() {
		return new EclipseLinkJpaPlatformVersion(VERSION, JpaProject2_0.FACET_VERSION_STRING);
	}

	protected AnnotationProvider buildAnnotationProvider() {
		return new JpaAnnotationProvider(
				Generic2_0JpaAnnotationDefinitionProvider.instance(),
				EclipseLink2_3JpaAnnotationDefinitionProvider.instance());
	}

	protected JpaPlatformVariation buildJpaVariation() {
		return new JpaPlatformVariation() {
			public Supported getTablePerConcreteClassInheritanceIsSupported() {
				return Supported.YES;
			}
			public boolean isJoinTableOverridable() {
				return true;
			}
			public AccessType[] getSupportedAccessTypes(JptResourceType resourceType) {
				return resourceType.isKindOf(EclipseLinkOrmXml2_1Definition.instance().getResourceType()) ?
						EclipseLink2_1JpaPlatformFactory.SUPPORTED_ACCESS_TYPES :
						GENERIC_SUPPORTED_ACCESS_TYPES;
			}
		};
	}
}