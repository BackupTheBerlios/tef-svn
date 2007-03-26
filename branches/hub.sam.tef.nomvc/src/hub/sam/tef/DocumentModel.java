package hub.sam.tef;

import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.adaptors.IASTProvider;
import hub.sam.tef.templates.adaptors.IAnnotationModelProvider;
import hub.sam.tef.templates.adaptors.IDocumentModelProvider;
import hub.sam.tef.templates.adaptors.ILanguageModelProvider;
import hub.sam.tef.treerepresentation.ASTElementNode;
import hub.sam.util.MultiMap;
import hub.sam.util.container.IDisposable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModelExtension;

public class DocumentModel implements IDisposable, IDocumentModelProvider, IAnnotationModelProvider {
	
	private final IModel fModel;
	private final Object fResource;
	private IAnnotationModelExtension fAnnotationModel = null;
	private final IDocument fDocument;
	
	private final ILanguageModelProvider fLanguageModel;
	
	private IModelElement topLevelModelElement;
	private ASTElementNode treeRepresentation;
	private MultiMap<IModelElement, Position> fOccurences = new MultiMap<IModelElement, Position>();	
	private String text = null;
	private Annotation[] previousAnnotations = new Annotation[] {};
	private Map<Annotation, Position> annotations = new HashMap<Annotation, Position>();
	
	public DocumentModel(final IModel model, final Object resource, 
			final IAnnotationModelExtension annotationModel, final IDocument document, 
			final ILanguageModelProvider languageModel) {
		super();
		fModel = model;
		fResource = resource;
		fAnnotationModel = annotationModel;
		fDocument = document;
		fLanguageModel = languageModel;
	}

	public void initialize() {
		topLevelModelElement = (IModelElement)fModel.getOutermostCompositesOfEditedResource().iterator().next();
		treeRepresentation = (ASTElementNode)fLanguageModel.getTopLevelTemplate().getAdapter(IASTProvider.class).
				createTreeRepresentation(null, null, topLevelModelElement, true, fLanguageModel.getLayout());
		text = treeRepresentation.getContent();
	}
	
	public void update(ASTElementNode newTree, IModelElement newModel) {
		if (newTree != null) {
			treeRepresentation.dispose();
			fModel.replaceOutermostComposite(fResource, topLevelModelElement, newModel);
			topLevelModelElement = newModel;
			treeRepresentation = newTree;
		}
		
		fAnnotationModel.replaceAnnotations(previousAnnotations, annotations);
		previousAnnotations = annotations.keySet().toArray(new Annotation[] {});			
		annotations.clear();
	}

	public void addModelElementOccurence(IModelElement element, Position occurence) {
		fOccurences.put(element, occurence);		
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
	}
}
