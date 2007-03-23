package hub.sam.tef.parse;

import hub.sam.tef.treerepresentation.SemanticsContext;
import hub.sam.tef.treerepresentation.ASTElementNode;

public interface ISemanticProvider {

	public void check(ASTElementNode representation, SemanticsContext context); 
			
}
