package hub.sam.tef.tdl.completions;

import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.completion.CompletionContext;
import hub.sam.tef.completion.SingleReductionCompletion;
import hub.sam.tef.completion.TEFCompletionProposal;
import hub.sam.tef.emf.EMFCompletions;
import hub.sam.tef.reconciliation.treerepresentation.ASTElementNode;

import java.util.Collection;

public class TDLTemplateReferenceCompletion extends SingleReductionCompletion {

	@Override
	public Rule getRule() {
		return new Rule( new String[] { "TemplateRef", "'@'" });
	}

	@Override
	public String[] getRulePrefix() {
		return new String[] { "'@'" };
	}

	public Collection<TEFCompletionProposal> createProposals(ASTElementNode completionNode, CompletionContext context) {
		return EMFCompletions.createProposals("TDLTemplate", "name", context);
	}

}
