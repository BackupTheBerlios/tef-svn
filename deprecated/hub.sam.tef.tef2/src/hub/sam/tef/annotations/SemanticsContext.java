package hub.sam.tef.annotations;

import hub.sam.tef.IDocumentModelProvider;
import hub.sam.tef.ILanguageModelProvider;
import hub.sam.tef.models.IModelElement;

import org.eclipse.jface.text.Position;

public class SemanticsContext {

	private final IDocumentModelProvider fDocumentModelProvider;
	private final ILanguageModelProvider fLanguageModelProvider;
	private final IModelElement fNewModel;
	
	private final IIdentifierResolver fResolver;

	public SemanticsContext(final IDocumentModelProvider documentProvider, final ILanguageModelProvider languageModelProvider,
			IModelElement newModel) {
		super();
		fDocumentModelProvider = documentProvider;
		fLanguageModelProvider = languageModelProvider;
		fNewModel = newModel;
		fResolver = fLanguageModelProvider.getIdentityResolver();
	}

	public IAnnotationModelProvider getAnnotationModelProvider() {
		return fDocumentModelProvider.getAnnotationModelProvider();
	}	
	
	public void addModelElementOccurence(IModelElement element, Position occurence) {
		fDocumentModelProvider.addModelElementOccurence(element, occurence);		
	}
		
	public IIdentifierResolver getIdentifierResolver() {
		return fResolver;
	}
	
	public IModelElement getNewModel() {
		return fNewModel;
	}
	
}
