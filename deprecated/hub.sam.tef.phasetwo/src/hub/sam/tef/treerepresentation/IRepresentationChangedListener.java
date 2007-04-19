package hub.sam.tef.treerepresentation;

public interface IRepresentationChangedListener {
	public void contentChanged(int start, int length, String text);
}
