/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.ui.internal.util;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * A suite of utility methods related to the user interface.
 *
 * @version 2.0
 * @since 1.0
 */
public class SWTUtil {

	/**
	 * @see Display#asyncExec(Runnable)
	 * @see #syncExec(Runnable)
	 * @see #timerExec(int, Runnable)
	 * @see #execute(Runnable)
	 */
	public static void asyncExec(Runnable runnable) {
		getStandardDisplay().asyncExec(runnable);
	}

	/**
	 * @see Display#syncExec(Runnable)
	 * @see #asyncExec(Runnable)
	 * @see #timerExec(int, Runnable)
	 * @see #execute(Runnable)
	 */
	public static void syncExec(Runnable runnable) {
		getStandardDisplay().syncExec(runnable);
	}

	/**
	 * Use the standard delay.
	 * @see OpenStrategy#getPostSelectionDelay()
	 * @see #timerExec(int, Runnable)
	 */
	public static void delayedExec(Runnable runnable) {
		timerExec(OpenStrategy.getPostSelectionDelay(), runnable);
	}

	/**
	 * @see Display#timerExec(int, Runnable)
	 * @see #asyncExec(Runnable)
	 * @see #syncExec(Runnable)
	 * @see #execute(Runnable)
	 */
	public static void timerExec(int milliseconds, Runnable runnable) {
		getStandardDisplay().timerExec(milliseconds, runnable);
	}

	/**
	 * Execute the specified runnable if the current thread is the UI thread;
	 * otherwise asynchrounously dispatch the runnable to the UI thread,
	 * returning immediately. This is useful for event handlers when it is not
	 * obviously whether the events are fired on the UI thread.
	 * 
	 * @see Display#asyncExec(Runnable)
	 * @see #asyncExec(Runnable)
	 * @see #syncExec(Runnable)
	 * @see #timerExec(int, Runnable)
	 */
	public static void execute(Runnable runnable) {
		Display display = Display.getCurrent();
		if (display != null) {
			// the current thread is the UI thread
			runnable.run();
		} else {
			Display.getDefault().asyncExec(runnable);
		}
	}

	/**
	 * Execute the specified runnable if the current thread is the specified
	 * viewer's thread;
	 * otherwise asynchrounously dispatch the runnable to the viewer's thread,
	 * returning immediately. This is useful for event handlers when it is not
	 * obviously whether the events are fired on the viewer's thread.
	 * 
	 * @see #execute(Runnable)
	 * @see Display#asyncExec(Runnable)
	 * @see #asyncExec(Runnable)
	 * @see #syncExec(Runnable)
	 * @see #timerExec(int, Runnable)
	 */
	public static void execute(Viewer viewer, Runnable runnable) {
		execute(viewer.getControl(), runnable);
	}

	/**
	 * Execute the specified runnable if the current thread is the specified
	 * control's thread;
	 * otherwise asynchrounously dispatch the runnable to the control's thread,
	 * returning immediately. This is useful for event handlers when it is not
	 * obviously whether the events are fired on the control's thread.
	 * 
	 * @see #execute(Runnable)
	 * @see Display#asyncExec(Runnable)
	 * @see #asyncExec(Runnable)
	 * @see #syncExec(Runnable)
	 * @see #timerExec(int, Runnable)
	 */
	public static void execute(Control control, Runnable runnable) {
		execute(control.getDisplay(), runnable);
	}

	/**
	 * Execute the specified runnable if the current thread is the specified
	 * display's thread;
	 * otherwise asynchrounously dispatch the runnable to the display's thread,
	 * returning immediately. This is useful for event handlers when it is not
	 * obviously whether the events are fired on the display's thread.
	 * 
	 * @see #execute(Runnable)
	 * @see Display#asyncExec(Runnable)
	 * @see #asyncExec(Runnable)
	 * @see #syncExec(Runnable)
	 * @see #timerExec(int, Runnable)
	 */
	public static void execute(Display display, Runnable runnable) {
		if (display.getThread() == Thread.currentThread()) {
			// the current thread is the display's thread
			runnable.run();
		} else {
			display.asyncExec(runnable);
		}
	}

	/**
	 * Tweaks the given <code>Combo</code> to remove the default value when the
	 * widget receives the focus and to show the default when the widget loses
	 * the focus.
	 *
	 * @param combo The widget having a default value that is always at the
	 * beginning of the list
	 */
	public static void attachDefaultValueHandler(Combo combo) {
		ComboHandler handler = new ComboHandler();
		combo.addFocusListener(handler);
		combo.addModifyListener(handler);
	}


	/**
	 * Convenience method for getting the current shell. If the current thread is
	 * not the UI thread, then an invalid thread access exception will be thrown.
	 *
	 * @return The shell, never <code>null</code>
	 */
	public static Shell getShell() {
		// Retrieve the active shell, which can be the shell from any window
		Shell shell = getStandardDisplay().getActiveShell();
		if (shell != null) {
			return shell;
		}

		// No shell could be found, revert back to the active workbench window
		shell = getWorkbench().getActiveWorkbenchWindow().getShell();
		return (shell != null) ? shell : new Shell();
	}

	/**
	 * Return the "standard" {@link Display display}. Return the
	 * {@link Display#getCurrent() display associated with the current thread}
	 * if it is present; otherwise return the
	 * {@link Display#getDefault() default display}.
	 */
	public static Display getStandardDisplay() {
		Display display = Display.getCurrent();
		return (display != null) ? display : Display.getDefault();
	}

	public static int getTableHeightHint(Table table, int rows) {
		if (table.getFont().equals(JFaceResources.getDefaultFont()))
			table.setFont(JFaceResources.getDialogFont());
		int result= table.getItemHeight() * rows + table.getHeaderHeight();
		if (table.getLinesVisible())
			result+= table.getGridLineWidth() * (rows - 1);
		return result;
	}

   /**
	 * Returns the Platform UI workbench.
	 *
	 * @return The workbench for this plug-in
	 */
	public static IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}

	/**
	 * Relays out the parents of the <code>Control</code>. This was taken from
	 * the widget <code>Section</code>.
	 *
	 * @param pane The pane to revalidate as well as its parents
	 */
	public static void reflow(Composite pane) {

		for (Composite composite = pane; composite != null; ) {
			composite.setRedraw(false);
			composite = composite.getParent();

			if (composite instanceof ScrolledForm || composite instanceof Shell) {
				break;
			}
		}

		for (Composite composite = pane; composite != null; ) {
			composite.layout(true);
			composite = composite.getParent();

			if (composite instanceof ScrolledForm) {
				((ScrolledForm) composite).reflow(true);
				break;
			}
		}

		for (Composite composite = pane; composite != null; ) {
			composite.setRedraw(true);
			composite = composite.getParent();

			if (composite instanceof ScrolledForm || composite instanceof Shell) {
				break;
			}
		}
	}


	/**
	 * Determines if the current thread is the UI event thread.
	 *
	 * @return <code>true</code> if it's the UI event thread, <code>false</code>
	 * otherwise
	 */
	public static boolean uiThread() {
		return Display.getCurrent() != null;
	}

	/**
	 * Determines if the current thread is the UI event thread by using the
	 * thread from which the given viewer's display was instantiated.
	 *
	 * @param viewer The viewer used to determine if the current thread
	 * is the UI event thread
	 * @return <code>true</code> if the current thread is the UI event thread;
	 * <code>false</code> otherwise
	 */
	public static boolean uiThread(Viewer viewer) {
		return uiThread(viewer.getControl());
	}

	/**
	 * Determines if the current thread is the UI event thread by using the
	 * thread from which the given widget's display was instantiated.
	 *
	 * @param widget The widget used to determine if the current thread
	 * is the UI event thread
	 * @return <code>true</code> if the current thread is the UI event thread;
	 * <code>false</code> otherwise
	 */
	public static boolean uiThread(Widget widget) {
		return widget.getDisplay().getThread() == Thread.currentThread();
	}


	/**
	 * This handler is responsible for removing the default value when the combo
	 * has the focus or when the selected item is the default value and to select
	 * it when the combo loses the focus.
	 */
	private static class ComboHandler implements ModifyListener,
	                                             FocusListener {

		public void focusGained(FocusEvent e) {
			Combo combo = (Combo) e.widget;

			if (combo.getSelectionIndex() == 0) {	
				// The text selection has to be changed outside of the context of this
				// listener otherwise the combo won't update because it's currently
				// notifying its listeners
				asyncExec(new SelectText(combo));
			}
		}

		public void focusLost(FocusEvent e) {
			//do nothing
		}

		public void modifyText(ModifyEvent e) {

			Combo combo = (Combo) e.widget;

			if (combo.isFocusControl() &&
			    combo.getSelectionIndex() == 0) {

				// The text has to be changed outside of the context of this
				// listener otherwise the combo won't update because it's currently
				// notifying its listeners
				asyncExec(new ModifyText(combo));
			}
		}

		private class ModifyText implements Runnable {
			private final Combo combo;

			public ModifyText(Combo combo) {
				super();
				this.combo = combo;
			}

			public void run() {
				if (this.combo.isDisposed()) {
					return;
				}
				String text = this.combo.getText();

				if (text.length() == 0) {
					text = this.combo.getItem(0);
					this.combo.setText(text);
				}

				this.combo.setSelection(new Point(0, text.length()));
			}
		}

		private class SelectText implements Runnable {
			private final Combo combo;

			public SelectText(Combo combo) {
				super();
				this.combo = combo;
			}

			public void run() {
				if (this.combo.isDisposed()) {
					return;
				}
				String text = this.combo.getText();
				this.combo.setSelection(new Point(0, text.length()));
			}
		}
	}
}