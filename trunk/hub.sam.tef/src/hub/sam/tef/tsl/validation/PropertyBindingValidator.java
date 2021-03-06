/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package hub.sam.tef.tsl.validation;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * A sample validator interface for {@link hub.sam.tef.tsl.PropertyBinding}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface PropertyBindingValidator {
	boolean validate();

	boolean validateProperty(EStructuralFeature value);

	boolean validateDefault(String value);

	boolean validateBindingId(String value);

	boolean validatePropertyBinding(EStructuralFeature value);
}
