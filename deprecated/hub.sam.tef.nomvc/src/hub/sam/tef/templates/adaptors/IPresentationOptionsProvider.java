package hub.sam.tef.templates.adaptors;

import hub.sam.tef.treerepresentation.ASTElementNode;

public interface IPresentationOptionsProvider {
	/**
	 * If tree is returned the model element represented by the tree representation is marked as occurence.
	 * @param treeRepresentation The tree representation with the cursor on
	 * @param localOffset The cursor position relative tp the tree representation
	 */
	public boolean markOccurences(ASTElementNode treeRepresentation, int localOffset);
}
