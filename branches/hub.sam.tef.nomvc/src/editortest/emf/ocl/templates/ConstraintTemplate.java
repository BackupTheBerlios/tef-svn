package editortest.emf.ocl.templates;

import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.SingleValueTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.ValueTemplate;
import hub.sam.tef.templates.adaptors.IDocumentModelProvider;

public class ConstraintTemplate  extends ElementTemplate {
	
	public ConstraintTemplate(IDocumentModelProvider modelProvider, IMetaModelElement metaModel) {
		super(modelProvider, metaModel);	
	}

	@Override
	public Template[] createTemplates() {
		return new Template[] {
				new SingleValueTemplate<IModelElement>(this, "body") {
					@Override
					protected ValueTemplate<IModelElement> createValueTemplate() {
						return new OclExpressionTemplate(this);
					}				
				}
		};
	}
	
}
