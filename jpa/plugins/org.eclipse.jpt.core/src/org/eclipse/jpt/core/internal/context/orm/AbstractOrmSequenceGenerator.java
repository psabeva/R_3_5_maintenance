/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import org.eclipse.jpt.core.context.SequenceGenerator;
import org.eclipse.jpt.core.context.XmlContextNode;
import org.eclipse.jpt.core.context.orm.OrmSequenceGenerator;
import org.eclipse.jpt.core.resource.orm.XmlSequenceGenerator;

/**
 * 
 */
public abstract class AbstractOrmSequenceGenerator
	extends AbstractOrmGenerator<XmlSequenceGenerator>
	implements OrmSequenceGenerator
{

	protected String specifiedSequenceName;
	protected String defaultSequenceName;


	protected AbstractOrmSequenceGenerator(XmlContextNode parent, XmlSequenceGenerator resourceSequenceGenerator) {
		super(parent);
		this.initialize(resourceSequenceGenerator);
	}


	@Override
	public int getDefaultInitialValue() {
		return SequenceGenerator.DEFAULT_INITIAL_VALUE;
	}

	// ********** sequence name **********

	public String getSequenceName() {
		return (this.specifiedSequenceName != null) ? this.specifiedSequenceName : this.defaultSequenceName;
	}

	public String getSpecifiedSequenceName() {
		return this.specifiedSequenceName;
	}

	public void setSpecifiedSequenceName(String specifiedSequenceName) {
		String old = this.specifiedSequenceName;
		this.specifiedSequenceName = specifiedSequenceName;
		this.getResourceGenerator().setSequenceName(specifiedSequenceName);
		this.firePropertyChanged(SPECIFIED_SEQUENCE_NAME_PROPERTY, old, specifiedSequenceName);
	}
	
	protected void setSpecifiedSequenceName_(String specifiedSequenceName) {
		String old = this.specifiedSequenceName;
		this.specifiedSequenceName = specifiedSequenceName;
		this.firePropertyChanged(SPECIFIED_SEQUENCE_NAME_PROPERTY, old, specifiedSequenceName);
	}

	public String getDefaultSequenceName() {
		return this.defaultSequenceName;
	}
	
	protected void setDefaultSequenceName(String defaultSequenceName) {
		String old = this.defaultSequenceName;
		this.defaultSequenceName = defaultSequenceName;
		this.firePropertyChanged(DEFAULT_SEQUENCE_NAME_PROPERTY, old, defaultSequenceName);
	}


	// ********** resource => context **********

	@Override
	protected void initialize(XmlSequenceGenerator sequenceGenerator) {
		super.initialize(sequenceGenerator);
		this.specifiedSequenceName = sequenceGenerator.getSequenceName();
		//TODO default sequence name
	}
	
	@Override
	public void update(XmlSequenceGenerator sequenceGenerator) {
		super.update(sequenceGenerator);
		this.setSpecifiedSequenceName_(sequenceGenerator.getSequenceName());
		//TODO default sequence name
	}

}
