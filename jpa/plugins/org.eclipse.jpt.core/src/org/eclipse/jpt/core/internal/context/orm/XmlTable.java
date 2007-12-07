/*******************************************************************************
 * Copyright (c) 2006, 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0, which accompanies this distribution and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import org.eclipse.jpt.core.internal.context.base.IEntity;
import org.eclipse.jpt.core.internal.context.base.InheritanceType;
import org.eclipse.jpt.core.internal.context.java.IJavaEntity;
import org.eclipse.jpt.core.internal.context.java.IJavaTable;
import org.eclipse.jpt.core.internal.resource.orm.AbstractTable;
import org.eclipse.jpt.core.internal.resource.orm.Entity;
import org.eclipse.jpt.core.internal.resource.orm.OrmFactory;

public class XmlTable extends AbstractXmlTable
{

	protected Entity entity;
	
	public XmlTable(XmlEntity parent) {
		super(parent);
	}

	public XmlEntity xmlEntity() {
		return (XmlEntity) super.parent();
	}
	
	@Override
	protected AbstractTable table() {
		return this.entity.getTable();
	}
	
	@Override
	protected void removeTableResource() {
		this.entity.setTable(null);
	}
	
	@Override
	protected void addTableResource() {
		this.entity.setTable(OrmFactory.eINSTANCE.createTable());
		
	}
	
	protected IJavaTable javaTable() {
		IJavaEntity javaEntity = xmlEntity().javaEntity();
		if (javaEntity != null) {
			return javaEntity.getTable();
		}
		return null;
	}
	
	
	public void initialize(Entity entity) {
		this.entity = entity;
		super.initialize(this.table());
	}
	
	public void update(Entity entity) {
		this.entity = entity;
		super.update(this.table());
	}

	@Override
	protected String defaultName() {
		IJavaTable javaTable = javaTable();
		if (javaTable != null) {
			if (xmlEntity().isMetadataComplete() || (table() != null)) {
				return javaTable.getDefaultName();
			}
			else {
				return javaTable.getName();
			}
		}
		IEntity rootEntity = xmlEntity().rootEntity();
		if (rootEntity != xmlEntity()) {
			if (rootEntity.getInheritanceStrategy() == InheritanceType.SINGLE_TABLE) {
				return rootEntity.getTable().getName();
			}
		}
		return xmlEntity().getName();
	}
	
	@Override
	protected String defaultSchema() {
		IJavaTable javaTable = javaTable();
		if (javaTable != null ) {
			if (xmlEntity().isMetadataComplete() || (table() != null)) {
				return javaTable.getDefaultSchema();
			}
			else {
				return javaTable.getSchema();
			}
		}
		IEntity rootEntity = xmlEntity().rootEntity();
		if (rootEntity != xmlEntity()) {
			if (rootEntity.getInheritanceStrategy() == InheritanceType.SINGLE_TABLE) {
				return rootEntity.getTable().getSchema();
			}
		}
		return entityMappings().getSchema();
	}
	
	@Override
	protected String defaultCatalog() {
		IJavaTable javaTable = javaTable();
		if (javaTable != null) {
			if (xmlEntity().isMetadataComplete() || (table() != null)) {
				return javaTable.getDefaultCatalog();
			}
			else {
				return javaTable.getCatalog();
			}
		}
		IEntity rootEntity = xmlEntity().rootEntity();
		if (rootEntity != xmlEntity()) {
			if (rootEntity.getInheritanceStrategy() == InheritanceType.SINGLE_TABLE) {
				return rootEntity.getTable().getCatalog();
			}
		}
		return entityMappings().getCatalog();
	}
}
