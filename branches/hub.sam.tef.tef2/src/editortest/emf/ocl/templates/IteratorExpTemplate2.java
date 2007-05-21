package editortest.emf.ocl.templates;

import editortest.emf.ocl.templates.IteratorExpTemplate1.MySyntaxProvider;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.reconciliation.syntax.ExpressionLayout;
import hub.sam.tef.reconciliation.syntax.ISyntaxProvider;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.ElementTemplateSemantics;
import hub.sam.tef.templates.EnumerationTemplate;
import hub.sam.tef.templates.SequenceTemplate;
import hub.sam.tef.templates.SingleValueTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.TerminalTemplate;
import hub.sam.tef.templates.ValueTemplate;
import hub.sam.tef.templates.WhitespaceTemplate;

public class IteratorExpTemplate2 extends ElementTemplate {

	public IteratorExpTemplate2(Template template) {
		super(template, template.getModel().getMetaElement("IteratorExp"));	
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
			new WhitespaceTemplate(this, ExpressionLayout.EMTPY),
			new TerminalTemplate(this, "->"),
			new WhitespaceTemplate(this, ExpressionLayout.EMTPY),
			new SingleValueTemplate<String>(this, "name") {
				@Override
				protected ValueTemplate<String> createValueTemplate() {
					return new EnumerationTemplate(this, "oclLoops", new String[] { "forAll", "exists", "select", "collect", "any" });
				}				
			},			
			new WhitespaceTemplate(this, ExpressionLayout.EMTPY),
			new TerminalTemplate(this, "("),						
			new WhitespaceTemplate(this, ExpressionLayout.EMTPY),
			new SingleValueTemplate<IModelElement>(this, "body") {
				@Override
				protected ValueTemplate<IModelElement> createValueTemplate() {
					return ExpTemplate.getExpTemplate(this);
				}				
			},
			new WhitespaceTemplate(this, ExpressionLayout.EMTPY),
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
			return super.getNonTerminal() + "2";
		}				
	}
}