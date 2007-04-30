package hub.sam.tef;

import hub.sam.tef.annotations.IIdentifierResolver;
import hub.sam.tef.completion.ICompletionComputer;
import hub.sam.tef.reconciliation.syntax.AbstractLayoutManager;
import hub.sam.tef.templates.Template;

import java.util.Collection;

public interface ILanguageModelProvider {
	public Template getTopLevelTemplate();
	public IIdentifierResolver getIdentityResolver();
	public AbstractLayoutManager getLayout();
	public Collection<ICompletionComputer> getCompletions();
}
