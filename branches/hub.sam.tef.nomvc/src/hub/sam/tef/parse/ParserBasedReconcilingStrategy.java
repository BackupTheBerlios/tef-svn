package hub.sam.tef.parse;

import hub.sam.tef.TEFDocument;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.Template;
import hub.sam.tef.treerepresentation.ITreeRepresentationProvider;
import hub.sam.tef.treerepresentation.SemanticsContext;
import hub.sam.tef.treerepresentation.TreeRepresentation;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.ui.PlatformUI;

public class ParserBasedReconcilingStrategy implements IReconcilingStrategy {
	
	private TEFDocument document;
	private ParserInterface fParserInterface;
	
	/**
	 * We ignore the dirtyRegion, because TEFDocuments keeps track of changes based on TEF-editing and
	 * text-editing, whereas dirtyRegion only includes text-editing.
	 * 
	 * TODO: do not parse twice
	 */	
	public void reconcile(IRegion dirtyRegion)  { 
		if (document.needsReconciling()) {
			if (getParserInterface().parse(document.get(), new EmptySemantic())) {
				// the current content can be parsed (contains no syntax errors)												
				UpdateTreeSemantic semantic = new UpdateTreeSemantic(getParserInterface());					
				getParserInterface().parse(document.get(), semantic);				
				final TreeRepresentation newAST = semantic.getCurrentResult();
				
				
				final IModelElement newModel = (IModelElement)document.getTopLevelTemplate().getAdapter(
						ITreeRepresentationProvider.class).createModel(null, null, newAST, true);
				newAST.getElement().getTemplate().getAdapter(ISemanticProvider.class).
				checkAndResolve(newAST, new SemanticsContext(document.getAnnotationModelProvider()));							
																				
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {				
					public void run() {			
						document.setModelContent(newModel, newAST);
					}				
				});
			} else {
				document.setSyntaxError(new SyntaxError("unknown"),  getParserInterface().getLastOffset());
			}
		}
	}
	

	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		reconcile(dirtyRegion);
	}
	
	private ParserInterface getParserInterface() {
		if (fParserInterface == null) {
			fParserInterface = new ParserInterface(document.getTopLevelTemplate()); 
		}
		return fParserInterface;
	}
	
	public void setDocument(IDocument document) {
		this.document = (TEFDocument)document;		
	}

}
