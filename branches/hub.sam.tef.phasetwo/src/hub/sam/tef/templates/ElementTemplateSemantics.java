package hub.sam.tef.templates;

import hub.sam.tef.models.ICommand;
import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelChangeListener;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.parse.IASTBasedModelUpdater;
import hub.sam.tef.parse.ISyntaxProvider;
import hub.sam.tef.parse.ModelUpdateConfiguration;
import hub.sam.tef.parse.TextBasedAST;
import hub.sam.tef.parse.TextBasedUpdatedAST;
import hub.sam.tef.treerepresentation.IDisposable;
import hub.sam.tef.treerepresentation.ITreeRepresentationProvider;
import hub.sam.tef.treerepresentation.ModelTreeContents;
import hub.sam.tef.treerepresentation.TreeRepresentation;
import hub.sam.tef.views.CompoundText;
import hub.sam.tef.views.Text;

public class ElementTemplateSemantics extends ValueTemplateSemantics implements IASTBasedModelUpdater, ISyntaxProvider, ITreeRepresentationProvider {

	private final IModel fModel;
	private final IMetaModelElement fMetaModelElement;
	private final ElementTemplate fElementTemplate;
	
	protected ElementTemplateSemantics(ElementTemplate elementTemplate) {
		super(elementTemplate);
		
		this.fElementTemplate = elementTemplate;
		this.fModel = elementTemplate.getModel();
		this.fMetaModelElement = elementTemplate.getMetaElement();
	}

	public void executeModelUpdate(ModelUpdateConfiguration configuration) {
		IModelElement newElement = null;
		
		
		if (configuration.getOwner() != null) {
			if (configuration.isOldNode() && configuration.isHasOldParent()) {
				// nothing, everything is old, everything stays old
				newElement = configuration.getAst().getElement().getModelElement();
			} else if (configuration.isOldNode() && !configuration.isHasOldParent()) {
				// set the old value into the new owner
				IModelElement theOldElement = configuration.getAst().getElement().getModelElement();
				newElement = theOldElement;
				if (configuration.isCollection()) {					
					if (configuration.hasPosition()) {
						fModel.getCommandFactory().add(configuration.getOwner(), configuration.getProperty(), theOldElement, configuration.getPosition()).execute();
					} else {
						fModel.getCommandFactory().add(configuration.getOwner(), configuration.getProperty(), theOldElement).execute();
					}
				} else {
					fModel.getCommandFactory().set(configuration.getOwner(), configuration.getProperty(), theOldElement).execute();;
				}
			} else if (!configuration.isOldNode() && configuration.isHasOldParent()) {
				// replace the old element in the old owner
				if (configuration.isComposite()) {
					if (configuration.isCollection()) {
						// just add, everything else has been taken care of by the sequence
						if (configuration.hasPosition()) {
							ICommand createChild = fModel.getCommandFactory().createChild(configuration.getOwner(), 
									fMetaModelElement, configuration.getProperty(), configuration.getPosition());
							createChild.execute();
							newElement = (IModelElement)createChild.getResult().iterator().next();
						} else {
							ICommand createChild = fModel.getCommandFactory().createChild(configuration.getOwner(), 
									fMetaModelElement, configuration.getProperty(), configuration.getPosition());
							createChild.execute();
							newElement = (IModelElement)createChild.getResult().iterator().next();
						}	
					} else {
						// remove the old element and add the new one							
						fModel.getCommandFactory().set(configuration.getOwner(), configuration.getProperty(), 
									null).execute();
						
						ICommand createChild = fModel.getCommandFactory().createChild(configuration.getOwner(), 
								fMetaModelElement, configuration.getProperty());
						createChild.execute();
						newElement = (IModelElement)createChild.getResult().iterator().next();
					}
				} else {
					newElement = fElementTemplate.createMockObject();
					fModel.getCommandFactory().set(configuration.getOwner(), configuration.getProperty(), newElement).execute();
				}
				// TODO What happens with the oldValue?
			} else {
				if (configuration.isComposite()) {
					ICommand createChild = fModel.getCommandFactory().createChild(configuration.getOwner(), 
							fMetaModelElement, configuration.getProperty());
					createChild.execute();
					newElement = (IModelElement)createChild.getResult().iterator().next();
				} else {
					newElement = fElementTemplate.createMockObject();
					fModel.getCommandFactory().set(configuration.getOwner(), configuration.getProperty(), newElement).execute();
				}
			}
		} else {
			if (!configuration.isOldNode()) {
				newElement = fModel.createElement(fMetaModelElement);					
				fModel.getCommandFactory().remove(fModel.getOutermostCompositesOfEditedResource(), 
						fModel.getOutermostCompositesOfEditedResource().iterator().next()).execute();
				fModel.getCommandFactory().add(fModel.getOutermostCompositesOfEditedResource(), newElement).execute();
			} else {
				newElement = configuration.getAst().getElement().getModelElement();
			}
		}
		
		for (Template nestedTemplate: fElementTemplate.getNestedTemplates()) {
			if (nestedTemplate instanceof PropertyTemplate) {
				PropertyTemplate propertyTemplate = (PropertyTemplate)nestedTemplate;
				TextBasedUpdatedAST propertyChild = configuration.getAst().getChild(propertyTemplate.getProperty());
				if (propertyChild != null) {
					// check wether the child is old, but not old. In that case this node is no old parent anymore.
					boolean isOldNode = configuration.isOldNode();
					if (propertyTemplate instanceof SingleValueTemplate) {
						isOldNode &= propertyChild.getElement() == null || propertyChild.getElement().getModelElement().equals(newElement.getValue(propertyTemplate.getProperty()));
					}
					propertyTemplate.getAdapter(IASTBasedModelUpdater.class).executeModelUpdate(new ModelUpdateConfiguration(
							propertyChild, newElement, propertyTemplate.getProperty(), isOldNode));
				} else {
					String propertyStringValue = configuration.getAst().getStringValueForProperty(propertyTemplate.getProperty());
											
					propertyTemplate.getValueTemplate().getAdapter(IASTBasedModelUpdater.class).
							executeModelUpdate(new ModelUpdateConfiguration(null, newElement, propertyTemplate.getProperty(), configuration.isOldNode()).
									createPrimitiveConfiguration(propertyStringValue));
				}
			}
		}	
		// TODO all flag templates or optional templates that are not covered by this rule have to be set with default values.
	}	

	public String getNonTerminal() {
		return fElementTemplate.getType().toString();
	}
	
	/**
	 * Provides a parser rule for this element template: a sequence based on all sub-templates.
	 */
	public String[][] getRules() {
		String[] result = new String[fElementTemplate.getNestedTemplates().length+1];
		result[0] = getNonTerminal();
		int i = 1;
		for(Template part: fElementTemplate.getNestedTemplates()) {
			result[i++] = part.getAdapter(ISyntaxProvider.class).getNonTerminal();
		}
		return new String[][] { result };					
	}

	public TextBasedAST createAST(TextBasedAST parent, IModelElement model, Text text) {
		if (!fElementTemplate.equals(text.getElement(Template.class))) {
			throw new RuntimeException("assert");
		}
		TextBasedAST result = new TextBasedAST(text);
		if (parent != null) {
			parent.addChild(result);
		}
		int i = 0;
		for (Template template: fElementTemplate.getNestedTemplates()) {
			if (template instanceof PropertyTemplate) {
				template.getAdapter(ISyntaxProvider.class).createAST(result, model, ((CompoundText)text).getTexts().get(i));
			}
			i++;
		}		
		return result;
	}

	public Object createTreeRepresentation(String notused, Object model) {
		ModelTreeContents contents = new ModelTreeContents(fElementTemplate, (IModelElement)model);
		TreeRepresentation result = new TreeRepresentation(contents);
		
		for (Template subTemplate: fElementTemplate.getNestedTemplates()) {
			if (subTemplate instanceof PropertyTemplate) {
				String property = ((PropertyTemplate)subTemplate).getProperty();
				result.addContent(property, subTemplate.getAdapter(ITreeRepresentationProvider.class).
						createTreeRepresentation(property, model));
				ModelChangeListener changeListener = 
						new ModelChangeListener(result, (PropertyTemplate)subTemplate, (IModelElement)model);
				result.registerComponent(changeListener);
			} else if (subTemplate instanceof TerminalTemplate) {
				result.addContent(((TerminalTemplate)subTemplate).getTerminalText());
			} else {
				throw new RuntimeException("assert");
			}
		}
		
		return result;
	}
	
	class ModelChangeListener implements IModelChangeListener, IDisposable {
		private final TreeRepresentation fRepresentation;
		private final PropertyTemplate fTemplate;
		private final IModelElement fModel;				
		
		public ModelChangeListener(final TreeRepresentation representation, final PropertyTemplate template, final IModelElement model) {
			super();
			fRepresentation = representation;
			fTemplate = template;
			fModel = model;
			fModel.addChangeListener(this);
		}
				
		public void dispose() {
			fModel.removeChangeListener(this);			
		}

		public void propertyChanged(Object element, String changedProperty) {
			String property = fTemplate.getProperty();
			if (changedProperty.equals(property)) {
				fRepresentation.changeContent(property, fTemplate.getAdapter(ITreeRepresentationProvider.class).
						createTreeRepresentation(property, fModel));
			}
		}		
	}
}
