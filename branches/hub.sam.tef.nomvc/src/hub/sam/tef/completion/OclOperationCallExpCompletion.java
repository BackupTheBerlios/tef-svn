package hub.sam.tef.completion;

import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.treerepresentation.ASTElementNode;

import java.util.Collection;

public class OclOperationCallExpCompletion extends SingleReductionCompletion {

	@Override
	public Rule getRule() {
		return new Rule( new String[] { "OperationCallExp", "OCLExpression", "'.'" });
	}

	@Override
	public String[] getRulePrefix() {
		return new String[] { "OCLExpression", "'.'" };
	}

	public Collection<TEFCompletionProposal> createProposals(ASTElementNode completionNode, CompletionContext context) {
		return EMFCompletions.createProposals("EOperation", context);
	}

	
}
