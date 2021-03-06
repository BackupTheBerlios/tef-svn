package hub.sam.tef.parse;

import hub.sam.tef.templates.Template;

import java.util.List;

import fri.patterns.interpreter.parsergenerator.Semantic;
import fri.patterns.interpreter.parsergenerator.Token.Range;
import fri.patterns.interpreter.parsergenerator.syntax.Rule;

/**
 * RCC semantics that comares the current parse with an existing AST.
 */
@Deprecated
public class ParseAlongTreeSemantic implements Semantic {

	public ParseAlongTreeSemantic(final TextBasedAST tree) {
		super();
		nextOnTextTree = tree.getFirstLeaf().getParent(); // the first non-terminal
	}
	
	private TextBasedAST nextOnTextTree = null;
	private boolean ok = true;

	public Object doSemantic(Rule rule, List parseResults, List<Range> resultRanges) {
		if (!ok) {
			return null;
		}
		if (nextOnTextTree == null) {
			if (rule.getNonterminal().equals("<START>")) {
				return null;
			} else {
				ok = false;
				return null;
			}
		}
		TextBasedAST siblings = nextOnTextTree.getFirstChild();
		for(Object parseResult: parseResults) {
			if (siblings == null) {
				ok = false;
				return null;
			}
			System.out.println("parseResult: " + parseResult);
			if (parseResult instanceof String) {				
				if (Rule.isTerminal(siblings.getSymbol())) {
					if (parseResult.equals(siblings.getElement().getContent())) {
						
					} else {
						ok = false;
						return null;
					}
				} else {
					ok = false;
					return null;
				}
			} else if (parseResult instanceof Template) {
				if (!Rule.isTerminal(siblings.getSymbol())) {
					if (parseResult.equals(siblings.getTemplate())) {
						
					} else {
						ok = false;
						return null;
					}
				} else {					
					ok = false;
					return null;
				}
			} else {
				throw new RuntimeException();
			}
			siblings = siblings.getNextSibling();
		}
		if (siblings != null) {
			ok = false;
			ok = false;
			return null;
		} 
		
		if (!Rule.isTerminal(nextOnTextTree.getSymbol())) {
			if (rule.getNonterminal().equals(nextOnTextTree.getSymbol())) {
				
			} else {				
				ok = false;
				return null;
			}
		} else {
			ok = false;
			return null;
		}
		
		Template result = nextOnTextTree.getTemplate();		
		TextBasedAST nextSibling = getNextNonTerminalSibling(nextOnTextTree);
		
		if (nextSibling == null) {
			nextOnTextTree = nextOnTextTree.getParent();
		} else {
			nextOnTextTree = nextSibling;
		}
						
		return result;
	}
	
	public boolean isOk() {
		return ok && nextOnTextTree == null;
	}

	private TextBasedAST getNextNonTerminalSibling(TextBasedAST tree) {
		TextBasedAST nextSibling = tree.getNextSibling();
		if (nextSibling == null) {
			return null;
		} else  if (nextSibling.isLeaf()) {
			return getNextNonTerminalSibling(nextSibling);
		} else {
			return nextSibling.getFirstLeaf().getParent();
		}
	}

	public Object doSemanticForErrorRecovery(String recoverSymbol) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
