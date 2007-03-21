package hub.sam.tef.templates;

import hub.sam.tef.ErrorAnnotation;
import hub.sam.tef.models.ICommand;
import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelChangeListener;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.models.extensions.InternalModelElement;
import hub.sam.tef.parse.ISemanticProvider;
import hub.sam.tef.treerepresentation.IDisposable;
import hub.sam.tef.treerepresentation.ISyntaxProvider;
import hub.sam.tef.treerepresentation.ITreeRepresentationProvider;
import hub.sam.tef.treerepresentation.ModelTreeContents;
import hub.sam.tef.treerepresentation.SemanticsContext;
import hub.sam.tef.treerepresentation.TreeRepresentation;
import hub.sam.tef.treerepresentation.TreeRepresentationLeaf;

import org.eclipse.jface.text.Position;

public class ElementTemplateSemantics extends ValueTemplateSemantics implements ISyntaxProvider, ITreeRepresentationProvider, ISemanticProvider {

	private final IModel fModel;
	private final IMetaModelElement fMetaModelElement;
	private final ElementTemplate fElementTemplate;
	
	protected ElementTemplateSemantics(ElementTemplate elementTemplate) {
		super(elementTemplate);
		
		this.fElementTemplate = elementTemplate;
		this.fModel = elementTemplate.getModel();
		this.fMetaModelElement = elementTemplate.getMetaElement();
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

	public TreeRepresentationLeaf createTreeRepresentation(IModelElement owner, String notused, Object model, boolean isComposite) {
		ModelTreeContents contents = new ModelTreeContents(fElementTemplate, (IModelElement)model);
		TreeRepresentation result = new TreeRepresentation(contents);
		if (model == null) {
			model = fElementTemplate.createMockObject();
			fElementTemplate.getModel().getCommandFactory().add(owner, notused, model);
		} else {
			//((IModelElement)model).registerOccurence(contents);
		}
		
		for (Template subTemplate: fElementTemplate.getNestedTemplates()) {
			if (subTemplate instanceof PropertyTemplate) {
				String property = ((PropertyTemplate)subTemplate).getProperty();
				result.addContent(property, subTemplate.getAdapter(ITreeRepresentationProvider.class).
						createTreeRepresentation((IModelElement)model, property, model, true));
				ModelChangeListener changeListener = 
						new ModelChangeListener(result, (PropertyTemplate)subTemplate, (IModelElement)model);
				result.registerComponentListener(changeListener);
			} else if (subTemplate instanceof TerminalTemplate) {
				result.addContent(((TerminalTemplate)subTemplate).getTerminalText());
			} else {
				throw new RuntimeException("assert");
			}
		}
		
		return result;
	}	

	public Object createCompositeModel(IModelElement parent, String property, TreeRepresentationLeaf tree, boolean isComposite) {
		IModelElement result = null;
		boolean createModelForProperties = true;
		if (parent != null && isComposite) {
			ICommand createCommand = fModel.getCommandFactory().createChild(parent, fMetaModelElement, property);
			createCommand.execute();
			result = (IModelElement)createCommand.getResult().iterator().next();
			tree.setElement(new ModelTreeContents(fElementTemplate, result));
		} else if (parent != null && !isComposite) {		
			createModelForProperties = false;			
		} else {
			result = fModel.createElement(fMetaModelElement);
			tree.setElement(new ModelTreeContents(fElementTemplate, result));
		}
		
		
		if (createModelForProperties) {
			for (Template subTemplate: fElementTemplate.getNestedTemplates()) {
				if (subTemplate instanceof PropertyTemplate) {
					String childProperty = ((PropertyTemplate)subTemplate).getProperty();
					subTemplate.getAdapter(ITreeRepresentationProvider.class).
							createCompositeModel(result, childProperty, ((TreeRepresentation)tree).getContent(childProperty), true);
				}
			}
		}
		return result;
	}
	
	public Object createReferenceModel(IModelElement parent, String property, TreeRepresentationLeaf tree, boolean isComposite, SemanticsContext context) {
		IModelElement result = null;
		boolean createModelForProperties = true;
		if (parent != null && isComposite) {
			result = ((ModelTreeContents)tree.getElement()).getModelElement();
		} else if (parent != null && !isComposite) {		
			// try to resolve			
			loop: for (IModelElement possibility: context.getValidElements(
					((ReferenceTemplate)tree.getParent().getElement().getTemplate()).getTypeModel())) {
				if (possibility.getValue("name") != null && 
						possibility.getValue("name").equals(((TreeRepresentation)tree).getContent("name").getContent())) {									
					result = possibility;
					break loop;
				}
			}						
			if (result == null) {
				result = fElementTemplate.createMockObject();
			} else {
				createModelForProperties = false;
			}
			fModel.getCommandFactory().set(parent, property, result).execute();
			tree.setElement(new ModelTreeContents(fElementTemplate, result));
		} else {
			result = ((ModelTreeContents)tree.getElement()).getModelElement();
		}		
		
		if (createModelForProperties) {
			for (Template subTemplate: fElementTemplate.getNestedTemplates()) {
				if (subTemplate instanceof PropertyTemplate) {
					String childProperty = ((PropertyTemplate)subTemplate).getProperty();
					subTemplate.getAdapter(ITreeRepresentationProvider.class).
							createReferenceModel(result, childProperty, ((TreeRepresentation)tree).getContent(childProperty), true, context);
				}
			}
		}
		return result;
	}

	public void check(TreeRepresentation representation, SemanticsContext context) {	
		IModelElement modelElement = ((ModelTreeContents)representation.getElement()).getModelElement();
		if (modelElement instanceof InternalModelElement) {			
			context.getAnnotationModelProvider().addAnnotation(new ErrorAnnotation(),
					new Position(representation.getAbsoluteOffset(0), representation.getLength()));
		} else {
			context.addModelElementOccurence(modelElement, 
					new Position(representation.getAbsoluteOffset(0), representation.getLength()));
		}
		
		for (Template subTemaplte: fElementTemplate.getNestedTemplates()) {
			if (subTemaplte instanceof PropertyTemplate) {
				Object value = representation.getContent(((PropertyTemplate)subTemaplte).getProperty());
				if (value instanceof TreeRepresentation) {
					subTemaplte.getAdapter(ISemanticProvider.class).check((TreeRepresentation)value, context);
				}
			}
		}
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

		public boolean isActive() {			
			return true;
		}

		public void propertyChanged(Object element, String changedProperty) {
			String property = fTemplate.getProperty();
			if (changedProperty.equals(property)) {
				fRepresentation.changeContent(property, fTemplate.getAdapter(ITreeRepresentationProvider.class).
						createTreeRepresentation(null, property, fModel, true));
			}
		}		
	}
}
