package hub.sam.tef.parse;

import fri.patterns.interpreter.parsergenerator.Semantic;
import fri.patterns.interpreter.parsergenerator.Token.Range;
import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.treerepresentation.IndexTreeRepresentationSelector;
import hub.sam.tef.treerepresentation.SyntaxTreeContent;
import hub.sam.tef.treerepresentation.TreeRepresentation;
import hub.sam.util.strings.Changes;
import hub.sam.util.trees.IChildSelector;
import hub.sam.util.trees.TreeIterator;

import java.util.List;

/**
 * This semantics builds a new AST {@link TextBasedUpdatedAST}. Thereby it puts references to the nodes
 * of an old existing AST into the new AST. When a node of the new AST was created in the
 * same way then in the old AST, it gets an reference to the according old AST node. A node
 * is created in the same way then the old AST, when (1) all it children either represent old input (terminals)
 * or nodes that already reference a node in the old AST (non-terminals). And (2) the according old
 * nodes reduced to the same symbol then the new node.
 */
public class UpdateTreeSemantic implements Semantic {

	private final TreeRepresentation fOldASTRootNode;
	private final Changes fChanges;
	private TreeRepresentation result;
	private final ParserInterface fParserInterface;
	
	public UpdateTreeSemantic(TreeRepresentation tree, Changes changes, ParserInterface parserInterface) {		
		this.fOldASTRootNode = tree;
		this.fChanges = changes;
		this.fParserInterface = parserInterface;
	}
	
	public Object doSemantic(Rule rule, List parseResults, List<Range> resultRanges) {
		if (parseResults.size() == 0) {
			throw new RuntimeException("assert");
		}
		int i = 0;
		boolean allOldParseResults = true;
		TreeRepresentation result = new TreeRepresentation(new SyntaxTreeContent(rule, 
				fParserInterface.getTemplateForNonTerminal(rule.getNonterminal())));
		for(Object parseResult: parseResults) {
			if (!isOldParseResult(parseResult, resultRanges.get(i))) {
				allOldParseResults = false;				
			} 
			result.addContent(parseResult); // TODO whitespaces			
		}
		
		if (allOldParseResults) {
			// check whether old results have same parent
			TreeRepresentation commonParent = null;
			i = 0;
			loop: for (Object parseResult: parseResults) {				
				TreeRepresentation currentParseResultParent = null;
				if (parseResult instanceof TreeRepresentation) {
					currentParseResultParent = ((TreeRepresentation)parseResult).getReferencedOldTreeNode().getParent();
				} else {					
					currentParseResultParent = findOldASTNode((String)parseResult, resultRanges.get(i));
					if (currentParseResultParent == null) {
						throw new RuntimeException("assert");
					}					
				}
				if (i == 0) {
					commonParent = currentParseResultParent;
				} else {
					if (!commonParent.equals(currentParseResultParent)) {
						// different reduce then in the old parse
						commonParent = null;
						break loop;
					}
				}
				i++;				
			}
			
			if (commonParent != null && commonParent.getElement().getSymbol().equals(rule.getNonterminal())) {
				// the old parent can be preserved
				result.setReferenceToOldTreeNode(commonParent);
			}	else {
				throw new RuntimeException("assert?");
			}
		}
		
		this.result = result;
		return result;
	}

	private boolean isOldParseResult(Object parseResult, Range range) {
		if (parseResult instanceof String) {
			return isOldInput(range.start.offset, range.end.offset);
		} else if (parseResult instanceof TreeRepresentation) {
			return ((TreeRepresentation)parseResult).referencesOldTreeNode();
		} else {
			throw new RuntimeException("assert");
		}
	}

	private TreeRepresentation findOldASTNode(String input, Range range) {
		int rangeStart = fChanges.getIndexBeforeChanges(range.start.offset);
		int rangeEnd = fChanges.getIndexBeforeChanges(range.end.offset) ;
		IChildSelector<TreeRepresentation> selector = new IndexTreeRepresentationSelector(rangeStart, rangeEnd-rangeStart);
		
		TreeRepresentation result = TreeIterator.select(selector, fOldASTRootNode);
		if (result == null) {
			return null;
		}
		int absolutOffset = result.getAbsoluteOffset(0);
		int relativeStart = rangeStart - absolutOffset;
		int relativeEnd = rangeEnd - absolutOffset;
		if (relativeStart >= 0 && relativeEnd >= 0 && relativeEnd <= result.getLength()) {
			if (result.getContent().substring(relativeStart, relativeEnd).equals(input)) {
				if (Rule.isTerminal(result.getElement().getSymbol())) {
					return result.getParent();
				} else {
					return result;
				}
			} else {
				return null;
			}
		}		
		return null;
	}
	
	private boolean isOldInput(int start, int end) {
		int startIndexInOldContent = fChanges.getIndexBeforeChanges(start);
		int endIndexInOldContent = fChanges.getIndexBeforeChanges(end);
		return startIndexInOldContent != -1 && endIndexInOldContent != -1;
	}
	
	public TreeRepresentation getCurrentResult() {
		return result;
	}
}
