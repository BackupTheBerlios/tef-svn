package editortest.emf.ocl.templates;

import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.ReferenceTemplate;
import hub.sam.tef.templates.SingleValueTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.TerminalTemplate;
import hub.sam.tef.templates.ValueTemplate;
import editortest.emf.ecore.templates.EIdentifierTemplate;

public class PropertyCallExpTemplate extends ElementTemplate {

	public PropertyCallExpTemplate(Template template) {
		super(template, template.getModel().getMetaElement("PropertyCallExp"));	
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
				new TerminalTemplate(this, "."),
				new SingleValueTemplate<IModelElement>(this, "referredProperty") {
					@Override
					protected ValueTemplate<IModelElement> createValueTemplate() {						
						return new ReferenceTemplate(this, getModel().getMetaElement("EStructuralFeature")) {
							@Override
							protected ElementTemplate getElementTemplate() {
								return new EIdentifierTemplate(this);
							}						
						};						
					}					
				}
		};
	}	
}
