/*******************************************************************************
 * Copyright (c) 2013, 2015 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal.predicate.int_;

import org.eclipse.jpt.common.utility.internal.BitTools;

/**
 * This predicate evaluates to <code>true</code> if the variable
 * has cleared the flag specified during construction.
 */
public class FlagIsOff
	extends IntCriterionIntPredicate
{
	/**
	 * Construct a predicate that will evaluate to <code>true</code> if the
	 * variable has cleared the specified flag.
	 */
	public FlagIsOff(int flag) {
		super(flag);
	}

	public boolean evaluate(int variable) {
		return BitTools.flagIsOff(variable, this.criterion);
	}
}
