/*******************************************************************************
 * Copyright (c) 2006, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import java.util.Iterator;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.ReadOnlyTable;
import org.eclipse.jpt.core.context.java.JavaEmbeddable;
import org.eclipse.jpt.core.context.java.JavaPersistentType;
import org.eclipse.jpt.core.context.orm.OrmEmbeddable;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.resource.orm.XmlEmbeddable;
import org.eclipse.jpt.core.resource.orm.XmlEntityMappings;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;

/**
 * <code>orm.xml</code> embeddable type mapping
 */
public abstract class AbstractOrmEmbeddable<X extends XmlEmbeddable>
	extends AbstractOrmTypeMapping<X>
	implements OrmEmbeddable
{
	protected AbstractOrmEmbeddable(OrmPersistentType parent, X resourceMapping) {
		super(parent, resourceMapping);
	}


	// ********** key **********

	public String getKey() {
		return MappingKeys.EMBEDDABLE_TYPE_MAPPING_KEY;
	}


	// ********** id class **********

	public JavaPersistentType getIdClass() {
		return null;
	}


	// ********** tables **********

	public Iterator<ReadOnlyTable> associatedTables() {
		return EmptyIterator.instance();
	}

	public Iterator<ReadOnlyTable> allAssociatedTables() {
		return EmptyIterator.instance();
	}

	public Iterator<String> allAssociatedTableNames() {
		return EmptyIterator.instance();
	}

	public boolean tableNameIsInvalid(String tableName) {
		return false;
	}


	// ********** Java **********

	@Override
	public JavaEmbeddable getJavaTypeMapping() {
		return (JavaEmbeddable) super.getJavaTypeMapping();
	}

	@Override
	public JavaEmbeddable getJavaTypeMappingForDefaults() {
		return (JavaEmbeddable) super.getJavaTypeMappingForDefaults();
	}


	// ********** entity mappings **********

	public int getXmlSequence() {
		return 2;
	}

	public void addXmlTypeMappingTo(XmlEntityMappings entityMappings) {
		entityMappings.getEmbeddables().add(this.xmlTypeMapping);
	}

	public void removeXmlTypeMappingFrom(XmlEntityMappings entityMappings) {
		entityMappings.getEmbeddables().remove(this.xmlTypeMapping);
	}


	// ********** validation **********

	@Override
	public boolean validatesAgainstDatabase() {
		return false;
	}
}
