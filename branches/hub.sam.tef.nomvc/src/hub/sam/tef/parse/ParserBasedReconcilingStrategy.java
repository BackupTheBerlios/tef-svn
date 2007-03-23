package hub.sam.tef.parse;

import hub.sam.tef.ErrorAnnotation;
import hub.sam.tef.TEFDocument;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.adaptors.IASTProvider;
import hub.sam.tef.templates.adaptors.IAnnotationModelProvider;
import hub.sam.tef.treerepresentation.ASTElementNode;
import hub.sam.tef.treerepresentation.SemanticsContext;

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

public class ParserBasedReconcilingStrategy implements IReconcilingStrategy {	
	private TEFDocument fDocument;	
	private ParserInterface fParserInterface;
	
	public ParserBasedReconcilingStrategy(final ISourceViewer viewer) {
		super();		
		fDocument = (TEFDocument)viewer.getDocument();
	}

	public void reconcile()  {				
		if (fDocument.needsReconciling()) {
			try {
				UpdateTreeSemantic semantic = new UpdateTreeSemantic(getParserInterface());	
				if (getParserInterface().parse(fDocument.get(), semantic)) {
					// the current content can be parsed (contains no syntax errors)																										
					final ASTElementNode newAST = semantic.getCurrentResult();
					// build a new model							
					final IModelElement newModel = (IModelElement)fDocument.getTopLevelTemplate().getAdapter(
							IASTProvider.class).createCompositeModel(null, null, newAST, true);
					
					final SemanticsContext semanticContext = new SemanticsContext(fDocument.getModelProvider(), fDocument,
							newModel);
					
					fDocument.getTopLevelTemplate().getAdapter(
							IASTProvider.class).createReferenceModel(null, null, newAST, true, semanticContext);
					
					// check the model and create error annotations				
					newAST.getElement().getTemplate().getAdapter(ISemanticProvider.class).
							check(newAST, semanticContext);								
																					
					// set the new content
					PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {				
						public void run() {			
							fDocument.setModelContent(newAST, newModel);
						}				
					});
				} else {
					fDocument.getModelProvider().getAnnotationModelProvider().
							addAnnotation(new ErrorAnnotation(), new Position(getParserInterface().getLastOffset(), 1));
					// set the new content
					PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {				
						public void run() {			
							fDocument.setModelContent(null, null);
						}				
					});
				}
			} catch (Exception ex) {
				System.out.println("RECONCILING FAILED: " + ex.getMessage());
			}
		}
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
