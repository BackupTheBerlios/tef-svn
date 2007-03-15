package hub.sam.tef.treerepresentation;

import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.models.IModelElement;
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
	
	public SyntaxTreeContent(final Template template, final IModelElement fmodel) {
		super();		
		fRule = null;
		fTemplate = template;
	}
	
	public String getSymbol() {
		return fTemplate.getAdapter(ISyntaxProvider.class).getNonTerminal();
	}

	public String toString() {
		return fTemplate.getAdapter(ISyntaxProvider.class).getNonTerminal();	
	}
}
