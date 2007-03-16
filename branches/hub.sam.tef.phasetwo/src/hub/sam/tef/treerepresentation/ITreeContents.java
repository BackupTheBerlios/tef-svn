package hub.sam.tef.treerepresentation;

import hub.sam.tef.parse.ISyntaxProvider;
import hub.sam.tef.templates.Template;

public interface ITreeContents {
	
	public String getSymbol();

	public ISyntaxProvider getSyntaxProvider();
	
	public Template getTemplate();
}
