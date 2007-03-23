package hub.sam.tef.templates;

import hub.sam.tef.models.ICollection;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.parse.ISemanticProvider;
import hub.sam.tef.templates.adaptors.ISyntaxProvider;
import hub.sam.tef.templates.adaptors.IASTProvider;
import hub.sam.tef.treerepresentation.ModelASTElement;
import hub.sam.tef.treerepresentation.SemanticsContext;
import hub.sam.tef.treerepresentation.ASTElementNode;
import hub.sam.tef.treerepresentation.ASTNode;

import java.util.List;
import java.util.Vector;

public class SequenceTemplateSemantics implements ISyntaxProvider, IASTProvider, ISemanticProvider {

	private final SequenceTemplate fTemplate;
		
	protected SequenceTemplateSemantics(final SequenceTemplate template) {
		super();
		fTemplate = template;
	}

	public String getNonTerminal() {
		return fTemplate.getValueTemplate().getAdapter(ISyntaxProvider.class).getNonTerminal() + "_sequence";
	}

	public String[][] getRules() {
		ISyntaxProvider valueSyntaxProvider = fTemplate.getValueTemplate().getAdapter(ISyntaxProvider.class);
		if (fTemplate.fSeparator != null) {
			if (fTemplate.fSeparateLast) {
				return new String[][] {
						new String[] { getNonTerminal(), "'" + fTemplate.fSeparator + "'" },
						new String[] { getNonTerminal(), getNonTerminal(), "'" +fTemplate.fSeparator + "'", valueSyntaxProvider.getNonTerminal() } 
				};
			} else {
				return new String[][] {
						new String[] { getNonTerminal(), valueSyntaxProvider.getNonTerminal() },
						new String[] { getNonTerminal(), getNonTerminal(), "'" + fTemplate.fSeparator + "'", valueSyntaxProvider.getNonTerminal() } 
				};
			}
		} else {
			return new String[][] {
					new String[] { getNonTerminal(), valueSyntaxProvider.getNonTerminal() },
					new String[] { getNonTerminal(), getNonTerminal(),  valueSyntaxProvider.getNonTerminal() } 
			};
		}
	}	
	
	private static final String valueKey = "VALUE_KEY";
	private static final String tailKey = "TAIL_KEY";
	
	public ASTNode createTreeRepresentation(IModelElement owner, String property, Object model, boolean isComposite) {
		ICollection elements = (ICollection)((IModelElement)model).getValue(property);
		int i = 0;		
		boolean first = true;
		ASTElementNode result = null;
		ASTElementNode parentNode = null;
		for (Object element: elements) {			
			ASTElementNode treeRepresentation = new ASTElementNode(
					new ModelASTElement(fTemplate, (IModelElement)model));
			
			if (first) {
				result = treeRepresentation;
				first = false;
			} else {
				parentNode.addNodeObject(tailKey, treeRepresentation);
			}
			
			treeRepresentation.addNodeObject(valueKey, fTemplate.getValueTemplate().getAdapter(IASTProvider.class).
					createTreeRepresentation(owner, null, (IModelElement)element, isComposite));
						
			if (fTemplate.fSeparator != null && i+1 < elements.size()) {
				treeRepresentation.addNodeObject(fTemplate.fSeparator);
			}			
			i++;						
			parentNode = treeRepresentation;
		}
		if (fTemplate.fSeparateLast && fTemplate.fSeparator != null) {			
			ASTElementNode treeRepresentation = 
					new ASTElementNode(new ModelASTElement(fTemplate, (IModelElement)model));										
			treeRepresentation.addNodeObject(fTemplate.fSeparator);			
			parentNode.addNodeObject(tailKey, treeRepresentation);
		}
		return result;		
	}		
	
	public Object createCompositeModel(IModelElement owner, String property, ASTNode tree, boolean isComposite) {		
		ASTElementNode nextTree = getTailNode((ASTElementNode)tree);
		if (nextTree != null) {
			nextTree.setElement(new ModelASTElement(fTemplate, owner));
			createCompositeModel(owner, property, nextTree, isComposite);
		}
		fTemplate.getValueTemplate().getAdapter(IASTProvider.class).
		createCompositeModel(owner, property, getValueNode((ASTElementNode)tree), true);
		return null;
	}
	
	

	public Object createReferenceModel(IModelElement owner, String property, ASTNode tree, boolean isComposite, SemanticsContext context) {
		ASTElementNode nextTree = getTailNode((ASTElementNode)tree);
		if (nextTree != null) {		
			createReferenceModel(owner, property, nextTree, isComposite, context);
		}
		fTemplate.getValueTemplate().getAdapter(IASTProvider.class).
				createReferenceModel(owner, property, getValueNode((ASTElementNode)tree), true, context);
		return null;
	}

	private ASTElementNode getTailNode(ASTElementNode ast) {
		for (ASTElementNode child: ast.getChildNodes()) {
			if (child.getElement().getSymbol().equals(ast.getElement().getSymbol())) {
				return child;
			}
		}
		return null;
	}

	private ASTElementNode getValueNode(ASTElementNode ast) {
		for (ASTElementNode child: ast.getChildNodes()) {
			if (!child.getElement().getSymbol().equals(ast.getElement().getSymbol())) {
				return child;
			}
		}
		// primitive value TODO
		throw new RuntimeException("assert");
	}
	
	private List<ASTElementNode> collectAllValueNodes(ASTElementNode head, List<ASTElementNode> nodes) {		
		ASTElementNode tail = getTailNode(head);
		if (tail != null) {
			collectAllValueNodes(tail, nodes);
		}
		nodes.add(getValueNode(head));
		return nodes;
	}

	
	public void check(ASTElementNode representation, SemanticsContext context) {	
		List<ASTElementNode> allValueNodes = collectAllValueNodes((ASTElementNode)representation, new Vector<ASTElementNode>());				
		for (ASTElementNode valueNode: allValueNodes) {
			fTemplate.getValueTemplate().getAdapter(ISemanticProvider.class).check(valueNode, context);
		}
	}	
}
