/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package hub.sam.sdl.impl;

import hub.sam.sdl.CoreAbstractionsMultiplicitiesMultiplicityElement;
import hub.sam.sdl.EmfSdlPackage;
import hub.sam.sdl.InstanciationMultiplicityFeature;
import hub.sam.sdl.InstanciationSlot;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Instanciation Multiplicity Feature</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link hub.sam.sdl.impl.InstanciationMultiplicityFeatureImpl#getIsOrdered <em>Is Ordered</em>}</li>
 *   <li>{@link hub.sam.sdl.impl.InstanciationMultiplicityFeatureImpl#getIsUnique <em>Is Unique</em>}</li>
 *   <li>{@link hub.sam.sdl.impl.InstanciationMultiplicityFeatureImpl#getLower <em>Lower</em>}</li>
 *   <li>{@link hub.sam.sdl.impl.InstanciationMultiplicityFeatureImpl#getUpper <em>Upper</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class InstanciationMultiplicityFeatureImpl extends CoreAbstractionsStructuralFeaturesStructuralFeatureImpl implements InstanciationMultiplicityFeature {
	/**
	 * The default value of the '{@link #getIsOrdered() <em>Is Ordered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIsOrdered()
	 * @generated
	 * @ordered
	 */
	protected static final String IS_ORDERED_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIsOrdered() <em>Is Ordered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIsOrdered()
	 * @generated
	 * @ordered
	 */
	protected String isOrdered = IS_ORDERED_EDEFAULT;

	/**
	 * The default value of the '{@link #getIsUnique() <em>Is Unique</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIsUnique()
	 * @generated
	 * @ordered
	 */
	protected static final String IS_UNIQUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIsUnique() <em>Is Unique</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIsUnique()
	 * @generated
	 * @ordered
	 */
	protected String isUnique = IS_UNIQUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLower() <em>Lower</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLower()
	 * @generated
	 * @ordered
	 */
	protected static final String LOWER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLower() <em>Lower</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLower()
	 * @generated
	 * @ordered
	 */
	protected String lower = LOWER_EDEFAULT;

	/**
	 * The default value of the '{@link #getUpper() <em>Upper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpper()
	 * @generated
	 * @ordered
	 */
	protected static final String UPPER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUpper() <em>Upper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpper()
	 * @generated
	 * @ordered
	 */
	protected String upper = UPPER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InstanciationMultiplicityFeatureImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EmfSdlPackage.eINSTANCE.getInstanciationMultiplicityFeature();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIsOrdered() {
		return isOrdered;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsOrdered(String newIsOrdered) {
		String oldIsOrdered = isOrdered;
		isOrdered = newIsOrdered;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_ORDERED, oldIsOrdered, isOrdered));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIsUnique() {
		return isUnique;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsUnique(String newIsUnique) {
		String oldIsUnique = isUnique;
		isUnique = newIsUnique;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_UNIQUE, oldIsUnique, isUnique));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLower() {
		return lower;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLower(String newLower) {
		String oldLower = lower;
		lower = newLower;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__LOWER, oldLower, lower));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUpper() {
		return upper;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUpper(String newUpper) {
		String oldUpper = upper;
		upper = newUpper;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__UPPER, oldUpper, upper));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InstanciationSlot instanciate() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String lowerBound() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String upperBound() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String isMultivalued() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String includesCardinality(String C) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String includesMultiplicity(CoreAbstractionsMultiplicitiesMultiplicityElement M) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_ORDERED:
				return getIsOrdered();
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_UNIQUE:
				return getIsUnique();
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__LOWER:
				return getLower();
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__UPPER:
				return getUpper();
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
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_ORDERED:
				setIsOrdered((String)newValue);
				return;
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_UNIQUE:
				setIsUnique((String)newValue);
				return;
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__LOWER:
				setLower((String)newValue);
				return;
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__UPPER:
				setUpper((String)newValue);
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
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_ORDERED:
				setIsOrdered(IS_ORDERED_EDEFAULT);
				return;
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_UNIQUE:
				setIsUnique(IS_UNIQUE_EDEFAULT);
				return;
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__LOWER:
				setLower(LOWER_EDEFAULT);
				return;
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__UPPER:
				setUpper(UPPER_EDEFAULT);
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
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_ORDERED:
				return IS_ORDERED_EDEFAULT == null ? isOrdered != null : !IS_ORDERED_EDEFAULT.equals(isOrdered);
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_UNIQUE:
				return IS_UNIQUE_EDEFAULT == null ? isUnique != null : !IS_UNIQUE_EDEFAULT.equals(isUnique);
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__LOWER:
				return LOWER_EDEFAULT == null ? lower != null : !LOWER_EDEFAULT.equals(lower);
			case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__UPPER:
				return UPPER_EDEFAULT == null ? upper != null : !UPPER_EDEFAULT.equals(upper);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == CoreAbstractionsMultiplicitiesMultiplicityElement.class) {
			switch (derivedFeatureID) {
				case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_ORDERED: return EmfSdlPackage.CORE_ABSTRACTIONS_MULTIPLICITIES_MULTIPLICITY_ELEMENT__IS_ORDERED;
				case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_UNIQUE: return EmfSdlPackage.CORE_ABSTRACTIONS_MULTIPLICITIES_MULTIPLICITY_ELEMENT__IS_UNIQUE;
				case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__LOWER: return EmfSdlPackage.CORE_ABSTRACTIONS_MULTIPLICITIES_MULTIPLICITY_ELEMENT__LOWER;
				case EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__UPPER: return EmfSdlPackage.CORE_ABSTRACTIONS_MULTIPLICITIES_MULTIPLICITY_ELEMENT__UPPER;
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
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == CoreAbstractionsMultiplicitiesMultiplicityElement.class) {
			switch (baseFeatureID) {
				case EmfSdlPackage.CORE_ABSTRACTIONS_MULTIPLICITIES_MULTIPLICITY_ELEMENT__IS_ORDERED: return EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_ORDERED;
				case EmfSdlPackage.CORE_ABSTRACTIONS_MULTIPLICITIES_MULTIPLICITY_ELEMENT__IS_UNIQUE: return EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__IS_UNIQUE;
				case EmfSdlPackage.CORE_ABSTRACTIONS_MULTIPLICITIES_MULTIPLICITY_ELEMENT__LOWER: return EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__LOWER;
				case EmfSdlPackage.CORE_ABSTRACTIONS_MULTIPLICITIES_MULTIPLICITY_ELEMENT__UPPER: return EmfSdlPackage.INSTANCIATION_MULTIPLICITY_FEATURE__UPPER;
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
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (isOrdered: ");
		result.append(isOrdered);
		result.append(", isUnique: ");
		result.append(isUnique);
		result.append(", lower: ");
		result.append(lower);
		result.append(", upper: ");
		result.append(upper);
		result.append(')');
		return result.toString();
	}

} //InstanciationMultiplicityFeatureImpl
