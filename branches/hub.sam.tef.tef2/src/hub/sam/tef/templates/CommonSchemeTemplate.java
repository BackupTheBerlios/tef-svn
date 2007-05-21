package hub.sam.tef.templates;

import hub.sam.tef.annotations.ISemanticProvider;
import hub.sam.tef.annotations.SemanticsContext;
import hub.sam.tef.models.ICollection;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.models.IType;
import hub.sam.tef.reconciliation.syntax.AbstractLayoutManager;
import hub.sam.tef.reconciliation.syntax.ISyntaxProvider;
import hub.sam.tef.reconciliation.treerepresentation.ASTElementNode;
import hub.sam.tef.reconciliation.treerepresentation.ASTNode;
import hub.sam.tef.reconciliation.treerepresentation.IASTProvider;
import hub.sam.tef.reconciliation.treerepresentation.ModelASTElement;

public abstract class CommonSchemeTemplate extends ValueTemplate<IModelElement> {

	private ElementTemplate schemeTemplate = null;
	private ElementTemplate[] alternativeTemplates = null;
	
	public CommonSchemeTemplate(Template template, IType type) {
		super(template, type);
	}
	
	protected abstract ElementTemplate createSchemeTemplate();	
	protected abstract ElementTemplate[] createAlternatives();		
	protected abstract void adoptTree(ASTElementNode tree, ElementTemplate alternative);
	protected abstract ElementTemplate selectAlternative(IModelElement owner, String property, ASTNode tree, boolean isComposite, SemanticsContext context);
	
	private final ElementTemplate getSchemeTemplate() {
		if (schemeTemplate == null) {
			schemeTemplate = createSchemeTemplate();
		}
		return schemeTemplate;
	}
	
	protected final ElementTemplate[] getAlternativeTemplates() {
		if (alternativeTemplates == null) {
			alternativeTemplates = createAlternatives();		
		}
		return alternativeTemplates;
	}			

	@Override
	public Template[] getNestedTemplates() {
		return new Template[] { getSchemeTemplate() };
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (IASTProvider.class == adapter || ISemanticProvider.class == adapter) {
			return (T)new ASTAndSemanticsProvider();
		} else if (ISyntaxProvider.class == adapter) {
			return (T)new SyntaxProvider();
		} else {
			return super.getAdapter(adapter);
		}
	}
	
	class SyntaxProvider implements ISyntaxProvider {
		public String getNonTerminal() {
			return getSchemeTemplate().getAdapter(ISyntaxProvider.class).getNonTerminal();
		}

		public String[][] getRules() {
			return getSchemeTemplate().getAdapter(ISyntaxProvider.class).getRules();
		}		
	}
	
	class ASTAndSemanticsProvider implements IASTProvider, ISemanticProvider {
		public Object createCompositeModel(IModelElement owner, String property, ASTNode tree, boolean isComposite) {
			return getSchemeTemplate().getAdapter(ISchemeASTProvider.class).createCompositeModel(owner, property, tree, isComposite);
		}

		public Object createReferenceModel(IModelElement owner, String property, ASTNode tree, boolean isComposite, SemanticsContext context) {
			IModelElement schemeElement = ((ModelASTElement)tree.getElement()).getModelElement();
			ElementTemplate choosenAlternative = selectAlternative(owner, property, tree, isComposite, context);
			
			if (isComposite) {
				getModel().getCommandFactory().delete(schemeElement).execute();
				adoptTree((ASTElementNode)tree, choosenAlternative);
				choosenAlternative.getAdapter(IASTProvider.class).
						createCompositeModel(owner, property, tree, isComposite);
		
				//getModel().getCommandFactory().replace(owner, property, schemeElement, newValue).execute();	
			} else {
				throw new RuntimeException("not implemented.");
			}
			
			return choosenAlternative.getAdapter(IASTProvider.class).createReferenceModel(owner, property, tree, isComposite, context);			
		}

		public ASTNode createTreeRepresentation(IModelElement owner, String property, Object model, boolean isComposite, AbstractLayoutManager layout) {
			if (model instanceof ICollection) {
				model = ((ICollection)model).iterator().next();
			}
			for (ValueTemplate alternative: getAlternativeTemplates()) {
				if (alternative.isTemplateFor(model)) {
					return alternative.getAdapter(IASTProvider.class).createTreeRepresentation(owner, 
							property, model, true, layout);																		
				}
			}
			throw new RuntimeException("assert");
		}
		
		public void check(ASTElementNode representation, SemanticsContext context) {
			representation.getElement().getTemplate().getAdapter(ISemanticProvider.class).check(representation, context);		
		}				
	}
}
