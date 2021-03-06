package editortest.emf.ocl.templates;

import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.ElementTemplateSemantics;
import hub.sam.tef.templates.ReferenceTemplate;
import hub.sam.tef.templates.SequenceTemplate;
import hub.sam.tef.templates.SingleValueTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.TerminalTemplate;
import hub.sam.tef.templates.ValueTemplate;
import hub.sam.tef.templates.adaptors.ISyntaxProvider;
import editortest.emf.ecore.templates.EIdentifierTemplate;

public class OperationCallExp1Template extends ElementTemplate {

	public OperationCallExp1Template(Template template) {
		super(template, template.getModel().getMetaElement("OperationCallExp"));	
	}

	@Override
	public Template[] createTemplates() {
		return new Template[] {
				new SingleValueTemplate<IModelElement>(this, "source") {
					@Override
					protected ValueTemplate<IModelElement> createValueTemplate() {
						return new OclExpressionTemplate(this);
					}				
				},			
				new TerminalTemplate(this, "->"),
				new SingleValueTemplate<IModelElement>(this, "referredOperation") {
					@Override
					protected ValueTemplate<IModelElement> createValueTemplate() {						
						return new ReferenceTemplate(this, getModel().getMetaElement("EOperation")) {
							@Override
							protected ElementTemplate getElementTemplate() {
								return new EIdentifierTemplate(this);
							}						
						};						
					}					
				},
				new TerminalTemplate(this, "("),
				new SequenceTemplate<IModelElement>(this, "argument", ",", false) {				
					@Override
					protected ValueTemplate<IModelElement> createValueTemplate() {
						return ExpTemplate.getExpTemplate(this);
					}				
				},
				new TerminalTemplate(this, ")")
		};
	}	
	
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (ISyntaxProvider.class == adapter) {
			return (T)new MySyntaxProvider(this);
		} else {
			return super.getAdapter(adapter);
		}
	}
	
	class MySyntaxProvider extends ElementTemplateSemantics {
		public MySyntaxProvider(ElementTemplate elementTemplate) {
			super(elementTemplate);		
		}
		
		@Override
		public String getNonTerminal() {	
			return super.getNonTerminal() + "1";
		}				
	}
}
