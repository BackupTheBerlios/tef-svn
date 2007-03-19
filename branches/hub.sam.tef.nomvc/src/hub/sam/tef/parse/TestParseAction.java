package hub.sam.tef.parse;

import hub.sam.tef.TEFEditor;
import hub.sam.tef.TEFModelDocument;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.Template;
import hub.sam.tef.treerepresentation.IRepresentationChangedListener;
import hub.sam.tef.treerepresentation.ITreeRepresentationProvider;
import hub.sam.tef.treerepresentation.TreeRepresentation;

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
		TEFModelDocument document = ((TEFEditor)editor).getDocument().getModelDocument();
		Template topLevelTemplate = document.getTopLevelTemplate();
		IModelElement model = document.getDocumentText().getTexts().get(0).getElement(IModelElement.class);
		TreeRepresentation representation = (TreeRepresentation)topLevelTemplate.getAdapter(ITreeRepresentationProvider.class).
				createTreeRepresentation(null, model);
		representation.print(System.out);
		final String oldContent = representation.getContent();
		System.out.println(oldContent);
		representation.addRepresentationChangedListener(new RepresentationChangedListener(oldContent, representation));			
		//new ParserInterface(document.getTopLevelTemplate()).test(document.getDocumentText());
	}

	public void selectionChanged(IAction action, ISelection selection) {
		action.setEnabled(true); // always on
	}
	
	class RepresentationChangedListener implements IRepresentationChangedListener {
		private String content;
		private final TreeRepresentation representation;
		
		public RepresentationChangedListener(String content, final TreeRepresentation representation) {
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
