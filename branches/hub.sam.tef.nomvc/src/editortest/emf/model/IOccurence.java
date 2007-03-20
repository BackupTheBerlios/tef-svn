package editortest.emf.model;

import hub.sam.tef.treerepresentation.IWatchedDisposable;

public interface IOccurence extends IWatchedDisposable {
	
	public int getAbsolutOffset(int innerOffset);
	
	public int getLength();

}
