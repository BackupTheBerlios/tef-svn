package hub.sam.tef.templates.adaptors;

import hub.sam.tef.templates.LayoutManager;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.layout.AbstractLayoutManager;

public interface ILanguageModelProvider {
	public Template getTopLevelTemplate();
	public IIdentifierResolver getIdentityResolver();
	public AbstractLayoutManager getLayout();
}
