package hub.sam.tef.templates;

import hub.sam.tef.models.ICollection;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.parse.ISemanticProvider;
import hub.sam.tef.treerepresentation.ISyntaxProvider;
import hub.sam.tef.treerepresentation.ITreeRepresentationProvider;
import hub.sam.tef.treerepresentation.ModelTreeContents;
import hub.sam.tef.treerepresentation.SemanticsContext;
import hub.sam.tef.treerepresentation.TreeRepresentation;
import hub.sam.tef.treerepresentation.TreeRepresentationLeaf;

import java.util.List;
import java.util.Vector;

public class CollectionTemplateSemantics implements ISyntaxProvider, ITreeRepresentationProvider, ISemanticProvider {

	private final CollectionTemplate fTemplate;
		
	protected CollectionTemplateSemantics(final CollectionTemplate template) {
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
	
	public TreeRepresentationLeaf createTreeRepresentation(IModelElement owner, String property, Object model, boolean isComposite) {
		ICollection elements = (ICollection)((IModelElement)model).getValue(property);
		int i = 0;		
		boolean first = true;
		TreeRepresentation result = null;
		TreeRepresentation parentNode = null;
		for (Object element: elements) {			
			TreeRepresentation treeRepresentation = new TreeRepresentation(
					new ModelTreeContents(fTemplate, (IModelElement)model));
			
			if (first) {
				result = treeRepresentation;
				first = false;
			} else {
				parentNode.addContent(tailKey, treeRepresentation);
			}
			
			treeRepresentation.addContent(valueKey, fTemplate.getValueTemplate().getAdapter(ITreeRepresentationProvider.class).
					createTreeRepresentation(owner, null, (IModelElement)element, isComposite));
						
			if (fTemplate.fSeparator != null && i+1 < elements.size()) {
				treeRepresentation.addContent(fTemplate.fSeparator);
			}			
			i++;						
			parentNode = treeRepresentation;
		}
		if (fTemplate.fSeparateLast && fTemplate.fSeparator != null) {			
			TreeRepresentation treeRepresentation = 
					new TreeRepresentation(new ModelTreeContents(fTemplate, (IModelElement)model));										
			treeRepresentation.addContent(fTemplate.fSeparator);			
			parentNode.addContent(tailKey, treeRepresentation);
		}
		return result;		
	}		
	
	public Object createCompositeModel(IModelElement owner, String property, TreeRepresentationLeaf tree, boolean isComposite) {		
		TreeRepresentation nextTree = getTailNode((TreeRepresentation)tree);
		if (nextTree != null) {
			nextTree.setElement(new ModelTreeContents(fTemplate, owner));
			createCompositeModel(owner, property, nextTree, isComposite);
		}
		fTemplate.getValueTemplate().getAdapter(ITreeRepresentationProvider.class).
		createCompositeModel(owner, property, getValueNode((TreeRepresentation)tree), true);
		return null;
	}
	
	

	public Object createReferenceModel(IModelElement owner, String property, TreeRepresentationLeaf tree, boolean isComposite, SemanticsContext context) {
		TreeRepresentation nextTree = getTailNode((TreeRepresentation)tree);
		if (nextTree != null) {		
			createReferenceModel(owner, property, nextTree, isComposite, context);
		}
		fTemplate.getValueTemplate().getAdapter(ITreeRepresentationProvider.class).
				createReferenceModel(owner, property, getValueNode((TreeRepresentation)tree), true, context);
		return null;
	}

	private TreeRepresentation getTailNode(TreeRepresentation ast) {
		for (TreeRepresentation child: ast.getChildNodes()) {
			if (child.getElement().getSymbol().equals(ast.getElement().getSymbol())) {
				return child;
			}
		}
		return null;
	}

	private TreeRepresentation getValueNode(TreeRepresentation ast) {
		for (TreeRepresentation child: ast.getChildNodes()) {
			if (!child.getElement().getSymbol().equals(ast.getElement().getSymbol())) {
				return child;
			}
		}
		// primitive value TODO
		throw new RuntimeException("assert");
	}
	
	private List<TreeRepresentation> collectAllValueNodes(TreeRepresentation head, List<TreeRepresentation> nodes) {		
		TreeRepresentation tail = getTailNode(head);
		if (tail != null) {
			collectAllValueNodes(tail, nodes);
		}
		nodes.add(getValueNode(head));
		return nodes;
	}

	
	public void check(TreeRepresentation representation, SemanticsContext context) {	
		List<TreeRepresentation> allValueNodes = collectAllValueNodes((TreeRepresentation)representation, new Vector<TreeRepresentation>());				
		for (TreeRepresentation valueNode: allValueNodes) {
			fTemplate.getValueTemplate().getAdapter(ISemanticProvider.class).check(valueNode, context);
		}
	}	
}
