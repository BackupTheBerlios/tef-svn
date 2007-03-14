package hub.sam.tef.treerepresentation;

import hub.sam.tef.models.IModelElement;
import hub.sam.tef.parse.ISyntaxProvider;
import hub.sam.tef.templates.Template;

public class ModelTreeContents implements ITreeContents {

	private final IModelElement fmodel;
	private final Template fTemplate;
	
	public ModelTreeContents(final Template template, final IModelElement fmodel) {
		super();
		this.fmodel = fmodel;
		fTemplate = template;
	}
	
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(fTemplate.getAdapter(ISyntaxProvider.class).getNonTerminal() + " ");
		return result.toString();
	}
}
