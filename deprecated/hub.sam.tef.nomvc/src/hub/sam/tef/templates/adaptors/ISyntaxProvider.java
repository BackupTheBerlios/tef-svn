package hub.sam.tef.templates.adaptors;

import fri.patterns.interpreter.parsergenerator.syntax.Rule;

public interface ISyntaxProvider {
	
	/**
	 * Provides a unique name used as symbol during parsing.
	 */
	public String getNonTerminal();
	
	/**
	 * Provides parser rules.
	 */
	public String[][] getRules();
	
}
