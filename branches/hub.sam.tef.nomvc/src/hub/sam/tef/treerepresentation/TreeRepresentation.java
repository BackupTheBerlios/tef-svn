package hub.sam.tef.treerepresentation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TreeRepresentation extends TreeRepresentationLeaf {
	
	private final Collection<IRepresentationChangedListener> fListeners = new Vector<IRepresentationChangedListener>();	
	private final List<TreeRepresentationLeaf> fContents = new Vector<TreeRepresentationLeaf>();
	private final Map<Object, Integer> fContentsMap = new HashMap<Object, Integer>();
	private final Map<Object, Integer> fChildrensMap = new HashMap<Object, Integer>();
	private final StringBuffer fStringContent = new StringBuffer("");	
	private final Map<Integer, RepresentationChangedListener> fChangeListeners = new 	HashMap<Integer, RepresentationChangedListener>();
	private final List<TreeRepresentation> fChildren = new Vector<TreeRepresentation>();
			
	private final Collection<IDisposable> fComponents = new Vector<IDisposable>();
			
	public TreeRepresentation(ITreeContents contents) {
		super(contents);	
		contents.setTreeRepresentation(this);
	}

	class RepresentationChangedListener implements IRepresentationChangedListener {
		private final TreeRepresentation node;
	
		public RepresentationChangedListener(final TreeRepresentation node) {
			super();
			this.node = node;
		}

		public void contentChanged(int start, int length, String text) {
			replaceStringContent(start + node.getOffsetWithInParent(0), length, text);
		}		
	}
	
	public void addContent(Object key, Object content) {
		TreeRepresentationLeaf treeRepresentationContent = createTreeRepresentationContent(content);		
		
		// add conent
		int index = fContents.size();
		if (key != null) {
			fContentsMap.put(key, index);
		}		
		fContents.add(treeRepresentationContent);				
		
		if (content instanceof TreeRepresentation) {
			// add listeners
			RepresentationChangedListener listener = new RepresentationChangedListener((TreeRepresentation)content);
			fChangeListeners.put(index, listener);
			((TreeRepresentation)content).addRepresentationChangedListener(listener);
			
			// add content as child
			if (key != null) {
				fChildrensMap.put(key, fChildren.size());
			}
			fChildren.add((TreeRepresentation)content);
		}		
		
		// set parent/previous of content
		treeRepresentationContent.parent = this;
		treeRepresentationContent.previous = (fContents.size() <= 1) ? null : fContents.get(fContents.size() - 2);
		
		// add string content		
		addStringContent(treeRepresentationContent.getContent());
		checkConsistency();
	}

	public void addContent(Object content) {
		addContent(null, content);
	}
	
	private TreeRepresentationLeaf createTreeRepresentationContent(Object content) {
		if (content instanceof TreeRepresentationLeaf) {
			return (TreeRepresentationLeaf)content;
		} else {
			return new PrimitiveTreeRepresentation(content);
		}
	}	
	
	public void changeContent(Object key, Object newContentAsObject) {
		//int startIndexOfOldContent = startIndexOf(key);
		int index = fContentsMap.get(key);
		TreeRepresentationLeaf oldContent = fContents.get(index);		
		int startIndexOfOldContent = oldContent.getOffsetWithInParent(0);
		TreeRepresentationLeaf newObject = createTreeRepresentationContent(newContentAsObject);
		fContents.set(index, newObject);
		int oldContentLength = oldContent.getLength();
		
		// dispose the old content
		newObject.parent = this;
		newObject.previous = oldContent;		
		oldContent.parent = null;
		oldContent.previous = null;
		oldContent.dispose();
		
		// replace content as child
		Integer childIndex = fChildrensMap.get(key);
		if (childIndex != null) {
			if (newContentAsObject instanceof TreeRepresentation) {
				fChildren.set(childIndex, (TreeRepresentation)newContentAsObject);
			} else {
				fChildren.remove(childIndex);
			}
		} else {
			if (newContentAsObject instanceof TreeRepresentation) {
				throw new RuntimeException("assert");
			}
		}

		// reset the listeners
		RepresentationChangedListener listener = fChangeListeners.get(index);
		if (listener != null) {
			((TreeRepresentation)oldContent).removeRepresentationChangedListener(listener);
		}
		if (newContentAsObject instanceof TreeRepresentation) {
			((TreeRepresentation)newContentAsObject).addRepresentationChangedListener(
					new RepresentationChangedListener((TreeRepresentation)newContentAsObject));
		} else {
			fChangeListeners.remove(index);
		}		
		
		// replace the string content
		replaceStringContent(startIndexOfOldContent, oldContentLength, newObject.getContent());		
	}
	
	public TreeRepresentationLeaf getContent(Object key) {
		return fContents.get(fContentsMap.get(key));
	}
	
	public int getLength() {
		return fStringContent.length();
	}
	
	private void replaceStringContent(int start, int length, String stringContent) {
		fStringContent.replace(start, start + length, stringContent);		
		//recalculateStartingIndices(contentIndex, stringContent.length() - length);		
		checkConsistency();
		fireContentChangedListener(start, length, stringContent);
	}
	
	private void addStringContent(String content) {
		int oldLength = getLength();
		fStringContent.append(content);
		fireContentChangedListener(oldLength, 0, content);
	}

	public String getContent() {
		return fStringContent.toString();
	}
	
	public void dispose() {
		for (IDisposable component: fComponents) {
			component.dispose();
		}
		for (RepresentationChangedListener listener: fChangeListeners.values()) {
			listener.node.removeRepresentationChangedListener(listener);
		}
		fChangeListeners.clear();
		getElement().dispose();
		for (Object content: fContents) {
			if (content instanceof TreeRepresentation) {
				((TreeRepresentation)content).getElement().dispose();
				((TreeRepresentation)content).dispose();
			}
		}
		
		fContents.clear();
		fContentsMap.clear();
		//fStartIndexes.clear();
	}
	
	public void addRepresentationChangedListener(IRepresentationChangedListener listener) {
		fListeners.add(listener);
	}
	
	public void removeRepresentationChangedListener(IRepresentationChangedListener listener) {
		fListeners.remove(listener);
	}
	
	protected void fireContentChangedListener(int start, int length, String text) {
		for (IRepresentationChangedListener listener: fListeners) {
			listener.contentChanged(start, length, text);
		}
	}
	
	public void registerComponentListener(IDisposable component) {
		fComponents.add(component);
	}
	
	public void disconnect() {
		for (IDisposable component: fComponents) {
			component.dispose();
		}
		fComponents.clear();
		for (Object content: fContents) {
			if (content instanceof TreeRepresentation) {				
				((TreeRepresentation)content).disconnect();
			}
		}
	}

	public List<TreeRepresentation> getChildNodes() {
		return Collections.unmodifiableList(fChildren);
	}
	
	public String toString() {		
		return getElement().toString() + "[" + getContent() + "]";	
	}
	
	public boolean checkConsistency() {
		// content
		{
			StringBuffer required = new StringBuffer("");		
			for (TreeRepresentationLeaf content: fContents) {				
					required.append(content.getContent());				
			}
			if (!required.toString().equals(getContent())) {
				throw new RuntimeException("assert");
			}
		}	
		return true;
	}
	
	
}
