package hub.sam.tef.tdl.delegators;

import java.util.HashMap;
import java.util.Map;

import hub.sam.tef.documents.IDocumentModelProvider;
import hub.sam.tef.tdl.TDLElementTemplate;
import hub.sam.tef.tdl.TDLIdentifierTemplate;
import hub.sam.tef.tdl.TDLPrimitiveValueTemplate;
import hub.sam.tef.tdl.TDLSingleValueTemplate;
import hub.sam.tef.tdl.TDLTemplate;
import hub.sam.tef.tdl.TemplateRef;
import hub.sam.tef.tdl.TemplateValue;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.primitives.IdentifierTemplate;

/**
 * A syntax description can define a template once and use it over and over again. We use this
 * factory to control the singleton property of each template even if it is used several times.
 * 
 */
public class TemplateFactory {

	private final Map<TDLTemplate, Template> templates = new HashMap<TDLTemplate, Template>();	
	
	public Template getTemplate(Template father, TemplateValue tdlTemplate) {
		if (tdlTemplate instanceof TemplateRef) {
			Template result = templates.get(tdlTemplate);
			if (result == null) {
				throw new TDLException("Not created template requested: " + 
						((TemplateRef)tdlTemplate).getTemplate().getName());
			} else {
				return result;
			}
		} else if (tdlTemplate instanceof TDLPrimitiveValueTemplate) {
			if (tdlTemplate instanceof TDLIdentifierTemplate) {
				return new IdentifierTemplate(father);
			} else {
				throw new TDLException("Unknown template kind: " + tdlTemplate.eClass().getName());
			}
		} else {
			Template result = null;
			if (tdlTemplate instanceof TDLElementTemplate) {
				result = new TDLElementTemplateDlg(father, (TDLElementTemplate)tdlTemplate, this);
			} else if (tdlTemplate instanceof TDLSingleValueTemplate) {
				if (!(father instanceof ElementTemplate)) {
					throw new TDLException("Parent template of a single value template is not an element template.");
				}
				result = new TDLSingleValueTemplateDlg((ElementTemplate)father, (TDLSingleValueTemplate)tdlTemplate, this);
			} else {
				throw new TDLException("Unknown template kind: " + tdlTemplate.eClass().getName());
			}
			templates.put((TDLTemplate)tdlTemplate, result);
			return result;
		}		
	}
	
	public Template createTemplate(IDocumentModelProvider modelProvider, TDLTemplate tdlTemplate) {
		if (tdlTemplate instanceof TDLElementTemplate) {
			return new TDLElementTemplateDlg(modelProvider, (TDLElementTemplate)tdlTemplate, this);
		} else {
			throw new TDLException("Unallowed top level template kind: " + tdlTemplate.eClass().getName());
		}
	}
}
