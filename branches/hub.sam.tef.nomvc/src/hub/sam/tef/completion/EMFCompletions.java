package hub.sam.tef.completion;

import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelElement;

import java.util.Collection;
import java.util.Vector;

public class EMFCompletions {
	public static Collection<CompletionContextInformation> createProposals(String metaModelElement, CompletionContext context) {
		String identifierPrefix = context.getIdentifierPrefix();
		Collection<CompletionContextInformation> result = new Vector<CompletionContextInformation>();
		Collection<IModelElement> validElements = getValidElements(context.getDocumentModelProvider().getModel(), 
				context.getDocumentModelProvider().getModel().getMetaElement(metaModelElement), 
				context.getDocumentModelProvider().getTopLevelElement());
		
		for (IModelElement possibleElement: validElements) {
			String name = (String)possibleElement.getValue("name");
			if (name != null && name.startsWith(identifierPrefix) && ! name.equals(identifierPrefix)) {
				result.add(new CompletionContextInformation(name.substring(identifierPrefix.length(), name.length()), name));
			}
		}
		return result;
	}

	public static Collection<IModelElement> getValidElements(IModel model, IMetaModelElement metaModelElement, IModelElement topLevelElement) {
		Collection<IModelElement> result = new Vector<IModelElement>();
		for (Object o: model.getElementExceptEditedResource(metaModelElement)) {
			result.add((IModelElement)o);
		}
		collectValidElementsFromModel(topLevelElement, 	metaModelElement, result);
		return result;
	}
	
	private static void collectValidElementsFromModel(IModelElement element, IMetaModelElement metaModelElement, 
			Collection<IModelElement> values) {
		if (element.getMetaElement().equals(metaModelElement)) {
			values.add(element);
		}
		for (Object o: element.getComponents()) {
			collectValidElementsFromModel((IModelElement)o, metaModelElement, values);
		}
	}
}
