/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.resource.java;


public interface JoinColumns extends ContainerAnnotation<NestableJoinColumn>
{
	String ANNOTATION_NAME = JPA.JOIN_COLUMNS;
	
	String JOIN_COLUMNS_LIST = "joinColumnsList";
	
}
