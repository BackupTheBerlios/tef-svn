package hub.sam.tef.treerepresentation;

import hub.sam.util.trees.IChildSelector;

public class IndexTreeRepresentationSelector implements IChildSelector<TreeRepresentation> {

	private int start;
	private int length;

	public IndexTreeRepresentationSelector(int start, int length) {
		super();
		this.start = start;
		this.length = length;
	}

	public boolean selectChild(TreeRepresentation node) {
		int nodeOffset = node.getAbsoluteOffset(0);
		return nodeOffset <= start && nodeOffset + node.getLength() >= start+length;					
	}
}
