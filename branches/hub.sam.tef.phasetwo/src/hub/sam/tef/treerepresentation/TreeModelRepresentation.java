package hub.sam.tef.treerepresentation;

import hub.sam.util.trees.AbstractTree;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TreeModelRepresentation extends AbstractTree<TreeModelRepresentation, ITreeContents> {

	private final ITreeContents fElement;
	
	public TreeModelRepresentation(final ITreeContents contents) {
		super();
		this.fElement = contents;
	}
	
	private final Collection<IContentChangedListener> fListeners = new Vector<IContentChangedListener>();	
	private final List<Object> fContents = new Vector<Object>();
	private final List<Integer> fStartIndexes = new Vector<Integer>();
	private final Map<Object, Integer> fContentsMap = new HashMap<Object, Integer>();
	private final Map<Object, Integer> fChildrensMap = new HashMap<Object, Integer>();
	private final StringBuffer fStringContent = new StringBuffer("");	
	private final Map<Integer, ContentChangedListener> fContentChangeListeners = new 	HashMap<Integer, ContentChangedListener>();
	private final List<TreeModelRepresentation> fChildren = new Vector<TreeModelRepresentation>();
	private TreeModelRepresentation parent = null;
	
	class ContentChangedListener implements IContentChangedListener {
		protected final int index;		
		
		public ContentChangedListener(final int index) {
			super();
			this.index = index;
		}

		public void contentChanged(int start, int length, String text) {
			replaceStringContent(start + fStartIndexes.get(index), length, text);
		}		
	}
	
	public void addContent(Object key, Object content) {
		// add conent
		int index = fContents.size();
		if (key != null) {
			fContentsMap.put(key, index);
		}		
		fContents.add(content);				
		
		if (content instanceof TreeModelRepresentation) {
			// add listeners
			ContentChangedListener listener = new ContentChangedListener(index);
			fContentChangeListeners.put(index, listener);
			((TreeModelRepresentation)content).addContentChangedListener(listener);
			
			// add content as child
			if (key != null) {
				fChildrensMap.put(key, fChildren.size());
			}
			fChildren.add((TreeModelRepresentation)content);
			
			// set parent of content
			((TreeModelRepresentation)content).parent = this;
		}		
		
		// add string content
		String stringContent = stringOfContent(content);
		fStartIndexes.add( ((fStartIndexes.size() == 0) ? 0 : fStartIndexes.get(fStartIndexes.size() - 1)) +
				 stringContent.length());
		addStringContent(stringContent);
	}

	public void addContent(Object content) {
		addContent(null, content);
	}
	
	public void changeContent(Object key, Object newContent) {
		int startIndexOfOldContent = startIndexOf(key);
		int index = fContentsMap.get(key);
		Object oldContent = fContents.get(index);		
		fContents.set(index, newContent);
		int oldContentLength = lengthOfContent(oldContent);
		
		// dispose the old content
		if (oldContent instanceof TreeModelRepresentation) {
			((TreeModelRepresentation)oldContent).parent = null;
			((TreeModelRepresentation)oldContent).dispose();
		}
		
		// recalculate all starting indizes of all contents
		int newContentLength = lengthOfContent(newContent);
		for (int i = index; i < fStartIndexes.size(); i++) {
			fStartIndexes.set(i, fStartIndexes.get(i) - oldContentLength + newContentLength);
		}
		
		// replace content as child
		Integer childIndex = fChildrensMap.get(key);
		if (childIndex != null) {
			if (newContent instanceof TreeModelRepresentation) {
				fChildren.set(childIndex, (TreeModelRepresentation)newContent);
			} else {
				fChildren.remove(childIndex);
			}
		} else {
			if (newContent instanceof TreeModelRepresentation) {
				throw new RuntimeException("assert");
			}
		}

		// reset the listeners
		ContentChangedListener listener = fContentChangeListeners.get(index);
		if (listener == null) {
			listener = new ContentChangedListener(index);
		} else {
			((TreeModelRepresentation)oldContent).removeContentChangedListener(listener);
		}
		if (newContent instanceof TreeModelRepresentation) {
			((TreeModelRepresentation)newContent).addContentChangedListener(listener);
		} else {
			fContentChangeListeners.remove(index);
		}
		
		// set parent
		if (newContent instanceof TreeModelRepresentation) {
			((TreeModelRepresentation)newContent).parent = this;
		}
		
		// replace the string content
		replaceStringContent(startIndexOfOldContent, oldContentLength, stringOfContent(newContent));		
	}
	
	public int getLength() {
		return fStringContent.length();
	}
	
	private int startIndexOf(Object key) {
		return fStartIndexes.get(fContentsMap.get(key));
	}
	
	private void replaceStringContent(int start, int length, String newContent) {
		fStringContent.replace(start, start + length, newContent);
		fireContentChangedListener(start, length, newContent);
	}
	
	private void addStringContent(String content) {
		int oldLength = getLength();
		fStringContent.append(content);
		fireContentChangedListener(oldLength, 0, content);
	}
	
	private int lengthOfContent(Object content) {
		if (content instanceof TreeModelRepresentation) {
			return ((TreeModelRepresentation)content).getLength();
		} else {
			return content.toString().length();
		}
	}
	
	private String stringOfContent(Object content) {
		if (content instanceof TreeModelRepresentation) {
			return ((TreeModelRepresentation)content).getContent();
		} else {
			return content.toString();
		}
	}

	public String getContent() {
		return fStringContent.toString();
	}
	
	public void dispose() {
		for (ContentChangedListener listener: fContentChangeListeners.values()) {
			((TreeModelRepresentation)fContents.get(listener.index)).removeContentChangedListener(listener);
		}
		fContentChangeListeners.clear();
		for (Object content: fContents) {
			if (content instanceof TreeModelRepresentation) {				
				((TreeModelRepresentation)content).dispose();
			}
		}
		
		fContents.clear();
		fContentsMap.clear();
		fStartIndexes.clear();
	}
	
	public void addContentChangedListener(IContentChangedListener listener) {
		fListeners.add(listener);
	}
	
	public void removeContentChangedListener(IContentChangedListener listener) {
		fListeners.remove(listener);
	}
	
	protected void fireContentChangedListener(int start, int length, String text) {
		for (IContentChangedListener listener: fListeners) {
			listener.contentChanged(start, length, text);
		}
	}

	public List<TreeModelRepresentation> getChildNodes() {
		return Collections.unmodifiableList(fChildren);
	}

	public ITreeContents getElement() {
		return fElement;
	}

	public TreeModelRepresentation getParent() {
		return parent;
	}
	
	public String toString() {
		return "%" + getElement().toString();
	}
}
