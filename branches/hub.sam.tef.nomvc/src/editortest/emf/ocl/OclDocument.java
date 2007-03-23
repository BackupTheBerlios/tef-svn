package editortest.emf.ocl;

import hub.sam.tef.TEFDocument;
import hub.sam.tef.controllers.IAnnotationModelProvider;
import hub.sam.tef.templates.Template;
import editortest.emf.ocl.templates.ConstraintTemplate;

public class OclDocument extends TEFDocument {

	@Override
	public Template createTopLevelTemplate(IAnnotationModelProvider annotationModelProvider) {
		return new ConstraintTemplate(getModelProvider(), getModel().getMetaElement("Constraint"));
	}

}
