/*******************************************************************************
 * Copyright (c) 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.tests.internal.jdbc;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JptCommonUtilityJDBCTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(JptCommonUtilityJDBCTests.class.getPackage().getName());

		suite.addTestSuite(JDBCTypeTests.class);

		return suite;
	}

	private JptCommonUtilityJDBCTests() {
		super();
		throw new UnsupportedOperationException();
	}
}
