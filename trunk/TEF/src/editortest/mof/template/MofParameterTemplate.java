package editortest.mof.template;

import java.util.Arrays;
import java.util.List;

import editortest.model.IModel;
import editortest.template.ElementTemplate;
import editortest.template.IdentifierTemplate;
import editortest.template.ReferenceTemplate;
import editortest.template.Template;
import editortest.template.TerminalTemplate;
import editortest.text.Proposal;

public class MofParameterTemplate extends ElementTemplate {
	public MofParameterTemplate(IModel model) {
		super(model, model.getMetaElement("Parameter"));
	}

	@Override
	public Template[] createTemplates() {		
		return new Template[] {
				new ReferenceTemplate(getModel(), "type", 
						getMetaElement(), 
						getModel().getMetaElement("Type")),
				new TerminalTemplate(" "),
				new IdentifierTemplate(getModel(), "name", getMetaElement())
		};
	}
	
	@Override
	public List<Proposal> getProposals() {
		return Arrays.asList(new Proposal[] { 
				new Proposal("parameter... ", "<borken-ref> <unnamed>")
		});
	}
}
