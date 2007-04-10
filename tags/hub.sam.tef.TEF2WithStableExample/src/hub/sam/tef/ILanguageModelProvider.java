package hub.sam.tef;

import hub.sam.tef.annotations.IIdentifierResolver;
import hub.sam.tef.syntax.AbstractLayoutManager;
import hub.sam.tef.templates.LayoutManager;
import hub.sam.tef.templates.Template;

public interface ILanguageModelProvider {
	public Template getTopLevelTemplate();
	public IIdentifierResolver getIdentityResolver();
	public AbstractLayoutManager getLayout();
}
