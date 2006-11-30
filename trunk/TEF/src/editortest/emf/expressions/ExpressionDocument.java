package editortest.emf.expressions;

import hub.sam.tef.TEFDocument;
import hub.sam.tef.models.ICollection;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.views.DocumentText;
import hub.sam.tef.views.FixText;
import editortest.emf.ecoretemplates.EPackageTemplate;
import editortest.emf.model.EMFModel;
import editortest.emf.model.EMFModelElement;
import editortest.emf.model.EMFSequence;

public class ExpressionDocument extends TEFDocument {

	@Override
	public DocumentText createDocument() {
		DocumentText result = new DocumentText(this);
		ICollection<IModelElement> outermostComposites = getModel().getOutermostComposites();
		IModelElement topLevelPackage = null;
		for (IModelElement o: outermostComposites) {
			if (o.getMetaElement().equals(getModel().getMetaElement("Parathesis"))) {
				topLevelPackage = o;
				result.addText(new EPackageTemplate(result).createView(topLevelPackage));
				result.addText(new FixText("\n"));
			}
		}
		if (topLevelPackage == null) {
			topLevelPackage = ((EMFModel)getModel()).createElement(getModel().getMetaElement("Paranthesis"));
			((EMFSequence)getModel().getOutermostComposites()).getEMFObject().add(
					((EMFModelElement)topLevelPackage).getEMFObject());
			//getModel().getOutermostComposites().add(topLevelPackage);
			result.addText(new EPackageTemplate(result).createView(topLevelPackage));
		}		
		return result;
	}

	
}
