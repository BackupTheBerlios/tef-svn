package hub.sam.tef.treerepresentation;


public interface ITreeRepresentationProvider {
	
	public Object createTreeRepresentation(String property, Object model);
	
	public void updateTreeRepresentation(TreeRepresentation treeRepresentation, String property, Object model);
	
	public boolean compare(TreeRepresentationLeaf treeRepresentation, String property, Object model);

}
