package hub.sam.tef;

import org.eclipse.jface.text.source.Annotation;

public class ErrorAnnotation extends Annotation {

	public static final String TEF_ERROR = "hub.sam.tef.error";
	
	public ErrorAnnotation() {
		super(TEF_ERROR, false, "An error...");
	}
	
}
