/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink1_1.core.resource.orm;

import org.eclipse.jpt.core.resource.orm.XmlGeneratedValue;
import org.eclipse.jpt.core.resource.orm.XmlSequenceGenerator;
import org.eclipse.jpt.core.resource.orm.XmlTableGenerator;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Xml Basic</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.jpt.eclipselink1_1.core.resource.orm.XmlBasic#getGeneratedValue <em>Generated Value</em>}</li>
 *   <li>{@link org.eclipse.jpt.eclipselink1_1.core.resource.orm.XmlBasic#getTableGenerator <em>Table Generator</em>}</li>
 *   <li>{@link org.eclipse.jpt.eclipselink1_1.core.resource.orm.XmlBasic#getSequenceGenerator <em>Sequence Generator</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.jpt.eclipselink1_1.core.resource.orm.EclipseLink1_1OrmPackage#getXmlBasic()
 * @model kind="class" interface="true" abstract="true"
 * @generated
 */
public interface XmlBasic extends org.eclipse.jpt.eclipselink.core.resource.orm.XmlBasic, XmlAttributeMapping
{

	/**
	 * Returns the value of the '<em><b>Generated Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generated Value</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generated Value</em>' containment reference.
	 * @see #setGeneratedValue(XmlGeneratedValue)
	 * @see org.eclipse.jpt.eclipselink1_1.core.resource.orm.EclipseLink1_1OrmPackage#getXmlBasic_GeneratedValue()
	 * @model containment="true"
	 * @generated
	 */
	XmlGeneratedValue getGeneratedValue();

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink1_1.core.resource.orm.XmlBasic#getGeneratedValue <em>Generated Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generated Value</em>' containment reference.
	 * @see #getGeneratedValue()
	 * @generated
	 */
	void setGeneratedValue(XmlGeneratedValue value);

	/**
	 * Returns the value of the '<em><b>Table Generator</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Table Generator</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Table Generator</em>' containment reference.
	 * @see #setTableGenerator(XmlTableGenerator)
	 * @see org.eclipse.jpt.eclipselink1_1.core.resource.orm.EclipseLink1_1OrmPackage#getXmlBasic_TableGenerator()
	 * @model containment="true"
	 * @generated
	 */
	XmlTableGenerator getTableGenerator();

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink1_1.core.resource.orm.XmlBasic#getTableGenerator <em>Table Generator</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Table Generator</em>' containment reference.
	 * @see #getTableGenerator()
	 * @generated
	 */
	void setTableGenerator(XmlTableGenerator value);

	/**
	 * Returns the value of the '<em><b>Sequence Generator</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sequence Generator</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sequence Generator</em>' containment reference.
	 * @see #setSequenceGenerator(XmlSequenceGenerator)
	 * @see org.eclipse.jpt.eclipselink1_1.core.resource.orm.EclipseLink1_1OrmPackage#getXmlBasic_SequenceGenerator()
	 * @model containment="true"
	 * @generated
	 */
	XmlSequenceGenerator getSequenceGenerator();

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.eclipselink1_1.core.resource.orm.XmlBasic#getSequenceGenerator <em>Sequence Generator</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sequence Generator</em>' containment reference.
	 * @see #getSequenceGenerator()
	 * @generated
	 */
	void setSequenceGenerator(XmlSequenceGenerator value);
} // XmlBasic
