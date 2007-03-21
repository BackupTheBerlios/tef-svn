package hub.sam.tef.parse;

import hub.sam.tef.DocumentModel;
import hub.sam.tef.ErrorAnnotation;
import hub.sam.tef.TEFDocument;
import hub.sam.tef.controllers.IAnnotationModelProvider;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.treerepresentation.ITreeRepresentationProvider;
import hub.sam.tef.treerepresentation.SemanticsContext;
import hub.sam.tef.treerepresentation.TreeRepresentation;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModelExtension;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.PlatformUI;

public class ParserBasedReconcilingStrategy implements IReconcilingStrategy, IAnnotationModelProvider {
	
	private TEFDocument fDocument;
	private final ISourceViewer fViewer;
	private ParserInterface fParserInterface;
	
	private Annotation[] previousAnnotations = new Annotation[] {};
	private Map<Annotation, Position> annotations = new HashMap<Annotation, Position>();
	
	public ParserBasedReconcilingStrategy(final ISourceViewer viewer) {
		super();
		fViewer = viewer;
		fDocument = (TEFDocument)viewer.getDocument();
	}

	public void reconcile()  {				
		if (fDocument.needsReconciling()) {
			UpdateTreeSemantic semantic = new UpdateTreeSemantic(getParserInterface());	
			if (getParserInterface().parse(fDocument.get(), semantic)) {
				// the current content can be parsed (contains no syntax errors)																										
				final TreeRepresentation newAST = semantic.getCurrentResult();
				// build a new model							
				final IModelElement newModel = (IModelElement)fDocument.getTopLevelTemplate().getAdapter(
						ITreeRepresentationProvider.class).createCompositeModel(null, null, newAST, true);
				
				final DocumentModel newDocumentModel = new DocumentModel(newModel, newAST);
				final SemanticsContext semanticContext = new SemanticsContext(this, newDocumentModel, fDocument.getModel());
				
				fDocument.getTopLevelTemplate().getAdapter(
						ITreeRepresentationProvider.class).createReferenceModel(null, null, newAST, true, semanticContext);
				
				// check the model and create error annotations				
				newAST.getElement().getTemplate().getAdapter(ISemanticProvider.class).
						check(newAST, semanticContext);								
																				
				// set the new content
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {				
					public void run() {			
						fDocument.setModelContent(newDocumentModel);
					}				
				});
			} else {
				addAnnotation(new ErrorAnnotation(), new Position(getParserInterface().getLastOffset(), 1));
			}
			
			((IAnnotationModelExtension)fViewer.getAnnotationModel()).replaceAnnotations(
					previousAnnotations, annotations);
			previousAnnotations = annotations.keySet().toArray(new Annotation[] {});			
			annotations.clear();
		}
	}	

	public void addAnnotation(Annotation annotation, Position position) {
		annotations.put(annotation, position);
	}

	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		reconcile(dirtyRegion);
	}	
	
	public void reconcile(IRegion partition) {
		reconcile();
	}


	private ParserInterface getParserInterface() {
		if (fParserInterface == null) {
			fParserInterface = new ParserInterface(fDocument.getTopLevelTemplate()); 
		}
		return fParserInterface;
	}
	
	public void setDocument(IDocument document) {
		this.fDocument = (TEFDocument)document;		
	}

}
