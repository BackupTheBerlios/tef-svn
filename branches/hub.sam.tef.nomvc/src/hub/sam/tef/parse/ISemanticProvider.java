package hub.sam.tef.parse;

import hub.sam.tef.treerepresentation.SemanticsContext;
import hub.sam.tef.treerepresentation.TreeRepresentation;

public interface ISemanticProvider {

	public void checkAndResolve(TreeRepresentation representation, SemanticsContext context); 
			
}
