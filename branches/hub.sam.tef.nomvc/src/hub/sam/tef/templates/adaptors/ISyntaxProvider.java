package hub.sam.tef.templates.adaptors;

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
