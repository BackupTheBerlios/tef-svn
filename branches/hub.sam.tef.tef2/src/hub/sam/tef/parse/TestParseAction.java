package hub.sam.tef.parse;

import hub.sam.tef.treerepresentation.IASTChangedListener;
import hub.sam.tef.treerepresentation.ASTElementNode;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

public class TestParseAction implements IEditorActionDelegate {

	private IEditorPart editor = null;
	
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.editor = targetEditor;
	}

	public void run(IAction action) {
		// emtpy
	}

	public void selectionChanged(IAction action, ISelection selection) {
		action.setEnabled(true); // always on
	}
	
	class RepresentationChangedListener implements IASTChangedListener {
		private String content;
		private final ASTElementNode representation;
		
		public RepresentationChangedListener(String content, final ASTElementNode representation) {
			super();
			this.content = content;
			this.representation = representation;
		}

		public void contentChanged(int start, int length, String text) {
			System.out.println("REPLACE:" + content.substring(start, start+length) + ":" + text);
			content = representation.getContent();
		}
	}

}
