package hub.sam.tef.tdl.templates;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;

import hub.sam.tef.documents.IDocumentModelProvider;
import hub.sam.tef.emf.model.EMFModel;
import hub.sam.tef.emf.model.EMFModelElement;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.reconciliation.syntax.BlockLayout;
import hub.sam.tef.reconciliation.syntax.ExpressionLayout;
import hub.sam.tef.tdl.EcoreModelDescriptor;
import hub.sam.tef.tdl.Syntax;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.OptionalTemplate;
import hub.sam.tef.templates.ReferenceTemplate;
import hub.sam.tef.templates.SequenceTemplate;
import hub.sam.tef.templates.SingleValueTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.TerminalTemplate;
import hub.sam.tef.templates.ValueTemplate;
import hub.sam.tef.templates.WhitespaceTemplate;
import hub.sam.tef.templates.primitives.IdentifierTemplate;
import hub.sam.tef.templates.primitives.StringLiteralTemplate;

public class SyntaxTemplate extends ElementTemplate {

	
	public SyntaxTemplate(IDocumentModelProvider modelProvider) {
		super(modelProvider, modelProvider.getModel().getMetaElement("Syntax"));	
	}

	@Override
	public Template[] createTemplates() {
		return new Template[] {
				new TerminalTemplate(this, "syntax"),
				new WhitespaceTemplate(this, BlockLayout.SPACE),
				new TerminalTemplate(this, "toplevel"),
				new WhitespaceTemplate(this, BlockLayout.SPACE),
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
				new SingleValueTemplate<IModelElement>(this, "ecoreModel") {
					@Override
					protected ValueTemplate<IModelElement> createValueTemplate() {
						return new OptionalTemplate(this, this.getModel().getMetaElement("EcoreModelDescriptor")) {
								@Override
								public Template[] createOptionTemplate() {
									return new Template[] {
										new WhitespaceTemplate(this, BlockLayout.EMPTY),
										new TerminalTemplate(this, ","),										
										new WhitespaceTemplate(this, BlockLayout.SPACE),
										new TerminalTemplate(this, "ecorepath"),
										new WhitespaceTemplate(this, BlockLayout.SPACE),
										new ElementTemplate(this, getModel().getMetaElement("EcoreModelDescriptor")) {
											@Override
											public Template[] createTemplates() {
												return new Template[] { 
														new SingleValueTemplate<String>(this, "pathToModel") {
															@Override
															protected ValueTemplate<String> createValueTemplate() {
																return new StringLiteralTemplate(this);
															}
														}
												};												
											}									
										}
									};
								}				
						};
					}				
				},	
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

	@Override
	public String check(IModelElement modelElement) {
		Syntax sytax = (Syntax)((EMFModelElement)modelElement).getEMFObject();
		EcoreModelDescriptor ecoreModel = sytax.getEcoreModel();
		if (ecoreModel != null) {			
			String pathString = ecoreModel.getPathToModel();
			try {
				EditingDomain domain = ((EMFModel)getModel()).getDomain();			
				URI resource = URI.createURI(pathString);					
				 domain.getResourceSet().getResource(resource, true);
			} catch (Exception ex) {
				return "could not load the path " + pathString + " (reason: " + ex.getMessage() + ")";
			}
		}
		return null;
	}		
}
