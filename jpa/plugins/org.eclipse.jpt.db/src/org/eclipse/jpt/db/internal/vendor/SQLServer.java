/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.db.internal.vendor;

import java.util.ArrayList;

import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.jpt.utility.internal.StringTools;

class SQLServer
	extends AbstractVendor
{
	// singleton
	private static final Vendor INSTANCE = new SQLServer();

	/**
	 * Return the singleton.
	 */
	static Vendor instance() {
		return INSTANCE;
	}

	/**
	 * Ensure single instance.
	 */
	private SQLServer() {
		super();
	}

	@Override
	public String getDTPVendorName() {
		return "SQL Server"; //$NON-NLS-1$
	}

	@Override
	CatalogStrategy getCatalogStrategy() {
		return SimpleCatalogStrategy.instance();
	}

	/**
	 * By default, SQL Server identifiers are case-sensitive, even without
	 * delimiters. This can depend on the collation setting....
	 */
	@Override
	FoldingStrategy getFoldingStrategy() {
		return NonFoldingStrategy.instance();
	}

	/**
	 * SQL Server will use the user-requested database; if that database is not
	 * found, it will default to 'master'.
	 */
	@Override
	void addDefaultCatalogNamesTo(Database database, String userName, ArrayList<String> names) {
		names.add(database.getName());
		names.add(MASTER_CATALOG_IDENTIFIER);
	}
	private static final String MASTER_CATALOG_IDENTIFIER = "master";  //$NON-NLS-1$

	/**
	 * The default schema on SQL Server for any database (catalog) is 'dbo'.
	 */
	@Override
	void addDefaultSchemaNamesTo(Database database, String userName, ArrayList<String> names) {
		names.add(DEFAULT_SCHEMA_NAME);
	}
	private static final String DEFAULT_SCHEMA_NAME = "dbo";  //$NON-NLS-1$

	@Override
	char[] getExtendedRegularNameStartCharacters() {
		return EXTENDED_REGULAR_NAME_START_CHARACTERS;
	}
	private static final char[] EXTENDED_REGULAR_NAME_START_CHARACTERS = new char[] { '_', '@', '#' };

	@Override
	char[] getExtendedRegularNamePartCharacters() {
		return EXTENDED_REGULAR_NAME_PART_CHARACTERS;
	}
	private static final char[] EXTENDED_REGULAR_NAME_PART_CHARACTERS = new char[] { '$' };

	/**
	 * By default, SQL Server delimits identifiers with brackets ([]); but it
	 * can also be configured to use double-quotes.
	 */
	@Override
	boolean identifierIsDelimited(String identifier) {
		return StringTools.stringIsBracketed(identifier)
					|| super.identifierIsDelimited(identifier);
	}

}