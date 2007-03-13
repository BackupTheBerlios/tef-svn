/*
 * Textual Editing Framework (TEF)
 * Copyright (C) 2006 Markus Scheidgen
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, 
 * MA 02111-1307 USA
 */
package hub.sam.tef.templates;

import hub.sam.tef.controllers.CursorMovementStrategy;
import hub.sam.tef.controllers.IDeleteEventHandler;
import hub.sam.tef.controllers.IProposalHandler;
import hub.sam.tef.controllers.MarkFlag;
import hub.sam.tef.controllers.Proposal;
import hub.sam.tef.models.ICommand;
import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.parse.IASTBasedModelUpdater;
import hub.sam.tef.parse.ISyntaxProvider;
import hub.sam.tef.parse.ModelUpdateConfiguration;
import hub.sam.tef.parse.TextBasedAST;
import hub.sam.tef.parse.TextBasedUpdatedAST;
import hub.sam.tef.treerepresentation.ITreeRepresentationFromModelProvider;
import hub.sam.tef.treerepresentation.ModelBasedTreeContent;
import hub.sam.tef.treerepresentation.TreeRepresentation;
import hub.sam.tef.views.CompoundText;
import hub.sam.tef.views.FixText;
import hub.sam.tef.views.Text;

import java.util.List;
import java.util.Vector;


/**
 * This is a ValueTemplate that represents different types of 
 * values.
 */
public abstract class ChoiceTemplate extends ValueTemplate<IModelElement> {
	
	private final ValueTemplate<IModelElement>[] fAlternativeTemplates;
	private final IMetaModelElement fMetaModelElement;
	
	public ChoiceTemplate(Template template, IMetaModelElement metaModelElement) {
		super(template, metaModelElement);
		this.fMetaModelElement = metaModelElement;
		this.fAlternativeTemplates = createAlternativeTemplates();
	}

	/**
	 * @return A set of value templates. These are the templates for all
	 *         possible values for this template.
	 */
	public abstract ValueTemplate<IModelElement>[] createAlternativeTemplates();
	
	@Override
	public Template[] getNestedTemplates() {
		return fAlternativeTemplates;
	}

	@Override
	public List<Proposal> getProposals() {
		List<Proposal> result = new Vector<Proposal>();
		for(ValueTemplate<IModelElement> alternativeTemplate: fAlternativeTemplates) {
			result.addAll(alternativeTemplate.getProposals());
		}
		return result;
	}
	
	private Text createChoiceValueView(IModelElement model, IValueChangeListener<IModelElement> changeListener) {		
			for(ValueTemplate alternativeTemplate: fAlternativeTemplates) {
				if (alternativeTemplate.isTemplateFor(model)) {
					return alternativeTemplate.getView(model, changeListener);				
				}
			}
			throw new TemplateException("non fullfilled alternative");
	}	
	
	public ICommand getCommandForProposal(Proposal proposal, IModelElement owner, 
			String property, int index) {
		for(ValueTemplate template: fAlternativeTemplates) {
			if (template.getProposals().contains(proposal)) {
				return template.getCommandForProposal(proposal, owner, property, index);
			}
		}
		return null;		
	}
	
	/**
	 * Returns true for those meta model elements that this element template
	 * provides representations for.
	 */
	@Override
	public boolean isTemplateFor(IModelElement model) {
		if (model instanceof IModelElement) {
			return fMetaModelElement.isMetaModelFor((IModelElement)model);
		} else {
			return super.isTemplateFor(model);
		}
	}	
	
	@Override
	public void updateView(Text view, IModelElement value) {
		((CompoundText)view).replaceText(((CompoundText)view).getTexts().get(0), createValueView(value, 
				view.getElement(IValueChangeListener.class))); 
	}

	@Override
	public Text createView(final IModelElement model, IValueChangeListener<IModelElement> changeListener) {
		CompoundText result = new CompoundText();
		Text valueText = createValueView(model, changeListener);
		result.setElement(IValueChangeListener.class, changeListener);
		result.addText(valueText);						
		return result;
	}
	
	private Text createValueView(IModelElement value, final IValueChangeListener<IModelElement> changeListener) {
		if (value != null) {
			Text valueText = createChoiceValueView(value, changeListener);
			valueText.setElement(CursorMovementStrategy.class, new CursorMovementStrategy(true, true));
			valueText.setElement(MarkFlag.class, new MarkFlag());
			valueText.addElement(IDeleteEventHandler.class,  new RemoveTextEventListener(changeListener));
			return valueText;
		} else{			
			Text seed = new FixText("<...>");
			seed.addElement(IProposalHandler.class, new IProposalHandler() {
				public ProposalKind getProposalKind() {
					return ProposalKind.insert;
				}		

				public List<Proposal> getProposals(Text context, int offset) {
					return ChoiceTemplate.this.getProposals();
				}

				public boolean handleProposal(Text text, int offset, Proposal proposal) {
					if (getProposals(text, offset).contains(proposal)) {
						changeListener.newValue(proposal, ChoiceTemplate.this);								
						return true;
					} else {
						return false;
					}
				}		
			});			
			seed.setElement(CursorMovementStrategy.class, new CursorMovementStrategy(true, true));
			return seed;
		}
	}
	
	/**
	 * This controller element is notified when the user selects a element for
	 * deletion. It will then remove the according value from the property's values.
	 */
	class RemoveTextEventListener implements IDeleteEventHandler {
		private final IValueChangeListener<IModelElement> fChangeListener;
		
		public RemoveTextEventListener(final IValueChangeListener<IModelElement> changeListener) {
			super();
			fChangeListener = changeListener;
		}

		public void handleEvent(Text text) {
			fChangeListener.removeValue();
		}			
	}		
		
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (IASTBasedModelUpdater.class == adapter || ISyntaxProvider.class == adapter) {
			return (T)new ModelUpdater(this);
		} else if (ITreeRepresentationFromModelProvider.class == adapter) {
			return (T)new TreeRepresentationProvider();
		} else {
			return super.getAdapter(adapter);
		}
	}

	class ModelUpdater extends ValueTemplateSemantics implements IASTBasedModelUpdater, ISyntaxProvider {
				
		protected ModelUpdater(ValueTemplate template) {
			super(template);		
		}

		public void executeModelUpdate(ModelUpdateConfiguration configuration) {	
			TextBasedUpdatedAST childNode = configuration.getAst().getChildNodes().get(0);
			boolean successful = false;
			for(ValueTemplate alternatives: fAlternativeTemplates) {
				if (alternatives.getAdapter(ISyntaxProvider.class).getNonTerminal().equals(childNode.getSymbol())) {
					alternatives.getAdapter(IASTBasedModelUpdater.class).
							executeModelUpdate(configuration.createDelegateConfiguration(childNode));
					successful = true;
				}
			}
			if (!successful) {
				throw new RuntimeException("assert");
			}
		}

		public String[][] getRules() {
			String[][] result = new String[fAlternativeTemplates.length][];
			int i = 0;
			for(Template choice: fAlternativeTemplates) {
				result[i++] = new String[] { getNonTerminal(), choice.getAdapter(ISyntaxProvider.class).getNonTerminal() };
			}
			return result;					
		}

		public TextBasedAST createAST(TextBasedAST parent, IModelElement model, Text text) {
			if (text.getElement(Template.class) != null) {
				if (!text.getElement(Template.class).equals(ChoiceTemplate.this)) {
					throw new RuntimeException("assert");
				}
				TextBasedAST result = new TextBasedAST(text);
				parent.addChild(result);
				parent = result;
				Text childText = ((CompoundText)text).getTexts().get(0);
				return childText.getElement(Template.class).getAdapter(ISyntaxProvider.class).createAST(parent, model, childText);
			} else {
				return null;
			}
		}			
	}
	
	class TreeRepresentationProvider implements ITreeRepresentationFromModelProvider {
		public TreeRepresentation createTreeRepresentation(TreeRepresentation parent, String property, Object model) {			
			ModelBasedTreeContent contents = new ModelBasedTreeContent(ChoiceTemplate.this, (IModelElement)model);
			TreeRepresentation treeRepresentation = new TreeRepresentation(contents);
			((ModelBasedTreeContent)parent.getElement()).addContent(contents);
			parent.addChild(treeRepresentation);
			for (ValueTemplate alternative: fAlternativeTemplates) {
				if (alternative.isTemplateFor(model)) {
					return alternative.getAdapter(ITreeRepresentationFromModelProvider.class).
							createTreeRepresentation(treeRepresentation, property, model);
				}
			}
			throw new RuntimeException("assert");
		}		
	}
}
