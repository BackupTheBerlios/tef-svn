package hub.sam.tef.templates;

import java.util.List;
import java.util.Vector;

import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.ErrorAnnotation;
import hub.sam.tef.annotations.ISemanticProvider;
import hub.sam.tef.models.ICommand;
import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelChangeListener;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.models.extensions.InternalModelElement;
import hub.sam.tef.syntax.AbstractLayoutManager;
import hub.sam.tef.syntax.IElementSyntaxProvider;
import hub.sam.tef.syntax.ISyntaxProvider;
import hub.sam.tef.treerepresentation.IASTProvider;
import hub.sam.tef.treerepresentation.ModelASTElement;
import hub.sam.tef.treerepresentation.SemanticsContext;
import hub.sam.tef.treerepresentation.ASTElementNode;
import hub.sam.tef.treerepresentation.ASTNode;
import hub.sam.util.container.IDisposable;

import org.eclipse.jface.text.Position;

public class ElementTemplateSemantics extends ValueTemplateSemantics implements IElementSyntaxProvider, IASTProvider, ISemanticProvider {

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
		String alternativeSymbol = fElementTemplate.getAlternativeSymbol();
		if (alternativeSymbol != null) {
			return alternativeSymbol;
		} else {
			return fElementTemplate.getType().toString();
		}
	}
	
	/**
	 * Provides a parser rule for this element template: a sequence based on all sub-templates.
	 */
	public String[][] getRules() {
		List<String> result = new Vector<String>();
		result.add(getNonTerminal());						
		for(Template part: fElementTemplate.getNestedTemplates()) {
			if (!(part instanceof WhitespaceTemplate) && !(part instanceof LayoutElementTemplate)) {
				result.add(part.getAdapter(ISyntaxProvider.class).getNonTerminal());
			}
		}
		return new String[][] { result.toArray(new String[] {}) };					
	}
	
	public String getPropertyForRuleAndPosition(Rule rule, int position) {
		int i = 0;
		for (Template subTemplate: fElementTemplate.getNestedTemplates()) {
			if (subTemplate instanceof WhitespaceTemplate) {
				
			} else if (subTemplate instanceof TerminalTemplate) {
				i++;
			} else if (subTemplate instanceof PropertyTemplate) {
				if (position == i) {
					return ((PropertyTemplate)subTemplate).getProperty();
				}
				i++;
			} else {
				throw new RuntimeException("assert");
			}
		} 
		return null;		
	}

	public ASTNode createTreeRepresentation(IModelElement owner, String notused, Object model, boolean isComposite, AbstractLayoutManager layout) {
		ModelASTElement contents = new ModelASTElement(fElementTemplate, (IModelElement)model);
		ASTElementNode result = new ASTElementNode(contents);
		if (model == null) {
			model = fElementTemplate.createMockObject();
			fElementTemplate.getModel().getCommandFactory().add(owner, notused, model);
		} else {
			//((IModelElement)model).registerOccurence(contents);
		}
		
		for (Template subTemplate: fElementTemplate.getNestedTemplates()) {
			if (subTemplate instanceof PropertyTemplate) {
				String property = ((PropertyTemplate)subTemplate).getProperty();
				result.addNodeObject(property, subTemplate.getAdapter(IASTProvider.class).
						createTreeRepresentation((IModelElement)model, property, model, true, layout));
				//ModelChangeListener changeListener = 
				//		new ModelChangeListener(result, (PropertyTemplate)subTemplate, (IModelElement)model);
				//result.registerComponentListener(changeListener);
			} else if (subTemplate instanceof TerminalTemplate) {
				result.addNodeObject(((TerminalTemplate)subTemplate).getTerminalText());
			} else if (subTemplate instanceof WhitespaceTemplate) {
				result.addNodeObject(((WhitespaceTemplate)subTemplate).getSpace(layout));
			} else {
				throw new RuntimeException("assert");
			}
		}
		
		return result;
	}	

	public Object createCompositeModel(IModelElement parent, String property, ASTNode tree, boolean isComposite) {
		IModelElement result = null;
		boolean createModelForProperties = true;
		if (parent != null && isComposite) {
			ICommand createCommand = fModel.getCommandFactory().createChild(parent, fMetaModelElement, property);
			createCommand.execute();
			result = (IModelElement)createCommand.getResult().iterator().next();
			tree.setElement(new ModelASTElement(fElementTemplate, result));
		} else if (parent != null && !isComposite) {		
			createModelForProperties = false;			
		} else {
			result = fModel.createElement(fMetaModelElement);
			tree.setElement(new ModelASTElement(fElementTemplate, result));
		}		
		
		if (createModelForProperties) {
			for (Template subTemplate: fElementTemplate.getNestedTemplates()) {
				if (subTemplate instanceof PropertyTemplate) {
					String childProperty = ((PropertyTemplate)subTemplate).getProperty();
					subTemplate.getAdapter(IASTProvider.class).
							createCompositeModel(result, childProperty, ((ASTElementNode)tree).getNode(childProperty), true);
				}
			}
		}
		return result;
	}
	
	public Object createReferenceModel(IModelElement parent, String property, ASTNode tree, boolean isComposite, SemanticsContext context) {
		IModelElement result = null;
		boolean createModelForProperties = true;
		if (parent != null && isComposite) {
			result = ((ModelASTElement)tree.getElement()).getModelElement();
		} else if (parent != null && !isComposite) {		
			result = context.getIdentifierResolver().resolveIdentifier(fElementTemplate.getModel(),
					(ASTElementNode)tree, parent, context.getNewModel(),
					((ReferenceTemplate)tree.getParent().getElement().getTemplate()).getTypeModel(), property);
			if (result == null) {
				result = fElementTemplate.createMockObject();
			} else {
				createModelForProperties = false;
			}
			fModel.getCommandFactory().set(parent, property, result).execute();
			tree.setElement(new ModelASTElement(fElementTemplate, result));
		} else {
			result = ((ModelASTElement)tree.getElement()).getModelElement();
		}		
		
		if (createModelForProperties) {
			for (Template subTemplate: fElementTemplate.getNestedTemplates()) {
				if (subTemplate instanceof PropertyTemplate) {
					String childProperty = ((PropertyTemplate)subTemplate).getProperty();
					subTemplate.getAdapter(IASTProvider.class).
							createReferenceModel(result, childProperty, ((ASTElementNode)tree).getNode(childProperty), true, context);
				}
			}
		}
		return result;
	}

	public void check(ASTElementNode representation, SemanticsContext context) {	
		IModelElement modelElement = ((ModelASTElement)representation.getElement()).getModelElement();
		if (modelElement instanceof InternalModelElement) {			
			context.getAnnotationModelProvider().addAnnotation(new ErrorAnnotation(),
					new Position(representation.getAbsoluteOffset(0), representation.getLength()));
		} else {
			context.addModelElementOccurence(modelElement, 
					new Position(representation.getAbsoluteOffset(0), representation.getLength()));
		}
		
		for (Template subTemaplte: fElementTemplate.getNestedTemplates()) {
			if (subTemaplte instanceof PropertyTemplate) {
				Object value = representation.getNode(((PropertyTemplate)subTemaplte).getProperty());
				if (value instanceof ASTElementNode) {
					subTemaplte.getAdapter(ISemanticProvider.class).check((ASTElementNode)value, context);
				}
			}
		}
	}

	class ModelChangeListener implements IModelChangeListener, IDisposable {
		private final ASTElementNode fRepresentation;
		private final PropertyTemplate fTemplate;
		private final IModelElement fModel;				
		
		public ModelChangeListener(final ASTElementNode representation, final PropertyTemplate template, final IModelElement model) {
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
				fRepresentation.changeNodeObject(property, fTemplate.getAdapter(IASTProvider.class).
						createTreeRepresentation(null, property, fModel, true, null)); // TODO layout
			}
		}		
	}
}
