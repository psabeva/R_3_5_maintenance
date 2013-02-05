/*******************************************************************************
 *  Copyright (c) 2012  Oracle. All rights reserved.
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0, which accompanies this distribution
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jaxb.eclipselink.ui.internal.navigator;

import org.eclipse.jpt.common.ui.internal.jface.AbstractItemTreeContentProvider;
import org.eclipse.jpt.common.utility.internal.model.value.ItemPropertyListValueModelAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.ListAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.ListCollectionValueModelAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.iterable.ListIterable;
import org.eclipse.jpt.common.utility.model.value.CollectionValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jaxb.eclipselink.core.context.ELJaxbPackage;
import org.eclipse.jpt.jaxb.eclipselink.core.context.oxm.OxmFile;
import org.eclipse.jpt.jaxb.eclipselink.core.context.oxm.OxmJavaType;
import org.eclipse.jpt.jaxb.eclipselink.core.context.oxm.OxmXmlBindings;

public class OxmFileContentProvider
		extends AbstractItemTreeContentProvider<OxmFile, OxmJavaType> {
	
	public OxmFileContentProvider(OxmFile item, Manager manager) {
		super(item, manager);
	}
	
	
	public ELJaxbPackage getParent() {
		// don't need to worry about change notification, as if the parent package changes,
		// this node will be represented in a different place in the tree
		return this.item.getJaxbPackage();
	}
	
	@Override
	protected CollectionValueModel<OxmJavaType> buildChildrenModel() {
		return new ListCollectionValueModelAdapter<OxmJavaType>(
				new ItemPropertyListValueModelAdapter<OxmJavaType>(
						new ListAspectAdapter<OxmXmlBindings, OxmJavaType>(buildXmlBindingsModel(), OxmXmlBindings.JAVA_TYPES_LIST) {
							@Override
							protected ListIterable<OxmJavaType> getListIterable() {
								return this.subject.getJavaTypes();
							}
							@Override
							protected int size_() {
								return this.subject.getJavaTypesSize();
							}
						}));
	}
	
	protected PropertyValueModel<OxmXmlBindings> buildXmlBindingsModel() {
		return new PropertyAspectAdapter<OxmFile, OxmXmlBindings>(OxmFile.XML_BINDINGS_PROPERTY, this.item) {
			@Override
			protected OxmXmlBindings buildValue_() {
				return this.subject.getXmlBindings();
			}
		};
	}
}