/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package hub.sam.sdl.impl;

import hub.sam.sdl.CoreAbstractionsTypedElementsType;
import hub.sam.sdl.CoreAbstractionsTypedElementsTypedElement;
import hub.sam.sdl.EmfSdlPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Core Abstractions Typed Elements Typed Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link hub.sam.sdl.impl.CoreAbstractionsTypedElementsTypedElementImpl#getTypedElement_type <em>Typed Element type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class CoreAbstractionsTypedElementsTypedElementImpl extends CoreAbstractionsNamespacesNamedElementImpl implements CoreAbstractionsTypedElementsTypedElement {
	/**
	 * The cached value of the '{@link #getTypedElement_type() <em>Typed Element type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypedElement_type()
	 * @generated
	 * @ordered
	 */
	protected CoreAbstractionsTypedElementsType typedElement_type;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CoreAbstractionsTypedElementsTypedElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EmfSdlPackage.eINSTANCE.getCoreAbstractionsTypedElementsTypedElement();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CoreAbstractionsTypedElementsType getTypedElement_type() {
		if (typedElement_type != null && typedElement_type.eIsProxy()) {
			InternalEObject oldTypedElement_type = (InternalEObject)typedElement_type;
			typedElement_type = (CoreAbstractionsTypedElementsType)eResolveProxy(oldTypedElement_type);
			if (typedElement_type != oldTypedElement_type) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EmfSdlPackage.CORE_ABSTRACTIONS_TYPED_ELEMENTS_TYPED_ELEMENT__TYPED_ELEMENT_TYPE, oldTypedElement_type, typedElement_type));
			}
		}
		return typedElement_type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CoreAbstractionsTypedElementsType basicGetTypedElement_type() {
		return typedElement_type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTypedElement_type(CoreAbstractionsTypedElementsType newTypedElement_type) {
		CoreAbstractionsTypedElementsType oldTypedElement_type = typedElement_type;
		typedElement_type = newTypedElement_type;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmfSdlPackage.CORE_ABSTRACTIONS_TYPED_ELEMENTS_TYPED_ELEMENT__TYPED_ELEMENT_TYPE, oldTypedElement_type, typedElement_type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EmfSdlPackage.CORE_ABSTRACTIONS_TYPED_ELEMENTS_TYPED_ELEMENT__TYPED_ELEMENT_TYPE:
				if (resolve) return getTypedElement_type();
				return basicGetTypedElement_type();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EmfSdlPackage.CORE_ABSTRACTIONS_TYPED_ELEMENTS_TYPED_ELEMENT__TYPED_ELEMENT_TYPE:
				setTypedElement_type((CoreAbstractionsTypedElementsType)newValue);
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
	public void eUnset(int featureID) {
		switch (featureID) {
			case EmfSdlPackage.CORE_ABSTRACTIONS_TYPED_ELEMENTS_TYPED_ELEMENT__TYPED_ELEMENT_TYPE:
				setTypedElement_type((CoreAbstractionsTypedElementsType)null);
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
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case EmfSdlPackage.CORE_ABSTRACTIONS_TYPED_ELEMENTS_TYPED_ELEMENT__TYPED_ELEMENT_TYPE:
				return typedElement_type != null;
		}
		return super.eIsSet(featureID);
	}

} //CoreAbstractionsTypedElementsTypedElementImpl
