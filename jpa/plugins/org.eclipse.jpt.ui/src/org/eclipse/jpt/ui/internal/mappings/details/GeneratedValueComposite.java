/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.mappings.details;

import java.util.Iterator;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jpt.core.internal.context.base.GenerationType;
import org.eclipse.jpt.core.internal.context.base.IGeneratedValue;
import org.eclipse.jpt.core.internal.context.base.IIdMapping;
import org.eclipse.jpt.ui.internal.IJpaHelpContextIds;
import org.eclipse.jpt.ui.internal.details.BaseJpaComposite;
import org.eclipse.jpt.ui.internal.mappings.JptUiMappingsMessages;
import org.eclipse.jpt.utility.internal.model.value.PropertyValueModel;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public class GeneratedValueComposite extends BaseJpaComposite<IIdMapping>
{
	private IGeneratedValue generatedValue;
	private Adapter generatedValueListener;
	private CCombo generatorNameCombo;
	private ComboViewer strategyComboViewer;

	public GeneratedValueComposite(PropertyValueModel<? extends IIdMapping> subjectHolder,
	                               Composite parent,
	                               TabbedPropertySheetWidgetFactory widgetFactory) {

		super(subjectHolder, parent, SWT.NULL, widgetFactory);
		this.generatedValueListener = buildGeneratedValueListener();
	}

	private Adapter buildGeneratedValueListener() {
		return new AdapterImpl() {
			@Override
			public void notifyChanged(Notification notification) {
				generatedValueChanged(notification);
			}
		};
	}

	protected CCombo buildGeneratorNameCombo(Composite parent) {
		CCombo combo = getWidgetFactory().createCCombo(parent, SWT.FLAT);
		combo.add(JptUiMappingsMessages.TableComposite_defaultEmpty);
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String generatorName = ((CCombo) e.getSource()).getText();

				if (generatorName.equals("")) { //$NON-NLS-1$
					if (generatedValue == null || generatedValue.getGenerator() == null || generatedValue.getGenerator().equals("")) {
						return;
					}
					generatorName = null;
				}
				if (generatedValue == null) {
					createGeneratedValue();
				}
				generatedValue.setSpecifiedGenerator(generatorName);
			}
		});
		return combo;
	}

	private ComboViewer buildStrategyComboViewer(Composite parent) {
		CCombo combo = getWidgetFactory().createCCombo(parent);
		ComboViewer viewer = new ComboViewer(combo);
		viewer.setLabelProvider(buildStrategyLabelProvider());
		viewer.add(GenerationType.values());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() instanceof StructuredSelection) {
					StructuredSelection selection = (StructuredSelection) event.getSelection();
					GenerationType selectedType = (GenerationType) selection.getFirstElement();
					if (generatedValue == null) {
						createGeneratedValue();
					}
					if (!generatedValue.getStrategy().equals(selectedType)) {
						generatedValue.setSpecifiedStrategy(selectedType);
					}
				}
			}
		});
		return viewer;
	}

	private IBaseLabelProvider buildStrategyLabelProvider() {
		return new LabelProvider() {
			@Override
			public String getText(Object element) {
				// TODO
				if (element == GenerationType.AUTO) {// GenerationType.DEFAULT) {
					//TODO need to move this to the model, don't want hardcoded String
					return NLS.bind(JptUiMappingsMessages.GeneratedValueComposite_default, "Auto");
				}
				return super.getText(element);
			}
		};
	}


	private void createGeneratedValue() {
		this.generatedValue = this.subject().createGeneratedValue();
		this.subject().setGeneratedValue(this.generatedValue);
	}

	@Override
	protected void disengageListeners() {
		super.disengageListeners();
//		if (this.generatedValue != null) {
//			this.generatedValue.eAdapters().remove(this.generatedValueListener);
//		}
	}

	@Override
	protected void doPopulate() {
		if (this.subject() == null) {
			this.generatedValue= null;
		}
		else {
			this.generatedValue = this.subject().getGeneratedValue();
		}
		if (this.generatedValue == null) {
			this.strategyComboViewer.getCCombo().setText("");
			this.generatorNameCombo.setText("");
			return;
		}
		populateStrategyCombo();
		populateGeneratorNameCombo();
	}

	@Override
	protected void engageListeners() {
		super.engageListeners();
//		if (this.generatedValue != null) {
//			this.generatedValue.eAdapters().add(this.generatedValueListener);
//		}
	}

	protected void generatedValueChanged(Notification notification) {
		if (notification.getFeatureID(IGeneratedValue.class) == JpaCoreMappingsPackage.IGENERATED_VALUE__STRATEGY) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (getControl().isDisposed()) {
						return;
					}
					if (selectedStrategy() != generatedValue.getStrategy()) {
						strategyComboViewer.setSelection(new StructuredSelection(generatedValue.getStrategy()));
					}
				}
			});
		}
		else if (notification.getFeatureID(IGeneratedValue.class) == JpaCoreMappingsPackage.IGENERATED_VALUE__GENERATOR) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (getControl().isDisposed()) {
						return;
					}
					populateGeneratorName();
				}
			});
		}
	}

	private IGeneratorRepository getGeneratorRepository() {
		return NullGeneratorRepository.instance(); //this.id.getJpaProject().getPlatform().generatorRepository(this.id.typeMapping().getPersistentType());
	}

	@Override
	protected void initializeLayout(Composite composite) {
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();

		getWidgetFactory().createLabel(composite, JptUiMappingsMessages.GeneratedValueComposite_strategy);

		this.strategyComboViewer = buildStrategyComboViewer(composite);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		this.strategyComboViewer.getCCombo().setLayoutData(gridData);
		helpSystem.setHelp(this.strategyComboViewer.getCCombo(), IJpaHelpContextIds.MAPPING_GENERATED_VALUE_STRATEGY);

		getWidgetFactory().createLabel(composite, JptUiMappingsMessages.GeneratedValueComposite_generatorName);

		this.generatorNameCombo = buildGeneratorNameCombo(composite);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		this.generatorNameCombo.setLayoutData(gridData);
		helpSystem.setHelp(this.generatorNameCombo, IJpaHelpContextIds.MAPPING_GENERATED_VALUE_GENERATOR_NAME);

		// TODO
		// buildGeneratorNameSelectionButton( this);
	}

	private void populateGeneratorName() {
		String generatorName = this.generatedValue.getGenerator();
		if (generatorName == null || generatorName.equals("")) {
			this.generatorNameCombo.setText("");
		}
		else if (!this.generatorNameCombo.getText().equals(generatorName)) {
			this.generatorNameCombo.setText(generatorName);
		}
	}
	private void populateGeneratorNameCombo() {
		this.generatorNameCombo.removeAll();
		for (Iterator<String> i = getGeneratorRepository().generatorNames(); i.hasNext(); ){
			this.generatorNameCombo.add(i.next());
		}

		populateGeneratorName();
	}

	private void populateStrategyCombo() {
		GenerationType selectedType = selectedStrategy();
		GenerationType strategy = this.generatedValue.getStrategy();
		if (strategy == GenerationType.AUTO) {
			if (selectedType != GenerationType.AUTO) {
				this.strategyComboViewer.setSelection(new StructuredSelection(GenerationType.AUTO));
			}
		}
		else if (strategy == GenerationType.SEQUENCE) {
			if (selectedType != GenerationType.SEQUENCE) {
				this.strategyComboViewer.setSelection(new StructuredSelection(GenerationType.SEQUENCE));
			}
		}
		else if (strategy == GenerationType.IDENTITY) {
			if (selectedType != GenerationType.IDENTITY) {
				this.strategyComboViewer.setSelection(new StructuredSelection(GenerationType.IDENTITY));
			}
		}
		else if (strategy == GenerationType.TABLE) {
			if (selectedType != GenerationType.TABLE) {
				this.strategyComboViewer.setSelection(new StructuredSelection(GenerationType.TABLE));
			}
		}
		else {
			if (selectedType != GenerationType.DEFAULT) {
				this.strategyComboViewer.setSelection(new StructuredSelection(GenerationType.DEFAULT));
			}
		}
		// TODO first initialization
	}

	private GenerationType selectedStrategy() {
		return (GenerationType) ((StructuredSelection) this.strategyComboViewer.getSelection()).getFirstElement();
	}
}