/*******************************************************************************
 * Copyright (c) 2012, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.tests.internal.comparator;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JptCommonUtilityComparatorTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(JptCommonUtilityComparatorTests.class.getPackage().getName());

		suite.addTestSuite(ReverseComparatorTests.class);
		suite.addTestSuite(VersionComparatorTests.class);

		return suite;
	}

	private JptCommonUtilityComparatorTests() {
		super();
		throw new UnsupportedOperationException();
	}
}
