/*******************************************************************************
 * Copyright (c) 2010, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa1.context.orm;

import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.jpa.core.context.Converter;
import org.eclipse.jpt.jpa.core.context.orm.OrmAttributeMapping;
import org.eclipse.jpt.jpa.core.context.orm.OrmConverter;
import org.eclipse.jpt.jpa.core.internal.context.orm.AbstractOrmXmlContextModel;

public class NullOrmConverter
	extends AbstractOrmXmlContextModel<OrmAttributeMapping>
	implements OrmConverter
{
	public NullOrmConverter(OrmAttributeMapping parent) {
		super(parent);
	}

	public void initialize() {
		// NOP
	}

	public Class<? extends Converter> getConverterType() {
		return null;
	}

	public TextRange getValidationTextRange() {
		return this.parent.getValidationTextRange();
	}
}
