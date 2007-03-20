package hub.sam.tef.treerepresentation;

import hub.sam.tef.controllers.IAnnotationModelProvider;

public class SemanticsContext {

	private final IAnnotationModelProvider fAnnotationModelProvider;

	public SemanticsContext(final IAnnotationModelProvider annotationModelProvider) {
		super();
		fAnnotationModelProvider = annotationModelProvider;
	}

	public IAnnotationModelProvider getAnnotationModelProvider() {
		return fAnnotationModelProvider;
	}	
	
}
