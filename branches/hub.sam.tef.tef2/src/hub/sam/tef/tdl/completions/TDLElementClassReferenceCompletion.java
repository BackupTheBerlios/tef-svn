package hub.sam.tef.tdl.completions;

import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.completion.CompletionContext;
import hub.sam.tef.completion.SingleReductionCompletion;
import hub.sam.tef.completion.TEFCompletionProposal;
import hub.sam.tef.emf.EMFCompletions;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.reconciliation.treerepresentation.ASTElementNode;

import java.util.Collection;
import java.util.Vector;

public class TDLElementClassReferenceCompletion extends SingleReductionCompletion {
	
	@Override
	public Rule getRule() {
		return new Rule( new String[] { "TDLElementTemplate", "'element'", "`identifier`", "'for'" });
	}

	@Override
	public String[] getRulePrefix() {
		return new String[] { "'element'", "`identifier`", "'for'" };
	}

	public Collection<TEFCompletionProposal> createProposals(
			ASTElementNode completionNode, CompletionContext context) {
		return EMFCompletions.createProposals("EClass", "name", context);		
	}
	
}
