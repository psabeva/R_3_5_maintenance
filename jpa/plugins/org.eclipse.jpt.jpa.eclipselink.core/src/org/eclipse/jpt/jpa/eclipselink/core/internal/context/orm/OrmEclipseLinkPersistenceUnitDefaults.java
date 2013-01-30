/*******************************************************************************
 * Copyright (c) 2006, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal.context.orm;

import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.utility.internal.iterable.EmptyIterable;
import org.eclipse.jpt.common.utility.internal.iterable.EmptyListIterable;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.common.utility.iterable.ListIterable;
import org.eclipse.jpt.jpa.core.context.DiscriminatorType;
import org.eclipse.jpt.jpa.core.context.ReadOnlyNamedColumn;
import org.eclipse.jpt.jpa.core.context.ReadOnlyNamedDiscriminatorColumn;
import org.eclipse.jpt.jpa.core.context.orm.OrmPersistenceUnitMetadata;
import org.eclipse.jpt.jpa.core.internal.context.JptValidator;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.orm.AbstractOrmPersistenceUnitDefaults;
import org.eclipse.jpt.jpa.db.Table;
import org.eclipse.jpt.jpa.eclipselink.core.context.ReadOnlyTenantDiscriminatorColumn2_3;
import org.eclipse.jpt.jpa.eclipselink.core.context.orm.EclipseLinkPersistenceUnitDefaults;
import org.eclipse.jpt.jpa.eclipselink.core.context.orm.OrmTenantDiscriminatorColumn2_3;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.EclipseLinkOrmFactory;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlAccessMethods;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlPersistenceUnitDefaults;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlTenantDiscriminatorColumn;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.v2_3.XmlTenantDiscriminatorColumn_2_3;

/**
 * EclipseLink <code>orm.xml</code> file
 * <br>
 * <code>persistence-unit-defaults</code> element
 */
public class OrmEclipseLinkPersistenceUnitDefaults
	extends AbstractOrmPersistenceUnitDefaults
	implements EclipseLinkPersistenceUnitDefaults
{

	protected final ContextListContainer<OrmTenantDiscriminatorColumn2_3, XmlTenantDiscriminatorColumn_2_3> tenantDiscriminatorColumnContainer;
	protected final ReadOnlyTenantDiscriminatorColumn2_3.Owner tenantDiscriminatorColumnOwner;

	protected String specifiedGetMethod;
	protected String specifiedSetMethod;
	
	// ********** constructor/initialization **********

	public OrmEclipseLinkPersistenceUnitDefaults(OrmPersistenceUnitMetadata parent) {
		super(parent);
		this.tenantDiscriminatorColumnOwner = this.buildTenantDiscriminatorColumnOwner();
		this.tenantDiscriminatorColumnContainer = this.buildTenantDiscriminatorColumnContainer();
		this.specifiedGetMethod = this.buildSpecifiedGetMethod();
		this.specifiedSetMethod = this.buildSpecifiedSetMethod();
	}

	@Override
	protected XmlPersistenceUnitDefaults buildXmlPersistenceUnitDefaults() {
		return EclipseLinkOrmFactory.eINSTANCE.createXmlPersistenceUnitDefaults();
	}

	@Override
	protected XmlPersistenceUnitDefaults getXmlDefaults() {
		return (XmlPersistenceUnitDefaults) super.getXmlDefaults();
	}

	@Override
	protected XmlPersistenceUnitDefaults getXmlDefaultsForUpdate() {
		return (XmlPersistenceUnitDefaults) super.getXmlDefaultsForUpdate();
	}

	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.syncTenantDiscriminatorColumns();
		this.setSpecifiedGetMethod_(this.buildSpecifiedGetMethod());
		this.setSpecifiedSetMethod_(this.buildSpecifiedSetMethod());
	}

	@Override
	public void update() {
		super.update();
		this.updateNodes(this.getTenantDiscriminatorColumns());
	}


	// ********** tenant discriminator columns **********

	public ListIterable<OrmTenantDiscriminatorColumn2_3> getTenantDiscriminatorColumns() {
		return this.tenantDiscriminatorColumnContainer.getContextElements();
	}

	public int getTenantDiscriminatorColumnsSize() {
		return this.tenantDiscriminatorColumnContainer.getContextElementsSize();
	}

	public boolean hasTenantDiscriminatorColumns() {
		return this.getTenantDiscriminatorColumnsSize() != 0;
	}

	public OrmTenantDiscriminatorColumn2_3 getTenantDiscriminatorColumn(int index) {
		return this.tenantDiscriminatorColumnContainer.getContextElement(index);
	}

	public OrmTenantDiscriminatorColumn2_3 addTenantDiscriminatorColumn() {
		return this.addTenantDiscriminatorColumn(this.getTenantDiscriminatorColumnsSize());
	}

	public OrmTenantDiscriminatorColumn2_3 addTenantDiscriminatorColumn(int index) {
		XmlTenantDiscriminatorColumn xmlJoinColumn = this.buildXmlTenantDiscriminatorColumn();
		OrmTenantDiscriminatorColumn2_3 joinColumn = this.tenantDiscriminatorColumnContainer.addContextElement(index, xmlJoinColumn);
		this.getXmlDefaultsForUpdate().getTenantDiscriminatorColumns().add(index, xmlJoinColumn);
		return joinColumn;
	}

	protected XmlTenantDiscriminatorColumn buildXmlTenantDiscriminatorColumn() {
		return EclipseLinkOrmFactory.eINSTANCE.createXmlTenantDiscriminatorColumn();
	}

	public void removeTenantDiscriminatorColumn(OrmTenantDiscriminatorColumn2_3 tenantDiscriminatorColumn) {
		this.removeTenantDiscriminatorColumn(this.tenantDiscriminatorColumnContainer.indexOfContextElement(tenantDiscriminatorColumn));
	}

	public void removeTenantDiscriminatorColumn(int index) {
		this.tenantDiscriminatorColumnContainer.removeContextElement(index);
		this.getXmlDefaults().getTenantDiscriminatorColumns().remove(index);
	}

	public void moveTenantDiscriminatorColumn(int targetIndex, int sourceIndex) {
		this.tenantDiscriminatorColumnContainer.moveContextElement(targetIndex, sourceIndex);
		this.getXmlDefaults().getTenantDiscriminatorColumns().move(targetIndex, sourceIndex);
	}

	protected void syncTenantDiscriminatorColumns() {
		this.tenantDiscriminatorColumnContainer.synchronizeWithResourceModel();
	}

	protected ListIterable<XmlTenantDiscriminatorColumn_2_3> getXmlTenantDiscriminatorColumns() {
		if (getXmlDefaults() == null) {
			return EmptyListIterable.instance();
		}
		// clone to reduce chance of concurrency problems
		return IterableTools.cloneLive(this.getXmlDefaults().getTenantDiscriminatorColumns());
	}

	/**
	 *  tenant discriminator column container
	 */
	protected class TenantDiscriminatorColumnContainer
		extends ContextListContainer<OrmTenantDiscriminatorColumn2_3, XmlTenantDiscriminatorColumn_2_3>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return TENANT_DISCRIMINATOR_COLUMNS_LIST;
		}
		@Override
		protected OrmTenantDiscriminatorColumn2_3 buildContextElement(XmlTenantDiscriminatorColumn_2_3 resourceElement) {
			return OrmEclipseLinkPersistenceUnitDefaults.this.buildTenantDiscriminatorColumn(resourceElement);
		}
		@Override
		protected ListIterable<XmlTenantDiscriminatorColumn_2_3> getResourceElements() {
			return OrmEclipseLinkPersistenceUnitDefaults.this.getXmlTenantDiscriminatorColumns();
		}
		@Override
		protected XmlTenantDiscriminatorColumn_2_3 getResourceElement(OrmTenantDiscriminatorColumn2_3 contextElement) {
			return contextElement.getXmlColumn();
		}
	}

	protected ReadOnlyTenantDiscriminatorColumn2_3.Owner buildTenantDiscriminatorColumnOwner() {
		return new TenantDiscriminatorColumnOwner();
	}

	protected OrmTenantDiscriminatorColumn2_3 buildTenantDiscriminatorColumn(XmlTenantDiscriminatorColumn_2_3 xmlTenantDiscriminatorColumn) {
		return new EclipseLinkOrmTenantDiscriminatorColumn2_3(this, this.tenantDiscriminatorColumnOwner, xmlTenantDiscriminatorColumn);
	}

	protected ContextListContainer<OrmTenantDiscriminatorColumn2_3, XmlTenantDiscriminatorColumn_2_3> buildTenantDiscriminatorColumnContainer() {
		TenantDiscriminatorColumnContainer container = new TenantDiscriminatorColumnContainer();
		container.initialize();
		return container;
	}


	// ********** OrmReadOnlyTenantDiscriminatorColumn.Owner implementation **********

	protected class TenantDiscriminatorColumnOwner 
		implements ReadOnlyTenantDiscriminatorColumn2_3.Owner
	{

		public String getDefaultContextPropertyName() {
			return ReadOnlyTenantDiscriminatorColumn2_3.DEFAULT_CONTEXT_PROPERTY;
		}

		public boolean getDefaultPrimaryKey() {
			return ReadOnlyTenantDiscriminatorColumn2_3.DEFAULT_PRIMARY_KEY;
		}

		public int getDefaultLength() {
			return ReadOnlyNamedDiscriminatorColumn.DEFAULT_LENGTH;
		}

		public DiscriminatorType getDefaultDiscriminatorType() {
			return ReadOnlyNamedDiscriminatorColumn.DEFAULT_DISCRIMINATOR_TYPE;
		}

		/**
		 * No default table name in the context of the persistence unit defaults
		 */
		public String getDefaultTableName() {
			return null;
		}

		public String getDefaultColumnName(ReadOnlyNamedColumn column) {
			return ReadOnlyTenantDiscriminatorColumn2_3.DEFAULT_NAME;
		}

		/**
		 * No table in the context of the persistence unit defaults
		 */
		public Table resolveDbTable(String tableName) {
			return null;
		}

		public Iterable<String> getCandidateTableNames() {
			return EmptyIterable.instance();
		}

		public boolean tableNameIsInvalid(String tableName) {
			return false;
		}

		/**
		 * no column validation can be done in the context of the persistence unit defaults
		 */
		public JptValidator buildColumnValidator(ReadOnlyNamedColumn column) {
			return JptValidator.Null.instance();
		}

		public TextRange getValidationTextRange() {
			return OrmEclipseLinkPersistenceUnitDefaults.this.getValidationTextRange();
		}
	}


	//*************** get method *****************

	public String getGetMethod() {
		return this.getSpecifiedGetMethod();
	}

	public String getDefaultGetMethod() {
		return null;
	}

	public String getSpecifiedGetMethod() {
		return this.specifiedGetMethod;
	}

	public void setSpecifiedGetMethod(String getMethod) {
		if (this.valuesAreDifferent(this.specifiedGetMethod, getMethod)) {
			XmlAccessMethods xmlAccessMethods = this.getXmlAccessMethodsForUpdate();
			this.setSpecifiedGetMethod_(getMethod);
			xmlAccessMethods.setGetMethod(getMethod);
			this.removeXmlAccessMethodsIfUnset();
		}
	}

	protected void setSpecifiedGetMethod_(String getMethod) {
		String old = this.specifiedGetMethod;
		this.specifiedGetMethod = getMethod;
		this.firePropertyChanged(SPECIFIED_GET_METHOD_PROPERTY, old, getMethod);
	}

	protected String buildSpecifiedGetMethod() {
		XmlAccessMethods accessMethods = this.getXmlAccessMethods();
		return accessMethods != null ? accessMethods.getGetMethod() : null;
	}


	//*************** set method *****************

	public String getSetMethod() {
		return this.getSpecifiedSetMethod();
	}

	public String getDefaultSetMethod() {
		return null;
	}

	public String getSpecifiedSetMethod() {
		return this.specifiedSetMethod;
	}

	public void setSpecifiedSetMethod(String setMethod) {
		if (this.valuesAreDifferent(this.specifiedSetMethod, setMethod)) {
			XmlAccessMethods xmlAccessMethods = this.getXmlAccessMethodsForUpdate();
			this.setSpecifiedSetMethod_(setMethod);
			xmlAccessMethods.setSetMethod(setMethod);
			this.removeXmlAccessMethodsIfUnset();
		}
	}

	protected void setSpecifiedSetMethod_(String setMethod) {
		String old = this.specifiedSetMethod;
		this.specifiedSetMethod = setMethod;
		this.firePropertyChanged(SPECIFIED_SET_METHOD_PROPERTY, old, setMethod);
	}

	protected String buildSpecifiedSetMethod() {
		XmlAccessMethods accessMethods = this.getXmlAccessMethods();
		return accessMethods != null ? accessMethods.getSetMethod() : null;
	}


	//*************** XML access methods *****************

	protected XmlAccessMethods getXmlAccessMethods() {
		XmlPersistenceUnitDefaults xmlDefaults = getXmlDefaults();
		return xmlDefaults != null ? getXmlDefaults().getAccessMethods() : null;
	}

	/**
	 * Build the XML access methods (and XML defaults and XML metadata if necessary) if it does not exist.
	 */
	protected XmlAccessMethods getXmlAccessMethodsForUpdate() {
		XmlPersistenceUnitDefaults xmlDefaults = this.getXmlDefaultsForUpdate();
		XmlAccessMethods xmlAccessMethods = xmlDefaults.getAccessMethods();
		return (xmlAccessMethods != null) ? xmlAccessMethods : this.buildXmlAccessMethods();
	}

	protected XmlAccessMethods buildXmlAccessMethods() {
		XmlAccessMethods xmlAccessMethods = this.buildXmlAccessMethods_();
		this.getXmlDefaults().setAccessMethods(xmlAccessMethods);
		return xmlAccessMethods;
	}

	protected XmlAccessMethods buildXmlAccessMethods_() {
		return EclipseLinkOrmFactory.eINSTANCE.createXmlAccessMethods();
	}

	/**
	 * clear the XML access methods (and the XML defaults and XML metadata) if appropriate
	 */
	protected void removeXmlAccessMethodsIfUnset() {
		if (this.getXmlAccessMethods().isUnset()) {
			this.getXmlDefaults().setAccessMethods(null);
			this.removeXmlDefaultsIfUnset();
		}
	}

	// ********** completion proposals **********

	@Override
	public Iterable<String> getCompletionProposals(int pos) {
		Iterable<String> result = super.getCompletionProposals(pos);
		if (result != null) {
			return result;
		}
		for (OrmTenantDiscriminatorColumn2_3 tenantDiscriminatorColumn : this.getTenantDiscriminatorColumns()) {
			result = tenantDiscriminatorColumn.getCompletionProposals(pos);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
}
