package hub.sam.tef.treerepresentation;

import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.adaptors.ISyntaxProvider;
import hub.sam.util.container.IDisposable;

public interface IASTElement extends IDisposable {
	
	public String getSymbol();

	public ISyntaxProvider getSyntaxProvider();
	
	public Template getTemplate();
	
	public void setAST(ASTElementNode treeNode);
}
