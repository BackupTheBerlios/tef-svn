package editortest.emf.ocl.templates;

import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.SequenceTemplate;
import hub.sam.tef.templates.SingleValueTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.TerminalTemplate;
import hub.sam.tef.templates.ValueTemplate;
import hub.sam.tef.templates.adaptors.Proposal;
import hub.sam.tef.templates.layout.ExpressionLayout;
import hub.sam.tef.templates.layout.WhitespaceTemplate;

import java.util.Arrays;
import java.util.List;

import editortest.emf.model.EMFMetaModelElement;

public class IteratorExpTemplate extends ElementTemplate {

	public IteratorExpTemplate(Template template) {
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
			new TerminalTemplate(this, "forAll"), // TODO
			new WhitespaceTemplate(this, ExpressionLayout.EMTPY),
			new TerminalTemplate(this, "("),
			new WhitespaceTemplate(this, ExpressionLayout.EMTPY),
			new SequenceTemplate<IModelElement>(this, "iterator", ",", false) {	
				@Override
				protected ValueTemplate<IModelElement> createValueTemplate() {
					return new VariableTemplate(this);
				}
				@Override
				protected WhitespaceTemplate createSeparatorWhitespace() {
					return new WhitespaceTemplate(this, ExpressionLayout.SPACE_SEPARATOR);
				}				
			},
			new WhitespaceTemplate(this, ExpressionLayout.EMTPY),
			new TerminalTemplate(this, "|"),
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
}
