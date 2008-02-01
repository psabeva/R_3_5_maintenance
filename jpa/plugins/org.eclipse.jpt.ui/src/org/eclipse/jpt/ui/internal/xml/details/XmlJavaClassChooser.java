/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.xml.details;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.ControlContentAssistHelper;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaTypeCompletionProcessor;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.window.Window;
import org.eclipse.jpt.core.internal.context.orm.XmlTypeMapping;
import org.eclipse.jpt.core.internal.resource.orm.TypeMapping;
import org.eclipse.jpt.ui.internal.JptUiPlugin;
import org.eclipse.jpt.ui.internal.widgets.AbstractFormPane;
import org.eclipse.jpt.ui.internal.xml.JptUiXmlMessages;
import org.eclipse.jpt.utility.internal.model.value.PropertyValueModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

//TODO possibly help the user and if they have chosen a package at the entity-mappings level
//only insert the class name in the xml file if they choose a class from the package.
//Not sure if this should be driven by the UI or by ui api in the model
public class XmlJavaClassChooser extends AbstractFormPane<XmlTypeMapping<?>> {

	private Composite composite;
	private Text text;
	private JavaTypeCompletionProcessor javaTypeCompletionProcessor;

	/**
	 * Creates a new <code>XmlJavaClassChooser</code>.
	 *
	 * @param parentPane The parent controller of this one
	 * @param subjectHolder The holder of this pane's subject
	 * @param parent The parent container
	 */
	public XmlJavaClassChooser(AbstractFormPane<?> parentPane,
        PropertyValueModel<? extends XmlTypeMapping<? extends TypeMapping>> subjectHolder,
        Composite parent) {

		super(parentPane, subjectHolder, parent);
	}

	/**
	 * Creates a new <code>XmlJavaClassChooser</code>.
	 *
	 * @param subjectHolder The holder of this pane's subject
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create various common widgets
	 */
	public XmlJavaClassChooser(PropertyValueModel<? extends XmlTypeMapping<? extends TypeMapping>> subjectHolder,
	                                   Composite parent,
	                                   TabbedPropertySheetWidgetFactory widgetFactory) {

		super(subjectHolder, parent, widgetFactory);
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void initializeLayout(Composite container) {
		this.composite = getWidgetFactory().createComposite(container);
	    GridLayout gridLayout = new GridLayout();
	    gridLayout.marginHeight = 0;
	    gridLayout.marginWidth = 1;
	    gridLayout.numColumns = 2;
	    this.composite.setLayout(gridLayout);

		text = getWidgetFactory().createText(this.composite);
		GridData data = new GridData();
	    data.grabExcessHorizontalSpace = true;
	    data.horizontalAlignment = GridData.FILL;
	    this.text.setLayoutData(data);
		text.addModifyListener(
			new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					textModified(e);
				}
			});

		//TODO bug 156185 - when this is fixed there should be api for this
		this.javaTypeCompletionProcessor = new JavaTypeCompletionProcessor(false, false);
		ControlContentAssistHelper.createTextContentAssistant(this.text,  this.javaTypeCompletionProcessor);


		Button browseButton = getWidgetFactory().createButton(this.composite, "Browse...", SWT.FLAT);
		data = new GridData();
		browseButton.setLayoutData(data);
		browseButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				IType type = chooseType();
				if (type != null) {
					text.setText(type.getFullyQualifiedName());
				}
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				IType type = chooseType();
				if (type != null) {
					text.setText(type.getFullyQualifiedName());
				}
			}
		});

	}

	private void textModified(ModifyEvent e) {
		String text = ((Text) e.getSource()).getText();
		subject().setClass(text);

		// TODO Does this need to be done?
		//this.editingDomain.getCommandStack().execute(SetCommand.create(this.editingDomain, this.entity, MappingsPackage.eINSTANCE.getEntity_SpecifiedName(), text));
	}


	protected IType chooseType() {
		IPackageFragmentRoot root= getPackageFragmentRoot();
		if (root == null) {
			return null;
		}

		IJavaElement[] elements= new IJavaElement[] { root.getJavaProject() };
		IJavaSearchScope scope= SearchEngine.createJavaSearchScope(elements);
		IProgressService service = PlatformUI.getWorkbench().getProgressService();

		SelectionDialog typeSelectionDialog;
		try {
			typeSelectionDialog = JavaUI.createTypeDialog(this.text.getShell(), service, scope, IJavaElementSearchConstants.CONSIDER_CLASSES, false, getType());
		}
		catch (JavaModelException e) {
			JptUiPlugin.log(e);
			throw new RuntimeException(e);
		}
		typeSelectionDialog.setTitle(JptUiXmlMessages.XmlJavaClassChooser_XmlJavaClassDialog_title);
		typeSelectionDialog.setMessage(JptUiXmlMessages.XmlJavaClassChooser_XmlJavaClassDialog_message);

		if (typeSelectionDialog.open() == Window.OK) {
			return (IType) typeSelectionDialog.getResult()[0];
		}
		return null;
	}

	private String getType() {
		return this.text.getText();
	}

	private IPackageFragmentRoot getPackageFragmentRoot() {
		IProject project = this.subject().jpaProject().project();
		IJavaProject root = JavaCore.create(project);
		try {
			return root.getAllPackageFragmentRoots()[0];
		}
		catch (JavaModelException e) {
			JptUiPlugin.log(e);
		}
		return null;
	}

	@Override
	protected void doPopulate() {
		super.doPopulate();
		populateText();
	}

	private void populateText() {
		if (this.subject() == null) {
			text.clearSelection();
			return;
		}

		IPackageFragmentRoot root = getPackageFragmentRoot();
		if (root != null) {
			 this.javaTypeCompletionProcessor.setPackageFragment(root.getPackageFragment(""));
		}

		String javaClass = this.subject().getClass_();

		if (javaClass == null) {
			setTextData("");
		}
		else {
			setTextData(javaClass);
		}
	}

	private void setTextData(String textData) {
		if (! textData.equals(text.getText())) {
			text.setText(textData);
		}
	}
}