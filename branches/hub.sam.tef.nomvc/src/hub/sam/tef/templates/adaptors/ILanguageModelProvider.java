package hub.sam.tef.templates.adaptors;

import hub.sam.tef.templates.Template;

public interface ILanguageModelProvider {
	public Template getTopLevelTemplate();
	public IIdentifierResolver getIdentityResolver();
}
