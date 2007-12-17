/*******************************************************************************
 * Copyright (c) 2006, 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.IMappingKeys;
import org.eclipse.jpt.core.internal.ITextRange;
import org.eclipse.jpt.core.internal.context.base.IBasicMapping;
import org.eclipse.jpt.core.internal.context.base.IGeneratedValue;
import org.eclipse.jpt.core.internal.context.base.IIdMapping;
import org.eclipse.jpt.core.internal.context.base.TemporalType;
import org.eclipse.jpt.core.internal.resource.orm.AttributeMapping;
import org.eclipse.jpt.core.internal.resource.orm.Id;
import org.eclipse.jpt.core.internal.resource.orm.OrmFactory;
import org.eclipse.jpt.core.internal.resource.orm.TypeMapping;
import org.eclipse.jpt.db.internal.Table;


public class XmlIdMapping extends XmlAttributeMapping
	implements IIdMapping
{
	protected final XmlColumn column;

//	protected IGeneratedValue generatedValue;
	
	protected TemporalType temporal;
	
	protected XmlTableGenerator tableGenerator;
	protected XmlSequenceGenerator sequenceGenerator;

	protected Id id;
	
	protected XmlIdMapping(XmlPersistentAttribute parent) {
		super(parent);
		this.column = new XmlColumn(this, this);
	}

	public XmlColumn getColumn() {
		return this.column;
	}

	public TemporalType getTemporal() {
		return this.temporal;
	}

	public void setTemporal(TemporalType newTemporal) {
		TemporalType oldTemporal = this.temporal;
		this.temporal = newTemporal;
		this.id.setTemporal(TemporalType.toOrmResourceModel(newTemporal));
		firePropertyChanged(IBasicMapping.TEMPORAL_PROPERTY, oldTemporal, newTemporal);
	}


//	public IGeneratedValue getGeneratedValue() {
//		return generatedValue;
//	}
//
//	public NotificationChain basicSetGeneratedValue(IGeneratedValue newGeneratedValue, NotificationChain msgs) {
//		IGeneratedValue oldGeneratedValue = generatedValue;
//		generatedValue = newGeneratedValue;
//		if (eNotificationRequired()) {
//			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OrmPackage.XML_ID__GENERATED_VALUE, oldGeneratedValue, newGeneratedValue);
//			if (msgs == null)
//				msgs = notification;
//			else
//				msgs.add(notification);
//		}
//		return msgs;
//	}
//
//	public void setGeneratedValue(IGeneratedValue newGeneratedValue) {
//		if (newGeneratedValue != generatedValue) {
//			NotificationChain msgs = null;
//			if (generatedValue != null)
//				msgs = ((InternalEObject) generatedValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OrmPackage.XML_ID__GENERATED_VALUE, null, msgs);
//			if (newGeneratedValue != null)
//				msgs = ((InternalEObject) newGeneratedValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OrmPackage.XML_ID__GENERATED_VALUE, null, msgs);
//			msgs = basicSetGeneratedValue(newGeneratedValue, msgs);
//			if (msgs != null)
//				msgs.dispatch();
//		}
//		else if (eNotificationRequired())
//			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.XML_ID__GENERATED_VALUE, newGeneratedValue, newGeneratedValue));
//	}

	public XmlSequenceGenerator addSequenceGenerator() {
		if (getSequenceGenerator() != null) {
			throw new IllegalStateException("sequenceGenerator already exists");
		}
		this.sequenceGenerator = new XmlSequenceGenerator(this);
		this.id.setSequenceGenerator(OrmFactory.eINSTANCE.createSequenceGenerator());
		firePropertyChanged(SEQUENCE_GENERATOR_PROPERTY, null, this.sequenceGenerator);
		return this.sequenceGenerator;
	}
	
	public void removeSequenceGenerator() {
		if (getSequenceGenerator() == null) {
			throw new IllegalStateException("sequenceGenerator does not exist, cannot be removed");
		}
		XmlSequenceGenerator oldSequenceGenerator = this.sequenceGenerator;
		this.sequenceGenerator = null;
		this.id.setSequenceGenerator(null);
		firePropertyChanged(SEQUENCE_GENERATOR_PROPERTY, oldSequenceGenerator, null);
	}
	
	public XmlSequenceGenerator getSequenceGenerator() {
		return this.sequenceGenerator;
	}

	protected void setSequenceGenerator(XmlSequenceGenerator newSequenceGenerator) {
		XmlSequenceGenerator oldSequenceGenerator = this.sequenceGenerator;
		this.sequenceGenerator = newSequenceGenerator;
		firePropertyChanged(SEQUENCE_GENERATOR_PROPERTY, oldSequenceGenerator, newSequenceGenerator);
	}

	public XmlTableGenerator addTableGenerator() {
		if (getTableGenerator() != null) {
			throw new IllegalStateException("tableGenerator already exists");
		}
		this.tableGenerator = new XmlTableGenerator(this);
		this.id.setTableGenerator(OrmFactory.eINSTANCE.createTableGenerator());
		firePropertyChanged(TABLE_GENERATOR_PROPERTY, null, this.tableGenerator);
		return this.tableGenerator;
	}
	
	public void removeTableGenerator() {
		if (getTableGenerator() == null) {
			throw new IllegalStateException("tableGenerator does not exist, cannot be removed");
		}
		XmlTableGenerator oldTableGenerator = this.tableGenerator;
		this.tableGenerator = null;
		this.id.setTableGenerator(null);
		firePropertyChanged(SEQUENCE_GENERATOR_PROPERTY, oldTableGenerator, null);	
	}
	
	public XmlTableGenerator getTableGenerator() {
		return this.tableGenerator;
	}

	protected void setTableGenerator(XmlTableGenerator newTableGenerator) {
		XmlTableGenerator oldTableGenerator = this.tableGenerator;
		this.tableGenerator = newTableGenerator;
		if (newTableGenerator != null) {
			this.id.setTableGenerator(OrmFactory.eINSTANCE.createTableGenerator());
		}
		else {
			this.id.setTableGenerator(null);
		}
		firePropertyChanged(TABLE_GENERATOR_PROPERTY, oldTableGenerator, newTableGenerator);
	}


	public String getKey() {
		return IMappingKeys.ID_ATTRIBUTE_MAPPING_KEY;
	}

	@Override
	protected void initializeOn(XmlAttributeMapping newMapping) {
		newMapping.initializeFromXmlIdMapping(this);
	}

	@Override
	public void initializeFromXmlBasicMapping(XmlBasicMapping oldMapping) {
		super.initializeFromXmlBasicMapping(oldMapping);
		setTemporal(oldMapping.getTemporal());
	}

	@Override
	public int xmlSequence() {
		return 0;
	}

	@Override
	public String primaryKeyColumnName() {
		return this.getColumn().getName();
	}

//	public IGeneratedValue createGeneratedValue() {
//		return OrmFactory.eINSTANCE.createXmlGeneratedValue();
//	}
//
//	public ISequenceGenerator createSequenceGenerator() {
//		return OrmFactory.eINSTANCE.createXmlSequenceGenerator();
//	}
//
//	public ITableGenerator createTableGenerator() {
//		return OrmFactory.eINSTANCE.createXmlTableGenerator();
//	}

	@Override
	public boolean isOverridableAttributeMapping() {
		return true;
	}

	@Override
	public boolean isIdMapping() {
		return true;
	}
	
	@Override
	public AttributeMapping addToResourceModel(TypeMapping typeMapping) {
		Id id = OrmFactory.eINSTANCE.createId();
		if (typeMapping.getAttributes() == null) {
			typeMapping.setAttributes(OrmFactory.eINSTANCE.createAttributes());
		}
		typeMapping.getAttributes().getIds().add(id);
		return id;
	}
	
	@Override
	public void removeFromResourceModel(TypeMapping typeMapping) {
		typeMapping.getAttributes().getIds().remove(this.id);
		if (typeMapping.getAttributes().isAllFeaturesUnset()) {
			typeMapping.setAttributes(null);
		}
	}

	public IGeneratedValue addGeneratedValue() {
		// TODO Auto-generated method stub
		return null;
	}


	public IGeneratedValue getGeneratedValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeGeneratedValue() {
		// TODO Auto-generated method stub
		
	}

	public String defaultTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Table dbTable(String tableName) {
		return typeMapping().dbTable(tableName);
	}

	public String defaultColumnName() {
		// TODO Auto-generated method stub
		return null;
	}

	public ITextRange validationTextRange(CompilationUnit astRoot) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void initialize(Id id) {
		this.id = id;
		this.temporal = this.specifiedTemporal(id);
		this.column.initialize(id);
		this.initializeSequenceGenerator(id);
		this.initializeTableGenerator(id);		
	}
	
	protected void initializeSequenceGenerator(Id id) {
		if (id.getSequenceGenerator() != null) {
			this.sequenceGenerator = new XmlSequenceGenerator(this);
			this.sequenceGenerator.initialize(id.getSequenceGenerator());
		}
	}
	
	protected void initializeTableGenerator(Id id) {
		if (id.getTableGenerator() != null) {
			this.tableGenerator = new XmlTableGenerator(this);
			this.tableGenerator.initialize(id.getTableGenerator());
		}
	}
	
	public void update(Id id) {
		this.id = id;
		this.setTemporal(this.specifiedTemporal(id));
		this.column.update(id);
		this.updateSequenceGenerator(id);
		this.updateTableGenerator(id);		
	}
	
	protected void updateSequenceGenerator(Id id) {
		if (id.getSequenceGenerator() == null) {
			if (getSequenceGenerator() != null) {
				setSequenceGenerator(null);
			}
		}
		else {
			if (getSequenceGenerator() == null) {
				setSequenceGenerator(new XmlSequenceGenerator(this));
				getSequenceGenerator().initialize(id.getSequenceGenerator());
			}
			else {
				getSequenceGenerator().update(id.getSequenceGenerator());
			}
		}
	}
	
	protected void updateTableGenerator(Id id) {
		if (id.getTableGenerator() == null) {
			if (getTableGenerator() != null) {
				setTableGenerator(null);
			}
		}
		else {
			if (getTableGenerator() == null) {
				setTableGenerator(new XmlTableGenerator(this));
				getTableGenerator().initialize(id.getTableGenerator());
			}
			else {
				getTableGenerator().update(id.getTableGenerator());
			}
		}
	}
	

	protected TemporalType specifiedTemporal(Id id) {
		return TemporalType.fromOrmResourceModel(id.getTemporal());
	}

}
