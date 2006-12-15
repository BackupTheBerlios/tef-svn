package editortest.emf.ecore.templates;

import java.util.Arrays;
import java.util.List;

import editortest.emf.model.EMFMetaModelElement;
import hub.sam.tef.controllers.Proposal;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.FlagTemplate;
import hub.sam.tef.templates.IntegerTemplate;
import hub.sam.tef.templates.LayoutElementTemplate;
import hub.sam.tef.templates.LayoutManager;
import hub.sam.tef.templates.ReferenceTemplate;
import hub.sam.tef.templates.SequenceTemplate;
import hub.sam.tef.templates.SingleValueTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.TerminalTemplate;
import hub.sam.tef.templates.ValueTemplate;

public class EAttributeTemplate extends ElementTemplate {

	public EAttributeTemplate(Template template) {
		super(template, template.getModel().getMetaElement("EReference"));
	}
	
	@Override
	public Template[] createTemplates() {
		return new Template[] {
				new LayoutElementTemplate(this, LayoutManager.INDENT),				
				new TerminalTemplate(this,  "reference ", TerminalTemplate.KEY_WORD_HIGHLIGHT),
				new SingleValueTemplate<Boolean>(this, "derived") {
					@Override
					protected ValueTemplate<Boolean> createValueTemplate() {
						return new FlagTemplate(this, "isDerived");
					}					
				},
				new SingleValueTemplate<Boolean>(this, "changeable") {
					@Override
					protected ValueTemplate<Boolean> createValueTemplate() {
						return new FlagTemplate(this, "isChangeable");
					}					
				},
				new SingleValueTemplate<Boolean>(this, "unsettable") {
					@Override
					protected ValueTemplate<Boolean> createValueTemplate() {
						return new FlagTemplate(this, "isUnsettable");
					}					
				},
				new SingleValueTemplate<String>(this, "eType") {
					@Override
					protected ValueTemplate createValueTemplate() {
						return new ReferenceTemplate(this, getModel().getMetaElement("EClass"), null) {
							@Override
							protected ElementTemplate getElementTemplate() {
								return new EIdentifierTemplate(this);
							}						
						};
					}					
				},	
				new TerminalTemplate(this, "["),
				new SingleValueTemplate(this, "lowerBound") {
					@Override
					protected ValueTemplate<Integer> createValueTemplate() {
						return new IntegerTemplate(this, 0);
					}					
				},
				new TerminalTemplate(this, ".."),
				new SingleValueTemplate(this, "upperBound") {
					@Override
					protected ValueTemplate<Integer> createValueTemplate() {
						return new IntegerTemplate(this, 1);
					}					
				},
				new TerminalTemplate(this, "] "),				
				new SingleValueTemplate<String>(this, "name") {
					@Override
					protected ValueTemplate<String> createValueTemplate() {
						return new IdentifierValueTemplate(this);
					}					
				}
		};
	}
	
	@Override
	public List<Proposal> getProposals() {
		return Arrays.asList(new Proposal[] { 
				new Proposal(((EMFMetaModelElement)getMetaElement()).getEMFObject().getName() + " ...", null, 0)
		});
	}

}
