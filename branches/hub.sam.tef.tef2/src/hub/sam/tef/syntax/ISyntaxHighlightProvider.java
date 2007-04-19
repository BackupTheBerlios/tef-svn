package hub.sam.tef.syntax;

import java.util.Collection;

import org.eclipse.jface.text.rules.IRule;

public interface ISyntaxHighlightProvider {
	public Collection<IRule> getHighlightRules();
}
