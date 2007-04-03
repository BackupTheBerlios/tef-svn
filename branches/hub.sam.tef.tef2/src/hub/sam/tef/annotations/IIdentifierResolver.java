package hub.sam.tef.annotations;

import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.treerepresentation.ASTElementNode;

public interface IIdentifierResolver {
	
	public IModelElement resolveIdentifier(IModel model, ASTElementNode node, IModelElement context, 
			IModelElement topLevelElement, 
			IMetaModelElement expectedType, String property);
	
}
