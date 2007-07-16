package hub.sam.tef.tdl.templates;

import hub.sam.tef.annotations.CouldNotResolveIdentifierException;
import hub.sam.tef.documents.IDocumentModelProvider;
import hub.sam.tef.models.ICollection;
import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.reconciliation.syntax.BlockLayout;
import hub.sam.tef.reconciliation.treerepresentation.ASTElementNode;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.ReferenceTemplate;
import hub.sam.tef.templates.SequenceTemplate;
import hub.sam.tef.templates.SingleValueTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.TerminalTemplate;
import hub.sam.tef.templates.ValueTemplate;
import hub.sam.tef.templates.WhitespaceTemplate;

public class SyntaxTemplate extends ElementTemplate {

	
	public SyntaxTemplate(IDocumentModelProvider modelProvider) {
		super(modelProvider, modelProvider.getModel().getMetaElement("Syntax"));	
	}

	@Override
	public Template[] createTemplates() {
		return new Template[] {
				new TerminalTemplate(this, "syntax"),
				new WhitespaceTemplate(this, BlockLayout.SPACE),
				new TerminalTemplate(this, "("),
				new WhitespaceTemplate(this, BlockLayout.EMPTY),
				new SingleValueTemplate<IModelElement>(this, "topLevelTemplate") {
					@Override
					protected ValueTemplate<IModelElement> createValueTemplate() {
						return new ReferenceTemplate(this, getModel().getMetaElement("TemplateValue")) {
							@Override
							protected ElementTemplate getElementTemplate() {
								return new TDLTemplateIdentifierTemplate(this);
							}							
						};
					}					
				},
				new WhitespaceTemplate(this, BlockLayout.EMPTY),
				new TerminalTemplate(this, ")"),
				new WhitespaceTemplate(this, BlockLayout.SPACE),
				new TerminalTemplate(this, "{"),
				new WhitespaceTemplate(this, BlockLayout.BEGIN_BLOCK),
				new SequenceTemplate<IModelElement>(this, "templates", null, false) {
					@Override
					protected ValueTemplate<IModelElement> createValueTemplate() {
						return new TDLTemplateTemplate(this);
					}					
				},
				new WhitespaceTemplate(this, BlockLayout.END_BLOCK),
				new TerminalTemplate(this, "}")
		};
	}
}
