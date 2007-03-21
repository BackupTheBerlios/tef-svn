package hub.sam.tef.treerepresentation;

import hub.sam.tef.DocumentModel;
import hub.sam.tef.controllers.IAnnotationModelProvider;
import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelElement;

import java.util.Collection;
import java.util.Vector;

import org.eclipse.jface.text.Position;

public class SemanticsContext {

	private final IAnnotationModelProvider fAnnotationModelProvider;	
	private final DocumentModel fDocumentModel;
	private final IModel fModel;

	public SemanticsContext(final IAnnotationModelProvider annotationModelProvider, 
			DocumentModel documentModel, IModel model) {
		super();
		fAnnotationModelProvider = annotationModelProvider;
		fDocumentModel = documentModel;
		fModel = model;
	}

	public IAnnotationModelProvider getAnnotationModelProvider() {
		return fAnnotationModelProvider;
	}	
	
	public void addModelElementOccurence(IModelElement element, Position occurence) {
		fDocumentModel.addModelElementOccurence(element, occurence);		
	}
	
	public Collection<IModelElement> getValidElements(IMetaModelElement metaModelElement) {
		Collection<IModelElement> result = new Vector<IModelElement>();
		for (Object o: fModel.getElementExceptEditedResource(metaModelElement)) {
			result.add((IModelElement)o);
		}
		collectValidElementsFromModel(fDocumentModel.getTopLevelModelElement(), metaModelElement, result);
		return result;
	}
	
	private void collectValidElementsFromModel(IModelElement element, IMetaModelElement metaModelElement, 
			Collection<IModelElement> values) {
		if (element.getMetaElement().equals(metaModelElement)) {
			values.add(element);
		}
		for (Object o: element.getComponents()) {
			collectValidElementsFromModel((IModelElement)o, metaModelElement, values);
		}
	}
}
