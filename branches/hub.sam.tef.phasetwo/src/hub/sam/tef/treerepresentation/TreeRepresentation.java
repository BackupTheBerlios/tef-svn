package hub.sam.tef.treerepresentation;

import hub.sam.util.trees.AbstractTree;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TreeRepresentation extends AbstractTree<TreeRepresentation, ITreeContents> {

	private final ITreeContents fElement;
	
	public TreeRepresentation(final ITreeContents contents) {
		super();
		this.fElement = contents;
	}
	
	private final Collection<IRepresentationChangedListener> fListeners = new Vector<IRepresentationChangedListener>();	
	private final List<Object> fContents = new Vector<Object>();
	private final List<Integer> fStartIndexes = new Vector<Integer>();
	private final Map<Object, Integer> fContentsMap = new HashMap<Object, Integer>();
	private final Map<Object, Integer> fChildrensMap = new HashMap<Object, Integer>();
	private final StringBuffer fStringContent = new StringBuffer("");	
	private final Map<Integer, RepresentationChangedListener> fChangeListeners = new 	HashMap<Integer, RepresentationChangedListener>();
	private final List<TreeRepresentation> fChildren = new Vector<TreeRepresentation>();
	private TreeRepresentation parent = null;
	private final Collection<IDisposable> fComponents = new Vector<IDisposable>();
	
	class RepresentationChangedListener implements IRepresentationChangedListener {
		protected final int index;		
		
		public RepresentationChangedListener(final int index) {
			super();
			this.index = index;
		}

		public void contentChanged(int start, int length, String text) {
			replaceStringContent(start + fStartIndexes.get(index), length, text, index);
		}		
	}
	
	public void addContent(Object key, Object content) {
		// add conent
		int index = fContents.size();
		if (key != null) {
			fContentsMap.put(key, index);
		}		
		fContents.add(content);				
		
		if (content instanceof TreeRepresentation) {
			// add listeners
			RepresentationChangedListener listener = new RepresentationChangedListener(index);
			fChangeListeners.put(index, listener);
			((TreeRepresentation)content).addRepresentationChangedListener(listener);
			
			// add content as child
			if (key != null) {
				fChildrensMap.put(key, fChildren.size());
			}
			fChildren.add((TreeRepresentation)content);
			
			// set parent of content
			((TreeRepresentation)content).parent = this;
			
		}		
		
		// add string content
		String stringContent = stringOfContent(content);
		fStartIndexes.add( (fStartIndexes.size() == 0) ? 0 : 
				fStartIndexes.get(fStartIndexes.size() - 1) + lengthOfContent(fContents.get(fStartIndexes.size() - 1)));
		addStringContent(stringContent);
		checkConsistency();
	}

	public void addContent(Object content) {
		addContent(null, content);
	}
	
	private void recalculateStartingIndices(int from, int change) {
		for (int i = from + 1; i < fStartIndexes.size(); i++) {
			fStartIndexes.set(i, fStartIndexes.get(i) + change);
		}
	}
	
	public void changeContent(Object key, Object newContent) {
		int startIndexOfOldContent = startIndexOf(key);
		int index = fContentsMap.get(key);
		Object oldContent = fContents.get(index);		
		fContents.set(index, newContent);
		int oldContentLength = lengthOfContent(oldContent);
		
		// dispose the old content
		if (oldContent instanceof TreeRepresentation) {
			((TreeRepresentation)oldContent).parent = null;
			((TreeRepresentation)oldContent).dispose();
		}
		
		// replace content as child
		Integer childIndex = fChildrensMap.get(key);
		if (childIndex != null) {
			if (newContent instanceof TreeRepresentation) {
				fChildren.set(childIndex, (TreeRepresentation)newContent);
			} else {
				fChildren.remove(childIndex);
			}
		} else {
			if (newContent instanceof TreeRepresentation) {
				throw new RuntimeException("assert");
			}
		}

		// reset the listeners
		RepresentationChangedListener listener = fChangeListeners.get(index);
		if (listener == null) {
			listener = new RepresentationChangedListener(index);
		} else {
			((TreeRepresentation)oldContent).removeRepresentationChangedListener(listener);
		}
		if (newContent instanceof TreeRepresentation) {
			((TreeRepresentation)newContent).addRepresentationChangedListener(listener);
		} else {
			fChangeListeners.remove(index);
		}
		
		// set parent
		if (newContent instanceof TreeRepresentation) {
			((TreeRepresentation)newContent).parent = this;
		}
		
		// replace the string content
		replaceStringContent(startIndexOfOldContent, oldContentLength, stringOfContent(newContent), index);		
	}
	
	public int getLength() {
		return fStringContent.length();
	}
	
	private int startIndexOf(Object key) {
		return fStartIndexes.get(fContentsMap.get(key));
	}
	
	private void replaceStringContent(int start, int length, String newContent, int contentIndex) {
		fStringContent.replace(start, start + length, newContent);		
		recalculateStartingIndices(contentIndex, newContent.length() - length);		
		checkConsistency();
		fireContentChangedListener(start, length, newContent);
	}
	
	private void addStringContent(String content) {
		int oldLength = getLength();
		fStringContent.append(content);
		fireContentChangedListener(oldLength, 0, content);
	}
	
	private int lengthOfContent(Object content) {
		if (content instanceof TreeRepresentation) {
			return ((TreeRepresentation)content).getLength();
		} else {
			return content.toString().length();
		}
	}
	
	private String stringOfContent(Object content) {
		if (content instanceof TreeRepresentation) {
			return ((TreeRepresentation)content).getContent();
		} else {
			return content.toString();
		}
	}

	public String getContent() {
		return fStringContent.toString();
	}
	
	public void dispose() {
		for (IDisposable component: fComponents) {
			component.dispose();
		}
		for (RepresentationChangedListener listener: fChangeListeners.values()) {
			((TreeRepresentation)fContents.get(listener.index)).removeRepresentationChangedListener(listener);
		}
		fChangeListeners.clear();
		for (Object content: fContents) {
			if (content instanceof TreeRepresentation) {				
				((TreeRepresentation)content).dispose();
			}
		}
		
		fContents.clear();
		fContentsMap.clear();
		fStartIndexes.clear();
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
	
	public void registerComponent(IDisposable component) {
		fComponents.add(component);
	}

	public List<TreeRepresentation> getChildNodes() {
		return Collections.unmodifiableList(fChildren);
	}

	public ITreeContents getElement() {
		return fElement;
	}

	public TreeRepresentation getParent() {
		return parent;
	}
	
	public String toString() {
		return "%" + getElement().toString();
	}
	
	public boolean checkConsistency() {
		// content
		{
			StringBuffer required = new StringBuffer("");		
			for (Object content: fContents) {
				if (content instanceof TreeRepresentation) {
					required.append(((TreeRepresentation)content).getContent());
				} else {
					required.append(content.toString());
				}
			}
			if (!required.toString().equals(getContent())) {
				throw new RuntimeException("assert");
			}
		}
		
		// start indices
		{
			if (fContents.size() != fStartIndexes.size()) {			
				throw new RuntimeException("assert");
			}
			int required = 0;
			for (int i = 0; i < fContents.size(); i++) {
				if (fStartIndexes.get(i) != required) {
					throw new RuntimeException("assert");
				}
				required += lengthOfContent(fContents.get(i));
			}
		}
		return true;
	}
}
