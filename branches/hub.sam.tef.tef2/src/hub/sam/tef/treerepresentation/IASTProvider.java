package hub.sam.tef.treerepresentation;

import hub.sam.tef.annotations.SemanticsContext;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.syntax.AbstractLayoutManager;


public interface IASTProvider {
	
	public ASTNode createTreeRepresentation(IModelElement owner, String property, Object model, boolean isComposite, AbstractLayoutManager layout);
	
	public Object createCompositeModel(IModelElement owner, String property, ASTNode tree, boolean isComposite);
	
	public Object createReferenceModel(IModelElement owner, String property, ASTNode tree, boolean isComposite, SemanticsContext context);
	
	//public void connectTreeToModel(TreeRepresentation tree);
	
}
