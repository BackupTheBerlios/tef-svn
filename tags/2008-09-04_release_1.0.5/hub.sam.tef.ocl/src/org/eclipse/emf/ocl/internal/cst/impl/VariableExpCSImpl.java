/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.ocl.internal.cst.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.emf.ocl.internal.cst.CSTPackage;
import org.eclipse.emf.ocl.internal.cst.IsMarkedPreCS;
import org.eclipse.emf.ocl.internal.cst.OCLExpressionCS;
import org.eclipse.emf.ocl.internal.cst.SimpleNameCS;
import org.eclipse.emf.ocl.internal.cst.VariableExpCS;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Variable Exp CS</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.ocl.internal.cst.impl.VariableExpCSImpl#getArguments <em>Arguments</em>}</li>
 *   <li>{@link org.eclipse.emf.ocl.internal.cst.impl.VariableExpCSImpl#getSimpleNameCS <em>Simple Name CS</em>}</li>
 *   <li>{@link org.eclipse.emf.ocl.internal.cst.impl.VariableExpCSImpl#getIsMarkedPreCS <em>Is Marked Pre CS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VariableExpCSImpl extends OCLExpressionCSImpl implements VariableExpCS {
	/**
	 * The cached value of the '{@link #getArguments() <em>Arguments</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArguments()
	 * @generated
	 * @ordered
	 */
	protected EList arguments;

	/**
	 * The cached value of the '{@link #getSimpleNameCS() <em>Simple Name CS</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSimpleNameCS()
	 * @generated
	 * @ordered
	 */
	protected SimpleNameCS simpleNameCS;

	/**
	 * The cached value of the '{@link #getIsMarkedPreCS() <em>Is Marked Pre CS</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIsMarkedPreCS()
	 * @generated
	 * @ordered
	 */
	protected IsMarkedPreCS isMarkedPreCS;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VariableExpCSImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return CSTPackage.Literals.VARIABLE_EXP_CS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getArguments() {
		if (arguments == null) {
			arguments = new EObjectResolvingEList(OCLExpressionCS.class, this, CSTPackage.VARIABLE_EXP_CS__ARGUMENTS);
		}
		return arguments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleNameCS getSimpleNameCS() {
		if (simpleNameCS != null && simpleNameCS.eIsProxy()) {
			InternalEObject oldSimpleNameCS = (InternalEObject)simpleNameCS;
			simpleNameCS = (SimpleNameCS)eResolveProxy(oldSimpleNameCS);
			if (simpleNameCS != oldSimpleNameCS) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CSTPackage.VARIABLE_EXP_CS__SIMPLE_NAME_CS, oldSimpleNameCS, simpleNameCS));
			}
		}
		return simpleNameCS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleNameCS basicGetSimpleNameCS() {
		return simpleNameCS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSimpleNameCS(SimpleNameCS newSimpleNameCS) {
		SimpleNameCS oldSimpleNameCS = simpleNameCS;
		simpleNameCS = newSimpleNameCS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CSTPackage.VARIABLE_EXP_CS__SIMPLE_NAME_CS, oldSimpleNameCS, simpleNameCS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IsMarkedPreCS getIsMarkedPreCS() {
		if (isMarkedPreCS != null && isMarkedPreCS.eIsProxy()) {
			InternalEObject oldIsMarkedPreCS = (InternalEObject)isMarkedPreCS;
			isMarkedPreCS = (IsMarkedPreCS)eResolveProxy(oldIsMarkedPreCS);
			if (isMarkedPreCS != oldIsMarkedPreCS) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CSTPackage.VARIABLE_EXP_CS__IS_MARKED_PRE_CS, oldIsMarkedPreCS, isMarkedPreCS));
			}
		}
		return isMarkedPreCS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IsMarkedPreCS basicGetIsMarkedPreCS() {
		return isMarkedPreCS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsMarkedPreCS(IsMarkedPreCS newIsMarkedPreCS) {
		IsMarkedPreCS oldIsMarkedPreCS = isMarkedPreCS;
		isMarkedPreCS = newIsMarkedPreCS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CSTPackage.VARIABLE_EXP_CS__IS_MARKED_PRE_CS, oldIsMarkedPreCS, isMarkedPreCS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CSTPackage.VARIABLE_EXP_CS__ARGUMENTS:
				return getArguments();
			case CSTPackage.VARIABLE_EXP_CS__SIMPLE_NAME_CS:
				if (resolve) return getSimpleNameCS();
				return basicGetSimpleNameCS();
			case CSTPackage.VARIABLE_EXP_CS__IS_MARKED_PRE_CS:
				if (resolve) return getIsMarkedPreCS();
				return basicGetIsMarkedPreCS();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CSTPackage.VARIABLE_EXP_CS__ARGUMENTS:
				getArguments().clear();
				getArguments().addAll((Collection)newValue);
				return;
			case CSTPackage.VARIABLE_EXP_CS__SIMPLE_NAME_CS:
				setSimpleNameCS((SimpleNameCS)newValue);
				return;
			case CSTPackage.VARIABLE_EXP_CS__IS_MARKED_PRE_CS:
				setIsMarkedPreCS((IsMarkedPreCS)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case CSTPackage.VARIABLE_EXP_CS__ARGUMENTS:
				getArguments().clear();
				return;
			case CSTPackage.VARIABLE_EXP_CS__SIMPLE_NAME_CS:
				setSimpleNameCS((SimpleNameCS)null);
				return;
			case CSTPackage.VARIABLE_EXP_CS__IS_MARKED_PRE_CS:
				setIsMarkedPreCS((IsMarkedPreCS)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case CSTPackage.VARIABLE_EXP_CS__ARGUMENTS:
				return arguments != null && !arguments.isEmpty();
			case CSTPackage.VARIABLE_EXP_CS__SIMPLE_NAME_CS:
				return simpleNameCS != null;
			case CSTPackage.VARIABLE_EXP_CS__IS_MARKED_PRE_CS:
				return isMarkedPreCS != null;
		}
		return super.eIsSet(featureID);
	}

} //VariableExpCSImpl
