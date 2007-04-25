package hub.sam.tef.completion;

import hub.sam.tef.syntax.ParserInterface;
import hub.sam.tef.syntax.UpdateTreeSemantic;
import hub.sam.tef.treerepresentation.ASTElementNode;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

public class CompletionEngine {

	private final ParserInterface fParserInterface;
	
	public CompletionEngine(final ParserInterface parser) {
		super();
		fParserInterface = parser;
	}

	public Collection<TEFCompletionProposal> collectCompletionsFromCompletionComputer(ICompletionComputer completion, 
			CompletionContext context) {
		try {
			UpdateTreeSemantic semantic = new UpdateTreeSemantic(fParserInterface, context.getContent());		
					
			CompletionParser parser = (CompletionParser)fParserInterface.getParser();
			parser.setCompletionOffset(context.getCompletionOffset());
			fParserInterface.parse(context.getContent(), semantic);
						
			context.setIdentifierPrefix(parser.getIdentifierPrefix());
			
			boolean completionOk = completion.reduceParseStack(parser);
			Collection<TEFCompletionProposal> proposals = new HashSet<TEFCompletionProposal>();
			if (completionOk) {
				if (parser.hasValidStack()) {
					proposals.addAll(completion.createProposals((ASTElementNode)parser.getParseResult(0), context));
				}
			}
	
			List<TEFCompletionProposal> sortedProposals = new Vector<TEFCompletionProposal>();
			sortedProposals.addAll(proposals);
			Collections.sort(sortedProposals, new Comparator<TEFCompletionProposal>() {
				public int compare(TEFCompletionProposal o1, TEFCompletionProposal o2) {
					return o1.getDisplayString().compareTo(o2.getDisplayString());
				}				
			});
			return sortedProposals;
		} catch (Exception ex) {
			System.out.print("COMPLETION FAILED: " + ex.getMessage());
			return Collections.EMPTY_LIST;
		}
	}
}
