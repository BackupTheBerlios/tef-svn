/*
 * Textual Editing Framework (TEF)
 * Copyright (C) 2006 Markus Scheidgen
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, 
 * MA 02111-1307 USA
 */
package hub.sam.tef.reconciliation;

import fri.patterns.interpreter.parsergenerator.Semantic;
import fri.patterns.interpreter.parsergenerator.Token.Range;
import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.annotations.ErrorAnnotation;
import hub.sam.tef.annotations.ISemanticProvider;
import hub.sam.tef.annotations.SemanticsContext;
import hub.sam.tef.documents.TEFDocument;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.reconciliation.syntax.ParserInterface;
import hub.sam.tef.reconciliation.syntax.UpdateTreeSemantic;
import hub.sam.tef.reconciliation.treerepresentation.ASTElementNode;
import hub.sam.tef.reconciliation.treerepresentation.IASTProvider;
import hub.sam.tef.templates.Template;

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
				try {
					fDocument.getModelProvider().getModel().getCommandFactory().delete(newModel).execute();
				} catch (Exception ex2) {
					throw new ReconciliationFailedException("Reconciliation failed (" + ex.getMessage() + ") and recovery from failure failed(" + ex2.getMessage() + ")");
				}
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
