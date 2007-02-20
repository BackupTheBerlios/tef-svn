package editortest.emf.ocl;

import hub.sam.tef.TEFDocument;
import hub.sam.tef.models.ICollection;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.views.DocumentText;
import hub.sam.tef.views.FixText;
import editortest.emf.model.EMFModel;
import editortest.emf.model.EMFModelElement;
import editortest.emf.model.EMFSequence;
import editortest.emf.ocl.templates.ConstraintTemplate;

public class OclDocument extends TEFDocument {
	
	@Override
	public DocumentText createDocument() {
		DocumentText result = new DocumentText(this);
		ICollection<IModelElement> outermostComposites = getModel().getOutermostComposites();
		IModelElement topLevelExpression = null;
		for (IModelElement o: outermostComposites) {
			if (o.getMetaElement().equals(getModel().getMetaElement("Constraint"))) {
				topLevelExpression = o;
				result.addText(new ConstraintTemplate(result, getModel().getMetaElement("Constraint")).createView(topLevelExpression));
				result.addText(new FixText("\n"));
			}
		}
		if (topLevelExpression == null) {
			topLevelExpression = ((EMFModel)getModel()).createElement(getModel().getMetaElement("Constraint"));
			((EMFSequence)getModel().getOutermostComposites()).getEMFObject().add(
					((EMFModelElement)topLevelExpression).getEMFObject());
			result.addText(new ConstraintTemplate(result, getModel().getMetaElement("Constraint")).
					createView(topLevelExpression));
		}		
		return result;
	}
}
