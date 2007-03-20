package hub.sam.tef.treerepresentation;

public interface IWatchedDisposable extends IDisposable {
	
	public void addDisposableStatusListener(IDisposbaleStatusListener statusListener);
	
	public void removeDisposableStatusListener(IDisposbaleStatusListener statusListener);
}
