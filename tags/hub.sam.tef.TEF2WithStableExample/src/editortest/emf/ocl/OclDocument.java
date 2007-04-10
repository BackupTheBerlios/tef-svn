package editortest.emf.ocl;

import hub.sam.tef.TEFDocument;
import hub.sam.tef.annotations.IAnnotationModelProvider;
import hub.sam.tef.annotations.IIdentifierResolver;
import hub.sam.tef.syntax.AbstractLayoutManager;
import hub.sam.tef.syntax.ExpressionLayout;
import hub.sam.tef.templates.Template;
import editortest.emf.EMFIdentifierResolver;
import editortest.emf.ocl.annotations.OclIdentifierResolver;
import editortest.emf.ocl.templates.ConstraintTemplate;

public class OclDocument extends TEFDocument {
	
	@Override
	public Template createTopLevelTemplate(IAnnotationModelProvider annotationModelProvider) {
		return new ConstraintTemplate(getModelProvider(), getModelProvider().getModel().getMetaElement("Constraint"));
	}

	public IIdentifierResolver getIdentityResolver() {
		return new OclIdentifierResolver();
	}

	@Override
	protected AbstractLayoutManager createLayout() {
		return new ExpressionLayout();
	}

}
