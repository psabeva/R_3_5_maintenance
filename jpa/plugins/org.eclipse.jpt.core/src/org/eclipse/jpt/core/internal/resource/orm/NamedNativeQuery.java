/**
 * <copyright>
 * </copyright>
 *
 * $Id: NamedNativeQuery.java,v 1.1.2.2 2007/10/16 17:01:40 pfullbright Exp $
 */
package org.eclipse.jpt.core.internal.resource.orm;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jpt.core.internal.resource.common.IJptEObject;
import org.eclipse.jpt.core.internal.resource.common.JptEObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Named Native Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.jpt.core.internal.resource.orm.NamedNativeQuery#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.jpt.core.internal.resource.orm.NamedNativeQuery#getResultClass <em>Result Class</em>}</li>
 *   <li>{@link org.eclipse.jpt.core.internal.resource.orm.NamedNativeQuery#getResultSetMapping <em>Result Set Mapping</em>}</li>
 *   <li>{@link org.eclipse.jpt.core.internal.resource.orm.NamedNativeQuery#getQuery <em>Query</em>}</li>
 *   <li>{@link org.eclipse.jpt.core.internal.resource.orm.NamedNativeQuery#getHints <em>Hints</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.jpt.core.internal.resource.orm.OrmPackage#getNamedNativeQuery()
 * @model kind="class"
 * @extends IJptEObject
 * @generated
 */
public class NamedNativeQuery extends JptEObject implements IJptEObject
{
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getResultClass() <em>Result Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultClass()
	 * @generated
	 * @ordered
	 */
	protected static final String RESULT_CLASS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getResultClass() <em>Result Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultClass()
	 * @generated
	 * @ordered
	 */
	protected String resultClass = RESULT_CLASS_EDEFAULT;

	/**
	 * The default value of the '{@link #getResultSetMapping() <em>Result Set Mapping</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultSetMapping()
	 * @generated
	 * @ordered
	 */
	protected static final String RESULT_SET_MAPPING_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getResultSetMapping() <em>Result Set Mapping</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultSetMapping()
	 * @generated
	 * @ordered
	 */
	protected String resultSetMapping = RESULT_SET_MAPPING_EDEFAULT;

	/**
	 * The default value of the '{@link #getQuery() <em>Query</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuery()
	 * @generated
	 * @ordered
	 */
	protected static final String QUERY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getQuery() <em>Query</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuery()
	 * @generated
	 * @ordered
	 */
	protected String query = QUERY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getHints() <em>Hints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHints()
	 * @generated
	 * @ordered
	 */
	protected EList<QueryHint> hints;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NamedNativeQuery()
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
		return OrmPackage.Literals.NAMED_NATIVE_QUERY;
	}

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.jpt.core.internal.resource.orm.OrmPackage#getNamedNativeQuery_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 * @generated
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.core.internal.resource.orm.NamedNativeQuery#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	public void setName(String newName)
	{
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.NAMED_NATIVE_QUERY__NAME, oldName, name));
	}

	/**
	 * Returns the value of the '<em><b>Result Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Result Class</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Result Class</em>' attribute.
	 * @see #setResultClass(String)
	 * @see org.eclipse.jpt.core.internal.resource.orm.OrmPackage#getNamedNativeQuery_ResultClass()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
	public String getResultClass()
	{
		return resultClass;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.core.internal.resource.orm.NamedNativeQuery#getResultClass <em>Result Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Result Class</em>' attribute.
	 * @see #getResultClass()
	 * @generated
	 */
	public void setResultClass(String newResultClass)
	{
		String oldResultClass = resultClass;
		resultClass = newResultClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.NAMED_NATIVE_QUERY__RESULT_CLASS, oldResultClass, resultClass));
	}

	/**
	 * Returns the value of the '<em><b>Result Set Mapping</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Result Set Mapping</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Result Set Mapping</em>' attribute.
	 * @see #setResultSetMapping(String)
	 * @see org.eclipse.jpt.core.internal.resource.orm.OrmPackage#getNamedNativeQuery_ResultSetMapping()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
	public String getResultSetMapping()
	{
		return resultSetMapping;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.core.internal.resource.orm.NamedNativeQuery#getResultSetMapping <em>Result Set Mapping</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Result Set Mapping</em>' attribute.
	 * @see #getResultSetMapping()
	 * @generated
	 */
	public void setResultSetMapping(String newResultSetMapping)
	{
		String oldResultSetMapping = resultSetMapping;
		resultSetMapping = newResultSetMapping;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.NAMED_NATIVE_QUERY__RESULT_SET_MAPPING, oldResultSetMapping, resultSetMapping));
	}

	/**
	 * Returns the value of the '<em><b>Query</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Query</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Query</em>' attribute.
	 * @see #setQuery(String)
	 * @see org.eclipse.jpt.core.internal.resource.orm.OrmPackage#getNamedNativeQuery_Query()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 * @generated
	 */
	public String getQuery()
	{
		return query;
	}

	/**
	 * Sets the value of the '{@link org.eclipse.jpt.core.internal.resource.orm.NamedNativeQuery#getQuery <em>Query</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Query</em>' attribute.
	 * @see #getQuery()
	 * @generated
	 */
	public void setQuery(String newQuery)
	{
		String oldQuery = query;
		query = newQuery;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OrmPackage.NAMED_NATIVE_QUERY__QUERY, oldQuery, query));
	}

	/**
	 * Returns the value of the '<em><b>Hints</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.jpt.core.internal.resource.orm.QueryHint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hints</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hints</em>' containment reference list.
	 * @see org.eclipse.jpt.core.internal.resource.orm.OrmPackage#getNamedNativeQuery_Hints()
	 * @model containment="true"
	 * @generated
	 */
	public EList<QueryHint> getHints()
	{
		if (hints == null)
		{
			hints = new EObjectContainmentEList<QueryHint>(QueryHint.class, this, OrmPackage.NAMED_NATIVE_QUERY__HINTS);
		}
		return hints;
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
			case OrmPackage.NAMED_NATIVE_QUERY__HINTS:
				return ((InternalEList<?>)getHints()).basicRemove(otherEnd, msgs);
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
			case OrmPackage.NAMED_NATIVE_QUERY__NAME:
				return getName();
			case OrmPackage.NAMED_NATIVE_QUERY__RESULT_CLASS:
				return getResultClass();
			case OrmPackage.NAMED_NATIVE_QUERY__RESULT_SET_MAPPING:
				return getResultSetMapping();
			case OrmPackage.NAMED_NATIVE_QUERY__QUERY:
				return getQuery();
			case OrmPackage.NAMED_NATIVE_QUERY__HINTS:
				return getHints();
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
			case OrmPackage.NAMED_NATIVE_QUERY__NAME:
				setName((String)newValue);
				return;
			case OrmPackage.NAMED_NATIVE_QUERY__RESULT_CLASS:
				setResultClass((String)newValue);
				return;
			case OrmPackage.NAMED_NATIVE_QUERY__RESULT_SET_MAPPING:
				setResultSetMapping((String)newValue);
				return;
			case OrmPackage.NAMED_NATIVE_QUERY__QUERY:
				setQuery((String)newValue);
				return;
			case OrmPackage.NAMED_NATIVE_QUERY__HINTS:
				getHints().clear();
				getHints().addAll((Collection<? extends QueryHint>)newValue);
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
			case OrmPackage.NAMED_NATIVE_QUERY__NAME:
				setName(NAME_EDEFAULT);
				return;
			case OrmPackage.NAMED_NATIVE_QUERY__RESULT_CLASS:
				setResultClass(RESULT_CLASS_EDEFAULT);
				return;
			case OrmPackage.NAMED_NATIVE_QUERY__RESULT_SET_MAPPING:
				setResultSetMapping(RESULT_SET_MAPPING_EDEFAULT);
				return;
			case OrmPackage.NAMED_NATIVE_QUERY__QUERY:
				setQuery(QUERY_EDEFAULT);
				return;
			case OrmPackage.NAMED_NATIVE_QUERY__HINTS:
				getHints().clear();
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
			case OrmPackage.NAMED_NATIVE_QUERY__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case OrmPackage.NAMED_NATIVE_QUERY__RESULT_CLASS:
				return RESULT_CLASS_EDEFAULT == null ? resultClass != null : !RESULT_CLASS_EDEFAULT.equals(resultClass);
			case OrmPackage.NAMED_NATIVE_QUERY__RESULT_SET_MAPPING:
				return RESULT_SET_MAPPING_EDEFAULT == null ? resultSetMapping != null : !RESULT_SET_MAPPING_EDEFAULT.equals(resultSetMapping);
			case OrmPackage.NAMED_NATIVE_QUERY__QUERY:
				return QUERY_EDEFAULT == null ? query != null : !QUERY_EDEFAULT.equals(query);
			case OrmPackage.NAMED_NATIVE_QUERY__HINTS:
				return hints != null && !hints.isEmpty();
		}
		return super.eIsSet(featureID);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", resultClass: ");
		result.append(resultClass);
		result.append(", resultSetMapping: ");
		result.append(resultSetMapping);
		result.append(", query: ");
		result.append(query);
		result.append(')');
		return result.toString();
	}

} // NamedNativeQuery
