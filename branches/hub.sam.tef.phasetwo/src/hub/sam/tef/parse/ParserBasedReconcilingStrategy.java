package hub.sam.tef.parse;

import hub.sam.tef.TEFDocument;
import hub.sam.tef.TEFModelDocument;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.Template;
import hub.sam.tef.treerepresentation.ITreeRepresentationProvider;
import hub.sam.tef.treerepresentation.TreeRepresentation;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

public class ParserBasedReconcilingStrategy implements IReconcilingStrategy {
	
	private TEFDocument document;	
	private ParserInterface fParserInterface;
	
	/**
	 * We ignore the dirtyRegion, because TEFDocuments keeps track of changes based on TEF-editing and
	 * text-editing, whereas dirtyRegion only includes text-editing.
	 */	
	public void reconcile(IRegion dirtyRegion)  {
		if (!document.isInTEFMode()) {
			if (getParserInterface().parse(document.getContent(), new EmptySemantic())) {
				// the current content can be parsed (contains no syntax errors)
				TEFModelDocument modelDocument = document.getModelDocument();
				Template topLevelTemplate = modelDocument.getTopLevelTemplate();
				IModelElement model = modelDocument.getDocumentText().getTexts().get(0).getElement(IModelElement.class);
				TreeRepresentation representation = (TreeRepresentation)topLevelTemplate.getAdapter(ITreeRepresentationProvider.class).
						createTreeRepresentation(null, model);				
								
				document.getModelDocument().getDocumentText().dispose();
				
				loop: while(true) {
					document.startReconciliation();
					UpdateTreeSemantic semantic = new UpdateTreeSemantic(representation, document.getChanges(), getParserInterface());					
					getParserInterface().parse(document.getContent(), semantic);				
					TreeRepresentation newAST = semantic.getCurrentResult();
					newAST.topDownInclusionOfOldAST(representation);
					newAST.print(System.out);
					
					return;
					/*
					document.getModelDocument().getTopLevelTemplate().getAdapter(IASTBasedModelUpdater.class).
									executeModelUpdate(new ModelUpdateConfiguration(newAST, null, null, false));
					if (document.stopReconciliation()) {
						PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {					
							public void run() {			
								document.postReconcilitation();
							}				
						});
						return;
					}
					*/
				}
								
			} else {
				System.out.println(document.getContent());
			}
		} else {
			// only normal changes, ignore them
			System.out.println("changes in TEF mode, ignored by reconciler");
		}
	}
	/*
	public void reconcile(IRegion dirtyRegion)  {
		if (!document.isInTEFMode()) {
			if (getParserInterface().parse(document.getContent(), new EmptySemantic())) {
				// the current content can be parsed (contains no syntax errors)
				TextBasedAST oldAST = document.getModelDocument().getTopLevelTemplate().getAdapter(ISyntaxProvider.class).createAST(null, null, 
						((CompoundText)document.getModelDocument().getDocumentText()).getTexts().get(0));
				oldAST.print(System.out);
				//TextBasedAST oldAST = TextBasedAST.createASTTree(document.getModelDocument().getDocumentText());
				document.getModelDocument().getDocumentText().dispose();
				
				loop: while(true) {
					document.startReconciliation();
					UpdatedASTTreeSemantic semantic = new UpdatedASTTreeSemantic(oldAST, document.getChanges(), getParserInterface());
					getParserInterface().parse(document.getContent(), semantic);				
					TextBasedUpdatedAST newAST = semantic.getCurrentResult();
					newAST.topDownInclusionOfOldAST(oldAST);
					newAST.print(System.out);
									
					document.getModelDocument().getTopLevelTemplate().getAdapter(IASTBasedModelUpdater.class).
									executeModelUpdate(new ModelUpdateConfiguration(newAST, null, null, false));
					if (document.stopReconciliation()) {
						PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {					
							public void run() {			
								document.postReconcilitation();
							}				
						});
						return;
					}
				}
								
			} else {
				System.out.println(document.getContent());
			}
		} else {
			// only normal changes, ignore them
			System.out.println("changes in TEF mode, ignored by reconciler");
		}
	}
	*/

	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		reconcile(dirtyRegion);
	}
	
	private ParserInterface getParserInterface() {
		if (fParserInterface == null) {
			fParserInterface = new ParserInterface(((TEFDocument)document).getModelDocument().getTopLevelTemplate()); 
		}
		return fParserInterface;
	}
	
	public void setDocument(IDocument document) {
		this.document = (TEFDocument)document;		
	}

}
