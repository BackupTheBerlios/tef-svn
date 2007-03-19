package hub.sam.tef.treerepresentation;

import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.parse.ISyntaxProvider;
import hub.sam.tef.templates.Template;

public class SyntaxTreeContent implements ITreeContents {

	private final Rule fRule;
	private final Template fTemplate;

	
	public SyntaxTreeContent(final Rule rule, final Template template) {
		super();
		fRule = rule;
		fTemplate = template;
	}
	
	public String getSymbol() {
		return getSyntaxProvider().getNonTerminal();
	}

	public ISyntaxProvider getSyntaxProvider() {
		return fTemplate.getAdapter(ISyntaxProvider.class);
	}	

	public String toString() {
		return fTemplate.getAdapter(ISyntaxProvider.class).getNonTerminal();	
	}

	public Template getTemplate() {
		return fTemplate;
	}
		
}
