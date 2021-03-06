/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package hub.sam.sdl;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Core Abstractions Multiplicity Expressions Multiplicity Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hub.sam.sdl.CoreAbstractionsMultiplicityExpressionsMultiplicityElement#getLower <em>Lower</em>}</li>
 *   <li>{@link hub.sam.sdl.CoreAbstractionsMultiplicityExpressionsMultiplicityElement#getUpper <em>Upper</em>}</li>
 *   <li>{@link hub.sam.sdl.CoreAbstractionsMultiplicityExpressionsMultiplicityElement#getUpperValue <em>Upper Value</em>}</li>
 *   <li>{@link hub.sam.sdl.CoreAbstractionsMultiplicityExpressionsMultiplicityElement#getLowerValue <em>Lower Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see hub.sam.sdl.EmfSdlPackage#getCoreAbstractionsMultiplicityExpressionsMultiplicityElement()
 * @model abstract="true"
 * @generated
 */
public interface CoreAbstractionsMultiplicityExpressionsMultiplicityElement extends CoreAbstractionsOwnershipsElement {
	/**
	 * Returns the value of the '<em><b>Lower</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lower</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lower</em>' attribute.
	 * @see #setLower(String)
	 * @see hub.sam.sdl.EmfSdlPackage#getCoreAbstractionsMultiplicityExpressionsMultiplicityElement_Lower()
	 * @model dataType="hub.sam.sdl.PrimitiveTypesInteger" derived="true" ordered="false"
	 * @generated
	 */
	String getLower();

	/**
	 * Sets the value of the '{@link hub.sam.sdl.CoreAbstractionsMultiplicityExpressionsMultiplicityElement#getLower <em>Lower</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lower</em>' attribute.
	 * @see #getLower()
	 * @generated
	 */
	void setLower(String value);

	/**
	 * Returns the value of the '<em><b>Upper</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Upper</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Upper</em>' attribute.
	 * @see #setUpper(String)
	 * @see hub.sam.sdl.EmfSdlPackage#getCoreAbstractionsMultiplicityExpressionsMultiplicityElement_Upper()
	 * @model dataType="hub.sam.sdl.PrimitiveTypesUnlimitedNatural" derived="true" ordered="false"
	 * @generated
	 */
	String getUpper();

	/**
	 * Sets the value of the '{@link hub.sam.sdl.CoreAbstractionsMultiplicityExpressionsMultiplicityElement#getUpper <em>Upper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Upper</em>' attribute.
	 * @see #getUpper()
	 * @generated
	 */
	void setUpper(String value);

	/**
	 * Returns the value of the '<em><b>Upper Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Upper Value</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Upper Value</em>' containment reference.
	 * @see #setUpperValue(CoreAbstractionsExpressionsValueSpecification)
	 * @see hub.sam.sdl.EmfSdlPackage#getCoreAbstractionsMultiplicityExpressionsMultiplicityElement_UpperValue()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
	CoreAbstractionsExpressionsValueSpecification getUpperValue();

	/**
	 * Sets the value of the '{@link hub.sam.sdl.CoreAbstractionsMultiplicityExpressionsMultiplicityElement#getUpperValue <em>Upper Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Upper Value</em>' containment reference.
	 * @see #getUpperValue()
	 * @generated
	 */
	void setUpperValue(CoreAbstractionsExpressionsValueSpecification value);

	/**
	 * Returns the value of the '<em><b>Lower Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lower Value</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lower Value</em>' containment reference.
	 * @see #setLowerValue(CoreAbstractionsExpressionsValueSpecification)
	 * @see hub.sam.sdl.EmfSdlPackage#getCoreAbstractionsMultiplicityExpressionsMultiplicityElement_LowerValue()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
	CoreAbstractionsExpressionsValueSpecification getLowerValue();

	/**
	 * Sets the value of the '{@link hub.sam.sdl.CoreAbstractionsMultiplicityExpressionsMultiplicityElement#getLowerValue <em>Lower Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lower Value</em>' containment reference.
	 * @see #getLowerValue()
	 * @generated
	 */
	void setLowerValue(CoreAbstractionsExpressionsValueSpecification value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model dataType="hub.sam.sdl.PrimitiveTypesInteger" required="true" ordered="false"
	 * @generated
	 */
	String lower();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model dataType="hub.sam.sdl.PrimitiveTypesUnlimitedNatural" required="true" ordered="false"
	 * @generated
	 */
	String upper();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model dataType="hub.sam.sdl.PrimitiveTypesInteger" required="true" ordered="false"
	 * @generated
	 */
	String lowerBound();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model dataType="hub.sam.sdl.PrimitiveTypesUnlimitedNatural" required="true" ordered="false"
	 * @generated
	 */
	String upperBound();

} // CoreAbstractionsMultiplicityExpressionsMultiplicityElement
