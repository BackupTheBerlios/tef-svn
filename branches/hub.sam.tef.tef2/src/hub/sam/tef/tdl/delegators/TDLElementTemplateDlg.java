package hub.sam.tef.tdl.delegators;

import java.util.ArrayList;

import hub.sam.tef.documents.IDocumentModelProvider;
import hub.sam.tef.tdl.TDLElementTemplate;
import hub.sam.tef.tdl.TemplateValue;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.Template;

public class TDLElementTemplateDlg extends ElementTemplate {

	private final TDLElementTemplate tdlElementTemplate;
	private final TemplateFactory factory;
	
	public TDLElementTemplateDlg(Template template, TDLElementTemplate tdlElementTemplate,
			TemplateFactory factory) {
		super(template, template.getModel().getMetaElement(tdlElementTemplate.getMetaElement().getName()));
		this.tdlElementTemplate = tdlElementTemplate;
		this.factory = factory;
	}

	public TDLElementTemplateDlg(IDocumentModelProvider modelProvider, TDLElementTemplate tdlElementTemplate,
			TemplateFactory factory) {
		super(modelProvider, modelProvider.getModel().getMetaElement(tdlElementTemplate.getMetaElement().getName()));
		this.tdlElementTemplate = tdlElementTemplate;
		this.factory = factory;
	}

	
	@Override
	public Template[] createTemplates() {
		ArrayList<Template> result = new ArrayList<Template>();
		for (Object o: tdlElementTemplate.getTemplates()) {
			result.add(factory.getTemplate(this, (TemplateValue)o));
		}
		return result.toArray(new Template[] {});
	}

}