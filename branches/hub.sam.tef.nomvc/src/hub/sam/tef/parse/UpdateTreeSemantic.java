package hub.sam.tef.parse;

import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.treerepresentation.SyntaxTreeContent;
import hub.sam.tef.treerepresentation.TreeRepresentation;

import java.util.List;

import fri.patterns.interpreter.parsergenerator.Semantic;
import fri.patterns.interpreter.parsergenerator.Token.Range;
import fri.patterns.interpreter.parsergenerator.syntax.Rule;

/**
 * This semantics builds a new AST {@link TextBasedUpdatedAST}. Thereby it puts references to the nodes
 * of an old existing AST into the new AST. When a node of the new AST was created in the
 * same way then in the old AST, it gets an reference to the according old AST node. A node
 * is created in the same way then the old AST, when (1) all it children either represent old input (terminals)
 * or nodes that already reference a node in the old AST (non-terminals). And (2) the according old
 * nodes reduced to the same symbol then the new node.
 */
public class UpdateTreeSemantic implements Semantic {

	private TreeRepresentation result;
	private final ParserInterface fParserInterface;
	
	public UpdateTreeSemantic(ParserInterface parserInterface) {		
		this.fParserInterface = parserInterface;
	}
	
	public Object doSemantic(Rule rule, List parseResults, List<Range> resultRanges) {
		if (parseResults.size() == 0) {
			throw new RuntimeException("assert");
		}
		int i = 0;
		Template template = fParserInterface.getTemplateForNonTerminal(rule.getNonterminal()); 
		TreeRepresentation result = new TreeRepresentation(new SyntaxTreeContent(rule, template));
				
		for(Object parseResult: parseResults) {
			 // TODO whitespaces
			if (template instanceof ElementTemplate) {
				String property = ((ElementTemplate)template).getPropertyForRuleAndPosition(rule, i);
				if (property != null) {
					result.addContent(property, parseResult);
				} else {
					// its a terminal
					result.addContent(parseResult);
				}
			} else {			
				result.addContent(parseResult);
			}
			i++;
		}	
		
		this.result = result;
		return result;
	}
	
	public TreeRepresentation getCurrentResult() {
		return result;
	}
}
