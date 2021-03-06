/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.ocl.internal.cst.validation;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ocl.internal.cst.OperationCS;

/**
 * A sample validator interface for {@link org.eclipse.emf.ocl.internal.cst.OperationContextDeclCS}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface OperationContextDeclCSValidator {
	boolean validate();

	boolean validateOperationCS(OperationCS value);
	boolean validatePrePostOrBodyDecls(EList value);
}
