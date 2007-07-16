package hub.sam.tef.tdl.templates;

import hub.sam.tef.models.IModelElement;
import hub.sam.tef.reconciliation.syntax.BlockLayout;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.ReferenceTemplate;
import hub.sam.tef.templates.SingleValueTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.TerminalTemplate;
import hub.sam.tef.templates.ValueTemplate;
import hub.sam.tef.templates.WhitespaceTemplate;

public class TDLSingleValueTemplateTemplate extends ElementTemplate {

	
	public TDLSingleValueTemplateTemplate(Template template) {
		super(template, template.getModel().getMetaElement("TDLSingleValueTemplate"));	
	}

	@Override
	public Template[] createTemplates() {
		return new Template[] {
			new WhitespaceTemplate(this, BlockLayout.INDENT),
			new TerminalTemplate(this, "single"),
			new WhitespaceTemplate(this, BlockLayout.EMPTY),
			new TerminalTemplate(this, "("),
			new WhitespaceTemplate(this, BlockLayout.EMPTY),
			new SingleValueTemplate<IModelElement>(this, "property") {
				@Override
				protected ValueTemplate<IModelElement> createValueTemplate() {
					return new ReferenceTemplate(this, getModel().getMetaElement("EStructuralFeature")) {
						@Override
						protected ElementTemplate getElementTemplate() {
							return new EMFIdentifierTemplate(this);
						}						
					};					
				}				
			},
			new WhitespaceTemplate(this, BlockLayout.EMPTY),
			new TerminalTemplate(this, ","),
			new WhitespaceTemplate(this, BlockLayout.SPACE),
			new SingleValueTemplate<IModelElement>(this, "valueTemplate") {
				@Override
				protected ValueTemplate<IModelElement> createValueTemplate() {
					return new TemplateValueTemplate(this);
				}				
			},
			new WhitespaceTemplate(this, BlockLayout.EMPTY),
			new TerminalTemplate(this, ")"),
			new WhitespaceTemplate(this, BlockLayout.STATEMENT),
		};
	}

}
