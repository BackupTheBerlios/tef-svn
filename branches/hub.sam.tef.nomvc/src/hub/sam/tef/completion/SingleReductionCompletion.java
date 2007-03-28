package hub.sam.tef.completion;

import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.treerepresentation.ASTElementNode;


/**
 * Is a completion computer that is based on a single syntax rule.
 */
public abstract class SingleReductionCompletion implements ICompletionComputer {
	
	/** 
	 * @return The symbols that must lay on top of the stack in order for this completion to apply.
	 */
	public abstract String[] getRulePrefix();
	
	/**
	 * @return The rule that describes the completion node of this completion.
	 */
	public abstract Rule getRule();
	
	public boolean reduceParseStack(CompletionParser parser) {
		String[] rulePrefix = getRulePrefix();
		for (int i = 0; i < rulePrefix.length; i++) {
			Object parseResult = parser.getParseResult(rulePrefix.length - i);
			if (parseResult instanceof ASTElementNode) {
				if (!((ASTElementNode)parseResult).getElement().getSymbol().equals(rulePrefix[i])) {
					return false;
				}
			} else if (parseResult instanceof String) {
				if (!("'" + parseResult + "'").equals(rulePrefix[i])) {
					return false;
				}
				// TODO complex terminal symbols ala `identifier` or `number`
			} else {
				throw new RuntimeException("assert");
			}					
		}
		parser.reduce(getRule(), rulePrefix.length);
		return true;
	}

}
