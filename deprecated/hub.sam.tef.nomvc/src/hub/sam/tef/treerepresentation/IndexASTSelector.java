package hub.sam.tef.treerepresentation;

import hub.sam.util.trees.IChildSelector;

public class IndexASTSelector implements IChildSelector<ASTElementNode> {

	private int start;
	private int length;

	public IndexASTSelector(int start, int length) {
		super();
		this.start = start;
		this.length = length;
	}

	public boolean selectChild(ASTElementNode node) {
		int nodeOffset = node.getAbsoluteOffset(0);
		return nodeOffset <= start && nodeOffset + node.getLength() >= start+length;					
	}
}
