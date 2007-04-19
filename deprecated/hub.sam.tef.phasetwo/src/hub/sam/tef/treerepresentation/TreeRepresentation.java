package hub.sam.tef.treerepresentation;

import hub.sam.tef.parse.TextBasedAST;
import hub.sam.tef.parse.TextBasedUpdatedAST;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import sun.rmi.transport.proxy.CGIHandler;

public class TreeRepresentation extends TreeRepresentationLeaf {
	
	private final Collection<IRepresentationChangedListener> fListeners = new Vector<IRepresentationChangedListener>();	
	private final List<TreeRepresentationLeaf> fContents = new Vector<TreeRepresentationLeaf>();
	//private final List<Integer> fStartIndexes = new Vector<Integer>();
	private final Map<Object, Integer> fContentsMap = new HashMap<Object, Integer>();
	private final Map<Object, Integer> fChildrensMap = new HashMap<Object, Integer>();
	private final StringBuffer fStringContent = new StringBuffer("");	
	private final Map<Integer, RepresentationChangedListener> fChangeListeners = new 	HashMap<Integer, RepresentationChangedListener>();
	private final List<TreeRepresentation> fChildren = new Vector<TreeRepresentation>();
			
	private final Collection<IDisposable> fComponents = new Vector<IDisposable>();
		
	
	public TreeRepresentation(ITreeContents contents) {
		super(contents);	
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
		//fStartIndexes.add( (fStartIndexes.size() == 0) ? 0 : 
		//		fStartIndexes.get(fStartIndexes.size() - 1) + lengthOfContent(fContents.get(fStartIndexes.size() - 1)));
		addStringContent(treeRepresentationContent.getContent());
		checkConsistency();
	}

	public void addContent(Object content) {
		addContent(null, content);
	}

	/*
	private void recalculateStartingIndices(int from, int change) {
		for (int i = from + 1; i < fStartIndexes.size(); i++) {
			fStartIndexes.set(i, fStartIndexes.get(i) + change);
		}
	}
	*/
	
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
	
	/*
	public Object getContent(int index) {
		int i = 0;
		for (int start: fStartIndexes) {
			if (start > index) {
				return fContents.get(i-1);
			}
			i++;
		}
		if (fContents.size() == 0) {
			return null;
		} else {
			return fContents.get(fContents.size() - 1);
		}
	}
	
	public int getStartingIndexOfContent(int index) {
		int i = 0;
		for (int start: fStartIndexes) {
			if (start > index) {
				return fStartIndexes.get(i-1);
			}
			i++;
		}
		if (fStartIndexes.size() == 0) {
			return 0;
		} else {
			return fStartIndexes.get(fStartIndexes.size() - 1);
		}
	}
	*/
	
	public int getLength() {
		return fStringContent.length();
	}
	
	/*
	private int startIndexOf(Object key) {
		return fStartIndexes.get(fContentsMap.get(key));
	}
	*/
	
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
		for (Object content: fContents) {
			if (content instanceof TreeRepresentation) {				
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
		if (referencesOldTreeNode()) {
			return "# " + getElement().toString() + "[" + getContent() + "]";
		} else {
			return "% " + getElement().toString() + "[" + getContent() + "]";
		}
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
		
		// start indices
		/*
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
		*/
		return true;
	}

	public void topDownInclusionOfOldAST(TreeRepresentation oldAST) {
		if (referencesOldTreeNode()) {
			return;
		}
		if (!oldAST.isReused() && oldAST.getElement().getSymbol().equals(getElement().getSymbol())) {
			
			List<TreeRepresentation> oldChildren = oldAST.getChildNodes();
			List<TreeRepresentation> children = getChildNodes();
			
			if (oldChildren.size() != children.size()) {
				return;
			} else {
				int i = 0;
				for (TreeRepresentation child: children) {
					if (!child.getElement().getSymbol().equals(oldChildren.get(i).getElement().getSymbol())) {
						return;
					}
					i++;
				}
			}			
						
			if (oldAST.getElement().getSyntaxProvider().tryToReuse()) {
				setReferenceToOldTreeNode(oldAST);
				int i = 0;
				List<TreeRepresentation> oldASTChildren = oldAST.getChildNodes();
				for (TreeRepresentation child: getChildNodes()) {
					child.topDownInclusionOfOldAST(oldASTChildren.get(i++));
				}
			}
		}
	}	
	
	private TreeRepresentation oldTreeNode = null;
	private boolean reused = false;
	
	private void setReused() {
		this.reused = true;
	}
	
	private boolean isReused() {
		return reused;
	}
	
	public void setReferenceToOldTreeNode(TreeRepresentation oldNode) {
		oldNode.setReused();
		this.oldTreeNode = oldNode;
	}
	
	public boolean referencesOldTreeNode() {
		return oldTreeNode != null;
	}
	
	public TreeRepresentation getReferencedOldTreeNode() {
		return oldTreeNode;
	}
	
	public void removeReferencecToOldTree() {
		this.reused = false;
		this.oldTreeNode = null;
		for (TreeRepresentation child: getChildNodes()) {
			child.removeReferencecToOldTree();
		}
	}
}
