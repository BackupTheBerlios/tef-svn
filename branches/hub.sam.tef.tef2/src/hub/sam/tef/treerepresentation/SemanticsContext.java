package hub.sam.tef.treerepresentation;

import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.adaptors.IAnnotationModelProvider;
import hub.sam.tef.templates.adaptors.IDocumentModelProvider;
import hub.sam.tef.templates.adaptors.IIdentifierResolver;
import hub.sam.tef.templates.adaptors.ILanguageModelProvider;

import org.eclipse.jface.text.Position;

public class SemanticsContext {

	private final IDocumentModelProvider fDocumentModelProvider;
	private final ILanguageModelProvider fLanguageModelProvider;
	private final IModelElement fNewModel;

	public SemanticsContext(final IDocumentModelProvider documentProvider, final ILanguageModelProvider languageModelProvider,
			IModelElement newModel) {
		super();
		fDocumentModelProvider = documentProvider;
		fLanguageModelProvider = languageModelProvider;
		fNewModel = newModel;
	}

	public IAnnotationModelProvider getAnnotationModelProvider() {
		return fDocumentModelProvider.getAnnotationModelProvider();
	}	
	
	public void addModelElementOccurence(IModelElement element, Position occurence) {
		fDocumentModelProvider.addModelElementOccurence(element, occurence);		
	}
		
	public IIdentifierResolver getIdentifierResolver() {
		return fLanguageModelProvider.getIdentityResolver();
	}
	
	public IModelElement getNewModel() {
		return fNewModel;
	}
	
}
