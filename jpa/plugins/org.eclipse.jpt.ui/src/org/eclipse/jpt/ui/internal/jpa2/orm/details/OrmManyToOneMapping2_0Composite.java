/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.jpa2.orm.details;

import org.eclipse.jpt.core.context.AccessHolder;
import org.eclipse.jpt.core.context.ManyToOneMapping;
import org.eclipse.jpt.core.context.orm.OrmManyToOneMapping;
import org.eclipse.jpt.ui.WidgetFactory;
import org.eclipse.jpt.ui.internal.details.AccessTypeComposite;
import org.eclipse.jpt.ui.internal.mappings.details.AbstractManyToOneMappingComposite;
import org.eclipse.jpt.ui.internal.mappings.details.CascadeComposite;
import org.eclipse.jpt.ui.internal.mappings.details.FetchTypeComposite;
import org.eclipse.jpt.ui.internal.mappings.details.ManyToOneJoiningStrategyPane;
import org.eclipse.jpt.ui.internal.mappings.details.OptionalComposite;
import org.eclipse.jpt.ui.internal.mappings.details.TargetEntityComposite;
import org.eclipse.jpt.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.widgets.Composite;

/**
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | TargetEntityComposite                                                 | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | JoiningStrategyComposite                                              | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | FetchTypeComposite                                                    | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | OptionalComposite                                                     | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | CascadeComposite                                                      | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see {@link ManyToOneMapping}
 * @see {@link BaseJpaUiFactory} - The factory creating this pane
 * @see {@link TargetEntityComposite}
 * @see {@link ManyToOneJoiningStrategyPane}
 * @see {@link FetchTypeComposite}
 * @see {@link OptionalComposite}
 * @see {@link CascadeComposite}
 *
 * @version 2.2
 * @since 2.2
 */
public class OrmManyToOneMapping2_0Composite extends AbstractManyToOneMappingComposite<OrmManyToOneMapping>
{
	/**
	 * Creates a new <code>EclipseLink1_1ManyToOneMappingComposite</code>.
	 *
	 * @param subjectHolder The holder of the subject <code>ManyToOneMapping</code>
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create various common widgets
	 */
	public OrmManyToOneMapping2_0Composite(PropertyValueModel<? extends OrmManyToOneMapping> subjectHolder,
	                                 Composite parent,
	                                 WidgetFactory widgetFactory) {

		super(subjectHolder, parent, widgetFactory);
	}

	@Override
	protected void initializeLayout(Composite container) {
		int groupBoxMargin = getGroupBoxMargin();
		
		new TargetEntityComposite(this, addPane(container, groupBoxMargin));
		new ManyToOneJoiningStrategyPane(this, buildJoiningHolder(), container);
		new AccessTypeComposite(this, buildAccessHolderHolder(), addPane(container, groupBoxMargin));
		new FetchTypeComposite(this, addPane(container, groupBoxMargin));
		new OptionalComposite(this, addPane(container, groupBoxMargin));
		new CascadeComposite(this, buildCascadeHolder(),  addSubPane(container, 5));
	}
	
	protected PropertyValueModel<AccessHolder> buildAccessHolderHolder() {
		return new PropertyAspectAdapter<OrmManyToOneMapping, AccessHolder>(getSubjectHolder()) {
			@Override
			protected AccessHolder buildValue_() {
				return this.subject.getPersistentAttribute();
			}
		};
	}

}
