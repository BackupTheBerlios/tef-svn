package hub.sam.tef.annotations;

import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.models.extensions.InternalModelElement;
import hub.sam.tef.treerepresentation.ASTElementNode;

public interface IIdentifierResolver {
	
	/**
	 * Implementing classes have the responsiblity to resolve identifiers and
	 * return an according object if the identifier can be resolved.
	 * 
	 * @param model
	 *            The overall model.
	 * @param node
	 *            The node in the syntax tree that represents the reference.
	 * @param context
	 *            The model element that owns the identifier.
	 * @param topLevelElement
	 *            The topLevelElement of the edited model.
	 * @param expectedType
	 *            The meta-type that the resolved element has to be assignable
	 *            to.
	 * @param property
	 *            The property of the context element that this identifier is
	 *            the value for.
	 * @return The resolved model element or null.
	 */
	public IModelElement resolveIdentifier(IModel model, ASTElementNode node, IModelElement context, 
			IModelElement topLevelElement, 
			IMetaModelElement expectedType, String property);
	
}
