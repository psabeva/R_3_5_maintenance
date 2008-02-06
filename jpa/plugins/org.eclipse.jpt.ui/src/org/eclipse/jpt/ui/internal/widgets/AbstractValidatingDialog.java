/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.widgets;

import org.eclipse.jpt.utility.internal.node.Node;
import org.eclipse.jpt.utility.internal.node.Problem;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * This dialog is similar to it superclass, <code>AbstractDialog</code>, with
 * the added value of an error message label below the main panel. A subclass
 * can set this error message as needed so that it can inform the user something
 * incorrect has been entered.
 * <p>
 * If there is an error message, it will be shown. If there is a warning
 * message, it will only be shown if there is no error message. Warning messages
 * have a different icon than error messages.
 *
 * @version 2.0
 * @since 2.0
 */
public abstract class AbstractValidatingDialog<T extends Node> extends AbstractDialog<T> {

	/**
	 * Creates a new <code>AbstractValidatingDialog</code>.
	 *
	 * @param parent The parent shell
	 */
	public AbstractValidatingDialog(Shell parent) {
		super(parent);
	}

	/**
	 * Creates a new <code>AbstractValidatingDialog</code>.
	 *
	 * @param parent The parent shell
	 * @param title The dialog's title
	 */
	public AbstractValidatingDialog(Shell parent, String title) {
		super(parent, title);
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	final Node.Validator buildValidator() {
		return new Node.Validator() {
			public void pause() {
			}

			public void resume() {
			}

			public void validate() {
				AbstractValidatingDialog.this.validate();
			}
		};
	}

	/**
	 * Clears the error message from the description pane.
	 */
	protected final void clearErrorMessage() {
		setErrorMessage(null);
	}

	/**
	 * Returns the description shown in the description pane.
	 *
	 * @return The description under the description's title
	 */
	protected String description() {
		return null;
	}

	/**
	 * Returns the image shown in the description pane.
	 *
	 * @return The image of the description pane or <code>null</code> if none is
	 * required
	 */
	protected Image descriptionImage() {
		return null;
	}

	/**
	 * Returns the title of the description pane.
	 *
	 * @return The title shown in the description pane
	 */
	protected String descriptionTitle() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected final boolean hasTitleArea() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void initializeUI() {

		super.initializeUI();

		// Update the description title
		String descriptionTitle = descriptionTitle();

		if (descriptionTitle != null) {
			setTitle(descriptionTitle);
		}

		// Update the description title
		String description = description();

		if (description != null) {
			setMessage(description);
		}

		// Update the description image
		Image image = descriptionImage();

		if (image != null) {
			setTitleImage(image);
		}
	}

	/**
	 * Updates the description pane by showing the given error message and format
	 * the text with the given list of arguments if any exists.
	 *
	 * @param errorMessage The error message to show in the description pane
	 * @param arguments The list of arguments used to format the error message
	 */
	protected final void setErrorMessage(String errorMessage, Object... arguments) {
		setErrorMessage(NLS.bind(errorMessage, arguments));
	}

	/**
	 * Updates the error message, either shows the first error problem or hides
	 * the error pane. If the progress bar is shown, then the error message will
	 * not be shown.
	 */
	private void updateErrorMessage() {
		if (subject().hasBranchProblems()) {
			Problem problem = subject().branchProblems().next();
			setErrorMessage(problem.messageKey(), problem.messageArguments());
		}
		// TODO: It would be nice to add warnings to the model
//		else if (this.subject().hasBranchWarnings()) {
//			Problem problem = this.subject().branchWarnings().next();
//			this.setWarningMessageKey(problem.getMessageKey(), problem.getMessageArguments());
//		}
		else {
			clearErrorMessage();
		}
	}

	/**
	 * Validates the state object and based on its status, update the description
	 * pane to show the first error if any exists and update the enablement of
	 * the OK button.
	 */
	private void validate() {
		subject().validateBranch();
		updateErrorMessage();
		getButton(OK).setEnabled(!subject().hasBranchProblems());
	}
}