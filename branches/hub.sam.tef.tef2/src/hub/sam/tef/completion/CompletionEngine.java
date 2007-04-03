package hub.sam.tef.completion;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import hub.sam.tef.parse.ParserInterface;
import hub.sam.tef.parse.UpdateTreeSemantic;
import hub.sam.tef.treerepresentation.ASTElementNode;

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
			Collection<TEFCompletionProposal> proposals = new Vector<TEFCompletionProposal>();
			if (completionOk) {
				if (parser.hasValidStack()) {
					proposals.addAll(completion.createProposals((ASTElementNode)parser.getParseResult(0), context));
				}
			}
	
			return proposals;
		} catch (Exception ex) {
			System.out.print("COMPLETION FAILED: " + ex.getMessage());
			return Collections.EMPTY_LIST;
		}
	}
}
