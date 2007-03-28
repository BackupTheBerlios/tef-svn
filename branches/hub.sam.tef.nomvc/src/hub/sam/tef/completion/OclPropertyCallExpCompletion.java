package hub.sam.tef.completion;

import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.treerepresentation.ASTElementNode;

import java.util.Collection;

public class OclPropertyCallExpCompletion extends SingleReductionCompletion {

	@Override
	public Rule getRule() {
		return new Rule( new String[] { "PropertyCallExp", "OCLExpression", "'.'" });
	}

	@Override
	public String[] getRulePrefix() {
		return new String[] { "OCLExpression", "'.'" };
	}

	public Collection<CompletionContextInformation> createProposals(ASTElementNode completionNode, CompletionContext context) {
		return EMFCompletions.createProposals("EStructuralFeature", context);
	}

	
}
