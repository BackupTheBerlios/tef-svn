package hub.sam.tef.templates;

import hub.sam.tef.models.ICollection;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.parse.AST;
import hub.sam.tef.parse.IASTBasedModelUpdater;
import hub.sam.tef.parse.ISyntaxProvider;
import hub.sam.tef.parse.ModelUpdateConfiguration;
import hub.sam.tef.parse.TextBasedAST;
import hub.sam.tef.parse.TextBasedUpdatedAST;
import hub.sam.tef.treerepresentation.ITreeRepresentationProvider;
import hub.sam.tef.treerepresentation.ModelTreeContents;
import hub.sam.tef.treerepresentation.SyntaxTreeContent;
import hub.sam.tef.treerepresentation.TreeRepresentation;
import hub.sam.tef.views.CompoundText;
import hub.sam.tef.views.Text;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

public class CollectionTemplateSemantics implements ISyntaxProvider, IASTBasedModelUpdater, ITreeRepresentationProvider {

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
	
	public TextBasedAST createAST(TextBasedAST parent, IModelElement model, Text text) {
		List<Text> elements = new Vector<Text>();		
		for(Text elementText: ((CompoundText)((CompoundText)text).getTexts().get(0)).getTexts()) {
			if (elementText instanceof CompoundText) {
				for (Text subText: ((CompoundText)elementText).getTexts()) {
					if (subText.getElement(Template.class) != null) {
						elements.add(subText);
					}
				}
			}
		}
		
		if (elements.size() == 0) {
			return null;
		}
		for (int i = 0; i < elements.size(); i++) {
			TextBasedAST valueNode = new TextBasedAST(elements.get(i));			
			TextBasedAST sequenceNode = new TextBasedAST(text);
			sequenceNode.addChild(valueNode);
			parent.addChild(sequenceNode);
			parent = sequenceNode;						
		}
		return null;
	}
	
	public Object createTreeRepresentation(String property, Object model) {
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
				parentNode.addContent(treeRepresentation);
			}
			
			treeRepresentation.addContent(fTemplate.getValueTemplate().getAdapter(ITreeRepresentationProvider.class).
					createTreeRepresentation(null, (IModelElement)element));
						
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
			parentNode.addContent(treeRepresentation);
		}
		return result;		
	}

	public void executeModelUpdate(ModelUpdateConfiguration configuration) {		
		List<TreeRepresentation> allValueNodes = collectAllValueNodes(configuration.getAst(), new Vector<TreeRepresentation>());
		if (configuration.isHasOldParent()) {
			List<TreeRepresentation> allOldValueNodes = 
				collectAllValueNodes((TreeRepresentation)configuration.getAst().getParent().getReferencedOldTreeNode().
						getContent(configuration.getProperty()), new Vector<TreeRepresentation>());								
			
			int actualPosition = 0;
			int actualPositionInOldValue = 0;
			for(TreeRepresentation valueNode: allValueNodes) {
				if (!valueNode.referencesOldTreeNode()) {
					// its a new value insert at position
					fTemplate.getValueTemplate().getAdapter(IASTBasedModelUpdater.class).
							executeModelUpdate(configuration.createCollectionConfiguration(valueNode, actualPosition));
					actualPosition++;
				} else {
					int positionInOldValues = allOldValueNodes.indexOf(valueNode.getReferencedOldTreeNode());
					if (positionInOldValues != -1) {
						// remove values from actionalPosition(incl) to actualPosition + positionInOldValue - actutualPositionInOldValues(excl)
						for(int i = actualPositionInOldValue; i < positionInOldValues - actualPositionInOldValue; i++) {
							fTemplate.getModel().getCommandFactory().remove(configuration.getOwner(), 
									configuration.getProperty(), allOldValueNodes.get(i)).execute();
						}
						actualPosition++;
						actualPositionInOldValue = positionInOldValues + 1;
					} else {
						// treat as new value, insert at position
						fTemplate.getValueTemplate().getAdapter(IASTBasedModelUpdater.class).
						executeModelUpdate(configuration.createCollectionConfiguration(valueNode, actualPosition));
						actualPosition++;
					}
				}
			}
			// delete everything behind actualPositionInOldValue
			for(int i = actualPositionInOldValue; i < allOldValueNodes.size(); i++) {
				fTemplate.getModel().getCommandFactory().remove(configuration.getOwner(), 
						configuration.getProperty(), ((ModelTreeContents)allOldValueNodes.get(i).getElement()).getModelElement()).execute();
			}
		} else {
			for(TreeRepresentation valueNode: allValueNodes) {
				fTemplate.getValueTemplate().getAdapter(IASTBasedModelUpdater.class).
						executeModelUpdate(configuration.createCollectionConfiguration(valueNode));	
			}
		}					
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
		nodes.add(getValueNode(head));
		TreeRepresentation tail = getTailNode(head);
		if (tail != null) {
			collectAllValueNodes(tail, nodes);
		}
		return nodes;
	}
}
