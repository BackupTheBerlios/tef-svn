package hub.sam.tef.treerepresentation;

import hub.sam.tef.templates.Template;

public interface ITreeContents extends IDisposable {
	
	public String getSymbol();

	public ISyntaxProvider getSyntaxProvider();
	
	public Template getTemplate();
	
	public void setTreeRepresentation(TreeRepresentation treeNode);
}
