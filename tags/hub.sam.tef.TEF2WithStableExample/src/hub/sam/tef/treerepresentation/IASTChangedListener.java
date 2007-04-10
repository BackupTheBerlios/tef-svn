package hub.sam.tef.treerepresentation;

public interface IASTChangedListener {
	public void contentChanged(int start, int length, String text);
}
