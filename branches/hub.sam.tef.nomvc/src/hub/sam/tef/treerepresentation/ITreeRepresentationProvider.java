package hub.sam.tef.treerepresentation;

import hub.sam.tef.models.IModelElement;


public interface ITreeRepresentationProvider {
	
	public TreeRepresentationLeaf createTreeRepresentation(IModelElement owner, String property, Object model, boolean isComposite);
	
	public Object createModel(IModelElement owner, String property, TreeRepresentationLeaf tree, boolean isComposite);
	
}
