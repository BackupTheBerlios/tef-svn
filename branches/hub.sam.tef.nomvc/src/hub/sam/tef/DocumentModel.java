package hub.sam.tef;

import java.util.Collection;

import org.eclipse.jface.text.Position;

import hub.sam.tef.models.IModelElement;
import hub.sam.tef.treerepresentation.IDisposable;
import hub.sam.tef.treerepresentation.TreeRepresentation;
import hub.sam.util.MultiMap;

public class DocumentModel implements IDisposable {
	
	private IModelElement topLevelModelElement;
	private MultiMap<IModelElement, Position> fOccurences = new MultiMap<IModelElement, Position>();
	private TreeRepresentation treeRepresentation;
	
	public DocumentModel(IModelElement topLevelModelElement, TreeRepresentation treeRepresentation) {
		super();
		this.topLevelModelElement = topLevelModelElement;
		this.treeRepresentation = treeRepresentation;
	}
	
	public void addModelElementOccurence(IModelElement element, Position occurence) {
		fOccurences.put(element, occurence);		
	}

	public TreeRepresentation getTreeRepresentation() {
		return treeRepresentation;
	}

	public IModelElement getTopLevelModelElement() {
		return topLevelModelElement;
	}

	public void dispose() {
		treeRepresentation.dispose();
	}

	public boolean isActive() {
		return true;
	}
	
	public Collection<Position> getOccurences(IModelElement forModelElement) {
		return fOccurences.get(forModelElement);
	}
}
