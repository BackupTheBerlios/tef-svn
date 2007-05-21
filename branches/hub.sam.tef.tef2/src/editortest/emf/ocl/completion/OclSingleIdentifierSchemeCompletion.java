package editortest.emf.ocl.completion;

import hub.sam.tef.completion.CompletionContext;
import hub.sam.tef.completion.EmptyReductionCompletion;
import hub.sam.tef.completion.TEFCompletionProposal;
import hub.sam.tef.emf.EMFCompletions;
import hub.sam.tef.reconciliation.treerepresentation.ASTElementNode;

import java.util.Collection;

import fri.patterns.interpreter.parsergenerator.syntax.Rule;

public class OclSingleIdentifierSchemeCompletion extends EmptyReductionCompletion {

	@Override
	public Rule getRule() {
		return new Rule(new String[] { "SingleIdentifierScheme" });
	}

	public Collection<TEFCompletionProposal> createProposals(ASTElementNode completionNode, CompletionContext context) {
		// TODO completions for self properties and types
		return EMFCompletions.createProposals("Variable", context);
	}
}
