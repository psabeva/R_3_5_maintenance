/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.platform.generic;

import org.eclipse.jpt.core.internal.IJpaFactory;
import org.eclipse.jpt.core.internal.platform.BaseJpaPlatform;
import org.eclipse.jpt.utility.internal.CommandExecutor;
import org.eclipse.jpt.utility.internal.CommandExecutorProvider;


public class GenericJpaPlatform extends BaseJpaPlatform
{
	public static String ID = "generic";
	
	
	protected static final CommandExecutorProvider MODIFY_SHARED_DOCUMENT_COMMAND_EXECUTOR_PROVIDER =
		new CommandExecutorProvider() {
			public CommandExecutor commandExecutor() {
				return CommandExecutor.Default.instance();
			}
		};
	
	
	public GenericJpaPlatform() {
		super();
	}
	
	//TODO this is just for tests.  need to get this from the IJpaProjct instead
	public CommandExecutorProvider modifySharedDocumentCommandExecutorProvider() {
		return MODIFY_SHARED_DOCUMENT_COMMAND_EXECUTOR_PROVIDER;
	}
	
	
	// **************** Model construction / updating *************************
	
	public IJpaFactory jpaFactory() {
		return GenericJpaFactory.instance();
	}
}
