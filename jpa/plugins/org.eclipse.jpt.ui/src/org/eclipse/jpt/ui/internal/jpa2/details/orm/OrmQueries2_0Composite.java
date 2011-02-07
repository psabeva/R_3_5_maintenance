/*******************************************************************************
* Copyright (c) 2009 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.ui.internal.jpa2.details.orm;

import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.core.context.QueryContainer;
import org.eclipse.jpt.core.context.orm.EntityMappings;
import org.eclipse.jpt.ui.internal.details.QueriesComposite;
import org.eclipse.jpt.ui.internal.details.orm.OrmQueriesComposite;
import org.eclipse.jpt.ui.internal.jpa2.details.Queries2_0Composite;
import org.eclipse.swt.widgets.Composite;

/**
 *  OrmQueries2_0Composite
 */
public class OrmQueries2_0Composite extends OrmQueriesComposite {

	/**
	 * Creates a new <code>OrmQueries2_0Composite</code>.
	 *
	 * @param parentPane The parent container of this one
	 * @param parent The parent container
	 */
	public OrmQueries2_0Composite(Pane<? extends EntityMappings> parentPane,
	                           Composite parent) {

		super(parentPane, parent);
	}

	@Override
	protected QueriesComposite buildQueriesComposite(Composite container, PropertyValueModel<QueryContainer> queryContainerHolder) {
		return new Queries2_0Composite(this, queryContainerHolder, container);
	}
	
}