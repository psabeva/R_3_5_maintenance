/*******************************************************************************
 * Copyright (c) 2012, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.ui.internal;

import org.eclipse.jpt.common.utility.internal.ObjectTools;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Convenience implementation of {@link IPartListener}.
 */
public class PartAdapter
	implements IPartListener
{
	public void partOpened(IWorkbenchPart part) {
		// NOP
	}

	public void partBroughtToTop(IWorkbenchPart part) {
		// NOP
	}

	public void partActivated(IWorkbenchPart part) {
		// NOP
	}

	public void partDeactivated(IWorkbenchPart part) {
		// NOP
	}

	public void partClosed(IWorkbenchPart part) {
		// NOP
	}

	@Override
	public String toString() {
		return ObjectTools.toString(this);
	}
}
