package hub.sam.tef;

import hub.sam.tef.annotations.IAnnotationModelProvider;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.reconciliation.ReconciliationFailedException;
import hub.sam.tef.reconciliation.ReconciliationResults;
import hub.sam.tef.reconciliation.ReconciliationUnit;
import hub.sam.tef.treerepresentation.ASTElementNode;
import hub.sam.tef.treerepresentation.IASTProvider;
import hub.sam.util.container.IDisposable;
import hub.sam.util.container.MultiMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModelExtension;

/**
 * The document model combines all model information for the edited model. This includes not only the model
 * itself, but also its textual representation, an annotation model, its tree representation, occurrences, and more. 
 */
public class DocumentModel implements IDisposable, IDocumentModelProvider, IAnnotationModelProvider {
	
	private final IModel fModel;
	private final Object fResource;
	private IAnnotationModelExtension fAnnotationModel = null;
	private final TEFDocument fDocument;
	
	private final ILanguageModelProvider fLanguageModel;
	
	private IModelElement topLevelModelElement;
	private ASTElementNode treeRepresentation;
	private MultiMap<IModelElement, Position> fOccurences = new MultiMap<IModelElement, Position>();	
	private String text = null;
	private Annotation[] previousAnnotations = new Annotation[] {};
	private Map<Annotation, Position> annotations = new HashMap<Annotation, Position>();
	
	public DocumentModel(final IModel model, final Object resource, 
			final IAnnotationModelExtension annotationModel, final TEFDocument document, 
			final ILanguageModelProvider languageModel) {
		super();
		fModel = model;
		fResource = resource;
		fAnnotationModel = annotationModel;
		fDocument = document;
		fLanguageModel = languageModel;
	}	
	
	public void initializeFromModel() {
		topLevelModelElement = (IModelElement)fModel.getOutermostCompositesOfEditedResource().iterator().next();
		treeRepresentation = (ASTElementNode)fLanguageModel.getTopLevelTemplate().getAdapter(IASTProvider.class).
				createTreeRepresentation(null, null, topLevelModelElement, true, fLanguageModel.getLayout());
		initializeFromText(treeRepresentation.getContent());
	}	
	
	public void initializeFromText(String text) {
		this.text = text;	
	}
	
	public void reconcile() {
		try {
			ReconciliationResults result = new ReconciliationUnit().run(fDocument);
			update(result.getTopLevelTreeNode(), result.getTopLevelElement());
		} catch (ReconciliationFailedException ex) {
			System.out.println("initial reconciliation failed");
		}	
	}
	
	public void update(ASTElementNode newTree, IModelElement newModel) {
		if (newTree != null) {
			treeRepresentation.dispose();
			fModel.replaceOutermostComposite(fResource, topLevelModelElement, newModel);
			topLevelModelElement = newModel;
			treeRepresentation = newTree;
		}
		updateAnnotations();
	}

	public void addModelElementOccurence(IModelElement element, Position occurence) {
		fOccurences.put(element, occurence);		
	}	

	public void resetModelElementOccurences() {
		fOccurences.clear();
	}

	public ASTElementNode getTreeRepresentation() {
		return treeRepresentation;
	}

	public IModelElement getTopLevelElement() {
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

	public IAnnotationModelProvider getAnnotationModelProvider() {
		return this;
	}

	public IModel getModel() {
		return fModel;
	}

	public void addAnnotation(Annotation annotation, Position position) {
		annotations.put(annotation, position);
	}

	public String getText() {
		return text;
	}		
	
	public void setAnnotationModel(IAnnotationModelExtension annotationModel) {
		this.fAnnotationModel = annotationModel;
		updateAnnotations();
	}
	
	private void updateAnnotations() {
		if (fAnnotationModel != null) {
			fAnnotationModel.replaceAnnotations(previousAnnotations, annotations);
			previousAnnotations = annotations.keySet().toArray(new Annotation[] {});			
			annotations.clear();
		}
	}
}