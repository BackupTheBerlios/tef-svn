package hub.sam.tef.treerepresentation;

import hub.sam.tef.models.IModelElement;
import hub.sam.tef.parse.ISyntaxProvider;
import hub.sam.tef.templates.Template;

@Deprecated
public class ModelTreeContents implements ITreeContents {

	private final IModelElement fModel;
	private final Template fTemplate;
	
	public ModelTreeContents(final Template template, final IModelElement fmodel) {
		super();
		this.fModel = fmodel;
		fTemplate = template;
	}
	
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(fTemplate.getAdapter(ISyntaxProvider.class).getNonTerminal() + " ");
		return result.toString();
	}
	
	public String getSymbol() {
		return fTemplate.getAdapter(ISyntaxProvider.class).getNonTerminal();
	}

}
