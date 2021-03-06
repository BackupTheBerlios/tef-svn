package hub.sam.tef.parse;

import hub.sam.tef.TEFDocument;
import hub.sam.tef.TEFEditor;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

public class PrintGrammarAction implements IEditorActionDelegate {

	private IEditorPart editor = null;
	
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.editor = targetEditor;
	}

	public void run(IAction action) {
		new ParserInterface(((TEFDocument)((TEFEditor)editor).getDocument()).getTopLevelTemplate()).printGrammar();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		action.setEnabled(true); // always on
	}

}
