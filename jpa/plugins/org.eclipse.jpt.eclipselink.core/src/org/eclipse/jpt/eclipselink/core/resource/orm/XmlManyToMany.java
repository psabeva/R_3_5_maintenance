/*******************************************************************************
 * Copyright (c) 2008, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/

package org.eclipse.jpt.eclipselink.core.resource.orm;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jpt.core.internal.utility.translators.SimpleTranslator;
import org.eclipse.jpt.core.resource.orm.CascadeType;
import org.eclipse.jpt.core.resource.orm.XmlAssociationOverride;
import org.eclipse.jpt.core.resource.orm.XmlAttributeOverride;
import org.eclipse.jpt.core.resource.orm.v2_0.JPA2_0;
import org.eclipse.jpt.core.resource.orm.v2_0.OrmV2_0Package;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.eclipselink.core.resource.orm.v2_0.EclipseLink2_0;
import org.eclipse.jpt.eclipselink.core.resource.orm.v2_0.EclipseLinkOrmV2_0Package;
import org.eclipse.jpt.eclipselink.core.resource.orm.v2_0.XmlCollectionMapping_2_0;
import org.eclipse.jpt.eclipselink.core.resource.orm.v2_0.XmlManyToMany_2_0;
import org.eclipse.jpt.eclipselink.core.resource.orm.v2_0.XmlMapKeyAssociationOverrideContainer_2_0;
import org.eclipse.jpt.eclipselink.core.resource.orm.v2_1.EclipseLink2_1;
import org.eclipse.jpt.eclipselink.core.resource.orm.v2_1.EclipseLinkOrmV2_1Package;
import org.eclipse.jpt.eclipselink.core.resource.orm.v2_1.XmlManyToMany_2_1;
import org.eclipse.jpt.eclipselink.core.resource.orm.v2_2.EclipseLink2_2;
import org.eclipse.jpt.eclipselink.core.resource.orm.v2_2.EclipseLinkOrmV2_2Package;
import org.eclipse.jpt.eclipselink.core.resource.orm.v2_2.XmlManyToMany_2_2;
import org.eclipse.wst.common.internal.emf.resource.Translator;

/**
 * <!-- begin-user-doc -->
 * 
 * A representation of the model object '<em><b>Xml Many To Many</b></em>'.
 *  
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 2.3
 * @since 2.1
 * 
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage#getXmlManyToMany()
 * @model kind="class"
 * @generated
 */
public class XmlManyToMany extends org.eclipse.jpt.core.resource.orm.XmlManyToMany implements XmlManyToMany_2_0, XmlManyToMany_2_1, XmlManyToMany_2_2, XmlAttributeMapping, XmlJoinFetch
{
	/**
	 * The cached value of the '{@link #getConverter() <em>Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConverter()
	 * @generated
	 * @ordered
	 */
	protected XmlConverter converter;

	/**
	 * The cached value of the '{@link #getTypeConverter() <em>Type Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeConverter()
	 * @generated
	 * @ordered
	 */
	protected XmlTypeConverter typeConverter;

	/**
	 * The cached value of the '{@link #getObjectTypeConverter() <em>Object Type Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectTypeConverter()
	 * @generated
	 * @ordered
	 */
	protected XmlObjectTypeConverter objectTypeConverter;

	/**
	 * The cached value of the '{@link #getStructConverter() <em>Struct Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStructConverter()
	 * @generated
	 * @ordered
	 */
	protected XmlStructConverter structConverter;

	/**
	 * The cached value of the '{@link #getMapKeyAssociationOverrides() <em>Map Key Association Overrides</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyAssociationOverrides()
	 * @generated
	 * @ordered
	 */
	protected EList<XmlAssociationOverride> mapKeyAssociationOverrides;

	/**
	 * The default value of the '{@link #getMapKeyConvert() <em>Map Key Convert</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyConvert()
	 * @generated
	 * @ordered
	 */
	protected static final String MAP_KEY_CONVERT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMapKeyConvert() <em>Map Key Convert</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMapKeyConvert()
	 * @generated
	 * @ordered
	 */
	protected String mapKeyConvert = MAP_KEY_CONVERT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBatchFetch() <em>Batch Fetch</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBatchFetch()
	 * @generated
	 * @ordered
	 */
	protected XmlBatchFetch batchFetch;

	/**
	 * The default value of the '{@link #getAttributeType() <em>Attribute Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributeType()
	 * @generated
	 * @ordered
	 */
	protected static final String ATTRIBUTE_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAttributeType() <em>Attribute Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributeType()
	 * @generated
	 * @ordered
	 */
	protected String attributeType = ATTRIBUTE_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getCascadeOnDelete() <em>Cascade On Delete</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCascadeOnDelete()
	 * @generated
	 * @ordered
	 */
	protected static final Boolean CASCADE_ON_DELETE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCascadeOnDelete() <em>Cascade On Delete</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCascadeOnDelete()
	 * @generated
	 * @ordered
	 */
	protected Boolean cascadeOnDelete = CASCADE_ON_DELETE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAccessMethods() <em>Access Methods</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccessMethods()
	 * @generated
	 * @ordered
	 */
	protected XmlAccessMethods accessMethods;

	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EList<XmlProperty> properties;

	/**
	 * changed this to null and removed the generated flag so emf won't generate over it
	 * we don't want a default for enums, just null if the tag does not exist
	 */
	protected static final XmlJoinFetchType JOIN_FETCH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getJoinFetch() <em>Join Fetch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJoinFetch()
	 * @generated
	 * @ordered
	 */
	protected XmlJoinFetchType joinFetch = JOIN_FETCH_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected XmlManyToMany()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass()
	{
		return EclipseLinkOrmPackage.Literals.XML_MANY_TO_MANY;
	}

	/**
	 * Returns the value of the '<em><b>Join Fetch</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.jpt.eclipselink.core.resource.orm.XmlJoinFetchType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Join Fetch</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Join Fetch</em>' attribute.
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.XmlJoinFetchType
	 * @see #setJoinFetch(XmlJoinFetchType)
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage#getXmlJoinFetch_JoinFetch()
	 * @model
	 * @generated
	 */
	public XmlJoinFetchType getJoinFetch()
	{
		return joinFetch;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.resource.orm.XmlManyToMany#getJoinFetch <em>Join Fetch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Join Fetch</em>' attribute.
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.XmlJoinFetchType
	 * @see #getJoinFetch()
	 * @generated
	 */
	public void setJoinFetch(XmlJoinFetchType newJoinFetch)
	{
		XmlJoinFetchType oldJoinFetch = joinFetch;
		joinFetch = newJoinFetch == null ? JOIN_FETCH_EDEFAULT : newJoinFetch;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__JOIN_FETCH, oldJoinFetch, joinFetch));
	}

	/**
	 * Returns the value of the '<em><b>Converter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Converter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Converter</em>' containment reference.
	 * @see #setConverter(XmlConverter)
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage#getXmlConverterHolder_Converter()
	 * @model containment="true"
	 * @generated
	 */
	public XmlConverter getConverter()
	{
		return converter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConverter(XmlConverter newConverter, NotificationChain msgs)
	{
		XmlConverter oldConverter = converter;
		converter = newConverter;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__CONVERTER, oldConverter, newConverter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.resource.orm.XmlManyToMany#getConverter <em>Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Converter</em>' containment reference.
	 * @see #getConverter()
	 * @generated
	 */
	public void setConverter(XmlConverter newConverter)
	{
		if (newConverter != converter)
		{
			NotificationChain msgs = null;
			if (converter != null)
				msgs = ((InternalEObject)converter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EclipseLinkOrmPackage.XML_MANY_TO_MANY__CONVERTER, null, msgs);
			if (newConverter != null)
				msgs = ((InternalEObject)newConverter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EclipseLinkOrmPackage.XML_MANY_TO_MANY__CONVERTER, null, msgs);
			msgs = basicSetConverter(newConverter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__CONVERTER, newConverter, newConverter));
	}

	/**
	 * Returns the value of the '<em><b>Type Converter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Converter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Converter</em>' containment reference.
	 * @see #setTypeConverter(XmlTypeConverter)
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage#getXmlConverterHolder_TypeConverter()
	 * @model containment="true"
	 * @generated
	 */
	public XmlTypeConverter getTypeConverter()
	{
		return typeConverter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTypeConverter(XmlTypeConverter newTypeConverter, NotificationChain msgs)
	{
		XmlTypeConverter oldTypeConverter = typeConverter;
		typeConverter = newTypeConverter;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__TYPE_CONVERTER, oldTypeConverter, newTypeConverter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.resource.orm.XmlManyToMany#getTypeConverter <em>Type Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Converter</em>' containment reference.
	 * @see #getTypeConverter()
	 * @generated
	 */
	public void setTypeConverter(XmlTypeConverter newTypeConverter)
	{
		if (newTypeConverter != typeConverter)
		{
			NotificationChain msgs = null;
			if (typeConverter != null)
				msgs = ((InternalEObject)typeConverter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EclipseLinkOrmPackage.XML_MANY_TO_MANY__TYPE_CONVERTER, null, msgs);
			if (newTypeConverter != null)
				msgs = ((InternalEObject)newTypeConverter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EclipseLinkOrmPackage.XML_MANY_TO_MANY__TYPE_CONVERTER, null, msgs);
			msgs = basicSetTypeConverter(newTypeConverter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__TYPE_CONVERTER, newTypeConverter, newTypeConverter));
	}

	/**
	 * Returns the value of the '<em><b>Object Type Converter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object Type Converter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object Type Converter</em>' containment reference.
	 * @see #setObjectTypeConverter(XmlObjectTypeConverter)
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage#getXmlConverterHolder_ObjectTypeConverter()
	 * @model containment="true"
	 * @generated
	 */
	public XmlObjectTypeConverter getObjectTypeConverter()
	{
		return objectTypeConverter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetObjectTypeConverter(XmlObjectTypeConverter newObjectTypeConverter, NotificationChain msgs)
	{
		XmlObjectTypeConverter oldObjectTypeConverter = objectTypeConverter;
		objectTypeConverter = newObjectTypeConverter;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__OBJECT_TYPE_CONVERTER, oldObjectTypeConverter, newObjectTypeConverter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.resource.orm.XmlManyToMany#getObjectTypeConverter <em>Object Type Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Object Type Converter</em>' containment reference.
	 * @see #getObjectTypeConverter()
	 * @generated
	 */
	public void setObjectTypeConverter(XmlObjectTypeConverter newObjectTypeConverter)
	{
		if (newObjectTypeConverter != objectTypeConverter)
		{
			NotificationChain msgs = null;
			if (objectTypeConverter != null)
				msgs = ((InternalEObject)objectTypeConverter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EclipseLinkOrmPackage.XML_MANY_TO_MANY__OBJECT_TYPE_CONVERTER, null, msgs);
			if (newObjectTypeConverter != null)
				msgs = ((InternalEObject)newObjectTypeConverter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EclipseLinkOrmPackage.XML_MANY_TO_MANY__OBJECT_TYPE_CONVERTER, null, msgs);
			msgs = basicSetObjectTypeConverter(newObjectTypeConverter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__OBJECT_TYPE_CONVERTER, newObjectTypeConverter, newObjectTypeConverter));
	}

	/**
	 * Returns the value of the '<em><b>Struct Converter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Struct Converter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Struct Converter</em>' containment reference.
	 * @see #setStructConverter(XmlStructConverter)
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage#getXmlConverterHolder_StructConverter()
	 * @model containment="true"
	 * @generated
	 */
	public XmlStructConverter getStructConverter()
	{
		return structConverter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStructConverter(XmlStructConverter newStructConverter, NotificationChain msgs)
	{
		XmlStructConverter oldStructConverter = structConverter;
		structConverter = newStructConverter;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__STRUCT_CONVERTER, oldStructConverter, newStructConverter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.resource.orm.XmlManyToMany#getStructConverter <em>Struct Converter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Struct Converter</em>' containment reference.
	 * @see #getStructConverter()
	 * @generated
	 */
	public void setStructConverter(XmlStructConverter newStructConverter)
	{
		if (newStructConverter != structConverter)
		{
			NotificationChain msgs = null;
			if (structConverter != null)
				msgs = ((InternalEObject)structConverter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EclipseLinkOrmPackage.XML_MANY_TO_MANY__STRUCT_CONVERTER, null, msgs);
			if (newStructConverter != null)
				msgs = ((InternalEObject)newStructConverter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EclipseLinkOrmPackage.XML_MANY_TO_MANY__STRUCT_CONVERTER, null, msgs);
			msgs = basicSetStructConverter(newStructConverter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__STRUCT_CONVERTER, newStructConverter, newStructConverter));
	}

	/**
	 * Returns the value of the '<em><b>Map Key Association Overrides</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.jpt.core.resource.orm.XmlAssociationOverride}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map Key Association Overrides</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Association Overrides</em>' containment reference list.
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage#getXmlMapKeyAssociationOverrideContainer_2_0_MapKeyAssociationOverrides()
	 * @model containment="true"
	 * @generated
	 */
	public EList<XmlAssociationOverride> getMapKeyAssociationOverrides()
	{
		if (mapKeyAssociationOverrides == null)
		{
			mapKeyAssociationOverrides = new EObjectContainmentEList<XmlAssociationOverride>(XmlAssociationOverride.class, this, EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES);
		}
		return mapKeyAssociationOverrides;
	}

	/**
	 * Returns the value of the '<em><b>Map Key Convert</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map Key Convert</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map Key Convert</em>' attribute.
	 * @see #setMapKeyConvert(String)
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage#getXmlCollectionMapping_2_0_MapKeyConvert()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
	public String getMapKeyConvert()
	{
		return mapKeyConvert;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.resource.orm.XmlManyToMany#getMapKeyConvert <em>Map Key Convert</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map Key Convert</em>' attribute.
	 * @see #getMapKeyConvert()
	 * @generated
	 */
	public void setMapKeyConvert(String newMapKeyConvert)
	{
		String oldMapKeyConvert = mapKeyConvert;
		mapKeyConvert = newMapKeyConvert;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_CONVERT, oldMapKeyConvert, mapKeyConvert));
	}

	/**
	 * Returns the value of the '<em><b>Batch Fetch</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Batch Fetch</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Batch Fetch</em>' containment reference.
	 * @see #setBatchFetch(XmlBatchFetch)
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage#getXmlBatchFetchHolder_BatchFetch()
	 * @model containment="true"
	 * @generated
	 */
	public XmlBatchFetch getBatchFetch()
	{
		return batchFetch;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBatchFetch(XmlBatchFetch newBatchFetch, NotificationChain msgs)
	{
		XmlBatchFetch oldBatchFetch = batchFetch;
		batchFetch = newBatchFetch;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__BATCH_FETCH, oldBatchFetch, newBatchFetch);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.resource.orm.XmlManyToMany#getBatchFetch <em>Batch Fetch</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Batch Fetch</em>' containment reference.
	 * @see #getBatchFetch()
	 * @generated
	 */
	public void setBatchFetch(XmlBatchFetch newBatchFetch)
	{
		if (newBatchFetch != batchFetch)
		{
			NotificationChain msgs = null;
			if (batchFetch != null)
				msgs = ((InternalEObject)batchFetch).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EclipseLinkOrmPackage.XML_MANY_TO_MANY__BATCH_FETCH, null, msgs);
			if (newBatchFetch != null)
				msgs = ((InternalEObject)newBatchFetch).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EclipseLinkOrmPackage.XML_MANY_TO_MANY__BATCH_FETCH, null, msgs);
			msgs = basicSetBatchFetch(newBatchFetch, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__BATCH_FETCH, newBatchFetch, newBatchFetch));
	}

	/**
	 * Returns the value of the '<em><b>Attribute Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attribute Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute Type</em>' attribute.
	 * @see #setAttributeType(String)
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage#getXmlManyToMany_2_1_AttributeType()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
	public String getAttributeType()
	{
		return attributeType;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.resource.orm.XmlManyToMany#getAttributeType <em>Attribute Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute Type</em>' attribute.
	 * @see #getAttributeType()
	 * @generated
	 */
	public void setAttributeType(String newAttributeType)
	{
		String oldAttributeType = attributeType;
		attributeType = newAttributeType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__ATTRIBUTE_TYPE, oldAttributeType, attributeType));
	}

	/**
	 * Returns the value of the '<em><b>Cascade On Delete</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cascade On Delete</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cascade On Delete</em>' attribute.
	 * @see #setCascadeOnDelete(Boolean)
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage#getXmlManyToMany_2_2_CascadeOnDelete()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.BooleanObject"
	 * @generated
	 */
	public Boolean getCascadeOnDelete()
	{
		return cascadeOnDelete;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.resource.orm.XmlManyToMany#getCascadeOnDelete <em>Cascade On Delete</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cascade On Delete</em>' attribute.
	 * @see #getCascadeOnDelete()
	 * @generated
	 */
	public void setCascadeOnDelete(Boolean newCascadeOnDelete)
	{
		Boolean oldCascadeOnDelete = cascadeOnDelete;
		cascadeOnDelete = newCascadeOnDelete;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__CASCADE_ON_DELETE, oldCascadeOnDelete, cascadeOnDelete));
	}

	/**
	 * Returns the value of the '<em><b>Access Methods</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Access Methods</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Access Methods</em>' containment reference.
	 * @see #setAccessMethods(XmlAccessMethods)
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage#getXmlAccessMethodsHolder_AccessMethods()
	 * @model containment="true"
	 * @generated
	 */
	public XmlAccessMethods getAccessMethods()
	{
		return accessMethods;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAccessMethods(XmlAccessMethods newAccessMethods, NotificationChain msgs)
	{
		XmlAccessMethods oldAccessMethods = accessMethods;
		accessMethods = newAccessMethods;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__ACCESS_METHODS, oldAccessMethods, newAccessMethods);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink.core.resource.orm.XmlManyToMany#getAccessMethods <em>Access Methods</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Access Methods</em>' containment reference.
	 * @see #getAccessMethods()
	 * @generated
	 */
	public void setAccessMethods(XmlAccessMethods newAccessMethods)
	{
		if (newAccessMethods != accessMethods)
		{
			NotificationChain msgs = null;
			if (accessMethods != null)
				msgs = ((InternalEObject)accessMethods).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EclipseLinkOrmPackage.XML_MANY_TO_MANY__ACCESS_METHODS, null, msgs);
			if (newAccessMethods != null)
				msgs = ((InternalEObject)newAccessMethods).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EclipseLinkOrmPackage.XML_MANY_TO_MANY__ACCESS_METHODS, null, msgs);
			msgs = basicSetAccessMethods(newAccessMethods, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EclipseLinkOrmPackage.XML_MANY_TO_MANY__ACCESS_METHODS, newAccessMethods, newAccessMethods));
	}

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.jpt.eclipselink.core.resource.orm.XmlProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' containment reference list.
	 * @see org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmPackage#getXmlPropertyContainer_Properties()
	 * @model containment="true"
	 * @generated
	 */
	public EList<XmlProperty> getProperties()
	{
		if (properties == null)
		{
			properties = new EObjectContainmentEList<XmlProperty>(XmlProperty.class, this, EclipseLinkOrmPackage.XML_MANY_TO_MANY__PROPERTIES);
		}
		return properties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
	{
		switch (featureID)
		{
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__CONVERTER:
				return basicSetConverter(null, msgs);
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__TYPE_CONVERTER:
				return basicSetTypeConverter(null, msgs);
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__OBJECT_TYPE_CONVERTER:
				return basicSetObjectTypeConverter(null, msgs);
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__STRUCT_CONVERTER:
				return basicSetStructConverter(null, msgs);
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES:
				return ((InternalEList<?>)getMapKeyAssociationOverrides()).basicRemove(otherEnd, msgs);
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__BATCH_FETCH:
				return basicSetBatchFetch(null, msgs);
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__ACCESS_METHODS:
				return basicSetAccessMethods(null, msgs);
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__PROPERTIES:
				return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType)
	{
		switch (featureID)
		{
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__CONVERTER:
				return getConverter();
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__TYPE_CONVERTER:
				return getTypeConverter();
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__OBJECT_TYPE_CONVERTER:
				return getObjectTypeConverter();
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__STRUCT_CONVERTER:
				return getStructConverter();
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES:
				return getMapKeyAssociationOverrides();
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_CONVERT:
				return getMapKeyConvert();
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__BATCH_FETCH:
				return getBatchFetch();
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__ATTRIBUTE_TYPE:
				return getAttributeType();
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__CASCADE_ON_DELETE:
				return getCascadeOnDelete();
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__ACCESS_METHODS:
				return getAccessMethods();
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__PROPERTIES:
				return getProperties();
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__JOIN_FETCH:
				return getJoinFetch();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__CONVERTER:
				setConverter((XmlConverter)newValue);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__TYPE_CONVERTER:
				setTypeConverter((XmlTypeConverter)newValue);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__OBJECT_TYPE_CONVERTER:
				setObjectTypeConverter((XmlObjectTypeConverter)newValue);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__STRUCT_CONVERTER:
				setStructConverter((XmlStructConverter)newValue);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES:
				getMapKeyAssociationOverrides().clear();
				getMapKeyAssociationOverrides().addAll((Collection<? extends XmlAssociationOverride>)newValue);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_CONVERT:
				setMapKeyConvert((String)newValue);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__BATCH_FETCH:
				setBatchFetch((XmlBatchFetch)newValue);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__ATTRIBUTE_TYPE:
				setAttributeType((String)newValue);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__CASCADE_ON_DELETE:
				setCascadeOnDelete((Boolean)newValue);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__ACCESS_METHODS:
				setAccessMethods((XmlAccessMethods)newValue);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection<? extends XmlProperty>)newValue);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__JOIN_FETCH:
				setJoinFetch((XmlJoinFetchType)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID)
	{
		switch (featureID)
		{
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__CONVERTER:
				setConverter((XmlConverter)null);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__TYPE_CONVERTER:
				setTypeConverter((XmlTypeConverter)null);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__OBJECT_TYPE_CONVERTER:
				setObjectTypeConverter((XmlObjectTypeConverter)null);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__STRUCT_CONVERTER:
				setStructConverter((XmlStructConverter)null);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES:
				getMapKeyAssociationOverrides().clear();
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_CONVERT:
				setMapKeyConvert(MAP_KEY_CONVERT_EDEFAULT);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__BATCH_FETCH:
				setBatchFetch((XmlBatchFetch)null);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__ATTRIBUTE_TYPE:
				setAttributeType(ATTRIBUTE_TYPE_EDEFAULT);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__CASCADE_ON_DELETE:
				setCascadeOnDelete(CASCADE_ON_DELETE_EDEFAULT);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__ACCESS_METHODS:
				setAccessMethods((XmlAccessMethods)null);
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__PROPERTIES:
				getProperties().clear();
				return;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__JOIN_FETCH:
				setJoinFetch(JOIN_FETCH_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID)
	{
		switch (featureID)
		{
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__CONVERTER:
				return converter != null;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__TYPE_CONVERTER:
				return typeConverter != null;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__OBJECT_TYPE_CONVERTER:
				return objectTypeConverter != null;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__STRUCT_CONVERTER:
				return structConverter != null;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES:
				return mapKeyAssociationOverrides != null && !mapKeyAssociationOverrides.isEmpty();
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_CONVERT:
				return MAP_KEY_CONVERT_EDEFAULT == null ? mapKeyConvert != null : !MAP_KEY_CONVERT_EDEFAULT.equals(mapKeyConvert);
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__BATCH_FETCH:
				return batchFetch != null;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__ATTRIBUTE_TYPE:
				return ATTRIBUTE_TYPE_EDEFAULT == null ? attributeType != null : !ATTRIBUTE_TYPE_EDEFAULT.equals(attributeType);
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__CASCADE_ON_DELETE:
				return CASCADE_ON_DELETE_EDEFAULT == null ? cascadeOnDelete != null : !CASCADE_ON_DELETE_EDEFAULT.equals(cascadeOnDelete);
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__ACCESS_METHODS:
				return accessMethods != null;
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__PROPERTIES:
				return properties != null && !properties.isEmpty();
			case EclipseLinkOrmPackage.XML_MANY_TO_MANY__JOIN_FETCH:
				return joinFetch != JOIN_FETCH_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass)
	{
		if (baseClass == XmlConverterHolder.class)
		{
			switch (derivedFeatureID)
			{
				case EclipseLinkOrmPackage.XML_MANY_TO_MANY__CONVERTER: return EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__CONVERTER;
				case EclipseLinkOrmPackage.XML_MANY_TO_MANY__TYPE_CONVERTER: return EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__TYPE_CONVERTER;
				case EclipseLinkOrmPackage.XML_MANY_TO_MANY__OBJECT_TYPE_CONVERTER: return EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__OBJECT_TYPE_CONVERTER;
				case EclipseLinkOrmPackage.XML_MANY_TO_MANY__STRUCT_CONVERTER: return EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__STRUCT_CONVERTER;
				default: return -1;
			}
		}
		if (baseClass == XmlMapKeyAssociationOverrideContainer_2_0.class)
		{
			switch (derivedFeatureID)
			{
				case EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES: return EclipseLinkOrmV2_0Package.XML_MAP_KEY_ASSOCIATION_OVERRIDE_CONTAINER_20__MAP_KEY_ASSOCIATION_OVERRIDES;
				default: return -1;
			}
		}
		if (baseClass == XmlCollectionMapping_2_0.class)
		{
			switch (derivedFeatureID)
			{
				case EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_CONVERT: return EclipseLinkOrmV2_0Package.XML_COLLECTION_MAPPING_20__MAP_KEY_CONVERT;
				default: return -1;
			}
		}
		if (baseClass == XmlManyToMany_2_0.class)
		{
			switch (derivedFeatureID)
			{
				default: return -1;
			}
		}
		if (baseClass == XmlBatchFetchHolder.class)
		{
			switch (derivedFeatureID)
			{
				case EclipseLinkOrmPackage.XML_MANY_TO_MANY__BATCH_FETCH: return EclipseLinkOrmPackage.XML_BATCH_FETCH_HOLDER__BATCH_FETCH;
				default: return -1;
			}
		}
		if (baseClass == XmlManyToMany_2_1.class)
		{
			switch (derivedFeatureID)
			{
				case EclipseLinkOrmPackage.XML_MANY_TO_MANY__ATTRIBUTE_TYPE: return EclipseLinkOrmV2_1Package.XML_MANY_TO_MANY_21__ATTRIBUTE_TYPE;
				default: return -1;
			}
		}
		if (baseClass == XmlManyToMany_2_2.class)
		{
			switch (derivedFeatureID)
			{
				case EclipseLinkOrmPackage.XML_MANY_TO_MANY__CASCADE_ON_DELETE: return EclipseLinkOrmV2_2Package.XML_MANY_TO_MANY_22__CASCADE_ON_DELETE;
				default: return -1;
			}
		}
		if (baseClass == XmlAccessMethodsHolder.class)
		{
			switch (derivedFeatureID)
			{
				case EclipseLinkOrmPackage.XML_MANY_TO_MANY__ACCESS_METHODS: return EclipseLinkOrmPackage.XML_ACCESS_METHODS_HOLDER__ACCESS_METHODS;
				default: return -1;
			}
		}
		if (baseClass == XmlPropertyContainer.class)
		{
			switch (derivedFeatureID)
			{
				case EclipseLinkOrmPackage.XML_MANY_TO_MANY__PROPERTIES: return EclipseLinkOrmPackage.XML_PROPERTY_CONTAINER__PROPERTIES;
				default: return -1;
			}
		}
		if (baseClass == XmlAttributeMapping.class)
		{
			switch (derivedFeatureID)
			{
				default: return -1;
			}
		}
		if (baseClass == XmlJoinFetch.class)
		{
			switch (derivedFeatureID)
			{
				case EclipseLinkOrmPackage.XML_MANY_TO_MANY__JOIN_FETCH: return EclipseLinkOrmPackage.XML_JOIN_FETCH__JOIN_FETCH;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass)
	{
		if (baseClass == XmlConverterHolder.class)
		{
			switch (baseFeatureID)
			{
				case EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__CONVERTER: return EclipseLinkOrmPackage.XML_MANY_TO_MANY__CONVERTER;
				case EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__TYPE_CONVERTER: return EclipseLinkOrmPackage.XML_MANY_TO_MANY__TYPE_CONVERTER;
				case EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__OBJECT_TYPE_CONVERTER: return EclipseLinkOrmPackage.XML_MANY_TO_MANY__OBJECT_TYPE_CONVERTER;
				case EclipseLinkOrmPackage.XML_CONVERTER_HOLDER__STRUCT_CONVERTER: return EclipseLinkOrmPackage.XML_MANY_TO_MANY__STRUCT_CONVERTER;
				default: return -1;
			}
		}
		if (baseClass == XmlMapKeyAssociationOverrideContainer_2_0.class)
		{
			switch (baseFeatureID)
			{
				case EclipseLinkOrmV2_0Package.XML_MAP_KEY_ASSOCIATION_OVERRIDE_CONTAINER_20__MAP_KEY_ASSOCIATION_OVERRIDES: return EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_ASSOCIATION_OVERRIDES;
				default: return -1;
			}
		}
		if (baseClass == XmlCollectionMapping_2_0.class)
		{
			switch (baseFeatureID)
			{
				case EclipseLinkOrmV2_0Package.XML_COLLECTION_MAPPING_20__MAP_KEY_CONVERT: return EclipseLinkOrmPackage.XML_MANY_TO_MANY__MAP_KEY_CONVERT;
				default: return -1;
			}
		}
		if (baseClass == XmlManyToMany_2_0.class)
		{
			switch (baseFeatureID)
			{
				default: return -1;
			}
		}
		if (baseClass == XmlBatchFetchHolder.class)
		{
			switch (baseFeatureID)
			{
				case EclipseLinkOrmPackage.XML_BATCH_FETCH_HOLDER__BATCH_FETCH: return EclipseLinkOrmPackage.XML_MANY_TO_MANY__BATCH_FETCH;
				default: return -1;
			}
		}
		if (baseClass == XmlManyToMany_2_1.class)
		{
			switch (baseFeatureID)
			{
				case EclipseLinkOrmV2_1Package.XML_MANY_TO_MANY_21__ATTRIBUTE_TYPE: return EclipseLinkOrmPackage.XML_MANY_TO_MANY__ATTRIBUTE_TYPE;
				default: return -1;
			}
		}
		if (baseClass == XmlManyToMany_2_2.class)
		{
			switch (baseFeatureID)
			{
				case EclipseLinkOrmV2_2Package.XML_MANY_TO_MANY_22__CASCADE_ON_DELETE: return EclipseLinkOrmPackage.XML_MANY_TO_MANY__CASCADE_ON_DELETE;
				default: return -1;
			}
		}
		if (baseClass == XmlAccessMethodsHolder.class)
		{
			switch (baseFeatureID)
			{
				case EclipseLinkOrmPackage.XML_ACCESS_METHODS_HOLDER__ACCESS_METHODS: return EclipseLinkOrmPackage.XML_MANY_TO_MANY__ACCESS_METHODS;
				default: return -1;
			}
		}
		if (baseClass == XmlPropertyContainer.class)
		{
			switch (baseFeatureID)
			{
				case EclipseLinkOrmPackage.XML_PROPERTY_CONTAINER__PROPERTIES: return EclipseLinkOrmPackage.XML_MANY_TO_MANY__PROPERTIES;
				default: return -1;
			}
		}
		if (baseClass == XmlAttributeMapping.class)
		{
			switch (baseFeatureID)
			{
				default: return -1;
			}
		}
		if (baseClass == XmlJoinFetch.class)
		{
			switch (baseFeatureID)
			{
				case EclipseLinkOrmPackage.XML_JOIN_FETCH__JOIN_FETCH: return EclipseLinkOrmPackage.XML_MANY_TO_MANY__JOIN_FETCH;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString()
	{
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (mapKeyConvert: ");
		result.append(mapKeyConvert);
		result.append(", attributeType: ");
		result.append(attributeType);
		result.append(", cascadeOnDelete: ");
		result.append(cascadeOnDelete);
		result.append(", joinFetch: ");
		result.append(joinFetch);
		result.append(')');
		return result.toString();
	}
	
	public TextRange getJoinFetchTextRange() {
		return getElementTextRange(EclipseLink.JOIN_FETCH);
	}
	
	
	// ********** translators **********
	
	public static Translator buildTranslator(String elementName, EStructuralFeature structuralFeature) {
		return new SimpleTranslator(
			elementName, 
			structuralFeature, 
			EclipseLinkOrmPackage.eINSTANCE.getXmlManyToMany(), 
			buildTranslatorChildren());
	}
	
	private static Translator[] buildTranslatorChildren() {
		return new Translator[] {
			buildNameTranslator(),
			buildTargetEntityTranslator(),
			buildFetchTranslator(),
			buildAccessTranslator(),
			buildMappedByTranslator(),
			buildAttributeTypeTranslator(),
			buildOrderByTranslator(),
			XmlOrderColumn.buildTranslator(JPA2_0.ORDER_COLUMN, OrmV2_0Package.eINSTANCE.getXmlOrderable_2_0_OrderColumn()),		
			buildMapKeyTranslator(),
			buildMapKeyClassTranslator(),
			buildMapKeyTemporalTranslator(),
			buildMapKeyEnumeratedTranslator(),
			buildMapKeyConvertTranslator(),
			buildMapKeyAttributeOverrideTranslator(),
			buildMapKeyAssociationOverrideTranslator(),
			buildMapKeyColumnTranslator(),
			buildMapKeyJoinColumnTranslator(),
			buildConverterTranslator(),
			buildTypeConverterTranslator(),
			buildObjectTypeConverterTranslator(),
			buildStructConverterTranslator(),
			buildJoinTableTranslator(),
			buildCascadeTranslator(),
			buildCascadeOnDeleteTranslator(),
			buildJoinFetchTranslator(),
			buildBatchFetchTranslator(),
			buildPropertyTranslator(),
			buildAccessMethodsTranslator()
		};
	}
	
	protected static Translator buildMapKeyConvertTranslator() {
		return new Translator(JPA2_0.MAP_KEY_CONVERT, EclipseLinkOrmV2_0Package.eINSTANCE.getXmlCollectionMapping_2_0_MapKeyConvert());
	}
	
	protected static Translator buildMapKeyAttributeOverrideTranslator() {
		return XmlAttributeOverride.buildTranslator(JPA2_0.MAP_KEY_ATTRIBUTE_OVERRIDE, OrmV2_0Package.eINSTANCE.getXmlMapKeyAttributeOverrideContainer_2_0_MapKeyAttributeOverrides());
	}
	
	protected static Translator buildMapKeyAssociationOverrideTranslator() {
		return XmlAssociationOverride.buildTranslator(EclipseLink2_0.MAP_KEY_ASSOCIATION_OVERRIDE, EclipseLinkOrmV2_0Package.eINSTANCE.getXmlMapKeyAssociationOverrideContainer_2_0_MapKeyAssociationOverrides());
	}	
	
	protected static Translator buildConverterTranslator() {
		return XmlConverter.buildTranslator(EclipseLink.CONVERTER, EclipseLinkOrmPackage.eINSTANCE.getXmlConverterHolder_Converter());
	}
	
	protected static Translator buildTypeConverterTranslator() {
		return XmlTypeConverter.buildTranslator(EclipseLink.TYPE_CONVERTER, EclipseLinkOrmPackage.eINSTANCE.getXmlConverterHolder_TypeConverter());
	}
	
	protected static Translator buildObjectTypeConverterTranslator() {
		return XmlObjectTypeConverter.buildTranslator(EclipseLink.OBJECT_TYPE_CONVERTER, EclipseLinkOrmPackage.eINSTANCE.getXmlConverterHolder_ObjectTypeConverter());
	}
	
	protected static Translator buildStructConverterTranslator() {
		return XmlStructConverter.buildTranslator(EclipseLink.STRUCT_CONVERTER, EclipseLinkOrmPackage.eINSTANCE.getXmlConverterHolder_StructConverter());
	}
	
	protected static Translator buildJoinFetchTranslator() {
		return new Translator(EclipseLink.JOIN_FETCH, EclipseLinkOrmPackage.eINSTANCE.getXmlJoinFetch_JoinFetch());
	}
	
	protected static Translator buildPropertyTranslator() {
		return XmlProperty.buildTranslator(EclipseLink.PROPERTY, EclipseLinkOrmPackage.eINSTANCE.getXmlPropertyContainer_Properties());
	}
	
	protected static Translator buildAccessMethodsTranslator() {
		return XmlAccessMethods.buildTranslator(EclipseLink.ACCESS_METHODS, EclipseLinkOrmPackage.eINSTANCE.getXmlAccessMethodsHolder_AccessMethods());
	}

	protected static Translator buildAttributeTypeTranslator() {
		return new Translator(EclipseLink2_1.ATTRIBUTE_TYPE, EclipseLinkOrmV2_1Package.eINSTANCE.getXmlManyToMany_2_1_AttributeType(), Translator.DOM_ATTRIBUTE);
	}

	protected static Translator buildBatchFetchTranslator() {
		return XmlBatchFetch.buildTranslator(EclipseLink2_1.BATCH_FETCH, EclipseLinkOrmPackage.eINSTANCE.getXmlBatchFetchHolder_BatchFetch());
	}

	protected static Translator buildCascadeOnDeleteTranslator() {
		return CascadeType.buildTranslator(EclipseLink2_2.CASCADE_ON_DELETE, EclipseLinkOrmV2_2Package.eINSTANCE.getXmlManyToMany_2_2_CascadeOnDelete());
	}

}
