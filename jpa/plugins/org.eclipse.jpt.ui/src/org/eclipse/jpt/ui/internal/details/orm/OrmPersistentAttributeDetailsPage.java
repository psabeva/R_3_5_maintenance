/*******************************************************************************
 * Copyright (c) 2006, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.details.orm;

import java.util.ArrayList;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.ui.internal.util.PaneEnabler;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.core.context.ReadOnlyPersistentAttribute;
import org.eclipse.jpt.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.core.context.orm.OrmReadOnlyPersistentAttribute;
import org.eclipse.jpt.ui.details.JpaComposite;
import org.eclipse.jpt.ui.internal.details.PersistentAttributeDetailsPage;
import org.eclipse.jpt.ui.internal.details.PersistentAttributeMapAsComposite;
import org.eclipse.swt.widgets.Composite;

/**
 * The default implementation of the details page used for the XML persistent
 * attribute.
 * <p>
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | OrmPersistentAttributeMapAsComposite                                  | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | Attribute mapping pane                                                | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see OrmPersistentAttribute
 * @see OrmPersistentAttributeMapAsComposite
 *
 * @version 2.3
 * @since 2.0
 */
public class OrmPersistentAttributeDetailsPage
	extends PersistentAttributeDetailsPage<OrmReadOnlyPersistentAttribute>
{
	/**
	 * Creates a new <code>OrmPersistentAttributeDetailsPage</code>.
	 *
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create various common widgets
	 */
	public OrmPersistentAttributeDetailsPage(Composite parent,
	                                         WidgetFactory widgetFactory) {

		super(parent, widgetFactory);
	}
	
	@Override
	protected void initializeLayout(Composite container) {

		ArrayList<Pane<?>> panes = new ArrayList<Pane<?>>(2);

		// Map As composite
		Pane<?> mapAsPane = buildMapAsPane(addSubPane(container, 0, 0, 5, 0));
		panes.add(mapAsPane);

		buildMappingPageBook(container);

		installPaneEnabler(panes);
	}
	
	protected Pane<ReadOnlyPersistentAttribute> buildMapAsPane(Composite parent) {
		return new PersistentAttributeMapAsComposite(this, parent);		
	}
	
	private void installPaneEnabler(ArrayList<Pane<?>> panes) {
		new PaneEnabler(buildPaneEnablerHolder(), panes);
	}
	
	private PropertyValueModel<Boolean> buildPaneEnablerHolder() {
		return new TransformationPropertyValueModel<OrmReadOnlyPersistentAttribute, Boolean>(getSubjectHolder()) {
			@Override
			protected Boolean transform_(OrmReadOnlyPersistentAttribute value) {
				return Boolean.valueOf(!value.isVirtual());
			}
		};
	}

	
	//TODO this probably needs to change and use a PaneEnabler instead.
	@Override
	protected JpaComposite getMappingComposite(String key) {
		JpaComposite mappingComposite = super.getMappingComposite(key);
		if (mappingComposite == null) {
			return null;
		}
		boolean enabled = false;

		if (getSubject() != null && getSubject().getParent() != null) {
			enabled = !getSubject().isVirtual();
		}

		mappingComposite.enableWidgets(enabled);
		return mappingComposite;
	}
}