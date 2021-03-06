/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.ocl.internal.cst.validation;

import org.eclipse.emf.ocl.internal.cst.OCLExpressionCS;
import org.eclipse.emf.ocl.internal.cst.VariableCS;

/**
 * A sample validator interface for {@link org.eclipse.emf.ocl.internal.cst.LoopExpCS}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface LoopExpCSValidator {
	boolean validate();

	boolean validateVariable1(VariableCS value);
	boolean validateVariable2(VariableCS value);
	boolean validateBody(OCLExpressionCS value);
}
