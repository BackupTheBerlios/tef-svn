/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package hub.sam.sdl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Communication Process</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hub.sam.sdl.CommunicationProcess#getInput <em>Input</em>}</li>
 * </ul>
 * </p>
 *
 * @see hub.sam.sdl.EmfSdlPackage#getCommunicationProcess()
 * @model abstract="true"
 * @generated
 */
public interface CommunicationProcess extends CoreAbstractionsElementsElement {
	/**
	 * Returns the value of the '<em><b>Input</b></em>' containment reference list.
	 * The list contents are of type {@link hub.sam.sdl.CommunicationListener}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input</em>' containment reference list.
	 * @see hub.sam.sdl.EmfSdlPackage#getCommunicationProcess_Input()
	 * @model containment="true" derived="true" ordered="false"
	 * @generated
	 */
	EList<CommunicationListener> getInput();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model dataType="hub.sam.sdl.PrimitiveTypesBoolean" required="true" ordered="false" listenerRequired="true" listenerOrdered="false" eventRequired="true" eventOrdered="false"
	 * @generated
	 */
	String consume(CommunicationListener listener, CommunicationEvent event);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void run();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void start();

} // CommunicationProcess
