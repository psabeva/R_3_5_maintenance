/*******************************************************************************
 * Copyright (c) 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.jpql;

import org.eclipse.jpt.jpa.core.context.NamedQuery;
import org.eclipse.persistence.jpa.jpql.ContentAssistProposals;
import org.eclipse.persistence.jpa.jpql.ResultQuery;
import org.eclipse.swt.graphics.Image;

/**
 * @version 3.3
 * @since 3.3
 * @author Pascal Filion
 */
final class JpqlJavaCompletionProposal extends JpqlCompletionProposal {

	JpqlJavaCompletionProposal(ContentAssistProposals proposals,
	                           String proposal,
	                           String displayString,
	                           String additionalInfo,
	                           Image image,
	                           NamedQuery namedQuery,
	                           String jpqlQuery,
	                           int tokenStart,
	                           int tokenEnd,
	                           int position,
	                           int cursorOffset) {

		super(proposals,
		      proposal,
		      displayString,
		      additionalInfo,
		      image,
		      namedQuery,
		      jpqlQuery,
		      tokenStart,
		      tokenEnd,
		      position,
		      cursorOffset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	ResultQuery buildResult() {
		return proposals.buildEscapedQuery(
			jpqlQuery,
			proposal,
			position,
			isCompletionInserts() ^ toggleCompletion
		);
	}
}