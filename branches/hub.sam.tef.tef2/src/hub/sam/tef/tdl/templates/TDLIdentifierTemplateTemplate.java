package hub.sam.tef.tdl.templates;

import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.TerminalTemplate;

public class TDLIdentifierTemplateTemplate extends ElementTemplate {

	public TDLIdentifierTemplateTemplate(Template template) {
		super(template, template.getModel().getMetaElement("TDLIdentifierTemplate"));
	}

	@Override
	public Template[] createTemplates() {
		return new Template[] {
				new TerminalTemplate(this, "identifier")
		};
	}

}
