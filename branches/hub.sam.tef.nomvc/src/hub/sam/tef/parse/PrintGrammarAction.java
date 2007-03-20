package hub.sam.tef.parse;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

@Deprecated
public class PrintGrammarAction implements IEditorActionDelegate {

	private IEditorPart editor = null;
	
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.editor = targetEditor;
	}

	public void run(IAction action) {
		/*
		TEFModelDocument document = ((TEFEditor)editor).getDocument().getModelDocument();
		new ParserInterface(document.getTopLevelTemplate()).printGrammar(document.getDocumentText());
		*/
	}

	public void selectionChanged(IAction action, ISelection selection) {
		action.setEnabled(true); // always on
	}

}
