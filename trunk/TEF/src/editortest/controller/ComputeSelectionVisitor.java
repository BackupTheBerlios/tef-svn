package editortest.controller;

import hub.sam.tef.views.CompoundText;
import hub.sam.tef.views.Text;
import editortest.template.CollectionTemplate;

public final class ComputeSelectionVisitor extends AbstractOffsetBasedVisitor {
	
	private Text result = null;
	private Text cursorPostionResult = null;
		
	public ComputeSelectionVisitor(final int forOffset) {
		super(forOffset);				
	}

	public void visitText(Text visitedText, int atOffset) {
		if (result == null && visitedText.getElement(CollectionTemplate.MarkFlag.class) != null) {
			result = visitedText;
		}
		if (!(visitedText instanceof CompoundText)) {
			cursorPostionResult = visitedText;
		}
	}

	public void visitCompoundText(CompoundText visitedText, int atOffset) {
		if (result == null) {
			visitText(visitedText, atOffset);
		}
	}	
	
	public Text getResult() {
		return result;
	}
	
	public Text getCursorPositionText() {
		return cursorPostionResult;
	}
}
