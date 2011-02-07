/*******************************************************************************
 * Copyright (c) 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.v2_0.context.java;

import java.util.List;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.internal.jpa2.context.java.AbstractJavaElementCollectionMapping2_0;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkJoinFetch;
import org.eclipse.jpt.eclipselink.core.internal.context.java.JavaEclipseLinkJoinFetch;
import org.eclipse.jpt.eclipselink.core.v2_0.context.EclipseLinkElementCollectionMapping2_0;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;


public class JavaEclipseLinkElementCollectionMapping2_0
	extends AbstractJavaElementCollectionMapping2_0
	implements EclipseLinkElementCollectionMapping2_0
{
	protected final JavaEclipseLinkJoinFetch joinFetch;


	public JavaEclipseLinkElementCollectionMapping2_0(JavaPersistentAttribute parent) {
		super(parent);
		this.joinFetch = new JavaEclipseLinkJoinFetch(this);
	}

	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.joinFetch.synchronizeWithResourceModel();
	}

	@Override
	public void update() {
		super.update();
		this.joinFetch.update();
	}


	// ********** join fetch **********

	public EclipseLinkJoinFetch getJoinFetch() {
		return this.joinFetch;
	}
	

	// ********** validation **********

	@Override
	public void validate(List<IMessage> messages, IReporter reporter, CompilationUnit astRoot) {
		super.validate(messages, reporter, astRoot);
		this.joinFetch.validate(messages, reporter, astRoot);
	}
}