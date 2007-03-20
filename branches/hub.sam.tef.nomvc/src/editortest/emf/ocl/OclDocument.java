package editortest.emf.ocl;

import org.eclipse.jface.text.IDocument;

import hub.sam.tef.TEFDocument;
import hub.sam.tef.controllers.IAnnotationModelProvider;
import hub.sam.tef.controllers.ICursorPostionProvider;
import hub.sam.tef.templates.Template;
import editortest.emf.expressions.ExpressionDocument;
import editortest.emf.ocl.templates.ConstraintTemplate;

public class OclDocument extends TEFDocument {

	/*
	@Override
	public void initializeDocument(DocumentText result) {				
		ICollection<IModelElement> outermostComposites = getModel().getOutermostComposites(getResource());
		IModelElement topLevelExpression = null;
		for (IModelElement o: outermostComposites) {
			if (o.getMetaElement().equals(getModel().getMetaElement("Constraint"))) {
				topLevelExpression = o;
				result.addText(((ElementTemplate)getTopLevelTemplate()).getView(topLevelExpression, null));
				result.addText(new FixText("\n"));
			}
		}
		if (topLevelExpression == null) {
			topLevelExpression = ((EMFModel)getModel()).createElement(getModel().getMetaElement("Constraint"));
			((EMFSequence)getModel().getOutermostComposites(getResource())).getEMFObject().add(
					((EMFModelElement)topLevelExpression).getEMFObject());
			result.addText(((ElementTemplate)getTopLevelTemplate()).getView(topLevelExpression, null));
		}						
	}
	*/

	@Override
	public Template createTopLevelTemplate(IAnnotationModelProvider annotationModelProvider, ICursorPostionProvider cursorPositionProvider) {
		return new ConstraintTemplate(annotationModelProvider, cursorPositionProvider, getModelRepresentationProvider(), getModel().getMetaElement("Constraint"));
	}

}
