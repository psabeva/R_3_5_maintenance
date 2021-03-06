/*******************************************************************************
 * Copyright (c) 2007, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.tests.internal.resource;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.eclipse.jpt.jpa.eclipselink.core.tests.internal.JptJpaEclipseLinkCoreTests;
import org.eclipse.jpt.jpa.eclipselink.core.tests.internal.resource.java.JptEclipseLinkCoreJavaResourceModelTests;

/**
 * Required Java system properties:<ul>
 * <li>-Dorg.eclipse.jpt.jpa.jar=<jpa.jar path>
 * <li>-Dorg.eclipse.jpt.eclipselink.jar=<eclipselink.jar path>
 * <ul>
 */
public class JptJpaEclipseLinkCoreResourceModelTests
	extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite(JptJpaEclipseLinkCoreResourceModelTests.class.getName());
		if(JptJpaEclipseLinkCoreTests.requiredJarsExists()) {
			suite.addTest(JptEclipseLinkCoreJavaResourceModelTests.suite());
		}
		else {
			suite.addTest(TestSuite.warning(JptJpaEclipseLinkCoreTests.buildMissingJarErrorMessage()));
		}
		return suite;
	}

	private JptJpaEclipseLinkCoreResourceModelTests() {
		super();
		throw new UnsupportedOperationException();
	}
}
