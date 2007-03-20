package hub.sam.tef.treerepresentation;

import java.util.Collections;
import java.util.List;

import hub.sam.util.trees.AbstractTree;

public abstract class TreeRepresentationLeaf extends AbstractTree<TreeRepresentationLeaf, ITreeContents> {
	
	protected TreeRepresentation parent = null;
	protected TreeRepresentationLeaf previous = null;
	
	private ITreeContents fElement;	
	
	protected TreeRepresentationLeaf(final ITreeContents contents) {
		super();
		this.fElement = contents;
	}
	
	public final TreeRepresentation getParent() {
		return parent;
	}

	public List<? extends TreeRepresentationLeaf> getChildNodes() {
		return Collections.EMPTY_LIST;
	}

	public final ITreeContents getElement() {
		return fElement;
	}
	
	public final void setElement(ITreeContents contents) {
		if (fElement != null && fElement instanceof IDisposable) {
			((IDisposable)fElement).dispose();
		}
		fElement = contents;
		if (this instanceof TreeRepresentation) {
			contents.setTreeRepresentation((TreeRepresentation)this);
		}
	}
	
	public abstract int getLength();
	
	public int getOffsetWithInParent(int offset) {
		if (previous == null) {
			return offset;
		} else {
			return previous.getOffsetWithInParent(0) + previous.getLength() + offset; 
		}
	}
	
	public int getAbsoluteOffset(int offset) {
		if (parent == null) {
			return offset;
		} else {
			return parent.getAbsoluteOffset(0) + getOffsetWithInParent(offset);
		}
	}	
	
	public abstract String getContent();
	
	public abstract void dispose();
}
