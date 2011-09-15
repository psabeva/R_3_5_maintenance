/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2005, 2010 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kiril Mitov - initial API, implementation and documentation
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.jpt.jpadiagrameditor.ui.internal.util;

import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.PropertyContainer;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.services.Graphiti;

public class PeServiceUtilImpl implements IPeServiceUtil {

	public FreeFormConnection createFreeFormConnection(Diagram diagram) {
		return Graphiti.getPeService().createFreeFormConnection(diagram);
	}
	
	public String getPropertyValue(PropertyContainer propertyContainer, String key) {
		return Graphiti.getPeService().getPropertyValue(propertyContainer, key);
	}

	public Property getProperty(PropertyContainer propertyContainer, String key) {
		return Graphiti.getPeService().getProperty(propertyContainer, key);
	}
	
	public boolean removeProperty(PropertyContainer propertyContainer, String key) {
		return Graphiti.getPeService().removeProperty(propertyContainer, key);
	}
	
	public void setPropertyValue(PropertyContainer propertyContainer, String key, String value) {	
		Graphiti.getPeService().setPropertyValue(propertyContainer, key, value);
	}

}
