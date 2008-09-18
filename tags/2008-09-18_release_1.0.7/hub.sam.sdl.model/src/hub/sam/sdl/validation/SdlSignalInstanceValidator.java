/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package hub.sam.sdl.validation;

import hub.sam.sdl.SdlAgentInstance;
import hub.sam.sdl.SdlVariableSlot;

/**
 * A sample validator interface for {@link hub.sam.sdl.SdlSignalInstance}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface SdlSignalInstanceValidator {
	boolean validate();

	boolean validateParameter(SdlVariableSlot value);
	boolean validateSender(SdlAgentInstance value);
	boolean validateReceiver(SdlAgentInstance value);
}
