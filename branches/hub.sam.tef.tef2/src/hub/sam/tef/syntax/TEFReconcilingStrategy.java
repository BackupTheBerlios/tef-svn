package hub.sam.tef.syntax;

import fri.patterns.interpreter.parsergenerator.Semantic;
import fri.patterns.interpreter.parsergenerator.Token.Range;
import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.ErrorAnnotation;
import hub.sam.tef.TEFDocument;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.adaptors.IASTProvider;
import hub.sam.tef.templates.adaptors.IAnnotationModelProvider;
import hub.sam.tef.treerepresentation.ASTElementNode;
import hub.sam.tef.treerepresentation.SemanticsContext;

import java.util.HashMap;
import java.util.List;
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

public class TEFReconcilingStrategy implements IReconcilingStrategy {	
	private TEFDocument fDocument;	
	private ParserInterface fParserInterface;
	
	public TEFReconcilingStrategy(final ISourceViewer viewer) {
		super();		
		fDocument = (TEFDocument)viewer.getDocument();
	}

	public void reconcile()  {				
		if (fDocument.needsReconciling()) {
			System.out.println("reconciling: parsing");
			getParserInterface().parse(fDocument.get(), new Semantic() {

				public Object doSemantic(Rule rule, List parseResults, List<Range> resultRanges) {				
					return null;
				}

				public Object doSemanticForErrorRecovery(String recoverSymbol) {
					return null;
				}
				
			});			
			try {
				String content = fDocument.get();
				UpdateTreeSemantic semantic = new UpdateTreeSemantic(getParserInterface(), content);
				System.out.println("reconciling: parsing 2 ");
				if (getParserInterface().parse(content, semantic)) {
					System.out.println("reconciling: parsing 3 ");
					fDocument.getModelProvider().resetModelElementOccurences();
					// the current content can be parsed (contains no syntax errors)																										
					final ASTElementNode newAST = semantic.getCurrentResult();
					// build a new model
					System.out.println("reconciling: composite");
					final IModelElement newModel = (IModelElement)fDocument.getTopLevelTemplate().getAdapter(
							IASTProvider.class).createCompositeModel(null, null, newAST, true);
					
					final SemanticsContext semanticContext = new SemanticsContext(fDocument.getModelProvider(), fDocument,
							newModel);
					
					System.out.println("reconciling: reference");
					fDocument.getTopLevelTemplate().getAdapter(
							IASTProvider.class).createReferenceModel(null, null, newAST, true, semanticContext);
					
					System.out.println("reconciling: check");
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
