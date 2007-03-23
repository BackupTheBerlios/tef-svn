package editortest.emf.ocl;

import hub.sam.tef.TEFDocument;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.adaptors.IAnnotationModelProvider;
import editortest.emf.ocl.templates.ConstraintTemplate;

public class OclDocument extends TEFDocument {

	@Override
	public Template createTopLevelTemplate(IAnnotationModelProvider annotationModelProvider) {
		return new ConstraintTemplate(getModelProvider(), getModel().getMetaElement("Constraint"));
	}

}
