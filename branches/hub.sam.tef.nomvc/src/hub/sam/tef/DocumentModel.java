package hub.sam.tef;

import java.util.Collection;

import org.eclipse.jface.text.Position;

import hub.sam.tef.models.IModelElement;
import hub.sam.tef.treerepresentation.ASTElementNode;
import hub.sam.util.MultiMap;
import hub.sam.util.container.IDisposable;

public class DocumentModel implements IDisposable {
	
	private IModelElement topLevelModelElement;
	private MultiMap<IModelElement, Position> fOccurences = new MultiMap<IModelElement, Position>();
	private ASTElementNode treeRepresentation;
	
	public DocumentModel(IModelElement topLevelModelElement, ASTElementNode treeRepresentation) {
		super();
		this.topLevelModelElement = topLevelModelElement;
		this.treeRepresentation = treeRepresentation;
	}
	
	public void addModelElementOccurence(IModelElement element, Position occurence) {
		fOccurences.put(element, occurence);		
	}

	public ASTElementNode getTreeRepresentation() {
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
