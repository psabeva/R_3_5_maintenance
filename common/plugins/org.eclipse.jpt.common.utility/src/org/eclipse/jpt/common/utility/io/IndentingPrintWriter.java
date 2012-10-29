/*******************************************************************************
 * Copyright (c) 2005, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.io;

import java.io.Writer;
import org.eclipse.jpt.common.utility.internal.StringTools;

/**
 * Extend {@link JptPrintWriter} to automatically indent new lines.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public class IndentingPrintWriter
	extends JptPrintWriter
{
	private final String indent;
	private int indentLevel;
	private boolean needsIndent;

	public static String DEFAULT_INDENT = "\t"; //$NON-NLS-1$


	/**
	 * Construct a writer that indents with tabs.
	 */
	public IndentingPrintWriter(Writer out) {
		this(out, DEFAULT_INDENT, StringTools.CR);
	}

	/**
	 * Construct a writer that indents with the specified string.
	 */
	public IndentingPrintWriter(Writer out, String indent) {
		this(out, indent, 0, StringTools.CR);
	}
	
	public IndentingPrintWriter(Writer out, String indent, String lineSeparator) {
		this(out, indent, 0, lineSeparator);
	}

	/**
	 * Construct a writer that indents with the specified string
	 * and begins with the specified indent level.
	 */
	public IndentingPrintWriter(Writer out, String indent, int initialIndentLevel) {
		this(out, indent, initialIndentLevel, StringTools.CR);
	}
	
	public IndentingPrintWriter(Writer out, String indent, int initialIndentLevel, String lineSeparator) {
		super(out, lineSeparator);
		if (indent == null) {
			throw new NullPointerException();
		}
		this.indent = indent;
		this.indentLevel = initialIndentLevel;
		this.needsIndent = true;
	}

	/**
	 * Set flag so following line is indented.
	 */
	@Override
	public void println() {
		synchronized (this.lock) {
			super.println();
			this.needsIndent = true;
		}
	}

	/**
	 * Print the appropriate indent.
	 * Pre-condition: synchronized
	 */
	private void printIndent() {
		if (this.needsIndent) {
			this.needsIndent = false;
			for (int i = this.indentLevel; i-- > 0; ) {
				this.print(this.indent);
			}
		}
	}

	/**
	 * Write a portion of an array of characters.
	 */
	@Override
	public void write(char buf[], int off, int len) {
		synchronized (this.lock) {
			this.printIndent();
			super.write(buf, off, len);
		}
	}

	/**
	 * Write a single character.
	 */
	@Override
	public void write(int c) {
		synchronized (this.lock) {
			this.printIndent();
			super.write(c);
		}
	}

	/**
	 * Write a portion of a string.
	 */
	@Override
	public void write(String s, int off, int len) {
		synchronized (this.lock) {
			this.printIndent();
			super.write(s, off, len);
		}
	}

	/**
	 * Bump the indent level.
	 */
	public void indent() {
		this.incrementIndentLevel();
	}

	/**
	 * Decrement the indent level.
	 */
	public void undent() {
		this.decrementIndentLevel();
	}

	/**
	 * Bump the indent level.
	 */
	public void incrementIndentLevel() {
		synchronized (this.lock) {
			this.indentLevel++;
		}
	}

	/**
	 * Decrement the indent level.
	 */
	public void decrementIndentLevel() {
		synchronized (this.lock) {
			this.indentLevel--;
		}
	}

	/**
	 * Return the current indent level.
	 */
	public int getIndentLevel() {
		synchronized (this.lock) {
			return this.indentLevel;
		}
	}

	/**
	 * Allow the indent level to be set directly.
	 * Return the previous indent level.
	 */
	public int setIndentLevel(int indentLevel) {
		synchronized (this.lock) {
			int old = this.indentLevel;
			this.indentLevel = indentLevel;
			return old;
		}
	}

}
