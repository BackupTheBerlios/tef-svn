/*
 * Textual Editing Framework (TEF)
 * Copyright (C) 2006-2008 Markus Scheidgen
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

package hub.sam.tef.editor;

import hub.sam.tef.TEFPlugin;
import hub.sam.tef.editor.text.TextEditor;
import hub.sam.tef.modelcreating.IModelCreatingContext;
import hub.sam.tef.modelcreating.ModelChecker;
import hub.sam.tef.modelcreating.ModelCreatingException;
import hub.sam.tef.modelcreating.ParseTreeNode;
import hub.sam.tef.modelcreating.Parser;
import hub.sam.tef.modelcreating.ParserError;
import hub.sam.tef.modelcreating.ParserSemantics;
import hub.sam.tef.modelcreating.ResolutionState;
import hub.sam.tef.semantics.AbstractError;
import hub.sam.tef.semantics.Error;
import hub.sam.tef.semantics.ModelCheckError;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.PlatformUI;

/**
 * This reconciling strategy is used for TEF text editors. It parsed the document text, creates a
 * model from it, resolve references, does model checking, and updates the showed annotations to
 * report errors to the user. The reconciler is configured with the editors meta-model, syntax, and
 * semantics.
 */
public class ReconcilingStrategy implements IReconcilingStrategy {

	private final Parser fParser;
	private final TextEditor fEditor;

	private IDocument document = null;
	private final ISourceViewer fSourceViewer;

	/**
	 * @param packages
	 *            is the meta-model.
	 * @param syntax
	 *            is the used syntax.
	 * @param semanticsProvider
	 *            a provider for the used semantics.
	 * @param sourceViewer
	 *            the sourceViewer that all reconciling results are reported to as annotations.
	 */
	public ReconcilingStrategy(TextEditor editor, ISourceViewer sourceViewer) {
		super();
		fEditor = editor;
		fSourceViewer = sourceViewer;
		fParser = new Parser(editor.getSyntax());
	}

	/**
	 * Performs the actual reconciling.
	 */
	private void reconcileWithExceptions() throws ModelCreatingException {
		String text = document.get();
		IModelCreatingContext context = fEditor.createModelCreatingContext();

		ParserSemantics parserSemantics = new ParserSemantics(fEditor.getSyntax());
		parserSemantics.DEBUG = true;
		boolean parseOk = fParser.parse(text, parserSemantics);

		if (!parseOk) {
			int lastOffset = fParser.getLastOffset();
			if (lastOffset == -1) {
				context.addError(new Error(new Position(0, text.length()),
						"unexpected parser error"));
			} else {
				context.addError(new ParserError(lastOffset));
			}
		} else {
			ParseTreeNode parseResult = parserSemantics.getResult();
			//parseResult.looseParents();
			EObject creationResult = null;

			creationResult = (EObject) parseResult.createModel(context, null);
			context.addCreatedObject(creationResult);
			
			parseResult.postCreate(context);

			ResolutionState state = new ResolutionState(creationResult);
			parseResult.resolveModel(context, state);
			context.executeResolutions();
			
			parseResult.postResolve(context);

			new ModelChecker().checkModel(creationResult, context);
			parseResult.looseParents();
		}

		// update annotations
		IAnnotationModel annotationModel = fSourceViewer.getAnnotationModel();
		Collection<Annotation> annotations = fEditor.getAnnotations();
		for (Annotation annotation : annotations) {
			annotationModel.removeAnnotation(annotation);
		}
		annotations.clear();
		for (AbstractError error : context.getErrors()) {
			Position position = error.getPosition(context);
			if (position == null) {
				MessageDialog.openWarning(fEditor.getSite().getShell(), "Warning",
						"There is an error in your document, but its position in the"
								+ " document could not be determined: " + error.getMessage());
			} else {
				Annotation annotation;
				if (error instanceof ModelCheckError
						&& ((ModelCheckError) error).getSeverity() == IStatus.WARNING) {
					annotation = new WarningAnnotation(error.getMessage());
				} else {
					annotation = new ErrorAnnotation(error.getMessage());
				}
				int offset = position.getOffset();
				int length = position.getLength();
				annotationModel.addAnnotation(annotation, new Position(offset, length));
				annotations.add(annotation);
			}
		}

		fEditor.updateCurrentModel(context);
	}

	private void reconcile() {
		try {
			reconcileWithExceptions();
		} catch (Throwable ex) {
			TEFPlugin.getDefault().getLog().log(
					new Status(Status.WARNING, TEFPlugin.PLUGIN_ID, Status.OK,
							"Reconciliation failed (" + ex.getMessage() + ")", ex));
			ex.printStackTrace(); // TODO debug out

			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					MessageDialog.openWarning(fEditor.getSite().getShell(), "Warning",
							"Reconciliation failed due to an unexpected exception.");
				}
			});
		} finally {
			fEditor.setReconciling(false);
		}
	}

	/**
	 * Delegator.
	 */
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		reconcile();
	}

	/**
	 * Delegator.
	 */
	public void reconcile(IRegion partition) {
		reconcile();
	}

	/**
	 * Sets the currently reconciled document and initially reconciles it.
	 */
	public void setDocument(IDocument document) {
		this.document = document;
		reconcile();
	}
}
