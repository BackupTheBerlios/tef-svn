/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package hub.sam.sdl.validation;

import hub.sam.sdl.EvaluationExpression;
import hub.sam.sdl.SdlVariable;

/**
 * A sample validator interface for {@link hub.sam.sdl.SdlAssignment}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface SdlAssignmentValidator {
	boolean validate();

	boolean validateExpression(EvaluationExpression value);
	boolean validateVariable(SdlVariable value);
}
