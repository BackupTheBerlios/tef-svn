package editortest.emf.ocl;

import hub.sam.tef.TEFDocument;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.adaptors.IAnnotationModelProvider;
import hub.sam.tef.templates.adaptors.IIdentifierResolver;
import hub.sam.tef.templates.layout.AbstractLayoutManager;
import hub.sam.tef.templates.layout.ExpressionLayout;
import editortest.emf.EMFIdentifierResolver;
import editortest.emf.ocl.templates.ConstraintTemplate;

public class OclDocument extends TEFDocument {
	
	@Override
	public Template createTopLevelTemplate(IAnnotationModelProvider annotationModelProvider) {
		return new ConstraintTemplate(getModelProvider(), getModelProvider().getModel().getMetaElement("Constraint"));
	}

	public IIdentifierResolver getIdentityResolver() {
		return new EMFIdentifierResolver();
	}

	@Override
	protected AbstractLayoutManager createLayout() {
		return new ExpressionLayout();
	}

}
