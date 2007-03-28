package hub.sam.tef.completion;

import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;

public class CompletionContextInformation implements IContextInformation {

	private final String contextDisplayString;
	private final String informationDisplayString;
	
	public CompletionContextInformation(final String contextDisplayString, final String informationDisplayString) {
		super();
		this.contextDisplayString = contextDisplayString;
		this.informationDisplayString = informationDisplayString;
	}

	public String getContextDisplayString() {
		return contextDisplayString;
	}

	public Image getImage() {
		return null;
	}

	public String getInformationDisplayString() {
		return informationDisplayString;
	}
}
