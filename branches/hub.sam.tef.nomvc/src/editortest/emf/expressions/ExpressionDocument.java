package editortest.emf.expressions;

import hub.sam.tef.TEFDocument;
import hub.sam.tef.controllers.IAnnotationModelProvider;
import hub.sam.tef.controllers.ICursorPostionProvider;
import hub.sam.tef.templates.Template;
import editortest.emf.expressions.templates.ParenthesisTemplate;

public class ExpressionDocument extends TEFDocument {

	/*
	@Override
	public void initializeDocument(DocumentText result) {
		ICollection<IModelElement> outermostComposites = getModel().getOutermostComposites(getResource());
		IModelElement topLevelExpression = null;
		for (IModelElement o: outermostComposites) {
			if (o.getMetaElement().equals(getModel().getMetaElement("Parenthesis"))) {
				topLevelExpression = o;
				result.addText(((ElementTemplate)getTopLevelTemplate()).getView(topLevelExpression, null));
				result.addText(new FixText("\n"));
			}
		}
		if (topLevelExpression == null) {
			topLevelExpression = ((EMFModel)getModel()).createElement(getModel().getMetaElement("Parenthesis"));
			((EMFSequence)getModel().getOutermostComposites(getResource())).getEMFObject().add(
					((EMFModelElement)topLevelExpression).getEMFObject());
			result.addText(((ElementTemplate)getTopLevelTemplate()).getView(topLevelExpression, null));
		}				
	}
	*/

	@Override
	public Template createTopLevelTemplate(IAnnotationModelProvider annotationModelProvider, ICursorPostionProvider cursorPositionProvider) {
		return new ParenthesisTemplate(annotationModelProvider, cursorPositionProvider,
				getModelRepresentationProvider(), getModel().getMetaElement("Parenthesis"));
	}
}
