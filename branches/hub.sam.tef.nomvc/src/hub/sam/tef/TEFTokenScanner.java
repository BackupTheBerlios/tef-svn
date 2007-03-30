package hub.sam.tef;

import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.adaptors.ILanguageModelProvider;
import hub.sam.tef.templates.adaptors.ISyntaxHighlightProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;

public class TEFTokenScanner extends RuleBasedScanner implements ITokenScanner {

	private final ISourceViewer fViewer;
	private boolean configured = false;
	
	public TEFTokenScanner(final ISourceViewer viewer) {
		super();
		fViewer = viewer;		
	}

	private synchronized void configure(ILanguageModelProvider language) {
		if (!configured) {
			Collection<IRule> rules = new Vector<IRule>();
			collectRules(language.getTopLevelTemplate(), rules, new HashSet<Template>());
			setRules(rules.toArray(new IRule[]{}));
			configured = true;
		}
	}
	
	
	@Override
	public void setRange(IDocument document, int offset, int length) {
		configure((TEFDocument)fViewer.getDocument());
		super.setRange(document, offset, length);
	}

	private void collectRules(Template template, Collection<IRule> rules, Collection<Template> visited) {
		visited.add(template);
		ISyntaxHighlightProvider provider = template.getAdapter(ISyntaxHighlightProvider.class);
		if (provider != null) {
			rules.addAll(provider.getHighlightRules());
		}
		for (Template nestedTemplate: template.getNestedTemplates()) {
			if (!visited.contains(nestedTemplate)) {
				collectRules(nestedTemplate, rules, visited);
			}
		}
	}
}
