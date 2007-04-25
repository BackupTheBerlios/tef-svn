package hub.sam.tef.reconciliation;

import fri.patterns.interpreter.parsergenerator.Semantic;
import fri.patterns.interpreter.parsergenerator.Token.Range;
import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.ErrorAnnotation;
import hub.sam.tef.TEFDocument;
import hub.sam.tef.annotations.ISemanticProvider;
import hub.sam.tef.annotations.SemanticsContext;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.syntax.ParserInterface;
import hub.sam.tef.syntax.UpdateTreeSemantic;
import hub.sam.tef.templates.Template;
import hub.sam.tef.treerepresentation.ASTElementNode;
import hub.sam.tef.treerepresentation.IASTProvider;

import java.util.List;

import org.eclipse.jdt.internal.ui.text.correction.NewMethodCompletionProposal;
import org.eclipse.jface.text.Position;

public class ReconciliationUnit {
	
	private ParserInterface fParserInterface;

	public ReconciliationResults run(TEFDocument fDocument) throws ReconciliationFailedException {
		ParserInterface parserInterface = getParserInterface(fDocument.getTopLevelTemplate());
		parserInterface.parse(fDocument.get(), new Semantic() {
			public Object doSemantic(Rule rule, List parseResults, List<Range> resultRanges) {				
				return null;
			}

			public Object doSemanticForErrorRecovery(String recoverSymbol) {
				return null;
			}
			
		});	
		IModelElement newModel = null;
		try {
			String content = fDocument.get();
			UpdateTreeSemantic semantic = new UpdateTreeSemantic(parserInterface, content);
			System.out.println("reconciling: parsing 2 ");
			if (parserInterface.parse(content, semantic)) {
				System.out.println("reconciling: parsing 3 ");
				fDocument.getModelProvider().resetModelElementOccurences();
				// the current content can be parsed (contains no syntax errors)																										
				final ASTElementNode newAST = semantic.getCurrentResult();
				// build a new model
				System.out.println("reconciling: composite");
				newModel = (IModelElement)fDocument.getTopLevelTemplate().getAdapter(
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
																				
				return new ReconciliationResults(newModel, newAST);
			} else {
				fDocument.getModelProvider().getAnnotationModelProvider().
						addAnnotation(new ErrorAnnotation("Could not parse the document."), 
								new Position(0, getParserInterface(fDocument.getTopLevelTemplate()).getLastOffset()));
				return new ReconciliationResults(null, null);
			}
		} catch (Exception ex) {			
			if (newModel != null) {
				fDocument.getModelProvider().getModel().getCommandFactory().delete(newModel).execute();
			}
			throw new ReconciliationFailedException(ex.getMessage());
		}		
	}
	
	private ParserInterface getParserInterface(Template topLevelTemplate) {
		if (fParserInterface == null) {
			fParserInterface = new ParserInterface(topLevelTemplate); 
		}
		return fParserInterface;
	}
}
