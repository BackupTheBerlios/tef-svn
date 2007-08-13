package hub.sam.tef.tdl.delegators;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import hub.sam.tef.documents.IDocumentModelProvider;
import hub.sam.tef.tdl.TDLBooleanLiteralTemplate;
import hub.sam.tef.tdl.TDLChoiceTemplate;
import hub.sam.tef.tdl.TDLElementTemplate;
import hub.sam.tef.tdl.TDLElementValueTemplate;
import hub.sam.tef.tdl.TDLEmptyElementTemplate;
import hub.sam.tef.tdl.TDLIdentifierTemplate;
import hub.sam.tef.tdl.TDLIntegerTemplate;
import hub.sam.tef.tdl.TDLPrimitiveValueTemplate;
import hub.sam.tef.tdl.TDLReferenceTemplate;
import hub.sam.tef.tdl.TDLSequenceTemplate;
import hub.sam.tef.tdl.TDLSingleValueTemplate;
import hub.sam.tef.tdl.TDLStringLiteralTemplate;
import hub.sam.tef.tdl.TDLTemplate;
import hub.sam.tef.tdl.TDLTerminalTemplate;
import hub.sam.tef.tdl.TemplateRef;
import hub.sam.tef.tdl.TemplateValue;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.TerminalTemplate;
import hub.sam.tef.templates.primitives.BooleanLiteralTemplate;
import hub.sam.tef.templates.primitives.IdentifierTemplate;
import hub.sam.tef.templates.primitives.IntegerTemplate;
import hub.sam.tef.templates.primitives.StringLiteralTemplate;

/**
 * A syntax description can define a template once and use it over and over again. We use this
 * factory to control the singleton property of each template even if it is used several times.
 * 
 */
public class TemplateFactory {

	private final Map<TDLTemplate, Template> templates = new HashMap<TDLTemplate, Template>();	
	private final Map<String, Class> userTemplateClasses = new HashMap<String, Class>();
	
	public Template getTemplate(Template father, TemplateValue tdlTemplate) {
		if (tdlTemplate instanceof TemplateRef) {
			TDLTemplate template = ((TemplateRef)tdlTemplate).getTemplate();
			Template result = templates.get(template);
			if (result == null) {
				return getTemplate(father, template);
				//throw new TDLException("Not created template requested: " + 
				//		((TemplateRef)tdlTemplate).getTemplate().getName());				
			} else {
				return result;
			}
		} else if (tdlTemplate instanceof TDLPrimitiveValueTemplate) {
			if (tdlTemplate instanceof TDLIdentifierTemplate) {
				return new IdentifierTemplate(father);
			} else if (tdlTemplate instanceof TDLIntegerTemplate) {
				return new IntegerTemplate(father, 0);
			} else if (tdlTemplate instanceof TDLStringLiteralTemplate) {
				return new StringLiteralTemplate(father);
			} else if (tdlTemplate instanceof TDLBooleanLiteralTemplate) {
				return new BooleanLiteralTemplate(father);
			} else {
				throw new TDLException("Unknown template kind: " + tdlTemplate.eClass().getName());
			}
		} else {
			Template result = tryToInstantiateUserTemplateClass(father, (TDLTemplate)tdlTemplate);
			if (result == null) {							
				if (tdlTemplate instanceof TDLElementTemplate) {							
					result = new TDLElementTemplateDlg(father, (TDLElementTemplate)tdlTemplate, this);
				} else if (tdlTemplate instanceof TDLEmptyElementTemplate) {
					loop: for (Object nestedTemplate: ((TDLEmptyElementTemplate)tdlTemplate).getTemplates()) {
						if (nestedTemplate instanceof TemplateRef) {
							nestedTemplate = ((TemplateRef)nestedTemplate).getTemplate();
						}
						if (nestedTemplate instanceof TDLElementValueTemplate) {
							result = new TDLEmptyElementTemplateDlg(father, (TDLEmptyElementTemplate)tdlTemplate, 
									((TDLElementValueTemplate)nestedTemplate).getMetaElement(), this);
							break loop;
						}
					}					
					if (result == null) {
						throw new TDLException("Empty element template " + 
								((TDLEmptyElementTemplate)tdlTemplate).getName() + " does not contain an element tempalte");
					}
				} else if (tdlTemplate instanceof TDLChoiceTemplate) {
					result = new TDLChoiceTemplateDlg(father, (TDLChoiceTemplate)tdlTemplate, this);
				} else if (tdlTemplate instanceof TDLSingleValueTemplate) {
					if (!(father instanceof ElementTemplate)) {
						throw new TDLException("Parent template of a single value template is not an element template.");
					}
					result = new TDLSingleValueTemplateDlg((ElementTemplate)father, (TDLSingleValueTemplate)tdlTemplate, this);
				} else if (tdlTemplate instanceof TDLSequenceTemplate) {
					if (!(father instanceof ElementTemplate)) {
						throw new TDLException("Parent template of a single value template is not an element template.");
					}
					result = new TDLSequenceTemplateDlg((ElementTemplate)father, (TDLSequenceTemplate)tdlTemplate, this);
				} else if (tdlTemplate instanceof TDLTerminalTemplate) {
					result = new TDLTerminalTemplateDlg(father, (TDLTerminalTemplate)tdlTemplate);
				} else if (tdlTemplate instanceof TDLReferenceTemplate) {
					result = new TDLReferenceTemplateDlg(father, (TDLReferenceTemplate)tdlTemplate, this);
				} else {
					throw new TDLException("Unknown template kind: " + tdlTemplate.eClass().getName());
				}
			}
			templates.put((TDLTemplate)tdlTemplate, result);
			return result;
		}		
	}
	
	public Template createTemplate(IDocumentModelProvider modelProvider, TDLTemplate tdlTemplate) {
		Template result = tryToInstantiateUserTemplateClass(modelProvider, tdlTemplate);
		if (result == null) {
			if (tdlTemplate instanceof TDLElementTemplate) {
				return new TDLElementTemplateDlg(modelProvider, (TDLElementTemplate)tdlTemplate, this);
			} else if (tdlTemplate instanceof TDLChoiceTemplate) {
				return new TDLChoiceTemplateDlg(modelProvider, (TDLChoiceTemplate)tdlTemplate, this);
			} else {
				throw new TDLException("Unallowed top level template kind: " + tdlTemplate.eClass().getName());
			}
		} else {
			return result;
		}
	}
	
	/**
	 * Allows to register classes with contain aditional functionality to be used as realisations for templates
	 * defined with TDL.
	 * 
	 * @param templateName The name of the template as defined in TDL.
	 * @param templateClass The template class.
	 */	
	public void registerTemplateClass(String templateName, Class templateClass) {
		userTemplateClasses.put(templateName, templateClass);
	}
	
	private Template tryToInstantiateUserTemplateClass(Object father, TDLTemplate tdlTemplate) {
		String name = tdlTemplate.getName();
		if (name != null) {
			Class templateClass = userTemplateClasses.get(name);
			if (templateClass != null) {
				for (Constructor constructor: templateClass.getConstructors()) {
					Class[] parameter = constructor.getParameterTypes();
					if (parameter.length == 3 && parameter[0].isAssignableFrom(father.getClass()) &&
							parameter[1].isAssignableFrom(tdlTemplate.getClass()) &&
							parameter[2].isAssignableFrom(this.getClass())) {
						try {
							return (Template)constructor.newInstance(new Object[] {father, tdlTemplate, this});
						} catch (Exception e) {
							System.err.print("Warning: could not instantiate user define template class!");
							return null;
						}	
					}
				}				
				System.err.print("Warning: user define template class with wrong constructor!");
				return null;				
			}
		}
		return null;
	}
}
