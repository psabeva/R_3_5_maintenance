/*******************************************************************************
 * Copyright (c) 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal.command;

import org.eclipse.jpt.common.utility.command.CommandExecutor;
import org.eclipse.jpt.common.utility.command.StatefulCommandExecutor;

/**
 * @see AbstractQueueingCommandExecutor
 */
public class QueueingCommandExecutor
	extends AbstractQueueingCommandExecutor<StatefulCommandExecutor>
{
	public QueueingCommandExecutor() {
		this(CommandExecutor.Default.instance());
	}

	public QueueingCommandExecutor(CommandExecutor commandExecutor) {
		this(new SimpleStatefulCommandExecutor(commandExecutor));
	}

	public QueueingCommandExecutor(StatefulCommandExecutor commandExecutor) {
		super(commandExecutor);
	}
}