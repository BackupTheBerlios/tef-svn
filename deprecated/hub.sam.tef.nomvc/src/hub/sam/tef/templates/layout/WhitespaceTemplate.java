package hub.sam.tef.templates.layout;

import hub.sam.tef.templates.Template;

public class WhitespaceTemplate extends Template {

	private final int fRole;
	
	public WhitespaceTemplate(Template template, final int role) {
		super(template);
		this.fRole = role;
	}

	public String getSpace(AbstractLayoutManager layout) {
		return layout.getSpace(fRole);
	}
}
