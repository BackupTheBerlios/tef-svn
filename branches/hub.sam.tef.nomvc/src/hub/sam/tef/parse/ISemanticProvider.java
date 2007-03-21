package hub.sam.tef.parse;

import hub.sam.tef.treerepresentation.SemanticsContext;
import hub.sam.tef.treerepresentation.TreeRepresentation;

public interface ISemanticProvider {

	public void check(TreeRepresentation representation, SemanticsContext context); 
			
}
