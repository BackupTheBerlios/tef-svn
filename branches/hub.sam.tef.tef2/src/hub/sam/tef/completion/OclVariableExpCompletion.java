package hub.sam.tef.completion;

import hub.sam.tef.treerepresentation.ASTElementNode;

import java.util.Collection;

import fri.patterns.interpreter.parsergenerator.syntax.Rule;

public class OclVariableExpCompletion extends EmptyReductionCompletion {

	@Override
	public Rule getRule() {
		return new Rule(new String[] { "VariableExp" });
	}

	public Collection<TEFCompletionProposal> createProposals(ASTElementNode completionNode, CompletionContext context) {
		return EMFCompletions.createProposals("Variable", context);
	}
}
