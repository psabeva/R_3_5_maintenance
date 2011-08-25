/*******************************************************************************
 * Copyright (c) 2006, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa1.context.java;

import org.eclipse.jpt.common.core.resource.java.JavaResourceType;
import org.eclipse.jpt.jpa.core.context.java.JavaEntity;
import org.eclipse.jpt.jpa.core.internal.context.java.AbstractJavaTable;
import org.eclipse.jpt.jpa.core.resource.java.TableAnnotation;

/**
 * Java table
 */
public class GenericJavaTable
	extends AbstractJavaTable<TableAnnotation>
{
	public GenericJavaTable(JavaEntity parent, Owner owner) {
		super(parent, owner);
	}


	// ********** table annotation **********

	@Override
	public TableAnnotation getTableAnnotation() {
		// TODO get the NullTableAnnotation from the resource model or build it here in the context model??
		return (TableAnnotation) this.getJavaResourceType().getNonNullAnnotation(this.getAnnotationName());
	}

	@Override
	protected void removeTableAnnotation() {
		this.getJavaResourceType().removeAnnotation(this.getAnnotationName());
	}

	protected String getAnnotationName() {
		return TableAnnotation.ANNOTATION_NAME;
	}

	protected JavaResourceType getJavaResourceType() {
		return this.getEntity().getPersistentType().getJavaResourceType();
	}


	// ********** defaults **********

	@Override
	protected String buildDefaultName() {
		return this.getEntity().getDefaultTableName();
	}

	/**
	 * Just to remember:<ol>
	 * <li>{@link org.eclipse.jpt.jpa.core.context.Entity#getDefaultSchema()}<br>
	 *     check inheritance; get default schema from root
	 * <li>{@link org.eclipse.jpt.jpa.core.context.orm.EntityMappings#getSchema()}<br>
	 *     check for specified schema
	 * <li>{@link org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit#getDefaultSchema()}<br>
	 *     {@link org.eclipse.jpt.jpa.core.context.orm.OrmPersistenceUnitDefaults#getSchema()}
	 * <li>{@link org.eclipse.jpt.jpa.core.JpaProject#getDefaultSchema()}<br>
	 *     check for user override project setting
	 * <li>{@link org.eclipse.jpt.jpa.db.Catalog#getDefaultSchema()}<br>
	 *     or {@link org.eclipse.jpt.jpa.db.Database#getDefaultSchema()}
	 * </ol>
	 */
	@Override
	protected String buildDefaultSchema() {
		return this.getEntity().getDefaultSchema();
	}

	@Override
	protected String buildDefaultCatalog() {
		return this.getEntity().getDefaultCatalog();
	}


	// ********** validation **********

	public boolean validatesAgainstDatabase() {
		return this.connectionProfileIsActive();
	}


	// ********** misc **********

	/**
	 * covariant override
	 */
	@Override
	public JavaEntity getParent() {
		return (JavaEntity) super.getParent();
	}

	protected JavaEntity getEntity() {
		return this.getParent();
	}

}